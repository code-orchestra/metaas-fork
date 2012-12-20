/*
 * ASTUtils.java
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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.runtime.*;
import org.asdt.core.internal.antlr.AS3Lexer;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.EmptySourceFileException;
import uk.co.badgersinfoil.metaas.SyntaxException;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.TypeDescriptor;
import uk.co.badgersinfoil.metaas.impl.antlr.BasicListUpdateDelegate;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTokenSource;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTokenStream;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTreeAdaptor;
import uk.co.badgersinfoil.metaas.impl.antlr.TreeTokenListUpdateDelegate;


/**
 * A collection of helper methods for dealing with AST nodes.
 */
public class ASTUtils {
	
	public static final LinkedListTreeAdaptor TREE_ADAPTOR = new LinkedListTreeAdaptor();
    private static final String END_OF_FILE = "<end-of-file>";

    static {
		TREE_ADAPTOR.setFactory(new LinkedListTreeAdaptor.Factory() {
			private BasicListUpdateDelegate basicDelegate = new BasicListUpdateDelegate();
			private ParentheticListUpdateDelegate blockDelegate = new ParentheticListUpdateDelegate(AS3Parser.LCURLY, AS3Parser.RCURLY);
			private ParentheticListUpdateDelegate metadataTagDelegate = new ParentheticListUpdateDelegate(AS3Parser.LBRACK, AS3Parser.RBRACK);
			private ParentheticListUpdateDelegate paramsDelegate = new ParentheticListUpdateDelegate(AS3Parser.LPAREN, AS3Parser.RPAREN);
			public TreeTokenListUpdateDelegate create(LinkedListTree payload) {
				if (payload.isNil()) {
					return basicDelegate;
				}
				switch (payload.getType()) {
				    case AS3Parser.BLOCK:
				    case AS3Parser.TYPE_BLOCK:
				    case AS3Parser.OBJECT_LITERAL:
					return blockDelegate;
				    case AS3Parser.ANNOTATION:
				    case AS3Parser.ARRAY_LITERAL:
					return metadataTagDelegate;
				    case AS3Parser.PARAMS:
				    case AS3Parser.ANNOTATION_PARAMS:
				    case AS3Parser.ARGUMENTS:
				    case AS3Parser.ENCPS_EXPR:
				    case AS3Parser.CONDITION:
					return paramsDelegate;
				    default:
					return basicDelegate;
				}
			}
		});
	}

	/**
	 * Stringifies the given IDENTIFIER node.
	 */
	public static String identText(LinkedListTree ast) {
		if (ast.getType() != AS3Parser.IDENTIFIER) {
			throw new IllegalArgumentException("expected IDENTIFIER, but token was a " + tokenName(ast));
		}
		return stringifyIdentAux((LinkedListTree)ast.getChild(0));
	}

    public static int findLineNumber(LinkedListTree ast) {
        LinkedListTree childWithALine = getFirstChildWithALineNumber(ast);
        if (childWithALine != null) {
            return childWithALine.getLine();
        }
        return -1;
    }

    public static LinkedListTree getFirstChildWithALineNumber(LinkedListTree ast) {
        for (int i = 0; i < ast.getChildCount(); i++) {
            Object treeObj = ast.getChild(i);
            if (treeObj instanceof LinkedListTree) {
                LinkedListTree subTree = (LinkedListTree) treeObj;
                if (subTree.getLine() > 0) {
                    return subTree;
                }
            }
        }
        return null;
    }

	private static String stringifyIdentAux(LinkedListTree ast) {
		StringBuffer result = new StringBuffer();
		if (ast.getType()==AS3Parser.DBL_COLON) {
			result.append(ast.getFirstChild());
			result.append("::");
			result.append(ast.getLastChild());
		} else if (ast.getType()==AS3Parser.PROPERTY_OR_IDENTIFIER
		           || ast.getType()==AS3Parser.DOT)
		{
			result.append(stringifyIdentAux(ast.getFirstChild()));
			result.append(".");
			result.append(stringifyIdentAux(ast.getLastChild()));
		} else {
			result.append(ast.getText());
		}
		return result.toString();
	}

