package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.SyntaxException;
import uk.co.badgersinfoil.metaas.dom.*;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This tests checks if the single statement "do while" statement is parsed correctly .
 *
 * @author Alexander Eliseyev
 */
public class DoWhileStatementTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(0);
        assertNotNull(statement);
        assertTrue(statement instanceof ASDoWhileStatement);

        ASDoWhileStatement doWhileStatement = (ASDoWhileStatement) statement;
        Statement body = doWhileStatement.getBody();
        assertNotNull(body);
        assertTrue(body instanceof ASExpressionStatement);
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("DoWhileStatement.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}