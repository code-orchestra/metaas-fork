package uk.co.badgersinfoil.metaas.dom;

/**
 * Delete expression.
 *
 * @author Alexander Eliseyev
 */
public interface ASDeleteExpression extends Expression {

    public Expression getSubexpression();
    public void setSubexpression(Expression expression);    

}
