/*
 * DocCommentUtils.java
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

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.regex.Pattern;
import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.RecognitionException;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.SyntaxException;
import uk.co.badgersinfoil.metaas.dom.DocComment;
import uk.co.badgersinfoil.metaas.dom.DocTag;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTokenSource;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTokenStream;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTreeAdaptor;
import uk.co.badgersinfoil.metaas.impl.parser.javadoc.JavadocLexer;
import uk.co.badgersinfoil.metaas.impl.parser.javadoc.JavadocParser;


/**
 * Helpers for dealing with documentation-comments.
 */
class DocCommentUtils {
	private static final LinkedListTreeAdaptor TREE_ADAPTOR = new LinkedListTreeAdaptor();

	private DocCommentUtils() { /* hide ctor */ }

	public static String getDocComment(LinkedListTree node) {
		LinkedListToken tok = findDocCommentToken(node);
		if (tok == null) {
			return null;
		}
		return commentToString(tok);
	}

	public static LinkedListToken findDocCommentToken(LinkedListTree node) {
		LinkedListToken tok=node.getStartToken();
		if (tok == null) {
			return null;
		}
		// find the first proper token,
		outer: while (true) {
			switch (tok.getType()) {
			    case AS3Parser.WS:
			    case AS3Parser.NL:
			    case AS3Parser.ML_COMMENT:
			    case AS3Parser.SL_COMMENT:
				tok = tok.getNext();
				break;
			    default:
				break outer;
			}
		}
		// search backwards from the first proper token until we reach the first ML_COMMENT
		for (tok=tok.getPrev(); tok!=null; tok=tok.getPrev()) {
			switch (tok.getType()) {
			    case AS3Parser.WS:
			    case AS3Parser.NL:
				continue;
			    case AS3Parser.ML_COMMENT:
				if (tok.getText().startsWith("/**")) {
					return tok;
				}
				// fall though,
			    default:
				return null;
			}
		}
		return null;
	}

	// TODO: need to tie comment indentation etc. to the actual current
	//       indentation level.

	private static String commentToString(LinkedListToken tok) {
		return tok.getText().replaceFirst("\\A/\\*+", "")
		                    .replaceFirst("(?:(?<=[\n\r])[ \t]+)?\\*+/\\Z", "")
		                    .replaceAll("([\n\r])\\s*\\*", "$1");
	}

	public static void setDocComment(LinkedListTree node, String text) {
		LinkedListToken tok = findDocCommentToken(node);
		if (text == null) {
			if (tok != null) {
				LinkedListToken next = tok.getNext();
				LinkedListToken prev = tok.getPrev();
				tok.delete();
				// TODO: looks like I didn't finish here,
				if (next.getType() == AS3Parser.NL) {
					next.getNext();
				}
				if (prev.getType() == AS3Parser.WS) {
					prev.getPrev();
				}
			}
			return;
		}
		assertValidContent(text);
		String indent = ASTUtils.findIndent(node);
		text = "/**" + text.replaceAll("(\n|\r\n|\r)", "$1"+indent+" *");
		if (!text.endsWith("*")) {
			 text += "*";
		}
		text += "/";
		if (tok == null) {
			LinkedListToken comment = TokenBuilder.newMLComment(text);

			insertDocComment(node, indent, comment);
		} else {
			tok.setText(text);
		}
	}

	private static void insertDocComment(LinkedListTree node, String indent, LinkedListToken comment) {
		LinkedListToken nl = TokenBuilder.newNewline();

		// TODO: try harder to find the right place/line to
		// insert the comment
		findTokenToInsertCommentBefore(node).beforeInsert(comment);
		comment.afterInsert(nl);
		if (indent.length() > 0) {
			LinkedListToken indentTok = TokenBuilder.newWhiteSpace(indent);
			nl.afterInsert(indentTok);
		}
	}

	private static LinkedListToken findTokenToInsertCommentBefore(LinkedListTree node) {
		LinkedListToken tok = node.getStartToken();
		outer: while (true) {
			switch (tok.getType()) {
				case AS3Parser.WS:
				case AS3Parser.NL:
					tok = tok.getNext();
					break;
				default:
					break outer;
			}
		}
		return tok;
	}

	public static void assertValidContent(String text) {
		int pos = text.indexOf("*/");
		if (pos != -1) {
			throw new SyntaxException("End-on-comment marker found at position "+pos);
		}
	}

	public static void increaseCommentIndent(LinkedListToken tok, String indent) {
		tok.setText(tok.getText().replaceAll("(\n|\r\n|\r)", "$1"+indent));
	}

	public static String getDescriptionString(LinkedListTree ast) {
		LinkedListTree javadoc = buildJavadoc(ast);
		if (javadoc == null) {
			return null;
		}
		LinkedListTree desc = javadoc.getFirstChild();
		return stringify(desc);
	}

	public static LinkedListTree buildJavadoc(LinkedListTree ast) {
		LinkedListToken doc = findDocCommentToken(ast);
		if (doc == null) {
			return null;
		}
		String body = getCommentBody(doc);
		LinkedListTree javadoc = parse(body);
		return javadoc;
	}

	private static String getCommentBody(LinkedListToken doc) {
		String text = doc.getText();
		return text.substring(3, text.length()-2);
	}

	private static String stringify(LinkedListTree ast) {
		StringBuffer result = new StringBuffer();
		for (LinkedListToken tok=ast.getStartToken(); tok!=null&&tok.getType()!=-1; tok=tok.getNext()) {
			result.append(stringify(tok));
			if (tok == ast.getStopToken()) {
				break;
			}
		}
		return result.toString();
	}

