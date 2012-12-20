/*
 * ASTDocTag.java
 *
 * Copyright (c) 2007 David Holroyd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.badgersinfoil.metaas.impl;

import uk.co.badgersinfoil.metaas.dom.DocTag;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.impl.parser.javadoc.JavadocParser;


public class ASTDocTag implements DocTag {

	private ASTDocComment comment;
	private LinkedListTree ast;

	public ASTDocTag(ASTDocComment comment, LinkedListTree ast) {
		this.comment = comment;
		this.ast = ast;
	}
	
	public LinkedListTree getAST() {
		return ast;
	}

	public String getBodyString() {
		StringBuffer result = new StringBuffer();
		for (LinkedListToken tok=ast.getStartToken().getNext(); tok!=null&&tok.getType()!=-1; tok=tok.getNext()) {
			result.append(stringify(tok));
			if (tok == ast.getStopToken()) {
				break;
			}
		}
		return result.toString();
	}

	private static String stringify(LinkedListToken tok) {
		switch (tok.getType()) {
			case JavadocParser.NL:
				// TODO: use the original line-ending format
				return "\n";
			default:
				return tok.getText();
		}
	}

	public void setBody(String text) {
		int lastIndex = ast.getChildCount()-1;
		String indentNL = DocCommentUtils.findNewline(ast);
		LinkedListTree trailingNL = null;
		if (lastIndex>=0 && ast.getChild(lastIndex).getType() == JavadocParser.NL) {
			trailingNL = (LinkedListTree)ast.getChild(lastIndex);
		}
		LinkedListTree tag = DocCommentUtils.parseParaTag(ast.getFirstChild().getText()+" "+text);
		replaceNLs(tag, indentNL);
		if (trailingNL != null) {
			tag.addChildWithTokens(trailingNL);
		}
		LinkedListTree parent = ast.getParent();
		int pos = parent.getIndexOfChild(ast);
		parent.setChildWithTokens(pos, tag);

		comment.commitModifiedAST();
	}

	private void replaceNLs(LinkedListTree tree, String indentNL) {
		for (LinkedListToken tok=tree.getStartToken().getNext(); tok!=null&&tok.getType()!=-1; tok=tok.getNext()) {
			if (tok.getType() == JavadocParser.NL) {
				tok.setText(indentNL);
			}
			if (tok == tree.getStopToken()) {
				break;
			}
		}
	}

	public String getName() {
		return ast.getStartToken().getText().substring(1);
	}

	public void setName(String name) {
		ast.getStartToken().setText("@" + name);
		comment.commitModifiedAST();
	}
}