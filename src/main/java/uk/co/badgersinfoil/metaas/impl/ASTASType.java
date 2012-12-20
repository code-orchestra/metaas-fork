/*
 * ASTASType.java
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

import java.util.Collections;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.runtime.tree.Tree;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public abstract class ASTASType extends ASTScriptElement implements ASType {

    public ASTASType(LinkedListTree ast) {
		super(ast);
	}

    public List findImports() {
        List result = new ArrayList();
        Tree typeBlock = ast.getFirstChildWithType(AS3Parser.TYPE_BLOCK);
        if (typeBlock != null) {
            LinkedListTree typeBlockAST = (LinkedListTree) typeBlock;
            for (LinkedListTree importAST : ASTUtils.findChildrenByType(typeBlockAST, AS3Parser.IMPORT)) {
                result.add(importText(importAST));
            }
        }
        return result;
    }

    private String importText(LinkedListTree imp) {
        return ASTUtils.identStarText(imp.getFirstChild());
    }

    public List<ASIncludeDirective> getIncludeDirectives() {
        List<ASIncludeDirective> directives = new ArrayList<ASIncludeDirective>();

        Tree typeBlock = ast.getFirstChildWithType(AS3Parser.TYPE_BLOCK);
        if (typeBlock != null) {
            LinkedListTree typeBlockAST = (LinkedListTree) typeBlock;
            for (LinkedListTree includeAST : ASTUtils.findChildrenByType(typeBlockAST, AS3Parser.INCLUDE_DIRECTIVE_AS3)) {
                directives.add(new ASTASIncludeDirective(includeAST));
            }
        }
        return directives;
    }

    public List<ASNamespaceDeclaration> getNamespaceDeclarations() {
        LinkedListTree typeBlockAST = ASTUtils.findChildByType(ast, AS3Parser.TYPE_BLOCK);
        if (typeBlockAST == null) {
            return null;
        }

        List<ASNamespaceDeclaration> declarations = new ArrayList<ASNamespaceDeclaration>();

        for (int i = 0; i < typeBlockAST.getChildCount(); i++) {
            Object astObj = typeBlockAST.getChild(i);
            if (astObj instanceof LinkedListTree) {
                LinkedListTree subTree = (LinkedListTree) astObj;
                if (subTree.getType() == AS3Parser.NAMESPACE_DEF) {
                    declarations.add(new ASTASNamespaceDeclaration(subTree));
                } else if (subTree.getType() == AS3Parser.STATIC_INITIALIZER) {
                    for (int j = 0; j < subTree.getChildCount(); j++) {
                        LinkedListTree subSubTree = (LinkedListTree) subTree.getChild(j);
                        if (subSubTree.getType() == AS3Parser.NAMESPACE_DEF) {
                            declarations.add(new ASTASNamespaceDeclaration(subSubTree));
                        }
                    }
                }
            }
        }

        // RE-3420
        // We need to keep the order of out of function statements and static initializers
        // so we can't just get all children of first type and then all of second type

//        for (LinkedListTree nsAST : ASTUtils.findChildrenByType(typeBlockAST, AS3Parser.NAMESPACE_DEF)) {
//            declarations.add(new ASTASNamespaceDeclaration(nsAST));
//        }

        return declarations;
    }

    public List<ASUseNamespaceStatement> getUseNamespaceDirectives() {
        List<ASUseNamespaceStatement> result = new ArrayList<ASUseNamespaceStatement>();

        Tree typeBlock = ast.getFirstChildWithType(AS3Parser.TYPE_BLOCK);
        if (typeBlock != null) {
            LinkedListTree typeBlockAST = (LinkedListTree) typeBlock;
            for (LinkedListTree includeAST : ASTUtils.findChildrenByType(typeBlockAST, AS3Parser.USE)) {
                result.add(new ASTASUseNamespaceStatement(includeAST));
            }
        }
        
        return result;
    }

    public String getName() {
		ASTIterator i = new ASTIterator(ast);
        LinkedListTree identifier;

        // ActionScript 3.0
        try {
            identifier = i.find(AS3Parser.IDENT);
            return identifier.getText();
        } catch (IllegalStateException e1) {
            // ActionScript 2.0
            try {
                i  = new ASTIterator(ast);
                identifier = i.find(AS3Parser.IDENTIFIER);
                String fqName = ASTUtils.identText(identifier);

                if (fqName.contains(".")) {
                    return fqName.substring(fqName.lastIndexOf(".") + 1, fqName.length());
                }

                return fqName;

            } catch (IllegalStateException e2) {
                throw new IllegalStateException("Type tree does not contain IDENT or IDENTIFIER child");
            }
        }
	}

	public void setName(String name) {
		ASTIterator i = new ASTIterator(ast);
		i.find(AS3Parser.IDENT);
		i.replace(ASTUtils.newAST(AS3Parser.IDENT, name));
	}
	
	public List getMethods() {
		List results = new LinkedList();
		for (ASTIterator i=blockIter(); i.hasNext(); ) {
			LinkedListTree member = i.next();
			if (member.getType() == AS3Parser.METHOD_DEF) {
				results.add(new ASTASMethod(member));
			} else if (member.getType() == AS3Parser.STATIC_INITIALIZER) {
                for (int j = 0; j < member.getChildCount(); j++) {
                    LinkedListTree subTree = (LinkedListTree) member.getChild(j);
                    if (subTree.getType() == AS3Parser.METHOD_DEF) {
                        results.add(new ASTASMethod(subTree));
                    }
                }
            }
		}
		return Collections.unmodifiableList(results);
	}

	/**
	 * Returns the ActionScript method with the given name, or null, if no
	 * such method exists.
	 */
	public ASMethod getMethod(String name) {
		// TODO: does AS3 do overloading?  This method will be no use
		//       if it does.
		for (ASTIterator i=blockIter(); i.hasNext(); ) {
			LinkedListTree member = i.next();
			if (member.getType() == AS3Parser.METHOD_DEF) {
				ASMethod meth = new ASTASMethod(member);
				if (meth.getName().equals(name)) {
					return meth;
				}
			}
		}
		return null;
	}

	public void removeMethod(String name) {
		// TODO: does AS3 do overloading?  This method will be no use
		//       if it does.
		for (ASTIterator i=blockIter(); i.hasNext(); ) {
			LinkedListTree member = i.next();
			if (member.getType() == AS3Parser.METHOD_DEF) {
				ASMethod meth = new ASTASMethod(member);
				if (meth.getName().equals(name)) {
					i.remove();
					return;
				}
			}
		}
	}

	protected ASTIterator blockIter() {
		return new ASTIterator(findTypeBlock());
	}

	protected LinkedListTree findTypeBlock() {
		return ASTUtils.findChildByType(ast, AS3Parser.TYPE_BLOCK);
	}

	public void setDocComment(String text) {
		DocCommentUtils.setDocComment(ast, text);
	}

	public String getDocComment() {
		return DocCommentUtils.getDocComment(ast);
	}

	public String getDescriptionString() {
		return getDocumentation().getDescriptionString();
	}

	public void setDescription(String description) {
		getDocumentation().setDescriptionString(description);
	}

	public DocComment getDocumentation() {
		return DocCommentUtils.createDocComment(ast);
	}

	public Visibility getVisibility() {
		return ModifierUtils.getVisibility(findModifiers());
	}

	public void setVisibility(Visibility visibility) {
		ModifierUtils.setVisibility(findModifiers(), visibility);
	}

	private LinkedListTree findModifiers() {
		return ASTUtils.findChildByType(ast, AS3Parser.MODIFIERS);
	}

	public List getAllMetaTags() {
		return TagUtils.getAllMetaTags(ast);
	}

	public ASMetaTag getFirstMetatag(String name) {
		return TagUtils.getFirstMetaTag(ast, name);
	}

	public List getMetaTagsWithName(String name) {
		return TagUtils.getMetaTagWithName(ast, name);
	}

	public ASMetaTag newMetaTag(String name) {
		return TagUtils.newMetaTag(ast, name);
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
        LinkedListTree tree = this.findModifiers();
        if (tree != null) {
            return CommentUtils.getCommentBefore(tree);
        }
        return CommentUtils.getCommentBefore(this.getAST());
    }
}