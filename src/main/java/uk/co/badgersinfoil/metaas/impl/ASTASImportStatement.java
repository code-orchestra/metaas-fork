package uk.co.badgersinfoil.metaas.impl;

import uk.co.badgersinfoil.metaas.dom.ASImportStatement;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ASTASImportStatement extends ASTScriptElement implements ASImportStatement {

    public ASTASImportStatement(LinkedListTree ast) {
        super(ast);
    }

    public String getImportText() {
        return ASTUtils.identStarText(ast.getFirstChild());
    }

    public List<Comment> getCommentsAfter() {
        return CommentUtils.getCommentAfter(this.getAST());
    }

    public int getSpacerSize() {
        return SpacerUtil.calculateSpacerSize(ast);
    }
}
