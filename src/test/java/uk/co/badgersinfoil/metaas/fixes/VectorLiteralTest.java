package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Vector literal expression parsing test.
 *
 * @author Alexander Eliseyev
 */
public class VectorLiteralTest extends TestCase {

	public void testIt() throws Throwable {
        ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASField field = classType.getField("_DELTA_INDEX");
        Expression expr = field.getInitializer();

        assertTrue(expr instanceof ASVectorLiteral);

        ASVectorLiteral vectorLiteral = (ASVectorLiteral) expr;
        assertNotNull(vectorLiteral.getTypeDescriptor());

        String vectorType = vectorLiteral.getTypeDescriptor().getName();
        assertEquals("Number", vectorType);
        assertEquals(101, vectorLiteral.getEntries().size());        
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("VectorLiteral.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}