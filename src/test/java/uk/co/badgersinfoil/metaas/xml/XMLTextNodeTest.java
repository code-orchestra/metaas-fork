package uk.co.badgersinfoil.metaas.xml;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.dom.xml.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * This tests checks if the text XML node is parsed correctly.
 *
 * @author Alexander Eliseyev
 */
public class XMLTextNodeTest extends TestCase {
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

        assertFalse(xmlElement.getChildren().isEmpty());

        // Child text node 1
        ASXMLInitializer childInitializer1 = xmlElement.getChildren().get(0);
        assertTrue(childInitializer1 instanceof ASXMLTextNode);
        ASXMLTextNode textNode = (ASXMLTextNode) childInitializer1;
        assertEquals("myText", textNode.getText());

        // Child expression node 2
        ASXMLInitializer childInitializer2 = xmlElement.getChildren().get(1);
        assertTrue(childInitializer2 instanceof ASXMLExpressionNode);
        ASXMLExpressionNode expressionNode = (ASXMLExpressionNode) childInitializer2;
        Expression exprNodeExpression = expressionNode.getExpression();
        assertNotNull(exprNodeExpression);
        assertTrue(exprNodeExpression instanceof ASSimpleNameExpression);
        assertEquals("b", (((ASSimpleNameExpression) exprNodeExpression).getName()));

        // Child text node 1
        ASXMLInitializer childInitializer3 = xmlElement.getChildren().get(2);
        assertTrue(childInitializer3 instanceof ASXMLTextNode);
        ASXMLTextNode textNode2 = (ASXMLTextNode) childInitializer3;
        assertEquals("another Text", textNode2.getText());
	}

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("XMLTextNode.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}