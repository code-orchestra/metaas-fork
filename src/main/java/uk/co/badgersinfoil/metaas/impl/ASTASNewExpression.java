/*
 * ASTASNewExpression.java
 * 
 * Copyright (c) 2008 David Holroyd
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
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASNewExpression;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;


public class ASTASNewExpression extends ASTInvocation implements
		ASNewExpression {

	public ASTASNewExpression(LinkedListTree ast) {
		super(ast);
	}

	private boolean hasArgs() {
		return ASTUtils.findChildByType(ast, AS3Parser.ARGUMENTS) != null;
	}

	public List getArguments() {
		if (hasArgs()) {
			return super.getArguments();
		}
		return null;
	}

	public void setArguments(List args) {
		if (hasArgs()) {
			if (args == null) {
				ast.deleteChild(1);
			} else {
				super.setArguments(args);
			}
		} else {
			if (args != null) {
				LinkedListTree argTree = ASTUtils.newParentheticAST(AS3Parser.ARGUMENTS, AS3Parser.LPAREN, "(", AS3Parser.RPAREN, ")");
				ast.addChildWithTokens(argTree);
				super.setArguments(args);
			}
		}
	}
}