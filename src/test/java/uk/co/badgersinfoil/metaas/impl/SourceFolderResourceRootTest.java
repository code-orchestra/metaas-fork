package uk.co.badgersinfoil.metaas.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import junit.framework.TestCase;


public class SourceFolderResourceRootTest extends TestCase {
	private File tmpDir;

	public void setUp() throws IOException {
		tmpDir = new File(System.getProperty("java.io.tmpdir"), "metaas-test");
		tmpDir.mkdir();
		File tmpFile = new File(tmpDir, "Test.as");
		tmpFile.createNewFile();
		File tmpPkg = new File(tmpDir, "pkg");
		tmpPkg.mkdir();
		File tmpFile2 = new File(tmpPkg, "Test2.as");
		tmpFile2.createNewFile();
	}

	public void tearDown() throws IOException {
		FileUtils.deleteDirectory(tmpDir);
	}

	public void testIt() {
		/*
		SourceFolderResourceRoot resourceRoot = new SourceFolderResourceRoot(tmpDir);
		List qnames = resourceRoot.getDefinitionQNames();
		assertEquals(2, qnames.size());
		assertEquals(new ASQName("Test"), qnames.get(0));
		assertEquals(new ASQName("pkg", "Test2"), qnames.get(1));
		*/
	}
}