	private static String stringify(LinkedListToken tok) {
		switch (tok.getType()) {
			case JavadocParser.NL:
				// TODO: use the original line-ending format
				return "\n";
			default:
				return tok.getText();
		}
	}

	public static void setDescriptionString(LinkedListTree ast, String description) {
		LinkedListToken doc = findDocCommentToken(ast);
		LinkedListTree javadoc = null;
		LinkedListTree desc = null;
		if (doc != null) {
			javadoc = parse(getCommentBody(doc));
			trimEOF(javadoc);
			desc = javadoc.getFirstChild();
		}
		if (description == null) {
			if (desc != null) {
				ASTUtils.deleteAllChildren(desc);
				doc.setText("/**"+ASTUtils.stringifyNode(javadoc)+"*/");
			}
			return;
		}
		assertValidContent(description);
		String newline = getNewlineText(ast, javadoc);
		if (!description.startsWith("\n")) {
			description = "\n" + description;
		}
		description = description.replaceAll("\n", newline);
		LinkedListTree newDesc = parseDescription(description);
		if (doc == null) {
			String indent = ASTUtils.findIndent(ast);
			doc = TokenBuilder.newMLComment("/**"+ASTUtils.stringifyNode(newDesc)+"\n"+indent+" */");
			insertDocComment(ast, indent, doc);
		} else {
			newDesc.appendToken(new LinkedListToken(JavadocParser.NL, newline));
			javadoc.setChildWithTokens(0, newDesc);
			doc.setText("/**"+ASTUtils.stringifyNode(javadoc)+"*/");
		}
	}

	public static String getNewlineText(LinkedListTree ast, LinkedListTree javadoc) {
		String newline = null;
		if (javadoc != null) {
			newline = findNewline(javadoc);
		}
		if (newline == null) {
			newline = "\n"+ASTUtils.findIndent(ast)+" * ";  // TODO: use document existing end-of-line format
		}
		return newline;
	}

	public static String findNewline(LinkedListTree javadoc) {
		LinkedListToken tok=javadoc.getStopToken();
		if (tok.getType() == JavadocParser.NL) {
			// Skip the very-last NL, since this will precede the
			// closing-comment marker, and therefore will lack the
			// '*' that should be present at the start of every
			// other line,
			tok=tok.getPrev();
		}
		for (; tok!=null; tok=tok.getPrev()) {
			if (tok.getType() == JavadocParser.NL) {
				return tok.getText();
			}
		}
		return null;
	}

	private static LinkedListTree parseDescription(String description) {
		try {
			JavadocParser parser = parserOn(description);
			LinkedListTree desc = (LinkedListTree)parser.description().getTree();
			LinkedListToken after = (LinkedListToken)parser.getTokenStream().LT(1);
			if (!isEOF(after)) {
				throw new SyntaxException("trailing content after description: "+ActionScriptFactory.str(after.getText()));
			}
			trimEOF(desc);
			return desc;
		} catch (IOException e) {
			throw new SyntaxException(e);
		} catch (RecognitionException e) {
			throw new SyntaxException(e);
		}
	}

	private static boolean isEOF(LinkedListToken after) {
		return after.getType() == JavadocParser.EOF;
	}

	/**
	 * removes trailing enf-of-file tokens from the AST
	 */
	private static void trimEOF(LinkedListTree desc) {
		LinkedListTree lastChild = desc.getLastChild();
		if (lastChild != null) {
			trimEOF(lastChild);
		}
		while (isEOF(desc.getStopToken())) {
			LinkedListToken stop = desc.getStopToken();
			LinkedListToken prev = stop.getPrev();
			desc.setStopToken(prev);
			stop.delete();
		}
	}

	private static LinkedListTree parse(String body) {
		try {
			JavadocParser parser = parserOn(body);
			LinkedListTree result = (LinkedListTree)parser.comment_body().getTree();
			trimEOF(result);
			return result;
		} catch (IOException e) {
			throw new SyntaxException(e);
		} catch (RecognitionException e) {
			throw new SyntaxException(e);
		}
	}

	public static LinkedListTree parseParaTag(String text) {
		try {
			JavadocParser parser = parserOn(text);
			LinkedListTree result = (LinkedListTree)parser.paragraph_tag().getTree();
			trimEOF(result);
			return result;
		} catch (IOException e) {
			throw new SyntaxException(e);
		} catch (MismatchedTokenException e) {
			throw new SyntaxException("Expexted "+JavadocParser.tokenNames[e.expecting]+" but found "+JavadocParser.tokenNames[e.getUnexpectedType()], e);
		} catch (RecognitionException e) {
			throw new SyntaxException(e);
		}
	}

	private static JavadocParser parserOn(String text) throws IOException {
		StringReader in = new StringReader(text);
		ANTLRReaderStream cs = new ANTLRReaderStream(in);
		JavadocLexer lexer = new JavadocLexer(cs);
		LinkedListTokenSource source = new LinkedListTokenSource(lexer);
		LinkedListTokenStream stream = new LinkedListTokenStream(source);
		JavadocParser parser = new JavadocParser(stream);
		parser.setTreeAdaptor(TREE_ADAPTOR);
		return parser;
	}

	public static ASTDocComment createDocComment(LinkedListTree ast) {
		return new ASTDocComment(ast);
	}

	public static DocTag findParam(DocComment doc, String name) {
		Iterator params = doc.findTags("param");
		Pattern p = Pattern.compile("\\s*\\Q"+name+"\\E\\b");
		while (params.hasNext()) {
			DocTag param = (DocTag)params.next();
			if (p.matcher(param.getBodyString()).lookingAt()) {
				return param;
			}
		}
		return null;
	}
}