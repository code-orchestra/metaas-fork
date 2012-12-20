/*
 * ASVarDeclarationFragment.java
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
 * The declaration of a single variable in a variable-declaration-statement,
 * such as the <code>a:String</code> in <code>var a:String;</code>
 * 
 * <p>This type is separate from {@link ASDeclarationStatement} since a single
 * statement may declare a list of variables.  For example:</p>
 * 
 * <pre class="eg">var a:String, b:int = 1;</pre>
 * 
 * @see ASDeclarationStatement#getVars()
 */
public interface ASVarDeclarationFragment extends ScriptElement {

    public TypeDescriptor getTypeDescriptor();

	public String getName();

	public String getTypeName();
    
	public Expression getInitializer();
}
