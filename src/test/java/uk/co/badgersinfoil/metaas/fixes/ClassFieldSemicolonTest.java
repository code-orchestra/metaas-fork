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
public class ClassFieldSemicolonTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();

        List fields = classType.getFields();
        assertEquals(4, fields.size());

        int i = 0;
        for (Object fieldObj : fields) {
            ASField field = (ASField) fieldObj;
            assertEquals("myField" + ++i, field.getName());
        }
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("ClassFieldSemicolon.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}