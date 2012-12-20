/*
 * ASTASSwitchStatement.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.runtime.tree.Tree;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASSwitchCase;
import uk.co.badgersinfoil.metaas.dom.ASSwitchDefault;
import uk.co.badgersinfoil.metaas.dom.ASSwitchStatement;
import uk.co.badgersinfoil.metaas.dom.SwitchLabel;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;


public class ASTASSwitchStatement extends ASTScriptElement implements ASSwitchStatement {

	public ASTASSwitchStatement(LinkedListTree ast) {
		super(ast);
	}
	
	private LinkedListTree block() {
		return ast.getLastChild();
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

	public ASSwitchCase newCase(String expr) {
		LinkedListTree caseStmt = ASTUtils.newAST(AS3Parser.CASE, "case");
		caseStmt.appendToken(TokenBuilder.newSpace());
		caseStmt.addChildWithTokens(AS3FragmentParser.parseExpr(expr));
		caseStmt.appendToken(TokenBuilder.newColon());
		LinkedListTree stmtList = ASTUtils.newPlaceholderAST(AS3Parser.SWITCH_STATEMENT_LIST);
		caseStmt.addChildWithTokens(stmtList);
		ASTUtils.addChildWithIndentation(block(), caseStmt);
		return new ASTASSwitchCase(caseStmt);
	}

	public ASSwitchDefault newDefault() {
		LinkedListTree defaultStmt = ASTUtils.newAST(AS3Parser.DEFAULT, "default");
		defaultStmt.appendToken(TokenBuilder.newColon());
		LinkedListTree stmtList = ASTUtils.newPlaceholderAST(AS3Parser.SWITCH_STATEMENT_LIST);
		defaultStmt.addChildWithTokens(stmtList);
		ASTUtils.addChildWithIndentation(block(), defaultStmt);
		return new ASTASSwitchDefault(defaultStmt);
	}
	
	private LinkedListTree cond() {
		return ast.getFirstChild();
	}

    public List<Expression> getConditions() {
        List<Expression> result = new ArrayList<Expression>();
        final LinkedListTree conditions = cond();
        for (int i = 0; i < conditions.getChildCount(); i++) {
            result.add(ExpressionBuilder.build((LinkedListTree) conditions.getChild(i)));
        }
        return result;
    }
    
	public Expression getCondition() {
		return ExpressionBuilder.build(cond().getFirstChild());
	}

	public void setCondition(Expression expr) {
		cond().setChildWithTokens(0, ast(expr));
	}

	public List getLabels() {
		List results = new ArrayList();
		ASTIterator i = new ASTIterator(block());
		while (i.hasNext()) {
			results.add(buildLabel(i.next()));
		}
		return Collections.unmodifiableList(results);
	}

	private SwitchLabel buildLabel(LinkedListTree lab) {
		switch (lab.getType()) {
		    case AS3Parser.CASE:
			return new ASTASSwitchCase(lab);
		    case AS3Parser.DEFAULT:
			return new ASTASSwitchDefault(lab);
		    default:
			throw new IllegalArgumentException("unhandled node type "+ASTUtils.tokenName(lab));
		}
	}

    public List<Comment> getCommentsAfter() {
        return CommentUtils.getCommentAfter(this.getAST());
    }

    public int getSpacerSize() {
        return SpacerUtil.calculateSpacerSize(ast);
    }
}