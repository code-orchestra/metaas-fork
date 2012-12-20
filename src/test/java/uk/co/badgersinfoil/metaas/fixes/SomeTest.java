package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLAttribute;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLElement;
import uk.co.badgersinfoil.metaas.impl.ASTASSwitchCase;
import uk.co.badgersinfoil.metaas.impl.parser.e4x.E4XParser;
import uk.co.badgersinfoil.metaas.impl.util.UnicodeInputStream;
import uk.co.badgersinfoil.metaas.visitor.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Some test.
 *
 * @author Alexander Eliseyev
 */
public class SomeTest extends TestCase {


    public void testIt() throws Throwable {
        ActionScriptFactory fact = new ActionScriptFactory();
        ASCompilationUnit unit = loadSyntaxExample(fact);

        for (Statement oopStatement : unit.getOutOfPackageStatements()) {
            System.out.println("@@@ " + oopStatement);
        }

        ASClassType classType = (ASClassType) unit.getType();
        assertNotNull(classType);

        List<ASNamespaceDeclaration> namespaceDeclarations = classType.getNamespaceDeclarations();
        for (ASNamespaceDeclaration nsDeclaration : namespaceDeclarations) {
            System.out.println("NS -> " + nsDeclaration);
        }

        List<ASUseNamespaceStatement> useNamespaceDirectives = classType.getUseNamespaceDirectives();
        for (ASUseNamespaceStatement useNs : useNamespaceDirectives) {
            System.out.println("USE -> " + useNs);
        }

        /*
        final ASMethod method = classType.getMethod("myFunc");

        final ASExpressionStatement expressiontStatement = (ASExpressionStatement) method.getStatementList().get(0);
        final ASXMLLiteral xmlLiteral = (ASXMLLiteral) expressiontStatement.getExpression();
        ASXMLElement xmlElement = (ASXMLElement) xmlLiteral.getRoot();
        final ASXMLAttribute asxmlAttribute = xmlElement.getAttributes().get(0);

        System.out.println(asxmlAttribute.getValueExpression());
        System.out.println(asxmlAttribute.getValueExpression().getClass());

        */
    }

    private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
        InputStream in = getClass().getClassLoader().getResourceAsStream("SomeTest.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
    }
}