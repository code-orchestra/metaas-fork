package uk.co.badgersinfoil.metaas.impl.xml;

import uk.co.badgersinfoil.metaas.dom.xml.ASXMLComment;
import uk.co.badgersinfoil.metaas.impl.ASTScriptElement;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

/**
 * @author Anton.I.Neverov
 */
public class ASTASXMLComment extends ASTScriptElement implements ASXMLComment {

    private static final String START = "<!--";
    private static final String END = "-->";
    private static final String TYPE = "comment";

    public ASTASXMLComment(LinkedListTree ast) {
        super(ast);
    }
    public String getData() {
        return ASTXMLUtils.getTaggedData(getAST(), START, END, TYPE);
    }
}
