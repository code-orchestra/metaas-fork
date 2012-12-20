package uk.co.badgersinfoil.metaas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASMetaTag;
import uk.co.badgersinfoil.metaas.dom.ASMethod;
import uk.co.badgersinfoil.metaas.dom.ASType;
import uk.co.badgersinfoil.metaas.dom.MetaTagable;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import junit.framework.TestCase;


public class MetaTagableTests extends TestCase {
	private ActionScriptFactory fact = new ActionScriptFactory();
	private ASCompilationUnit unit;

	protected void setUp() {
		unit = fact.newClass("Test");
	}

	protected void tearDown() throws IOException {
		CodeMirror.assertReflection(fact, unit);
	}
	
	// TODO: test doc-comments get attaced in the correct place

	
	public void testMethod() {
		ASClassType clazz = (ASClassType)unit.getType();
		ASMethod meth = clazz.newMethod("test", Visibility.PUBLIC, null);
		check(meth);
	}

	public void testType() {
		check(unit.getType());
	}

	public void check(MetaTagable taggable) {
		assertEquals(Collections.EMPTY_LIST, taggable.getAllMetaTags());
		assertNull(taggable.getFirstMetatag("missing"));
		assertEquals(Collections.EMPTY_LIST,
		             taggable.getMetaTagsWithName("missing"));
		ASMetaTag biddable = taggable.newMetaTag("Biddable");
		assertEquals("Biddable", biddable.getName());
		assertEquals(1, taggable.getAllMetaTags().size());
		assertEquals(Collections.EMPTY_LIST, biddable.getParams());
		assertNull(biddable.getParamValue("missing"));
		biddable.addParam(true);
		biddable.addParam(123);
		biddable.addParam("test");
		biddable.addParam("name", "value");
		List params = biddable.getParams();
		assertEquals(4, params.size());
		assertEquals(Boolean.TRUE, params.get(0));
		assertEquals(new Integer(123), params.get(1));
		assertEquals("test", params.get(2));
		assertEquals("value", biddable.getParamValue("name"));
		ASMetaTag event = taggable.newMetaTag("Event");
		assertNotNull(taggable.getFirstMetatag("Event"));
		ASMetaTag foo = taggable.newMetaTag("Foo");
		assertEquals(1, taggable.getMetaTagsWithName("Foo").size());
	}
	
	public void testParsed() throws FileNotFoundException {
		String src =
			"package framework.business{\n" +
			"	import com.adobe.cairngorm.business.ServiceLocator;\n" +
			"	import mx.rpc.IResponder;\n" +
			"	/**\n" +
			"	 * BDelegate comment\n" +
			"	 */\n" +
			"	[Bindable]\n" +
			"	public class TestDelegate {\n" +
			"		private var responder:IResponder;\n" +
			"		private var service:Object;\n" +
			"		public function TestDelegate(responder:mx.rpc.IResponder) {\n" +
			"			//Save Responder\n" +
			"			this.responder = responder;\n" +
			"			//Save Service\n" +
			"			this.service = ServiceLocator.getInstance().getService(\"TestRO\");\n" +
			"		}\n" +
			"		public function findAndUpdate():void {\n" +
			"			var call:Object = this.service.findAndUpdate();\n" +
			"			call.addResponder(responder);\n" +
			"		}\n" +
			"	}\n" +
			"}\n";
		unit = fact.newParser().parse(new StringReader(src));
		ASType testType = unit.getType();
		assertEquals(1, testType.getAllMetaTags().size());
		assertEquals(0, testType.getMetaTagsWithName("RemoteClass").size());
		assertEquals(1, testType.getMetaTagsWithName("Bindable").size());
	}

	public void testParseValuedParam() {
        	final String AS_CLASS =
        	"package org.test\n" +
        	"{\n" +
        	"       public class MetaTagTest\n" +
        	"       {\n" +
        	"               [MyMetaTag(param=\"value\")]  \n" +
        	"               public function myFunction():void\n" +
        	"               {\n" +
        	"               }\n" +
        	"       }\n" +
        	"}";

		unit = fact.newParser().parse(new StringReader(AS_CLASS));
		ASClassType asClass = (ASClassType)unit.getType();
		ASMethod asMethod = (ASMethod)asClass.getMethods().iterator().next();
		ASMetaTag asMetaTag = (ASMetaTag) asMethod.getAllMetaTags().iterator().next();

		assertEquals("MyMetaTag", asMetaTag.getName());
		assertEquals("value", asMetaTag.getParamValue("param"));

		List params = asMetaTag.getParams();
		ExtraAssertions.assertSize(1, params);
	}
}
