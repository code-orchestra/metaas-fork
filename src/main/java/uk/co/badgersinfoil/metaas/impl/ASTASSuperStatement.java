/*
 * ASTASSuperStatement.java
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

import java.util.List;
import uk.co.badgersinfoil.metaas.dom.ASSuperStatement;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTASSuperStatement extends ASTScriptElement implements ASSuperStatement {

	public ASTASSuperStatement(LinkedListTree superStmt) {
		super(superStmt);
	}

	public List getArguments() {
		return ArgumentUtils.astToExpressionList(ArgumentUtils.findArgs(ast));
	}

	public void setArguments(List args) {
		ArgumentUtils.overwriteArgsWithExpressionList(ArgumentUtils.findArgs(ast), args);
	}

    public List<Comment> getCommentsAfter() {
        return CommentUtils.getCommentAfter(this.getAST());
    }

    public int getSpacerSize() {
        return SpacerUtil.calculateSpacerSize(ast);
    }
}
