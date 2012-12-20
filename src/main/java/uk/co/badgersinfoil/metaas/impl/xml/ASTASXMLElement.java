package uk.co.badgersinfoil.metaas.impl.xml;

import org.antlr.runtime.tree.Tree;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLAttribute;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLElement;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLInitializer;
import uk.co.badgersinfoil.metaas.impl.ASTScriptElement;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.impl.parser.e4x.E4XParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class ASTASXMLElement extends ASTScriptElement implements ASXMLElement {

    public ASTASXMLElement(LinkedListTree ast) {
        super(ast);
    }

    public String getClosingNamespace() {
        if (isEmpty()) {
            throw new IllegalStateException("Element is empty, no closing name available");
        }
        if (isClosingNameAnExpression()) {
            throw new IllegalStateException("Element name is an expression, not a static name");
        }
        LinkedListTree nameAST = getClosingNameAST();
        if (nameAST != null) {
            String elementFQName = nameAST.getText();
            if (elementFQName.contains(":")) {
                return elementFQName.substring(0, elementFQName.indexOf(":"));
            }
        }
        return null;
    }

    public String getClosingName() {
        if (isEmpty()) {
            throw new IllegalStateException("Element is empty, no closing name available");
        }
        if (isClosingNameAnExpression()) {
            throw new IllegalStateException("Element name is an expression, not a static name");
        }
        LinkedListTree nameAST = getClosingNameAST();
        if (nameAST != null) {
            String elementFQName = nameAST.getText();
            if (elementFQName.contains(":")) {
                return elementFQName.substring(elementFQName.indexOf(":") + 1, elementFQName.length());
            }
            return nameAST.getText();
        }
        return null;
    }

    public Expression getClosingNameExpression() {
        if (isEmpty()) {
            throw new IllegalStateException("Element is empty, no closing name available");
        }
        if (!isClosingNameAnExpression()) {
            throw new IllegalStateException("Closing element name is not an expression, but a static name");
        }
        return ASTXMLUtils.getExpressionFromXMLExprAST(getClosingNameAST());
    }

    public boolean isClosingNameAnExpression() {
        if (isEmpty()) {
            throw new IllegalStateException("Element is empty, no closing name available");
        }
        return ast.getType() != E4XParser.XML_LIST && getClosingNameAST().getType() == E4XParser.XML_EXPRESSION;
    }

    private LinkedListTree getClosingNameAST() {
        if (ast.getType() == E4XParser.XML_LIST) {
            return null;
        }
        for (int i = 1; i < ast.getChildCount(); i++) {
            Object astObj = ast.getChild(i);
            if (astObj instanceof LinkedListTree) {
                LinkedListTree ast = (LinkedListTree) astObj;
                if (ast.getType() != E4XParser.XML_ATTRIBUTE) {
                    return ast;
                }
            }
        }
        return null;
    }

    public boolean isEmpty() {
        if (ast.getType() == E4XParser.XML_ELEMENT) {
            return false;
        } else if (ast.getType() == E4XParser.XML_EMPTY_ELEMENT) {
            return true;
        } else if (ast.getType() == E4XParser.XML_LIST) {
            return false;
        }
        throw new IllegalStateException("Must have type XML_ELEMENT or XML_EMPTY_ELEMENT, or XML_LIST");
    }

    public List<ASXMLAttribute> getAttributes() {
        List<ASXMLAttribute> result = new ArrayList<ASXMLAttribute>();
        for (LinkedListTree attrAST : ASTUtils.findChildrenByType(ast, E4XParser.XML_ATTRIBUTE)) {
            result.add(new ASTASXMLAttribute(attrAST));
        }
        return result;
    }

    public boolean isNameAnExpression() {
        return ast.getType() != E4XParser.XML_LIST && getAST().getFirstChild().getType() == E4XParser.XML_EXPRESSION;
    }

    public List<ASXMLInitializer> getChildren() {
        List<ASXMLInitializer> children = new ArrayList<ASXMLInitializer>();

        // First AST child is the element's name
        int firstElementChild = 1;
        if (!isEmpty()) {
            // Second AST child is the closing element's name in case of not empty element
            firstElementChild = 2;
        }
        // XML_LIST has neither name, nor closing name
        if (ast.getType() == E4XParser.XML_LIST) {
            firstElementChild = 0;
        }

        for (int i = firstElementChild; i < ast.getChildCount(); i++) {
            Object astObj = ast.getChild(i);
            if (astObj instanceof LinkedListTree) {
                LinkedListTree subTree = (LinkedListTree) astObj;
                if (subTree.getType() == E4XParser.XML_ELEMENT) {
                    children.add(new ASTASXMLElement(subTree));
                } else if (subTree.getType() == E4XParser.XML_EMPTY_ELEMENT) {
                    children.add(new ASTASXMLElement(subTree));
                } else if (subTree.getType() == E4XParser.XML_EXPRESSION) {
                    children.add(new ASTASXMLExpressionNode(subTree));
                } else if (subTree.getType() == E4XParser.XML_TEXT_NODE) {
                    // This nasty hack is intended to glue the sibling text nodes if they aren't
                    // separated by a node of another kind

                    // Look ahead, if the next child is also a text node, glue it together and
                    // increment the 'i' to jump it over
                    ASTASXMLTextNode textNode = new ASTASXMLTextNode(subTree);

                    int skipChildren = 0;
                    if (i < ast.getChildCount() - 1) {
                        if (textNode.getText().trim().length() == 0) {
                            // Trim empty (trim().length==0) text nodes until unempty text node comes                            
                            for (int j = i + 1; j < ast.getChildCount(); j++) {
                                Object lookAheadAstObj = ast.getChild(j);
                                if (lookAheadAstObj instanceof LinkedListTree) {
                                    LinkedListTree lookAheadTree = (LinkedListTree) lookAheadAstObj;
                                    if (lookAheadTree.getType() == E4XParser.XML_TEXT_NODE) {
                                        ASTASXMLTextNode lookAheadTextNode = new ASTASXMLTextNode(lookAheadTree);
                                        if (lookAheadTextNode.getText().trim().length() == 0) {
                                            skipChildren++;
                                        } else {
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                        } else {
                            // Glue unempty text nodes together
                            for (int j = i + 1; j < ast.getChildCount(); j++) {
                                Object lookAheadAstObj = ast.getChild(j);
                                if (lookAheadAstObj instanceof LinkedListTree) {
                                    LinkedListTree lookAheadTree = (LinkedListTree) lookAheadAstObj;
                                    if (lookAheadTree.getType() == E4XParser.XML_TEXT_NODE) {
                                        skipChildren++;
                                        textNode.glue(new ASTASXMLTextNode(lookAheadTree));
                                    } else {
                                        break;
                                    }
                                }
                            }
                            children.add(textNode);
                        }
                        i += skipChildren;
                    } else {
                        if (textNode.getText().trim().length() > 0) {
                            children.add(textNode);
                        }
                    }
                } else if (subTree.getType() == E4XParser.XML_CDATA) {
                    children.add(new ASTASXMLCDATA(subTree));
                } else if (subTree.getType() == E4XParser.XML_COMMENT) {
                    children.add(new ASTASXMLComment(subTree));
                } else if (subTree.getType() == E4XParser.XML_PI) {
                    children.add(new ASTASXMLPI(subTree));
                }
            }
        }

        return children;
    }

    public Expression getNameExpression() {
        if (!isNameAnExpression()) {
            throw new IllegalStateException("Element name is not an expression, but a static name");
        }
        return ASTXMLUtils.getExpressionFromXMLExprAST(getAST().getFirstChild());
    }

    public String getName() {
        if (isNameAnExpression()) {
            throw new IllegalStateException("Element name is an expression, not a static name");
        }
        if (ast.getType() == E4XParser.XML_LIST) {
            return null;
        }
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

    public String getNamespace() {
        if (isNameAnExpression()) {
            throw new IllegalStateException("Element name is an expression, not a static name");
        }
        LinkedListTree nameAST = ASTUtils.findChildByType(ast, E4XParser.XML_NAME);
        if (nameAST != null) {
            String elementFQName = nameAST.getText();
            if (elementFQName.contains(":")) {
                return elementFQName.substring(0, elementFQName.indexOf(":"));
            }
        }
        return null;
    }
}
