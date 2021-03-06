package uk.co.badgersinfoil.metaas.xml;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLAttribute;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLElement;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLInitializer;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLTextNode;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This tests checks if the XML element and attributes namespaces are parsed correctly.
 *
 * @author Alexander Eliseyev
 */
public class XMLNamespaceTest extends TestCase {
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
        assertEquals("myNamespace", xmlElement.getNamespace());

        // Attribute
        assertFalse(xmlElement.getAttributes().isEmpty());
        ASXMLAttribute xmlAttribute = xmlElement.getAttributes().get(0);        
        assertEquals("b", xmlAttribute.getName());
        assertEquals("anotherNamespace", xmlAttribute.getNamespace());
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("XMLNamespace.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}