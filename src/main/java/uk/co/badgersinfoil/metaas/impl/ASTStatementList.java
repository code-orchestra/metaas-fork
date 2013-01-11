/*
 * ASTStatementList.java
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

package uk.co.badgersinfoil.metaas.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASBlock;
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
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

// TODO: make the ASBlock implementation independent of the StatementContainer implementation

/**
 * Implements the behaviour of StatementContainer.  May be managing statements
 * for either be a block, or a switch-statement label.
 */
public class ASTStatementList extends ASTScriptElement implements ASBlock {

    public ASTStatementList(LinkedListTree ast) {
        super(ast);
        assertBlockAST(ast);
    }

    public String getLabel() {
        LinkedListTree labelAST = ASTUtils.findChildByType(ast, AS3Parser.LABEL);
        if (labelAST != null) {
            LinkedListTree labelValueAST = labelAST.getFirstChild();
            if (labelValueAST != null) {
                return labelValueAST.getText();
            }
        }
        return null;
    }

    private void assertBlockAST(LinkedListTree ast) {
        switch (ast.getType()) {
            case AS3Parser.BLOCK:
            case AS3Parser.SWITCH_STATEMENT_LIST:
                break;
            default:
                throw new IllegalArgumentException("statement is not a block: " + ASTUtils.tokenName(ast));
        }
    }

    public Statement addStmt(String statement) {
        LinkedListTree stmt = AS3FragmentParser.parseStatement(statement);
        stmt.getStopToken().setNext(null);
        addStatement(stmt);
        return StatementBuilder.build(stmt);
    }

    public ASExpressionStatement newExprStmt(String expr) {
        LinkedListTree stmt = AS3FragmentParser.parseExprStatement(expr);
        addStatement(stmt);
        return new ASTASExpressionStatement(stmt);
    }

    public ASExpressionStatement newExprStmt(Expression expr) {
        LinkedListTree stmt = ASTBuilder.newExprStmt(ast(expr));
        addStatement(stmt);
        return new ASTASExpressionStatement(stmt);
    }

    public void addComment(String text) {
        LinkedListToken comment = TokenBuilder.newSLComment("//" + text);
        String indent = findIndentForComment();
        ast.appendToken(TokenBuilder.newNewline());
        ast.appendToken(TokenBuilder.newWhiteSpace(indent));
        ast.appendToken(comment);
    }

    private String findIndentForComment() {
        LinkedListTree last = ast.getLastChild();
        String indent;
        if (last == null) {
            indent = "\t" + ASTUtils.findIndent(ast);
        } else {
            indent = ASTUtils.findIndent(last);
        }
        return indent;
    }

    public ASIfStatement newIf(String condition) {
        LinkedListTree ifStmt = ASTBuilder.newIf(condition);
        addStatement(ifStmt);
        return new ASTASIfStatement(ifStmt);
    }

    public ASIfStatement newIf(Expression condition) {
        LinkedListTree ifStmt = ASTBuilder.newIf(ast(condition));
        addStatement(ifStmt);
        return new ASTASIfStatement(ifStmt);
    }

    public ASForStatement newFor(String init, String condition, String iterate) {
        LinkedListTree forStmt = ASTBuilder.newFor(init, condition, iterate);
        appendBlock(forStmt);
        addStatement(forStmt);
        return new ASTASForStatement(forStmt);
    }

    public ASForStatement newFor(Expression init, Expression condition, Expression iterate) {
        LinkedListTree forStmt = ASTBuilder.newFor(ast(init),
                ast(condition),
                ast(iterate));
        appendBlock(forStmt);
        addStatement(forStmt);
        return new ASTASForStatement(forStmt);
    }

    public ASForInStatement newForIn(String declaration, String expression) {
        LinkedListTree forStmt = ASTBuilder.newForIn(declaration, expression);
        appendBlock(forStmt);
        addStatement(forStmt);
        return new ASTASForInStatement(forStmt);
    }

    public ASForInStatement newForIn(Expression declaration, Expression expression) {
        LinkedListTree forStmt = ASTBuilder.newForIn(ast(declaration),
                ast(expression));
        appendBlock(forStmt);
        addStatement(forStmt);
        return new ASTASForInStatement(forStmt);
    }

