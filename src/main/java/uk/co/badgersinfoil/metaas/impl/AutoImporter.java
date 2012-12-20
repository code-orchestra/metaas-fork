/*
 * AutoImportor.java
 * 
 * Copyright (c) 2006-2007 David Holroyd
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;


/**
 * Modify all CompilationUnits in an ASProject so that fully qualified names
 * in the code are replaced with unqualified names, and the appropriate
 * import statements are added to the package declaration. 
 */
public class AutoImporter {

	private List visibleQNames = new ArrayList();
	
	// TODO: maintaining a list of names that *could* clash is not as nice
	// as actually working out which names *do* clash in a particular
	// compilation unit.  However, that would require two passes over the
	// compilation unit, so we're taking the simpler route for the mo.
	private Set possiblyAmbigLocalNames = new HashSet();

	public void performAutoImport(ASTActionScriptProject project) {
		loadAccessableLanguageElements(project);
		processSources(project);
	}

	private void loadAccessableLanguageElements(ASTActionScriptProject project) {
		final Set seenLocalNames = new HashSet();
		
		// look for definitions within the current project code itself
		ASVisitor visitor = new ASVisitor.Null() {
			public void visit(ASPackage pkg) {
				// TODO: will need to look for other top-level
				// elements, once metaas has support for them
				ASType type = pkg.getType();
				ASQName name = new ASQName(pkg.getName(), type.getName());
				visibleQNames.add(name);
				if (seenLocalNames.contains(name.getLocalName())) {
					possiblyAmbigLocalNames.add(name.getLocalName());
				} else {
					seenLocalNames.add(name.getLocalName());
				}
			}
		};
		new ASWalker(visitor).walk(project);

		// handle project classpath definitions
		Iterator i = project.getResourceRoots().iterator();
		while (i.hasNext()) {
			ResourceRoot resourceRoot = (ResourceRoot)i.next();
			Iterator j = resourceRoot.getDefinitionQNames().iterator();
			while (j.hasNext()) {
				ASQName name = (ASQName)j.next();
				visibleQNames.add(name);
				if (seenLocalNames.contains(name.getLocalName())) {
					possiblyAmbigLocalNames.add(name.getLocalName());
				} else {
					seenLocalNames.add(name.getLocalName());
				}
			}
		}
	}


	private void processSources(ASTActionScriptProject project) {
		new ASWalker(new ImportSourceProcessor()).walk(project);
	}

	private class ImportSourceProcessor extends ASVisitor.Null {
		private String currentPackageName;
		private ASQName currentTypeName;
		private ASPackage currentPackage;
		private Set currentPackageImports = new HashSet();
		private ASType currentType;

		// TODO: handle local names that would be ambiguous if not
		// fully-qualified.

		public void visit(ASPackage pkg) {
			currentPackage = pkg;
			currentPackageName = pkg.getName();
			inspectImports(pkg);
		}

		private void inspectImports(ASPackage pkg) {
			currentPackageImports.clear();
			List imports = pkg.findImports();
			for (Iterator i=imports.iterator(); i.hasNext(); ) {
				ASQName imp = new ASQName((String)i.next());
				if (imp.getLocalName().equals("*")) {
					// expand, to list all the imported names
					importNamesFromPackage(imp.getPackagePrefix());
				} else {
					currentPackageImports.add(imp);
				}
			}
		}

		private void importNamesFromPackage(String packagePrefix) {
			for (Iterator i=visibleQNames.iterator(); i.hasNext(); ) {
				ASQName name = (ASQName)i.next();
				if (packagePrefix.equals(name.getPackagePrefix())) {
					currentPackageImports.add(name);
				}
			}
		}

		public void visit(ASType type) {
			currentTypeName = new ASQName(currentPackageName, type.getName());
			currentType = type;
		}
		
		public void visit(ASClassType clazz) {
			String superClassName = clazz.getSuperclass();
			String processed = processTypeName(superClassName);
			if (processed != null) {
				clazz.setSuperclass(processed);
			}
			List interfaceNames = clazz.getImplementedInterfaces();
			for (Iterator i=interfaceNames.iterator(); i.hasNext(); ) {
				String interfaceName = (String)i.next();
				processed = processTypeName(interfaceName);
				if (processed != null) {
					clazz.removeImplementedInterface(interfaceName);
					clazz.addImplementedInterface(processed);
				}
			}
		}

		public void visit(ASInterfaceType iface) {
			List interfaceNames = iface.getSuperInterfaces();
			for (Iterator i=interfaceNames.iterator(); i.hasNext(); ) {
				String interfaceName = (String)i.next();
				String processed = processTypeName(interfaceName);
				if (processed != null) {
					iface.removeSuperInterface(interfaceName);
					iface.addSuperInterface(processed);
				}
			}
		}

