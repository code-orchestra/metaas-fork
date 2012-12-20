package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.visitor.ActionScriptWalker;
import uk.co.badgersinfoil.metaas.visitor.FilterStrategy;
import uk.co.badgersinfoil.metaas.visitor.ScriptElementStrategy;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Bitwise NOT (~) parsing test.
 *
 * @author Alexander Eliseyev
 */
public class BitwiseNotTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        ActionScriptWalker walker = new ActionScriptWalker(new FilterStrategy(new ScriptElementStrategy() {
            public void handle(ScriptElement element) {
                // do nothing
            }
        }));
        walker.walk(myFuncMethod);

        Statement statement0 = (Statement) myFuncMethod.getStatementList().get(0);
        assertNotNull(statement0);
        assertTrue(statement0 instanceof ASExpressionStatement);

        ASExpressionStatement asExpressionStatement = (ASExpressionStatement) statement0;
        Expression expression = asExpressionStatement.getExpression();

        assertTrue(expression instanceof ASPrefixExpression);
        ASPrefixExpression prefixExpression = (ASPrefixExpression) expression;

        print(prefixExpression.getOperator().toString());
    }

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("BitwiseNot.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}