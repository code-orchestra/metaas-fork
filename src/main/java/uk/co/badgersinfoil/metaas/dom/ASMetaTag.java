/*
 * ASMetaTag.java
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

import java.util.List;

// TODO: extend Documentable, so that javadoc can be attached

/**
 * A 'metadata tag' which may be attached to types, methods or
 * fields.  A metadata tag is represented in ActionScript source code as a
 * definition within square brackets, that preceeds the API element to which
 * the metadata belongs.  For instance, this code defines the 'id' field to
 * have the metadata property 'Bindable':
 * <pre>
 * [Bindable]
 * public var id:Number;
 * </pre>
 * 
 * @see MetaTagable
 */
public interface ASMetaTag extends ScriptElement {
	/**
	 * Returns the name of this metadata tag.  e.g. given the tag,
	 * <pre>[Bindable]</pre>
	 * this method would return "Bindable".
	 */
	public String getName();
	
	/**
	 * Returns the list of parameters of this tag, or an empty list if the
	 * tag has no parameters. The values in the list will be of types
	 * <ul>
	 * <li>String</li>
	 * <li>Integer</li>
	 * <li>Boolean</li>
	 * <li>{@link ASMetaTag.Param}</li>
	 * </ul>
	 */
	public List getParams();
	
	/**
	 * Returns the value of the named parameter, or null if no such
	 * parameter is present in this metadata tag.  The type of the returned
	 * value will be one of,
	 * <ul>
	 * <li>String</li>
	 * <li>Integer</li>
	 * <li>Boolean</li>
	 * </ul>
	 */
	public Object getParamValue(String name);

	public void addParam(String constant);
	public void addParam(int constant);
	public void addParam(boolean constant);
	public void addParam(String name, String constant);
	public void addParam(String name, int constant);
	public void addParam(String name, boolean constant);

	/**
	 * A 'named parameter' within a metatag.
	 * For example the following tag,
	 * <pre>[Duplicatable(false, defaultValue="circular")]</pre>
	 * has two parameters, the second of which is a named parameter.
	 * If {@link ASMetaTag#getParams()} were called on the object
	 * representing that example, the second value in the list
	 * would be an ASMetaTag.Param instance which would return
	 * "defaultValue" for getName() and the String "circular" for
	 * getValue()
	 */
	public static interface Param {
		public String getName();
		/**
		 * The type of the returned
		 * value will be one of,
		 * <ul>
		 * <li>String</li>
		 * <li>Integer</li>
		 * <li>Boolean</li>
		 * </ul>
		 */
		public Object getValue();
	}
}
