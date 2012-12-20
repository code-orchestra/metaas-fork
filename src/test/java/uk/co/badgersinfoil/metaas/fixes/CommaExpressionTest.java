package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Some test.
 *
 * @author Alexander Eliseyev
 */
public class CommaExpressionTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement0 = (Statement) myFuncMethod.getStatementList().get(0);
        assertNotNull(statement0);
        assertTrue(statement0 instanceof ASDeclarationStatement);

        ASDeclarationStatement declarationStatement = (ASDeclarationStatement) statement0;
        Expression expression = declarationStatement.getFirstVarInitializer();
        assertTrue(expression instanceof ASParenthesizedExpression);
        ASParenthesizedExpression parenthesizedExpression = (ASParenthesizedExpression) expression;
        assertEquals(3, parenthesizedExpression.getSubExpressions().size());

        Statement statement1 = (Statement) myFuncMethod.getStatementList().get(1);
        assertNotNull(statement1);
        assertTrue(statement1 instanceof ASExpressionStatement);
        ASExpressionStatement expressionStatement = (ASExpressionStatement) statement1;
        assertEquals(4, expressionStatement.getSubExpressions().size());
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("CommaExpression.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}