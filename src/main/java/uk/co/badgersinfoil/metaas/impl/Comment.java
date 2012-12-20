package uk.co.badgersinfoil.metaas.impl;

/**
 * @author Anton.I.Neverov
 */
public class Comment {

    private CommentType type;
    private String text;

    public Comment(String text) {
        this.text = text;
        this.type = CommentType.SINGLE;
    }

    public Comment(String text, CommentType type) {
        this.text = text;
        this.type = type;
    }

    public String getComment() {
        return this.text;
    }

    public CommentType getType() {
        return this.type;
    }
}
