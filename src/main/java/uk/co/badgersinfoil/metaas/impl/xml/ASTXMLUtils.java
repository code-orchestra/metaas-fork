package uk.co.badgersinfoil.metaas.impl.xml;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.impl.AS3FragmentParser;
import uk.co.badgersinfoil.metaas.impl.ExpressionBuilder;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.impl.parser.e4x.E4XParser;

/**
 * @author Alexander Eliseyev
 */
public final class ASTXMLUtils {

    public static Expression getExpressionFromXMLExprAST(LinkedListTree ast) {
        if (ast == null) {
            return null;
        }
        if (ast.getType() != E4XParser.XML_EXPRESSION) {
            throw new IllegalArgumentException("XML_EXPRESSION AST expected");
        }
        if (ast.getChildCount() == 0) {
            return null;
        }

        LinkedListTree expressionAST = AS3FragmentParser.parseExpr(getExpressionStringFromXMLExprAST(ast));
        return ExpressionBuilder.build(expressionAST);
    }

    private static String getExpressionStringFromXMLExprAST(LinkedListTree ast) {
        if (ast == null) {
            return null;
        }
        if (ast.getType() != E4XParser.XML_EXPRESSION) {
            throw new IllegalArgumentException("XML_EXPRESSION AST expected");
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < ast.getChildCount(); i++) {
            final LinkedListTree expressionContainerCandidate = (LinkedListTree) ast.getChild(i);
            if (expressionContainerCandidate.getType() == E4XParser.XML_EXPRESSION) {
                sb.append("{");
                sb.append(getExpressionStringFromXMLExprAST(expressionContainerCandidate));
                sb.append("}");
            } else if (expressionContainerCandidate.getType() == E4XParser.XML_WS) {
                // Do nothing
            } else {
                sb.append(expressionContainerCandidate.getText());
            }
        }

        return sb.toString();
    }

    public static String getTaggedData(LinkedListTree ast, String start, String end, String type) {
        String taggedCDATA = ast.getText();
        if (taggedCDATA == null) {
            return null;
        }

        taggedCDATA = taggedCDATA.trim();

        if (taggedCDATA.startsWith(start) && taggedCDATA.endsWith(end)) {
            return taggedCDATA.substring(start.length(), taggedCDATA.length() - end.length());
        }
        throw new IllegalStateException("Illegal " + type + ": " + ast.getText());

    }

}
