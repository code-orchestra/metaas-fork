/*
 * ASTASArrayLiteral.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASArrayLiteral;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTASArrayLiteral extends ASTLiteral implements ASArrayLiteral {

	public ASTASArrayLiteral(LinkedListTree ast) {
		super(ast);
	}

	public List getEntries() {
        return ASTUtils.getArrayEntries(ast);
	}

	public void add(Expression entry) {
		if (ast.getChildCount() > 0) {
			ast.appendToken(TokenBuilder.newComma());
			ast.appendToken(TokenBuilder.newSpace());
		}
		ast.addChildWithTokens(ast(entry));
	}

	public Expression remove(int i) {
		LinkedListTree old = (LinkedListTree)ast.getChild(i);
		if (ast.getChildCount()-1 > i) {
			ASTUtils.removeTrailingWhitespaceAndComma(old.getStopToken());
		} else if (i > 0) {
			ASTUtils.removePreceedingWhitespaceAndComma(old.getStartToken());
		}
		ast.deleteChild(i);
		return ExpressionBuilder.build(old);
	}
}