	public static String qualifiedIdentText(LinkedListTree ast) {
		if (ast.getType()==AS3Parser.DBL_COLON) {
			return ast.getFirstChild() + "::" + ast.getLastChild();
		}
		return ast.getText();
	}

	public static String identStarText(LinkedListTree ast) {
		if (ast.getType() != AS3Parser.IDENTIFIER_STAR) {
			throw new IllegalArgumentException("expected IDENTIFIER_STAR, but token was a " + tokenName(ast));
		}
		StringBuffer result = new StringBuffer();
		for (int i=0; i<ast.getChildCount(); i++) {
			LinkedListTree part = (LinkedListTree)ast.getChild(i);
			if (result.length() > 0) {
				result.append(".");
			}
			result.append(part.getText());
		}
		return result.toString();
	}

	/**
	 * Stringifies the type name from the given TYPE_SPEC node.
	 */
	public static String typeSpecText(LinkedListTree ast) {
		if (ast.getType() == AS3Parser.VECTOR) {
            return "Vector";
        }
        if (ast.getType() != AS3Parser.TYPE_SPEC) {
			throw new IllegalArgumentException("expected TYPE_SPEC or VECTOR, but token was a " + tokenName(ast));
		}
		LinkedListTree type = ast.getFirstChild();
		if (type.getType() == AS3Parser.IDENTIFIER) {
			return identText(type);
		}
		// handle e.g. "void",
		return type.getText();
	}

    /**
	 * Stringifies the type name from the given TYPE_SPEC node.
	 */
	public static TypeDescriptor typeSpecParameter(LinkedListTree ast) {
        if (ast.getType() != AS3Parser.TYPE_SPEC && ast.getType() != AS3Parser.VECTOR) {
			throw new IllegalArgumentException("expected TYPE_SPEC or VECTOR, but token was a " + tokenName(ast));
		}
		if (ast.getChildCount() == 1 && ast.getType() == AS3Parser.VECTOR) {
          return new TypeDescriptorImpl(ast.getFirstChild());  
        } else if (ast.getChildCount() == 2 && ast.getType() == AS3Parser.TYPE_SPEC) {
          return new TypeDescriptorImpl(ast.getLastChild());
        } else {
           return null;
        }
	}

	/**
	 * Helper for constructing error messages
	 */
	public static String tokenName(LinkedListTree ast) {
		return tokenName(ast.getType());
	}

	/**
	 * Helper for constructing error messages
	 */
	public static String tokenName(int type) {
		if (type == -1) {
			return "<EOF>";
		}
		return AS3Parser.tokenNames[type];
	}

	/**
	 * Returns the first child of the given AST node which has the given
	 * type, or null, if no such node exists.
	 */
	public static LinkedListTree findChildByType(LinkedListTree ast, int type) {
		return (LinkedListTree)ast.getFirstChildWithType(type);
	}

    public static List<LinkedListTree> findChildrenByType(LinkedListTree ast, int type) {
        List<LinkedListTree> result = new ArrayList<LinkedListTree>();

        for (int i = 0; i < ast.getChildCount(); i++) {
            Object astObj = ast.getChild(i);
            if (astObj instanceof LinkedListTree) {
               LinkedListTree subTree = (LinkedListTree) astObj;
               if (subTree.getType() == type) {
                   result.add(subTree);
               }
            }
        }

        return result;
    }

	/**
	 * Returns an ActionScript3 parser which will recognise input from the
	 * given string.
	 */
	public static AS3Parser parse(String text) {
		return parse(new StringReader(text));
	}

	/**
	 * Returns an ActionScript3 parser which will recognise input from the
	 * given reader.
	 */
	public static AS3Parser parse(Reader in) {
		ANTLRReaderStream cs;
		try {
			cs = new ANTLRReaderStream(in);
		} catch (IOException e) {
			throw new SyntaxException(e);
		}
		AS3Lexer lexer = new AS3Lexer(cs);
		LinkedListTokenSource linker = new LinkedListTokenSource(lexer);
		LinkedListTokenStream tokenStream = new LinkedListTokenStream(linker);
		AS3Parser parser = new AS3Parser(tokenStream);
		parser.setInput(lexer, cs);
		parser.setTreeAdaptor(TREE_ADAPTOR);
		return parser;
	}

