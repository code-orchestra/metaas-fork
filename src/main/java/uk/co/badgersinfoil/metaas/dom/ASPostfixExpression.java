/*
 * ASPostfixExpression.java
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
 * A postfix-expression, such as <code>a++</code> or <code>a--</code>.
 * 
 * @see uk.co.badgersinfoil.metaas.ActionScriptFactory#newPostDecExpression(Expression)
 * @see uk.co.badgersinfoil.metaas.ActionScriptFactory#newPostIncExpression(Expression)
 */
public interface ASPostfixExpression extends Expression {
	public Op getOperator();
	public void setOperator(Op operator);
	public Expression getSubexpression();
	public void setSubexpression(Expression expression);

	/**
	 * The operators allowed for postfix expressions
	 */
	public static class Op {
		private String name;
		private Op(String name) {
			this.name = name;
		}

		/** Post-decrement <code>expr--</code> */
		public static final Op POSTDEC = new Op("POSTDEC");
		/** Post-increment <code>expr++</code> */
		public static final Op POSTINC = new Op("POSTINC");

		public String toString() {
			return name;
		}		
	}
}