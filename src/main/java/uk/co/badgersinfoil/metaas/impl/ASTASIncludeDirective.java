package uk.co.badgersinfoil.metaas.impl;

import org.antlr.runtime.tree.Tree;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASIncludeDirective;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

/**
 * @author Alexander Eliseyev
 */
public class ASTASIncludeDirective extends ASTScriptElement implements ASIncludeDirective {

    public ASTASIncludeDirective(LinkedListTree ast) {
        super(ast);
    }

    public String getPath() {
        Tree type = ast.getFirstChildWithType(AS3Parser.STRING_LITERAL);
        if (type == null) {
            return null;
        }

        return new ASTASStringLiteral((LinkedListTree) type).getValue();
    }
}
