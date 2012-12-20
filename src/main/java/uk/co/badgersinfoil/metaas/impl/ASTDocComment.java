/*
 * ASTASDocComment.java
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

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import uk.co.badgersinfoil.metaas.dom.DocComment;
import uk.co.badgersinfoil.metaas.dom.DocTag;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.impl.parser.javadoc.JavadocParser;


public class ASTDocComment extends ASTScriptElement implements DocComment {

	private LinkedListTree javadoc;

	public ASTDocComment(LinkedListTree ast) {
		super(ast);
		javadoc = DocCommentUtils.buildJavadoc(ast);
	}

	public String getDescriptionString() {
		return DocCommentUtils.getDescriptionString(ast);
	}

	public void setDescriptionString(String description) {
		DocCommentUtils.setDescriptionString(ast, description);
	}

	public Iterator findTags(String name) {
		if (javadoc == null) {
			return Collections.EMPTY_LIST.iterator();
		}
		String tagname = tagName(name);
		LinkedList tags = new LinkedList();
		ASTIterator i = new ASTIterator(javadoc);
		LinkedListTree para;
		while ((para=i.search(JavadocParser.PARA_TAG)) != null) {
			LinkedListTree tag = para.getFirstChild();
			if (tag.getText().equals(tagname)) {
				tags.add(new ASTDocTag(this, para));
			}
		}
		return tags.iterator();
	}

	private static String tagName(String name) {
		return "@" + name;
	}

	public DocTag findFirstTag(String name) {
		if (javadoc == null) {
			return null;
		}
		String tagname = tagName(name);
		ASTIterator i = new ASTIterator(javadoc);
		LinkedListTree para;
		while ((para=i.search(JavadocParser.PARA_TAG)) != null) {
			LinkedListTree tag = para.getFirstChild();
			if (tag.getText().equals(tagname)) {
				return new ASTDocTag(this, para);
			}
		}
		return null;
	}
	
	public void delete(DocTag tag) {
		LinkedListTree tagAST = ((ASTDocTag)tag).getAST();
		int index = javadoc.getIndexOfChild(tagAST);
		javadoc.deleteChild(index);
		commitModifiedAST();
	}

	public void addParaTag(String name, String body) {
		DocCommentUtils.assertValidContent(body);
		String newline = DocCommentUtils.getNewlineText(ast, javadoc);
		body = body.replaceAll("\n", newline);
		String tagname = tagName(name);
		if (javadoc == null) {
			DocCommentUtils.setDocComment(ast, "\n "+tagname+" "+body+"\n");
			javadoc = DocCommentUtils.buildJavadoc(ast);
		} else {
			LinkedListTree lastChild = javadoc.getLastChild();
			LinkedListTree para = DocCommentUtils.parseParaTag(tagname+" "+body);
			javadoc.addChildWithTokens(para);
			// the final NL token doesn't usually have a '*' in it
			// (as the end-of-comment provides on) so lets make
			// sure we find that and re-insert it at the new end
			// of the comment
			LinkedListTree trainingNL = null;
			int lastChildLast = lastChild.getChildCount()-1;
			lastChild.addChildWithTokens(ASTUtils.newAST(JavadocParser.NL, newline));
			if (lastChild.getChild(lastChildLast).getType() == JavadocParser.NL) {
				trainingNL = (LinkedListTree)lastChild.deleteChild(lastChildLast);
			}
			if (trainingNL != null) {
				para.addChildWithTokens(trainingNL);
			}
			commitModifiedAST();
		}
	}
	
	/**
	 * Stores modifications to the javadoc comment's AST back into the
	 * comment token in the containing ActionScript AST.
	 */
	public void commitModifiedAST() {
		LinkedListToken doc = DocCommentUtils.findDocCommentToken(ast);
		doc.setText("/**"+ASTUtils.stringifyNode(javadoc)+"*/");
	}
}