/*
 * ASTASMember.java
 * 
 * Copyright (c) 2006-2008 David Holroyd
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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;


public abstract class ASTASMember extends ASTScriptElement implements ASMember {

	public ASTASMember(LinkedListTree ast) {
		super(ast);
	}

    public String getNamespace() {
        return ModifierUtils.getNamespace(findModifiers());
    }

    public Visibility getVisibility() {
		return ModifierUtils.getVisibility(findModifiers());
	}

	public void setVisibility(Visibility protection) {
		ModifierUtils.setVisibility(findModifiers(), protection);
	}

	public boolean isStatic() {
		LinkedListTree modifiers = findModifiers();
		return ASTUtils.findChildByType(modifiers, AS3Parser.STATIC) != null;
	}

	protected LinkedListTree findModifiers() {
		return ASTUtils.findChildByType(ast, AS3Parser.MODIFIERS);
	}

	private ASTIterator modifierIter() {
		return new ASTIterator(findModifiers());
	}

	public void setStatic(boolean s) {
		for (ASTIterator i=modifierIter(); i.hasNext(); ) {
			LinkedListTree mod = i.next();
			if (mod.getType() == AS3Parser.STATIC) {
				if (!s) {
					i.remove();
				}
				return;
			}
		}
		if (s) {
			LinkedListTree modifiers = findModifiers();
			LinkedListTree modStatic = ASTUtils.newAST(AS3Parser.STATIC, "static");
			modStatic.appendToken(TokenBuilder.newSpace());
			modifiers.addChildWithTokens(modStatic);
		}
	}

	public List getAllMetaTags() {
		return TagUtils.getAllMetaTags(ast);
	}

	public ASMetaTag getFirstMetatag(String name) {
		return TagUtils.getFirstMetaTag(ast, name);
	}

	public List getMetaTagsWithName(String name) {
		return TagUtils.getMetaTagWithName(ast, name);
	}

	public ASMetaTag newMetaTag(String name) {
		return TagUtils.newMetaTag(ast, name);
	}

	public void setDocComment(String text) {
		DocCommentUtils.setDocComment(ast, text);
	}

	public String getDocComment() {
		return DocCommentUtils.getDocComment(ast);
	}

	public String getDescriptionString() {
		return getDocumentation().getDescriptionString();
	}

	public void setDescription(String description) {
		getDocumentation().setDescriptionString(description);
	}

	public DocComment getDocumentation() {
		return DocCommentUtils.createDocComment(ast);
	}
}