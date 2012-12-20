package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * This test checks if method declaration w/o semicolons works fine.
 *
 * @author Alexander Eliseyev
 */
public class InterfaceMetodsSemicolonTest extends TestCase {
    public void testIt() {
        ActionScriptFactory fact = new ActionScriptFactory();
        ASCompilationUnit unit = loadSyntaxExample(fact);

        ASInterfaceType interfaceType = (ASInterfaceType) unit.getType();

        List methods = interfaceType.getMethods();
        assertEquals(4, methods.size());

        int i = 0;
        for (Object methodObj : methods) {
            ASMethod method = (ASMethod) methodObj;
            print(method.getName());
            assertEquals("$myFunc" + ++i, method.getName());
        }
    }

    private static void print(String str) {
        System.out.println(str);
    }

    private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
        InputStream in = getClass().getClassLoader().getResourceAsStream("InterfaceMetodsSemicolon.as");
        ActionScriptParser parser = fact.newParser();
        ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
        return unit;
    }
}