    public ASForEachInStatement newForEachIn(String declaration, String expression) {
        LinkedListTree forStmt = ASTBuilder.newForEachIn(declaration, expression);
        appendBlock(forStmt);
        addStatement(forStmt);
        return new ASTASForEachInStatement(forStmt);
    }

    public ASForEachInStatement newForEachIn(Expression declaration, Expression expression) {
        LinkedListTree forStmt = ASTBuilder.newForEachIn(ast(declaration),
                ast(expression));
        appendBlock(forStmt);
        addStatement(forStmt);
        return new ASTASForEachInStatement(forStmt);
    }

    public ASWhileStatement newWhile(String condition) {
        LinkedListTree whileStmt = ASTBuilder.newWhile(condition);
        appendBlock(whileStmt);
        addStatement(whileStmt);
        return new ASTASWhileStatement(whileStmt);
    }

    public ASWhileStatement newWhile(Expression condition) {
        LinkedListTree whileStmt = ASTBuilder.newWhile(ast(condition));
        appendBlock(whileStmt);
        addStatement(whileStmt);
        return new ASTASWhileStatement(whileStmt);
    }

    public ASDoWhileStatement newDoWhile(String condition) {
        LinkedListTree doWhileStmt = ASTBuilder.newDoWhile(condition);
        addStatement(doWhileStmt);
        return new ASTASDoWhileStatement(doWhileStmt);
    }

    public ASDoWhileStatement newDoWhile(Expression condition) {
        LinkedListTree doWhileStmt = ASTBuilder.newDoWhile(ast(condition));
        addStatement(doWhileStmt);
        return new ASTASDoWhileStatement(doWhileStmt);
    }

    public ASSwitchStatement newSwitch(String condition) {
        LinkedListTree switchStmt = ASTBuilder.newSwitch(condition);
        addStatement(switchStmt);
        return new ASTASSwitchStatement(switchStmt);
    }

    public ASSwitchStatement newSwitch(Expression condition) {
        LinkedListTree switchStmt = ASTBuilder.newSwitch(ast(condition));
        addStatement(switchStmt);
        return new ASTASSwitchStatement(switchStmt);
    }

    public ASWithStatement newWith(String expr) {
        LinkedListTree withStmt = ASTBuilder.newWith(expr);
        appendBlock(withStmt);
        addStatement(withStmt);
        return new ASTASWithStatement(withStmt);
    }

    public ASWithStatement newWith(Expression expr) {
        LinkedListTree withStmt = ASTBuilder.newWith(ast(expr));
        appendBlock(withStmt);
        addStatement(withStmt);
        return new ASTASWithStatement(withStmt);
    }

    public ASDeclarationStatement newDeclaration(String assignment) {
        LinkedListTree declStmt = ASTBuilder.newDeclaration(assignment);
        addStatement(declStmt);
        return new ASTASDeclarationStatement(declStmt);
    }

    public ASDeclarationStatement newDeclaration(Expression assignment) {
        LinkedListTree declStmt = ASTBuilder.newDeclaration(ast(assignment));
        addStatement(declStmt);
        return new ASTASDeclarationStatement(declStmt);
    }

    public ASReturnStatement newReturn(String expr) {
        LinkedListTree returnStmt = ASTBuilder.newReturn(expr);
        addStatement(returnStmt);
        return new ASTASReturnStatement(returnStmt);
    }

    public ASReturnStatement newReturn(Expression expr) {
        LinkedListTree returnStmt = ASTBuilder.newReturn(ast(expr));
        addStatement(returnStmt);
        return new ASTASReturnStatement(returnStmt);
    }

    public ASReturnStatement newReturn() {
        LinkedListTree returnStmt = ASTBuilder.newReturn((String) null);
        addStatement(returnStmt);
        return new ASTASReturnStatement(returnStmt);
    }

    public ASSuperStatement newSuper(List args) {
        LinkedListTree superStmt = ASTBuilder.newSuperStatement(args);
        addStatement(superStmt);
        return new ASTASSuperStatement(superStmt);
    }

    public ASBreakStatement newBreak() {
        LinkedListTree breakStmt = ASTBuilder.newBreakStatement();
        addStatement(breakStmt);
        return new ASTASBreakStatement(breakStmt);
    }

