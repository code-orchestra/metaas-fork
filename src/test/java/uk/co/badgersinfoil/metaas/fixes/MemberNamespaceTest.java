package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Alexander Eliseyev
 */
public class MemberNamespaceTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();

        ASMethod myFuncMethod = classType.getMethod("myFunc");
        assertEquals("mynamespace", myFuncMethod.getNamespace());

        ASMethod myFuncMethod2 = classType.getMethod("myFunc2");
        assertNull(myFuncMethod2.getNamespace());

        ASMethod myFuncMethod3 = classType.getMethod("myFunc3");
        assertEquals("mynamespace", myFuncMethod.getNamespace());

        ASField myField = classType.getField("myField");
        assertEquals("mynamespace", myField.getNamespace());
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("MemberNamespace.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}