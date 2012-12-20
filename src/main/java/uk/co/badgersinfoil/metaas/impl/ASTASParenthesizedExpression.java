package uk.co.badgersinfoil.metaas.impl;

import org.antlr.runtime.tree.Tree;
import uk.co.badgersinfoil.metaas.dom.ASParenthesizedExpression;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class ASTASParenthesizedExpression extends ASTExpression implements ASParenthesizedExpression {

    public ASTASParenthesizedExpression(LinkedListTree ast) {
		super(ast);
	}

    public List<Expression> getSubExpressions() {
        List<Expression> expressions = new ArrayList<Expression>();
        LinkedListTree ast = getAST();
        for (int i = 0; i < ast.getChildCount(); i++) {
            Tree child = ast.getChild(i);
            if (child instanceof LinkedListTree) {
                expressions.add(ExpressionBuilder.build((LinkedListTree) child));
            }
        }
        return expressions;
    }

    @Deprecated
    public Expression getSubExpression() {
        return ExpressionBuilder.build(getAST().getFirstChild());
    }
}
