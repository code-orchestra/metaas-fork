package uk.co.badgersinfoil.metaas;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASIntegerLiteral;
import uk.co.badgersinfoil.metaas.dom.ASInvocationExpression;
import uk.co.badgersinfoil.metaas.dom.ASStringLiteral;
import uk.co.badgersinfoil.metaas.dom.Invocation;
import uk.co.badgersinfoil.metaas.impl.AS3FragmentParser;
import uk.co.badgersinfoil.metaas.impl.ASTExpression;
import uk.co.badgersinfoil.metaas.impl.ASTInvocation;
import uk.co.badgersinfoil.metaas.impl.ASTPrinter;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import junit.framework.TestCase;

public class InvocationExpressionTests extends TestCase {
	protected ActionScriptFactory fact = new ActionScriptFactory();
	protected Expression sub;
	protected Invocation expr;
	protected List args;

	public void setUp() {
		args = new ArrayList();
		sub = fact.newExpression("com.example.MyClass");
	}

	public void tearDown() {
		StringWriter buff = new StringWriter();
		if (expr != null) {
			LinkedListTree ast = ((ASTInvocation)expr).getAST();
			new ASTPrinter(buff).print(ast);
			LinkedListTree parsed = AS3FragmentParser.parseExpr(buff.toString());
			CodeMirror.assertASTMatch(ast, parsed);
		}
	}
	
	protected Invocation newTestExpr(Expression sub, List args) {
		return fact.newInvocationExpression(sub, args);
	}

	public void testBasic() {
		args.add(fact.newIntegerLiteral(1));
		expr = newTestExpr(sub, args);
		assertEquals(sub, expr.getTargetExpression());
		List actualArgs = expr.getArguments();
		assertEquals(1, actualArgs.size());
	}

	public void testSetExpression() {
		expr = newTestExpr(sub, args);
		sub = fact.newExpression("Replacement");
		expr.setTargetExpression(sub);
		assertEquals(sub, expr.getTargetExpression());
	}

	public void testSetArguments() {
		expr = newTestExpr(sub, args);
		List newArgs = new ArrayList();
		ASIntegerLiteral arg1 = fact.newIntegerLiteral(1);
		newArgs.add(arg1);
		ASStringLiteral arg2 = fact.newStringLiteral("foo");
		newArgs.add(arg2);
		expr.setArguments(newArgs);
		assertEquals(2, expr.getArguments().size());
		assertEquals(arg1, expr.getArguments().get(0));
		assertEquals(arg2, expr.getArguments().get(1));
	}

	public void testReplaceArguments() {
		expr = newTestExpr(sub, args);
		List args = new ArrayList();
		ASIntegerLiteral arg = fact.newIntegerLiteral(1);
		args.add(arg);
		expr.setArguments(args);
		List newArgs = new ArrayList();
		ASStringLiteral newArg = fact.newStringLiteral("foo");
		newArgs.add(newArg);
		expr.setArguments(newArgs);
		assertEquals(1, expr.getArguments().size());
		assertEquals(newArg, expr.getArguments().get(0));
	}
}