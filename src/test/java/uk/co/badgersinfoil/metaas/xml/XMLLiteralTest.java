package uk.co.badgersinfoil.metaas.xml;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLAttribute;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLCDATA;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLElement;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLInitializer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * This tests checks if the parametrized XML literal is parsed correctly.
 *
 * @author Alexander Eliseyev
 */
public class XMLLiteralTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(0);
        assertNotNull(statement);
        assertTrue(statement instanceof ASExpressionStatement);

        ASExpressionStatement expressionStatement = (ASExpressionStatement) statement;
        Expression expression = expressionStatement.getExpression();

        assertTrue(expression instanceof ASXMLLiteral);
        ASXMLLiteral xmlLiteral = (ASXMLLiteral) expression;
        assertNotNull(xmlLiteral.getRoot());

        assertTrue(xmlLiteral.getRoot() instanceof ASXMLElement);
        
        // Root element
        ASXMLElement xmlElement = (ASXMLElement) xmlLiteral.getRoot();
        assertFalse(xmlElement.isNameAnExpression());
        assertEquals("a", xmlElement.getName());

        // Root element attributes
        List<ASXMLAttribute> attributes = xmlElement.getAttributes();
        assertEquals(2, attributes.size());

        // Regular attribute
        ASXMLAttribute attribute1 = attributes.get(0);
        assertFalse(attribute1.isNameAnExpression());
        assertEquals("b", attribute1.getName());
        assertFalse(attribute1.isValueAnExpression());
        assertEquals("3", attribute1.getValue());

        // Expression defined key-value
        ASXMLAttribute attribute2 = attributes.get(1);

        assertTrue(attribute2.isNameAnExpression());
        Expression nameExpression = attribute2.getNameExpression();
        assertTrue(nameExpression instanceof ASSimpleNameExpression);
        assertEquals("z", ((ASSimpleNameExpression) nameExpression).getName());

        assertTrue(attribute2.isValueAnExpression());
        Expression valueExpression = attribute2.getValueExpression();
        assertTrue(valueExpression instanceof ASIntegerLiteral);
        assertEquals(6, ((ASIntegerLiteral) valueExpression).getValue());

        // Sub-elements
        List<ASXMLInitializer> subs = xmlElement.getChildren();
        assertEquals(2, subs.size());

        // Sub-element - Element
        assertTrue(subs.get(0) instanceof ASXMLElement);

        ASXMLElement subElement = (ASXMLElement) subs.get(0);
        assertTrue(subElement.isNameAnExpression());
        Expression elNameExpression = subElement.getNameExpression();
        assertTrue(elNameExpression instanceof ASSimpleNameExpression);
        ASSimpleNameExpression simpleElNameExpression = (ASSimpleNameExpression) elNameExpression;
        assertEquals("x", simpleElNameExpression.getName());

        // Sub-element - CDATA
        assertTrue(subs.get(1) instanceof ASXMLCDATA);
        ASXMLCDATA cdata = (ASXMLCDATA) subs.get(1);
        assertEquals("<sender>John Smith</sender>", cdata.getData());
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("XMLLiteral.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}