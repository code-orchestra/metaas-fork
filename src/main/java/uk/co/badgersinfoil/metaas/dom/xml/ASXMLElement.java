package uk.co.badgersinfoil.metaas.dom.xml;

import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ScriptElement;

import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public interface ASXMLElement extends ASXMLInitializer {

    String getNamespace();

    String getName();

    String getClosingNamespace();

    String getClosingName();

    Expression getClosingNameExpression();

    boolean isNameAnExpression();

    boolean isClosingNameAnExpression();

    Expression getNameExpression();
    
    List<ASXMLAttribute> getAttributes();

    boolean isEmpty();

    List<ASXMLInitializer> getChildren();

}
