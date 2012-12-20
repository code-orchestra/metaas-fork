package uk.co.badgersinfoil.metaas.impl;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASField;
import uk.co.badgersinfoil.metaas.dom.ASMethod;
import uk.co.badgersinfoil.metaas.dom.MXMLASBlock;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class MXMLASBlockImpl extends ASTScriptElement implements MXMLASBlock {

    public MXMLASBlockImpl(LinkedListTree ast) {
        super(ast);
    }

    public List<String> getImports() {
        ASTIterator i = new ASTIterator(ast);
        LinkedListTree imp;
        List<String> result = new ArrayList<String>();
        while ((imp = i.search(AS3Parser.IMPORT)) != null) {
            result.add(ASTUtils.identStarText(imp.getFirstChild()));
        }
        return result;
    }

    public List<ASField> getFields() {
		List<ASField> results = new LinkedList<ASField>();
        ASTIterator blockIter = new ASTIterator(ast);
		for (; blockIter.hasNext(); ) {
			LinkedListTree member = blockIter.next();
			if (member.getType() == AS3Parser.VAR_DEF) {
				results.add(new ASTASField(member));
			}
		}
		return Collections.unmodifiableList(results);
    }

    public List<ASMethod> getMethods() {
		List<ASMethod> results = new LinkedList<ASMethod>();
        ASTIterator blockIter = new ASTIterator(ast);
		for (; blockIter.hasNext(); ) {
			LinkedListTree member = blockIter.next();
			if (member.getType() == AS3Parser.METHOD_DEF) {
				results.add(new ASTASMethod(member));
			}
		}
		return Collections.unmodifiableList(results);
    }
}