    public static SyntaxException buildSyntaxException(String statement,
	                                                   AS3Parser parser,
	                                                   RecognitionException e) {
        return buildSyntaxException(statement, parser, e, null);
    }

	public static SyntaxException buildSyntaxException(String statement,
	                                                   AS3Parser parser,
	                                                   RecognitionException e, Token token)
	{
		String msg;
		if (e instanceof NoViableAltException) {
			NoViableAltException ex = (NoViableAltException)e;

            // RE-320
            String tokenName = tokenName(parser, ex.getUnexpectedType());
            if (END_OF_FILE.equals(tokenName)) {
                return new EmptySourceFileException();
            }

            msg = "Unexpected token "+ tokenName;
			if (statement != null) {
				msg += " in "+ActionScriptFactory.str(statement);
			}

            if (ex.getUnexpectedType() == AS3Parser.IDENT && token != null) {
                msg += " with text [" + token.getText() + "]";
            }
		} else if (e instanceof MismatchedTokenException) {
			MismatchedTokenException ex = (MismatchedTokenException)e;
			msg = "Unexpected token "+tokenName(parser, ex.getUnexpectedType())+" (expecting "+tokenName(parser, ex.expecting)+")";
			if (statement != null) {
				msg += " in "+ActionScriptFactory.str(statement);
			}
		} else {
			if (statement == null) {
				msg = "";
			} else {
				msg = "Problem parsing "+ActionScriptFactory.str(statement);
			}
		}
		msg += " at line " + e.line;
		return new SyntaxException(msg, e, e.line);
	}

	private static String tokenName(AS3Parser parser, int type) {
		if (type == -1) {
			return END_OF_FILE;
		}
		return parser.getTokenNames()[type];
	}

	/**
	 * Constructs a new AST of the given type, initialized to contain
	 * text matching the token's name (use for non-literals only).
	 * @deprecated
	 */
	public static LinkedListTree newAST(int type) {
		return newImaginaryAST(type);
	}

	/**
	 * Constructs a new AST of the given type, initialized to contain
	 * text matching the token's name (use for non-literals only).
	 */
	public static LinkedListTree newImaginaryAST(int type) {
		return (LinkedListTree)TREE_ADAPTOR.create(type, tokenName(type));
	}

	public static LinkedListTree newPlaceholderAST(int type) {
		LinkedListTree node = newImaginaryAST(type);
		LinkedListToken placeholder = TokenBuilder.newPlaceholder(node);
		return node;
	}

	/**
	 * Constructs a new AST of the given type, initialized to contain the
	 * given text.
	 */
	public static LinkedListTree newAST(int type, String text) {
		LinkedListToken tok = TokenBuilder.newToken(type, text);
		LinkedListTree ast = (LinkedListTree)TREE_ADAPTOR.create(tok);
		return ast;
	}

	public static LinkedListTree newAST(LinkedListToken tok) {
		return (LinkedListTree)TREE_ADAPTOR.create(tok);
	}

	public static LinkedListTree newParentheticAST(int type,
	                                                int startType,
	                                                String startText,
	                                                int endType,
	                                                String endText) {
		LinkedListTree ast = newImaginaryAST(type);
		LinkedListToken start = TokenBuilder.newToken(startType, startText);
		ast.setStartToken(start);
		LinkedListToken stop = TokenBuilder.newToken(endType, endText);
		ast.setStopToken(stop);
		start.setNext(stop);
		ast.setInitialInsertionAfter(start);
		return ast;
	}

	public static void increaseIndent(LinkedListTree node, String indent) {
		LinkedListToken newStart = increaseIndentAt(node.getStartToken(), indent);
		node.setStartToken(newStart);
		increaseIndentAfterFirstLine(node, indent);
	}

	public static void increaseIndentAfterFirstLine(LinkedListTree node, String indent) {
		for (LinkedListToken tok=node.getStartToken() ; tok!=node.getStopToken(); tok=tok.getNext()) {
			switch (tok.getType()) {
			case AS3Parser.NL:
				tok = increaseIndentAt(tok.getNext(), indent);
				break;
			case AS3Parser.ML_COMMENT:
				DocCommentUtils.increaseCommentIndent(tok, indent);
				break;
			}
		}
	}

