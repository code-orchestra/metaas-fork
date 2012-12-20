/*
 * ASAssignmentExpression.java
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
 * An assignment expression, such as <code>a = b</code> or <code>a += b</code>.
 * 
 * @see ActionScriptFactory#newAssignExpression(Expression, Expression)
 * @see ActionScriptFactory#newAddAssignExpression(Expression, Expression)
 * @see ActionScriptFactory#newBitAndAssignExpression(Expression, Expression)
 * @see ActionScriptFactory#newBitOrAssignExpression(Expression, Expression)
 * @see ActionScriptFactory#newBitXorAssignExpression(Expression, Expression)
 * @see ActionScriptFactory#newDivideAssignExpression(Expression, Expression)
 * @see ActionScriptFactory#newModuloAssignExpression(Expression, Expression)
 * @see ActionScriptFactory#newMultiplyAssignExpression(Expression, Expression)
 * @see ActionScriptFactory#newShiftLeftAssignExpression(Expression, Expression)
 * @see ActionScriptFactory#newShiftRightAssignExpression(Expression, Expression)
 * @see ActionScriptFactory#newShiftRightUnsignedAssignExpression(Expression, Expression)
 * @see ActionScriptFactory#newSubtractAssignExpression(Expression, Expression)
 */
public interface ASAssignmentExpression extends Expression {
	public Op getOperator();
	public void setOperator(Op operator);
	public Expression getLeftSubexpression();
	public void setLeftSubexpression(Expression left);
	public Expression getRightSubexpression();
	public void setRightSubexpression(Expression right);

	/**
	 * Operators allowed for assignment-expressions
	 */
	public static class Op {
		private String name;
		private Op(String name) {
			this.name = name;
		}
		
		/** '+=' */
		public static final Op ADD_ASSIGN = new Op("ADD_ASSIGN");
		/** Assignment '=' */
		public static final Op ASSIGN = new Op("ASSIGN");
		/** '&=' */
		public static final Op BITAND_ASSIGN = new Op("BITAND_ASSIGN");
		/** '|=' */
		public static final Op BITOR_ASSIGN = new Op("BITOR_ASSIGN");
		/** '^=' */
		public static final Op BITXOR_ASSIGN = new Op("BITXOR_ASSIGN");
		/** '/=' */
		public static final Op DIV_ASSIGN = new Op("DIV_ASSIGN");
		/** '%=' */
		public static final Op MOD_ASSIGN = new Op("MOD_ASSIGN");
		/** '*=' */
		public static final Op MUL_ASSIGN = new Op("MUL_ASSIGN");
		/** Left Shift Assigment '<<=' */
		public static final Op SL_ASSIGN = new Op("SL_ASSIGN");
		/** Right Shift Assigment '>>=' */
		public static final Op SR_ASSIGN = new Op("SR_ASSIGN");
		/** Unsigned Right Shift Assigment '>>>=' */
		public static final Op SRU_ASSIGN = new Op("SRU_ASSIGN");
		/** '-=' */
		public static final Op SUB_ASSIGN = new Op("SUB_ASSIGN");
        /** '&&=' */
		public static final Op LAND_ASSIGN = new Op("LAND_ASSIGN");
        /** '||=' */
		public static final Op LOR_ASSIGN = new Op("LOR_ASSIGN");


		public String toString() {
			return name;
		}
	}
}