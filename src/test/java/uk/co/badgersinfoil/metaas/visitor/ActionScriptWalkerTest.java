package uk.co.badgersinfoil.metaas.visitor;

import java.io.InputStream;
import java.io.InputStreamReader;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ScriptElement;
import junit.framework.TestCase;

public class ActionScriptWalkerTest extends TestCase {
	public void testIt() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = loadSyntaxExample(fact);

		// walk the whole model,
		FilterStrategy noNulls = new FilterStrategy(new ScriptElementStrategy() {
			public void handle(ScriptElement element) {
				assertNotNull(element);
			}
		});
		ActionScriptWalker walker = new ActionScriptWalker(noNulls);
		walker.walk(unit);
	}

	private ASCompilationUnit loadSyntaxExample(ActionScriptFactory fact) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("AllSyntax.as");
		ActionScriptParser parser = fact.newParser();
		ASCompilationUnit unit = parser.parse(new InputStreamReader(in));
		return unit;
	}
}
