package uk.co.badgersinfoil.metaas.impl.xml;

import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLExpressionNode;
import uk.co.badgersinfoil.metaas.impl.ASTScriptElement;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

/**
 * @author Alexander Eliseyev
 */
public class ASTASXMLExpressionNode extends ASTScriptElement implements ASXMLExpressionNode {

    public ASTASXMLExpressionNode(LinkedListTree ast) {
        super(ast);
    }

    public Expression getExpression() {
        return ASTXMLUtils.getExpressionFromXMLExprAST(ast);
    }
}
