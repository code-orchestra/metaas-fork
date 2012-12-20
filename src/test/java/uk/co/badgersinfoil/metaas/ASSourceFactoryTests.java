package uk.co.badgersinfoil.metaas;

import java.io.IOException;
import java.io.StringReader;
import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.asdt.core.internal.antlr.AS3Parser;
import org.asdt.core.internal.antlr.AS3Lexer;
import junit.framework.TestCase;

public class ASSourceFactoryTests extends TestCase {
	public void testStr() throws IOException, RecognitionException {
		final String input = "\\\"\n\t\r'";
		String result = ActionScriptFactory.str(input);
		StringReader in = new StringReader(result);
		ANTLRReaderStream stream = new ANTLRReaderStream(in);
		AS3Lexer lex = new AS3Lexer(stream);
		Token tok = lex.nextToken();
		assertEquals(AS3Parser.STRING_LITERAL, tok.getType());
		assertEquals("\"\\\\\\\"\\n\\t\\r'\"", tok.getText());
	}
}