	private static LinkedListToken increaseIndentAt(LinkedListToken tok, String indentStr) {
		if (tok.getType() == AS3Parser.WS) {
			tok.setText(indentStr + tok.getText());
			return tok;
		}
		LinkedListToken indent = TokenBuilder.newWhiteSpace(indentStr);
		tok.beforeInsert(indent);
		return indent;
	}

	/**
	 * Inspects the whitespace preceeding the given tree node to try and
	 * determine its level of indentation.  Scans backwards from the
	 * startToken and returns the contents of the first whitespace token
	 * following a newline token, or the empty string if no such pattern
	 * is found.
	 */
	public static String findIndent(LinkedListTree node) {
		LinkedListToken tok = node.getStartToken();
		if (tok == null) {
			return findIndent(node.getParent());
		}
		// the start-token of this AST node is actually whitespace, so
		// scan forward until we hit a non-WS token,
		while (tok.getType()==AS3Parser.NL || tok.getType()==AS3Parser.WS) {
			if (tok.getNext() == null) {
				break;
			}
			tok = tok.getNext();
		}
		// search backwards though the tokens, looking for the start of
		// the line,
		for (; tok.getPrev()!=null; tok=tok.getPrev()) {
			if (tok.getType() == AS3Parser.NL) {
				break;
			}
		}
		if (tok.getType() == AS3Parser.WS) {
			return tok.getText();
		}
		if (tok.getType()!=AS3Parser.NL) {
			return "";
		}
		LinkedListToken startOfLine = tok.getNext();
		if (startOfLine.getType() == AS3Parser.WS) {
			return startOfLine.getText();
		}
		return "";
	}

	public static void addChildWithIndentation(LinkedListTree ast, LinkedListTree stmt) {
		LinkedListTree last = ast.getLastChild();
		String indent;
		if (last == null) {
			indent = "\t" + ASTUtils.findIndent(ast);
		} else {
			indent = ASTUtils.findIndent(last);
		}
		ASTUtils.increaseIndent(stmt, indent);
		stmt.addToken(0, TokenBuilder.newNewline());
		ast.addChildWithTokens(stmt);
	}

	public static void addChildWithIndentation(LinkedListTree ast, int index, LinkedListTree stmt) {
		LinkedListTree last = (LinkedListTree)ast.getChild(index);
		String indent;
		if (last == null) {
			indent = "\t" + ASTUtils.findIndent(ast);
		} else {
			indent = ASTUtils.findIndent(last);
		}
		ASTUtils.increaseIndent(stmt, indent);
		stmt.addToken(0, TokenBuilder.newNewline());
		ast.addChildWithTokens(index, stmt);
	}

	public static String decodeStringLiteral(String str) {
		StringBuffer result = new StringBuffer();
		char[] s = str.toCharArray();
		int end = s.length - 1;
		if (s[0] != '"' && s[0] != '\'') {
			throw new SyntaxException("Invalid delimiter at position 0: "+s[0]);
		}
		char delimiter = s[0];
		for (int i=1; i<end; i++) {
			char c = s[i];
			switch (c) {
			case '\\':
				c = s[++i];
				switch (c) {
				case 'n':
					result.append('\n');
					break;
				case 't':
					result.append('\t');
					break;
				case '\\':
					result.append('\\');
					break;
				default:
					result.append(c);
				}
				break;
			default:
				result.append(c);
			}
		}
		if (s[end] != delimiter) {
			throw new SyntaxException("End delimiter doesn't match "+delimiter+" at position "+end);
		}
		return result.toString();
	}

	public static LinkedListToken newStringLiteral(String constant) {
		return new LinkedListToken(AS3Parser.STRING_LITERAL, ActionScriptFactory.str(constant));
	}

