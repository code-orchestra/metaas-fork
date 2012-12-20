/*
 * ASTASIfStatement.java
 * 
 * Copyright (c) 2006-2008 David Holroyd
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
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.SyntaxException;
import uk.co.badgersinfoil.metaas.dom.ASBlock;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASIfStatement;
import uk.co.badgersinfoil.metaas.dom.Statement;
import uk.co.badgersinfoil.metaas.dom.StatementContainer;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.List;


public class ASTASIfStatement extends ContainerDelegate implements ASIfStatement {

	private static final int THEN_INDEX = 1;
	private static final int ELSE_INDEX = 2;

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

	public ASTASIfStatement(LinkedListTree ast) {
		super(ast);
		ASTUtils.assertAS3TokenTypeIs(AS3Parser.IF, ast.getType());
	}

	protected StatementContainer getStatementContainer() {
		LinkedListTree child = thenClause();
		if (AS3Parser.BLOCK != child.getType()) {
			throw new SyntaxException("statement is not a block");
		}
		return new ASTStatementList(child);
	}

	private LinkedListTree thenClause() {
		return (LinkedListTree)ast.getChild(THEN_INDEX);
	}

	public Statement getThenStatement() {
        LinkedListTree thenClause = thenClause();
        if (thenClause != null) {
            return StatementBuilder.build(thenClause);
        }
        return null;
	}
	
	public ASBlock getElse() {
		return elseBlock();
	}

	private LinkedListTree elseClause() {
		for (int i = ELSE_INDEX; i < ast.getChildCount(); i++) {
            final Tree child = ast.getChild(i);
            if (child.getType() == AS3Parser.LABEL) {
                continue;
            }
            return (LinkedListTree) child;
        }
        return null;
	}

	public ASBlock elseBlock() {
		LinkedListTree elseClause = elseClause();
		if (elseClause == null) {
			String indent = ASTUtils.findIndent(ast);
			elseClause = ASTUtils.newAST(AS3Parser.ELSE, "else");
			ast.appendToken(TokenBuilder.newSpace());
			ast.addChildWithTokens(elseClause);
			elseClause.appendToken(TokenBuilder.newSpace());
			LinkedListTree block = ASTBuilder.newBlock();
			elseClause.addChildWithTokens(block);
			ASTUtils.increaseIndentAfterFirstLine(block, indent);
			return new ASTStatementList(block);
		}
		Statement stmt = StatementBuilder.build(elseClause.getFirstChild());
		if (!(stmt instanceof ASBlock)) {
			throw new SyntaxException("Expected a block, got "+ASTUtils.tokenName(elseClause.getFirstChild()));
		}
		return (ASBlock)stmt;
	}

	public Statement getElseStatement() {
		LinkedListTree elseClause = elseClause();
		if (elseClause == null) {
			return null;
		}
		return StatementBuilder.build(elseClause.getFirstChild());
	}


	public void setThen(ASBlock block) {
		LinkedListTree theBlock = ((ASTStatementList)block).getAST();
		ASTIterator i = new ASTIterator(ast);
		i.next();  // move to condition
		i.next();  // move to then-branch
		i.replace(theBlock);
		String indent = ASTUtils.findIndent(ast);
		ASTUtils.increaseIndentAfterFirstLine(theBlock, indent);
	}

	public void setThenStatement(Statement then) {
		LinkedListTree thenAST = ((ASTStatementList)then).getAST();
		ast.setChildWithTokens(THEN_INDEX, thenAST);
		if (AS3Parser.BLOCK == thenAST.getType()) {
			String indent = ASTUtils.findIndent(ast);
			ASTUtils.increaseIndentAfterFirstLine(thenAST, indent);
		}
	}

	private LinkedListTree condition() {
		return ast.getFirstChild();
	}

	public String getConditionString() {
		return ASTUtils.stringifyNode(condition().getFirstChild());
	}

	public void setCondition(String expr) {
		LinkedListTree cond = AS3FragmentParser.parseCondition(expr);
		ast.setChildWithTokens(0, cond);
	}

    public List<Expression> getConditions() {
        List<Expression> conditions = new java.util.ArrayList<Expression>();

        final LinkedListTree conditionsAST = condition();
        for (int i = 0; i < conditionsAST.getChildCount(); i++) {
            conditions.add(ExpressionBuilder.build((LinkedListTree) conditionsAST.getChild(i)));
        }

        return conditions;
    }

	public Expression getCondition() {
		return ExpressionBuilder.build(condition().getFirstChild());
	}

	public void setCondition(Expression expr) {
		LinkedListTree condExpr = ((ASTExpression)expr).getAST();
		condition().setChildWithTokens(0, condExpr);
	}

    public List<Comment> getCommentsAfter() {
        return CommentUtils.getCommentAfter(this.getAST());
    }

    public int getSpacerSize() {
        return SpacerUtil.calculateSpacerSize(ast);
    }
}