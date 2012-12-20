package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.ASTStatementList;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class NestedVectorTest extends TestCase {

    public void testIt() {
        ActionScriptFactory fact = new ActionScriptFactory();
        ASTStatementList result = loadStatementList(fact);

        List statementList = result.getStatementList();

        Expression expr;

        for (int i = 0; i < 8; i++) {
            expr = ((ASExpressionStatement) statementList.get(i)).getSubExpressions().get(0);
            assertTrue(expr instanceof ASInvocationExpression);
            expr = ((ASInvocationExpression) expr).getTargetExpression();
            assertTrue(expr instanceof ASVectorExpression);
        }

        expr = ((ASExpressionStatement) statementList.get(5)).getSubExpressions().get(0);
        expr = ((ASInvocationExpression) expr).getTargetExpression();
        TypeDescriptor typeDescriptor = ((ASVectorExpression) expr).getTypeDescriptor();
        assertEquals("a.A", typeDescriptor.getParameter().getName());

        expr = ((ASExpressionStatement) statementList.get(7)).getSubExpressions().get(0);
        expr = ((ASInvocationExpression) expr).getTargetExpression();
        typeDescriptor = ((ASVectorExpression) expr).getTypeDescriptor();
        for (int i = 0; i < 7; i++)
            typeDescriptor = typeDescriptor.getParameter();
        assertEquals("a.a.a.a.a.a.a.A", typeDescriptor.getParameter().getName());

        expr = ((ASExpressionStatement) statementList.get(8)).getSubExpressions().get(0);
        expr = ((ASNewExpression) expr).getTargetExpression();
        assertTrue(expr instanceof ASVectorExpression);

        expr = ((ASExpressionStatement) statementList.get(9)).getSubExpressions().get(0);
        assertEquals("e", ((ASSimpleNameExpression) ((ASVectorLiteral) expr).getEntries().get(2)).getName());
        typeDescriptor = ((ASVectorLiteral) expr).getTypeDescriptor();
        assertEquals("A", typeDescriptor.getName());

        expr = ((ASExpressionStatement) statementList.get(10)).getSubExpressions().get(0);
        assertEquals("e", ((ASSimpleNameExpression) ((ASVectorLiteral) expr).getEntries().get(2)).getName());
        typeDescriptor = ((ASVectorLiteral) expr).getTypeDescriptor();
        typeDescriptor = typeDescriptor.getParameter();
        assertEquals("A", typeDescriptor.getName());

        assertTrue(((ASBinaryExpression) ((ASExpressionStatement) statementList.get(11)).getSubExpressions().get(0)).getOperator().equals(ASBinaryExpression.Op.GT));
        assertTrue(((ASBinaryExpression) ((ASExpressionStatement) statementList.get(12)).getSubExpressions().get(0)).getOperator().equals(ASBinaryExpression.Op.GE));
        assertTrue(((ASBinaryExpression) ((ASExpressionStatement) statementList.get(13)).getSubExpressions().get(0)).getOperator().equals(ASBinaryExpression.Op.SR));
        assertTrue(((ASBinaryExpression) ((ASExpressionStatement) statementList.get(14)).getSubExpressions().get(0)).getOperator().equals(ASBinaryExpression.Op.SRU));
        assertTrue(((ASAssignmentExpression) ((ASExpressionStatement) statementList.get(15)).getSubExpressions().get(0)).getOperator().equals(ASAssignmentExpression.Op.SR_ASSIGN));
        assertTrue(((ASAssignmentExpression) ((ASExpressionStatement) statementList.get(16)).getSubExpressions().get(0)).getOperator().equals(ASAssignmentExpression.Op.SRU_ASSIGN));

        for (int i = 17; i < 45; i++)
            assertEquals("Vector", ((ASDeclarationStatement) statementList.get(i)).getFirstVarType().getName());
    }

    private static void print(String str) {
        System.out.println(str);
    }

    private static void print(int i) {
        System.out.println(i);
    }

    private ASTStatementList loadStatementList(ActionScriptFactory fact) {
        InputStream in = getClass().getClassLoader().getResourceAsStream("NestedVectorTest.as");
        ActionScriptParser parser = fact.newParser();
        ASTStatementList unit = parser.parseAsStatementList(new InputStreamReader(in));
        return unit;
    }

}
