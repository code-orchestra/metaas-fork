package uk.co.badgersinfoil.metaas.impl;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASVectorExpression;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.TypeDescriptor;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

/**
 * @author Alexander Eliseyev
 */
public class ASTASVectorExpression extends ASTExpression implements ASVectorExpression {

     public ASTASVectorExpression(LinkedListTree ast) {
		super(ast);
	}

    public TypeDescriptor getTypeDescriptor() {
        return new TypeDescriptorImpl(ast);
    }
}
