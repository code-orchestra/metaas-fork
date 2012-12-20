package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASNamespaceDeclaration;
import uk.co.badgersinfoil.metaas.dom.ASType;
import uk.co.badgersinfoil.metaas.dom.Visibility;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This tests checks if the type namespace declaration is parsed correctly.
 *
 * @author Alexander Eliseyev
 */
public class NamespaceDefinitionTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);
        assertNotNull(unit);

        ASType type = unit.getType();
        assertNotNull(type.getNamespaceDeclarations());

        ASNamespaceDeclaration namespaceDeclaration = type.getNamespaceDeclarations().get(0);
        assertEquals("parsing", namespaceDeclaration.getName());
        assertEquals(Visibility.PROTECTED, namespaceDeclaration.getVisibility());

        ASNamespaceDeclaration namespaceDeclaration2 = type.getNamespaceDeclarations().get(1);
        assertEquals("foobar", namespaceDeclaration2.getName());
        assertEquals(Visibility.PRIVATE, namespaceDeclaration2.getVisibility());
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("NamespaceDefinition.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}