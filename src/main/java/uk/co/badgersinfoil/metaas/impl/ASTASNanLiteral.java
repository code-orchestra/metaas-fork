package uk.co.badgersinfoil.metaas.impl;

import uk.co.badgersinfoil.metaas.dom.ASNanLiteral;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTASNanLiteral extends ASTLiteral implements ASNanLiteral {

	public ASTASNanLiteral(LinkedListTree ast) {
		super(ast);
	}
}