/*
 * Copyright (c) 2006 David Holroyd
 */

package uk.co.badgersinfoil.metaas.impl.antlr;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.IdentityHashMap;
import java.util.Map;
import org.antlr.runtime.Token;
import org.asdt.core.internal.antlr.AS3Parser;


/**
 * A little hack to create a 'dot' file representing the structure of the
 * given LinkedListTree, suitable to visualisation using the graphviz.org
 * 'dot' tool.
 */
public class ASTDot {
	private PrintWriter out;
	private Map doneTokens = new IdentityHashMap();

	public ASTDot(Writer writer) {
		out = new PrintWriter(writer);
	}

	public ASTDot(OutputStream stream) {
		out = new PrintWriter(stream);
	}

	public static void dump(LinkedListTree ast) {
		new ASTDot(System.err).dotify(ast);
	}

	public void dotify(LinkedListTree ast) {
		println("digraph ast_diagram {");
		println("  node [fontsize=12];");
		nodeSubGraph(ast);
		tokenSubGraph(ast);
		startStopTokenEdges(ast);
		println("}");
		out.flush();
	}

	private void nodeSubGraph(LinkedListTree ast) {
		println("  subgraph tree {");
		treeNodes(ast);
		println("  }");
	}

	private void treeNodes(LinkedListTree ast) {
		println("    "+nodeName(ast)+" [label="+label(ast)+",shape=invhouse];");
		for (int i=0; i<ast.getChildCount(); i++) {
			LinkedListTree child = (LinkedListTree)ast.getChild(i);
			println("    "+nodeName(ast)+" -> "+nodeName(child)+" [weight=10,style=bold];");
			treeNodes(child);
		}
	}

	private String label(LinkedListTree ast) {
		return label(AS3Parser.tokenNames[ast.getType()]);
	}

	private String nodeName(LinkedListTree ast) {
		return "ast_0x"+Integer.toHexString(System.identityHashCode(ast));
	}


	private void tokenSubGraph(LinkedListTree ast) {
		println("  subgraph cluster_tokens {");
		println("    rankdir=LR;");
		tokenNodes(ast);
		println("  }");
	}

	private void tokenNodes(LinkedListTree ast) {
		for (LinkedListToken tok = ast.getStartToken(); tok!=null; tok=tok.getNext()) {
			tokenNode(tok);
		}
		for (int i=0; i<ast.getChildCount(); i++) {
			LinkedListTree child = (LinkedListTree)ast.getChild(i);
			tokenNodes(child);
		}
	}

	private int runSeq = 0;

	private void tokenNode(LinkedListToken tok) {
		if (doneTokens.containsKey(tok)) {
			return;
		}
		Integer runId = new Integer(runSeq++);
		out.print("    tok_"+runId+" [label=\"");
		for (; tok!=null; tok=tok.getNext()) {
			out.print("<"+fieldName(tok)+"> "+label(tok));
			doneTokens.put(tok, runId);
			if (tok.getNext() != null) {
				out.print("|");
			}
		}
		out.println("\",shape=record];");
	}

	private String label(LinkedListToken tok) {
		if (tok.getType() == Token.EOF) {
			return "EOF";
		}
		if (tok.getType() == AS3Parser.VIRTUAL_PLACEHOLDER) {
			return "PLACEHOLD";
		}
		String txt = tok.getText();
		if (txt.length() > 10) {
			txt = txt.substring(0, 7) + "...";
		}
		return "\\\""+txt.replaceAll("\\|", "\\\\|")
		                    .replaceAll("\"", "\\\\\"")
		                    .replaceAll("\\{", "\\\\{")
		                    .replaceAll("\\}", "\\\\}")
		                    .replaceAll("<", "\\\\<")
		                    .replaceAll(">", "\\\\>")
		                    .replaceAll("\n", "\\\\\\\\n")
		                    .replaceAll("\t", "\\\\\\\\t")+"\\\"";
	}

	private String nodeName(LinkedListToken tok) {
		return "tok_"+doneTokens.get(tok)+":"+fieldName(tok);
	}

	private String fieldName(LinkedListToken tok) {
		return "f"+Integer.toHexString(System.identityHashCode(tok));
	}

	private void startStopTokenEdges(LinkedListTree ast) {
		if (ast.getStartToken() != null) {
			println("  "+nodeName(ast)+" -> "+nodeName(ast.getStartToken())+" [minlen=2,color=green,tailport=w];");
		}
		if (ast.getStopToken() != null) {
			println("  "+nodeName(ast)+" -> "+nodeName(ast.getStopToken())+" [minlen=2,color=red,tailport=e];");
		}
		for (int i=0; i<ast.getChildCount(); i++) {
			LinkedListTree child = (LinkedListTree)ast.getChild(i);
			startStopTokenEdges(child);
		}
	}

	private String label(String str) {
		if (str.length() > 15) {
			str = str.substring(0, 15);
		}
		StringBuffer res = new StringBuffer("\"");
		for (int i=0; i<str.length(); i++) {
			char c = str.charAt(i);
			switch (c) {
			    case '\n':
				res.append("\\\\n");
				break;
			    case '\r':
				res.append("\\\\r");
				break;
			    case '\t':
				res.append("\\\\t");
				break;
			    case '"':
				res.append("\\\\\\\"");
				break;
			    default:
				res.append(c);
			}
		}
		res.append("\"");
		return res.toString();
	}

	private void println(String txt) {
		out.println(txt);
	}
}