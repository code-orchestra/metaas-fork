/*
 * PrefixOperatorMap.java
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

import java.util.HashMap;
import java.util.Map;
import org.antlr.runtime.Token;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASPrefixExpression;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;

public class PrefixOperatorMap {

	private static final Map OPERATORS_BY_TYPE = new HashMap();
	private static final Map TYPES_BY_OPERATOR = new HashMap();

	private static void mapOp(int type, String text, ASPrefixExpression.Op operator) {
		OPERATORS_BY_TYPE.put(new Integer(type), operator);
		TYPES_BY_OPERATOR.put(operator, new LinkedListToken(type, text));
	}

	static {
		mapOp(AS3Parser.UNARY_MINUS, "-", ASPrefixExpression.Op.NEG);
        mapOp(AS3Parser.BNOT, "~", ASPrefixExpression.Op.BNOT);
		mapOp(AS3Parser.UNARY_PLUS, "+", ASPrefixExpression.Op.POS);
		mapOp(AS3Parser.PRE_DEC, "--", ASPrefixExpression.Op.PREDEC);
		mapOp(AS3Parser.PRE_INC, "++", ASPrefixExpression.Op.PREINC);
	}


	public static ASPrefixExpression.Op opFromType(int type) {
		ASPrefixExpression.Op op = (ASPrefixExpression.Op)OPERATORS_BY_TYPE.get(new Integer(type));
		if (op == null) {
			throw new IllegalArgumentException("No operator for token-type "+ASTUtils.tokenName(type));
		}
		return op;
	}

	public static void initialiseFromOp(ASPrefixExpression.Op operator, Token tok) {
		LinkedListToken type = (LinkedListToken)TYPES_BY_OPERATOR.get(operator);
		if (type == null) {
			throw new IllegalArgumentException("No operator for Op "+operator);
		}
		tok.setType(type.getType());
		tok.setText(type.getText());
	}
}