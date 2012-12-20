/*
 * ArgumentUtils.java
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

package uk.co.badgersinfoil.metaas.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

/**
 * Utilities for argument lists
 */
public class ArgumentUtils {

	/**
	 * Finds the first child of the given AST node of type ARGUMENTS
	 * 
	 * @return the matching AST node, or null if none is found
	 */
	public static LinkedListTree findArgs(LinkedListTree parent) {
		return ASTUtils.findChildByType(parent, AS3Parser.ARGUMENTS);
	}

	/**
	 * 
	 * @param args the ARGUMENTS AST node
	 * @return a list of ASExpression
	 */
	public static List astToExpressionList(LinkedListTree args) {
		ASTIterator i = new ASTIterator(args);
		List result = new ArrayList(args.getChildCount());
		while (i.hasNext()) {
			result.add(ExpressionBuilder.build(i.next()));
		}
		return Collections.unmodifiableList(result);
	}

	/**
	 * 
	 * @param argumentNode the ARGUMENTS AST node
	 * @param args list of ASExpression objects to be used as the new
	 *        argument list
	 */
	public static void overwriteArgsWithExpressionList(LinkedListTree argumentNode, List args) {
		// get rid of any old arguments,
		ASTUtils.deleteAllChildren(argumentNode);
		// add the new arguments,
		for (Iterator i=args.iterator(); i.hasNext(); ) {
			ASTExpression arg = (ASTExpression)i.next();
			argumentNode.addChildWithTokens(arg.getAST());
			if (i.hasNext()) {
				argumentNode.appendToken(TokenBuilder.newComma());
				argumentNode.appendToken(TokenBuilder.newSpace());
			}
		}
	}
}