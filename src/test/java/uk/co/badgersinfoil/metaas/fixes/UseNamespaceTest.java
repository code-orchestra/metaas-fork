package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Use namespace directives test.
 *
 * @author Alexander Eliseyev
 */
public class UseNamespaceTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASPackage asPackage = unit.getPackage();
        List<ASUseNamespaceStatement> useNamespaceDirectives = asPackage.getUseNamespaceDirectives();
        assertNotNull(useNamespaceDirectives);
        assertEquals(1, useNamespaceDirectives.size());

        ASUseNamespaceStatement packageUseNamespace = useNamespaceDirectives.get(0);
        assertEquals("mx_internal_2", packageUseNamespace.getNamespace());

        ASClassType classType = (ASClassType) unit.getType();
        ASUseNamespaceStatement asUseNamespaceStatement = classType.getUseNamespaceDirectives().get(0);
        assertEquals("mx_internal_3", asUseNamespaceStatement.getNamespace());

        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(0);
        assertTrue(statement instanceof ASUseNamespaceStatement);

        ASUseNamespaceStatement useNamespaceStatement = (ASUseNamespaceStatement) statement;
        assertEquals("mx_internal", useNamespaceStatement.getNamespace());
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("UseNamespace.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}