package uk.co.badgersinfoil.metaas.impl;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ASTASLocalFunction extends ASTScriptElement implements ASLocalFunction {

	private ASTFunctionCommon funcMixin;
	private ASTStatementList stmtListMixin;

    public ASTASLocalFunction(LinkedListTree ast) {
		super(ast);
		stmtListMixin = new ASTStatementList(ASTUtils.findChildByType(ast, AS3Parser.BLOCK));
		funcMixin = new ASTFunctionCommon(ast);
	}

    public String getName() {
		ASTIterator i = new ASTIterator(ast);
		LinkedListTree name = i.find(AS3Parser.IDENT);
		return name.getText();
    }

    public List<Comment> getCommentsAfter() {
        return CommentUtils.getCommentAfter(this.getAST());
    }

    public List<Comment> getCommentsBefore() {
        return CommentUtils.getCommentBefore(this.getAST());
    }

	public ASArg addParam(String name, String type) {
		return funcMixin.addParam(name, type);
	}

	public ASArg addRestParam(String name) {
		return funcMixin.addRestParam(name);
	}

	public List getArgs() {
		return funcMixin.getArgs();
	}

	public TypeDescriptor getType() {
		return funcMixin.getType();
	}

	public ASArg removeParam(String name) {
		return funcMixin.removeParam(name);
	}

	public void setType(String typeName) {
		funcMixin.setType(typeName);
	}

	public void addComment(String text) {
		stmtListMixin.addComment(text);
	}

	public Statement addStmt(String statement) {
		return stmtListMixin.addStmt(statement);
	}

	public boolean containsCode() {
		return stmtListMixin.containsCode();
	}

	public List getStatementList() {
		return stmtListMixin.getStatementList();
	}

	public ASBreakStatement newBreak() {
		return stmtListMixin.newBreak();
	}

	public ASContinueStatement newContinue() {
		return stmtListMixin.newContinue();
	}

	public ASDeclarationStatement newDeclaration(Expression assignment) {
		return stmtListMixin.newDeclaration(assignment);
	}

	public ASDeclarationStatement newDeclaration(String assignment) {
		return stmtListMixin.newDeclaration(assignment);
	}

	public ASDoWhileStatement newDoWhile(Expression condition) {
		return stmtListMixin.newDoWhile(condition);
	}

	public ASDoWhileStatement newDoWhile(String condition) {
		return stmtListMixin.newDoWhile(condition);
	}

	public ASExpressionStatement newExprStmt(Expression expr) {
		return stmtListMixin.newExprStmt(expr);
	}

	public ASExpressionStatement newExprStmt(String expr) {
		return stmtListMixin.newExprStmt(expr);
	}

	public ASForStatement newFor(Expression init, Expression condition,
			Expression iterate) {
		return stmtListMixin.newFor(init, condition, iterate);
	}

	public ASForStatement newFor(String init, String condition,
			String iterate) {
		return stmtListMixin.newFor(init, condition, iterate);
	}

	public ASForEachInStatement newForEachIn(Expression declaration,
			Expression expression) {
		return stmtListMixin.newForEachIn(declaration, expression);
	}

	public ASForEachInStatement newForEachIn(String declaration,
			String expression) {
		return stmtListMixin.newForEachIn(declaration, expression);
	}

	public ASForInStatement newForIn(Expression declaration,
			Expression expression) {
		return stmtListMixin.newForIn(declaration, expression);
	}

	public ASForInStatement newForIn(String declaration, String expression) {
		return stmtListMixin.newForIn(declaration, expression);
	}

	public ASIfStatement newIf(Expression condition) {
		return stmtListMixin.newIf(condition);
	}

	public ASIfStatement newIf(String condition) {
		return stmtListMixin.newIf(condition);
	}

	public ASReturnStatement newReturn() {
		return stmtListMixin.newReturn();
	}

	public ASReturnStatement newReturn(Expression expr) {
		return stmtListMixin.newReturn(expr);
	}

	public ASReturnStatement newReturn(String expr) {
		return stmtListMixin.newReturn(expr);
	}

	public ASSwitchStatement newSwitch(Expression condition) {
		return stmtListMixin.newSwitch(condition);
	}

	public ASSwitchStatement newSwitch(String condition) {
		return stmtListMixin.newSwitch(condition);
	}

	public ASThrowStatement newThrow(Expression t) {
		return stmtListMixin.newThrow(t);
	}

	public ASTryStatement newTryCatch(String var, String type) {
		return stmtListMixin.newTryCatch(var, type);
	}

	public ASTryStatement newTryFinally() {
		return stmtListMixin.newTryFinally();
	}

	public ASWhileStatement newWhile(Expression condition) {
		return stmtListMixin.newWhile(condition);
	}

	public ASWhileStatement newWhile(String condition) {
		return stmtListMixin.newWhile(condition);
	}

	public ASWithStatement newWith(Expression expr) {
		return stmtListMixin.newWith(expr);
	}

	public ASWithStatement newWith(String expr) {
		return stmtListMixin.newWith(expr);
	}

	public ASDefaultXMLNamespaceStatement newDefaultXMLNamespace(String namespace) {
		return stmtListMixin.newDefaultXMLNamespace(namespace);
	}

    public int getSpacerSize() {
        return SpacerUtil.calculateSpacerSize(ast);
    }
}
