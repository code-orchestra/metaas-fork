package uk.co.badgersinfoil.metaas.antlr;

import java.io.IOException;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
import uk.co.badgersinfoil.metaas.impl.TokenBuilder;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import junit.framework.TestCase;

public class ASTTest extends TestCase {
	public void testIt() throws IOException {
		LinkedListTree switchStmt = ASTUtils.newAST(AS3Parser.SWITCH, "switch");
		LinkedListTree defaultStmt1 = ASTUtils.newAST(AS3Parser.DEFAULT, "default");
		switchStmt.addChildWithTokens(defaultStmt1);
		LinkedListToken colon = TokenBuilder.newColon();
		defaultStmt1.appendToken(colon);
		LinkedListTree stmtList = ASTUtils.newImaginaryAST(AS3Parser.SWITCH_STATEMENT_LIST);
		defaultStmt1.addChildWithTokens(stmtList);
		assertEquals(":", defaultStmt1.getStopToken().getText());
		assertNull(colon.getNext());

		LinkedListTree defaultStmt2 = ASTUtils.newAST(AS3Parser.DEFAULT, "default");
		switchStmt.addChildWithTokens(defaultStmt2);
		assertNull(((LinkedListToken)defaultStmt2.token).getNext());

		LinkedListTree semi = ASTUtils.newAST(AS3Parser.SEMI, ";");
		ASTUtils.addChildWithIndentation(stmtList, semi);
		assertEquals(semi.token, defaultStmt1.getStopToken());
		assertEquals(defaultStmt2.getStartToken(), ((LinkedListToken)semi.token).getNext());
		// SEMI should have been inserted somewhere mid-token-stream
		assertNotNull(((LinkedListToken)semi.token).getNext());
	}
}
