package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This tests checks if the instance of binary operation is parsed correctly
 *
 * @author Alexander Eliseyev
 */
public class InstanceOfExpressionTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(0);
        assertNotNull(statement);
        assertTrue(statement instanceof ASExpressionStatement);

        ASExpressionStatement expressionStatement = (ASExpressionStatement) statement;
        Expression expression = expressionStatement.getExpression();

        assertTrue(expression instanceof ASBinaryExpression);

        ASBinaryExpression binaryExpression = (ASBinaryExpression) expression;
        assertEquals(binaryExpression.getOperator(), ASBinaryExpression.Op.INSTANCEOF);

        assertTrue(binaryExpression.getLeftSubexpression() instanceof ASSimpleNameExpression);
        assertEquals(((ASSimpleNameExpression) binaryExpression.getLeftSubexpression()).getName(), "a");

        assertTrue(binaryExpression.getRightSubexpression() instanceof ASSimpleNameExpression);
        assertEquals(((ASSimpleNameExpression) binaryExpression.getRightSubexpression()).getName(), "Bar");
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("InstanceOfExpression.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}