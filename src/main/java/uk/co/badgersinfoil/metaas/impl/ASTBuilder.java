/*
 * ASTBuilder.java
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.SyntaxException;
import uk.co.badgersinfoil.metaas.dom.ASAssignmentExpression;
import uk.co.badgersinfoil.metaas.dom.ASBinaryExpression;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.impl.antlr.PlaceholderLinkedListToken;


/**
 * Utilities to build and assemble Abstract Syntax Tree fragments for inclusion
 * into the compilation unit being generated.
 */
public class ASTBuilder {

	private ASTBuilder() {
		// hide default ctor
	}

	public static LinkedListTree newExprStmt(LinkedListTree expr) {
		LinkedListTree exprStmt = ASTUtils.newImaginaryAST(AS3Parser.EXPR_STMNT);
		exprStmt.addChildWithTokens(expr);
		exprStmt.appendToken(TokenBuilder.newSemi());
		return exprStmt;
	}

	public static AS3ASTCompilationUnit synthesizeClass(String qualifiedName) {
		LinkedListTree unit = ASTUtils.newImaginaryAST(AS3Parser.COMPILATION_UNIT);
		LinkedListTree pkg = ASTUtils.newAST(AS3Parser.PACKAGE, "package");
		pkg.appendToken(TokenBuilder.newSpace());
		unit.addChildWithTokens(pkg);
		pkg.appendToken(TokenBuilder.newSpace());
		String packageName = packageNameFrom(qualifiedName);
		if (packageName != null) {
			pkg.addChildWithTokens(AS3FragmentParser.parseIdent(packageName));
		}
		LinkedListTree packageBlock = newBlock();
		pkg.addChildWithTokens(packageBlock);
		String className = typeNameFrom(qualifiedName);
		
		LinkedListTree clazz = synthesizeAS3Class(className);
		ASTUtils.addChildWithIndentation(packageBlock, clazz);
		return new AS3ASTCompilationUnit(unit);
	}

	public static ASCompilationUnit synthesizeInterface(String qualifiedName) {
		LinkedListTree unit = ASTUtils.newImaginaryAST(AS3Parser.COMPILATION_UNIT);
		LinkedListTree pkg = ASTUtils.newAST(AS3Parser.PACKAGE, "package");
		unit.addChildWithTokens(pkg);
		pkg.appendToken(TokenBuilder.newSpace());
		String packageName = packageNameFrom(qualifiedName);
		if (packageName != null) {
			pkg.addChildWithTokens(AS3FragmentParser.parseIdent(packageName));
		}
		LinkedListTree packageBlock = newBlock();
		pkg.addChildWithTokens(packageBlock);
		
		LinkedListTree iface = synthesizeAS3Interface(qualifiedName);
		ASTUtils.addChildWithIndentation(packageBlock, iface);
		return new AS3ASTCompilationUnit(unit);
	}

	private static LinkedListTree synthesizeAS3Interface(String qualifiedName) {
		LinkedListTree iface = ASTUtils.newImaginaryAST(AS3Parser.INTERFACE_DEF);
		LinkedListTree modifiers = ASTUtils.newImaginaryAST(AS3Parser.MODIFIERS);
		iface.addChildWithTokens(modifiers);
		modifiers.addChildWithTokens(ASTUtils.newAST(AS3Parser.PUBLIC, "public"));
		modifiers.appendToken(TokenBuilder.newSpace());
		iface.appendToken(TokenBuilder.newInterface());
		iface.appendToken(TokenBuilder.newSpace());
		iface.addChildWithTokens(ASTUtils.newAST(AS3Parser.IDENT, typeNameFrom(qualifiedName)));
		iface.appendToken(TokenBuilder.newSpace());
		iface.addChildWithTokens(newTypeBlock());
		LinkedListTree annos = ASTUtils.newPlaceholderAST(AS3Parser.ANNOTATIONS);
		iface.addChildWithTokens(0, annos);
		return iface;
	}

	private static String typeNameFrom(String qualifiedName) {
		int p = qualifiedName.lastIndexOf('.');
		if (p == -1) {
			return qualifiedName;
		}
		return qualifiedName.substring(p+1);
	}

