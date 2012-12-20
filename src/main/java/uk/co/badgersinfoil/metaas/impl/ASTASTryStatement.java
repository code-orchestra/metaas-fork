/*
 * ASTASTryStatement.java
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
import uk.co.badgersinfoil.metaas.SyntaxException;
import uk.co.badgersinfoil.metaas.dom.ASCatchClause;
import uk.co.badgersinfoil.metaas.dom.ASFinallyClause;
import uk.co.badgersinfoil.metaas.dom.ASTryStatement;
import uk.co.badgersinfoil.metaas.dom.StatementContainer;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTASTryStatement extends ContainerDelegate implements ASTryStatement {

	public ASTASTryStatement(LinkedListTree ast) {
		super(ast);
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

	public List getCatchClauses() {
		List results = new ArrayList();
		ASTIterator i = new ASTIterator(ast);
		LinkedListTree catchClause;
		while ((catchClause = i.search(AS3Parser.CATCH)) != null) {
			results.add(new ASTASCatchClause(catchClause));
		}
		return Collections.unmodifiableList(results);
	}

	private LinkedListTree finallyClause() {
		return ASTUtils.findChildByType(ast, AS3Parser.FINALLY);
	}

	public ASFinallyClause getFinallyClause() {
		LinkedListTree finallyClause = finallyClause();
		if (finallyClause == null) {
			return null;
		}
		return new ASTASFinallyClause(finallyClause);
	}

	public ASFinallyClause newFinallyClause() {
		LinkedListTree finallyClause = finallyClause();
		if (finallyClause != null) {
			throw new SyntaxException("only one finally-clause allowed");
		}
		finallyClause = ASTBuilder.newFinallyClause();
		ast.addChildWithTokens(finallyClause);
		return new ASTASFinallyClause(finallyClause);
	}

	protected StatementContainer getStatementContainer() {
		return new ASTStatementList(ast.getFirstChild());
	}

	public ASCatchClause newCatchClause(String var, String type) {
		LinkedListTree catchClause = ASTBuilder.newCatchClause(var, type);
		LinkedListToken space = TokenBuilder.newSpace();
		catchClause.getStartToken().beforeInsert(space);
		catchClause.setStartToken(space);
		if (finallyClause() == null) {
			ast.addChildWithTokens(catchClause);
		} else {
			ast.addChildWithTokens(ast.getChildCount()-1, catchClause);
		}
		String indent = ASTUtils.findIndent(ast);
		ASTUtils.increaseIndentAfterFirstLine(catchClause, indent);
		return new ASTASCatchClause(catchClause);
	}

    public List<Comment> getCommentsAfter() {
        return CommentUtils.getCommentAfter(this.getAST());
    }

    public int getSpacerSize() {
        return SpacerUtil.calculateSpacerSize(ast);
    }
}
