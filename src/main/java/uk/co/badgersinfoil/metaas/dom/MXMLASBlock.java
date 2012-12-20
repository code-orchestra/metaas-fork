package uk.co.badgersinfoil.metaas.dom;

import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public interface MXMLASBlock extends ScriptElement{
    public List<String> getImports();
    public List<ASField> getFields();
    public List<ASMethod> getMethods();
}
