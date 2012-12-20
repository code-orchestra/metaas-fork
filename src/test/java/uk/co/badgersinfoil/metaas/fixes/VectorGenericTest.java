package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.ASTASVarDeclarationFragment;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This tests checks if the parametrized type is parsed correctly.
 *
 * @author Alexander Eliseyev
 */
public class VectorGenericTest extends TestCase {

    public void testMethodDeclaration() {
       ASClassType classType = getClassType();
       ASMethod myFuncMethod = classType.getMethod("myFunc");

        assertNotNull(myFuncMethod);
        assertNotNull(myFuncMethod.getType());
        assertEquals("Vector", myFuncMethod.getType().getName());

        TypeDescriptor parameter = myFuncMethod.getType().getParameter();
        assertNotNull(parameter);
        assertEquals("String", parameter.getName());
        assertNull(parameter.getParameter());
    }

    public void testFieldDeclaration() {
        ASClassType classType = getClassType();
        ASField myField = classType.getField("myField");

        assertNotNull(myField);
        assertNotNull(myField.getType());
        assertEquals("Vector", myField.getType().getName());

        TypeDescriptor parameter = myField.getType().getParameter();
        assertNotNull(parameter);
        assertEquals("String", parameter.getName());
        assertNull(parameter.getParameter());
    }

    public void testNewExpression() {
       ASClassType classType = getClassType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(1);
        assertNotNull(statement);
        assertTrue(statement instanceof ASExpressionStatement);

        Expression expression = ((ASExpressionStatement) statement).getExpression();
        assertTrue(expression instanceof ASNewExpression);

        ASNewExpression newExpression = (ASNewExpression) expression;
        Expression targetExpression = newExpression.getTargetExpression();

        assertTrue(targetExpression instanceof ASVectorExpression);
        ASVectorExpression vectorExpression = (ASVectorExpression) targetExpression;

        TypeDescriptor typeDescriptor = vectorExpression.getTypeDescriptor();
        assertEquals("String", typeDescriptor.getParameter().getName());
    }

    public void testVariableDeclaration() {
        ASClassType classType = getClassType();
        ASMethod myFuncMethod = classType.getMethod("myFunc");

        Statement statement = (Statement) myFuncMethod.getStatementList().get(0);
        assertNotNull(statement);
        assertTrue(statement instanceof ASDeclarationStatement);

        ASDeclarationStatement declarationStatement = (ASDeclarationStatement) statement;
        assertEquals(1, declarationStatement.getVars().size());

        ASTASVarDeclarationFragment asVarDeclarationFragment = (ASTASVarDeclarationFragment) declarationStatement.getVars().get(0);
        assertNotNull(asVarDeclarationFragment.getTypeDescriptor());

        TypeDescriptor parameterDescriptor = asVarDeclarationFragment.getTypeDescriptor();

        assertEquals("Vector", parameterDescriptor.getName());
        assertNotNull(parameterDescriptor.getParameter());

        TypeDescriptor parameterTypeDescriptor = parameterDescriptor.getParameter();
        assertEquals("Vector", parameterTypeDescriptor.getName());
        assertNotNull(parameterTypeDescriptor.getParameter());

        TypeDescriptor subParameterDescriptor = parameterTypeDescriptor.getParameter();
        assertEquals("String", subParameterDescriptor.getName());
        assertNull(subParameterDescriptor.getParameter());
    }

    private ASClassType getClassType() {
        ActionScriptFactory fact = new ActionScriptFactory();
        ASCompilationUnit unit = loadSyntaxExample(fact);

        return (ASClassType) unit.getType();
    }


    private static void print(String str) {
        System.out.println(str);
    }

    private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
        InputStream in = getClass().getClassLoader().getResourceAsStream("VectorGeneric.as");
        ActionScriptParser parser = fact.newParser();
        ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
        return unit;
    }
}