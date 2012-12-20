/*
 * ActionScriptParser.java
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

package uk.co.badgersinfoil.metaas;

import java.io.Reader;
import java.util.List;

import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.ASTStatementList;


/**
 * Parse an entire ActionScript source file from the given Reader,
 * returning a from CompilationUnit which details of the type contained
 * in the file can be obtained.
 * 
 * @see ActionScriptFactory#newParser()
 */
public interface ActionScriptParser {
	public ASCompilationUnit parse(Reader in);
	public ASTStatementList parseAsStatementList(Reader in);
	public ExpressionList parseAsExpressionList(Reader in);
    public List<ScriptElement> parseAsTypeMembers(Reader in);
    public ASType parseAsClass(Reader in);
    public MXMLASBlock parseAsMXMLASblock(Reader in);
}
