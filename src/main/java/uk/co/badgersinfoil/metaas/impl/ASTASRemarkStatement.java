package uk.co.badgersinfoil.metaas.impl;

import uk.co.badgersinfoil.metaas.dom.ASRemarkStatement;

import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ASTASRemarkStatement implements ASRemarkStatement {

    Comment content;

    public ASTASRemarkStatement(Comment comment) {
        this.content = comment;
    }

    public Comment getContent() {
        return content;
    }

    public List<Comment> getCommentsAfter() {
        return null;
    }

    public int getSpacerSize() {
        return 0;
    }
}