	private static LinkedListTree synthesizeAS3Class(String className) {
		LinkedListTree clazz = ASTUtils.newImaginaryAST(AS3Parser.CLASS_DEF);
		LinkedListTree modifiers = ASTUtils.newImaginaryAST(AS3Parser.MODIFIERS);
		clazz.addChildWithTokens(modifiers);
		LinkedListTree modPublic = ASTUtils.newAST(AS3Parser.PUBLIC, "public");
		modifiers.addChildWithTokens(modPublic);
		modifiers.appendToken(TokenBuilder.newSpace());
		clazz.appendToken(TokenBuilder.newClass());
		clazz.appendToken(TokenBuilder.newSpace());
		clazz.addChildWithTokens(ASTUtils.newAST(AS3Parser.IDENT, className));
		clazz.appendToken(TokenBuilder.newSpace());
		clazz.addChildWithTokens(newTypeBlock());
		LinkedListTree annos = ASTUtils.newPlaceholderAST(AS3Parser.ANNOTATIONS);
		clazz.addChildWithTokens(0, annos);
		return clazz;
	}

	private static String packageNameFrom(String qualifiedName) {
		int p = qualifiedName.lastIndexOf('.');
		if (p == -1) {
			return null;
		}
		return qualifiedName.substring(0, p);
	}

	public static ASTASMethod newClassMethod(String name, Visibility visibility, String returnType) {
		LinkedListTree def = ASTUtils.newImaginaryAST(AS3Parser.METHOD_DEF);
		LinkedListTree annos = ASTUtils.newPlaceholderAST(AS3Parser.ANNOTATIONS);
		def.addChildWithTokens(annos);
		def.addChildWithTokens(ModifierUtils.toModifiers(visibility));
		def.appendToken(TokenBuilder.newFunction());
		def.appendToken(TokenBuilder.newSpace());
		LinkedListTree acc = ASTUtils.newPlaceholderAST(AS3Parser.ACCESSOR_ROLE);
		def.addChildWithTokens(acc);
		LinkedListTree methName = ASTUtils.newAST(AS3Parser.IDENT, name);
		def.addChildWithTokens(methName);
		def.addChildWithTokens(ASTUtils.newParentheticAST(AS3Parser.PARAMS, AS3Parser.LPAREN, "(", AS3Parser.RPAREN, ")"));
		if (returnType != null) {
			def.addChildWithTokens(AS3FragmentParser.parseTypeSpec(returnType));
		}
		def.appendToken(TokenBuilder.newSpace());
		LinkedListTree block = newBlock();
		def.addChildWithTokens(block);

		return new ASTASMethod(def);
	}

	public static ASTASField newField(String name, Visibility visibility, String type) {
		if (name.indexOf('.') != -1) {
			throw new SyntaxException("field name must not contain '.'");
		}
		LinkedListTree decl = ASTUtils.newImaginaryAST(AS3Parser.VAR_DEF);
		LinkedListTree annos = ASTUtils.newPlaceholderAST(AS3Parser.ANNOTATIONS);
		decl.addChildWithTokens(annos);
		decl.addChildWithTokens(ModifierUtils.toModifiers(visibility));
		decl.addChildWithTokens(ASTUtils.newAST(AS3Parser.VAR, "var"));
		decl.appendToken(TokenBuilder.newSpace());
		LinkedListTree def = ASTUtils.newAST(AS3Parser.IDENT, name);
		decl.addChildWithTokens(def);
		if (type != null) {
			def.addChildWithTokens(AS3FragmentParser.parseTypeSpec(type));
		}
		decl.appendToken(TokenBuilder.newSemi());
		return new ASTASField(decl);
	}

	public static ASTASMethod newInterfaceMethod(String name, Visibility visibility, String returnType) {
		LinkedListTree def = ASTUtils.newImaginaryAST(AS3Parser.METHOD_DEF);
		LinkedListTree annos = ASTUtils.newPlaceholderAST(AS3Parser.ANNOTATIONS);
		def.addChildWithTokens(annos);
		def.addChildWithTokens(ModifierUtils.toModifiers(visibility));
		def.appendToken(TokenBuilder.newFunction());
		def.appendToken(TokenBuilder.newSpace());
		LinkedListTree acc = ASTUtils.newPlaceholderAST(AS3Parser.ACCESSOR_ROLE);
		def.addChildWithTokens(acc);
		LinkedListTree methName = ASTUtils.newAST(AS3Parser.IDENT, name);
		def.addChildWithTokens(methName);
		def.addChildWithTokens(ASTUtils.newParentheticAST(AS3Parser.PARAMS, AS3Parser.LPAREN, "(", AS3Parser.RPAREN, ")"));
		if (returnType != null) {
			def.addChildWithTokens(AS3FragmentParser.parseTypeSpec(returnType));
		}
		def.appendToken(TokenBuilder.newSemi());

		return new ASTASMethod(def);
	}

