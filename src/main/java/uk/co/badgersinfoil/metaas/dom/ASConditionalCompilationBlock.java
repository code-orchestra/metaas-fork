package uk.co.badgersinfoil.metaas.dom;

/**
 * @author Anton.I.Neverov
 */
public interface ASConditionalCompilationBlock extends Statement {

    public ASBlock getBlock();

    public String getNamespace();

    public String getIdentifier();

}
