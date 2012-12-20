/*
 * ASTASReturnStatement.java
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

import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASReturnStatement;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.ArrayList;
import java.util.List;


public class ASTASReturnStatement extends ASTScriptElement implements ASReturnStatement {

	public ASTASReturnStatement(LinkedListTree ast) {
		super(ast);
	}

	public String getExpressionString() {
		if (hasExpr()) {
			return null;
		}
		return ASTUtils.stringifyNode(ast.getFirstChild());
	}

	public Expression getExpression() {
		if (hasExpr()) {
			return null;
		}
		return ExpressionBuilder.build(ast.getFirstChild());
	}

    public List<Expression> getExpressions() {
        ArrayList<Expression> expressions = new ArrayList<Expression>();
        for (int i = 0; i < ast.getChildCount(); i++) {
            expressions.add(ExpressionBuilder.build((LinkedListTree) ast.getChild(i)));
        }
        return expressions;
    }

	public void setExpression(String expr) {
		if (expr == null) {
			if (hasExpr()) {
				return;
			}
			ast.deleteChild(0);
			return;
		}
		LinkedListTree expression = AS3FragmentParser.parseExpr(expr);
		setExpr(expression);
	}

	public void setExpression(Expression expr) {
		if (expr == null) {
			if (hasExpr()) {
				return;
			}
			ast.deleteChild(0);
			return;
		}
		setExpr(ast(expr));
	}

	private void setExpr(LinkedListTree expression) {
		if (hasExpr()) {
			LinkedListToken after = ast.getStopToken();
			LinkedListToken before = after.getPrev();
			ast.addChild(expression);
			before.setNext(expression.getStartToken());
			after.setPrev(expression.getStopToken());
			before.afterInsert(TokenBuilder.newSpace());
		} else {
			ast.setChildWithTokens(0, expression);
		}
	}

	private boolean hasExpr() {
		return ast.getChildCount() == 0;
	}

    public List<Comment> getCommentsAfter() {
        return CommentUtils.getCommentAfter(this.getAST());
    }

    public int getSpacerSize() {
        return SpacerUtil.calculateSpacerSize(ast);
    }
}