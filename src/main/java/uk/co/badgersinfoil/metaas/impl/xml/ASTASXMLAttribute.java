package uk.co.badgersinfoil.metaas.impl.xml;

import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLAttribute;
import uk.co.badgersinfoil.metaas.impl.ASTScriptElement;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.impl.parser.e4x.E4XParser;

/**
 * @author Alexander Eliseyev
 */
public class ASTASXMLAttribute extends ASTScriptElement implements ASXMLAttribute {

    public ASTASXMLAttribute(LinkedListTree ast) {
        super(ast);
    }

    public Expression getNameExpression() {
        if (!isNameAnExpression()) {
            throw new IllegalStateException("Attribute name is not an expression, but a static name");
        }
        return ASTXMLUtils.getExpressionFromXMLExprAST(getAST().getFirstChild());
    }

    public Expression getValueExpression() {
        if (!isValueAnExpression()) {
            throw new IllegalStateException("Attribute value is not an expression, but a static value");
        }
        return ASTXMLUtils.getExpressionFromXMLExprAST(getAST().getLastChild());
    }

    public String getName() {
        if (isNameAnExpression()) {
            throw new IllegalStateException("Attribute name is an expression, not a static name");
        }
        String fqName = getAST().getFirstChild().getText();
        if (fqName.contains(":")) {
            return fqName.substring(fqName.indexOf(":") + 1, fqName.length());
        }
        return fqName;
    }

    public String getNamespace() {
        if (isNameAnExpression()) {
            throw new IllegalStateException("Attribute name is an expression, not a static name");
        }
        String fqName = getAST().getFirstChild().getText();
        if (fqName.contains(":")) {
            return fqName.substring(0, fqName.indexOf(":"));
        }
        return null;
    }

    public boolean isNameAnExpression() {
        return getAST().getFirstChild().getType() == E4XParser.XML_EXPRESSION;
    }

    public String getValue() {
        if (isValueAnExpression()) {
            throw new IllegalStateException("Attribute value is an expression, not a static value");
        }
        // If attribute is empty it doesn't get into the tree
        if (getAST().getChildCount() == 1) {
            return "";
        }

        String quotedValue = "";
        for (int i = 1; i < getAST().getChildCount(); i++) {
            quotedValue += getAST().getChild(i).getText();
        }

        // RE-3407
        // Attribute value is not a single token. It is three tokens - apostrophe/quote, value, apostrophe/quote
        // apostrophe/quote do not get into the tree, so here we have only value and don't need to extract it

//        if (quotedValue != null) {
//            if (quotedValue.startsWith("'") || quotedValue.startsWith("\"")) {
//                return quotedValue.substring(1, quotedValue.length() - 1);
//            }
//            throw new IllegalStateException("Attribute value is expected to be quoted either by ['] or [\"]");
//        }
//        return null;

        return quotedValue;
    }

     public boolean isValueAnExpression() {
        return getAST().getLastChild().getType() == E4XParser.XML_EXPRESSION;
    }
}
