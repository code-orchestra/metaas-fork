package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This tests check if the field access with namespace is parsed correctly.
 *
 * @author Alexander Eliseyev
 */
public class NamespaceFieldAccessTest extends TestCase {

    public void testFieldAccessExpression() {
        ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(1);
        assertNotNull(statement);
        assertTrue(statement instanceof ASExpressionStatement);

        ASExpressionStatement expressionStatement = (ASExpressionStatement) statement;
        Expression expression = expressionStatement.getExpression();

        assertTrue(expression instanceof ASFieldAccessExpression);
        ASFieldAccessExpression fieldAccessExpression = (ASFieldAccessExpression) expression;

        assertEquals("stuff", fieldAccessExpression.getName());
        assertEquals("myns", fieldAccessExpression.getNamespace());        
    }

    public void testFieldAccessExpression_emptyNS() {
        ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(3);
        assertNotNull(statement);
        assertTrue(statement instanceof ASExpressionStatement);

        ASExpressionStatement expressionStatement = (ASExpressionStatement) statement;
        Expression expression = expressionStatement.getExpression();

        assertTrue(expression instanceof ASFieldAccessExpression);
        ASFieldAccessExpression fieldAccessExpression = (ASFieldAccessExpression) expression;

        assertEquals("stuff", fieldAccessExpression.getName());
        assertNull(fieldAccessExpression.getNamespace());
    }

    public void testSimpleNameExpression() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(0);
        assertNotNull(statement);
        assertTrue(statement instanceof ASExpressionStatement);

        ASExpressionStatement expressionStatement = (ASExpressionStatement) statement;
        Expression expression = expressionStatement.getExpression();

        assert (expression instanceof ASSimpleNameExpression);
        ASSimpleNameExpression simpleNameExpression = (ASSimpleNameExpression) expression;
        assertEquals("stuff", simpleNameExpression.getName());
        assertEquals("myns", simpleNameExpression.getNamespace());
	}

    public void testSimpleNameExpression_emptyNS() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(2);
        assertNotNull(statement);
        assertTrue(statement instanceof ASExpressionStatement);

        ASExpressionStatement expressionStatement = (ASExpressionStatement) statement;
        Expression expression = expressionStatement.getExpression();

        assertTrue(expression instanceof ASSimpleNameExpression);
        ASSimpleNameExpression simpleNameExpression = (ASSimpleNameExpression) expression;
        assertEquals("stuff", simpleNameExpression.getName());
        assertNull(simpleNameExpression.getNamespace());
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("NamespaceFieldAccess.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}