/*
 * StatementContainer.java
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

package uk.co.badgersinfoil.metaas.dom;

import java.util.List;
import uk.co.badgersinfoil.metaas.SyntaxException;

/**
 * Defines the common services provided by structures which can contain
 * ActionScript 'statements'.
 * 
 * <p>Some elements in the metaas DOM extend StatementContainer while they are not
 * strictly containers for multiple statements.  This is a convinience for
 * the common case where the element in question usually appears with an
 * attached block-statement.  So, for example, rather than writing...</p>
 * 
 * <pre class="eg">List stmts = ((ASBlock)ifStmt.getThenStatement()).getStatementList();</pre>
 * 
 * <p>...we can instead write...</p>
 * 
 * <pre class="eg">List stmts = ifStmt.getStatementList();</pre>
 * 
 * If, in the above example, the 'then-clause' of the ASIfStatement was not
 * actually a block, a SyntaxException would be raised.
 */
public interface StatementContainer {
	/**
	 * Checks the syntax of the given code, and then adds the statement to
	 * the end of the current block.
	 * 
	 * @throws SyntaxException if the syntax of the given code fragment is
	 *         incorrect.
	 */
	public Statement addStmt(String statement);


	/**
	 * Adds a new expression-statement to the code, and returns a reference
	 * to it.  An 'expression-statement' is a statement that just contains
	 * an expression (followed in normal ActionScript by a semicolon, though
	 * that should be omitted from the supplied string).
	 */
	public ASExpressionStatement newExprStmt(String expr);

	public ASExpressionStatement newExprStmt(Expression expr);

	/**
	 * Adds a single-line comment to list of statements being generated
	 * 
	 * @param text the text of the comment (minus the initial '//') which
	 *        must not include any newline characters.
	 */
	public void addComment(String text);

	/**
	 * Adds an if-statement to the code. e.g.
	 * <pre>block.newIf("test()").addStmt("trace('success')")</pre>
	 * results in
	 * <pre>
	 * if (test()) {
	 * 	trace('success');
	 * }</pre>
	 */
	public ASIfStatement newIf(String condition);
	public ASIfStatement newIf(Expression condition);

	/**
	 * Adds a C-style for-loop to the code.  e.g.
	 * <pre>block.newFor("var i=0", "i&lt;10", "i++").addStmt("trace(i)")</pre>
	 * results in
	 * <pre>
	 * for (var i=0; i&lt;10; i++) {
	 * 	trace(i);
	 * }</pre>
	 */
	public ASForStatement newFor(String init, String condition, String update);
	public ASForStatement newFor(Expression init, Expression condition, Expression update);

	/**
	 * Adds a for-in-loop to the code.  e.g.
	 * <pre>block.newForIn("var i", "myArray").addStmt("trace(i)")</pre>
	 * results in
	 * <pre>
	 * for (var i in myArray) {
	 * 	trace(i);
	 * }</pre>
	 */
	public ASForInStatement newForIn(String init, String list);
	public ASForInStatement newForIn(Expression init, Expression list);

	public ASForEachInStatement newForEachIn(String init, String list);
	public ASForEachInStatement newForEachIn(Expression init, Expression list);

	/**
	 * Adds a while-loop to the code.  e.g.
	 * <pre>block.newWhile("test()").addStmt("trace('hi there')")</pre>
	 * results in
	 * <pre>
	 * while (test()) {
	 * 	trace('hi there');
	 * }</pre>
	 */
	public ASWhileStatement newWhile(String condition);
	public ASWhileStatement newWhile(Expression condition);

	/**
	 * Adds a do-while-loop to the code.  e.g.
	 * <pre>block.newDoWhile("test()").addStmt("trace('hi there')")</pre>
	 * results in
	 * <pre>
	 * do {
	 * 	trace('hi there');
	 * } while (test());</pre>
	 */
	public ASDoWhileStatement newDoWhile(String condition);
	public ASDoWhileStatement newDoWhile(Expression condition);

	/**
	 * Adds a switch-statement to the code. See {@link ASSwitchStatement}
	 * for more information.
	 */
	public ASSwitchStatement newSwitch(String condition);
	public ASSwitchStatement newSwitch(Expression condition);

	/**
	 * Adds a new with-statement to the code, and returns a reference to
	 * it.  e.g.
	 * <pre class="eg">method.newWith("value").addStmt("trace(test)");</pre>
	 * <p>results in,</p>
	 * <pre class="eg">with (value) {
	 * 	trace(test);
	 * }</pre>
	 */
	public ASWithStatement newWith(String string);
	public ASWithStatement newWith(Expression string);

	/**
	 * Adds a new variable declaration to the code, and returns a reference
	 * to it.  e.g.
	 * <pre class="eg">method.newDeclaration("a=1");</pre>
	 * <p>results in,</p>
	 * <pre class="eg">var a=1;</pre>
	 */
	public ASDeclarationStatement newDeclaration(String assignment);
	public ASDeclarationStatement newDeclaration(Expression assignment);

	/**
	 * Adds a new return-statement to the code (with optional return
	 * expression), and returns a reference to it.  e.g.
	 * <pre class="eg">method.newReturn(null);</pre>
	 * <p>results in a plain return statement,</p>
	 * <pre class="eg">return;</pre>
	 * <p>Whereas passing an expression,</p>
	 * <pre class="eg">method.newReturn("theVal()");</pre>
	 * <p>results will cause that expression to be returned,</p>
	 * <pre class="eg">return theVal();</pre>
	 */
	public ASReturnStatement newReturn(String expr);
	public ASReturnStatement newReturn(Expression expr);
	public ASReturnStatement newReturn();

	/**
	 * Creates a new <code>break</code> statement.
	 */
	public ASBreakStatement newBreak();
	
	public ASTryStatement newTryCatch(String var, String type);

	public ASTryStatement newTryFinally();

	public ASContinueStatement newContinue();

	public ASThrowStatement newThrow(Expression t);

	public ASDefaultXMLNamespaceStatement newDefaultXMLNamespace(String namespace);

	/**
	 * Returns true if if this container currently contains at
	 * least one statement, and false if it is empty, or contains only
	 * comments and whitespace.
	 */
	public boolean containsCode();

	/**
	 * Returns a list of the {@link Statement} objects held in the
	 * containing element.  The list is immutable (entries cannnot be
	 * added, removed or replaced) but the objects obtained from the list
	 * my be modified via the methods they provide.
	 */
	public List getStatementList();
}