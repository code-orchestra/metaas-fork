package uk.co.badgersinfoil.metaas.impl;

import uk.co.badgersinfoil.metaas.dom.ASDeleteExpression;

import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

/**
 * AS delete expression impl.
 */
public class ASTASDeleteExpression extends ASTExpression implements ASDeleteExpression{

    public ASTASDeleteExpression(LinkedListTree ast) {
        super(ast);
    }

    public Expression getSubexpression() {
        return ExpressionBuilder.build(ast.getFirstChild());
    }

    public void setSubexpression(Expression subexpression) {
        ASTExpression sub = (ASTExpression)subexpression;
        ast.setChildWithTokens(0, sub.getAST());
    }
}
