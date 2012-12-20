package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.SyntaxException;
import uk.co.badgersinfoil.metaas.dom.*;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This tests checks if the for statement w/o code block {} is parsed correctly.
 *
 * @author Alexander Eliseyev
 */
public class ForStatementTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(0);
        assertNotNull(statement);
        assertTrue(statement instanceof ASForStatement);

        ASForStatement forStatement = (ASForStatement) statement;

        for (Expression condition : forStatement.getConditions()) {
          print(condition.toString());   
        }

        for (Expression update : forStatement.getUpdates()) {
          print(update.toString());   
        }

        try {
            forStatement.getStatementList();
            fail("SyntaxException expected as no statement list is available");
        } catch (SyntaxException e) {
            assertEquals("Loop body is not a block", e.getMessage());
        }
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("ForStatement.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}