package uk.co.badgersinfoil.metaas.impl;

import uk.co.badgersinfoil.metaas.dom.ASSuperExpression;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

/**
 * @author Alexander Eliseyev
 */
public class ASTASSuperExpression extends ASTExpression implements ASSuperExpression {

    public ASTASSuperExpression(LinkedListTree ast) {
		super(ast);
	}

}
