/*
 * ASTASCatchClause.java
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

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASCatchClause;
import uk.co.badgersinfoil.metaas.dom.StatementContainer;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTASCatchClause extends ContainerDelegate implements ASCatchClause {

	public ASTASCatchClause(LinkedListTree ast) {
		super(ast);
	}

	public String getParamName() {
		return ast.getFirstChild().getText();
	}

	public String getTypeName() {
		LinkedListTree type = ASTUtils.findChildByType(ast, AS3Parser.TYPE_SPEC);
		if (type == null) {
			return null;
		}
        final LinkedListTree typeAST = type.getFirstChild();
        if (typeAST.getType() == AS3Parser.STAR) {
            return "*";
        }
        return ASTUtils.identText(typeAST);
	}

	private LinkedListTree block() {
		return ASTUtils.findChildByType(ast, AS3Parser.BLOCK);
	}

	protected StatementContainer getStatementContainer() {
		return new ASTStatementList(block());
	}
}
