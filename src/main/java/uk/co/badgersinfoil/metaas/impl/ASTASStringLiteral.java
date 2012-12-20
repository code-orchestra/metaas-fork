/*
 * ASTASStringLiteral.java
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

import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.dom.ASStringLiteral;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTASStringLiteral extends ASTLiteral implements ASStringLiteral {

    public String getUnescapedValue() {
        String tokenText = getTokenText();
        if (tokenText == null) {
            return null;
        }
        return tokenText.substring(1, tokenText.length() - 1);
    }

    public ASTASStringLiteral(LinkedListTree ast) {
		super(ast);
	}

    public boolean isApostropheEnclosed() {
        String tokenText = getTokenText();
        if (tokenText == null) {
            return false;
        }
        return tokenText.charAt(0) == '\'';
    }

    public String getValue() {
        return ASTUtils.decodeStringLiteral(getTokenText());
	}

	public void setValue(String value) {
		setTokenText(ActionScriptFactory.str(value));
	}
}