	public static LinkedListTree newBlock() {
		return newBlock(AS3Parser.BLOCK);
	}

	public static LinkedListTree newTypeBlock() {
		return newBlock(AS3Parser.TYPE_BLOCK);
	}

	private static LinkedListTree newBlock(int type) {
		LinkedListTree ast = ASTUtils.newParentheticAST(type,
		                                                AS3Parser.LCURLY, "{",
		                                                AS3Parser.RCURLY, "}");
		LinkedListToken nl = TokenBuilder.newNewline();
		ast.getInitialInsertionAfter().afterInsert(nl);
		ast.setInitialInsertionAfter(nl);
		return ast;
	}

	public static LinkedListTree newMetadataTag(String name) {
		LinkedListTree ast = ASTUtils.newParentheticAST(AS3Parser.ANNOTATION,
		                                                AS3Parser.LBRACK, "[",
		                                                AS3Parser.RBRACK, "]");
		ast.addChildWithTokens(ASTUtils.newAST(AS3Parser.IDENT, name));
		return ast;
	}

	/**
	 * returns a CONDITION node with the given expression as its child
	 */
	private static LinkedListTree condition(LinkedListTree expr) {
		LinkedListTree cond =  ASTUtils.newParentheticAST(AS3Parser.CONDITION,
		                                                  AS3Parser.LPAREN, "(",
		                                                  AS3Parser.RPAREN, ")");
		cond.addChildWithTokens(expr);
		return cond;
	}

    /**
	 * returns a CONDITION node with the given expression as its child
	 */
	private static LinkedListTree conditionList(LinkedListTree expr) {
		LinkedListTree cond =  ASTUtils.newParentheticAST(AS3Parser.CONDITION_LIST,
		                                                  AS3Parser.LPAREN, "(",
		                                                  AS3Parser.RPAREN, ")");
		cond.addChildWithTokens(expr);
		return cond;
	}

	public static LinkedListTree newIf(String condition) {
		return newIf(AS3FragmentParser.parseExpr(condition));
	}
	public static LinkedListTree newIf(LinkedListTree condition) {
		LinkedListTree ifStmt = ASTUtils.newAST(AS3Parser.IF, "if");
		ifStmt.appendToken(TokenBuilder.newSpace());
		ifStmt.addChildWithTokens(conditionList(condition));
		ifStmt.appendToken(TokenBuilder.newSpace());
		ifStmt.addChildWithTokens(ASTBuilder.newBlock());
		return ifStmt;
	}

	public static LinkedListTree newFor(String init, String condition, String iterate) {
		return newFor(init==null      ? null : AS3FragmentParser.parseForInit(init),
		              condition==null ? null : AS3FragmentParser.parseForCond(condition),
		              iterate==null   ? null : AS3FragmentParser.parseForIter(iterate));
	}
	public static LinkedListTree newFor(LinkedListTree init, LinkedListTree condition, LinkedListTree iterate) {
		LinkedListTree forStmt = ASTUtils.newAST(AS3Parser.FOR, "for");
		forStmt.appendToken(TokenBuilder.newSpace());
		forStmt.appendToken(TokenBuilder.newLParen());
		if (init != null) {
			forStmt.addChildWithTokens(init);
		} else {
			LinkedListTree initStmt = ASTUtils.newPlaceholderAST(AS3Parser.FOR_INIT);
			forStmt.addChildWithTokens(initStmt);
		}
		forStmt.appendToken(TokenBuilder.newSemi());
		forStmt.appendToken(TokenBuilder.newSpace());
		if (condition != null) {
			forStmt.addChildWithTokens(condition);
		} else {
			LinkedListTree condStmt = ASTUtils.newPlaceholderAST(AS3Parser.FOR_CONDITION);
			forStmt.addChildWithTokens(condStmt);
		}
		forStmt.appendToken(TokenBuilder.newSemi());
		forStmt.appendToken(TokenBuilder.newSpace());
		if (iterate != null) {
			forStmt.addChildWithTokens(iterate);
		} else {
			LinkedListTree iterStmt = ASTUtils.newPlaceholderAST(AS3Parser.FOR_ITERATOR);
			forStmt.addChildWithTokens(iterStmt);
		}
		forStmt.appendToken(TokenBuilder.newRParen());
		return forStmt;
	}

