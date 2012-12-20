/*
 * Visibility.java
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
 * Access allowed to a class member from other classes, as
 * specified by the <code>public</code>, <code>private</code>,
 * <code>protected</code> and <code>internal</code> modifiers (or lack of) in
 * the member's definition.
 */
public interface Visibility {

	/**
	 * Private access, as specified by the "private" modifier.
	 */
	public static final Visibility PRIVATE = new AccessVisibility("private");

	/**
	 * Public access, as specified by the "public" modifier.
	 */
	public static final Visibility PUBLIC = new AccessVisibility("public");

	/**
	 * Protected access, as specified by the "protected" modifier.
	 */
	public static final Visibility PROTECTED = new AccessVisibility("protected");

	/**
	 * Internal access, as specified by the "internal" modifier.
	 */
	public static final Visibility INTERNAL = new AccessVisibility("internal");

	/**
	 * Default access, as specified by the lack of any specific keyword.
	 */
	public static final Visibility DEFAULT = new AccessVisibility("[default]");
}