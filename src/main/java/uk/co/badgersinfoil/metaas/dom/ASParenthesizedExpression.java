package uk.co.badgersinfoil.metaas.dom;

import java.util.List;

/**
 * Parenthesized expression: ( subExpression ).
 *
 * @author Alexander Eliseyev
 */
public interface ASParenthesizedExpression extends Expression {

    List<Expression> getSubExpressions();

    @Deprecated
    Expression getSubExpression();

}
