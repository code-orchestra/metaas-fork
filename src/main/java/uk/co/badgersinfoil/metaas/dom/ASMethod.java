/*
 * ASMethod.java
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

package uk.co.badgersinfoil.metaas.dom;


/**
 * An ActionScript method definition within an ActionScript class or
 * interface.
 * 
 * @see ASType#newMethod(String, Visibility, String)
 */
public interface ASMethod extends FunctionCommon, StatementContainer, ASMember, CommentableBefore {

	/**
	 * Constants defined by this class denote whether a method is actually
	 * an accessor 'get' or 'set' function.
	 * 
	 * @see ASMethod#getAccessorRole()
	 * @see ASMethod#setAccessorRole(AccessorRole)
	 */
	public static final class AccessorRole {
		private String text;

		private AccessorRole(String text) { this.text = text; }
		/**
		 * A standard method; not a getter or setter.
		 */
		public static final AccessorRole NORMAL_METHOD = new AccessorRole("NORMAL_METHOD");
		/**
		 * The method is a 'get' accessor
		 */
		public static final AccessorRole SETTER = new AccessorRole("SETTER");
		/**
		 * The method is a 'set' accessor
		 */
		public static final AccessorRole GETTER = new AccessorRole("GETTER");
		
		public String toString() { return text; }
	}

	/**
	 * Returns a value representing any protection-against-access defined
	 * for this ActionScript method.
	 */
	public Visibility getVisibility();

	/**
	 * Defines the level of protection-against-external-access for this
	 * ActionScript method.
	 */
	public void setVisibility(Visibility visibility);

	public void setOverride(boolean value);

	public boolean isOverride();

	/**
	 * Returns true if this ActionScript method is static (i.e. the method
	 * definition uses the <code>static</code> keyword).
	 */
	public boolean isStatic();

    boolean isVirtual();

    boolean isNative();

    boolean isFinal();

	/**
	 * Defines whether this ActionScript method is static or not.
	 */
	public void setStatic(boolean s);

	/**
	 * Returns on of {@link AccessorRole#NORMAL_METHOD},
	 * {@link AccessorRole#GETTER} or {@link AccessorRole#SETTER}, with
	 * NORMAL_METHOD being the default for newly synthesised methods.
	 */
	public AccessorRole getAccessorRole();

	/**
	 * Allows the role of a method to be changed.
	 */
	public void setAccessorRole(AccessorRole getter);

	/**
	 * Shortcut method to update the <code><span>@</span>return</code>
	 * tagged paragraph in this method's documentation comment, or create
	 * one if it doesn't exist.
	 */
	public void setReturnDescription(String description);

	/**
	 * Returns any description of the return value from this methods
	 * documentation comment.  If there is no doc-comment, or if the
	 * documentation does not contain a <code><span>@</span>return</code>
	 * tagged paragraph, this method will return null.
	 * 
	 * @see DocComment
	 */
	public String getReturnDescriptionString();
}