    public ASTryStatement newTryCatch(String var, String type) {
        LinkedListTree tryStmt = ASTBuilder.newTryStatement();
        tryStmt.appendToken(TokenBuilder.newSpace());
        tryStmt.addChildWithTokens(ASTBuilder.newCatchClause(var, type));
        addStatement(tryStmt);
        return new ASTASTryStatement(tryStmt);
    }

    public ASTryStatement newTryFinally() {
        LinkedListTree tryStmt = ASTBuilder.newTryStatement();
        tryStmt.appendToken(TokenBuilder.newSpace());
        tryStmt.addChildWithTokens(ASTBuilder.newFinallyClause());
        addStatement(tryStmt);
        return new ASTASTryStatement(tryStmt);
    }

    public ASContinueStatement newContinue() {
        LinkedListTree breakStmt = ASTBuilder.newContinueStatement();
        addStatement(breakStmt);
        return new ASTASContinueStatement(breakStmt);
    }

    public ASThrowStatement newThrow(Expression t) {
        LinkedListTree throwStmt = ASTBuilder.newThrowStatement(ast(t));
        addStatement(throwStmt);
        return new ASTASThrowStatement(throwStmt);
    }

    public ASDefaultXMLNamespaceStatement newDefaultXMLNamespace(String namespace) {
        LinkedListTree nsStmt = ASTBuilder.newDefaultXMLNamespace(ASTBuilder.newString(namespace));
        addStatement(nsStmt);
        return new ASTASDefaultXMLNamespaceStatement(nsStmt);
    }

    private void addStatement(LinkedListTree stmt) {
        ASTUtils.addChildWithIndentation(ast, stmt);
    }

    public boolean containsCode() {
        return ast.getFirstChild() != null;
    }

    public LinkedListTree getAST() {
        return ast;
    }

    /**
     * Appends a block node as a child of the given node (preceeding it
     * with a space token) and returns a reference to the new block.
     * <p/>
     * NB doesn't do indentation properly, so add the block to the statement
     * before the statement is added to its container.
     */
    private static LinkedListTree appendBlock(LinkedListTree ast) {
        ast.appendToken(TokenBuilder.newSpace());
        LinkedListTree block = ASTBuilder.newBlock();
        ast.addChildWithTokens(block);
        return block;
    }

    /**
     * Returns the first BLOCK child of the given node
     */
    private static LinkedListTree findBlock(LinkedListTree ast) {
        return (LinkedListTree) ast.getFirstChildWithType(AS3Parser.BLOCK);
    }

    public List getStatementList() {
        List result = new ArrayList();
        ASTIterator i = new ASTIterator(ast);
        while (i.hasNext()) {
            final LinkedListTree listTree = i.next();
            if (listTree.getType() == AS3Parser.LABEL) {
                continue;
            }
            List<Comment> commentList = CommentUtils.getCommentBefore(listTree);
            if (commentList != null) {
                for (Comment aCommentList : commentList) {
                    result.add(new ASTASRemarkStatement(aCommentList));
                }
            }
            result.add(StatementBuilder.build(listTree));
            if (!i.hasNext()) {
                List<Comment> singleComment = CommentUtils.getCommentAfter(listTree);
                boolean selfCommentExists = !(singleComment == null || singleComment.isEmpty());
                commentList = CommentUtils.getCommentAfter(listTree, false);
                if (commentList != null) {
                    if (selfCommentExists) {
                        commentList.remove(0);
                    }
                    for (Comment aCommentList : commentList) {
                        result.add(new ASTASRemarkStatement(aCommentList));
                    }
                }
            }
        }
        if (result.isEmpty()) { // Empty block still can have comments inside
            List<Comment> commentsAfterToken = CommentUtils.getCommentsAfterToken(ast.getStartToken(), false);
            if (commentsAfterToken != null) {
                for (Comment aCommentList : commentsAfterToken) {
                    result.add(new ASTASRemarkStatement(aCommentList));
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

    public List<Comment> getCommentsAfter() {
        return CommentUtils.getCommentAfter(this.getAST());
    }

    public int getSpacerSize() {
        return SpacerUtil.calculateSpacerSize(ast);
    }
}