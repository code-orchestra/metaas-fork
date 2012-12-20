package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASNamespaceDeclaration;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Some test.
 *
 * @author Alexander Eliseyev
 */
public class GlobalNamespaceTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASNamespaceDeclaration globalNSDeclaration = unit.getGlobalNamespaceDeclaration();
        assertEquals("mx_internal", globalNSDeclaration.getName());
        assertEquals("http://www.adobe.com/2006/flex/mx/internal", globalNSDeclaration.getURI());
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("GlobalNamespace.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}