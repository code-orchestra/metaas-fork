/*
 * ASTASParser.java
 * 
 * Copyright (c) 2006 David Holroyd
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

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.EmptySourceFileException;
import uk.co.badgersinfoil.metaas.SyntaxException;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;


public class ASTActionScriptParser implements ActionScriptParser {

	public ASCompilationUnit parse(Reader in) {
		AS3Parser parser = ASTUtils.parse(in);
		LinkedListTree cu;
		try {
			cu = AS3FragmentParser.tree(parser.compilationUnit());
		} catch (RecognitionException e) {
            SyntaxException syntaxException = ASTUtils.buildSyntaxException(null, parser, e, e.token);
            if (syntaxException instanceof EmptySourceFileException) {
                return null;
            }
            throw syntaxException;
		}
        if (cu.getFirstChildWithType(AS3Parser.PACKAGE) != null) {
            return new AS3ASTCompilationUnit(cu);
        } else {
            return new AS2ASTCompilationUnit(cu);
        }
	}

	public ASTStatementList parseAsStatementList(Reader in) {
        AS3Parser parser = ASTUtils.parse(in);
		LinkedListTree cu;
		try {
            ParserRuleReturnScope returnScope = parser.statementList();
            if (returnScope == null) {
                // CO-4910
                // returnScope can be null only in case when first token in regular stream is EOF
                // It can be in case of empty source or in case when text is fully commented

                ASTCommentList astCommentList = new ASTCommentList();
                List<Comment> comments = astCommentList.getComments();

                TokenStream tokenStream = parser.getTokenStream();
                tokenStream.rewind();
                // tokenStream is fully loaded so we can iterate over it's tokens
                for (int i = 0; i < tokenStream.size(); i++) {
                    Token token = tokenStream.get(i);
                    if (token.getChannel() == Token.HIDDEN_CHANNEL) {
                        String tokenText = token.getText();
                        comments.add(CommentUtils.convertStringToComment(tokenText));
                    }
                }

                if (!comments.isEmpty()) {
                    return astCommentList;
                }

                return null;
            }
			cu = AS3FragmentParser.tree(returnScope);
		} catch (RecognitionException e) {
            SyntaxException syntaxException = ASTUtils.buildSyntaxException(null, parser, e, e.token);
            if (syntaxException instanceof EmptySourceFileException) {
                return null;
            }
            throw syntaxException;
		}
		return new ASTStatementList(cu);
	}

    public ExpressionList parseAsExpressionList(Reader in) {
        AS3Parser parser = ASTUtils.parse(in);
		LinkedListTree cu;
		try {
            ParserRuleReturnScope returnScope = parser.expressionOnly();
            if (returnScope == null) {
                return null;
            }
			cu = AS3FragmentParser.tree(returnScope);
		} catch (RecognitionException e) {
            SyntaxException syntaxException = ASTUtils.buildSyntaxException(null, parser, e, e.token);
            if (syntaxException instanceof EmptySourceFileException) {
                return null;
            }
            throw syntaxException;
		}
        ExpressionList list = new ExpressionListImpl();
        try{
            list.addExpression(ExpressionBuilder.build(cu));
        } catch (IllegalArgumentException e) {
            for (int i = 0; i < cu.getChildCount(); i++) {
                if (cu.getChild(i) instanceof LinkedListTree) {
                    list.addExpression(ExpressionBuilder.build((LinkedListTree) cu.getChild(i)));
                }
            }
        }
		return list;
    }

    public List<ScriptElement> parseAsTypeMembers(Reader in) {
        AS3Parser parser = ASTUtils.parse(in);
        List<ScriptElement> result = new ArrayList<ScriptElement>();
		try {
            ParserRuleReturnScope returnScope = parser.asTypeMember();
            if (returnScope == null) {
                return Collections.EMPTY_LIST;
            }
            LinkedListTree tree = (LinkedListTree) returnScope.getTree();
            switch (tree.getType()) {
                case AS3Parser.VAR_DEF:
                    result.add(new ASTASField(tree));
                    break;
                case AS3Parser.METHOD_DEF:
                    result.add(new ASTASMethod(tree));
                    break;
                case AS3Parser.NAMESPACE_DEF:
                    result.add(new ASTASNamespaceDeclaration(tree));
                    break;
                case 0:
                    for (int i = 0; i < tree.getChildCount(); i++) {
                        LinkedListTree child = (LinkedListTree) tree.getChild(i);
                        switch (child.getType()) {
                            case AS3Parser.VAR_DEF:
                                result.add(new ASTASField(child));
                                break;
                            case AS3Parser.METHOD_DEF:
                                result.add(new ASTASMethod(child));
                                break;
                            case AS3Parser.NAMESPACE_DEF:
                                result.add(new ASTASNamespaceDeclaration(child));
                                break;
                            default:
                        }
                    }
                    break;
                default:
            }
		} catch (RecognitionException e) {
            if (e.getUnexpectedType() == AS3Parser.EOF) {
                return Collections.EMPTY_LIST;
            }
            SyntaxException syntaxException = ASTUtils.buildSyntaxException(null, parser, e, e.token);
            if (syntaxException instanceof EmptySourceFileException) {
                return Collections.EMPTY_LIST;
            }
            throw syntaxException;
		}
		return result;
    }

	public ASType parseAsClass(Reader in) {
		AS3Parser parser = ASTUtils.parse(in);
		LinkedListTree cu;
		try {
			cu = AS3FragmentParser.tree(parser.classOrInterface());
		} catch (RecognitionException e) {
            SyntaxException syntaxException = ASTUtils.buildSyntaxException(null, parser, e, e.token);
            if (syntaxException instanceof EmptySourceFileException) {
                return null;
            }
            throw syntaxException;
		}
        if (cu.getToken().getType() == AS3Parser.CLASS_DEF) {
		    return new ASTASClassType(cu);
        } else if (cu.getToken().getType() == AS3Parser.INTERFACE_DEF) {
		    return new ASTASInterfaceType(cu);
        } else {
            return null;
        }
	}

    public MXMLASBlock parseAsMXMLASblock(Reader in) {
        AS3Parser parser = ASTUtils.parse(in);
		LinkedListTree cu;
		try {
            ParserRuleReturnScope returnScope = parser.mxmlASBlock();
            if (returnScope == null) {
                return null;
            }
			cu = AS3FragmentParser.tree(returnScope);
		} catch (RecognitionException e) {
            SyntaxException syntaxException = ASTUtils.buildSyntaxException(null, parser, e, e.token);
            if (syntaxException instanceof EmptySourceFileException) {
                return null;
            }
            throw syntaxException;
		}
		return new MXMLASBlockImpl(cu);
    }
}