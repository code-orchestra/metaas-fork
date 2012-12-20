package uk.co.badgersinfoil.metaas.impl;

import uk.co.badgersinfoil.metaas.dom.ASOctalLiteral;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

/**
 * @author Anton.I.Neverov
 */
public class ASTASOctalLiteral extends ASTLiteral implements ASOctalLiteral {

	public ASTASOctalLiteral(LinkedListTree ast) {
		super(ast);
	}

	public String getValue() {
		return getTokenText();
	}

	public void setValue(String value) {
		setTokenText(value);
	}
}
