/*
 * ASInterfaceType.java
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

/**
 * A handle on the definition of an ActionScript interface.
 * 
 * @see uk.co.badgersinfoil.metaas.ActionScriptFactory#newInterface(String)
 */
public interface ASInterfaceType extends ASType {

	/**
	 * Returns a list of Strings which are the names of ActionScript
	 * interfaces which this ActionScript interface extends.
	 */
	public List getSuperInterfaces();

	/**
	 * Adds the given ActionScript interface name to the list of interfaces
	 * which this ActionScript interface extends.
	 */
	public void addSuperInterface(String interfaceName);

	/**
	 * Removes the given ActionScript interface name from the list of
	 * interfaces which this ActionScript interface extends.
	 */
	public void removeSuperInterface(String interfaceName);
}
