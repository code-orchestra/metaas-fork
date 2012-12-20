package uk.co.badgersinfoil.metaas.impl.util;

import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.dom.ASFieldAccessExpression;
import uk.co.badgersinfoil.metaas.dom.AttributeExpression;
import uk.co.badgersinfoil.metaas.dom.Expression;

/**
 * @author Alexander Eliseyev
 */
public final class AttributeExpressionHelper {

    private static final ActionScriptFactory asf = new ActionScriptFactory();

    private AttributeExpressionHelper() {        
    }

    public static AttributeExpression getAttributeExpression(ASFieldAccessExpression fieldAccessExpression) {
        if (fieldAccessExpression == null) {
            return null;
        }
        try {
            Expression expression = asf.newExpression(fieldAccessExpression.getName());
            if (expression != null && expression instanceof AttributeExpression) {
                return (AttributeExpression) expression;
            }
            return null;
        } catch (Throwable th) {
            return null;
        }
    }

}
