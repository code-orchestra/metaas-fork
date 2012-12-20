/*
 * ASTASFilterExpression.java
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
import uk.co.badgersinfoil.metaas.dom.ASFilterExpression;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.List;

public class ASTASFilterExpression extends ASTExpression implements ASFilterExpression {

	public ASTASFilterExpression(LinkedListTree ast) {
		super(ast);
	}

	public Expression getQuery() {
		return ExpressionBuilder.build(ast.getLastChild());
	}

    public void addExpression(Expression expression) {
        throw new UnsupportedOperationException();
    }

    public List<Expression> getExpressions() {
        List<Expression> result = new java.util.ArrayList<Expression>();

        if (ast.getChildCount() < 2) {
            return result;
        }

        for (int i = 1; i < ast.getChildCount(); i++) {
            final LinkedListTree child = (LinkedListTree) ast.getChild(i);
            result.add(ExpressionBuilder.build(child));
        }
        
        return result;
    }

    public Expression getTarget() {
		return ExpressionBuilder.build(ast.getFirstChild());
	}
}