	public static LinkedListTree newForIn(String declaration, String expression) {
		return newForIn(AS3FragmentParser.parseForInVar(declaration),
		                AS3FragmentParser.parseExpr(expression));
	}
	public static LinkedListTree newForIn(LinkedListTree declaration, LinkedListTree expression) {
		LinkedListTree forStmt = ASTUtils.newAST(AS3Parser.FOR_IN, "for");
		forStmt.appendToken(TokenBuilder.newSpace());
		genForInSetup(forStmt, declaration, expression);
		return forStmt;
	}

	public static LinkedListTree newForEachIn(String declaration, String expression) {
		return newForEachIn(AS3FragmentParser.parseForInVar(declaration),
		                    AS3FragmentParser.parseExpr(expression));
	}
	public static LinkedListTree newForEachIn(LinkedListTree declaration, LinkedListTree expression) {
		LinkedListTree forStmt = ASTUtils.newAST(AS3Parser.FOR_EACH, "for");
		forStmt.appendToken(TokenBuilder.newSpace());
		forStmt.appendToken(TokenBuilder.newEach());
		genForInSetup(forStmt, declaration, expression);
		return forStmt;
	}

	/**
	 * Common code for both for-in and for-each-in loop setup
	 */
	private static void genForInSetup(LinkedListTree forStmt,
	                                  LinkedListTree declaration,
	                                  LinkedListTree expression)
	{
		forStmt.appendToken(TokenBuilder.newLParen());
		forStmt.addChildWithTokens(declaration);
		forStmt.appendToken(TokenBuilder.newSpace());
		forStmt.appendToken(TokenBuilder.newIn());
		forStmt.appendToken(TokenBuilder.newSpace());
		forStmt.addChildWithTokens(expression);
		forStmt.appendToken(TokenBuilder.newRParen());
	}

	public static LinkedListTree newWhile(String condition) {
		return newWhile(AS3FragmentParser.parseExpr(condition));
	}
	public static LinkedListTree newWhile(LinkedListTree condition) {
		LinkedListTree whileStmt = ASTUtils.newAST(AS3Parser.WHILE, "while");
		whileStmt.appendToken(TokenBuilder.newSpace());
		whileStmt.addChildWithTokens(condition(condition));
		return whileStmt;
	}

	public static LinkedListTree newDoWhile(String condition) {
		return newDoWhile(AS3FragmentParser.parseExpr(condition));
	}
	public static LinkedListTree newDoWhile(LinkedListTree condition) {
		LinkedListTree doWhileStmt = ASTUtils.newAST(AS3Parser.DO, "do");
		doWhileStmt.appendToken(TokenBuilder.newSpace());
		LinkedListTree block = ASTBuilder.newBlock();
		doWhileStmt.addChildWithTokens(block);
		doWhileStmt.appendToken(TokenBuilder.newSpace());
		doWhileStmt.appendToken(TokenBuilder.newWhile());
		doWhileStmt.appendToken(TokenBuilder.newSpace());
		doWhileStmt.addChildWithTokens(condition(condition));
		doWhileStmt.appendToken(TokenBuilder.newSemi());
		return doWhileStmt;
	}

	public static LinkedListTree newSwitch(String condition) {
		return newSwitch(AS3FragmentParser.parseExpr(condition));
	}
	public static LinkedListTree newSwitch(LinkedListTree condition) {
		LinkedListTree switchStmt = ASTUtils.newAST(AS3Parser.SWITCH, "switch");
		switchStmt.appendToken(TokenBuilder.newSpace());
		switchStmt.addChildWithTokens(condition(condition));
		switchStmt.appendToken(TokenBuilder.newSpace());
		LinkedListTree block = ASTBuilder.newBlock();
		switchStmt.addChildWithTokens(block);
		return switchStmt;
	}

	public static LinkedListTree newWith(String expr) {
		return newWith(AS3FragmentParser.parseExpr(expr));
	}
	public static LinkedListTree newWith(LinkedListTree expr) {
		LinkedListTree withStmt = ASTUtils.newAST(AS3Parser.WITH, "with");
		withStmt.appendToken(TokenBuilder.newSpace());
		withStmt.addChildWithTokens(condition(expr));
		return withStmt;
	}

