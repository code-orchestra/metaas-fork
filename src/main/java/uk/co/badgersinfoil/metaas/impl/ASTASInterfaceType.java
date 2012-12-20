/*
 * ASTASInterfaceType.java
 * 
 * Copyright (c) 2006-2007 David Holroyd
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
import uk.co.badgersinfoil.metaas.dom.ASInterfaceType;
import uk.co.badgersinfoil.metaas.dom.ASMethod;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;


public class ASTASInterfaceType extends ASTASType implements ASInterfaceType {

	private static final int EXTENDS_INDEX = 3;

	public ASTASInterfaceType(LinkedListTree ast) {
		super(ast);
	}

	public List getSuperInterfaces() {
		List results = new LinkedList();
		LinkedListTree impls = ASTUtils.findChildByType(ast, AS3Parser.EXTENDS);
		if (impls != null) {
			for (ASTIterator i=new ASTIterator(impls); i.hasNext(); ) {
				results.add(ASTUtils.identText(i.next()));
			}
		}
		return Collections.unmodifiableList(results);
	}

	public void addSuperInterface(String interfaceName) {
		LinkedListTree iface = AS3FragmentParser.parseIdent(interfaceName);
		LinkedListTree ext = ASTUtils.findChildByType(ast, AS3Parser.EXTENDS);
		if (ext == null) {
			ext = ASTUtils.newAST(AS3Parser.EXTENDS, "extends");
			ext.appendToken(TokenBuilder.newSpace());
			// hack a space in at the right point,
			LinkedListToken sp = TokenBuilder.newSpace();
			ext.getStartToken().beforeInsert(sp);
			ext.setStartToken(sp);
			ast.addChildWithTokens(EXTENDS_INDEX, ext);
		} else {
			ext.appendToken(TokenBuilder.newComma());
		}
		ext.appendToken(TokenBuilder.newSpace());
		ext.addChildWithTokens(iface);
	}

	public void removeSuperInterface(String interfaceName) {
		LinkedListTree impls = ASTUtils.findChildByType(ast, AS3Parser.EXTENDS);
		int count = 0;
		for (ASTIterator i=new ASTIterator(impls); i.hasNext(); ) {
			LinkedListTree iface = i.next();
			String name = ASTUtils.identText(iface);
			if (name.equals(interfaceName)) {
				if (i.hasNext()) {
					ASTUtils.removeTrailingWhitespaceAndComma(iface.getStopToken());
				} else if (count == 0) {
					// no interfaces left, so remove the
					// 'implements' clause completely,
					ast.deleteChild(ast.getIndexOfChild(impls));
					break;
				}
				i.remove();
				if (i.hasNext()) {
					count++;
				}
				break;
			}
			count++;
		}
	}

	public ASMethod newMethod(String name, Visibility visibility, String returnType) {
		ASTASMethod meth = ASTBuilder.newInterfaceMethod(name, visibility, returnType);
		addMethod(meth);
		return meth;
	}

	public void addMethod(ASTASMethod meth) {
		ASTUtils.addChildWithIndentation(findTypeBlock(), meth.getAST());
	}
}