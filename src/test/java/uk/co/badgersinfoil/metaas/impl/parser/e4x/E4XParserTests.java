/*
 * Copyright (c) 2006-2007 David Holroyd
 */

package uk.co.badgersinfoil.metaas.impl.parser.e4x;

import java.io.IOException;
import java.io.StringReader;
import uk.co.badgersinfoil.metaas.impl.AS3FragmentParser;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.impl.parser.E4XHelper;
import junit.framework.TestCase;


public class E4XParserTests extends TestCase {
	public void testComment() throws Exception {
		E4XParser parser = parserOn("<!--foo-->");
		LinkedListTree tree = AS3FragmentParser.tree(parser.xmlMarkup());
		assertEquals(E4XParser.XML_COMMENT, tree.getType());
	}

	public void testPI() throws Exception {
		E4XParser parser = parserOn("<?foo?>");
		LinkedListTree tree = AS3FragmentParser.tree(parser.xmlMarkup());
		assertEquals(E4XParser.XML_PI, tree.getType());
	}

	public void testCDATA() throws Exception {
		E4XParser parser = parserOn("<![CDATA[foo]]>");
		LinkedListTree tree = AS3FragmentParser.tree(parser.xmlMarkup());
		assertEquals(E4XParser.XML_CDATA, tree.getType());
	}

	public void testEmptyElement() throws Exception {
		E4XParser parser = parserOn("<foo/>");
		LinkedListTree tree = AS3FragmentParser.tree(parser.xmlElement());
		assertEquals(E4XParser.XML_EMPTY_ELEMENT, tree.getType());
		LinkedListTree child = tree.getFirstChild();
		assertEquals("foo", child.getText());
	}

	public void testRemainder() throws Exception {
		E4XParser parser = parserOn("<foo/>)");
		parser.xmlPrimary();
		assertEquals(")", parser.getInputTail());
	}

	public void testElementBody() throws Exception {
		E4XParser parser = parserOn("<foo>bar &amp; blat</foo>");
		LinkedListTree tree = AS3FragmentParser.tree(parser.xmlPrimary());
		assertEquals(E4XParser.XML_ELEMENT, tree.getType());
	}

	public void testList() throws Exception {
		E4XParser parser = parserOn("<><foo/></>");
		LinkedListTree tree = AS3FragmentParser.tree(parser.xmlListInitialiser());
		assertEquals(E4XParser.XML_LIST, tree.getType());
		LinkedListTree child = tree.getFirstChild();
		assertEquals(E4XParser.XML_EMPTY_ELEMENT, child.getType());
	}

	public void testEmptyList() throws Exception {
		E4XParser parser = parserOn("<></>");
		LinkedListTree tree = AS3FragmentParser.tree(parser.xmlListInitialiser());
		assertEquals(E4XParser.XML_LIST, tree.getType());
		LinkedListTree child = tree.getFirstChild();
		assertNull(child);
	}

	/*
    public void testAttribute() throws Exception {
		E4XParser parser = parserOn(" a=\"b\"");
		LinkedListTree tree = AS3FragmentParser.tree(parser.xmlAttribute());
		assertEquals(E4XParser.XML_ATTRIBUTE, tree.getType());
		assertEquals("a", tree.getFirstChild().getText());
		assertEquals("\"b\"", tree.getLastChild().getText());
	}
	*/

	private static E4XParser parserOn(String str) throws IOException {
		StringReader in = new StringReader(str);
		return E4XHelper.parserOn(in);
	}
}