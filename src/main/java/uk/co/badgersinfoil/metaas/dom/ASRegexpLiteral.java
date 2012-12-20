/*
 * ASRegexpLiteral.java
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
 * A literal 'regular expression', such as <code>/[a-z]+/</code>
 * 
 * @see uk.co.badgersinfoil.metaas.ActionScriptFactory#newRegexpLiteral(String, int)
 */
public interface ASRegexpLiteral extends Literal {

	/** represents no active flags for a regexp */
	public static final int FLAG_NONE = 0;
	/** represents the 'i' regexp flag */
	public static final int FLAG_IGNORE_CASE = 0;
	/** represents the 'g' regexp flag */
	public static final int FLAG_GLOBAL = 0;
	/** represents the 's' regexp flag */
	public static final int FLAG_DOT_ALL = 0;
	/** represents the 'x' regexp flag */
	public static final int FLAG_EXTENDED = 0;

    String getValue();

    String getModifier();

}
