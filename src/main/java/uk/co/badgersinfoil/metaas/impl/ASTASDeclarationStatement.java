/*
 * ASTASDeclarationStatement.java
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
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;


public class ASTASDeclarationStatement extends ASTScriptElement implements ASDeclarationStatement {

	public ASTASDeclarationStatement(LinkedListTree ast) {
		super(ast);
	}

	public boolean isConstant() {
		return ast.getType() == AS3Parser.CONST;
	}

	public void setConstant(boolean constant) {
		if (constant) {
			ast.token.setType(AS3Parser.CONST);
			ast.token.setText("const");
		} else {
			ast.token.setType(AS3Parser.VAR);
			ast.token.setText("var");
		}
	}

	public Expression getFirstVarInitializer() {
		return firstVar().getInitializer();
	}

	public String getFirstVarName() {
		return firstVar().getName();
	}

	public TypeDescriptor getFirstVarType() {
		return firstVar().getTypeDescriptor();
	}

	public List getVars() {
		List results = new ArrayList();
		for (ASTIterator i = new ASTIterator(ast); i.hasNext(); ) {
            LinkedListTree next = i.next();
            if (next.getType() != AS3Parser.ANNOTATIONS) {
                results.add(build(next));
            }
		}
		return Collections.unmodifiableList(results);
	}

	private ASVarDeclarationFragment firstVar() {
        LinkedListTree firstChild = ast.getFirstChild();
        if (firstChild.getType() == AS3Parser.ANNOTATIONS) {
            return build((LinkedListTree) ast.getChild(1));
        }
        return build(firstChild);
	}

	private ASVarDeclarationFragment build(LinkedListTree varDecl) {
		return new ASTASVarDeclarationFragment(varDecl);
	}

    public List<Comment> getCommentsAfter() {
        return CommentUtils.getCommentAfter(this.getAST());
    }

    public int getSpacerSize() {
        return SpacerUtil.calculateSpacerSize(ast);
    }

    public ASMetaTag getFirstMetatag(String name) {
        if (ast.getFirstChild().getType() == AS3Parser.ANNOTATIONS) {
            return TagUtils.getFirstMetaTag(ast, name);
        }
        return null;
    }

    public List getAllMetaTags() {
        if (ast.getFirstChild().getType() == AS3Parser.ANNOTATIONS) {
            return TagUtils.getAllMetaTags(ast);
        }
        return Collections.EMPTY_LIST;
    }

    public List getMetaTagsWithName(String name) {
        if (ast.getFirstChild().getType() == AS3Parser.ANNOTATIONS) {
            return TagUtils.getMetaTagWithName(ast, name);
        }
        return Collections.EMPTY_LIST;
    }

    public ASMetaTag newMetaTag(String name) {
        throw new UnsupportedOperationException("This method is never used in parser, so it is not implemented");
    }
}