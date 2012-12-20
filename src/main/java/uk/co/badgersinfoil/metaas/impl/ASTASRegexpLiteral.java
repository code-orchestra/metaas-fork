/*
 * ASTASRegexpLiteral.java
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

import uk.co.badgersinfoil.metaas.dom.ASRegexpLiteral;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTASRegexpLiteral extends ASTLiteral implements ASRegexpLiteral {
    private static final String SLASH = "/";

    public ASTASRegexpLiteral(LinkedListTree ast) {
		super(ast);
	}

    public String getValue() {
        return toString().substring(1, getClosingSlashIndex());
    }

    public String getModifier() {
        String rawValue = toString();
        int closingSlashIndex = getClosingSlashIndex();

        if (closingSlashIndex + 1 == rawValue.length()) {
            return null;
        }

        return rawValue.substring(closingSlashIndex + 1);
    }

    private int getClosingSlashIndex() {
        String regexpInSlashes = toString();
        int closingSlashIndex = regexpInSlashes.length();
        while (closingSlashIndex-->0) {
            if (regexpInSlashes.charAt(closingSlashIndex) == SLASH.charAt(0)) {
                break;
            }
        }
        return closingSlashIndex;
    }
}
