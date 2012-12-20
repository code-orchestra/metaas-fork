package uk.co.badgersinfoil.metaas;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLInitializer;
import uk.co.badgersinfoil.metaas.impl.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.visitor.ActionScriptWalker;
import uk.co.badgersinfoil.metaas.visitor.FilterStrategy;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class TempTest extends TestCase {

    public void testIt() {
        ActionScriptFactory fact = new ActionScriptFactory();
        ASCompilationUnit result = loadSyntaxExample(fact);
        //ExpressionList result = loadExpression(fact);
        //ASTStatementList result = loadStatementList(fact);
        //List<ScriptElement> result = loadTypeMembers(fact);

        ASType type = result.getType();
        ASMethod main = type.getMethod("Main");
        List statementList = main.getStatementList();

        print(result.toString());

    }

    private static void print(String str) {
        System.out.println(str);
    }

    private static void print(int i) {
        System.out.println(i);
    }

    private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
        InputStream in = getClass().getClassLoader().getResourceAsStream("Test.as");
        ActionScriptParser parser = fact.newParser();
        ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
        return unit;
    }

    private ASTStatementList loadStatementList(ActionScriptFactory fact) {
        InputStream in = getClass().getClassLoader().getResourceAsStream("Test.as");
        ActionScriptParser parser = fact.newParser();
        ASTStatementList unit = parser.parseAsStatementList(new InputStreamReader(in));
        return unit;
    }

    private ExpressionList loadExpression(ActionScriptFactory fact) {
        InputStream in = getClass().getClassLoader().getResourceAsStream("Test.as");
        ActionScriptParser parser = fact.newParser();
        ExpressionList unit = parser.parseAsExpressionList(new InputStreamReader(in));
        return unit;
    }

    private List<ScriptElement> loadTypeMembers(ActionScriptFactory fact) {
        InputStream in = getClass().getClassLoader().getResourceAsStream("Test.as");
        ActionScriptParser parser = fact.newParser();
        List<ScriptElement> unit = parser.parseAsTypeMembers(new InputStreamReader(in));
        return unit;
    }
}
