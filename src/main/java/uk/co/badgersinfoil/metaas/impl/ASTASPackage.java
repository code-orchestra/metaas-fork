/*
 * ASTASPackage.java
 * 
 * Copyright (c) 2006-2008 David Holroyd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.badgersinfoil.metaas.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.runtime.tree.Tree;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;


public class ASTASPackage extends ASTScriptElement implements ASPackage {

    public ASTASPackage(LinkedListTree ast) {
        super(ast);
    }

    public List<Statement> getInitStatementList() {
        List<Statement> statements = new ArrayList<Statement>();

        Tree packageBlock = ast.getFirstChildWithType(AS3Parser.BLOCK);
        if (packageBlock != null) {
            List<LinkedListTree> statementsAST = ASTUtils.findChildrenByType((LinkedListTree) packageBlock, AS3Parser.OUT_OF_FUNCTION_STMT);
            for (LinkedListTree statementAST : statementsAST) {
                final LinkedListTree child = statementAST.getFirstChild();
                if (child != null) {
                    statements.add(StatementBuilder.build(child));
                }
            }
        }

        return statements;
    }

    public List<ASIncludeDirective> getIncludeDirectives() {
        List<ASIncludeDirective> directives = new ArrayList<ASIncludeDirective>();

        Tree packageBlock = ast.getFirstChildWithType(AS3Parser.BLOCK);
        if (packageBlock != null) {
            LinkedListTree typeBlockAST = (LinkedListTree) packageBlock;
            for (LinkedListTree includeAST : ASTUtils.findChildrenByType(typeBlockAST, AS3Parser.INCLUDE_DIRECTIVE_AS3)) {
                directives.add(new ASTASIncludeDirective(includeAST));
            }
        }
        return directives;
    }

    public String getName() {
        LinkedListTree first = ast.getFirstChild();
        if (first.getType() == AS3Parser.IDENTIFIER) {
            return ASTUtils.identText(first);
        }
        return null;
    }

    public void setName(String name) {
        ASTIterator i = new ASTIterator(ast);
        LinkedListTree first = i.next();
        // null name given, so remove any existing name,
        if (name == null && first.getType() == AS3Parser.IDENTIFIER) {
            i.remove();
            return;
        }
        // otherwise, add or replace the name given,
        LinkedListTree newName = AS3FragmentParser.parseIdent(name);
        if (first.getType() == AS3Parser.IDENTIFIER) {
            i.replace(newName);
        } else {
            i.insertBeforeCurrent(newName);
            newName.appendToken(TokenBuilder.newSpace());
        }
    }

    public void addImport(String name) {
        LinkedListTree imp = AS3FragmentParser.parseImport(name);
        int pos = findNextImportInsertionPoint();
        ASTUtils.addChildWithIndentation(getPkgBlockNode(), pos, imp);
    }

    private int findNextImportInsertionPoint() {
        ASTIterator i = getPkgBlockIter();
        int index = 0;
        while (i.search(AS3Parser.IMPORT) != null) {
            index = i.getCurrentIndex() + 1;
        }
        return index;
    }

    public List<ASUseNamespaceStatement> getUseNamespaceDirectives() {
        ASTIterator i = getPkgBlockIter();
        LinkedListTree imp;
        List<ASUseNamespaceStatement> result = new ArrayList<ASUseNamespaceStatement>();
        while ((imp = i.search(AS3Parser.USE)) != null) {
            result.add(new ASTASUseNamespaceStatement(imp));
        }
        return result;
    }

    public List findImports() {
        ASTIterator i = getPkgBlockIter();
        LinkedListTree imp;
        List result = new ArrayList();
        while ((imp = i.search(AS3Parser.IMPORT)) != null) {
            result.add(importText(imp));
        }

        // RE-932
        ASType type = getType();
        if (type != null) {
            result.addAll(type.findImports());
        }

        return result;
    }

    private String importText(LinkedListTree imp) {
        return ASTUtils.identStarText(imp.getFirstChild());
    }

    public boolean removeImport(String name) {
        ASTIterator i = getPkgBlockIter();
        LinkedListTree imp;
        while ((imp = i.search(AS3Parser.IMPORT)) != null) {
            if (importText(imp).equals(name)) {
                i.remove();
                return true;
            }
        }
        return false;
    }

    public List<ScriptElement> getAllMembers() {
        LinkedListTree block = ASTUtils.findChildByType(ast, AS3Parser.BLOCK);
        List<ScriptElement> result = new ArrayList<ScriptElement>();

        for (int i = 0; i < block.getChildCount(); i++) {
            if (block.getChild(i).getType() == AS3Parser.CLASS_DEF) {
                result.add(new ASTASClassType((LinkedListTree) block.getChild(i)));
            } else if (block.getChild(i).getType() == AS3Parser.INTERFACE_DEF) {
                result.add(new ASTASInterfaceType((LinkedListTree) block.getChild(i)));
            } else if (block.getChild(i).getType() == AS3Parser.VAR_DEF) {
                result.add(new ASTASField((LinkedListTree) block.getChild(i)));
            } else if (block.getChild(i).getType() == AS3Parser.METHOD_DEF) {
                result.add(new ASTASMethod((LinkedListTree) block.getChild(i)));
            } else if (block.getChild(i).getType() == AS3Parser.NAMESPACE_DEF) {
                result.add(new ASTASNamespaceDeclaration((LinkedListTree) block.getChild(i)));
            }
        }

        return result;
    }

    public ASType getType() {
        LinkedListTree block = ASTUtils.findChildByType(ast, AS3Parser.BLOCK);
        LinkedListTree type = ASTUtils.findChildByType(block, AS3Parser.CLASS_DEF);
        if (type != null) {
            return new ASTASClassType(type);
        }
        type = ASTUtils.findChildByType(block, AS3Parser.INTERFACE_DEF);
        if (type != null) {
            return new ASTASInterfaceType(type);
        }
        return null;
    }

    public ASField getGlobalField() {
        LinkedListTree block = ASTUtils.findChildByType(ast, AS3Parser.BLOCK);
        LinkedListTree fieldAST = ASTUtils.findChildByType(block, AS3Parser.VAR_DEF);
        if (fieldAST != null) {
            return new ASTASField(fieldAST);
        }
        return null;
    }

    public ASMethod getGlobalFunction() {
        LinkedListTree block = ASTUtils.findChildByType(ast, AS3Parser.BLOCK);
        LinkedListTree function = ASTUtils.findChildByType(block, AS3Parser.METHOD_DEF);
        if (function != null) {
            return new ASTASMethod(function);
        }
        return null;
    }

    private LinkedListTree getPkgBlockNode() {
        return ASTUtils.findChildByType(ast, AS3Parser.BLOCK);
    }

    private ASTIterator getPkgBlockIter() {
        return new ASTIterator(getPkgBlockNode());
    }

    protected String clearComment(String comment) {
        String result = comment;
        Pattern p = Pattern.compile("^[\\s&&[^\\n\\r]]*(//)*[\\s&&[^\\n\\r]]*", Pattern.MULTILINE);
        Matcher m = p.matcher(result);
        result = m.replaceAll("");
        m.reset();
        p = Pattern.compile("/\\*([^*]([^*/]|[^*]/|\\*[^/])*)\\*/", Pattern.DOTALL);
        m = p.matcher(result);

        if (m.lookingAt())
        {
            result = m.replaceFirst(m.group(1));
            m.reset(result);
        }
        result = result.trim();
        if (result.isEmpty())
        {
            return null;
        }
        return result;
    }

    public List<Comment> getCommentsBefore() {
        return CommentUtils.getCommentBefore(this.getAST());
    }
}
