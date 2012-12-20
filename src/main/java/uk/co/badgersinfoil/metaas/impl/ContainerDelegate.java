/*
 * ContainerDelegate.java
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

package uk.co.badgersinfoil.metaas.impl;

import java.util.List;
import uk.co.badgersinfoil.metaas.dom.ASBreakStatement;
import uk.co.badgersinfoil.metaas.dom.ASContinueStatement;
import uk.co.badgersinfoil.metaas.dom.ASDeclarationStatement;
import uk.co.badgersinfoil.metaas.dom.ASDefaultXMLNamespaceStatement;
import uk.co.badgersinfoil.metaas.dom.ASDoWhileStatement;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASExpressionStatement;
import uk.co.badgersinfoil.metaas.dom.ASForEachInStatement;
import uk.co.badgersinfoil.metaas.dom.ASForInStatement;
import uk.co.badgersinfoil.metaas.dom.ASForStatement;
import uk.co.badgersinfoil.metaas.dom.ASIfStatement;
import uk.co.badgersinfoil.metaas.dom.ASReturnStatement;
import uk.co.badgersinfoil.metaas.dom.ASSuperStatement;
import uk.co.badgersinfoil.metaas.dom.ASSwitchStatement;
import uk.co.badgersinfoil.metaas.dom.ASThrowStatement;
import uk.co.badgersinfoil.metaas.dom.ASTryStatement;
import uk.co.badgersinfoil.metaas.dom.ASWhileStatement;
import uk.co.badgersinfoil.metaas.dom.ASWithStatement;
import uk.co.badgersinfoil.metaas.dom.Statement;
import uk.co.badgersinfoil.metaas.dom.StatementContainer;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public abstract class ContainerDelegate extends ASTScriptElement implements StatementContainer {

	public ContainerDelegate(LinkedListTree ast) {
		super(ast);
	}

	protected abstract StatementContainer getStatementContainer();

    private StatementContainer getSafeStatementContainer() {
        StatementContainer statementContainer = getStatementContainer();
        if (statementContainer != null) {
            return statementContainer;
        }

        return new StatementContainer() {
            public void addComment(String text) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public Statement addStmt(String statement) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASExpressionStatement newExprStmt(String expr) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASExpressionStatement newExprStmt(Expression expr) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASIfStatement newIf(String condition) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASIfStatement newIf(Expression condition) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASForStatement newFor(String init, String condition, String update) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASForStatement newFor(Expression init, Expression condition, Expression update) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASForInStatement newForIn(String init, String list) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASForInStatement newForIn(Expression init, Expression list) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASForEachInStatement newForEachIn(String init, String list) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASForEachInStatement newForEachIn(Expression init, Expression list) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASWhileStatement newWhile(String condition) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASWhileStatement newWhile(Expression condition) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASDoWhileStatement newDoWhile(String condition) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASDoWhileStatement newDoWhile(Expression condition) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASSwitchStatement newSwitch(String condition) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASSwitchStatement newSwitch(Expression condition) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASWithStatement newWith(String string) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASWithStatement newWith(Expression string) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASDeclarationStatement newDeclaration(String assignment) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASDeclarationStatement newDeclaration(Expression assignment) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASReturnStatement newReturn(String expr) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASReturnStatement newReturn(Expression expr) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASReturnStatement newReturn() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASBreakStatement newBreak() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASTryStatement newTryCatch(String var, String type) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASTryStatement newTryFinally() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASContinueStatement newContinue() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASThrowStatement newThrow(Expression t) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ASDefaultXMLNamespaceStatement newDefaultXMLNamespace(String namespace) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean containsCode() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public List getStatementList() {
                return new java.util.ArrayList();  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }

	public Statement addStmt(String statement) {
		return getSafeStatementContainer().addStmt(statement);
	}

	public ASExpressionStatement newExprStmt(String expr) {
		return getSafeStatementContainer().newExprStmt(expr);
	}
	public ASExpressionStatement newExprStmt(Expression expr) {
		return getSafeStatementContainer().newExprStmt(expr);
	}

	public void addComment(String text) {
		getSafeStatementContainer().addComment(text);
	}

	public ASIfStatement newIf(String condition) {
		return getSafeStatementContainer().newIf(condition);
	}
	public ASIfStatement newIf(Expression condition) {
		return getSafeStatementContainer().newIf(condition);
	}

	public ASForStatement newFor(String init, String condition, String update) {
		return getSafeStatementContainer().newFor(init, condition, update);
	}
	public ASForStatement newFor(Expression init, Expression condition, Expression update) {
		return getSafeStatementContainer().newFor(init, condition, update);
	}

	public ASForInStatement newForIn(String init, String list) {
		return getSafeStatementContainer().newForIn(init, list);
	}
	public ASForInStatement newForIn(Expression init, Expression list) {
		return getSafeStatementContainer().newForIn(init, list);
	}

	public ASForEachInStatement newForEachIn(String init, String list) {
		return getSafeStatementContainer().newForEachIn(init, list);
	}
	public ASForEachInStatement newForEachIn(Expression init, Expression list) {
		return getSafeStatementContainer().newForEachIn(init, list);
	}


	public ASWhileStatement newWhile(String condition) {
		return getSafeStatementContainer().newWhile(condition);
	}
	public ASWhileStatement newWhile(Expression condition) {
		return getSafeStatementContainer().newWhile(condition);
	}

	public ASDoWhileStatement newDoWhile(String condition) {
		return getSafeStatementContainer().newDoWhile(condition);
	}
	public ASDoWhileStatement newDoWhile(Expression condition) {
		return getSafeStatementContainer().newDoWhile(condition);
	}

	public ASSwitchStatement newSwitch(String condition) {
		return getSafeStatementContainer().newSwitch(condition);
	}
	public ASSwitchStatement newSwitch(Expression condition) {
		return getSafeStatementContainer().newSwitch(condition);
	}

	public ASWithStatement newWith(String expr) {
		return getSafeStatementContainer().newWith(expr);
	}
	public ASWithStatement newWith(Expression expr) {
		return getSafeStatementContainer().newWith(expr);
	}

	public ASDeclarationStatement newDeclaration(String assignment) {
		return getSafeStatementContainer().newDeclaration(assignment);
	}
	public ASDeclarationStatement newDeclaration(Expression assignment) {
		return getSafeStatementContainer().newDeclaration(assignment);
	}

	public ASReturnStatement newReturn(String expr) {
		return getSafeStatementContainer().newReturn(expr);
	}
	public ASReturnStatement newReturn(Expression expr) {
		return getSafeStatementContainer().newReturn(expr);
	}
	public ASReturnStatement newReturn() {
		return getSafeStatementContainer().newReturn();
	}

	public ASBreakStatement newBreak() {
		return getSafeStatementContainer().newBreak();
	}

	public ASTryStatement newTryCatch(String var, String type) {
		return getSafeStatementContainer().newTryCatch(var, type);
	}

	public ASTryStatement newTryFinally() {
		return getSafeStatementContainer().newTryFinally();
	}

	public ASContinueStatement newContinue() {
		return getSafeStatementContainer().newContinue();
	}

	public ASThrowStatement newThrow(Expression t) {
		return getSafeStatementContainer().newThrow(t);
	}
	
	public ASDefaultXMLNamespaceStatement newDefaultXMLNamespace(String namespace) {
		return getSafeStatementContainer().newDefaultXMLNamespace(namespace);
	}

	public boolean containsCode() {
		return getSafeStatementContainer().containsCode();
	}
	
	public List getStatementList() {
		return getSafeStatementContainer().getStatementList();
	}
}