/*
 * ASTASForStatement.java
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

import org.antlr.runtime.tree.Tree;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.SyntaxException;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.ArrayList;
import java.util.List;


public class ASTASForStatement extends ContainerDelegate implements ASForStatement {

    private static final int INDEX_INIT = 0;
    private static final int INDEX_CONDITION = 1;
    private static final int INDEX_UPDATE = 2;
    private static final int INDEX_STATEMENT = 3;

    public ASTASForStatement(LinkedListTree ast) {
        super(ast);
    }

    @Override
    public List getStatementList() {
        try {
            return super.getStatementList();
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            if (message != null && message.startsWith("statement is not a block")) {
                throw new SyntaxException("Loop body is not a block");
            }
            throw e;
        }
    }

    public Statement getBody() {
        return StatementBuilder.build(stmt());
    }

    private LinkedListTree stmt() {
        return getChild(INDEX_STATEMENT);
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

    public String getInitString() {
        if (!hasInit()) {
            return null;
        }
        return ASTUtils.stringifyNode(findInit());
    }

    public ScriptElement getInit() {
        if (!hasInit()) {
            return null;
        }

        LinkedListTree initListTree = findInit();
        LinkedListTree initFirstAST = initListTree.getFirstChild();
        if (initFirstAST.getType() == AS3Parser.VAR || initFirstAST.getType() == AS3Parser.CONST) {
            return new ASTASDeclarationStatement(initFirstAST);
        }

        ExpressionList expressionList = new ExpressionListImpl();
        for (int i = 0; i < initListTree.getChildCount(); i++) {
            expressionList.addExpression(ExpressionBuilder.build((LinkedListTree) initListTree.getChild(i)));     
        }
        return expressionList;
    }

    public String getConditionString() {
        if (!hasCondition()) {
            return null;
        }
        return ASTUtils.stringifyNode(findCondition().getFirstChild());
    }

    public List<Expression> getConditions() {
        List<Expression> conditions = new ArrayList<Expression>();

        LinkedListTree conditionsTree = findCondition();
        if (conditionsTree != null) {
            for (int i = 0; i < conditionsTree.getChildCount(); i++) {
                Tree child = conditionsTree.getChild(i);
                if (child instanceof LinkedListTree) {
                    Expression condition = ExpressionBuilder.build((LinkedListTree) child);
                    if (condition != null) {
                        conditions.add(condition);
                    }
                }
            }
        }

        return conditions;
    }

    @Deprecated
    public Expression getCondition() {
        if (!hasCondition()) {
            return null;
        }
        return ExpressionBuilder.build(findCondition().getFirstChild());
    }

    public String getUpdateString() {
        if (!hasUpdate()) {
            return null;
        }
        return ASTUtils.stringifyNode(findUpdate().getFirstChild());
    }

    public List<Expression> getUpdates() {
        List<Expression> updates = new ArrayList<Expression>();

        LinkedListTree updatesTree = findUpdate();
        if (updatesTree != null) {
            for (int i = 0; i < updatesTree.getChildCount(); i++) {
                Tree child = updatesTree.getChild(i);
                if (child instanceof LinkedListTree) {
                    Expression update = ExpressionBuilder.build((LinkedListTree) child);
                    if (update != null) {
                        updates.add(update);
                    }
                }
            }
        }

        return updates;
    }

    public Expression getUpdate() {
        if (!hasUpdate()) {
            return null;
        }
        return ExpressionBuilder.build(findUpdate().getFirstChild());
    }

    private boolean hasCondition() {
        return findCondition().getChildCount() > 0;
    }

    private boolean hasInit() {
        return findInit().getChildCount() > 0;
    }

    private boolean hasUpdate() {
        return findUpdate().getChildCount() > 0;
    }

    private LinkedListTree getChild(int index) {
        return (LinkedListTree)ast.getChild(index);
    }

    protected StatementContainer getStatementContainer() {
        LinkedListTree listTree = getChild(INDEX_STATEMENT);
        if (listTree != null) {
            return new ASTStatementList(listTree);
        }
        return null;
    }

    private LinkedListTree findInit() {
        return getChild(INDEX_INIT);
    }

    private LinkedListTree findCondition() {
        return getChild(INDEX_CONDITION);
    }

    private LinkedListTree findUpdate() {
        return getChild(INDEX_UPDATE);
    }

    public void setCondition(String expr) {
        if (expr == null) {
            deleteAnyChild(findCondition());
        } else {
            LinkedListTree cond = AS3FragmentParser.parseForCond(expr);
            ast.setChildWithTokens(INDEX_CONDITION, cond);
        }
    }

    public void setCondition(Expression expr) {
        LinkedListTree cond = findCondition();
        if (expr == null) {
            deleteAnyChild(cond);
        } else {
            setFirstChild(cond, ast(expr));
        }
    }

    public void setInit(String expr) {
        if (expr == null) {
            deleteAnyChild(findInit());
        } else {
            LinkedListTree init = AS3FragmentParser.parseForInit(expr);
            ast.setChildWithTokens(INDEX_INIT, init);
        }
    }

    public void setUpdate(String expr) {
        if (expr == null) {
            deleteAnyChild(findUpdate());
        } else {
            LinkedListTree update = AS3FragmentParser.parseForIter(expr);
            ast.setChildWithTokens(INDEX_UPDATE, update);
        }
    }

    public void setUpdate(Expression expr) {
        if (expr == null) {
            deleteAnyChild(findUpdate());
        } else {
            setFirstChild(findUpdate(), ast(expr));
        }
    }

    private void deleteAnyChild(LinkedListTree tree) {
        if (tree.getChildCount() > 0) {
            tree.deleteChild(0);
        }
    }

    private static void setFirstChild(LinkedListTree parent, LinkedListTree child) {
        if (parent.getChildCount() == 0) {
            parent.addChildWithTokens(child);
        } else {
            parent.setChildWithTokens(0, child);
        }
    }

    public List<Comment> getCommentsAfter() {
        return CommentUtils.getCommentAfter(this.getAST());
    }

    public int getSpacerSize() {
        return SpacerUtil.calculateSpacerSize(ast);
    }
}