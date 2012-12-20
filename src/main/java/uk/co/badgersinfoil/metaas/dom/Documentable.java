/*
 * Documentable.java
 * 
 * Copyright (c) 2006-2008 David Holroyd
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
 * Interface extended by ScriptElements which can have API
 * documentation comments attached.  The actual documentation itself is
 * represented by an instance of {@link DocComment}, as produced by
 * a Documentable object's {@link #getDocumentation()} method.
 */
public interface Documentable {

	/**
	 * @deprecated use {@link #getDescriptionString()} or
	 *             {@link #getDocumentation()} instead.
	 */
	public String getDocComment();
	
	/**
	 * Set the contents of the 'documentation comment' for this ActionScript
	 * API element.  If the given text has multiple lines, each line will
	 * have initial indentation and a '*' character added, so you shouldn't
	 * include these in the text yourself.  Supplying a null value removes
	 * any existing comment.
	 * 
	 * @throws SyntaxException if the given text contains an end-of-comment
	 *         marker
	 * @deprecated use {@link #setDescription(String)} or
	 *             {@link #getDocumentation()} instead.
	 */
	public void setDocComment(String text);

	/**
	 * Returns the 'description' part of this documentation comment as
	 * a string.  The description is the comment text from the start of
	 * the comment up to the first 'tagged paragraph'.
	 * 
	 * <p>This method is a shortcut for
	 * <code>getDocumentation().getDescriptionString()</code>.</p>
	 * 
	 * @see DocComment#getDescriptionString()
	 */
	public String getDescriptionString();

	/**
	 * Defines the 'description' part of the documentation comment of this
	 * API element.
	 * 
	 * <p>This method is a shortcut for
	 * <code>getDocumentation().setDescription(description)</code>.</p>
	 * 
	 * @throws SyntaxException if the given text contains an end-of-comment
	 *         marker, or a tagged-paragraph
	 * 
	 * @see DocComment#setDescriptionString(String)
	 */
	public void setDescription(String description);

	/**
	 * Returns a reference to an object allowing manipulation of
	 * documentation associated with this Documentable API element.  If
	 * there is currently no documentation available, this method will
	 * return an 'empty' object, but modifications to that object will
	 * cause a documentation-comment to be created in the source code.
	 */
	public DocComment getDocumentation();
}