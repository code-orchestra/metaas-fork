package uk.co.badgersinfoil.metaas.impl;

import org.antlr.runtime.tree.Tree;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASVectorLiteral;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.TypeDescriptor;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class ASTASVectorLiteral extends ASTExpression implements ASVectorLiteral {

    public ASTASVectorLiteral(LinkedListTree ast) {
        super(ast);
    }

    public List<Expression> getEntries() {
        Tree arrayLiteralAST = ast.getFirstChildWithType(AS3Parser.ARRAY_LITERAL);
        if (arrayLiteralAST != null && arrayLiteralAST instanceof LinkedListTree) {
            return ASTUtils.getArrayEntries((LinkedListTree) arrayLiteralAST);
        } else {
            return new ArrayList<Expression>();
        }
    }

    public TypeDescriptor getTypeDescriptor() {
        LinkedListTree typeAST = ASTUtils.findChildByType(ast, AS3Parser.TYPE_SPEC);
        if (typeAST == null) {
            typeAST = ASTUtils.findChildByType(ast, AS3Parser.VECTOR);
        }
        return new TypeDescriptorImpl(typeAST);
    }

}
