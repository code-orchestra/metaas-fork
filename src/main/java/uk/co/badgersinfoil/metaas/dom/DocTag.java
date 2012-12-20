/*
 * ASTDocTag.java
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
 * A 'block' tag within a {@link DocComment}.
 * 
 * <p>The start of a block tag is indicated within the comment by an
 * '@'-prefixed word appearing at the start of a line (ignoring optional
 * leading whitespace and '*' characters).</p>
 * 
 * <p>For example the following comment includes a 'see' block tag:</p>
 * 
 * <pre class="eg">/*
 * * Good.
 * * @<span/>see MyClass
 * *<span/>/</pre>
 * 
 * <p>If this text is not the first thing on the line, it is not interpreted
 * as a block tag.  Here, '@see' will just be interpreted as part of the
 * description text, and metaas will not generate a DocTag for it:</p>
 * 
 * <pre class="eg">/*
 * * Bad. @<span/>see MyClass
 * *<span/>/</pre>
 * 
 * @see DocComment#addParaTag(String, String)
 * @see DocComment#delete(DocTag)
 * @see DocComment#findFirstTag(String)
 * @see DocComment#findTags(String)
 */
public interface DocTag {

	public String getName();
	
	public void setName(String name);

	public String getBodyString();

	public void setBody(String text);
}