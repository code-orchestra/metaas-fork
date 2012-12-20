/*
 * ASTASXMLLiteral.java
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

import uk.co.badgersinfoil.metaas.dom.ASXMLLiteral;
import uk.co.badgersinfoil.metaas.dom.xml.ASXMLInitializer;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.impl.parser.e4x.E4XParser;
import uk.co.badgersinfoil.metaas.impl.xml.ASTASXMLCDATA;
import uk.co.badgersinfoil.metaas.impl.xml.ASTASXMLComment;
import uk.co.badgersinfoil.metaas.impl.xml.ASTASXMLElement;
import uk.co.badgersinfoil.metaas.impl.xml.ASTASXMLPI;

public class ASTASXMLLiteral extends ASTLiteral implements ASXMLLiteral {

	public ASTASXMLLiteral(LinkedListTree ast) {
		super(ast);
	}
	
	public String getValueString() {
		return ASTUtils.stringifyNode(ast.getFirstChild());
	}

    public ASXMLInitializer getRoot() {
        LinkedListTree rootAST = ASTUtils.findChildByType(ast, E4XParser.XML_ELEMENT);
        if (rootAST != null) {
            return new ASTASXMLElement(rootAST);
        }
        rootAST = ASTUtils.findChildByType(ast, E4XParser.XML_EMPTY_ELEMENT);
        if (rootAST != null) {
            return new ASTASXMLElement(rootAST);
        }
        rootAST = ASTUtils.findChildByType(ast, E4XParser.XML_LIST);
        if (rootAST != null) {
            return new ASTASXMLElement(rootAST);
        }
        rootAST = ASTUtils.findChildByType(ast, E4XParser.XML_CDATA);
        if (rootAST != null) {
            return new ASTASXMLCDATA(rootAST);
        }
        rootAST = ASTUtils.findChildByType(ast, E4XParser.XML_COMMENT);
        if (rootAST != null) {
            return new ASTASXMLComment(rootAST);
        }
        rootAST = ASTUtils.findChildByType(ast, E4XParser.XML_PI);
        if (rootAST != null) {
            return new ASTASXMLPI(rootAST);
        }
        return null;
    }
}