	/**
	 * Deletes any whitespace tokens following (but not including) the given
	 * token up to a comma token, and then deletes the comma token too.
	 * 
	 * Used when removing elements from comma-seperated lists.
	 */
	public static void removeTrailingWhitespaceAndComma(LinkedListToken stopToken) {
		for (LinkedListToken tok = stopToken.getNext(); tok!=null; tok=tok.getNext()) {
			if (tok.getChannel() == AS3Parser.HIDDEN) {
				// TODO: this might be incorrect (but never called?) see code in removePreceeding...
				tok.delete();
			} else if (tok.getType() == AS3Parser.COMMA) {
				tok.delete();
				break;
			} else {
				throw new SyntaxException("Unexpected token: "+tok);
			}
		}
	}

	public static void removePreceedingWhitespaceAndComma(LinkedListToken startToken) {
		for (LinkedListToken tok = startToken.getPrev(); tok!=null; tok=tok.getPrev()) {
			if (tok.getChannel() == AS3Parser.HIDDEN) {
				LinkedListToken del = tok;
				tok = tok.getNext();
				del.delete();
				continue;
			} else if (tok.getType() == AS3Parser.COMMA) {
				tok.delete();
				break;
			} else {
				throw new SyntaxException("Unexpected token: "+tok);
			}
		}
	}
	
	public static void assertAS3TokenTypeIs(int expected, int actual) {
		if (expected != actual) {
			throw new SyntaxException("Expected "+tokenName(expected)+", got "+tokenName(actual));
		}
	}
	
	public static void assertAS3TokenTypeIs(String msg, int expected, int actual) {
		if (expected != actual) {
			throw new SyntaxException(msg+" Expected "+tokenName(expected)+", got "+tokenName(actual));
		}
	}

	public static String stringifyNode(LinkedListTree ast) {
		StringBuffer result = new StringBuffer();
		for (LinkedListToken tok=ast.getStartToken(); tok!=null&&tok.getType()!=-1; tok=tok.getNext()) {
			result.append(tok.getText());
			if (tok == ast.getStopToken()) {
				break;
			}
		}
		return result.toString();
	}

	public static void deleteAllChildren(LinkedListTree ast) {
		while (ast.getChildCount() > 0) {
			ast.deleteChild(0);
		}
	}

	public static LinkedListTree ast(Expression expr) {
		return ((ASTExpression)expr).getAST();
	}

    public static boolean lineBreakNearToken(LinkedListTree ast) {

        LinkedListToken rootToken = null;

        Token token = ast.getToken();
        if (token != null) {
            if (token instanceof LinkedListToken) {
                rootToken = (LinkedListToken) token;
            } else {
                LinkedListToken t = ast.getStartToken();
                LinkedListToken stopToken = ast.getStopToken();
                if (t != null && stopToken != null) {
                    t = t.getNext();
                    while (t != stopToken) {
                        if (token.getLine() == t.getLine() && token.getCharPositionInLine() == t.getCharPositionInLine()) {
                            rootToken = t;
                            break;
                        }
                        t = t.getNext();
                    }
                }
            }
        }

        if (rootToken == null) {
            // This case is not critical error, so we do not interrupt parsing
            return false;
        }

        LinkedListToken tokenToCheck = rootToken.getNext();
        
        while (tokenToCheck.getChannel() == 99) {
            if (tokenToCheck.getText().contains("\n") || tokenToCheck.getText().contains("\r")) {
                return true;
            }
            tokenToCheck = tokenToCheck.getNext();
        }

        tokenToCheck = rootToken.getPrev();
        while (tokenToCheck.getChannel() == 99) {
            if (tokenToCheck.getText().contains("\n") || tokenToCheck.getText().contains("\r")) {
                return true;
            }
            tokenToCheck = tokenToCheck.getPrev();
        }

        return false;
    }

    public static List<Expression> getArrayEntries(LinkedListTree ast) {
        List<Expression> entries = new ArrayList<Expression>();
        ASTIterator i = new ASTIterator(ast);
        while (i.hasNext()) {
            LinkedListTree tree = i.next();
            if (tree.getType() == AS3Parser.EMPTY_LIST_ITEM) {
                // It skips last empty item
                if (i.hasNext()) {
                    entries.add(new ASTASNullLiteral(tree));
                }
                continue;
            }
            entries.add(ExpressionBuilder.build(tree));
        }
        return Collections.unmodifiableList(entries);
    }
}