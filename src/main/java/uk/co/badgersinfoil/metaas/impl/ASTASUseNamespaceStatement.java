package uk.co.badgersinfoil.metaas.impl;

import uk.co.badgersinfoil.metaas.dom.ASUseNamespaceStatement;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class ASTASUseNamespaceStatement extends ASTScriptElement implements ASUseNamespaceStatement {

    public ASTASUseNamespaceStatement(LinkedListTree ast) {
        super(ast);
    }

    public String getNamespace() {
        LinkedListTree child = ast.getFirstChild();
        if (child != null) {
            return child.getText();
        }
        return null;
    }

    public List<Comment> getCommentsAfter() {
        return CommentUtils.getCommentAfter(this.getAST());
    }

    public int getSpacerSize() {
        return SpacerUtil.calculateSpacerSize(ast);
    }
}
