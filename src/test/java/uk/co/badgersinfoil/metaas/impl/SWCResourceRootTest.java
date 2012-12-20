package uk.co.badgersinfoil.metaas.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import junit.framework.TestCase;


public class SWCResourceRootTest extends TestCase {
	public void testIt() throws IOException {
		File file = File.createTempFile("test", ".swc");
		createTextSWC(file);
		SWCResourceRoot root = new SWCResourceRoot(file.getAbsolutePath());
		List list = root.getDefinitionQNames();
		assertEquals(2, list.size());
		assertEquals(new ASQName("flashy.events", "EventWrecker"),
		             list.get(0));
		assertEquals(new ASQName("NoPackage"),
		             list.get(1));
		file.delete();
	}

	private void createTextSWC(File file) throws FileNotFoundException, IOException {
		ZipEntry catalogEntry = new ZipEntry("catalog.xml");
		FileOutputStream out = new FileOutputStream(file);
		file.deleteOnExit();
		ZipOutputStream zip = new ZipOutputStream(out);
		zip.putNextEntry(catalogEntry);
		OutputStreamWriter writer = new OutputStreamWriter(zip);
		writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+"<swc xmlns=\"http://www.adobe.com/flash/swccatalog/9\">"
			+"  <versions>"
			+"    <swc version=\"1.0\"/>"
			+"    <flex version=\"2.0\" build=\"0\"/>"
			+"  </versions>"
			+"  <features>"
			+"    <feature-script-deps/>"
			+"    <feature-components/>"
			+"    <feature-files/>"
			+"  </features>"
			+"  <libraries>"
			+"    <library path=\"library.swf\">"
			+"      <script name=\"EventWrecker\" mod=\"1234567890123\">"
			+"        <def id=\"flashy.events:EventWrecker\"/>"
			+"        <def id=\"NoPackage\"/>"
			+"        <dep id=\"Object\" type=\"i\"/>"
			+"      </script>"
			+"    </library>"
			+"  </libraries>"
			+"  <files>"
			+"  </files>"
			+"</swc>");
		writer.flush();
		zip.close();
	}
}
