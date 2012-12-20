package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.ASTASBinaryExpression;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This tests checks if the function variable anonymous function initializer is
 * parsed correctly.
 *
 * @author Alexander Eliseyev
 */
public class DeleteExpressionTest extends TestCase {
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

        assertTrue(expression instanceof ASDeleteExpression);

        ASDeleteExpression deleteExpression = (ASDeleteExpression) expression;
        assertNotNull(deleteExpression.getSubexpression());
        assertTrue(deleteExpression.getSubexpression() instanceof ASSimpleNameExpression);

        ASSimpleNameExpression subExpr = (ASSimpleNameExpression) deleteExpression.getSubexpression();
        assertEquals(subExpr.getName(), "myVar");

	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("DeleteExpression.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}