package uk.co.badgersinfoil.metaas.impl.parser.javadoc;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.RecognitionException;
import uk.co.badgersinfoil.metaas.impl.antlr.ASTDot;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTokenSource;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTokenStream;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTreeAdaptor;
import junit.framework.TestCase;

public class JavadocParserTests extends TestCase {
	private static final LinkedListTreeAdaptor TREE_ADAPTOR = new LinkedListTreeAdaptor();

	public void testEmptyBody() throws IOException, RecognitionException {
		LinkedListTree tree = parse("");
		assertEquals(JavadocParser.JAVADOC, tree.getType());
		assertEquals(JavadocParser.DESCRIPTION, tree.getFirstChild().getType());
	}

	public void testParaBasic() throws IOException, RecognitionException {
		String doc = "* desc\n" +
			     "* ription\n" +
			     "* @param foo bar";
		LinkedListTree tree = parse(doc);

		LinkedListTree desc = (LinkedListTree)tree.getChild(0);
		assertEquals(JavadocParser.DESCRIPTION, desc.getType());

		LinkedListTree param = (LinkedListTree)tree.getChild(1);
		assertEquals(JavadocParser.PARA_TAG, param.getType());
		assertEquals("@param", param.getFirstChild().getText());
	}

	public void testLonelyParaTag() throws IOException, RecognitionException {
		String doc = "\n\t\t * @param test a test!\n\t\t ";
		LinkedListTree tree = parse(doc);
	}

	public void testJustNewlines() throws IOException, RecognitionException {
		String doc = "\n\n";
		LinkedListTree tree = parse(doc);
	}

	public void testInlineTag() throws IOException, RecognitionException {
		LinkedListTree tree = parse("{@link foo}");
//OutputStreamWriter out = new OutputStreamWriter(System.out);
//new ASTDot(out).dotify(tree);
//out.flush();
		LinkedListTree desc = tree.getFirstChild();
		LinkedListTree tag = desc.getFirstChild();
		assertEquals(JavadocParser.INLINE_TAG, tag.getType());
		assertEquals("@link", tag.getFirstChild().getText());
	}

	private static LinkedListTree parse(String str) throws IOException, RecognitionException {
		JavadocParser parser = parserOn(str);
		return parser.comment_body().tree;
	}

	private static JavadocParser parserOn(String str) throws IOException {
		StringReader in = new StringReader(str);
		ANTLRReaderStream cs = new ANTLRReaderStream(in);
		JavadocLexer lexer = new JavadocLexer(cs);
		LinkedListTokenSource source = new LinkedListTokenSource(lexer);
		LinkedListTokenStream stream = new LinkedListTokenStream(source);
		JavadocParser parser = new JavadocParser(stream);
		parser.setTreeAdaptor(TREE_ADAPTOR);
//		parser.setInput(lexer, cs);
		return parser;
	}
}
