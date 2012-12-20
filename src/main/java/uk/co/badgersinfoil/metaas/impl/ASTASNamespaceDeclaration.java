package uk.co.badgersinfoil.metaas.impl;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASNamespaceDeclaration;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class ASTASNamespaceDeclaration extends ASTScriptElement implements ASNamespaceDeclaration {

    public ASTASNamespaceDeclaration(LinkedListTree ast) {
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

    public Visibility getVisibility() {
        return ModifierUtils.getVisibility(findModifiers());
    }

    protected LinkedListTree findModifiers() {
		return ASTUtils.findChildByType(ast, AS3Parser.MODIFIERS);
	}

    public List<Comment> getCommentsBefore() {
        LinkedListTree tree = this.findModifiers();
        if (tree != null) {
            return CommentUtils.getCommentBefore(tree);
        }
        return CommentUtils.getCommentBefore(this.getAST());
    }
}
