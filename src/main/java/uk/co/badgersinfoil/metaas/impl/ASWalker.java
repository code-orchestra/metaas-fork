/*
 * ASWalker.java
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

import java.util.Iterator;
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

public class ASWalker {
	private ASVisitor visitor;

	public ASWalker(ASVisitor visitor) {
		this.visitor = visitor;
	}

	public void walk(ActionScriptProject project) {
		visitor.visit(project);
		for (Iterator i=project.getCompilationUnits().iterator(); i.hasNext(); ) {
			ASTASCompilationUnit cu = (ASTASCompilationUnit)i.next();
			walk(cu);
		}
	}

	public void walk(ASCompilationUnit cu) {
		visitor.visit(cu);
		walk(cu.getPackage());
	}

	public void walk(ASPackage pkg) {
		visitor.visit(pkg);
		walk(pkg.getType());
	}

	public void walk(ASType type) {
		visitor.visit(type);
		if (type instanceof ASClassType) {
			walk((ASClassType)type);
		}
		if (type instanceof ASInterfaceType) {
			walk((ASInterfaceType)type);
		}
	}

	public void walk(ASClassType clazz) {
		visitor.visit(clazz);
		Iterator i;
		for (i=clazz.getMethods().iterator(); i.hasNext(); ) {
			ASMethod method = (ASMethod)i.next();
			walk((ASMember)method);
			walk(method);
		}
		for (i=clazz.getFields().iterator(); i.hasNext(); ) {
			ASField field = (ASField)i.next();
			walk((ASMember)field);
			walk(field);
		}
	}

	public void walk(ASInterfaceType iface) {
		visitor.visit(iface);
		Iterator i;
		for (i=iface .getMethods().iterator(); i.hasNext(); ) {
			ASMethod method = (ASMethod)i.next();
			walk((ASMember)method);
			walk(method);
		}
	}

	public void walk(ASMember member) {
		visitor.visit(member);
	}

	public void walk(ASMethod method) {
		visitor.visit(method);
	}

	public void walk(ASField field) {
		visitor.visit(field);
	}
}