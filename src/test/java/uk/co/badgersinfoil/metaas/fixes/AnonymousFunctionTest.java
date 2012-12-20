package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This tests checks if the function variable anonymous function initializer is
 * parsed correctly.
 *
 * @author Alexander Eliseyev
 */
public class AnonymousFunctionTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(1);
        assertNotNull(statement);
        assertTrue(statement instanceof ASDeclarationStatement);

        ASDeclarationStatement declarationStatement = (ASDeclarationStatement) statement;

        TypeDescriptor varTypeName = declarationStatement.getFirstVarType();
        assertEquals("function", varTypeName.getName());

        Expression initExpr = (Expression) declarationStatement.getFirstVarInitializer();
        assertNotNull(initExpr);
        assertTrue(initExpr instanceof ASFunctionExpression);

        ASFunctionExpression functionExpression = (ASFunctionExpression) initExpr;
        assertEquals("SomeType", functionExpression.getType().getName());
        assertEquals("myArg", ((ASArg) functionExpression.getArgs().get(0)).getName());
        
        assertFalse(functionExpression.getStatementList().isEmpty());
        Statement firstAnnonFuncStatement = (Statement) functionExpression.getStatementList().get(1);
        assertTrue(firstAnnonFuncStatement instanceof ASExpressionStatement);

        ASExpressionStatement expressionStatement = (ASExpressionStatement) firstAnnonFuncStatement;
        assertTrue(expressionStatement.getExpression() instanceof ASPostfixExpression);
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("AnonymousFunction.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}