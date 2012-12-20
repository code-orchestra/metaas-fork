/*
 * ASSourceFactory.java
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

package uk.co.badgersinfoil.metaas;

import java.util.List;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

// TODO: an isKeyword(String) test would be nice

/**
 * Core class providing access to metaas functionality.
 */
public class ActionScriptFactory {

	/**
	 * Creates a new CompilationUnit which defines a class with the given
	 * name.  To populate the new class, you can do something like,
	 * <pre>
	 * CompilationUnit cu = fact.newClass("MyTest");
	 * ASTClassType myclass = (ASTClassType)cu.getType();
	 * // ... add stuff to myclass  ...
	 * </pre>
	 */
	public ASCompilationUnit newClass(String qualifiedClassName) {
		return ASTBuilder.synthesizeClass(qualifiedClassName);
	}

	/**
	 * Creates a new CompilationUnit which defines an interface with the
	 * given name.  To populate the new interface, you can do something
	 * like,
	 * <pre>
	 * CompilationUnit cu = fact.newClass("MyTest");
	 * ASTClassType myiface = (ASTInterfaceType)cu.getType();
	 * // ... add stuff to myiface  ...
	 * </pre>
	 */
	public ASCompilationUnit newInterface(String qualifiedInterfaceName) {
		return ASTBuilder.synthesizeInterface(qualifiedInterfaceName);
	}

	public ActionScriptWriter newWriter() {
		return new ASTActionScriptWriter();
	}
	
	public ActionScriptParser newParser() {
		return new ASTActionScriptParser();
	}

	/**
	 * Escape the given String and place within double quotes so that it
	 * will be a valid ActionScript string literal.
	 */
	public static String str(String str) {
		StringBuffer result = new StringBuffer("\"");
		for (int i=0; i<str.length(); i++) {
			char c = str.charAt(i);
			switch (c) {
			    case '\n':
				result.append("\\n");
				break;
			    case '\t':
				result.append("\\t");
				break;
			    case '\r':
				result.append("\\r");
				break;
			    case '"':
				result.append("\\\"");
				break;
			    case '\\':
				result.append("\\\\");
				break;
			    default:
				result.append(c);
			}
		}
		result.append('"');
		return result.toString();
	}

	/**
	 * Creates a new ActionScript block statement.  Can be supplied to an
	 * {@link uk.co.badgersinfoil.metaas.dom.ASIfStatement}, for instance.
	 */
	public ASBlock newBlock() {
		LinkedListTree block = ASTBuilder.newBlock();
		return new ASTStatementList(block);
	}

	public ActionScriptProject newEmptyASProject(String outputLocation) {
		ASTActionScriptProject project = new ASTActionScriptProject(this);
		project.setOutputLocation(outputLocation);
		return project;
	}

	public ASStringLiteral newStringLiteral(String string) {
		return new ASTASStringLiteral(ASTUtils.newAST(AS3Parser.STRING_LITERAL, str(string)));
	}

	public ASIntegerLiteral newIntegerLiteral(int i) {
		return new ASTASIntegerLiteral(ASTUtils.newAST(AS3Parser.DECIMAL_LITERAL, String.valueOf(i)));
	}

	public ASNullLiteral newNullLiteral() {
		return new ASTASNullLiteral(ASTUtils.newAST(AS3Parser.NULL, "null"));
	}

	public ASBooleanLiteral newBooleanLiteral(boolean b) {
		LinkedListTree ast = b ? ASTUtils.newAST(AS3Parser.TRUE, "true")
		                       : ASTUtils.newAST(AS3Parser.FALSE, "false");
		return new ASTASBooleanLiteral(ast);
	}

	public ASUndefinedLiteral newUndefinedLiteral() {
		return new ASTASUndefinedLiteral(ASTUtils.newAST(AS3Parser.UNDEFINED, "undefined"));
	}

