/*
 * ASSwitchDefault.java
 * 
 * Copyright (c) 2007-2008 David Holroyd
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
 * A switch-statement <code>default:</code> label, and the list of
 * statements immediately following it.
 * 
 * <p>For example, given the following code...</p>
 * 
 * <pre class="eg">switch (c) {
 *    case 1:
 *	trace("one");
 *	break;
 *    <strong>default:
 *	trace("many");</strong>
 *}</pre>
 * 
 * <p>...an ASSwitchDefault instance would be available which will provide the
 * <code>trace("many");</code> statement when its {@link #getStatementList()}
 * method is called.</p>
 * 
 * @see ASSwitchStatement#newDefault()
 * @see ASSwitchStatement
 */
public interface ASSwitchDefault extends SwitchLabel {
	// no methods
}