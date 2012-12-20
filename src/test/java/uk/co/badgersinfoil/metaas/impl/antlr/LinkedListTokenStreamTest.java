package uk.co.badgersinfoil.metaas.impl.antlr;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenSource;
import org.asdt.core.internal.antlr.AS3Lexer;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
import junit.framework.TestCase;

public class LinkedListTokenStreamTest extends TestCase {
	private static final int TYPE_TEST = 123;

	public void testEOF() {
		TokenSource source = new TokenSource() {
			private boolean done = false;
			public Token nextToken() {
				if (done) {
					return Token.EOF_TOKEN;
				}
				done = true;
				return new CommonToken(TYPE_TEST, "test");
			}
		};
		LinkedListTokenSource linkedSource = new LinkedListTokenSource(source);
		LinkedListTokenStream stream = new LinkedListTokenStream(linkedSource);
		LinkedListToken tok = next(stream);
		assertEquals(TYPE_TEST, tok.getType());
		LinkedListToken eof = next(stream);
		assertEquals(CharStream.EOF, eof.getType());
		assertEquals(tok, eof.getPrev());

		LinkedListToken eof2 = next(stream);
		assertEquals(CharStream.EOF, eof2.getType());
		assertSame(eof, eof2);
		assertEquals(tok, eof2.getPrev());
	}

	private LinkedListToken next(LinkedListTokenStream stream) {
		LinkedListToken tok = (LinkedListToken)stream.LT(1);
		stream.consume();
		return tok;
	}

	public void testMarkRewind() {
		ANTLRStringStream input = new ANTLRStringStream("package  { b");
		AS3Lexer lex = new AS3Lexer(input);
		LinkedListTokenSource src = new LinkedListTokenSource(lex);
		LinkedListTokenStream str = new LinkedListTokenStream(src);

		ASTUtils.assertAS3TokenTypeIs(AS3Parser.PACKAGE, str.LA(1));
		str.consume();
		ASTUtils.assertAS3TokenTypeIs(AS3Parser.LCURLY, str.LA(1));
		int mark = str.mark();
		assertEquals(2, mark);
		str.consume();
		ASTUtils.assertAS3TokenTypeIs(AS3Parser.IDENT, str.LA(1));

		str.rewind(mark);
		ASTUtils.assertAS3TokenTypeIs(AS3Parser.LCURLY, str.LA(1));
		str.consume();

		// no-args version should just go back to last mark
		str.rewind();
		assertEquals(mark, str.index());
	}
}
