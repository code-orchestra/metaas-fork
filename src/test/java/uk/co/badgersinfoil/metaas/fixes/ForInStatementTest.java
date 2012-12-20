package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.ASTASForInStatement;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This tests checks if the for in statement var declaration is parsed correctly.
 *
 * @author Alexander Eliseyev
 */
public class ForInStatementTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(0);
        assertNotNull(statement);
        assertTrue(statement instanceof ASForInStatement);

        ASForInStatement forInStatement1 = (ASForInStatement) myFuncMethod.getStatementList().get(0);
        assertTrue(forInStatement1.hasVarDeclaration());
        assertEquals("e", forInStatement1.getDeclarationStatement().getFirstVarName());
        assertEquals("integer", forInStatement1.getDeclarationStatement().getFirstVarType().getName());
        assertEquals("myLabel", forInStatement1.getLabel());

        ASForInStatement forInStatement2 = (ASForInStatement) myFuncMethod.getStatementList().get(1);
        assertFalse(forInStatement2.hasVarDeclaration());
        assertEquals("e", forInStatement2.getVarName());
        assertEquals("myLabel", forInStatement2.getLabel());
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("ForInStatement.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}