	public static LinkedListTree newDeclaration(String assignment) {
		return newDeclaration(AS3FragmentParser.parseVariableDeclarator(assignment));
	}
	public static LinkedListTree newDeclaration(LinkedListTree assignment) {
		LinkedListTree declStmt = ASTUtils.newAST(AS3Parser.VAR, "var");
		declStmt.appendToken(TokenBuilder.newSpace());
		declStmt.addChildWithTokens(assignment);
		declStmt.appendToken(TokenBuilder.newSemi());
		return declStmt;
	}

	public static LinkedListTree newReturn(String expr) {
		return newReturn(expr==null ? null : AS3FragmentParser.parseExpr(expr));
	}
	public static LinkedListTree newReturn(LinkedListTree expr) {
		LinkedListTree returnStmt = ASTUtils.newAST(AS3Parser.RETURN, "return");
		if (expr != null) {
			returnStmt.appendToken(TokenBuilder.newSpace());
			returnStmt.addChildWithTokens(expr);
		}
		returnStmt.appendToken(TokenBuilder.newSemi());
		return returnStmt;
	}

	/**
	 * @param args a list of ASExpression objects
	 */
	public static LinkedListTree newSuperStatement(List args) {
		LinkedListTree superStmt = ASTUtils.newAST(AS3Parser.SUPER, "super");
		LinkedListTree argumentsNode = ASTUtils.newParentheticAST(AS3Parser.ARGUMENTS, AS3Parser.LPAREN, "(", AS3Parser.RPAREN, ")");
		ArgumentUtils.overwriteArgsWithExpressionList(argumentsNode, args);
		superStmt.addChildWithTokens(argumentsNode);
		superStmt.appendToken(TokenBuilder.newSemi());
		return superStmt;
	}

	public static LinkedListTree newBreakStatement() {
		LinkedListTree breakStmt = ASTUtils.newAST(AS3Parser.BREAK, "break");
		breakStmt.appendToken(TokenBuilder.newSemi());
		return breakStmt;
	}

	public static LinkedListTree newTryStatement() {
		LinkedListTree tryStmt = ASTUtils.newAST(AS3Parser.TRY, "try");
		tryStmt.appendToken(TokenBuilder.newSpace());
		tryStmt.addChildWithTokens(newBlock());
		return tryStmt;
	}

	public static LinkedListTree newCatchClause(String var, String type) {
		LinkedListTree tryStmt = ASTUtils.newAST(AS3Parser.CATCH, "catch");
		tryStmt.appendToken(TokenBuilder.newSpace());
		tryStmt.appendToken(TokenBuilder.newLParen());
		tryStmt.addChildWithTokens(AS3FragmentParser.parseSimpleIdent(var));
		if (type != null) {
			tryStmt.addChildWithTokens(AS3FragmentParser.parseTypeSpec(type));
		}
		tryStmt.appendToken(TokenBuilder.newRParen());
		tryStmt.appendToken(TokenBuilder.newSpace());
		tryStmt.addChildWithTokens(newBlock());
		return tryStmt;
	}

	public static LinkedListTree newFinallyClause() {
		LinkedListTree tryStmt = ASTUtils.newAST(AS3Parser.FINALLY, "finally");
		tryStmt.appendToken(TokenBuilder.newSpace());
		tryStmt.addChildWithTokens(newBlock());
		return tryStmt;
	}

	public static LinkedListTree newContinueStatement() {
		LinkedListTree continueStmt = ASTUtils.newAST(AS3Parser.CONTINUE, "continue");
		continueStmt.appendToken(TokenBuilder.newSemi());
		return continueStmt;
	}

	public static ASBinaryExpression newBinaryExpression(LinkedListToken op, Expression left, Expression right) {
		LinkedListTree ast = ASTUtils.newAST(op);
		LinkedListTree leftExpr = ((ASTExpression)left).getAST();
		assertNoParent("left-hand expression", leftExpr);
		LinkedListTree rightExpr = ((ASTExpression)right).getAST();
		if (precidence(ast) < precidence(leftExpr)) {
			leftExpr = parenthise(leftExpr);
		}
		if (precidence(ast) < precidence(rightExpr)) {
			rightExpr = parenthise(rightExpr);
		}
		// don't use addChildWithTokens(); special handling below,
		ast.addChild(leftExpr);
		ast.addChild(rightExpr);
		leftExpr.getStopToken().setNext(op);
		rightExpr.getStartToken().setPrev(op);
		ast.setStartToken(leftExpr.getStartToken());
		ast.setStopToken(rightExpr.getStopToken());
		spaceEitherSide(op);
		return new ASTASBinaryExpression(ast);
	}

