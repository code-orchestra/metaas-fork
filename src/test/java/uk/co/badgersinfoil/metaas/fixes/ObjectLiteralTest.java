package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Object literal key as a string literal test (RE-828)
 *
 * @author Alexander Eliseyev
 */
public class ObjectLiteralTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(0);
        assertNotNull(statement);
        assertTrue(statement instanceof ASDeclarationStatement);

        ASDeclarationStatement declarationStatement = (ASDeclarationStatement) statement;
        Expression expression = declarationStatement.getFirstVarInitializer();

        assertTrue(expression instanceof ASObjectLiteral);

        ASObjectLiteral objectLiteral = (ASObjectLiteral) expression;

        ASObjectLiteral.Field field1 = (ASObjectLiteral.Field) objectLiteral.getFields().get(0);
        assertEquals(field1.getName(), "\"id\"");

        ASObjectLiteral.Field field2 = (ASObjectLiteral.Field) objectLiteral.getFields().get(1);
        assertEquals(field2.getName(), "link");
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("ObjectLiteral.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}