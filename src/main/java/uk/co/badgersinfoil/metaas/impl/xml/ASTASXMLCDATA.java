package uk.co.badgersinfoil.metaas.impl.xml;

import uk.co.badgersinfoil.metaas.dom.xml.ASXMLCDATA;
import uk.co.badgersinfoil.metaas.impl.ASTScriptElement;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

/**
 * @author Alexander Eliseyev
 */
public class ASTASXMLCDATA extends ASTScriptElement implements ASXMLCDATA {

    private static final String START = "<![CDATA[";
    private static final String END = "]]>";
    private static final String TYPE = "CDATA";

    public ASTASXMLCDATA(LinkedListTree ast) {
        super(ast);
    }

    public String getData() {
        return ASTXMLUtils.getTaggedData(getAST(), START, END, TYPE);
    }
}
