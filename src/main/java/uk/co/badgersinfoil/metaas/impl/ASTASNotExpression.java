package uk.co.badgersinfoil.metaas.impl;

import uk.co.badgersinfoil.metaas.dom.ASNotExpression;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

/**
 * @author Anton.I.Neverov
 */
public class ASTASNotExpression extends ASTExpression implements ASNotExpression {

	public ASTASNotExpression(LinkedListTree ast) {
		super(ast);
	}

    public Expression getSubexpression() {
		return ExpressionBuilder.build(ast.getFirstChild());
    }
}
