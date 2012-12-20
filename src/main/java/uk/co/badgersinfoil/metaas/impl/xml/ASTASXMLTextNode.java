package uk.co.badgersinfoil.metaas.impl.xml;

import uk.co.badgersinfoil.metaas.dom.xml.ASXMLTextNode;
import uk.co.badgersinfoil.metaas.impl.ASTScriptElement;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

/**
 * @author Alexander Eliseyev
 */
public class ASTASXMLTextNode extends ASTScriptElement implements ASXMLTextNode {

    private String staticValue;

    public ASTASXMLTextNode(LinkedListTree ast) {
        super(ast);
    }

    public String getText() {
        if (staticValue == null) {
            return ast.getFirstChild().getText();
        }
        return staticValue;
    }

    public void glue(ASTASXMLTextNode textNode) {
        this.staticValue = getText() + textNode.getText();
    }
    
}
