/*
 * ASTASWhileStatement.java
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

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.SyntaxException;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASWhileStatement;
import uk.co.badgersinfoil.metaas.dom.Statement;
import uk.co.badgersinfoil.metaas.dom.StatementContainer;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.List;


public class ASTASWhileStatement extends ContainerDelegate implements ASWhileStatement {

	private static final int INDEX_CONDITION = 0;
	private static final int INDEX_STATEMENT = 1;

	public ASTASWhileStatement(LinkedListTree ast) {
		super(ast);
		ASTUtils.assertAS3TokenTypeIs(AS3Parser.WHILE, ast.getType());
	}

    public String getLabel() {
        LinkedListTree labelAST = ASTUtils.findChildByType(ast, AS3Parser.LABEL);
        if (labelAST != null) {
           LinkedListTree labelValueAST = labelAST.getFirstChild();
            if (labelValueAST != null) {
                return labelValueAST.getText();
            }
        }
        return null;
    }

	private LinkedListTree getChild(int index) {
		return (LinkedListTree)ast.getChild(index);
	}
	protected StatementContainer getStatementContainer() {
		LinkedListTree stmt = stmt();
		if (stmt.getType() != AS3Parser.BLOCK) {
			throw new SyntaxException("Loop body is not a block");
		}
		return new ASTStatementList(stmt);
	}

	private LinkedListTree stmt() {
		return getChild(INDEX_STATEMENT);
	}

	public String getConditionString() {
		return ASTUtils.stringifyNode(condition().getFirstChild());
	}

	public Expression getCondition() {
		return ExpressionBuilder.build(condition().getFirstChild());
	}

	public void setCondition(String expr) {
		LinkedListTree cond = AS3FragmentParser.parseCondition(expr);
		ast.setChildWithTokens(INDEX_CONDITION, cond);
	}

	public void setCondition(Expression expr) {
		condition().setChildWithTokens(INDEX_CONDITION, ast(expr));
	}

	private LinkedListTree condition() {
		return ast.getFirstChild();
	}

	public Statement getBody() {
		return StatementBuilder.build(stmt());
	}

    public List<Expression> getConditions() {
        List<Expression> conditions = new java.util.ArrayList<Expression>();

        final LinkedListTree conditionsAST = condition();
        for (int i = 0; i < conditionsAST.getChildCount(); i++) {
            conditions.add(ExpressionBuilder.build((LinkedListTree) conditionsAST.getChild(i)));
        }

        return conditions;
    }

    public List<Comment> getCommentsAfter() {
        return CommentUtils.getCommentAfter(this.getAST());
    }

    public int getSpacerSize() {
        return SpacerUtil.calculateSpacerSize(ast);
    }
}
