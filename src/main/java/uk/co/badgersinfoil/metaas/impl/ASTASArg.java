/*
 * ASTASArg.java
 * 
 * Copyright (c) 2006-2007 David Holroyd
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
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;


public class ASTASArg extends ASTScriptElement implements ASArg {

	public ASTASArg(LinkedListTree ast) {
		super(ast);
	}

	public String getName() {
		LinkedListTree name = ASTUtils.findChildByType(ast, AS3Parser.IDENT);
		if (name == null) {
			if (isRest()) {
				return ASTFunctionCommon.ELLIPSIS;
			}
			throw new IllegalStateException("No parameter name, and not a 'rest' parameter");
		}
		return name.getText();
	}

	public TypeDescriptor getType() {
		LinkedListTree typeAST = ASTUtils.findChildByType(ast, AS3Parser.TYPE_SPEC);
        if (typeAST == null) {
            typeAST = ASTUtils.findChildByType(ast, AS3Parser.VECTOR);
        }
        return new TypeDescriptorImpl(typeAST);
	}

	public void setType(String typeName) {
		if (isRest()) {
			throw new SyntaxException("type specification not allowed for 'rest' parameters");
		}
		LinkedListTree type = ASTUtils.findChildByType(ast, AS3Parser.TYPE_SPEC);
		if (typeName == null) {
			if (type != null) {
				ast.deleteChild(1);
			}
			return;
		}
		LinkedListTree newType = AS3FragmentParser.parseTypeSpec(typeName);
		if (type == null) {
			ast.addChildWithTokens(newType);
		} else {
			type.setChildWithTokens(0, newType.getFirstChild());
		}
	}

	public String toString() {
		TypeDescriptor type = getType();
		if (type == null || type.getName() == null) {
			return getName();
		}
		return getName() + ":" + type;
	}

    public Expression getInitializer() {
        LinkedListTree init = ASTUtils.findChildByType(ast, AS3Parser.ASSIGN);
        if (init == null) {
            return null;
        }
        return ExpressionBuilder.build(init.getFirstChild());
    }

	public void setDefault(String value) {
		if (isRest()) {
			throw new SyntaxException("default value not allowed for 'rest' parameters");
		}
		ASTIterator i = new ASTIterator(ast);
		LinkedListTree assign = i.search(AS3Parser.ASSIGN);
		if (value == null) {
			if (assign != null) {
				i.remove();
			}
			return;
		}
		LinkedListTree def = AS3FragmentParser.parseParameterDefault(value);
		if (assign == null) {
			ast.addChildWithTokens(def);
		} else {
			assign.setChildWithTokens(0, def.getFirstChild());
		}
	}

	public String getDefaultString() {
		LinkedListTree assign = ASTUtils.findChildByType(ast, AS3Parser.ASSIGN);
		if (assign == null) {
			return null;
		}
		return ASTUtils.stringifyNode(assign.getFirstChild());
	}

	public boolean isRest() {
		return ast.getFirstChild().getType() == AS3Parser.REST;
	}

	public String getDescriptionString() {
		DocComment doc = getMethod().getDocumentation();
		String name = getName();
		DocTag tag = DocCommentUtils.findParam(doc, name);
		if (tag == null) {
			return null;
		}
		return tag.getBodyString().substring(name.length()+1);
	}

	public void setDescription(String description) {
		ASTDocComment doc = (ASTDocComment)getMethod().getDocumentation();
		String name = getName();
		DocTag tag = DocCommentUtils.findParam(doc, name);
		if (tag == null) {
			doc.addParaTag("param", name+" "+description);
		} else {
			tag.setBody(name+" "+description);
		}
	}

	private ASMethod getMethod() {
		return new ASTASMethod(ast.getParent().getParent());
	}
}