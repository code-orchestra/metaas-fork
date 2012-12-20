package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.ASTASBinaryExpression;
import uk.co.badgersinfoil.metaas.impl.ASTASForEachInStatement;
import uk.co.badgersinfoil.metaas.impl.ASTASForInStatement;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This tests checks if the for each in statement var declaration is parsed correctly.
 *
 * @author Alexander Eliseyev
 */
public class ForEachInStatementTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(0);
        assertNotNull(statement);
        assertTrue(statement instanceof ASForEachInStatement);

        ASForEachInStatement forEachInStatement1 = (ASForEachInStatement) myFuncMethod.getStatementList().get(0);
        assertTrue(forEachInStatement1.hasVarDeclaration());
        assertEquals("e", forEachInStatement1.getDeclarationStatement().getFirstVarName());
        assertEquals("integer", forEachInStatement1.getDeclarationStatement().getFirstVarType().getName());
        assertNull(forEachInStatement1.getLabel());

        ASForEachInStatement forEachInStatement2 = (ASForEachInStatement) myFuncMethod.getStatementList().get(1);
        assertFalse(forEachInStatement2.hasVarDeclaration());
        assertEquals("e", forEachInStatement2.getVarName());
        assertNull(forEachInStatement2.getLabel());
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("ForEachInStatement.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}