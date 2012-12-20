package uk.co.badgersinfoil.metaas.dom;

import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public interface ASVectorLiteral extends Expression {

    TypeDescriptor getTypeDescriptor();

    List<Expression> getEntries();

}
