/*
 * ASVisitor.java
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

package uk.co.badgersinfoil.metaas.impl;

import uk.co.badgersinfoil.metaas.ActionScriptProject;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASField;
import uk.co.badgersinfoil.metaas.dom.ASInterfaceType;
import uk.co.badgersinfoil.metaas.dom.ASMember;
import uk.co.badgersinfoil.metaas.dom.ASMethod;
import uk.co.badgersinfoil.metaas.dom.ASPackage;
import uk.co.badgersinfoil.metaas.dom.ASType;

// TODO: promote this to the public API at some point

/**
 * To be implemented by classes that want to participate with {@link ASWalker}
 * in an iteration over the contents of an ASProject.
 */
public interface ASVisitor {
	public void visit(ActionScriptProject project);
	public void visit(ASCompilationUnit cu);
	public void visit(ASPackage pkg);
	/**
	 * called just before {@link #visit(ASClassType)} or
	 * {@link #visit(ASInterfaceType)} (as appropriate) with the same value.
	 */
	public void visit(ASType type);
	public void visit(ASClassType clazz);
	public void visit(ASInterfaceType iface);
	/**
	 * called just before {@link #visit(ASMethod)} or
	 * {@link #visit(ASField)} (as appropriate) with the same value.
	 */
	public void visit(ASMember member);
	public void visit(ASMethod method);
	public void visit(ASField field);
	
	/**
	 * An ASVisitor that provides empty implementations of all methods,
	 * usefull for easy subclassing when not all methods need implementing.
	 */
	public class Null implements ASVisitor {
		public void visit(ASClassType clazz)     { /* no-op */ }
		public void visit(ASField field)         { /* no-op */ }
		public void visit(ASInterfaceType iface) { /* no-op */ }
		public void visit(ASMember member)       { /* no-op */ }
		public void visit(ASMethod method)       { /* no-op */ }
		public void visit(ASPackage pkg)         { /* no-op */ }
		public void visit(ActionScriptProject project)     { /* no-op */ }
		public void visit(ASType type)           { /* no-op */ }
		public void visit(ASCompilationUnit cu)    { /* no-op */ }
	}
}
