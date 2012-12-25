package uk.co.badgersinfoil.metaas.impl;

import org.antlr.runtime.Token;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ASTCommentList extends ASTStatementList {

    private final static String ERROR = "Should not be called";

    private final static LinkedListTree fakeAST = new LinkedListTree() {{ token = new LinkedListToken(AS3Parser.BLOCK, ""); }};

    private List<Comment> comments = new ArrayList<Comment>();

    public ASTCommentList(LinkedListTree ast) {
        super(fakeAST);
    }

    public ASTCommentList() {
        this(fakeAST); // ASTCommentList has no AST
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public Statement addStmt(String statement) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASExpressionStatement newExprStmt(String expr) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASExpressionStatement newExprStmt(Expression expr) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public void addComment(String text) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASIfStatement newIf(String condition) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASIfStatement newIf(Expression condition) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASForStatement newFor(String init, String condition, String iterate) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASForStatement newFor(Expression init, Expression condition, Expression iterate) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASForInStatement newForIn(String declaration, String expression) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASForInStatement newForIn(Expression declaration, Expression expression) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASForEachInStatement newForEachIn(String declaration, String expression) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASForEachInStatement newForEachIn(Expression declaration, Expression expression) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASWhileStatement newWhile(String condition) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASWhileStatement newWhile(Expression condition) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASDoWhileStatement newDoWhile(String condition) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASDoWhileStatement newDoWhile(Expression condition) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASSwitchStatement newSwitch(String condition) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASSwitchStatement newSwitch(Expression condition) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASWithStatement newWith(String expr) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASWithStatement newWith(Expression expr) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASDeclarationStatement newDeclaration(String assignment) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASDeclarationStatement newDeclaration(Expression assignment) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASReturnStatement newReturn(String expr) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASReturnStatement newReturn(Expression expr) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASReturnStatement newReturn() {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASSuperStatement newSuper(List args) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASBreakStatement newBreak() {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASTryStatement newTryCatch(String var, String type) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASTryStatement newTryFinally() {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASContinueStatement newContinue() {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASThrowStatement newThrow(Expression t) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public ASDefaultXMLNamespaceStatement newDefaultXMLNamespace(String namespace) {
        throw new RuntimeException(ERROR);
    }

    @Override
    public boolean containsCode() {
        throw new RuntimeException(ERROR);
    }

    @Override
    public LinkedListTree getAST() {
        throw new RuntimeException(ERROR);
    }

    @Override
    public List getStatementList() {
        List result = new ArrayList();
        for (Comment comment : comments) {
            result.add(new ASTASRemarkStatement(comment));
        }
        return result;
    }

    @Override
    public List<Comment> getCommentsAfter() {
        throw new RuntimeException(ERROR);
    }

    @Override
    public int getSpacerSize() {
        throw new RuntimeException(ERROR);
    }

    @Override
    public String toString() {
        return "Comments";
    }

    public List<Comment> getComments() {
        return comments;
    }
}
