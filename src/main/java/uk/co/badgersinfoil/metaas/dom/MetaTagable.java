/*
 * MetaTagable.java
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
 * The common interface for API elements that may be tagged with metadata.
 * 
 * @see ASClassType
 * @see ASInterfaceType
 * @see ASField
 * @see ASMethod
 */
public interface MetaTagable {
	/**
	 * Returns the ASMetaTag with the given name.  If multiple metatags
	 * with the name are attached to this API element, only the first is
	 * returned.  If no such meta tag is found, this method returns null.
	 * 
	 * @see #getMetaTagsWithName(String)
	 */
	public ASMetaTag getFirstMetatag(String name);

	/**
	 * Returns the (possibly empty) list of ASMetaTag objects attached to
	 * this API element.
	 */
	public List getAllMetaTags();

	/**
	 * Returns a (possibly empty) list of ASMetaTag objects attached to
	 * this API element whose names match the given value.
	 */
	public List getMetaTagsWithName(String name);

	/**
	 * Creates and returns a new ASMetaTag attached to this API element.
	 */
	public ASMetaTag newMetaTag(String name);
}