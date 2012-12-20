/*
 * ASField.java
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

import java.util.List;

/**
 * A field definition within an ActionScript class.
 * 
 * @see ASClassType#newField(String, Visibility, String)
 */
public interface ASField extends ASMember, Commentable {
	/**
	 * Sets the ActionScript expression defining the initial value for
	 * this ActionScript field.
	 */
	void setInitializer(String expr);

	public void setInitializer(Expression expr);

    /**
     * Returns a list of {@link ASVarDeclarationFragment} objects.
     */
    List<ASField> getSubFields();    

	/**
	 * returns the initialiser expression for this field, or null if it
	 * has none.
	 */
	public Expression getInitializer();

	public void setConst(boolean isConst);

	public boolean isConst();
}