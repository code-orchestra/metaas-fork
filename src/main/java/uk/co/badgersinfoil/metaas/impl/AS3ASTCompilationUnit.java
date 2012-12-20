/*
 * AS3ASTCompilationUnit.java
 * 
 * Copyright (c) 2006 David Holroyd
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

import org.antlr.runtime.tree.Tree;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.ArrayList;
import java.util.List;


public class AS3ASTCompilationUnit extends ASTASCompilationUnit {

    public AS3ASTCompilationUnit(LinkedListTree ast) {
        super(ast);
    }

    public List<ScriptElement> getAllMembers() {
        ASPackage pkg = getPackage();
        if (pkg == null) {
            return null;
        }
        return pkg.getAllMembers();
    }

    public ASMethod getGlobalFunction() {
        ASPackage pkg = getPackage();
        if (pkg == null) {
            return null;
        }
        return pkg.getGlobalFunction();
    }

    public ASField getGlobalField() {
        ASPackage pkg = getPackage();
        if (pkg == null) {
            return null;
        }
        return pkg.getGlobalField();
    }

    public ASNamespaceDeclaration getGlobalNamespaceDeclaration() {
        ASTASPackage pkg = (ASTASPackage) getPackage();
        if (pkg == null) {
            return null;
        }

        Tree block = pkg.getAST().getFirstChildWithType(AS3Parser.BLOCK);
        if (block == null) {
            return null;
        }

        if (block instanceof LinkedListTree) {
            Tree nsTree = ((LinkedListTree) block).getFirstChildWithType(AS3Parser.NAMESPACE_DEF);
            if (nsTree == null || !(nsTree instanceof LinkedListTree)) {
                return null;
            }
            return new ASTASNamespaceDeclaration((LinkedListTree) nsTree);
        }

        return null;
    }

    public String getPackageName() {
        return getPackage().getName();
    }

    public void setPackageName(String pkgName) {
        getPackage().setName(pkgName);
    }

    public ASType getType() {
        ASPackage pkg = getPackage();
        if (pkg == null) {
            return null;
        }
        return pkg.getType();
    }

    public List<ASType> getOutOfPackageTypes() {
        List<ASType> types = new ArrayList<ASType>();

        for (LinkedListTree type : ASTUtils.findChildrenByType(ast, AS3Parser.CLASS_DEF)) {
            types.add(new ASTASClassType(type));
        }

        for (LinkedListTree type : ASTUtils.findChildrenByType(ast, AS3Parser.INTERFACE_DEF)) {
            types.add(new ASTASInterfaceType(type));
        }

        return types;
    }

    public List<ASField> getOutOfPackageFields() {
        List<ASField> fields = new ArrayList<ASField>();

        for (LinkedListTree fieldAST : ASTUtils.findChildrenByType(ast, AS3Parser.VAR_DEF)) {
            fields.addAll(new ASTASField(fieldAST).getSubFields());
        }

        return fields;
    }

    public List<ASMethod> getOutOfPackageFunctions() {
        List<ASMethod> functions = new ArrayList<ASMethod>();

        for (LinkedListTree functionAST : ASTUtils.findChildrenByType(ast, AS3Parser.METHOD_DEF)) {
            functions.add(new ASTASMethod(functionAST));
        }

        return functions;
    }

    public List<String> getOutOfPackageImports() {
        ASTIterator i = new ASTIterator(ast);
        LinkedListTree imp;
        List<String> result = new ArrayList<String>();
        while ((imp = i.search(AS3Parser.IMPORT)) != null) {
            result.add(importText(imp));
        }
        return result;
    }

    public List<Statement> getOutOfPackageStatements() {
        List<Statement> statements = new ArrayList<Statement>();

        List<LinkedListTree> statementsAST = ASTUtils.findChildrenByType(ast, AS3Parser.OUT_OF_FUNCTION_STMT);
        for (LinkedListTree statementAST : statementsAST) {
            final LinkedListTree child = statementAST.getFirstChild();
            if (child != null) {
                statements.add(StatementBuilder.build(child));
            }
        }

        return statements;
    }

    public List<ASNamespaceDeclaration> getOutOfPackageNamespaces() {
        List<ASNamespaceDeclaration> namespaces = new ArrayList<ASNamespaceDeclaration>();

        List<LinkedListTree> namespacesAST = ASTUtils.findChildrenByType(ast, AS3Parser.NAMESPACE_DEF);
        for (LinkedListTree namespaceAST : namespacesAST) {
            final LinkedListTree child = namespaceAST.getFirstChild();
            if (child != null) {
                namespaces.add(new ASTASNamespaceDeclaration(namespaceAST));
            }
        }

        return namespaces;
    }

    private String importText(LinkedListTree imp) {
        return ASTUtils.identStarText(imp.getFirstChild());
    }

    private LinkedListTree getPkgNode() {
        return ASTUtils.findChildByType(ast, AS3Parser.PACKAGE);
    }

    public ASPackage getPackage() {
        LinkedListTree pkg = getPkgNode();
        if (pkg == null) {
            return null;
        }
        return new ASTASPackage(pkg);
    }
}