package uk.co.badgersinfoil.metaas;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import uk.co.badgersinfoil.metaas.dom.ASArg;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASType;
import uk.co.badgersinfoil.metaas.dom.DocComment;
import uk.co.badgersinfoil.metaas.dom.ASMethod;
import uk.co.badgersinfoil.metaas.dom.DocTag;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import junit.framework.TestCase;


public class DocCommentTests extends TestCase {

	private ActionScriptFactory fact = new ActionScriptFactory();
	private ASCompilationUnit unit;
	private ASClassType clazz;

	protected void setUp() {
		ActionScriptFactory fact = new ActionScriptFactory();
		unit = fact.newClass("Test");
		clazz = (ASClassType)unit.getType();
	}

	protected void tearDown() throws IOException {
		CodeMirror.assertReflection(fact, unit);
	}

	public void testEndOfCommentMarker() {
		try {
			clazz.getDocumentation().setDescriptionString("*/");
			fail("should not allow '*/' within a doc-comment");
		} catch (SyntaxException e) {
			// expected
		}
	}

	public void testParaEndOfCommentMarker() {
		try {
			clazz.getDocumentation().addParaTag("foo", "*/");
			fail("should not allow '*/' within a doc-comment");
		} catch (SyntaxException e) {
			// expected
		}
	}

	public void testSetNewDescription() {
		clazz.setDescription("hello");
		assertEquals("hello", clazz.getDescriptionString().trim());
	}

	public void testSetBadDescription() {
		try {
			clazz.setDescription("hello\n@param foo great");
			fail("should have rejected description with trailing tagged-paragraph");
		} catch (SyntaxException e) {
			// expected
		}
	}

	public void testGetDescriptionNoDocs() {
		assertNull(clazz.getDescriptionString());
	}

	public void testRemoveDescription() {
		clazz.setDescription(null);
		assertEquals(null, clazz.getDescriptionString());
	}

	public void testGetExistingDescription() {
		clazz.getDocumentation().setDescriptionString("hello world");
		clazz.getDocumentation().addParaTag("param", "foo bar");
		assertEquals("hello world", clazz.getDescriptionString().trim());
	}

	public void testNewArgNoDocs() {
		ASMethod meth = clazz.newMethod("test", Visibility.PUBLIC, "void");
		ASArg test = meth.addParam("test", "String");
		assertNull(test.getDescriptionString());
		test.setDescription("a test!");
		assertEquals("a test!", test.getDescriptionString().trim());
		// insert a description before the @param
		meth.setDescription("foo\nbar");
		assertEquals("foo\nbar", meth.getDescriptionString().trim());
		String actualDesc = test.getDescriptionString();
		assertNotNull(actualDesc);
		assertEquals("a test!", actualDesc.trim());
	}

	public void testUpdateArgDocs() {
		ASMethod meth = clazz.newMethod("test", Visibility.PUBLIC, "void");
		ASArg test = meth.addParam("test", "String");
		assertNull(test.getDescriptionString());
		test.setDescription("initial text!");
		assertEquals("initial text!", test.getDescriptionString().trim());
		test.setDescription("replacement text");
		assertEquals("replacement text", test.getDescriptionString().trim());
	}

	public void testParaTagUpdateNewline() throws IOException {
		DocComment doc = clazz.getDocumentation();
		doc.addParaTag("test", "initial text");
		DocTag tag = doc.findFirstTag("test");
		tag.setBody("replacement\ncontent");
		ActionScriptWriter writer = fact.newWriter();
		StringWriter buff = new StringWriter();
		writer.write(buff, unit);
		String[] lines = buff.toString().split("\n");
		// check indentation, skipping first and last lines,
		for (int i=1; i<lines.length-1; i++) {
			assertIndent("\t", lines[i]);
		}
	}

	private static void assertIndent(String indent, String line) {
		if (line.length() == 0) {
			// blank lines allowed
			return;
		}
		assertTrue("expected indentation "+ActionScriptFactory.str(indent)+" but line started with "+ActionScriptFactory.str(line.substring(0, indent.length())), line.startsWith(indent));
	}

