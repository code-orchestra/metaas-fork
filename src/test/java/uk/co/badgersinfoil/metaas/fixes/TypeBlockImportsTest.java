package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * This tests checks if the type block imports are parsed correctly.
 *
 * @author Alexander Eliseyev
 */
public class TypeBlockImportsTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        List imports = unit.getPackage().findImports();
        assertEquals(4, imports.size());

        int i = 1;
        for (Object importObj : imports) {
            assertEquals("com.eliseyev.test" + i++, importObj);
        }
    }

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("TypeBlockImports.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}