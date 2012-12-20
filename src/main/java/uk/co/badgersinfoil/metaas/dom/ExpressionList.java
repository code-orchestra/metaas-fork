package uk.co.badgersinfoil.metaas.dom;

import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public interface ExpressionList extends ScriptElement {

    List<Expression> getExpressions();

    void addExpression(Expression expression);

}


