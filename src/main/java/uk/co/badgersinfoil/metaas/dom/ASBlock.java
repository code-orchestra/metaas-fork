/*
 * ASBlock.java
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

import uk.co.badgersinfoil.metaas.ActionScriptFactory;

/**
 * A code-block, as used for a while-loop body or if-statement branch.
 *  
 * @see ActionScriptFactory#newBlock()
 */
public interface ASBlock extends Statement, StatementContainer, LabeledStatement {
	// An implementation of StatementContainer (e.g. an ASMethod) will not
	// be an appropriate value for things like a loop body.  Hence this
	// interface exists to differentiate those StatementContainer
	// implementations which are actually blocks; even though it doesn't
	// define any methods.
}
