/*
 * ASTASWithStatement.java
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
import uk.co.badgersinfoil.metaas.SyntaxException;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASWithStatement;
import uk.co.badgersinfoil.metaas.dom.Statement;
import uk.co.badgersinfoil.metaas.dom.StatementContainer;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.List;

public class ASTASWithStatement extends ContainerDelegate implements ASWithStatement {

	private static final int INDEX_CONDITION = 0;

	public ASTASWithStatement(LinkedListTree ast) {
		super(ast);
	}

	protected StatementContainer getStatementContainer() {
		LinkedListTree body = body();
		if (body.getType() != AS3Parser.BLOCK) {
			throw new SyntaxException("'with' body is not a block");
		}
		return new ASTStatementList(body);
	}

	private LinkedListTree body() {
		return ast.getLastChild();
	}

	public String getScopeString() {
		return ASTUtils.stringifyNode(scope().getFirstChild());
	}

	public Expression getScope() {
		return ExpressionBuilder.build(scope().getFirstChild());
	}

	private LinkedListTree scope() {
		return ast.getFirstChild();
	}

	public void setScope(String expr) {
		LinkedListTree cond = AS3FragmentParser.parseCondition(expr);
		ast.setChildWithTokens(INDEX_CONDITION, cond);
	}

	public void setScope(Expression expr) {
		scope().setChildWithTokens(0, ast(expr));
	}

	public Statement getBody() {
		return StatementBuilder.build(body());
	}

    public List<Comment> getCommentsAfter() {
        return CommentUtils.getCommentAfter(this.getAST());
    }

    public int getSpacerSize() {
        return SpacerUtil.calculateSpacerSize(ast);
    }
}