		public void visit(ASMember member) {
			String processedName = processTypeName(member.getType().getName());
			if (processedName != null) {
				member.setType(processedName);
			}
		}

		private String processTypeName(String typeName) {
			if (typeName != null) {
				ASQName qname = new ASQName(typeName);
				if (qname.isQualified()
				    && visibleQNames.contains(qname))
				{
					if (importRequired(qname)) {
						addImport(qname);
					}
					if (!possiblyAmbigLocalNames.contains(qname.getLocalName())) {
						return qname.getLocalName();
					}
				}
			}
			return null;
		}

		private boolean importRequired(ASQName qname) {
			if (currentPackageName != null && currentPackageName.equals(qname.getPackagePrefix())) {
				return false;
			}
			return !currentPackageImports.contains(qname);
		}

		private void addImport(ASQName qname) {
			currentPackage.addImport(qname.toString());
			currentPackageImports.add(qname);
		}

		public void visit(ASMethod method) {
			// there's no visit(ASArg),
			processAllArgs(method);
			if (currentType instanceof ASClassType) {
				// the methods in an interface lack a body,
				processBody(method);
			}
		}

		private void processAllArgs(ASMethod method) {
			List args = method.getArgs();
			for (Iterator i=args.iterator(); i.hasNext(); ) {
				processArg((ASArg)i.next());
			}
		}

		private void processArg(ASArg arg) {
			String processedName = processTypeName(arg.getType().getName());
			if (processedName != null) {
				arg.setType(processedName);
			}
		}

		private void processBody(ASMethod method) {
			LinkedListTree ast = ((ASTASMethod)method).getAST();
			LinkedListTree body = (LinkedListTree)ast.getFirstChildWithType(AS3Parser.BLOCK);
			processTreeForImports(body);
		}

		// metaas doesn't have any representations for things at the
		// statement / expression level, so now we have to walk the AST,

		private void processTreeForImports(LinkedListTree ast) {
			if (processNodeForImports(ast)) {
				for (int i=0; i<ast.getChildCount(); i++) {
					LinkedListTree child = (LinkedListTree)ast.getChild(i);
					processTreeForImports(child);
				}
			}
		}

		private boolean processNodeForImports(LinkedListTree ast) {
			switch (ast.getType()) {
			case AS3Parser.TYPE_SPEC:
				processTypeSpecForImports(ast);
				return false;
			case AS3Parser.IDENTIFIER:
				processIdentifierForImports(ast);
				return false;
			case AS3Parser.PROPERTY_OR_IDENTIFIER:
				processPropertyOrIdentifierForImports(ast);
				return false;
			}
			return true;
		}

		private void processTypeSpecForImports(LinkedListTree ast) {
			String typeName = ASTUtils.typeSpecText(ast);
			String processedName = processTypeName(typeName);
			if (processedName != null) {
				LinkedListTree newTypeSpec = AS3FragmentParser.parseTypeSpec(processedName);
				ast.setChildWithTokens(0, newTypeSpec.getFirstChild());
			}
		}

		private void processIdentifierForImports(LinkedListTree ast) {
System.out.println(ASTUtils.identText(ast));
		}

		private ASQName processPropertyOrIdentifierForImports(LinkedListTree ast) {
//System.out.println("processPropertyOrIdentifierForImports("+ast.toStringTree()+")");
			LinkedListTree target = ast.getFirstChild();
			LinkedListTree name = ast.getLastChild();
			if (name.getType() != AS3Parser.IDENT) {
				return null;
			}
			ASQName qname = null;
			if (target.getType() == AS3Parser.PROPERTY_OR_IDENTIFIER) {
				ASQName tmp = processPropertyOrIdentifierForImports(target);
				if (tmp != null) {
					qname = new ASQName(tmp.toString(), name.getText());
				}
			} else if (target.getType() == AS3Parser.IDENT) {
				qname = new ASQName(target.getText(), name.getText());
			}
			if (qname == null) {
				ast.token.setType(AS3Parser.PROPERTY_ACCESS);
			} else {
				String processedName = processTypeName(qname.toString());
//System.out.println("  : processTypeName("+qname+") => "+processedName);
				if (processedName != null) {
//System.out.println("  : ast.start="+ast.getStartToken()+" ast.stop="+ast.getStopToken());
					int myIndex = ast.getParent().getIndexOfChild(ast);
					ast.getParent().setChildWithTokens(myIndex, ASTUtils.newAST(AS3Parser.IDENT, processedName));
					qname = null;
				}
			}
			return qname;
		}
	}
}
