package uk.co.badgersinfoil.metaas.impl.parser.regexsimple;

import java.io.IOException;
import java.io.StringReader;
import uk.co.badgersinfoil.metaas.impl.AS3FragmentParser;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.impl.parser.RegexSimpleHelper;
import uk.co.badgersinfoil.metaas.impl.parser.regexsimple.RegexSimpleParser;
import junit.framework.TestCase;


public class RegexSimpleParserTests extends TestCase {
	public void testSimple() throws Exception {
		RegexSimpleParser parser = parserOn("/@/");
		LinkedListTree tree = AS3FragmentParser.tree(parser.regexpLiteral());
		assertEquals(RegexSimpleParser.REGEXP_LITERAL, tree.getType());
	}

	public void testRemainder() throws Exception {
		RegexSimpleParser parser = parserOn("/@/ ");
		LinkedListTree tree = AS3FragmentParser.tree(parser.regexpLiteral());
		assertEquals(RegexSimpleParser.REGEXP_LITERAL, tree.getType());
		assertEquals(" ", parser.getInputTail());
	}
	public void testEscapedDelimiter() throws Exception {
		RegexSimpleParser parser = parserOn("/\\//");
		LinkedListTree tree = AS3FragmentParser.tree(parser.regexpLiteral());
		assertEquals(RegexSimpleParser.REGEXP_LITERAL, tree.getType());
	}

	public void testSimpleFlags() throws Exception {
		RegexSimpleParser parser = parserOn("/@/mi");
		LinkedListTree tree = AS3FragmentParser.tree(parser.regexpLiteral());
		assertEquals(RegexSimpleParser.REGEXP_LITERAL, tree.getType());
	}

	public void testNullEscapes() throws Exception {
		RegexSimpleParser parser = parserOn("/\\_/\\_");
		LinkedListTree tree = AS3FragmentParser.tree(parser.regexpLiteral());
		assertEquals(RegexSimpleParser.REGEXP_LITERAL, tree.getType());
	}

	public void testHexEscapes() throws Exception {
		RegexSimpleParser parser = parserOn("/\\x12\\u1234\\U12345678/");
		LinkedListTree tree = AS3FragmentParser.tree(parser.regexpLiteral());
		assertEquals(RegexSimpleParser.REGEXP_LITERAL, tree.getType());
	}

	private static RegexSimpleParser parserOn(String str) throws IOException {
		StringReader in = new StringReader(str);
		return RegexSimpleHelper.parserOn(in);
	}
}