	public static ASAssignmentExpression newAssignExpression(LinkedListToken op, Expression left, Expression right) {
		LinkedListTree ast = ASTUtils.newAST(op);
		LinkedListTree leftExpr = ((ASTExpression)left).getAST();
		assertNoParent("left-hand expression", leftExpr);
		LinkedListTree rightExpr = ((ASTExpression)right).getAST();
		if (precidence(ast) < precidence(leftExpr)) {
			leftExpr = parenthise(leftExpr);
		}
		if (precidence(ast) < precidence(rightExpr)) {
			rightExpr = parenthise(rightExpr);
		}
		// don't use addChildWithTokens(); special handling below,
		ast.addChild(leftExpr);
		ast.addChild(rightExpr);
		leftExpr.getStopToken().setNext(op);
		rightExpr.getStartToken().setPrev(op);
		ast.setStartToken(leftExpr.getStartToken());
		ast.setStopToken(rightExpr.getStopToken());
		spaceEitherSide(op);
		return new ASTASAssignmentExpression(ast);
	}

	public static void assertNoParent(String astDescription, LinkedListTree ast)
	{
		if (ast.getParent() != null) {
			throw new SyntaxException(astDescription+" cannot be added to a second parent node");
		}
	}

	private static LinkedListTree parenthise(LinkedListTree expr) {
		LinkedListTree result = ASTUtils.newParentheticAST(AS3Parser.ENCPS_EXPR, AS3Parser.LPAREN, "(", AS3Parser.RPAREN, ")");
		result.addChildWithTokens(expr);
		return result;
	}

	private static int precidence(LinkedListTree ast) {
		switch (ast.getType()) {
			case AS3Parser.ASSIGN:
			case AS3Parser.STAR_ASSIGN:
			case AS3Parser.DIV_ASSIGN:
			case AS3Parser.MOD_ASSIGN:
			case AS3Parser.PLUS_ASSIGN:
			case AS3Parser.MINUS_ASSIGN:
			case AS3Parser.SL_ASSIGN:
			case AS3Parser.SR_ASSIGN:
			case AS3Parser.BSR_ASSIGN:
			case AS3Parser.BAND_ASSIGN:
			case AS3Parser.BXOR_ASSIGN:
			case AS3Parser.BOR_ASSIGN:
			case AS3Parser.LAND_ASSIGN:
			case AS3Parser.LOR_ASSIGN:
				return 13;
			case AS3Parser.QUESTION:
				return 12;
			case AS3Parser.LOR:
				return 11;
			case AS3Parser.LAND:
				return 10;
			case AS3Parser.BOR:
				return 9;
			case AS3Parser.BXOR:
				return 8;
			case AS3Parser.BAND:
				return 7;
			case AS3Parser.STRICT_EQUAL:
			case AS3Parser.STRICT_NOT_EQUAL:
			case AS3Parser.NOT_EQUAL:
			case AS3Parser.EQUAL:
				return 6;
			case AS3Parser.IN:
			case AS3Parser.LT:
			case AS3Parser.GT:
			case AS3Parser.LE:
			case AS3Parser.GE:
			case AS3Parser.IS:
			case AS3Parser.AS:
			case AS3Parser.INSTANCEOF:
				return 5;
			case AS3Parser.SL:
			case AS3Parser.SR:
			case AS3Parser.BSR:
				return 4;
			case AS3Parser.PLUS:
			case AS3Parser.MINUS:
				return 3;
			case AS3Parser.STAR:
			case AS3Parser.DIV:
			case AS3Parser.MOD:
				return 2;
			default:
				return 1;
		}
	}

	public static LinkedListTree newFieldAccessExpression(LinkedListTree target, LinkedListTree name) {
		LinkedListToken op = TokenBuilder.newDot();
		LinkedListTree ast = ASTUtils.newAST(op);
		assertNoParent("target expression", target);
		// don't use addChildWithTokens(); special handling below,
		ast.addChild(target);
		ast.addChild(name);
		target.getStopToken().setNext(op);
		name.getStartToken().setPrev(op);
		ast.setStartToken(target.getStartToken());
		ast.setStopToken(name.getStopToken());
		return ast;
	}