	public ASBinaryExpression newAddExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newPlus(), left, right);
	}

	public ASBinaryExpression newAndExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newAnd(), left, right);
	}

	public ASBinaryExpression newBitAndExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newBitAnd(), left, right);
	}

	public ASBinaryExpression newBitOrExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newBitOr(), left, right);
	}

	public ASBinaryExpression newBitXorExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newBitXor(), left, right);
	}

	public ASBinaryExpression newDivisionExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newDiv(), left, right);
	}

	public ASBinaryExpression newEqualsExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newEquals(), left, right);
	}

	public ASBinaryExpression newGreaterEqualsExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newGreaterEquals(), left, right);
	}

	public ASBinaryExpression newGreaterThanExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newGreater(), left, right);
	}

	public ASBinaryExpression newLessEqualsExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newLessEquals(), left, right);
	}

	public ASBinaryExpression newLessThanExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newLess(), left, right);
	}

	public ASBinaryExpression newModuloExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newModulo(), left, right);
	}

	public ASBinaryExpression newMultiplyExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newMult(), left, right);
	}

	public ASBinaryExpression newNotEqualsExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newNotEquals(), left, right);
	}

	public ASBinaryExpression newOrExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newOr(), left, right);
	}

	public ASBinaryExpression newShiftLeftExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newShiftLeft(), left, right);
	}

	public ASBinaryExpression newShiftRightExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newShiftRight(), left, right);
	}

	public ASBinaryExpression newShiftRightUnsignedExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newShiftRightUnsigned(), left, right);
	}

	public ASBinaryExpression newSubtractExpression(Expression left, Expression right) {
		return ASTBuilder.newBinaryExpression(TokenBuilder.newMinus(), left, right);
	}

	/**
	 * 
	 * @throws SyntaxException if the given string is not a vaild
	 *         ActionScript 3 expression.
	 */
	public Expression newExpression(String expr) {
		LinkedListTree ast = AS3FragmentParser.parseExpr(expr);
		// ANTLR creates a 'nil' parent node (in case the result is a
		// list).  We break the link to that parent because we assert
		// the parent is null when child nodes are attached elsewhere
		// in the tree.
		ast.setParent(null);
		return ExpressionBuilder.build(ast);
	}

	private ASPrefixExpression newPrefixExpression(LinkedListToken op, Expression sub) {
		LinkedListTree ast = ASTUtils.newAST(op);
		LinkedListTree subExpr = ast(sub);
		ASTBuilder.assertNoParent("sub-expression", subExpr);
		ast.addChildWithTokens(subExpr);
		return new ASTASPrefixExpression(ast);
	}

	public ASPrefixExpression newPreDecExpression(Expression sub) {
		return newPrefixExpression(TokenBuilder.newPreDec(), sub);
	}

	public ASPrefixExpression newPreIncExpression(Expression sub) {
		return newPrefixExpression(TokenBuilder.newPreInc(), sub);
	}

	public ASPrefixExpression newPositiveExpression(Expression sub) {
		return newPrefixExpression(TokenBuilder.newPlus(), sub);
	}

	public ASPrefixExpression newNegativeExpression(Expression sub) {
		return newPrefixExpression(TokenBuilder.newMinus(), sub);
	}

	public ASPrefixExpression newNotExpression(Expression sub) {
		return newPrefixExpression(TokenBuilder.newNot(), sub);
	}

	private ASPostfixExpression newPostfixExpression(LinkedListToken op, Expression sub) {
		LinkedListTree ast = ASTUtils.newAST(op);
		LinkedListTree subExpr = ast(sub);
		ASTBuilder.assertNoParent("sub-expression", subExpr);
		ast.addChild(subExpr);
		ast.setStartToken(subExpr.getStartToken());
		subExpr.getStopToken().setNext(op);
		return new ASTASPostfixExpression(ast);
	}      

	public ASPostfixExpression newPostDecExpression(Expression sub) {
		return newPostfixExpression(TokenBuilder.newPostDec(), sub);
	}

	public ASPostfixExpression newPostIncExpression(Expression sub) {
		return newPostfixExpression(TokenBuilder.newPostInc(), sub);
	}

	public ASNewExpression newNewExpression(Expression subexpression, List args) {
		LinkedListTree ast = ASTUtils.newAST(AS3Parser.NEW, "new");
		ast.appendToken(TokenBuilder.newSpace());
		LinkedListTree subExpr = ast(subexpression);
		ASTBuilder.assertNoParent("sub-expression", subExpr);
		// TODO: recursively check the given subexpression
		ast.addChildWithTokens(subExpr);
		LinkedListTree arguments = ASTUtils.newParentheticAST(AS3Parser.ARGUMENTS, AS3Parser.LPAREN, "(", AS3Parser.RPAREN, ")");
		ast.addChildWithTokens(arguments);
		ASTASNewExpression result = new ASTASNewExpression(ast);
		result.setArguments(args);
		return result;
	}

	public ASInvocationExpression newInvocationExpression(Expression sub, List args) {
		LinkedListTree ast = ASTUtils.newImaginaryAST(AS3Parser.METHOD_CALL);
		LinkedListTree subExpr = ast(sub);
		ASTBuilder.assertNoParent("sub-expression", subExpr);
		// TODO: recursively check the given subexpression
		ast.addChildWithTokens(subExpr);
		LinkedListTree arguments = ASTUtils.newParentheticAST(AS3Parser.ARGUMENTS, AS3Parser.LPAREN, "(", AS3Parser.RPAREN, ")");
		ast.addChildWithTokens(arguments);
		ASTASInvocationExpression result = new ASTASInvocationExpression(ast);
		result.setArguments(args);
		return result;
	}

	public ASArrayAccessExpression newArrayAccessExpression(Expression target,
	                                                        Expression subscript)
	{
		LinkedListTree ast = ASTUtils.newImaginaryAST(AS3Parser.ARRAY_ACC);
		LinkedListTree targetExpr = ast(target);
		ASTBuilder.assertNoParent("target expression", targetExpr);
		// TODO: recursively check the given subexpression
		ast.addChildWithTokens(targetExpr);
		ast.appendToken(TokenBuilder.newLBrack());
		LinkedListTree subscriptExpr = ast(subscript);
		ASTBuilder.assertNoParent("subscript expression", subscriptExpr);
		ast.addChildWithTokens(subscriptExpr);
		ast.appendToken(TokenBuilder.newRBrack());
		ASTASArrayAccessExpression result = new ASTASArrayAccessExpression(ast);
		return result;
	}

	public ASAssignmentExpression newAssignExpression(Expression left,
	                                                  Expression right)
	{
		return ASTBuilder.newAssignExpression(TokenBuilder.newAssign(),
		                                      left, right);
	}

	public ASAssignmentExpression newAddAssignExpression(Expression left,
	                                                     Expression right)
	{
		return ASTBuilder.newAssignExpression(TokenBuilder.newAddAssign(),
		                                      left, right);
	}

	public ASAssignmentExpression newBitAndAssignExpression(Expression left,
	                                                        Expression right)
	{
		return ASTBuilder.newAssignExpression(TokenBuilder.newBitAndAssign(),
		                                      left, right);
	}

	public ASAssignmentExpression newBitOrAssignExpression(Expression left,
	                                                       Expression right)
	{
		return ASTBuilder.newAssignExpression(TokenBuilder.newBitOrAssign(),
		                                      left, right);
	}

	public ASAssignmentExpression newBitXorAssignExpression(Expression left,
	                                                        Expression right)
	{
		return ASTBuilder.newAssignExpression(TokenBuilder.newBitXorAssign(),
		                                      left, right);
	}

	public ASAssignmentExpression newDivideAssignExpression(Expression left,
	                                                        Expression right)
	{
		return ASTBuilder.newAssignExpression(TokenBuilder.newDivAssign(),
		                                      left, right);
	}

	public ASAssignmentExpression newModuloAssignExpression(Expression left,
	                                                        Expression right)
	{
		return ASTBuilder.newAssignExpression(TokenBuilder.newModAssign(),
		                                      left, right);
	}

	public ASAssignmentExpression newMultiplyAssignExpression(Expression left,
	                                                          Expression right)
	{
		return ASTBuilder.newAssignExpression(TokenBuilder.newMulAssign(),
		                                      left, right);
	}

	public ASAssignmentExpression newShiftLeftAssignExpression(Expression left,
	                                                          Expression right)
	{
		return ASTBuilder.newAssignExpression(TokenBuilder.newSLAssign(),
		                                      left, right);
	}

	public ASAssignmentExpression newShiftRightAssignExpression(Expression left,
	                                                            Expression right)
	{
		return ASTBuilder.newAssignExpression(TokenBuilder.newSRAssign(),
		                                      left, right);
	}

	public ASAssignmentExpression newShiftRightUnsignedAssignExpression(Expression left,
	                                                                    Expression right)
	{
		return ASTBuilder.newAssignExpression(TokenBuilder.newSRUAssign(),
		                                      left, right);
	}

	public ASAssignmentExpression newSubtractAssignExpression(Expression left,
	                                                  Expression right)
	{
		return ASTBuilder.newAssignExpression(TokenBuilder.newSubAssign(),
		                                      left, right);
	}

	public ASFieldAccessExpression newFieldAccessExpression(Expression target, String name) {
		LinkedListTree ast
			= ASTBuilder.newFieldAccessExpression(ast(target),
		                                              ASTUtils.newAST(AS3Parser.IDENT, name));
		return new ASTASFieldAccessExpression(ast);
	}

	public ASConditionalExpression newConditionalExpression(Expression conditionExpr,
	                                                        Expression thenExpr,
	                                                        Expression elseExpr)
	{
		LinkedListTree ast
			= ASTBuilder.newConditionalExpression(ast(conditionExpr),
		                                              ast(thenExpr),
		                                              ast(elseExpr));
		return new ASTASConditionalExpression(ast);
	}

	private static LinkedListTree ast(Expression expr) {
		return ASTUtils.ast(expr);
	}

	public ASFunctionExpression newFunctionExpression() {
		LinkedListTree ast = ASTBuilder.newFunctionExpression();
		return new ASTASFunctionExpression(ast);
	}

	/**
	 * @param entries a list of {@link Expression} objects.
	 */
	public ASArrayLiteral newArrayLiteral() {
		LinkedListTree ast = ASTBuilder.newArrayLiteral();
		return new ASTASArrayLiteral(ast);
	}

	public ASObjectLiteral newObjectLiteral() {
		LinkedListTree ast = ASTBuilder.newObjectLiteral();
		return new ASTASObjectLiteral(ast);
	}

	public ASXMLLiteral newXMLLiteral(String value) {
		LinkedListTree ast = AS3FragmentParser.parseXMLLiteral(value);
		return new ASTASXMLLiteral(ast);
	}

	public ASRegexpLiteral newRegexpLiteral(String value, int flags) {
		StringBuffer flagList = new StringBuffer();
		if ((flags & ASRegexpLiteral.FLAG_DOT_ALL) != 0) {
			flagList.append('s');
		}
		if ((flags & ASRegexpLiteral.FLAG_EXTENDED) != 0) {
			flagList.append('x');
		}
		if ((flags & ASRegexpLiteral.FLAG_GLOBAL) != 0) {
			flagList.append('g');
		}
		if ((flags & ASRegexpLiteral.FLAG_IGNORE_CASE) != 0) {
			flagList.append('i');
		}
		value = regexpEscapeDelimiter(value);
		LinkedListTree ast = AS3FragmentParser.parseRegexpLiteral("/"+value+"/"+flagList);
		return new ASTASRegexpLiteral(ast);
	}

	private static String regexpEscapeDelimiter(String value) {
		StringBuffer result = new StringBuffer();
		for (int i=0; i<value.length(); i++) {
			char c = value.charAt(i);
			switch (c) {
				case '/': result.append("\\/"); break;
				default: result.append(c); break;
			}
		}
		return result.toString();
	}

	public Expression dup(Expression expr) {
		LinkedListTree ast = ASTUtils.ast(expr);
		return ExpressionBuilder.build(ASTBuilder.dup(ast));
	}

	public ASDescendantExpression newDescendantExpression(Expression target,
	                                                      Expression selector)
	{
		LinkedListTree ast
			= ASTBuilder.newDescendantExpression(ast(target),
			                                     ast(selector));
		return new ASTASDescendantExpression(ast);
	}

	public ASFilterExpression newFilterExpression(Expression target,
	                                              Expression selector)
	{
		LinkedListTree ast
			= ASTBuilder.newFilterExpression(ast(target),
			                                 ast(selector));
		return new ASTASFilterExpression(ast);
	}

	public ASStarAttribute newStarAttribute() {
		LinkedListTree ast = ASTBuilder.newStarAttribute();
		return new ASTASStarAttribute(ast);
	}

	public ASPropertyAttribute newPropertyAttribute(String propertyName) {
		LinkedListTree ast = ASTBuilder.newPropertyAttribute(propertyName);
		return new ASTASPropertyAttribute(ast);
	}

	public ASExpressionAttribute newExpressionAttribute(Expression expr) {
		LinkedListTree ast = ASTBuilder.newExpressionAttribute(ast(expr));
		return new ASTASExpressionAttribute(ast);
	}

	public ASSimpleNameExpression newSimpleName(String name) {
		LinkedListTree ast = AS3FragmentParser.parseSimpleIdent(name);
		return new ASTASSimpleNameExpression(ast);
	}
}
