/*
 * ActionScriptVisitor.java
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

package uk.co.badgersinfoil.metaas.visitor;

import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.dom.ASObjectLiteral.Field;

public interface ActionScriptVisitor {
	public void visitArg(ASArg arg);

	public void visitArrayAccessExpression(ASArrayAccessExpression expr);

    public void visitParenthesizedExpression(ASParenthesizedExpression expr);

	public void visitArrayLiteral(ASArrayLiteral lit);

    public void visitFloatLiteral(ASFloatLiteral lit);

    public void visitSuperExpression(ASSuperExpression superExpression);

    public void visitHexLiteral(ASHexLiteral hexLiteral);

    public void visitOctalLiteral(ASOctalLiteral octalLiteral);

    public void visitTypeOfExpression(ASTypeOfExpression typeOfExpression);

	public void visitAssignmentExpression(ASAssignmentExpression expr);

	public void visitBinaryExpression(ASBinaryExpression expr);

	public void visitBlockStatement(ASBlock stmt);

    public void visitConditionalCompilationBlockStatement(ASConditionalCompilationBlock stmt);

	public void visitBooleanLiteral(ASBooleanLiteral lit);

	public void visitBreakStatement(ASBreakStatement stmt);

	public void visitCatchClause(ASCatchClause catchClause);

	public void visitClassType(ASClassType type);

	public void visitCompilationUnit(ASCompilationUnit unit);

	public void visitConditionalExpression(ASConditionalExpression expr);

	public void visitContinueStatement(ASContinueStatement stmt);

    public void visitLocalFunction(ASLocalFunction stmt);

	public void visitDeclarationStatement(ASDeclarationStatement stmt);

	public void visitDefaultXMLNamespaceStatement(ASDefaultXMLNamespaceStatement stmt);

	public void visitDoWhileStatement(ASDoWhileStatement stmt);

	public void visitDescendantExpression(ASDescendantExpression expr);

	public void visitExpressionAttribute(ASExpressionAttribute expr);

	public void visitExpressionStatement(ASExpressionStatement stmt);

	public void visitField(ASField field);

    public void visitDeleteExpression(ASDeleteExpression deleteExpression);

	public void visitFieldAccessExpression(ASFieldAccessExpression expr);

	public void visitFilterExpression(ASFilterExpression expr);

	public void visitFinallyClause(ASFinallyClause fin);

	public void visitForEachInStatement(ASForEachInStatement stmt);

	public void visitForInStatement(ASForInStatement stmt);

	public void visitForStatement(ASForStatement stmt);

	public void visitFunctionExpression(ASFunctionExpression e);

	public void visitIfStatement(ASIfStatement stmt);

	public void visitIntegerLiteral(ASIntegerLiteral lit);

	public void visitInterfaceType(ASInterfaceType type);

	public void visitInvocationExpression(ASInvocationExpression expr);

	public void visitMetaTag(ASMetaTag tag);

	public void visitMethod(ASMethod method);

	public void visitNewExpression(ASNewExpression expr);

	public void visitNullLiteral(ASNullLiteral lit);

	public void visitObjectField(ASObjectLiteral.Field field);

	public void visitObjectLiteral(ASObjectLiteral lit);

	public void visitPackage(ASPackage pkg);

	public void visitPostfixExpression(ASPostfixExpression expr);

	public void visitNotExpression(ASNotExpression expr);

	public void visitPrefixExpression(ASPrefixExpression expr);

	public void visitPropertyAttribute(ASPropertyAttribute expr);

	public void visitRegexpLiteral(ASRegexpLiteral e);

	public void visitReturnStatement(ASReturnStatement stmt);

	public void visitSimpleNameExpression(ASSimpleNameExpression e);

	public void visitStringLiteral(ASStringLiteral lit);

	public void visitStarAttribute(ASStarAttribute expr);

	public void visitSuperStatement(ASSuperStatement stmt);

	public void visitSwitchCase(ASSwitchCase lab);

	public void visitSwitchDefault(ASSwitchDefault lab);

	public void visitSwitchStatement(ASSwitchStatement stmt);

	public void visitThrowStatement(ASThrowStatement stmt);

	public void visitTryStatement(ASTryStatement stmt);

	public void visitUndefinedLiteral(ASUndefinedLiteral lit);

	public void visitVarDeclarationFragment(ASVarDeclarationFragment var);

    public void visitVectorLiteral(ASVectorLiteral var);

	public void visitWhileStatement(ASWhileStatement stmt);

	public void visitWithStatement(ASWithStatement stmt);

	public void visitXMLLiteral(ASXMLLiteral lit);

	public void visitImportStatement(ASImportStatement stmt);

    public void visitNamespaceStatement(ASNamespaceStatement stmt);

	public static class Null implements ActionScriptVisitor {
        public void visitTypeOfExpression(ASTypeOfExpression typeOfExpression) {
        }
        public void visitHexLiteral(ASHexLiteral hexLiteral) {
        }
        public void visitOctalLiteral(ASOctalLiteral octalLiteral) {
        }
        public void visitFloatLiteral(ASFloatLiteral lit) {
        }
        public void visitSuperExpression(ASSuperExpression superExpression) {
        }
        public void visitArg(ASArg arg) {
		}
        public void visitDeleteExpression(ASDeleteExpression deleteExpression) {
        }
        public void visitArrayAccessExpression(ASArrayAccessExpression expr) {
		}
		public void visitArrayLiteral(ASArrayLiteral lit) {
		}
        public void visitParenthesizedExpression(ASParenthesizedExpression expr) {
        }
        public void visitAssignmentExpression(ASAssignmentExpression expr) {
		}
		public void visitBinaryExpression(ASBinaryExpression expr) {
		}
		public void visitBlockStatement(ASBlock stmt) {
		}
        public void visitConditionalCompilationBlockStatement(ASConditionalCompilationBlock e) {
        }
		public void visitBooleanLiteral(ASBooleanLiteral lit) {
		}
		public void visitBreakStatement(ASBreakStatement stmt) {
		}
		public void visitCatchClause(ASCatchClause catchClause) {
		}
		public void visitClassType(ASClassType type) {
		}
		public void visitCompilationUnit(ASCompilationUnit unit) {
		}
		public void visitConditionalExpression(ASConditionalExpression expr) {
		}
		public void visitContinueStatement(ASContinueStatement stmt) {
		}
        public void visitLocalFunction(ASLocalFunction stmt) {
        }
        public void visitDeclarationStatement(ASDeclarationStatement stmt) {
		}
		public void visitDefaultXMLNamespaceStatement(ASDefaultXMLNamespaceStatement stmt) {
		}
		public void visitDoWhileStatement(ASDoWhileStatement stmt) {
		}
		public void visitDescendantExpression(ASDescendantExpression expr) {
		}
		public void visitExpressionAttribute(ASExpressionAttribute expr) {
		}
		public void visitExpressionStatement(ASExpressionStatement stmt) {
		}
		public void visitField(ASField field) {
		}
		public void visitFieldAccessExpression(ASFieldAccessExpression expr) {
		}
		public void visitFilterExpression(ASFilterExpression expr) {
		}
		public void visitFinallyClause(ASFinallyClause fin) {
		}
		public void visitForEachInStatement(ASForEachInStatement stmt) {
		}
		public void visitForInStatement(ASForInStatement stmt) {
		}
		public void visitForStatement(ASForStatement stmt) {
		}
		public void visitFunctionExpression(ASFunctionExpression e) {
		}
		public void visitIfStatement(ASIfStatement stmt) {
		}
		public void visitIntegerLiteral(ASIntegerLiteral lit) {
		}
		public void visitInterfaceType(ASInterfaceType type) {
		}
		public void visitInvocationExpression(ASInvocationExpression expr) {
		}
		public void visitMetaTag(ASMetaTag tag) {
		}
		public void visitMethod(ASMethod method) {
		}
		public void visitNewExpression(ASNewExpression expr) {
		}
		public void visitNullLiteral(ASNullLiteral lit) {
		}
		public void visitObjectField(Field field) {
		}
		public void visitObjectLiteral(ASObjectLiteral lit) {
		}
		public void visitPackage(ASPackage pkg) {
		}
		public void visitPostfixExpression(ASPostfixExpression expr) {
		}
        public void visitNotExpression(ASNotExpression expr) {
        }
        public void visitPrefixExpression(ASPrefixExpression expr) {
		}
		public void visitPropertyAttribute(ASPropertyAttribute expr) {
		}
		public void visitRegexpLiteral(ASRegexpLiteral e) {
		}
		public void visitReturnStatement(ASReturnStatement stmt) {
		}
		public void visitSimpleNameExpression(ASSimpleNameExpression e) {
		}
		public void visitStringLiteral(ASStringLiteral lit) {
		}
		public void visitStarAttribute(ASStarAttribute expr) {
		}
		public void visitSuperStatement(ASSuperStatement stmt) {
		}
		public void visitSwitchCase(ASSwitchCase lab) {
		}
		public void visitSwitchDefault(ASSwitchDefault lab) {
		}
		public void visitSwitchStatement(ASSwitchStatement stmt) {
		}
		public void visitThrowStatement(ASThrowStatement stmt) {
		}
		public void visitTryStatement(ASTryStatement stmt) {
		}
		public void visitUndefinedLiteral(ASUndefinedLiteral lit) {
		}
		public void visitVarDeclarationFragment(ASVarDeclarationFragment var) {
		}
        public void visitVectorLiteral(ASVectorLiteral var) {
        }
        public void visitWhileStatement(ASWhileStatement stmt) {
		}
		public void visitWithStatement(ASWithStatement stmt) {
		}
		public void visitXMLLiteral(ASXMLLiteral lit) {
		}
        public void visitImportStatement(ASImportStatement stmt) {
        }
        public void visitNamespaceStatement(ASNamespaceStatement stmt) {
        }
    }
}
