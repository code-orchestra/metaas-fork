/*
 * ASTASObjectLiteral.java
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
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASObjectLiteral;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTASObjectLiteral extends ASTLiteral implements ASObjectLiteral {

	public ASTASObjectLiteral(LinkedListTree ast) {
		super(ast);
	}

	public List getFields() {
		List fields = new ArrayList();
		ASTIterator i = new ASTIterator(ast);
		while (i.hasNext()) {
			fields.add(buildField(i.next()));
		}
		return Collections.unmodifiableList(fields);
	}

	private ASTField buildField(LinkedListTree field) {
		ASTUtils.assertAS3TokenTypeIs(AS3Parser.OBJECT_FIELD,
		                              field.getType());
		return new ASTField(field);
	}

	public Field newField(String name, Expression value) {
		LinkedListTree field = ASTBuilder.newObjectField(name, ast(value));
		String indent = ASTUtils.findIndent(ast) + "\t";
		ASTUtils.increaseIndent(field, indent);
		if (ast.getChildCount() > 0) {
			ast.appendToken(TokenBuilder.newComma());
		}
		ast.appendToken(TokenBuilder.newNewline());
		ast.addChildWithTokens(field);
		return new ASTField(field);
	}

	public static class ASTField implements ASObjectLiteral.Field {

		private LinkedListTree ast;

		public ASTField(LinkedListTree ast) {
			this.ast = ast;
		}

		public String getName() {
            String name = ast.getFirstChild().getText();

            // RE-1198

            /*
            if (name != null) {
                if (name.startsWith("\"") || name.startsWith("'")) {
                    return name.substring(1, name.length() - 1);
                }
            }
            */

            return name;
		}

		public Expression getValue() {
			return ExpressionBuilder.build(ast.getLastChild());
		}
	}
}
