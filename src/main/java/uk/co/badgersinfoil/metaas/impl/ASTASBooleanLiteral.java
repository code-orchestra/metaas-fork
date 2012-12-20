/*
 * ASTASBooleanLiteral.java
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

import org.antlr.runtime.Token;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASBooleanLiteral;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTASBooleanLiteral extends ASTExpression implements
		ASBooleanLiteral {

	public ASTASBooleanLiteral(LinkedListTree ast) {
		super(ast);
	}

	public boolean getValue() {
		return Boolean.parseBoolean(ast.getToken().getText());
	}

	public void setValue(boolean value) {
		Token t = ast.getToken();
		if (value) {
			t.setText("true");
			t.setType(AS3Parser.TRUE);
		} else {
			t.setText("false");
			t.setType(AS3Parser.FALSE);
		}
	}
}
