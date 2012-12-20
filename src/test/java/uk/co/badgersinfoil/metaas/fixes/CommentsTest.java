package uk.co.badgersinfoil.metaas.fixes;

import junit.framework.TestCase;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.ASTStatementList;
import uk.co.badgersinfoil.metaas.impl.Comment;
import uk.co.badgersinfoil.metaas.impl.CommentType;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class CommentsTest extends TestCase {
    public void testIt() {
        ActionScriptFactory fact = new ActionScriptFactory();
        ASCompilationUnit unit = loadSyntaxExample(fact);

        ASPackage pkg = unit.getPackage();
        List<Comment> list = pkg.getCommentsBefore();
        assertEquals("javadoc\n\n@author Anton.I.Neverov", list.get(0).getComment());
        assertEquals(CommentType.JAVADOC, list.get(0).getType());
        assertEquals("comment before package", list.get(1).getComment());
        assertEquals(CommentType.SINGLE, list.get(1).getType());

        ASClassType classType = (ASClassType) unit.getType();
        list = classType.getCommentsBefore();
        assertEquals("comment before class", list.get(0).getComment());
        assertEquals(CommentType.SINGLE, list.get(0).getType());

        List<ASField> listOfFields = classType.getAllFields();
        list = listOfFields.get(0).getCommentsBefore();
        assertEquals("static comment before1", list.get(0).getComment());
        assertEquals(CommentType.SINGLE, list.get(0).getType());
        assertEquals("static comment before2", list.get(1).getComment());
        assertEquals(CommentType.SINGLE, list.get(1).getType());
        list = listOfFields.get(0).getCommentsAfter();
        assertEquals("static comment after", list.get(0).getComment());
        assertEquals(CommentType.SINGLE, list.get(0).getType());
        list = listOfFields.get(1).getCommentsBefore();
        assertEquals("comment before javadoc", list.get(0).getComment());
        assertEquals(CommentType.SINGLE, list.get(0).getType());
        assertEquals("javadoc that describes fields\n\n@param abcde", list.get(1).getComment());
        assertEquals(CommentType.JAVADOC, list.get(1).getType());
        assertNull(listOfFields.get(1).getCommentsAfter());

        list = listOfFields.get(2).getCommentsBefore();
        assertEquals("comment before second field", list.get(0).getComment());
        assertEquals(CommentType.SINGLE, list.get(0).getType());
        assertNull(listOfFields.get(2).getCommentsAfter());
        assertNull(listOfFields.get(3).getCommentsBefore());
        assertNull(listOfFields.get(3).getCommentsAfter());
        assertNull(listOfFields.get(4).getCommentsBefore());
        list = listOfFields.get(4).getCommentsAfter();
        assertEquals("comment after second field", list.get(0).getComment());
        assertEquals(CommentType.SINGLE, list.get(0).getType());
        assertNull(listOfFields.get(4).getCommentsBefore());
        list = listOfFields.get(5).getCommentsAfter();
        assertEquals("Very strange\n                                       comment", list.get(0).getComment());
        assertEquals(CommentType.MULTILINE, list.get(0).getType());

        List<ASMethod> listOfMethods = classType.getMethods();
        list = listOfMethods.get(0).getCommentsBefore();
        assertEquals("constructor", list.get(0).getComment());
        assertEquals(CommentType.JAVADOC, list.get(0).getType());
        list = listOfMethods.get(1).getCommentsBefore();
        assertEquals("comment before method's javadoc", list.get(0).getComment());
        assertEquals(CommentType.SINGLE, list.get(0).getType());
        assertEquals("javadoc before method\n  @param pam pam!", list.get(1).getComment());
        assertEquals(CommentType.JAVADOC, list.get(1).getType());
        assertEquals("comment after method's javadoc", list.get(2).getComment());
        assertEquals(CommentType.SINGLE, list.get(2).getType());

        List<Statement> listOfStatements = listOfMethods.get(1).getStatementList();
        assertEquals(" Multi\n\t\t\t\tmulti\n\t\t\t\tmultiline\n\t\t\t\tcomment", ((ASRemarkStatement) listOfStatements.get(0)).getContent().getComment());
        assertEquals(CommentType.MULTILINE, ((ASRemarkStatement) listOfStatements.get(0)).getContent().getType());
        assertEquals("comment after expr statement", listOfStatements.get(1).getCommentsAfter().get(0).getComment());
        assertEquals(CommentType.SINGLE, listOfStatements.get(1).getCommentsAfter().get(0).getType());
        assertEquals("", ((ASRemarkStatement) listOfStatements.get(2)).getContent().getComment());
        assertEquals(CommentType.SINGLE, ((ASRemarkStatement) listOfStatements.get(2)).getContent().getType());
        assertEquals("comment after DoWhile statement", listOfStatements.get(3).getCommentsAfter().get(0).getComment());
        assertEquals(CommentType.SINGLE, listOfStatements.get(3).getCommentsAfter().get(0).getType());
        assertEquals("Suddenly!", ((ASRemarkStatement) listOfStatements.get(4)).getContent().getComment());
        assertEquals(CommentType.SINGLE, ((ASRemarkStatement) listOfStatements.get(4)).getContent().getType());
        assertEquals("comment after return", listOfStatements.get(6).getCommentsAfter().get(0).getComment());
        assertEquals(CommentType.SINGLE, listOfStatements.get(6).getCommentsAfter().get(0).getType());

        ASDoWhileStatement dwstmt = (ASDoWhileStatement) listOfStatements.get(3);
        ASTStatementList body = (ASTStatementList) dwstmt.getBody();
        List<Statement> stmtlst = body.getStatementList();
        assertEquals("comment before expr (a = 2)", ((ASRemarkStatement) stmtlst.get(0)).getContent().getComment());
        assertEquals(CommentType.SINGLE, ((ASRemarkStatement) stmtlst.get(0)).getContent().getType());
        assertEquals("comment after expr (a = 2)", stmtlst.get(1).getCommentsAfter().get(0).getComment());
        assertEquals(CommentType.SINGLE, stmtlst.get(1).getCommentsAfter().get(0).getType());
        assertEquals("remark", ((ASRemarkStatement) stmtlst.get(2)).getContent().getComment());
        assertEquals(CommentType.MULTILINE, ((ASRemarkStatement) stmtlst.get(2)).getContent().getType());
        assertEquals("comment after break", stmtlst.get(3).getCommentsAfter().get(0).getComment());
        assertEquals(CommentType.SINGLE, stmtlst.get(3).getCommentsAfter().get(0).getType());

        ASForStatement forstmt = (ASForStatement) listOfStatements.get(5);
        body = (ASTStatementList) forstmt.getBody();
        stmtlst = body.getStatementList();
        assertEquals("comment after continue", stmtlst.get(0).getCommentsAfter().get(0).getComment());
        assertEquals(CommentType.SINGLE, stmtlst.get(0).getCommentsAfter().get(0).getType());
    }

    private static void print(String str) {
        System.out.println(str);
    }

    private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
        InputStream in = getClass().getClassLoader().getResourceAsStream("Comments.as");
        ActionScriptParser parser = fact.newParser();
        ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
        return unit;
    }
}