	public static LinkedListTree newConditionalExpression(LinkedListTree conditionExpr,
	                                                      LinkedListTree thenExpr,
	                                                      LinkedListTree elseExpr)
	{
		LinkedListToken op = TokenBuilder.newQuestion();
		LinkedListToken colon = TokenBuilder.newColon();
		LinkedListTree ast = ASTUtils.newAST(op);
		// don't use addChildWithTokens(); special handling below,
		ast.addChild(conditionExpr);
		conditionExpr.getStopToken().setNext(op);
		ast.addChild(thenExpr);
		thenExpr.getStartToken().setPrev(op);
		thenExpr.getStopToken().setNext(colon);
		ast.addChild(elseExpr);
		elseExpr.getStartToken().setPrev(colon);
		ast.setStartToken(conditionExpr.getStartToken());
		ast.setStopToken(elseExpr.getStopToken());
		spaceEitherSide(op);
		spaceEitherSide(colon);
		return ast;
	}

	private static void spaceEitherSide(LinkedListToken token) {
		token.beforeInsert(TokenBuilder.newSpace());
		token.afterInsert(TokenBuilder.newSpace());
	}

	public static LinkedListTree newFunctionExpression() {
		LinkedListTree def = ASTUtils.newImaginaryAST(AS3Parser.FUNC_DEF);
		def.appendToken(TokenBuilder.newFunction());
		def.appendToken(TokenBuilder.newSpace());
		// TODO: placeholder for name?
		def.addChildWithTokens(ASTUtils.newParentheticAST(AS3Parser.PARAMS, AS3Parser.LPAREN, "(", AS3Parser.RPAREN, ")"));
		def.appendToken(TokenBuilder.newSpace());
		LinkedListTree block = newBlock();
		def.addChildWithTokens(block);
		return def;
	}

	public static LinkedListTree newArrayLiteral() {
		LinkedListTree lit = ASTUtils.newParentheticAST(AS3Parser.ARRAY_LITERAL, AS3Parser.LBRACK, "[", AS3Parser.RBRACK, "]");
		return lit;
	}

	public static LinkedListTree newObjectLiteral() {
		LinkedListTree lit = newBlock(AS3Parser.OBJECT_LITERAL);
		return lit;
	}

	public static LinkedListTree newObjectField(String name,
	                                            LinkedListTree value)
	{
		LinkedListTree field = ASTUtils.newImaginaryAST(AS3Parser.OBJECT_FIELD);
		field.addChildWithTokens(AS3FragmentParser.parseSimpleIdent(name));
		field.appendToken(TokenBuilder.newColon());
		field.appendToken(TokenBuilder.newSpace());
		field.addChildWithTokens(value);
		return field;
	}

	public static LinkedListTree newThrowStatement(LinkedListTree ast) {
		LinkedListTree thrw = ASTUtils.newAST(AS3Parser.THROW, "throw");
		thrw.appendToken(TokenBuilder.newSpace());
		thrw.addChildWithTokens(ast);
		thrw.appendToken(TokenBuilder.newSemi());
		return thrw;
	}

	public static LinkedListTree dup(LinkedListTree ast) {
		Map tokens = dupTokStream(ast);
		return dupTree(ast, tokens);
	}

	private static LinkedListTree dupTree(LinkedListTree ast, Map tokens) {
		LinkedListToken newTok = (LinkedListToken)tokens.get(ast.getToken());
		LinkedListTree result = new LinkedListTree(newTok);
		result.setStartToken((LinkedListToken)tokens.get(ast.getStartToken()));
		result.setStopToken((LinkedListToken)tokens.get(ast.getStopToken()));
		result.setTokenListUpdater(ast.getTokenListUpdater());
		result.setInitialInsertionAfter(ast.getInitialInsertionAfter());
		result.setInitialInsertionBefore(ast.getInitialInsertionBefore());
		for (ASTIterator i=new ASTIterator(ast); i.hasNext(); ) {
			LinkedListTree newChild = dupTree(i.next(), tokens);
			result.addChild(newChild);
		}
		return result;
	}

	private static Map dupTokStream(LinkedListTree ast) {
		Map tokens = new HashMap();
		LinkedListToken last = null;
		for (LinkedListToken tok=ast.getStartToken(); tok!=null&&tok.getType()!=-1; tok=tok.getNext()) {
			LinkedListToken newTok = dupTok(tok);
			tokens.put(tok, newTok);
			if (last != null) {
				last.setNext(newTok);
			}
			if (tok == ast.getStopToken()) {
				break;
			}
			last = newTok;
		}
		return tokens;
	}

