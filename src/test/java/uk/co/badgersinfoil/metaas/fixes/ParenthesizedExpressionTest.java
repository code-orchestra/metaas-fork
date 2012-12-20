package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.ASTASBinaryExpression;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Alexander Eliseyev
 */
public class ParenthesizedExpressionTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(0);
        assertNotNull(statement);

        ASExpressionStatement expressionStatement = (ASExpressionStatement) statement;
        ASTASBinaryExpression binaryExpression = (ASTASBinaryExpression) expressionStatement.getExpression();
        assertNotNull(binaryExpression);

        assertEquals(binaryExpression.getOperator(), ASBinaryExpression.Op.MUL);
        assertTrue(binaryExpression.getLeftSubexpression() instanceof ASParenthesizedExpression);
        assertTrue(binaryExpression.getRightSubexpression() instanceof ASIntegerLiteral);        
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("ParenthesizedExpression.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}