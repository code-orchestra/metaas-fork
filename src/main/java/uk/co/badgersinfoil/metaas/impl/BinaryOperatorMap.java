/*
 * BinaryOperatorMap.java
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
import uk.co.badgersinfoil.metaas.dom.ASBinaryExpression;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;

public class BinaryOperatorMap {

	private static final Map OPERATORS_BY_TYPE = new HashMap();
	private static final Map TYPES_BY_OPERATOR = new HashMap();

	private static void mapOp(int type, String text, ASBinaryExpression.Op operator) {
		OPERATORS_BY_TYPE.put(new Integer(type), operator);
		TYPES_BY_OPERATOR.put(operator, new LinkedListToken(type, text));
	}

	static {
		mapOp(AS3Parser.PLUS, "+", ASBinaryExpression.Op.ADD);
		mapOp(AS3Parser.LAND, "&&", ASBinaryExpression.Op.AND);
		mapOp(AS3Parser.BAND, "&", ASBinaryExpression.Op.BITAND);
		mapOp(AS3Parser.BOR, "|", ASBinaryExpression.Op.BITOR);
		mapOp(AS3Parser.BXOR, "^", ASBinaryExpression.Op.BITXOR);
		mapOp(AS3Parser.DIV, "/", ASBinaryExpression.Op.DIV);
		mapOp(AS3Parser.EQUAL, "==", ASBinaryExpression.Op.EQ);
		mapOp(AS3Parser.GE, ">=", ASBinaryExpression.Op.GE);
		mapOp(AS3Parser.GT, ">", ASBinaryExpression.Op.GT);
		mapOp(AS3Parser.LE, "<=", ASBinaryExpression.Op.LE);
		mapOp(AS3Parser.LT, "<", ASBinaryExpression.Op.LT);
		mapOp(AS3Parser.MOD, "%", ASBinaryExpression.Op.MOD);
		mapOp(AS3Parser.MULT, "*", ASBinaryExpression.Op.MUL);
		mapOp(AS3Parser.NOT_EQUAL, "!=", ASBinaryExpression.Op.NE);
		mapOp(AS3Parser.LOR, "||", ASBinaryExpression.Op.OR);
		mapOp(AS3Parser.SL, "<<", ASBinaryExpression.Op.SL);
		mapOp(AS3Parser.SR, ">>", ASBinaryExpression.Op.SR);
		mapOp(AS3Parser.BSR, ">>>", ASBinaryExpression.Op.SRU);
		mapOp(AS3Parser.MINUS, "-", ASBinaryExpression.Op.SUB);
        mapOp(AS3Parser.INSTANCEOF, "instanceof", ASBinaryExpression.Op.INSTANCEOF);
        mapOp(AS3Parser.IS, "is", ASBinaryExpression.Op.IS);
        mapOp(AS3Parser.IN, "in", ASBinaryExpression.Op.IN);
        mapOp(AS3Parser.AS, "as", ASBinaryExpression.Op.AS);
        mapOp(AS3Parser.STRICT_EQUAL, "===", ASBinaryExpression.Op.STRICT_EQ);
        mapOp(AS3Parser.STRICT_NOT_EQUAL, "!==", ASBinaryExpression.Op.STRICT_NEQ);        
	}


	public static ASBinaryExpression.Op opFromType(int type) {
		ASBinaryExpression.Op op = (ASBinaryExpression.Op)OPERATORS_BY_TYPE.get(new Integer(type));
		if (op == null) {
			throw new IllegalArgumentException("No operator for token-type "+ASTUtils.tokenName(type));
		}
		return op;
	}

	public static void initialiseFromOp(ASBinaryExpression.Op operator, Token tok) {
		LinkedListToken type = (LinkedListToken)TYPES_BY_OPERATOR.get(operator);
		if (type == null) {
			throw new IllegalArgumentException("No operator for Op "+operator);
		}
		tok.setType(type.getType());
		tok.setText(type.getText());
	}
}