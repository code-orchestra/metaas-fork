/*
 * TagUtils.java
 * 
 * Copyright (c) 2006 David Holroyd
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
import java.util.LinkedList;
import java.util.List;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASMetaTag;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;


public class TagUtils {

	public static List getAllMetaTags(LinkedListTree ast) {
		ASTIterator i = iterTags(ast);
		List result = new LinkedList();
		while (i.hasNext()) {
			result.add(toMetaTag(i.next()));
		}
		return Collections.unmodifiableList(result);
	}

	public static ASMetaTag getFirstMetaTag(LinkedListTree ast, String name) {
		ASTIterator i = iterTags(ast);
		while (i.hasNext()) {
			ASTASMetaTag tag = toMetaTag(i.next());
			if (tag.getName().equals(name)) {
				return tag;
			}
		}
		return null;
	}

	public static List getMetaTagWithName(LinkedListTree ast, String name) {
		ASTIterator i = iterTags(ast);
		List result = new LinkedList();
		while (i.hasNext()) {
			ASTASMetaTag tag = toMetaTag(i.next());
			if (tag.getName().equals(name)) {
				result.add(tag);
			}
		}
		return Collections.unmodifiableList(result);
	}

	public static ASMetaTag newMetaTag(LinkedListTree ast, String name) {
		LinkedListTree tag = ASTBuilder.newMetadataTag(name);
		LinkedListToken trailingnl = TokenBuilder.newNewline();
		tag.getStopToken().afterInsert(trailingnl);
		tag.setStopToken(trailingnl);
		String indent = ASTUtils.findIndent(ast);
		if (indent.length() > 0) {
			LinkedListToken stopToken = tag.getStopToken();
			LinkedListToken indentTok = TokenBuilder.newWhiteSpace(indent);
			stopToken.afterInsert(indentTok);
			tag.setStopToken(indentTok);
		}
		findTags(ast).addChildWithTokens(tag);
		return toMetaTag(tag);
	}


	private static ASTIterator iterTags(LinkedListTree ast) {
		return new ASTIterator(findTags(ast));
	}

	private static LinkedListTree findTags(LinkedListTree ast) {
		return ASTUtils.findChildByType(ast, AS3Parser.ANNOTATIONS);
	}

	private static ASTASMetaTag toMetaTag(LinkedListTree tag) {
		ASTUtils.assertAS3TokenTypeIs(AS3Parser.ANNOTATION, tag.getType());
		return new ASTASMetaTag(tag);
	}
}