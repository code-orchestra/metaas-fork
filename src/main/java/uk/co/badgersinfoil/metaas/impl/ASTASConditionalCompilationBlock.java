package uk.co.badgersinfoil.metaas.impl;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASBlock;
import uk.co.badgersinfoil.metaas.dom.ASConditionalCompilationBlock;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ASTASConditionalCompilationBlock extends ASTScriptElement implements ASConditionalCompilationBlock {

    public ASTASConditionalCompilationBlock(LinkedListTree ast) {
        super(ast);
    }

    public ASBlock getBlock() {
        LinkedListTree labelAST = ASTUtils.findChildByType(ast, AS3Parser.BLOCK);
        if (labelAST != null) {
            return new ASTStatementList(labelAST);
        }
        return null;
    }

    public String getNamespace() {
        List<LinkedListTree> labelAST = ASTUtils.findChildrenByType(ast, AS3Parser.IDENT);
        if (labelAST != null) {
            LinkedListTree labelValueAST = labelAST.get(0);
            if (labelValueAST != null) {
                return labelValueAST.getText();
            }
        }
        return null;
    }

    public String getIdentifier() {
        List<LinkedListTree> labelAST = ASTUtils.findChildrenByType(ast, AS3Parser.IDENT);
        if (labelAST != null) {
            LinkedListTree labelValueAST = labelAST.get(1);
            if (labelValueAST != null) {
                return labelValueAST.getText();
            }
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
