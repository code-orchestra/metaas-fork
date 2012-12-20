/*
 * AS3FragmentParser.java
 * 
 * Copyright (c) 2007 David Holroyd
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

import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.SyntaxException;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;


public class AS3FragmentParser {

	public static LinkedListTree parseStatement(String statement) {
		// We need to add a ';' so that LA(2) at the end of an
		// expression-statement will not unexpectedly find EOF. we add
		// a second ';' so that the trailing ';' can simply be ommitted
		// by the caller
		// TODO: assert that there really is no extra code trailing the
		//       parsed statement
		AS3Parser parser = ASTUtils.parse(statement+";;");
		LinkedListTree stmt;
		try {
			stmt = tree(parser.statement());
		} catch (RecognitionException e) {
			throw ASTUtils.buildSyntaxException(statement, parser, e);
		}
		return stmt;
	}


	public static LinkedListTree parseExprStatement(String expr) {
		AS3Parser parser = ASTUtils.parse(expr + ";");
		try {
			return tree(parser.expressionStatement());
		} catch (RecognitionException e) {
			throw ASTUtils.buildSyntaxException(expr, parser, e);
		}
	}

	/**
	 * Attempts to parse the given string as an AS3 expression for use as
	 * a parenthesised conditional or with-statement parameter (the
	 * surronding parenthesis should not be included).
	 * 
	 * @throws SyntaxException if the given text doesn't form a valid
	 * expression.
	 */
	public static LinkedListTree parseCondition(String expr) {
		AS3Parser parser = ASTUtils.parse("(" + expr + ")");
		try {
			return tree(parser.condition());
		} catch (RecognitionException e) {
			throw new SyntaxException("invalid condition "+ActionScriptFactory.str(expr), e);
		}
	}

	/**
	 * Attempts to parse the given string as an AS3 expression.
	 * 
	 * @throws SyntaxException if the given text doesn't form a valid
	 * expression.
	 */
	public static LinkedListTree parseExpr(String expr) {
		AS3Parser parser = ASTUtils.parse(expr + ")");
		try {
			LinkedListTree result = tree(parser.expression());
			ensureRemainingInputIs(parser.getTokenStream(), AS3Parser.RPAREN, expr);
			// zap the trailing ')'
			result.getStopToken().setNext(null);
			return result;
		} catch (RecognitionException e) {
			throw new SyntaxException("invalid expression "+ActionScriptFactory.str(expr), e);
		}
	}

	/**
	 * Attempts to parse the given string as an AS3 expression list.
	 * 
	 * @throws SyntaxException if the given text doesn't form a valid
	 * expression.
	 */
	public static LinkedListTree parseExprList(String expr) {
		AS3Parser parser = ASTUtils.parse(expr + ")");
		try {
			return tree(parser.expressionList());
		} catch (RecognitionException e) {
			throw new SyntaxException("invalid expression-list "+ActionScriptFactory.str(expr), e);
		}
	}

	/**
	 * Helper to access the AST produced by a parser production
	 */
	public static LinkedListTree tree(ParserRuleReturnScope returnScope) {
		return (LinkedListTree)returnScope.getTree();
	}

	/**
	 * Parses the given value, returning an IDENT node.
	 * 
	 * @throws SyntaxException if the given text doesn't form a valid
	 * identifier.
	 */
	public static LinkedListTree parseIdent(String value) {
		AS3Parser parser = ASTUtils.parse(value);
		try {
			LinkedListTree result = tree(parser.identifier());
			parser.endOfFile();
			return result;
		} catch (RecognitionException e) {
			throw new SyntaxException("invalid identifier "+ActionScriptFactory.str(value), e);
		}
	}

	public static LinkedListTree parseParameterDefault(String value) {
		AS3Parser parser = ASTUtils.parse("="+value);
		LinkedListTree def;
		try {
			def = tree(parser.parameterDefault());
		} catch (RecognitionException e) {
			throw new SyntaxException(e.getMessage());
		}
		return def;
	}

	public static LinkedListTree parseForInit(String expr) {
		AS3Parser parser = ASTUtils.parse(expr + ";");
		try {
			LinkedListTree result = tree(parser.forInit());
			ensureRemainingInputIs(parser.getTokenStream(), AS3Parser.SEMI, expr);
			return result;
		} catch (RecognitionException e) {
			throw new SyntaxException(e);
		}
	}

	public static LinkedListTree parseForCond(String expr) {
		AS3Parser parser = ASTUtils.parse(expr + ";");
		try {
			LinkedListTree result = tree(parser.forCond());
			ensureRemainingInputIs(parser.getTokenStream(), AS3Parser.SEMI, expr);
			return result;
		} catch (RecognitionException e) {
			throw new SyntaxException(e);
		}
	}

	public static LinkedListTree parseForIter(String expr) {
		AS3Parser parser = ASTUtils.parse(expr + ")");
		try {
			LinkedListTree result = tree(parser.forIter());
			ensureRemainingInputIs(parser.getTokenStream(), AS3Parser.RPAREN, expr);
			return result;
		} catch (RecognitionException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * Given the type name to import, creates an IMPORT AST node
	 */
	public static LinkedListTree parseImport(String name) {
		AS3Parser parser = ASTUtils.parse("import "+name+";");
		try {
			LinkedListTree result = tree(parser.importDefinition());
			parser.endOfFile();  // assert no trailing data
			return result;
		} catch (RecognitionException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * Parses the given value as an IDENT, using it to construct a new
	 * TYPE_SPEC node.
	 * 
	 * @throws SyntaxException if the given text doesn't form a valid
	 * identifier.
	 */
	public static LinkedListTree parseTypeSpec(String value) {
		AS3Parser parser = ASTUtils.parse(":" + value);
		try {
			LinkedListTree result = tree(parser.typeExpression());
			ensureNoMoreInput(parser.getTokenStream(), value);
			return result;
		} catch (RecognitionException e) {
			throw new SyntaxException("invalid type-spec "+ActionScriptFactory.str(value), e);
		}
	}

	private static void ensureNoMoreInput(TokenStream input, String value) {
		if (input.LA(1) != AS3Parser.EOF) {
			throw new SyntaxException("Unexpected tokens in input: "+value);
		}
	}

	private static void ensureRemainingInputIs(TokenStream input, int expectedTokenType, String value) {
		if (input.LA(1) != expectedTokenType) {
			throw new SyntaxException("Unexpected tokens in input: "+value);
		}
	}

	public static LinkedListTree parseForInVar(String expr) {
		AS3Parser parser = ASTUtils.parse(expr + " in");
		try {
			return tree(parser.forInClauseDecl());
		} catch (RecognitionException e) {
			throw new SyntaxException(e);
		}
	}

	public static LinkedListTree parseForInIterated(String expr) {
		AS3Parser parser = ASTUtils.parse(expr + ")");
		try {
			return tree(parser.forInClauseTail());
		} catch (RecognitionException e) {
			throw new SyntaxException(e);
		}
	}

	public static LinkedListTree parseVariableDeclarator(String assignment) {
		AS3Parser parser = ASTUtils.parse(assignment + ";");
		try {
			return tree(parser.variableDeclarator());
		} catch (RecognitionException e) {
			throw new SyntaxException(e);
		}
	}


	public static LinkedListTree parseSimpleIdent(String value) {
		AS3Parser parser = ASTUtils.parse(value);
		try {
			LinkedListTree result = tree(parser.ident());
			parser.endOfFile();
			return result;
		} catch (RecognitionException e) {
			throw new SyntaxException("invalid identifier "+ActionScriptFactory.str(value), e);
		}
	}

	public static LinkedListTree parseXMLLiteral(String value) {
		AS3Parser parser = ASTUtils.parse(value+" ");
		try {
			LinkedListTree result = tree(parser.xmlLiteral());
			parser.endOfFile();
			return result;
		} catch (RecognitionException e) {
			throw ASTUtils.buildSyntaxException(value, parser, e);
		}
	}


	public static LinkedListTree parseRegexpLiteral(String value) {
		AS3Parser parser = ASTUtils.parse(value + " ");
		try {
			LinkedListTree result = tree(parser.regexpLiteral());
			parser.endOfFile();
			return result;
		} catch (RecognitionException e) {
			throw ASTUtils.buildSyntaxException(value, parser, e);
		}
	}


	public static LinkedListTree parseQualifiedIdent(String value) {
		AS3Parser parser = ASTUtils.parse(value);
		try {
			LinkedListTree result = tree(parser.qualifiedIdent());
			parser.endOfFile();
			return result;
		} catch (RecognitionException e) {
			throw new SyntaxException("invalid identifier "+ActionScriptFactory.str(value), e);
		}
	}
}