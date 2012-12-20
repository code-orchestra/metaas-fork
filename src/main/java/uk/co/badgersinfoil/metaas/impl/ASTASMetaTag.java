/*
 * ASTASMetaTag.java
 * 
 * Copyright (c) 2006 David Holroyd
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
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.SyntaxException;
import uk.co.badgersinfoil.metaas.dom.ASMetaTag;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;


public class ASTASMetaTag extends ASTScriptElement implements ASMetaTag {

	public ASTASMetaTag(LinkedListTree ast) {
		super(ast);
	}

	public void addParam(String constant) {
		addParam(toAST(constant));
	}

	private LinkedListTree toAST(String constant) {
		return ASTUtils.newAST(AS3Parser.STRING_LITERAL, ActionScriptFactory.str(constant));
	}

	private void addParam(LinkedListTree str) {
		LinkedListTree params = findOrAddParams();
		if (params.getChildCount() > 0) {
			params.appendToken(TokenBuilder.newComma());
			params.appendToken(TokenBuilder.newSpace());
		}
		params.addChildWithTokens(str);
	}

	public void addParam(int constant) {
		addParam(toAST(constant));
	}

	private LinkedListTree toAST(int constant) {
		return ASTUtils.newAST(AS3Parser.DECIMAL_LITERAL, String.valueOf(constant));
	}

	public void addParam(boolean constant) {
		addParam(toAST(constant));
	}

	private LinkedListTree toAST(boolean constant) {
		LinkedListTree str;
		if (constant) {
			str = ASTUtils.newAST(AS3Parser.TRUE, "true");
		} else {
			str = ASTUtils.newAST(AS3Parser.FALSE, "false");
		}
		return str;
	}

	public void addParam(String name, String constant) {
		addParam(name, toAST(constant));
	}

	private void addParam(String name, LinkedListTree val) {
		LinkedListTree assign = ASTUtils.newImaginaryAST(AS3Parser.ASSIGN);
		LinkedListTree ident = ASTUtils.newAST(AS3Parser.IDENT, name);
		assign.addChildWithTokens(ident);
		LinkedListToken assignToken = new LinkedListToken(AS3Parser.ASSIGN, "=");
		assign.appendToken(assignToken);
		assign.token = assignToken;
		assign.addChildWithTokens(val);

		addParam(assign);
	}

	public void addParam(String name, int constant) {
		addParam(name, toAST(constant));
	}

	public void addParam(String name, boolean constant) {
		addParam(name, toAST(constant));
	}

	public String getName() {
		return ast.getFirstChild().getText();
	}

	public Object getParamValue(String name) {
		LinkedListTree params = findParams();
		if (params == null) {
			return null;
		}
		for (ASTIterator i=new ASTIterator(params); i.hasNext(); ) {
			LinkedListTree child = i.next();
			if (child.getType()==AS3Parser.ASSIGN) {
				if (child.getFirstChild().getText().equals(name)) {
					return toParamValue(child.getLastChild());
				}
			}
		}
		return null;
	}

	public List getParams() {
		LinkedListTree params = findParams();
		if (params == null) {
			return Collections.EMPTY_LIST;
		}
		List result = new ArrayList();
		for (ASTIterator i=new ASTIterator(params); i.hasNext(); ) {
			LinkedListTree param = i.next();
			result.add(toParamValue(param));
		}
		return result;
	}

	private static Object toParamValue(LinkedListTree child) {
		switch (child.getType()) {
		    case AS3Parser.STRING_LITERAL:
			return ASTUtils.decodeStringLiteral(child.getText());
		    case AS3Parser.TRUE:
			return Boolean.TRUE;
		    case AS3Parser.FALSE:
			return Boolean.FALSE;
		    case AS3Parser.DECIMAL_LITERAL:
			return Integer.valueOf(child.getText());
            case AS3Parser.HEX_LITERAL:
            case AS3Parser.OCTAL_LITERAL:
            case AS3Parser.FLOAT_LITERAL:
            case AS3Parser.INFINITY:
            case AS3Parser.NAN:
            case AS3Parser.NULL:
                return child.getText();
		    case AS3Parser.ASSIGN:
			return new ASTParam(child);
		    default:
			throw new SyntaxException("Unexpected type "+ASTUtils.tokenName(child));
		}
	}

	private LinkedListTree findOrAddParams() {
		LinkedListTree params = findParams();
		if (params == null) {
			params = ASTUtils.newParentheticAST(AS3Parser.ANNOTATION_PARAMS, AS3Parser.LPAREN, "(", AS3Parser.RPAREN, ")");
			ast.addChildWithTokens(params);
		}
		return params;
	}
	private LinkedListTree findParams() {
		return ASTUtils.findChildByType(ast, AS3Parser.ANNOTATION_PARAMS);
	}

	public static class ASTParam implements ASMetaTag.Param {
		private LinkedListTree param;
		public ASTParam(LinkedListTree param) {
			this.param = param;
		}
		public String getName() {
			return param.getFirstChild().getText();
		}
		public Object getValue() {
			return toParamValue(param.getLastChild());
		}
	}
}