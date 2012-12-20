/*
 * ASTASMethod.java
 * 
 * Copyright (c) 2006-2007 David Holroyd
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

import java.util.List;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.SyntaxException;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;


public class ASTASMethod extends ASTASMember implements ASMethod {
	private ASTStatementList stmtList;
	private ASTFunctionCommon funcMixin;

	public ASTASMethod(LinkedListTree ast) {
		super(ast);
		LinkedListTree block = ASTUtils.findChildByType(ast, AS3Parser.BLOCK);
		if (block != null) {
			// class methods have BLOCK, interface methods don't,
			stmtList = new ASTStatementList(block);
		}
		funcMixin = new ASTFunctionCommon(ast);
	}

	public boolean isOverride() {
		return ModifierUtils.isOverride(findModifiers());
	}

    public boolean isNative() {
        return ModifierUtils.isNative(findModifiers());
    }

    public boolean isFinal() {
        return ModifierUtils.isFinal(findModifiers());
    }

    public boolean isVirtual() {
        return ModifierUtils.isVirtual(findModifiers());
    }

    public void setOverride(boolean value) {
		ModifierUtils.setOverride(findModifiers(), value);
	}

	public String getName() {
		ASTIterator i = new ASTIterator(ast);
		LinkedListTree name = i.find(AS3Parser.IDENT);
		return name.getText();
	}

	public void setName(String name) {
		if (name.indexOf('.') != -1) {
			throw new SyntaxException("Method name must not contain '.'");
		}
		if (name.indexOf(':') != -1) {
			throw new SyntaxException("Method name must not contain ':'");
		}
		ASTIterator i = new ASTIterator(ast);
		i.find(AS3Parser.IDENT);
		LinkedListTree newName = AS3FragmentParser.parseIdent(name).getFirstChild();
		i.replace(newName);
	}

	public List getArgs() {
		return funcMixin.getArgs();
	}

	public TypeDescriptor getType() {
		return funcMixin.getType();
	}

	public void setType(String typeName) {
		funcMixin.setType(typeName);
	}

	public Statement addStmt(String statement) {
		return stmtList().addStmt(statement);
	}

	public ASExpressionStatement newExprStmt(String expr) {
		return stmtList().newExprStmt(expr);
	}
	public ASExpressionStatement newExprStmt(Expression expr) {
		return stmtList().newExprStmt(expr);
	}

	public ASArg addParam(String name, String type) {
		return funcMixin.addParam(name, type);
	}

	public ASArg addRestParam(String name) {
		return funcMixin.addRestParam(name);
	}

	public ASArg removeParam(String name) {
		return funcMixin.removeParam(name);
	}

	public void addComment(String text) {
		stmtList().addComment(text);
	}

	public AccessorRole getAccessorRole() {
		LinkedListTree decl = ASTUtils.findChildByType(ast, AS3Parser.ACCESSOR_ROLE);
		LinkedListTree roleNode = decl.getFirstChild();
		if (roleNode == null) {
			return AccessorRole.NORMAL_METHOD;
		}
		switch (roleNode.getType()) {
		    case AS3Parser.GET:
			return AccessorRole.GETTER;
		    case AS3Parser.SET:
			return AccessorRole.SETTER;
		    default:
			throw new SyntaxException("expected GET or SET; got "+roleNode);
		}
	}

	public void setAccessorRole(AccessorRole role) {
		LinkedListTree decl = ASTUtils.findChildByType(ast, AS3Parser.ACCESSOR_ROLE);
		LinkedListTree roleNode = decl.getFirstChild();
		if (roleNode != null) {
			if (role == AccessorRole.NORMAL_METHOD) {
				decl.deleteChild(0);
			} else if (role == AccessorRole.GETTER) {
				roleNode.token.setType(AS3Parser.GET);
				roleNode.token.setText("get");
			} else if (role == AccessorRole.SETTER) {
				roleNode.token.setType(AS3Parser.SET);
				roleNode.token.setText("set");
			} else {
				throw new IllegalArgumentException("bad role: "+role);
			}
		} else {
			if (role == AccessorRole.GETTER) {
				LinkedListTree node = ASTUtils.newAST(AS3Parser.GET, "get");
				node.appendToken(TokenBuilder.newSpace());
				decl.addChildWithTokens(node);
			} else if (role == AccessorRole.SETTER) {
				LinkedListTree node = ASTUtils.newAST(AS3Parser.SET, "set");
				node.appendToken(TokenBuilder.newSpace());
				decl.addChildWithTokens(node);
			}
		}
	}

	public ASIfStatement newIf(String condition) {
		return stmtList().newIf(condition);
	}
	public ASIfStatement newIf(Expression condition) {
		return stmtList().newIf(condition);
	}

	public ASForStatement newFor(String init, String condition, String update) {
		return stmtList().newFor(init, condition, update);
	}
	public ASForStatement newFor(Expression init, Expression condition, Expression update) {
		return stmtList().newFor(init, condition, update);
	}

	public ASForInStatement newForIn(String init, String list) {
		return stmtList().newForIn(init, list);
	}
	public ASForInStatement newForIn(Expression init, Expression list) {
		return stmtList().newForIn(init, list);
	}

	public ASForEachInStatement newForEachIn(String init, String list) {
		return stmtList().newForEachIn(init, list);
	}
	public ASForEachInStatement newForEachIn(Expression init, Expression list) {
		return stmtList().newForEachIn(init, list);
	}


	public ASWhileStatement newWhile(String condition) {
		return stmtList().newWhile(condition);
	}
	public ASWhileStatement newWhile(Expression condition) {
		return stmtList().newWhile(condition);
	}

	public ASDoWhileStatement newDoWhile(String condition) {
		return stmtList().newDoWhile(condition);
	}
	public ASDoWhileStatement newDoWhile(Expression condition) {
		return stmtList().newDoWhile(condition);
	}

	public ASSwitchStatement newSwitch(String condition) {
		return stmtList().newSwitch(condition);
	}
	public ASSwitchStatement newSwitch(Expression condition) {
		return stmtList().newSwitch(condition);
	}

	public ASWithStatement newWith(String expr) {
		return stmtList().newWith(expr);
	}
	public ASWithStatement newWith(Expression expr) {
		return stmtList().newWith(expr);
	}

	public ASDeclarationStatement newDeclaration(String assignment) {
		return stmtList().newDeclaration(assignment);
	}
	public ASDeclarationStatement newDeclaration(Expression assignment) {
		return stmtList().newDeclaration(assignment);
	}

	public ASReturnStatement newReturn(String expr) {
		return stmtList().newReturn(expr);
	}
	public ASReturnStatement newReturn(Expression expr) {
		return stmtList().newReturn(expr);
	}
	public ASReturnStatement newReturn() {
		return stmtList().newReturn();
	}

	public ASSuperStatement newSuper(List args) {
		return stmtList().newSuper(args);
	}

	public ASBreakStatement newBreak() {
		return stmtList().newBreak();
	}

	public ASTryStatement newTryCatch(String var, String type) {
		return stmtList().newTryCatch(var, type);
	}

	public ASTryStatement newTryFinally() {
		return stmtList().newTryFinally();
	}

	public ASContinueStatement newContinue() {
		return stmtList().newContinue();
	}

	public ASThrowStatement newThrow(Expression t) {
		return stmtList().newThrow(t);
	}

	public ASDefaultXMLNamespaceStatement newDefaultXMLNamespace(String namespace) {
		return stmtList().newDefaultXMLNamespace(namespace);
	}

	public boolean containsCode() {
		return stmtList().containsCode();
	}

	public List getStatementList() {
        ASTStatementList statementList = stmtList();
        if (statementList == null) {
            return new java.util.ArrayList();
        }
        return statementList.getStatementList();
	}

	public String getReturnDescriptionString() {
		DocComment doc = getDocumentation();
		DocTag ret = doc.findFirstTag("return");
		if (ret == null) {
			return null;
		}
		return ret.getBodyString();
	}

	public void setReturnDescription(String description) {
		DocComment doc = getDocumentation();
		DocTag ret = doc.findFirstTag("return");
		if (ret == null) {
			if (description != null) {
				doc.addParaTag("return", description);
			}
		} else {
			if (description == null) {
				doc.delete(ret);
			} else {
				ret.setBody(description);
			}
		}
	}

	private ASTStatementList stmtList() {
		if (stmtList == null) {
			return null;
            //throw new SyntaxException("Interface methods don't have a body");
		}
		return stmtList;
	}

    public List<Comment> getCommentsBefore() {
        LinkedListTree tree = this.findModifiers();
        if (tree != null) {
            return CommentUtils.getCommentBefore(tree);
        }
        return CommentUtils.getCommentBefore(this.getAST());
    }
}