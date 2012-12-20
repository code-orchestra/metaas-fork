package uk.co.badgersinfoil.metaas;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.impl.ASTASCompilationUnit;
import uk.co.badgersinfoil.metaas.impl.ASTIterator;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
import uk.co.badgersinfoil.metaas.impl.antlr.ASTDot;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import junit.framework.Assert;


public class CodeMirror {

	/**
	 * Serialises the given compilation unit to a string, parses it back
	 * to a second compilation unit, and serialises that to a second string,
	 * finally asserting that the first string and the second string of
	 * source code are the same.
	 */
	public static ASCompilationUnit assertReflection(ActionScriptFactory fact, ASCompilationUnit unit) {
		LinkedListTree ast = ((ASTASCompilationUnit)unit).getAST();
		saintyCheckTokenStream(ast);
		saintyCheckStartStopTokens(ast);
		assertTokenStreamNotDisjoint(ast);
		StringWriter out = new StringWriter();
		ActionScriptWriter writer = fact.newWriter(); 
		try {
			writer.write(out, unit);
		} catch (IOException e) {
			throw new Error(e);
		}
		try {
			ASCompilationUnit unit2 = fact.newParser().parse(new StringReader(out.toString()));
			LinkedListTree ast2 = ((ASTASCompilationUnit)unit2).getAST();
			assertASTMatch(ast, ast2);
			StringWriter out2 = new StringWriter();
			try {
				writer.write(out2, unit2);
			} catch (IOException e) {
				throw new Error(e);
			}
			Assert.assertEquals(out.toString(), out2.toString());
			return unit2;
		} catch (SyntaxException e) {
			Assert.fail(e.toString()+"\n"+out.toString());
			return null; // never reached
		}
	}

	public static Set assertTokenStreamNotDisjoint(LinkedListTree ast) {
		Set tokensFromStartToStop = tokenStreamToSet(ast);
		for (int i=0; i<ast.getChildCount(); i++) {
			LinkedListTree child = (LinkedListTree)ast.getChild(i);
			Set childTokens = assertTokenStreamNotDisjoint(child);
			Assert.assertTrue("'"+child+"' (child "+i+" of '"+ast+"') had a token stream disjoint with its parent",
			                  tokensFromStartToStop.containsAll(childTokens));
		}
		return tokensFromStartToStop;
	}

	private static Set tokenStreamToSet(LinkedListTree ast) {
		Set tokens = new HashSet();
		for (LinkedListToken tok = ast.getStartToken(); tok!=null;) {
			tokens.add(tok);
			if (tok == ast.getStopToken()) {
				break;
			}
			tok = tok.getNext();
		}
		return tokens;
	}

	public static void assertASTMatch(LinkedListTree ast1, LinkedListTree ast2) {
		String path = pathTo(ast1);
		ASTUtils.assertAS3TokenTypeIs("At "+path, ast1.getType(), ast2.getType());
		if (ast1.getType() == AS3Parser.IDENT) {
			Assert.assertEquals("At "+path, ast1.getText(), ast2.getText());
		}
		Assert.assertEquals("At "+path+" child count mismatch: "+stringifyFirstLevel(ast1)+" vs. "+stringifyFirstLevel(ast2),
		                    ast1.getChildCount(), ast2.getChildCount());
		for (int i=0; i<ast1.getChildCount(); i++) {
			assertASTMatch((LinkedListTree)ast1.getChild(i),
			               (LinkedListTree)ast2.getChild(i));
		}
	}

	private static String pathTo(LinkedListTree ast) {
		StringBuffer buff = new StringBuffer();
		while (ast != null) {
			buff.insert(0, ast);
			ast = ast.getParent();
			if (ast != null) {
				buff.insert(0, "/");
			}
		}
		return buff.toString();
	}

	private static String stringifyFirstLevel(LinkedListTree ast) {
		StringBuffer buf = new StringBuffer("(");
		for (int i=0; i<ast.getChildCount(); i++) {
			if (i > 0) {
				buf.append(" ");
			}
			buf.append(ast.getChild(i));
		}
		buf.append(")");
		return buf.toString();
	}

	/**
	 * Search through the stream for tokens who's 'prev' property doesn't
	 * match the token who's 'last' property we just dereferenced in
	 * the last iteration.
	 * i.e. checks that when prev.next==next, then next.prev==prev too.
	 */
	private static void saintyCheckTokenStream(LinkedListTree ast) {
		LinkedListToken last = null;
		for (LinkedListToken tok=ast.getStartToken(); tok!=null; tok=tok.getNext()) {
			if (last != null && last != tok.getPrev()) {
				Assert.fail("last["+last+"].next=>["+tok+"] but next.prev=>["+tok.getPrev()+"]");
			}
			last = tok;
		}
	}
	private static void saintyCheckStartStopTokens(LinkedListTree ast) {
		assertStopNotBeforeStart(ast);
		ASTIterator i = new ASTIterator(ast);
		while (i.hasNext()) {
			saintyCheckStartStopTokens(i.next());
		}
	}

	private static void assertStopNotBeforeStart(LinkedListTree ast) {
		LinkedListToken start = ast.getStartToken();
		LinkedListToken stop = ast.getStopToken();
		if (stop == start) return;
		for (LinkedListToken tok=stop; tok!=null; tok=tok.getNext()) {
			Assert.assertFalse("Found stopToken preceeding startToken: "+ast+"("+start+" - "+stop+")",
			                   tok==start);
		}
	}
}