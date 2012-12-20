/*
 * ASTFunctionCommon.java
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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASArg;
import uk.co.badgersinfoil.metaas.dom.FunctionCommon;
import uk.co.badgersinfoil.metaas.dom.TypeDescriptor;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTFunctionCommon implements FunctionCommon {
	public static final String ELLIPSIS = "...";

	public LinkedListTree ast;
	
	public ASTFunctionCommon(LinkedListTree ast) {
		this.ast = ast;
	}

	public List getArgs() {
		List results = new LinkedList();
		LinkedListTree params = ASTUtils.findChildByType(ast, AS3Parser.PARAMS);
		for (ASTIterator i=new ASTIterator(params); i.hasNext(); ) {
			results.add(new ASTASArg(i.next()));
		}
		return Collections.unmodifiableList(results);
	}

	public TypeDescriptor getType() {
		LinkedListTree typeSpec = ASTUtils.findChildByType(ast, AS3Parser.TYPE_SPEC);
        if (typeSpec == null) {
            typeSpec = ASTUtils.findChildByType(ast, AS3Parser.VECTOR);
        }
		return new TypeDescriptorImpl(typeSpec);
	}

	public void setType(String typeName) {
		LinkedListTree type = ASTUtils.findChildByType(ast, AS3Parser.TYPE_SPEC);
		if (typeName == null) {
			if (type != null) {
				ast.deleteChild(ast.getIndexOfChild(type));
			}
			return;
		}
		LinkedListTree newType = AS3FragmentParser.parseTypeSpec(typeName);
		if (type == null) {
			ast.addChildWithTokens(ast.getChildCount()-1, newType);
		} else {
			type.setChildWithTokens(0, newType.getFirstChild());
		}
	}

	private LinkedListTree findParams() {
		return ASTUtils.findChildByType(ast, AS3Parser.PARAMS);
	}

	public ASArg addParam(String name, String type) {
		LinkedListTree param = ASTUtils.newAST(AS3Parser.PARAM);
		param.addChildWithTokens(ASTUtils.newAST(AS3Parser.IDENT, name));
		if (type != null) {
			param.addChildWithTokens(AS3FragmentParser.parseTypeSpec(type));
		}
		return param(param);
	}

	private ASArg param(LinkedListTree param) {
		LinkedListTree params = findParams();
		if (params.getChildCount() > 0) {
			params.appendToken(TokenBuilder.newComma());
			params.appendToken(TokenBuilder.newSpace());
		}
		params.addChildWithTokens(param);
		return new ASTASArg(param);
	}

	public ASArg addRestParam(String name) {
		if (ELLIPSIS.equals(name)) {
			return addAnonRestParam();
		}
		return addNamedRestParam(name);
	}

	private ASArg addAnonRestParam() {
		LinkedListTree param = ASTUtils.newAST(AS3Parser.PARAM);
		param.addChildWithTokens(ASTUtils.newAST(AS3Parser.REST, ELLIPSIS));
		return param(param);
	}

	private ASArg addNamedRestParam(String name) {
		LinkedListTree param = ASTUtils.newAST(AS3Parser.PARAM);
		param.addChildWithTokens(ASTUtils.newAST(AS3Parser.REST, ELLIPSIS));
		param.addChildWithTokens(ASTUtils.newAST(AS3Parser.IDENT, name));
		return param(param);
	}

	public ASArg removeParam(String name) {
		LinkedListTree params = ASTUtils.findChildByType(ast, AS3Parser.PARAMS);
		int count = 0;
		for (ASTIterator i=new ASTIterator(params); i.hasNext(); ) {
			LinkedListTree ast = i.next();
			ASTASArg arg = new ASTASArg(ast);
			if (arg.getName().equals(name)) {
				if (i.hasNext()) {
					ASTUtils.removeTrailingWhitespaceAndComma(ast.getStopToken());
				} else if (count > 0) {
					ASTUtils.removePreceedingWhitespaceAndComma(ast.getStartToken());
				}
				i.remove();
				return arg;
			}
			count++;
		}
		return null;
	}
}
