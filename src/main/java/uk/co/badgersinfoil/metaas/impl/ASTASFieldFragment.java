package uk.co.badgersinfoil.metaas.impl;

import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class ASTASFieldFragment extends ASTASVarDeclarationFragment implements ASField {

    private ASField field;

    private List<Comment> commentBefore;
    private List<Comment> commentAfter;

    public ASTASFieldFragment(LinkedListTree ast, ASField parentField) {
        super(ast);
        this.field = parentField;
    }

    public List<ASField> getSubFields() {
        throw new UnsupportedOperationException();
    }

    public void setInitializer(String expr) {
        throw new UnsupportedOperationException();
    }

    public void setInitializer(Expression expr) {
        throw new UnsupportedOperationException();
    }

    public void setConst(boolean isConst) {
        throw new UnsupportedOperationException();
    }

    public boolean isConst() {
        return field.isConst();
    }

    public List<Comment> getCommentsBefore() {
        return commentBefore;
    }

    public void setCommentBefore(List<Comment> commentBefore) {
        this.commentBefore = commentBefore;
    }

    public List<Comment> getCommentsAfter() {
        return commentAfter;
    }

    public void setCommentAfter(List<Comment> commentAfter) {
        this.commentAfter = commentAfter;
    }

    public String getNamespace() {
        return field.getNamespace();
    }

    public void setName(String string) {
        throw new UnsupportedOperationException();
    }

    public TypeDescriptor getType() {
        return getTypeDescriptor();
    }

    public void setType(String string) {
        throw new UnsupportedOperationException();
    }

    public Visibility getVisibility() {
        return field.getVisibility();
    }

    public void setVisibility(Visibility visibility) {
        throw new UnsupportedOperationException();
    }

    public boolean isStatic() {
        return field.isStatic();
    }

    public void setStatic(boolean s) {
        throw new UnsupportedOperationException();
    }

    public String getDocComment() {
        return field.getDocComment();
    }

    public void setDocComment(String text) {
        throw new UnsupportedOperationException();
    }

    public String getDescriptionString() {
        return field.getDescriptionString();
    }

    public void setDescription(String description) {
        throw new UnsupportedOperationException();
    }

    public DocComment getDocumentation() {
        return field.getDocumentation();
    }

    public ASMetaTag getFirstMetatag(String name) {
        return field.getFirstMetatag(name);
    }

    public List getAllMetaTags() {
        return field.getAllMetaTags();
    }

    public List getMetaTagsWithName(String name) {
        return field.getMetaTagsWithName(name);
    }

    public ASMetaTag newMetaTag(String name) {
        throw new UnsupportedOperationException();
    }
}
