/*
 * ASTForInCommon.java
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
import uk.co.badgersinfoil.metaas.dom.ASDeclarationStatement;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.Statement;
import uk.co.badgersinfoil.metaas.dom.StatementContainer;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.List;


/**
 * Common code implementing 'for-in' and 'for-each-in' loop behaviour.
 */
abstract class ASTForInCommon extends ContainerDelegate {

	protected static final int INDEX_VAR = 0;
	protected static final int INDEX_ITERATED = 1;
	protected static final int INDEX_STATEMENT = 2;

	public ASTForInCommon(LinkedListTree ast) {
		super(ast);
	}

    public Statement getBody() {
        return StatementBuilder.build(stmt());
    }

    private LinkedListTree stmt() {
        return getChild(INDEX_STATEMENT);
    }

    @Override
    public List getStatementList() {
        try {
            return super.getStatementList();
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            if (message != null && message.startsWith("statement is not a block")) {
                throw new SyntaxException("Loop body is not a block");
            }
            throw e;
        }
    }

    public boolean hasVarDeclaration() {
        LinkedListTree initChild = getChild(INDEX_VAR);
        return initChild.getType() == AS3Parser.VAR;
    }

    public ASDeclarationStatement getDeclarationStatement() {
        if (!hasVarDeclaration()) {
            throw new IllegalStateException("This for statement doesn't have declaration statement, it uses an existing variable");
        }
        return (ASDeclarationStatement) StatementBuilder.build((LinkedListTree) ast.getChild(INDEX_VAR));
    }

    @Deprecated
    public String getVarName() {
        if (hasVarDeclaration()) {
            throw new IllegalStateException("This for statement has a declaration statement - use getDeclarationStatement()");
        }
        return getVarString();
    }

    public Expression getIterExpression() {
        if (hasVarDeclaration()) {
            throw new IllegalStateException("This for statement has a declaration statement - use getDeclarationStatement()");
        }
        Expression iterExpression = ExpressionBuilder.build((LinkedListTree) ast.getChild(INDEX_VAR));
        return iterExpression;
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

	public String getVarString() {
		return ASTUtils.stringifyNode(getChild(INDEX_VAR));
	}

	public String getIteratedString() {
		return ASTUtils.stringifyNode(iterated());
	}

	public Expression getIterated() {
		return ExpressionBuilder.build(iterated());
	}

	public void setVar(String expr) {
		LinkedListTree var = AS3FragmentParser.parseForInVar(expr);
		ast.setChildWithTokens(INDEX_VAR, var);
	}

	public void setIterated(String expr) {
		setIter(AS3FragmentParser.parseForInIterated(expr));
	}

	public void setIterated(Expression expr) {
		setIter(ast(expr));
	}

	private LinkedListTree getChild(int index) {
		return (LinkedListTree)ast.getChild(index);
	}

	protected StatementContainer getStatementContainer() {
        LinkedListTree statementContainer = getChild(INDEX_STATEMENT);
        // RE-3658
        if (statementContainer == null) {
            return null;
        }
        return new ASTStatementList(statementContainer);
	}
	private LinkedListTree iterated() {
		return getChild(INDEX_ITERATED);
	}
	private void setIter(LinkedListTree iterAST) {
		ast.setChildWithTokens(INDEX_ITERATED, iterAST);
	}
}