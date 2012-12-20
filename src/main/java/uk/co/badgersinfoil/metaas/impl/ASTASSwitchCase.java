/*
 * ASTASSwitchStatement.java
 * 
 * Copyright (c) 2007-2008 David Holroyd
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
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASSwitchCase;
import uk.co.badgersinfoil.metaas.dom.StatementContainer;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.List;

public class ASTASSwitchCase extends ContainerDelegate implements ASSwitchCase {

	private static final int INDEX_LABELVAL = 0;
	private static final int INDEX_CONTAINER = 1;

	public ASTASSwitchCase(LinkedListTree ast) {
		super(ast);
	}

    public List<Expression> getConditions() {
        List<Expression> result = new java.util.ArrayList<Expression>();

        int i = INDEX_LABELVAL;
        LinkedListTree conditionAST = null;
        while ((conditionAST = getChild(i++)) != null) {
            if (conditionAST.getType() == AS3Parser.SWITCH_STATEMENT_LIST) {
                break;
            }

            result.add(ExpressionBuilder.build(conditionAST));

            if (i == ast.getChildCount() - 1) {
                break;
            }
        }

        return result;
    }

	public Expression getLabelValue() {
		return ExpressionBuilder.build(getChild(INDEX_LABELVAL));
	}
	
	public String getLabelValueString() {
		return ASTUtils.stringifyNode(getChild(INDEX_LABELVAL));
	}

	public void setLabelValue(String constant) {
		LinkedListTree label = AS3FragmentParser.parseExpr(constant);
		ast.setChildWithTokens(INDEX_LABELVAL, label);
	}

	private LinkedListTree getChild(int index) {
		return (LinkedListTree)ast.getChild(index);
	}
	protected StatementContainer getStatementContainer() {
        final LinkedListTree stmtContainerAST = ASTUtils.findChildByType(ast, AS3Parser.SWITCH_STATEMENT_LIST);
        return new ASTStatementList(stmtContainerAST);
	}

}