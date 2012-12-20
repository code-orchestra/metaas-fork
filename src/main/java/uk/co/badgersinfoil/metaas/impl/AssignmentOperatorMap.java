/*
 * AssignmentOperatorMap.java
 * 
 * Copyright (c) 2008 Matthew J Tretter
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
import uk.co.badgersinfoil.metaas.dom.ASAssignmentExpression;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;

public class AssignmentOperatorMap {

	private static final Map OPERATORS_BY_TYPE = new HashMap();
	private static final Map TYPES_BY_OPERATOR = new HashMap();

	private static void mapOp(int type, String text, ASAssignmentExpression.Op operator) {
		OPERATORS_BY_TYPE.put(new Integer(type), operator);
		TYPES_BY_OPERATOR.put(operator, new LinkedListToken(type, text));
	}

	static {
		mapOp(AS3Parser.PLUS_ASSIGN, "+=", ASAssignmentExpression.Op.ADD_ASSIGN);
		mapOp(AS3Parser.ASSIGN, "=", ASAssignmentExpression.Op.ASSIGN);
		mapOp(AS3Parser.BAND_ASSIGN, "&=", ASAssignmentExpression.Op.BITAND_ASSIGN);
		mapOp(AS3Parser.BOR_ASSIGN, "|=", ASAssignmentExpression.Op.BITOR_ASSIGN);
		mapOp(AS3Parser.BXOR_ASSIGN, "^=", ASAssignmentExpression.Op.BITXOR_ASSIGN);
		mapOp(AS3Parser.DIV_ASSIGN, "/=", ASAssignmentExpression.Op.DIV_ASSIGN);
		mapOp(AS3Parser.MOD_ASSIGN, "%=", ASAssignmentExpression.Op.MOD_ASSIGN);
		mapOp(AS3Parser.STAR_ASSIGN, "*=", ASAssignmentExpression.Op.MUL_ASSIGN);
		mapOp(AS3Parser.SL_ASSIGN, "<<=", ASAssignmentExpression.Op.SL_ASSIGN);
		mapOp(AS3Parser.SR_ASSIGN, ">>=", ASAssignmentExpression.Op.SR_ASSIGN);
		mapOp(AS3Parser.BSR_ASSIGN, ">>=", ASAssignmentExpression.Op.SRU_ASSIGN);
		mapOp(AS3Parser.MINUS_ASSIGN, "-=", ASAssignmentExpression.Op.SUB_ASSIGN);
        mapOp(AS3Parser.LAND_ASSIGN, "&&=", ASAssignmentExpression.Op.LAND_ASSIGN);
        mapOp(AS3Parser.LOR_ASSIGN, "||=", ASAssignmentExpression.Op.LOR_ASSIGN);
	}

	public static ASAssignmentExpression.Op opFromType(int type) {
		ASAssignmentExpression.Op op = (ASAssignmentExpression.Op)OPERATORS_BY_TYPE.get(new Integer(type));
		if (op == null) {
			throw new IllegalArgumentException("No operator for token-type "+ASTUtils.tokenName(type));
		}
		return op;
	}

	public static void initialiseFromOp(ASAssignmentExpression.Op operator, Token tok) {
		LinkedListToken type = (LinkedListToken)TYPES_BY_OPERATOR.get(operator);
		if (type == null) {
			throw new IllegalArgumentException("No operator for Op "+operator);
		}
		tok.setType(type.getType());
		tok.setText(type.getText());
	}
}