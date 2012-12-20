package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLInitializer;
import uk.co.badgersinfoil.metaas.impl.parser.e4x.E4XParser;
import uk.co.badgersinfoil.metaas.impl.xml.ASTASXMLElement;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Some test.
 *
 * @author Alexander Eliseyev
 */
public class EmptyXmlLiteralTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

        ASClassType classType = (ASClassType) unit.getType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(0);
        assertTrue(statement instanceof ASExpressionStatement);

        ASExpressionStatement expressionStatement = (ASExpressionStatement) statement;
        Expression expression = expressionStatement.getExpression();

        assertTrue(expression instanceof ASXMLLiteral);
        ASXMLLiteral xmLiteral = (ASXMLLiteral) expression;

        print(xmLiteral.toString());

        ASXMLInitializer asxmlInitializer = xmLiteral.getRoot();
        assert(asxmlInitializer instanceof ASTASXMLElement);
        assert(((ASTASXMLElement) asxmlInitializer).getAST().getType() == E4XParser.XML_LIST);
    }

    private static void print(String str) {
        System.out.println(str);
    }

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("EmptyXmlLiteral.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}