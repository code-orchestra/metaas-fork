/*
 * ASSuperStatement.java
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

import java.util.List;

/**
 * A call to a superclass constructor, such as <code>super(args);</code>.
 * 
 * <p><strong>NB</strong> the other use of the <code>super</code> keyword,
 * in <a href="http://livedocs.adobe.com/specs/actionscript/3/as3_specification129.html">super-expressions</a> is not represented by this class.</p>
 * 
 * @see StatementContainer#newSuper(List)
 */
public interface ASSuperStatement extends Statement {
	/**
	 * @param args a list of {@link Expression}
	 */
	public void setArguments(List args);
	/**
	 * @return a list of {@link Expression}
	 */
	public List getArguments();
}
