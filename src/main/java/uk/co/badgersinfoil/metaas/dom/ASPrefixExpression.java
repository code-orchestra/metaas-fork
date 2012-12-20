/*
 * ASPrefixExpression.java
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

/**
 * A prefix-expression, such as <code>!a</code> or <code>++a</code>.
 * 
 * @see uk.co.badgersinfoil.metaas.ActionScriptFactory#newPositiveExpression(Expression)
 * @see uk.co.badgersinfoil.metaas.ActionScriptFactory#newPreDecExpression(Expression)
 * @see uk.co.badgersinfoil.metaas.ActionScriptFactory#newPostIncExpression(Expression)
 * @see uk.co.badgersinfoil.metaas.ActionScriptFactory#newNotExpression(Expression)
 * @see uk.co.badgersinfoil.metaas.ActionScriptFactory#newNegativeExpression(Expression)
 */
public interface ASPrefixExpression extends Expression {
	public Op getOperator();
	public void setOperator(Op operator);
	public Expression getSubexpression();
	public void setSubexpression(Expression expression);

	/**
	 * The operators allowed for a prefix-expression
	 */
	public static class Op {
		private String name;
		private Op(String name) {
			this.name = name;
		}

		/** Negation <code>-expr</code> */
		public static final Op NEG = new Op("NEG");
        /** Bitwise NOT <code>~expr</code> */
		public static final Op BNOT = new Op("BNOT");
		/** Positive expression <code>+expr</code> */
		public static final Op POS = new Op("POS");
		/** Pre-decrement <code>--expr</code> */
		public static final Op PREDEC = new Op("PREDEC");
		/** Pre-increment <code>++expr</code> */
		public static final Op PREINC = new Op("PREINC");

		public String toString() {
			return name;
		}		
	}
}