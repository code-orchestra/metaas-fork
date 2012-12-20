package uk.co.badgersinfoil.metaas.dom.xml;

import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public interface ASXMLPI extends ASXMLInitializer {

    String getName();

    List<ASXMLAttribute> getAttributes();

}
