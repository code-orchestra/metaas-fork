/*
 * E4XHelper.java
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

package uk.co.badgersinfoil.metaas.impl.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.asdt.core.internal.antlr.AS3Lexer;
import uk.co.badgersinfoil.metaas.impl.AS3FragmentParser;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTokenSource;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTokenStream;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTreeAdaptor;
import uk.co.badgersinfoil.metaas.impl.parser.e4x.E4XParser;
import uk.co.badgersinfoil.metaas.impl.parser.e4x.E4XLexer;


public class E4XHelper {
	private static final LinkedListTreeAdaptor TREE_ADAPTOR = new LinkedListTreeAdaptor();

    private static final Set<Integer> closingXMLTokens = new HashSet<Integer>();
    static {
        for (int i = 0; i < E4XParser.tokenNames.length; i++) {
            String tokenName = E4XParser.tokenNames[i];
            if ("'/>'".equals(tokenName) || "'>'".equals(tokenName)) {
                closingXMLTokens.add(i);
            }
        }
    }

	/**
	 * Creates a properly-configured parser object for the E4X grammar.
	 */
	public static E4XParser parserOn(Reader in) throws IOException {
		ANTLRReaderStream cs = new ANTLRReaderStream(in);
		E4XLexer lexer = new E4XLexer(cs);
		LinkedListTokenSource source = new LinkedListTokenSource(lexer);
		LinkedListTokenStream stream = new LinkedListTokenStream(source);
		E4XParser parser = new E4XParser(stream);
		parser.setTreeAdaptor(TREE_ADAPTOR);
		parser.setInput(lexer, cs);
		return parser;
	}

	public static LinkedListTree parseXMLLiteral(AS3Lexer lexer, CharStream cs, LinkedListTokenStream stream) throws RecognitionException {

        // RE-3384
        // Remove last token from stream - we need E4X parser to get it by itself
        Token initialTail = stream.get(stream.index() - 1);
        cs.seek(cs.index() - initialTail.getText().length());
        stream.scrub(1);

		String tail = cs.substring(cs.index(), cs.size()-1);
		int initialTailLength = tail.length();
		E4XParser parser;
		try {
			parser = e4xParserOn(new StringReader(tail), stream);
		} catch (IOException e) {
			// TODO: better exception type?
			throw new RuntimeException(e);
		}
		LinkedListTree ast = AS3FragmentParser.tree(parser.xmlPrimary());

		tail = parser.getInputTail();
		// skip over the XML in the original, underlying CharStream
        int currentTokenLength = 0;
        if (stream.LT(1).getText() != null) {
            currentTokenLength = stream.LT(1).getText().length();
        }
		cs.seek(cs.index() + (initialTailLength - tail.length() - currentTokenLength) + 1);

		LinkedListTokenSource source = (LinkedListTokenSource)stream.getTokenSource();
		stream.setTokenSource(source);  // cause any remembered E4X state to be dropped
		stream.scrub(1); // erase the subsequent token that the E4X parser got from this stream
		source.setDelegate(lexer);

        // RE-3384
        // We substitute first token from ast with initialTail
        // because AS3 parser holds references to it in stack of 'expression' rules
        assert (initialTail instanceof LinkedListToken);
        LinkedListToken startT = (LinkedListToken) initialTail;
        LinkedListToken nextT = ast.getStartToken();
        startT.setType(nextT.getType());
        startT.setText(nextT.getText());
        startT.setNext(nextT.getNext());
        if (nextT.getPrev() != null) {
            nextT.getPrev().setNext(startT);
        }
        if (nextT.getNext() != null) {
            nextT.getNext().setPrev(startT);
        } else {
            stream.setTail(startT);
        }
        nextT.setPrev(null);
        nextT.setNext(null);
        if (ast.getStartToken() == ast.getStopToken()) {
            ast.setStopToken(startT);
        }
        ast.setStartToken(startT);

		return ast;
	}

	private static E4XParser e4xParserOn(Reader in, LinkedListTokenStream stream) throws IOException {
		ANTLRReaderStream cs = new ANTLRReaderStream(in);
		E4XLexer lexer = new E4XLexer(cs);
		LinkedListTokenSource source = (LinkedListTokenSource)stream.getTokenSource();
		source.setDelegate(lexer);

        // RE-3384
        // We can't delete initial token, but we can substitute its fields
        // and delete first token from E4X stream and ast
        //
		// The AS3 grammar will see the initial '<' as an LT (less-than)
		// token, and lookahead in the AS3Parser will have already
		// grabbed references to that token in order to make it the
		// startToken for various AST subtrees, so we can't just delete
		// it.  We therefore find the LT token and change its type to
		// match the E4X vocabulary, and then rewind the token stream
		// so that this will be the first token that the E4XParser will
		// see.
		//LinkedListToken startMarker = (LinkedListToken)stream.LT(-1);
		//startMarker.setType(E4XParser.XML_LCHEVRON);
		//stream.seek(stream.index()-2);

		E4XParser parser = new E4XParser(stream);
		parser.setTreeAdaptor(new LinkedListTreeAdaptor());
		parser.setInput(lexer, cs);
		return parser;
	}

    public static boolean isXMLClosingTokenType(int type) {
        return closingXMLTokens.contains(type);
    }

}