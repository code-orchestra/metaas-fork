package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * This tests checks if the static initialization statements are parsed correctly.
 *
 * @author Alexander Eliseyev
 */
public class StaticInitTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASPackage asPackage = unit.getPackage();
        List<Statement> packageStatements = asPackage.getInitStatementList();
        assertEquals(1, packageStatements.size());
        assertTrue(packageStatements.get(0) instanceof ASExpressionStatement);

        ASClassType classType = (ASClassType) unit.getType();
        List<Statement> statements = classType.getStaticInitStatementList();

        assertEquals(2, statements.size());

        Statement statement0 = statements.get(0);
        assertTrue(statement0 instanceof ASExpressionStatement);

        Statement statement1 = statements.get(1);
        assertTrue(statement1 instanceof ASContinueStatement);
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("StaticInit.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}