	public void testReturnDocs() {
		ASMethod meth = clazz.newMethod("test", Visibility.PUBLIC, "void");
		assertNull(meth.getReturnDescriptionString());
		meth.setReturnDescription(null);  // should not fail
		meth.setReturnDescription("blah blah");
		assertEquals("blah blah", meth.getReturnDescriptionString().trim());
	}

	public void testRemoveReturnDocs() {
		ASMethod meth = clazz.newMethod("test", Visibility.PUBLIC, "void");
		meth.setReturnDescription("blah blah");
		meth.setReturnDescription(null);
		assertNull(meth.getReturnDescriptionString());
	}

	public void testDocumentation() {
		DocComment doc = clazz.getDocumentation();
		assertNotNull(doc);
		assertNull(doc.getDescriptionString());
	}

	public void testFindTags() {
		DocComment doc = clazz.getDocumentation();
		doc.addParaTag("param", "arg0 blah");
		doc.addParaTag("param", "arg1 blah blah");
		doc.addParaTag("param", "arg2 blah blah blah");
		Iterator i = doc.findTags("param");
		assertTrue(i.hasNext());
		assertTrue(((DocTag)i.next()).getBodyString().trim().startsWith("arg0"));
		assertTrue(i.hasNext());
		assertTrue(((DocTag)i.next()).getBodyString().trim().startsWith("arg1"));
		assertTrue(i.hasNext());
		assertTrue(((DocTag)i.next()).getBodyString().trim().startsWith("arg2"));
	}

	public void testDocTagName() {
		DocComment doc = clazz.getDocumentation();
		doc.addParaTag("param", "arg0 blah");
		DocTag tag = doc.findFirstTag("param");
		assertEquals("param", tag.getName());
		tag.setName("see");
		assertEquals("see", tag.getName());
		// TODO: assert that the comment is removed?
	}

	public void testDelete() {
		DocComment doc = clazz.getDocumentation();
		doc.setDescriptionString("boo\nfoo");
		doc.addParaTag("param", "arg0 blah");
		doc.addParaTag("param", "arg1 blah blah");
		doc.addParaTag("param", "arg2 blah blah blah");
		DocTag arg1 = null;
		for (Iterator i=doc.findTags("param"); i.hasNext(); ) {
			DocTag tag = (DocTag)i.next();
			if (tag.getBodyString().trim().startsWith("arg1")) {
				arg1 = tag;
				break;
			}
		}
		assertNotNull(arg1);
		doc.delete(arg1);
		// check it's gone,
		Iterator i = doc.findTags("param");
		assertTrue(i.hasNext());
		assertTrue(((DocTag)i.next()).getBodyString().trim().startsWith("arg0"));
		assertTrue(i.hasNext());
		assertTrue(((DocTag)i.next()).getBodyString().trim().startsWith("arg2"));
//		doc.delete(doc.findFirstTag("param"));
//		doc.delete(doc.findFirstTag("param"));
//		i = doc.findTags();
//		assertFalse(i.hasNext());
	}

	public void testAddToParsedContent() throws IOException {
		ActionScriptParser p = fact.newParser();
		StringReader in = new StringReader(
			"package {\n" +
			"	public class Test {\n" +
			"		public function foo() {\n" +
			"		}\n" +
			"	}\n" +
			"}\n"
		);
		unit = p.parse(in);
		ASType type = unit.getType();
		ASMethod foo = type.getMethod("foo");
		foo.setDescription("woo!");
		ActionScriptWriter writer = fact.newWriter();
		StringWriter out = new StringWriter();
		writer.write(out, unit);
		String result = out.toString();
		// find the end of what would be the only comment block,
		int pos = result.indexOf("*/");
		char firstCharAfterComment = result.charAt(pos+2);
		assertTrue("expected a newline to appear after newly inserted comment: 0x"+Integer.toHexString(firstCharAfterComment),
		           firstCharAfterComment=='\n' || firstCharAfterComment=='\r');
	}
}