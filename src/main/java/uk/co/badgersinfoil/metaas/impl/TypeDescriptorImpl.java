package uk.co.badgersinfoil.metaas.impl;

import uk.co.badgersinfoil.metaas.dom.TypeDescriptor;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

/**
 * Type descriptor implementation.
 *
 * @author Alexander Eliseyev
 */
public class TypeDescriptorImpl implements TypeDescriptor {

    private LinkedListTree ast;

    public TypeDescriptorImpl(LinkedListTree ast) {
      this.ast = ast;
    }

    public String getName() {
        if (ast == null) {
            return null;
        }
        return ASTUtils.typeSpecText(ast);
    }

    public TypeDescriptor getParameter() {
        if (ast == null) {
            return null;
        }
        return ASTUtils.typeSpecParameter(ast);
    }

    @Override
    public String toString() {
        TypeDescriptor typeParameter = getParameter();
        if (typeParameter == null) {
            return getName();
        }
        return getName() + ".<" + typeParameter + ">";
    }
}
