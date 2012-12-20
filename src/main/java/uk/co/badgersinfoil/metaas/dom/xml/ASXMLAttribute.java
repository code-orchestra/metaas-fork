package uk.co.badgersinfoil.metaas.dom.xml;

import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ScriptElement;

/**
 * @author Alexander Eliseyev
 */
public interface ASXMLAttribute extends ScriptElement {

    String getNamespace();

    String getName();

    boolean isNameAnExpression();

    Expression getNameExpression();

    String getValue();

    boolean isValueAnExpression();

    Expression getValueExpression();

}
