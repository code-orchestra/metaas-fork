/*
 * ASTASFieldAccessExpression.java
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

import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASFieldAccessExpression;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTASFieldAccessExpression extends ASTExpression  implements ASFieldAccessExpression {

    private static final String DBL_COLON = "::";

    public ASTASFieldAccessExpression(LinkedListTree ast) {
		super(ast);
	}

    public String getNamespace() {
        String fqName = getFQName();
        if (fqName.contains(DBL_COLON)) {
            return fqName.split(DBL_COLON)[0];
        }
        return null;
    }

    public String getName() {
		String fqName = getFQName();
        if (fqName.contains(DBL_COLON)) {
            return fqName.split(DBL_COLON)[1];
        }
        return fqName;
	}

    private String getFQName() {
        return ASTUtils.stringifyNode(ast.getLastChild());
    }

	public void setName(String expr) {
		LinkedListTree name = AS3FragmentParser.parseIdent(expr);
		ast.setChildWithTokens(1, name);
	}
	
	public Expression getTargetExpression() {
		return ExpressionBuilder.build(ast.getFirstChild());
	}
	
	public void setTargetExpression(Expression expr)
	{
		LinkedListTree exprAST = ((ASTExpression)expr).getAST();
		ast.setChildWithTokens(0, exprAST);
	}

    public boolean isLineBreak() {
        return ASTUtils.lineBreakNearToken(ast);
    }
	
}