	private static LinkedListToken dupTok(LinkedListToken tok) {
		LinkedListToken result;
		if (tok instanceof PlaceholderLinkedListToken) {
			result = new PlaceholderLinkedListToken(((PlaceholderLinkedListToken)tok).getHeld());
		} else {
			result = new LinkedListToken(tok.getType(),
		                                     tok.getText());
		}
		result.setChannel(tok.getChannel());
		result.setCharPositionInLine(tok.getCharPositionInLine());
		result.setLine(tok.getLine());
		return result;
	}

	public static LinkedListTree newDescendantExpression(LinkedListTree target,
	                                                     LinkedListTree selector)
	{
		LinkedListToken op = TokenBuilder.newE4XDescendant();
		LinkedListTree ast = ASTUtils.newAST(op);
		assertNoParent("left-hand expression", target);
		// TODO: is this needed..?
		if (precidence(ast) < precidence(target)) {
			target = parenthise(target);
		}
		if (precidence(ast) < precidence(selector)) {
			selector = parenthise(selector);
		}
		// don't use addChildWithTokens(); special handling below,
		ast.addChild(target);
		ast.addChild(selector);
		target.getStopToken().setNext(op);
		selector.getStartToken().setPrev(op);
		ast.setStartToken(target.getStartToken());
		ast.setStopToken(selector.getStopToken());
		return ast;
	}

	public static LinkedListTree newFilterExpression(LinkedListTree target,
	                                                 LinkedListTree selector)
	{
		LinkedListTree ast = ASTUtils.newImaginaryAST(AS3Parser.E4X_FILTER);
		assertNoParent("target expression", target);
		assertNoParent("query expression", target);
		// don't use addChildWithTokens(); special handling below,
		ast.addChildWithTokens(target);
		ast.appendToken(TokenBuilder.newDot());
		ast.appendToken(TokenBuilder.newLParen());
		ast.addChildWithTokens(selector);
		ast.appendToken(TokenBuilder.newRParen());
		return ast;
	}

	public static LinkedListTree newStarAttribute() {
		LinkedListTree ast = ASTUtils.newImaginaryAST(AS3Parser.E4X_ATTRI_STAR);
		ast.appendToken(TokenBuilder.newAt());
		ast.appendToken(TokenBuilder.newStar());
		return ast;
	}

	public static LinkedListTree newPropertyAttribute(String propertyName) {
		LinkedListTree ast = ASTUtils.newImaginaryAST(AS3Parser.E4X_ATTRI_PROPERTY);
		ast.appendToken(TokenBuilder.newAt());
		LinkedListTree prop = AS3FragmentParser.parseQualifiedIdent(propertyName);
		ast.addChildWithTokens(prop);
		return ast;
	}

	public static LinkedListTree newExpressionAttribute(LinkedListTree expr) {
		LinkedListTree ast = ASTUtils.newImaginaryAST(AS3Parser.E4X_ATTRI_EXPR);
		ast.appendToken(TokenBuilder.newAt());
		ast.appendToken(TokenBuilder.newLBrack());
		ast.addChildWithTokens(expr);
		ast.appendToken(TokenBuilder.newRBrack());
		return ast;
	}

	public static LinkedListTree newString(String value) {
		return ASTUtils.newAST(AS3Parser.STRING_LITERAL, ActionScriptFactory.str(value));
	}

	public static LinkedListTree newDefaultXMLNamespace(LinkedListTree namespace) {
		LinkedListTree ast = ASTUtils.newImaginaryAST(AS3Parser.DEFAULT_XML_NAMESPACE);
		ast.appendToken(TokenBuilder.newDefault());
		ast.appendToken(TokenBuilder.newSpace());
		ast.appendToken(TokenBuilder.newXml());
		ast.appendToken(TokenBuilder.newSpace());
		ast.appendToken(TokenBuilder.newNamespace());
		ast.appendToken(TokenBuilder.newSpace());
		ast.appendToken(TokenBuilder.newAssign());
		ast.appendToken(TokenBuilder.newSpace());
		ast.addChildWithTokens(namespace);
		ast.appendToken(TokenBuilder.newSemi());
		return ast;
	}
}
