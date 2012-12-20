/*
 * ASBinaryExpression.java
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

package uk.co.badgersinfoil.metaas.dom;

import uk.co.badgersinfoil.metaas.ActionScriptFactory;

/**
 * A binary expression, such as <code>a + b</code> or <code>a && b</code>.
 * 
 * @see ActionScriptFactory#newAddExpression(Expression, Expression)
 * @see ActionScriptFactory#newAndExpression(Expression, Expression)
 * @see ActionScriptFactory#newBitAndExpression(Expression, Expression)
 * @see ActionScriptFactory#newBitOrExpression(Expression, Expression)
 * @see ActionScriptFactory#newBitXorExpression(Expression, Expression)
 * @see ActionScriptFactory#newDivisionExpression(Expression, Expression)
 * @see ActionScriptFactory#newEqualsExpression(Expression, Expression)
 * @see ActionScriptFactory#newGreaterEqualsExpression(Expression, Expression)
 * @see ActionScriptFactory#newGreaterThanExpression(Expression, Expression)
 * @see ActionScriptFactory#newLessEqualsExpression(Expression, Expression)
 * @see ActionScriptFactory#newLessThanExpression(Expression, Expression)
 * @see ActionScriptFactory#newModuloExpression(Expression, Expression)
 * @see ActionScriptFactory#newMultiplyExpression(Expression, Expression)
 * @see ActionScriptFactory#newNotEqualsExpression(Expression, Expression)
 * @see ActionScriptFactory#newOrExpression(Expression, Expression)
 * @see ActionScriptFactory#newShiftLeftExpression(Expression, Expression)
 * @see ActionScriptFactory#newShiftRightExpression(Expression, Expression)
 * @see ActionScriptFactory#newShiftRightUnsignedExpression(Expression, Expression)
 * @see ActionScriptFactory#newSubtractExpression(Expression, Expression)
 */
public interface ASBinaryExpression extends Expression {
	public Op getOperator();
	public void setOperator(Op operator);
	public Expression getLeftSubexpression();
	public void setLeftSubexpression(Expression left);
	public Expression getRightSubexpression();
	public void setRightSubexpression(Expression right);
    public boolean isLineBreak();

	/**
	 * Operators allowed for binary-expressions
	 */
	public static class Op {
		private String name;
		private Op(String name) {
			this.name = name;
		}

        /** As */
		public static final Op AS = new Op("AS");
        /** Is */
		public static final Op IS = new Op("IS");
        /** In */
		public static final Op IN = new Op("IN");
        /** InstanceOf */
		public static final Op INSTANCEOF = new Op("INSTANCEOF");
		/** Addition '+' */
		public static final Op ADD = new Op("ADD");
		/** Logical and '&&' */
		public static final Op AND = new Op("AND");
		/** Bit-wise and '&' */
		public static final Op BITAND = new Op("BITAND");
		/** Bit-wise or '|' */
		public static final Op BITOR = new Op("BITOR");
		/** Bit-wise xor '^' */
		public static final Op BITXOR = new Op("BITXOR");
		/** Division '/' */
		public static final Op DIV = new Op("DIV");
		/** Equality '==' */
		public static final Op EQ = new Op("EQ");
		/** Greater-than-or-equals '>=' */
		public static final Op GE = new Op("GE");
		/** Strictly greater-than '>' */
		public static final Op GT = new Op("GT");
		/** Less-than-or-equals '<=' */
		public static final Op LE = new Op("LE");
		/** Strictly less-than '<' */
		public static final Op LT = new Op("LT");
		/** Modulo '%' */
		public static final Op MOD = new Op("MOD");
		/** Multiplication '*' */
		public static final Op MUL = new Op("MUL");
		/** Not equal '!=' */
		public static final Op NE = new Op("NE");
		/** Logical or '||' */
		public static final Op OR = new Op("OR");
		/** Shift left '<<' */
		public static final Op SL = new Op("SL");
		/** Shift right '>>' */
		public static final Op SR = new Op("SR");
		/** Shift right, unsigned '>>>' */
		public static final Op SRU = new Op("SRU");
		/** Subtraction '-' */
		public static final Op SUB = new Op("SUB");
		public static final Op STRICT_EQ = new Op("STRICT_EQ"); 
        public static final Op STRICT_NEQ = new Op("STRICT_NEQ");

		public String toString() {
			return name;
		}
	}
}