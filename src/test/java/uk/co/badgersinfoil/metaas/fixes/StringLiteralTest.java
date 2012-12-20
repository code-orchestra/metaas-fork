package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This tests checks if the string literal is parsed correctly.
 *
 * @author Alexander Eliseyev
 */
public class StringLiteralTest extends TestCase {

    public void testApostrophe() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(1);
        assertNotNull(statement);
        assertTrue(statement instanceof ASExpressionStatement);

        ASExpressionStatement expressionStatement = (ASExpressionStatement) statement;
        Expression expression = expressionStatement.getExpression();

        assertTrue(expression instanceof ASAssignmentExpression);
        ASAssignmentExpression assignmentExpression = (ASAssignmentExpression) expression;

        assertTrue(assignmentExpression.getRightSubexpression() instanceof ASStringLiteral);
        ASStringLiteral stringLiteral = (ASStringLiteral) assignmentExpression.getRightSubexpression();
        assertTrue(stringLiteral.isApostropheEnclosed());
    }

    public void testEscape() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(0);
        assertNotNull(statement);
        assertTrue(statement instanceof ASExpressionStatement);

        ASExpressionStatement expressionStatement = (ASExpressionStatement) statement;
        Expression expression = expressionStatement.getExpression();
        ASAssignmentExpression assignmentExpression = (ASAssignmentExpression) expression;

        assertTrue(expression instanceof ASAssignmentExpression);

        assertTrue(assignmentExpression.getRightSubexpression() instanceof ASStringLiteral);
        ASStringLiteral stringLiteral = (ASStringLiteral) assignmentExpression.getRightSubexpression();
        assertFalse(stringLiteral.isApostropheEnclosed());
        assertEquals("set <FONT COLOR=\"#000099\">", stringLiteral.getValue());
        assertEquals("set <FONT COLOR=\\\"#000099\\\">", stringLiteral.getUnescapedValue());
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("StringLiteral.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}