package uk.co.badgersinfoil.metaas.impl;

import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ExpressionList;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Alexander Eliseyev
 */
public class ExpressionListImpl implements ExpressionList {

    private List<Expression> expressions = new ArrayList<Expression>();

    public void addExpression(Expression expression) {
        expressions.add(expression);
    }

    public List<Expression> getExpressions() {
        return expressions;
    }
}
