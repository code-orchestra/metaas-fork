/*
 * ASTASExpressionStatement.java
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

import org.antlr.runtime.tree.Tree;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASExpressionStatement;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.ArrayList;
import java.util.List;

public class ASTASExpressionStatement extends ASTScriptElement implements ASExpressionStatement {

	public ASTASExpressionStatement(LinkedListTree ast) {
		super(ast);
	}

    public List<Expression> getSubExpressions() {
        List<Expression> expressions = new ArrayList<Expression>();
        LinkedListTree ast = getAST();
        for (int i = 0; i < ast.getChildCount(); i++) {
            Tree child = ast.getChild(i);
            if (child instanceof LinkedListTree) {
                expressions.add(ExpressionBuilder.build((LinkedListTree) child));
            }
        }
        return expressions;
    }

    public Expression getExpression() {
		return ExpressionBuilder.build(ast.getFirstChild());
	}
	
	public String getExpressionString() {
		return ASTUtils.stringifyNode(ast.getFirstChild());
	}

	public void setExpression(String expr) {
		ast.setChildWithTokens(0, AS3FragmentParser.parseExprList(expr));
	}

    public List<Comment> getCommentsAfter() {
        return CommentUtils.getCommentAfter(this.getAST());
    }

    public int getSpacerSize() {
        return SpacerUtil.calculateSpacerSize(ast);
    }
}
