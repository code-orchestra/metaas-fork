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
public class SuperExpressionTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        // Superclass field access
        Statement statement = (Statement) myFuncMethod.getStatementList().get(0);
        assertNotNull(statement);
        assertTrue(statement instanceof ASExpressionStatement);

        ASExpressionStatement expressionStatement = (ASExpressionStatement) statement;
        Expression expression = expressionStatement.getExpression();

        assertTrue(expression instanceof ASFieldAccessExpression);
        ASFieldAccessExpression fieldAccessExpression = (ASFieldAccessExpression) expression;

        assertTrue(fieldAccessExpression.getTargetExpression() instanceof ASSuperExpression);

        // Superclass constructor invocation
        statement = (Statement) myFuncMethod.getStatementList().get(1);
        expressionStatement = (ASExpressionStatement) statement;
        expression = expressionStatement.getExpression();

        assertTrue(expression instanceof ASInvocationExpression);

        ASInvocationExpression invocationExpression = (ASInvocationExpression) expression;
        assertTrue(invocationExpression.getTargetExpression() instanceof ASSuperExpression);
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("SuperExpression.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}