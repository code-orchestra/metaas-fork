package uk.co.badgersinfoil.metaas.impl;

import uk.co.badgersinfoil.metaas.dom.ASTypeOfExpression;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

/**
 * @author Alexander Eliseyev
 */
public class ASTASTypeOfExpression extends ASTExpression implements ASTypeOfExpression {

    public ASTASTypeOfExpression(LinkedListTree ast) {
        super(ast);
    }

    public Expression getSubexpression() {
        return ExpressionBuilder.build(ast.getFirstChild());
    }
}
