/*
 * ASTASVarDeclarationFragment.java
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

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASVarDeclarationFragment;
import uk.co.badgersinfoil.metaas.dom.TypeDescriptor;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTASVarDeclarationFragment extends ASTScriptElement implements ASVarDeclarationFragment {

    public ASTASVarDeclarationFragment(LinkedListTree ast) {
        super(ast);
    }

    public Expression getInitializer() {
        LinkedListTree init = ASTUtils.findChildByType(ast, AS3Parser.ASSIGN);
        if (init == null) {
            return null;
        }
        return ExpressionBuilder.build(init.getFirstChild());
    }

    public TypeDescriptor getTypeDescriptor() {
        LinkedListTree typeAST = ASTUtils.findChildByType(ast, AS3Parser.TYPE_SPEC);
        if (typeAST == null) {
            typeAST = ASTUtils.findChildByType(ast, AS3Parser.VECTOR);
        }
        return new TypeDescriptorImpl(typeAST);
    }

    public String getName() {
        return ast.getText();
    }

    public String getTypeName() {
        LinkedListTree type = ASTUtils.findChildByType(ast, AS3Parser.TYPE_SPEC);
        if (type == null) {
            type = ASTUtils.findChildByType(ast, AS3Parser.VECTOR);
        }
        if (type == null) {
            return null;
        }
        return ASTUtils.typeSpecText(type);
    }
}
