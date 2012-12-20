/*
 * ScriptElementSwitch.java
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

public class ScriptElementSwitch implements ScriptElementStrategy {
	private ActionScriptVisitor v;

	public ScriptElementSwitch(ActionScriptVisitor v) {
		this.v = v;
	}

	public void handle(ScriptElement e) {
		if (e instanceof ASArrayAccessExpression) {
			v.visitArrayAccessExpression((ASArrayAccessExpression)e);
		} else if (e instanceof ASArrayLiteral) {
			v.visitArrayLiteral((ASArrayLiteral)e);
        } else if (e instanceof ASFloatLiteral) {
			v.visitFloatLiteral((ASFloatLiteral)e);
		} else if (e instanceof ASArg) {
			v.visitArg((ASArg)e);
		} else if (e instanceof ASAssignmentExpression) {
			v.visitAssignmentExpression((ASAssignmentExpression)e);
		} else if (e instanceof ASBinaryExpression) {
			v.visitBinaryExpression((ASBinaryExpression)e);
		} else if (e instanceof ASBooleanLiteral) {
			v.visitBooleanLiteral((ASBooleanLiteral)e);
		} else if (e instanceof ASConditionalCompilationBlock) {
			v.visitConditionalCompilationBlockStatement((ASConditionalCompilationBlock)e);
		} else if (e instanceof ASBlock) {
			v.visitBlockStatement((ASBlock)e);
		} else if (e instanceof ASBreakStatement) {
			v.visitBreakStatement((ASBreakStatement)e);
		} else if (e instanceof ASCatchClause) {
			v.visitCatchClause((ASCatchClause)e);
		} else if (e instanceof ASConditionalExpression) {
			v.visitConditionalExpression((ASConditionalExpression)e);
		} else if (e instanceof ASContinueStatement) {
			v.visitContinueStatement((ASContinueStatement)e);
		} else if (e instanceof ASCompilationUnit) {
			v.visitCompilationUnit((ASCompilationUnit)e);
		} else if (e instanceof ASClassType) {
			v.visitClassType((ASClassType)e);
		} else if (e instanceof ASLocalFunction) {
			v.visitLocalFunction((ASLocalFunction)e);
		} else if (e instanceof ASDeclarationStatement) {
			v.visitDeclarationStatement((ASDeclarationStatement)e);
		} else if (e instanceof ASDefaultXMLNamespaceStatement) {
			v.visitDefaultXMLNamespaceStatement((ASDefaultXMLNamespaceStatement)e);
		} else if (e instanceof ASDoWhileStatement) {
			v.visitDoWhileStatement((ASDoWhileStatement)e);
		} else if (e instanceof ASDescendantExpression) {
			v.visitDescendantExpression((ASDescendantExpression)e);
		} else if (e instanceof ASExpressionAttribute) {
			v.visitExpressionAttribute((ASExpressionAttribute)e);
		} else if (e instanceof ASExpressionStatement) {
			v.visitExpressionStatement((ASExpressionStatement)e);
		} else if (e instanceof ASFieldAccessExpression) {
			v.visitFieldAccessExpression((ASFieldAccessExpression)e);
		} else if (e instanceof ASFilterExpression) {
			v.visitFilterExpression((ASFilterExpression)e);
		} else if (e instanceof ASFinallyClause) {
			v.visitFinallyClause((ASFinallyClause)e);
		} else if (e instanceof ASForEachInStatement) {
			v.visitForEachInStatement((ASForEachInStatement)e);
		} else if (e instanceof ASForInStatement) {
			v.visitForInStatement((ASForInStatement)e);
		} else if (e instanceof ASForStatement) {
			v.visitForStatement((ASForStatement)e);
		} else if (e instanceof ASField) {
			v.visitField((ASField)e);
        } else if (e instanceof ASDeleteExpression) {
            v.visitDeleteExpression((ASDeleteExpression) e);
		} else if (e instanceof ASFunctionExpression) {
			v.visitFunctionExpression((ASFunctionExpression)e);
		} else if (e instanceof ASInvocationExpression) {
			v.visitInvocationExpression((ASInvocationExpression)e);
		} else if (e instanceof ASIfStatement) {
			v.visitIfStatement((ASIfStatement)e);
		} else if (e instanceof ASIntegerLiteral) {
			v.visitIntegerLiteral((ASIntegerLiteral)e);
		} else if (e instanceof ASInterfaceType) {
			v.visitInterfaceType((ASInterfaceType)e);
		} else if (e instanceof ASMetaTag) {
			v.visitMetaTag((ASMetaTag)e);
		} else if (e instanceof ASMethod) {
			v.visitMethod((ASMethod)e);
		} else if (e instanceof ASNewExpression) {
			v.visitNewExpression((ASNewExpression)e);
		} else if (e instanceof ASNullLiteral) {
			v.visitNullLiteral((ASNullLiteral)e);
		} else if (e instanceof ASObjectLiteral) {
			v.visitObjectLiteral((ASObjectLiteral)e);
		} else if (e instanceof ASObjectLiteral.Field) {
			v.visitObjectField((ASObjectLiteral.Field)e);
		} else if (e instanceof ASPackage) {
			v.visitPackage((ASPackage)e);
		} else if (e instanceof ASPostfixExpression) {
			v.visitPostfixExpression((ASPostfixExpression)e);
		} else if (e instanceof ASNotExpression) {
			v.visitNotExpression((ASNotExpression)e);
		} else if (e instanceof ASPrefixExpression) {
			v.visitPrefixExpression((ASPrefixExpression)e);
		} else if (e instanceof ASPropertyAttribute) {
			v.visitPropertyAttribute((ASPropertyAttribute)e);
		} else if (e instanceof ASRegexpLiteral) {
			v.visitRegexpLiteral((ASRegexpLiteral)e);
		} else if (e instanceof ASReturnStatement) {
			v.visitReturnStatement((ASReturnStatement)e);
		} else if (e instanceof ASSimpleNameExpression) {
			v.visitSimpleNameExpression((ASSimpleNameExpression)e);
		} else if (e instanceof ASStarAttribute) {
			v.visitStarAttribute((ASStarAttribute)e);
		} else if (e instanceof ASStringLiteral) {
			v.visitStringLiteral((ASStringLiteral)e);
		} else if (e instanceof ASSwitchCase) {
			v.visitSwitchCase((ASSwitchCase)e);
		} else if (e instanceof ASSwitchDefault) {
			v.visitSwitchDefault((ASSwitchDefault)e);
		} else if (e instanceof ASSwitchStatement) {
			v.visitSwitchStatement((ASSwitchStatement)e);
		} else if (e instanceof ASSuperStatement) {
			v.visitSuperStatement((ASSuperStatement)e);
		} else if (e instanceof ASThrowStatement) {
			v.visitThrowStatement((ASThrowStatement)e);
		} else if (e instanceof ASTryStatement) {
			v.visitTryStatement((ASTryStatement)e);
		} else if (e instanceof ASUndefinedLiteral) {
			v.visitUndefinedLiteral((ASUndefinedLiteral)e);
		} else if (e instanceof ASVarDeclarationFragment) {
			v.visitVarDeclarationFragment((ASVarDeclarationFragment)e);
		} else if (e instanceof ASWhileStatement) {
			v.visitWhileStatement((ASWhileStatement)e);
		} else if (e instanceof ASWithStatement) {
			v.visitWithStatement((ASWithStatement)e);
        } else if (e instanceof ASParenthesizedExpression) {
            v.visitParenthesizedExpression((ASParenthesizedExpression) e);
		} else if (e instanceof ASXMLLiteral) {
			v.visitXMLLiteral((ASXMLLiteral)e);
        } else if (e instanceof ASSuperExpression) {
            v.visitSuperExpression((ASSuperExpression) e);
        } else if (e instanceof ASHexLiteral) {
            v.visitHexLiteral((ASHexLiteral) e);
        } else if (e instanceof ASOctalLiteral) {
            v.visitOctalLiteral((ASOctalLiteral) e);
        } else if (e instanceof ASTypeOfExpression) {
            v.visitTypeOfExpression((ASTypeOfExpression) e);
        } else if (e instanceof ASVectorLiteral) {
            v.visitVectorLiteral((ASVectorLiteral) e);
        } else if (e instanceof ASImportStatement) {
            v.visitImportStatement((ASImportStatement) e);
        } else if (e instanceof ASNamespaceStatement) {
            v.visitNamespaceStatement((ASNamespaceStatement) e);
        } else if (e instanceof ASInfinityLiteral) {
            // Do nothing
        } else if (e instanceof ASNanLiteral) {
            // Do nothing
        } else if (e instanceof ASVectorExpression) {
            // Do nothing
        } else if (e instanceof ASUseNamespaceStatement) {
            // Do nothing
        } else if (e instanceof ExpressionList) {
            // Do nothing
        } else if (e instanceof ASRemarkStatement) {
            // Do nothing
        } else {
			throw new IllegalArgumentException("ScriptElementSwitch: unhandled ScriptElement "+e.getClass().getName());
		}
	}
}
