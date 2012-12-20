/*
 * ExpressionBuilder.java
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

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ExpressionBuilder {
    private static final String DOUBLE_DOT = "..";
    private static final String DOT = ".";

    public static Expression build(LinkedListTree ast) {
		switch (ast.getType()) {			
            case AS3Parser.HEX_LITERAL:
                return new ASTASHexLiteral(ast);
            case AS3Parser.OCTAL_LITERAL:
                return new ASTASOctalLiteral(ast);
            case AS3Parser.FLOAT_LITERAL:
                return new ASTASFloatLiteral(ast);
            case AS3Parser.DECIMAL_LITERAL:
				return new ASTASIntegerLiteral(ast);
			case AS3Parser.STRING_LITERAL:
				return new ASTASStringLiteral(ast);
			case AS3Parser.NULL:
				return new ASTASNullLiteral(ast);
			case AS3Parser.TRUE:
			case AS3Parser.FALSE:
				return new ASTASBooleanLiteral(ast);
			case AS3Parser.PLUS_ASSIGN:
			case AS3Parser.ASSIGN:
			case AS3Parser.BAND_ASSIGN:
			case AS3Parser.BOR_ASSIGN:
			case AS3Parser.BXOR_ASSIGN:
			case AS3Parser.DIV_ASSIGN:
			case AS3Parser.MOD_ASSIGN:
			case AS3Parser.STAR_ASSIGN:
			case AS3Parser.SL_ASSIGN:
			case AS3Parser.SR_ASSIGN:
			case AS3Parser.BSR_ASSIGN:
			case AS3Parser.MINUS_ASSIGN:
            case AS3Parser.LAND_ASSIGN:
            case AS3Parser.LOR_ASSIGN:                
				return new ASTASAssignmentExpression(ast);
			case AS3Parser.PLUS:
			case AS3Parser.LAND:
			case AS3Parser.BAND:
			case AS3Parser.BOR:
			case AS3Parser.BXOR:
			case AS3Parser.DIV:
			case AS3Parser.EQUAL:
			case AS3Parser.GE:
			case AS3Parser.GT:
			case AS3Parser.LE:
			case AS3Parser.LT:
			case AS3Parser.MOD:
			case AS3Parser.MULT:
			case AS3Parser.NOT_EQUAL:
			case AS3Parser.LOR:
			case AS3Parser.SL:
			case AS3Parser.SR:
			case AS3Parser.BSR:
			case AS3Parser.MINUS:
            case AS3Parser.INSTANCEOF:
            case AS3Parser.IS:
            case AS3Parser.IN:                
            case AS3Parser.AS:
            case AS3Parser.STRICT_EQUAL:
            case AS3Parser.STRICT_NOT_EQUAL:
				return new ASTASBinaryExpression(ast);
			case AS3Parser.POST_DEC:
			case AS3Parser.POST_INC:
				return new ASTASPostfixExpression(ast);
			case AS3Parser.LNOT:
				return new ASTASNotExpression(ast);
			case AS3Parser.PRE_DEC:
			case AS3Parser.PRE_INC:
			case AS3Parser.UNARY_PLUS:
			case AS3Parser.UNARY_MINUS:
            case AS3Parser.BNOT:
				return new ASTASPrefixExpression(ast);
			case AS3Parser.IDENT:
				return new ASTASSimpleNameExpression(ast);
			case AS3Parser.PROPERTY_OR_IDENTIFIER:
                String dotTokenText = ast.getText();
                if (DOUBLE_DOT.equals(dotTokenText)) {
                    return new ASTASDescendantExpression(ast);
                } else if (DOT.equals(dotTokenText)) {
                    return new ASTASFieldAccessExpression(ast);                    
                } else {
                    throw new IllegalStateException("Wrong dot type: " + dotTokenText);
                }
			case AS3Parser.METHOD_CALL:
				return new ASTASInvocationExpression(ast);
			case AS3Parser.ENCPS_EXPR:
                return new ASTASParenthesizedExpression(ast);
			case AS3Parser.ARRAY_ACC:
				return new ASTASArrayAccessExpression(ast);
			case AS3Parser.NEW:
				return new ASTASNewExpression(ast);
			case AS3Parser.QUESTION:
				return new ASTASConditionalExpression(ast);
			case AS3Parser.UNDEFINED:
				return new ASTASUndefinedLiteral(ast);
			case AS3Parser.FUNC_DEF:
				return new ASTASFunctionExpression(ast);
			case AS3Parser.ARRAY_LITERAL:
				return new ASTASArrayLiteral(ast);
			case AS3Parser.OBJECT_LITERAL:
				return new ASTASObjectLiteral(ast);
            case AS3Parser.DELETE:
                return new ASTASDeleteExpression(ast);
			case AS3Parser.XML_LITERAL:
				return new ASTASXMLLiteral(ast);
			case AS3Parser.REGEXP_LITERAL:
				return new ASTASRegexpLiteral(ast);
			case AS3Parser.E4X_DESC:
				return new ASTASDescendantExpression(ast);
			case AS3Parser.E4X_FILTER:
				return new ASTASFilterExpression(ast);
			case AS3Parser.E4X_ATTRI_STAR:
				return new ASTASStarAttribute(ast);
			case AS3Parser.E4X_ATTRI_PROPERTY:
				return new ASTASPropertyAttribute(ast);
			case AS3Parser.E4X_ATTRI_EXPR:
				return new ASTASExpressionAttribute(ast);
            case AS3Parser.SUPER:
                return new ASTASSuperExpression(ast);
            case AS3Parser.VECTOR:
                return new ASTASVectorExpression(ast);
            case AS3Parser.INFINITY:
                return new ASTASInfinityLiteral(ast);
            case AS3Parser.NAN:
                return new ASTASNanLiteral(ast);
            case AS3Parser.TYPEOF:
                return new ASTASTypeOfExpression(ast);
            case AS3Parser.DBL_COLON:
                return new ASTASSimpleNameExpression(ast);
            case AS3Parser.VECTOR_LITERAL:
                return new ASTASVectorLiteral(ast);
			default:
				throw new IllegalArgumentException("unhandled expression node type: "+ASTUtils.tokenName(ast));
		}
	}
}