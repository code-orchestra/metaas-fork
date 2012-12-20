package uk.co.badgersinfoil.metaas.impl.xml;

import uk.co.badgersinfoil.metaas.dom.xml.ASXMLAttribute;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLPI;
import uk.co.badgersinfoil.metaas.impl.ASTScriptElement;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.impl.parser.e4x.E4XParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ASTASXMLPI extends ASTScriptElement implements ASXMLPI {

    public ASTASXMLPI(LinkedListTree ast) {
        super(ast);
    }

    public String getName() {
        LinkedListTree nameAST = ASTUtils.findChildByType(ast, E4XParser.XML_NAME);
        if (nameAST != null) {
            String elementFQName = nameAST.getText();
            if (elementFQName.contains(":")) {
                return elementFQName.substring(elementFQName.indexOf(":") + 1, elementFQName.length());
            }
            return nameAST.getText();
        }
        return null;
    }

    public List<ASXMLAttribute> getAttributes() {
        List<ASXMLAttribute> result = new ArrayList<ASXMLAttribute>();
        for (LinkedListTree attrAST : ASTUtils.findChildrenByType(ast, E4XParser.XML_ATTRIBUTE)) {
            result.add(new ASTASXMLAttribute(attrAST));
        }
        return result;
    }
}
