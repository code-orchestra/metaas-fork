/*
 * ActionScriptWriter.java
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

import java.io.IOException;
import java.io.Writer;

import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;

/**
 * Writes the ActionScript code in the given CompilationUnit to the
 * given Writer.
 * 
 * @see ActionScriptFactory#newWriter()
 */
public interface ActionScriptWriter {
	public void write(Writer writer, ASCompilationUnit cu) throws IOException;
}
