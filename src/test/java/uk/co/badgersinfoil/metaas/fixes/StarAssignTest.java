package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This tests checks if the <code>*=</code> and </code>a : *= b</a> are parsed correctly.
 *
 * @author Alexander Eliseyev
 */
public class StarAssignTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(0);
        assertNotNull(statement);
        assertTrue(statement instanceof ASDeclarationStatement);

        ASDeclarationStatement decl = (ASDeclarationStatement) statement;
        assertEquals("*", decl.getFirstVarType().getName());

        Statement statement1 = (Statement) myFuncMethod.getStatementList().get(1);
        assertTrue(statement1 instanceof ASExpressionStatement);
        ASExpressionStatement expressionStatement = (ASExpressionStatement) statement1;
        Expression expression = expressionStatement.getExpression();
        assertTrue(expression instanceof ASAssignmentExpression);
        ASAssignmentExpression assignmentExpression = (ASAssignmentExpression) expression;
        assertEquals(ASAssignmentExpression.Op.MUL_ASSIGN, assignmentExpression.getOperator());

        Statement statement2 = (Statement) myFuncMethod.getStatementList().get(2);
        assertTrue(statement2 instanceof ASExpressionStatement);
        ASExpressionStatement expressionStatement2 = (ASExpressionStatement) statement2;
        Expression expression2 = expressionStatement2.getExpression();
        assertTrue(expression2 instanceof ASBinaryExpression);
        ASBinaryExpression binaryExpression = (ASBinaryExpression) expression2;
        assertEquals(ASBinaryExpression.Op.MUL, binaryExpression.getOperator());
    }

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("StarAssign.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}