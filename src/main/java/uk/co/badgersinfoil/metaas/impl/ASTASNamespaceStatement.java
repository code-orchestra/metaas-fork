package uk.co.badgersinfoil.metaas.impl;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASNamespaceStatement;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ASTASNamespaceStatement extends ASTScriptElement implements ASNamespaceStatement {

    public ASTASNamespaceStatement(LinkedListTree ast) {
        super(ast);
    }

    public String getName() {
        return ASTUtils.findChildByType(ast, AS3Parser.IDENT).getText();
    }

    public String getURI() {
        LinkedListTree uriTree = ASTUtils.findChildByType(ast, AS3Parser.STRING_LITERAL);
        if (uriTree == null) {
            return null;
        }
        return new ASTASStringLiteral(uriTree).getValue();
    }

    public List<Comment> getCommentsAfter() {
        return CommentUtils.getCommentAfter(this.getAST());
    }

    public int getSpacerSize() {
        return SpacerUtil.calculateSpacerSize(ast);
    }
}
