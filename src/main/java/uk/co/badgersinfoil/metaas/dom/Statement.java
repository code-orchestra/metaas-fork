/*
 * Statement.java
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
 * Super-interface for tagging objects that represent ActionScript 'statements'.
 * 
 * <p>New Statements can be created via the methods of
 * {@link StatementContainer}.</p>
 * 
 * @see StatementContainer#getStatementList()
 */
public interface Statement extends ScriptElement, CommentableAfter, HasSpacerAfter {

}
