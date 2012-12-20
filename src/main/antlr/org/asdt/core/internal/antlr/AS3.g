/*
 * AS3.g3
 *
 * Copyright (c) 2005 Martin Schnabel
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


grammar AS3;

// Originally derived from the ANTLRv2 ActionScript grammar by
// Martin Schnabel, included in the ASDT project,
//   http://www.asdt.org
//   http://sourceforge.net/projects/aseclipseplugin/

options {
	k = 2;
	output=AST;
	ASTLabelType=LinkedListTree;
}

tokens {
	COMPILATION_UNIT;
	TYPE_BLOCK; METHOD_DEF; VAR_DEF;
	ANNOTATIONS; ANNOTATION; ANNOTATION_PARAMS;
	MODIFIERS; NAMESPACE_DEF;
	ACCESSOR_ROLE;
	CLASS_DEF; INTERFACE_DEF;
	PARAMS;
	PARAM; TYPE_SPEC;
	BLOCK; ELIST;
	CONDITIONAL_COMPILATION_BLOCK;
	CONDITION; ARGUMENTS;
	CONDITION_LIST;
	EXPR_STMNT;
	ENCPS_EXPR;
	VAR_INIT;
	INCLUDE_DIRECTIVE_AS3;
	METHOD_CALL; PROPERTY_OR_IDENTIFIER; PROPERTY_ACCESS; TYPE_NAME;
	ARRAY_ACC;
	MULT;
	UNARY_PLUS; UNARY_MINUS; POST_INC; POST_DEC; PRE_INC; PRE_DEC;
	ARRAY_LITERAL;
	OBJECT_LITERAL;
	OBJECT_FIELD; FUNC_DEF;
	FOR_INIT; FOR_CONDITION; FOR_ITERATOR;
	FOR_EACH; FOR_IN;
	SWITCH_STATEMENT_LIST;
	IDENTIFIER; IDENTIFIER_STAR;
	DEFAULT_XML_NAMESPACE;
	XML_LITERAL; REGEXP_LITERAL;
	E4X_FILTER;
	E4X_ATTRI_PROPERTY; E4X_ATTRI_STAR; E4X_ATTRI_EXPR;
	SUPER_CONSTRUCTOR_INVOCATION;
	GENERIC_TYPE;
	VIRTUAL_PLACEHOLDER;
	STAR_ASSIGN;
	LABEL;
	OUT_OF_FUNCTION_STMT;
	VECTOR_LITERAL;
	ENUMERABLE;
	SR;
	BSR;
	GE;
	SR_ASSIGN;
	BSR_ASSIGN;
	STATIC_INITIALIZER;
	EMPTY_LIST_ITEM;
}

scope InOperator {
	boolean allowed;
}

@parser::header {
package org.asdt.core.internal.antlr;

import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTreeAdaptor;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTokenStream;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTokenSource;
import java.io.StringReader;
import java.io.Reader;
import java.io.IOException;
import uk.co.badgersinfoil.metaas.impl.parser.E4XHelper;
import uk.co.badgersinfoil.metaas.impl.parser.RegexSimpleHelper;
import uk.co.badgersinfoil.metaas.impl.TokenBuilder;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
}
@lexer::header {
package org.asdt.core.internal.antlr;
}

// disable standard error handling; be strict
@rulecatch { }

@parser::members {

	public static final int CHANNEL_PLACEHOLDER = 999;

	protected void mismatch(IntStream input, int ttype, BitSet follow)
		throws RecognitionException
	{
		throw new MismatchedTokenException(ttype, input);
	}
	
	private AS3Lexer lexer;
	private CharStream cs;

	public void setInput(AS3Lexer lexer, CharStream cs) {
		this.lexer = lexer;
		this.cs = cs;
	}

	/**
	 * Handle 'island grammar' for embeded XML-literal elements.
	 */
	private LinkedListTree parseXMLLiteral() throws RecognitionException {
		return E4XHelper.parseXMLLiteral(lexer, cs, (LinkedListTokenStream)input);
	}

	/**
	 * Handle 'island grammar' for skipping over embeded
	 * regular-expression-literal values.
	 */
	private LinkedListTree parseRegexpLiteral(LinkedListToken startMarker) throws RecognitionException {
		return RegexSimpleHelper.parseRegexpLiteral(startMarker, lexer, cs, (LinkedListTokenStream)input);
	}

	private boolean virtualSemi() {
		LinkedListToken tok = ((LinkedListToken)input.LT(1)).getPrev();
		while (tok.getType() == WS) {
			tok = tok.getPrev();
		}
		return tok.getType() == NL;
	}
	
	/**
	 * Adds a placeholder token into the stream, corresponding to the
	 * location of the given AST node, if the given AST node has no
	 * children.  Only use this function with 'imaginary' nodes, not with
	 * nodes that already correspond to a concrete Token.
	 */
	private LinkedListToken placeholder(LinkedListTree imaginary) {
		if (imaginary.getChildCount() > 0) {
			// one of this node's children will presumably have
			// some tokens of its own, so there's no need for us
			// to synthesize one,
			return imaginary.getStartToken();
		}

		// Since this this AST node is 'imaginary', it does not directly
		// correspond to any Token in the input stream.  Also, since
		// it has no children, there is nothing anchoring it to a
		// position in the token-stream, which will make it difficult to
		// know where new tokens should be inserted if the calling app
		// adds a child to this node.  We therefore insert a virtual
		// token at this point in the stream just so that the AST node
		// can remember its own location.

		LinkedListToken tok = (LinkedListToken)input.LT(1);
    		LinkedListToken placeholder = TokenBuilder.newPlaceholder(imaginary);
		tok.beforeInsert(placeholder);
		return placeholder;
	}

	private void demarcate(LinkedListTree parent) {
		parent.setStartToken(parent.getFirstChild().getStartToken());
		parent.setStopToken(parent.getLastChild().getStopToken());
	}
	
	
    private final void promoteEOL(ParserRuleReturnScope rule) {
        // Get current token and its type (the possibly offending token).
        Token myLt = input.LT(1);
        int la = myLt.getType();

        // We only need to promote an EOL when the current token is offending (not a SEMIC, EOF, RBRACE, EOL or MultiLineComment).
        // EOL and MultiLineComment are not offending as they're already promoted in a previous call to this method.
        // Promoting an EOL means switching it from off channel to on channel.
        // A MultiLineComment gets promoted when it contains an EOL.
        if (!(la == SEMI || la == EOF || la == RCURLY || la == NL || la == ML_COMMENT || la == SL_COMMENT))
        {
            if (myLt instanceof LinkedListToken) {
                LinkedListToken linkedListToken = ((LinkedListToken) myLt).getPrev();
                while (linkedListToken != null) {
                    if (linkedListToken.getChannel() == Token.DEFAULT_CHANNEL) {
                        // On channel token found: stop scanning.
                        break;
                    } else if (linkedListToken.getType() == NL || linkedListToken.getType() == SL_COMMENT || (linkedListToken.getType() == ML_COMMENT && linkedListToken.getText().matches("/.*\r\n|\r|\n"))) {
                        // We found our EOL: promote the token to on channel, position the input on it and reset the rule start.
                        linkedListToken.setChannel(Token.DEFAULT_CHANNEL);
                        ((LinkedListTokenStream) input).setCurrentToken(linkedListToken);
                        
                        if (rule != null)
                        {
                            rule.start = linkedListToken;
                        }
                        break;
                    }
                    linkedListToken = linkedListToken.getPrev();
                }
            }
        }
    }
	
}

@lexer::members {
	public boolean objectLiteralFieldName = false;	
}


/**
 * this is the start rule for this parser
 */
compilationUnit
	:	(	as2CompilationUnit
		|	as3CompilationUnit
		) -> ^(COMPILATION_UNIT as2CompilationUnit? as3CompilationUnit?)
	;

as2CompilationUnit
	:	importDefinition*
		as2Type
	;

as2Type
	:	annos=annotations! { placeholder($annos.tree); }
		mods=modifiers!
	(	as2ClassDefinition[$annos.tree,$mods.tree]
	|	as2InterfaceDefinition[$annos.tree,$mods.tree]
	)
	;

as3CompilationUnit
	:	packageDecl
		packageBlockEntryBlock*
		EOF!
	;

packageDecl
	:	PACKAGE^ identifier?
		packageBlock
	;

packageBlock
	:	LCURLY
		packageBlockEntry*
		RCURLY
		-> ^(BLOCK packageBlockEntry*)
	;


packageBlockEntryBlock
	:
		(
			LCURLY packageBlockEntry* RCURLY
		|
			packageBlockEntry
		)
	;

packageBlockEntry
	:		importDefinition
		|	(includeDirective) => includeDirective
		|	(annotations) => annos=annotations! { retval.start=placeholder($annos.tree); }
			mods=modifiers! { placeholder($mods.tree); }
			(	classDefinition[$annos.tree,$mods.tree]
			|	interfaceDefinition[$annos.tree,$mods.tree]
			|	variableDefinition[$annos.tree,$mods.tree]
			|	methodDefinition[$annos.tree,$mods.tree]
			|	namespaceDefinition[$annos.tree,$mods.tree]
			|	useNamespaceDirective
			|	importDefinition
			)
		|	outOfFunctionStatement -> ^(OUT_OF_FUNCTION_STMT outOfFunctionStatement)
//		|	statement
		
	;
	
classOrInterface
	:	(annotations) => annos=annotations! { retval.start=placeholder($annos.tree); }
			mods=modifiers! { placeholder($mods.tree); }
			(	classDefinition[$annos.tree,$mods.tree]
			|	interfaceDefinition[$annos.tree,$mods.tree]
			)	
	;

endOfFile
	:	EOF!
	;

importDefinition
	:	IMPORT^ identifierStar ( semi | memberOnSameLine )
	;

semi
@init
{
	// Mark current position so we can unconsume a RBRACE.
	int marker = input.mark();
	// Promote EOL if appropriate	
	promoteEOL(retval);
}
	: SEMI!
	| EOF!
	| RCURLY! { input.rewind(marker); }
	| NL! | ML_COMMENT! | SL_COMMENT! // (with EOL in it)
	| ELSE! { input.rewind(marker); }
	;
	
memberOnSameLine
@init
{
	// Mark current position so we can unconsume matched token
	int marker = input.mark();
}
	: 
	( PUBLIC!
	| PROTECTED!
	| PRIVATE!
	| LCURLY! // Next block
	| LBRACK! // Annotation
	| IDENT! // Possible namespace visibility
	| VAR!
	| FUNCTION!
	| CLASS!
	| CONST!
	| STATIC!
	| INTERNAL!
	| OVERRIDE!
	| USE!
	| NAMESPACE!
	| IMPORT!
	| FINAL!
	| INCLUDE
	) { input.rewind(marker); }
	;

classDefinition[LinkedListTree annos, LinkedListTree mods]
	:	CLASS ident LNOT?
		classExtendsClause
		implementsClause
		typeBlock
		-> ^(CLASS_DEF {$annos} {$mods} ident classExtendsClause implementsClause typeBlock)
	;

as2ClassDefinition[LinkedListTree annos, LinkedListTree mods]
	:	CLASS identifier
		classExtendsClause
		implementsClause
		typeBlock
		-> ^(CLASS_DEF {$annos} {$mods} identifier classExtendsClause implementsClause typeBlock)
	;

interfaceDefinition[LinkedListTree annos, Tree mods]
	:	{ retval.start = annos.getStartToken(); }
		INTERFACE ident
		interfaceExtendsClause
		typeBlock
		-> ^(INTERFACE_DEF {$annos} {$mods} ident interfaceExtendsClause typeBlock)
	;

as2InterfaceDefinition[LinkedListTree annos, Tree mods]
	:	INTERFACE identifier
		interfaceExtendsClause
		typeBlock
		-> ^(INTERFACE_DEF {$annos} {$mods} identifier interfaceExtendsClause typeBlock)
	;

classExtendsClause
	:	(EXTENDS^ identifier)?
	;
interfaceExtendsClause
	:	(EXTENDS^ identifier ( COMMA! identifier)*)?
	;
implementsClause
	:	(IMPLEMENTS^ identifier ( COMMA! identifier)*)?
	;
typeBlock
	:	LCURLY
		typeBlockEntry*
		RCURLY
		-> ^(TYPE_BLOCK typeBlockEntry*)
	;

typeBlockEntry
	:	
		(annotations) => a=annotations! { retval.start = placeholder($a.tree); }
		m=modifiers! { placeholder($m.tree); }
		(	fieldDefinition[$a.tree,$m.tree]
		|	methodDefinition[$a.tree,$m.tree] 
		|	namespaceDefinition[$a.tree,$m.tree]
		)
	|	staticInitializer -> ^(STATIC_INITIALIZER staticInitializer)
	|	outOfFunctionStatement -> ^(OUT_OF_FUNCTION_STMT outOfFunctionStatement)
	|	importDefinition
	|	useNamespaceDirective
	|	as2IncludeDirective
	|	includeDirective
	;
	
asTypeMember
	:
	(
		(annotations) => a=annotations! { retval.start = placeholder($a.tree); }
		m=modifiers! { placeholder($m.tree); }
		(	fieldDefinition[$a.tree,$m.tree]
		|	methodDefinition[$a.tree,$m.tree] 
		|	namespaceDefinition[$a.tree,$m.tree]
		)
	)+
	EOF!
	;

mxmlASBlock
	:	mxmlASBlockEntry* -> ^(BLOCK mxmlASBlockEntry*)
	;
		
mxmlASBlockEntry
	:	
		(annotations) => a=annotations! { retval.start = placeholder($a.tree); }
		m=modifiers! { placeholder($m.tree); }
		(	fieldDefinition[$a.tree,$m.tree]
		|	methodDefinition[$a.tree,$m.tree] 
		)
	|	importDefinition
	;


as2IncludeDirective
	:	INCLUDE_DIRECTIVE
		STRING_LITERAL
	;

includeDirective
	:	INCLUDE
		STRING_LITERAL
		semi -> ^(INCLUDE_DIRECTIVE_AS3 STRING_LITERAL)
	;

methodDefinition[LinkedListTree annos, Tree mods]
	:	{ retval.start = annos.getStartToken(); }
		FUNCTION
		r=optionalAccessorRole  { placeholder($r.tree); }
		ident
		parameterDeclarationList
		typeExpression?
		block?
		( semi | memberOnSameLine )
		-> ^(METHOD_DEF {$annos} {$mods}
		                optionalAccessorRole ident
				parameterDeclarationList
				typeExpression?
				block?)
	;

optionalAccessorRole
	:	accessorRole?
		-> ^(ACCESSOR_ROLE accessorRole?)
	;

accessorRole
	:	GET | SET
	;
	
staticInitializer
	:
	LCURLY!
	(
		(annotations) => a=annotations! { retval.start = placeholder($a.tree); }
		m=modifiers! { placeholder($m.tree); }
		(	fieldDefinition[$a.tree,$m.tree]
		|	methodDefinition[$a.tree,$m.tree] 
		|	namespaceDefinition[$a.tree,$m.tree]
		)
	|	outOfFunctionStatement	
	)*
	 RCURLY!
	;

namespaceDefinition[LinkedListTree annos, Tree mods]
	:	NAMESPACE ident (ASSIGN STRING_LITERAL)? ( semi | memberOnSameLine )
		-> ^(NAMESPACE_DEF {$annos} {$mods} NAMESPACE ident STRING_LITERAL?)
	;

useNamespaceDirective
	:	USE NAMESPACE ident ( semi | memberOnSameLine ) -> ^(USE ident)
	;

variableDefinition[LinkedListTree annos, Tree mods]
scope InOperator;
@init {
	$InOperator::allowed = true;
}
	:	decl=varOrConst variableDeclarator
		(COMMA variableDeclarator)*
		semi
		-> ^(VAR_DEF {$annos} {$mods} $decl variableDeclarator+)
	;
	
fieldDefinition[LinkedListTree annos, Tree mods]
scope InOperator;
@init {
	$InOperator::allowed = true;
}
	:	decl=varOrConst variableDeclarator
		(COMMA variableDeclarator)*
		( semi | memberOnSameLine )
		-> ^(VAR_DEF {$annos} {$mods} $decl variableDeclarator+)
	;	

varOrConst
	:	VAR | CONST
	;

variableDeclarator
	:	ident^ typeExpression? 
		variableInitializer?
	;
declaration
	:	annotationsNotEmpty?
		varOrConst^ variableDeclarator
		declarationTail
	;

declarationTail
scope InOperator;
@init {
	$InOperator::allowed = true;
}
	:	(COMMA! variableDeclarator)*
	;

variableInitializer
	:	ASSIGN^ assignmentExpression
	;

// A list of formal parameters
// TODO: shouldn't the 'rest' parameter only be allowed in the last position?
parameterDeclarationList
	:	LPAREN
		(	parameterDeclaration
			(COMMA parameterDeclaration)*
		)?
		RPAREN
		-> ^(PARAMS parameterDeclaration*)
	;


parameterDeclaration
	:	basicParameterDeclaration | parameterRestDeclaration
	;

basicParameterDeclaration
	:	CONST? ident typeExpression?		
		parameterDefault?
		-> ^(PARAM CONST? ident typeExpression? parameterDefault?)
	;

parameterDefault
scope InOperator;
@init {
	$InOperator::allowed = true;
}
		// TODO: can we be more strict about allowed values?
	:	ASSIGN^ assignmentExpression
	;

parameterRestDeclaration
	:	REST (ident || ident typeExpression)?
		-> ^(PARAM REST ident? typeExpression?)
	;
	
block
	:	label? LCURLY blockEntry* RCURLY
		-> ^(BLOCK blockEntry* label?)
	;
	
statementList
@init {
	// RF-27
	if (input.LT(1).getType() == EOF) {
		return null;
	}
}
	:	blockEntry+ -> ^(BLOCK blockEntry+)
	;
	
conditionalCompilationBlock
	:	namespaceName DBL_COLON ident block
		-> ^(CONDITIONAL_COMPILATION_BLOCK block namespaceName ident)
	;

blockEntry
	:
/*options { k=*; }
	:	a=annotations! { placeholder($a.tree); }
		m=modifiers!
		(	variableDefinition[$a.tree,$m.tree]
		|	methodDefinition[$a.tree,$m.tree]
		)
	|*/	statement
	;

condition
	:	LPAREN expression RPAREN
		-> ^(CONDITION expression)
	;

conditionList
scope InOperator;
@init {
	$InOperator::allowed = true;
}
	:	LPAREN expressionList RPAREN
		-> ^(CONDITION_LIST expressionList)
	;

statement
	:
		declarationStatement
	|	namespaceDefinitionStatement
	|	useNamespaceDirective
	|	localFunction	
	|	outOfFunctionStatement
	|	importDefinition
	;		


outOfFunctionStatement
options { k = 3 }
@init
{
// Hack for expression statement that is a simple name
Token firstToken = input.LT(1);

if (firstToken.getType() == AS3Parser.IDENT) {
    Token secondToken = input.LT(2);
    switch (secondToken.getType()) {
        case AS3Parser.IDENT:
        case AS3Parser.FOR:
        case AS3Parser.IMPORT:
        case AS3Parser.IF:
        case AS3Parser.TRY:
        case AS3Parser.BREAK:
        case AS3Parser.CONTINUE:
        case AS3Parser.WITH:
        case AS3Parser.THROW:
        case AS3Parser.VAR:
            LinkedListToken myT = (LinkedListToken) firstToken;
            while ((myT = myT.getNext()) != secondToken) {
                if (myT.getType() == AS3Parser.NL) {
                    myT.setChannel(Token.DEFAULT_CHANNEL);
                    break;
                }
            }
            break;
        default:
    }
}
}
	:	(label? LCURLY)=> block
	|	(namespaceName DBL_COLON ident LCURLY)=> conditionalCompilationBlock
	|	expressionStatement (NL!)?
	|	labelledStatement
	// with statement
	|	withStatement
	
	// get out of a loop (or switch)
	|	breakStatement

	// do next iteration of a loop
	|	continueStatement

	// Return an expression
	|	returnStatement

	// throw an exception
	|	throwStatement	

	// handle 'default xml namespace = "foo"'
	|	defaultXMLNamespaceStatement

	// empty statement
	|	SEMI!
	;
	
labelledStatement
	:
		label? labelEnabledStatement -> ^(labelEnabledStatement label?)
	;	
	
labelEnabledStatement
	:
		ifStatement
		|
		loopStatement
		|
		tryStatement
	;	
	
loopStatement
	:
	// For statements
		forEachStatement
	|	forStatement
	// While statement
	|	whileStatement		
	// do-while statement
	|	doWhileStatement	
	|	switchStatement
	;	

declarationStatement
scope InOperator;
@init {
	$InOperator::allowed = true;
}
	:	declaration semi
	;
	
namespaceDefinitionStatement
	:	NAMESPACE ident (ASSIGN STRING_LITERAL)? semi
		-> ^(NAMESPACE_DEF NAMESPACE ident STRING_LITERAL?)
	;

expressionStatement
scope InOperator;
@init {
	$InOperator::allowed = true;
}
	:	expressionList { 
		    Token myLt = input.LT(1);
                    int la = myLt.getType();

                    if (!(la == SEMI || la == EOF || la == RCURLY || la == NL || la == ML_COMMENT)) {
                        if (myLt instanceof LinkedListToken) {
                            LinkedListToken linkedListToken = ((LinkedListToken) myLt).getPrev();
                            while (linkedListToken != null) {
                                if (linkedListToken.getChannel() != Token.HIDDEN_CHANNEL) {
                                    if (E4XHelper.isXMLClosingTokenType(linkedListToken.getType())) {
                                        ((LinkedListTokenStream) input).insertCurrentToken(SEMI, ";");
                                    }
                                    break;
                                }
                                linkedListToken = linkedListToken.getPrev();
                            }
                        }
                    }	
	 	}
	  	semi
		-> ^(EXPR_STMNT expressionList)
	;
	
ifStatement
	:	IF^ conditionList statement
		((ELSE)=>elseClause)?
	;

elseClause
	:	ELSE^ statement
	;

throwStatement
	:	THROW^ { promoteEOL(null); } expression semi!
	;

tryStatement
	:	TRY^ block catchBlock* finallyBlock?
	;

catchBlock
	:	CATCH LPAREN ident typeExpression? RPAREN
		block
		-> ^(CATCH ident typeExpression? block)
	;

finallyBlock
	:	FINALLY^ block
	;

returnStatement
scope InOperator;
@init {
	$InOperator::allowed = true;
}
	:	RETURN^ { promoteEOL(null); } expressionList? semi
	;
		
continueStatement
	:	CONTINUE^ { if (input.LA(1) == IDENT) promoteEOL(null); } ident? semi
	;

breakStatement
	:	BREAK^ { if (input.LA(1) == IDENT) promoteEOL(null); } ident? semi
	;

switchStatement
	:	SWITCH^ conditionList
		switchBlock
	;

switchBlock
	:	LCURLY
		(switchCase)*
		RCURLY
		-> ^(BLOCK switchCase*)
	;

switchCase 
	:	
	caseStatement
	|
	defaultStatement	
	;

caseStatement
	:	CASE^ expressionList COLON! l=switchStatementList { placeholder($l.tree); }
	;
	
defaultStatement
	:	DEFAULT^ COLON! l=switchStatementList { placeholder($l.tree); }
	;

switchStatementList
	:	statement* -> ^(SWITCH_STATEMENT_LIST statement*)
	;

label
	:	ident COLON -> ^(LABEL ident)
	;

forEachStatement
	:	f=FOR EACH
		LPAREN
		forInClause
		RPAREN
		statement
		-> ^(FOR_EACH[$f] forInClause statement)
	;

forStatement
scope InOperator;
@init {
	$InOperator::allowed = false;
}
	:	f=FOR
		LPAREN
		(	(forInClauseDecl IN)=>forInClause RPAREN statement
			-> ^(FOR_IN[$f] forInClause statement)

		|	traditionalForClause RPAREN statement
			-> ^($f traditionalForClause statement)
		)
	;

traditionalForClause
	:	a=forInit {placeholder($a.tree);} SEMI!	// initializer
		b=forCond {placeholder($b.tree);} SEMI!	// condition test
		c=forIter {placeholder($c.tree);}		// updater
	;

forInClause
	:	forInClauseDecl IN! forInClauseTail
	;

forInClauseDecl
scope InOperator;
@init {
	$InOperator::allowed = false;
}
	:	declaration | postfixExpression
	;


forInClauseTail
scope InOperator;
@init {
	$InOperator::allowed = true;
}
	:	expressionList
	;

// The initializer for a for loop
forInit	
scope InOperator;
@init {
	$InOperator::allowed = false;
}
	:	(declaration | expressionList )?
		-> ^(FOR_INIT declaration? expressionList?)
	;

forCond
scope InOperator;
@init {
	$InOperator::allowed = true;
}
	:	expressionList?
		-> ^(FOR_CONDITION expressionList?)
	;

forIter
scope InOperator;
@init {
	$InOperator::allowed = true;
}
	:	expressionList?
		-> ^(FOR_ITERATOR expressionList?)
	;

whileStatement
	:	WHILE^ conditionList statement
	;

doWhileStatement
	:	DO^ statement WHILE! conditionList semi
	;

withStatement
	:	WITH^ condition statement
	;

defaultXMLNamespaceStatement
	:	DEFAULT XML NAMESPACE ASSIGN expression semi
		-> ^(DEFAULT_XML_NAMESPACE expression)
	;

typeExpression
	:	c=COLON
		// TODO: identifier allows namespaces, and I suspect a type:spec shouldn't
		typeIdent LNOT? QUESTION? -> typeIdent
	;	
	
typeIdent
	:
		i=identifier -> ^(TYPE_SPEC $i)		
		|
		vectorExpression
		|
		(VOID | STAR | FUNCTION) -> ^(TYPE_SPEC VOID? STAR? FUNCTION?)
	;

identifier 
	:	( 	qualifiedIdent -> qualifiedIdent
		)
		(	options{greedy=true;}
		: 	poi=propOrIdent[root_0, retval.start] -> $poi
		)*
		-> ^(IDENTIFIER $identifier)
	;

propertyIdentifier
	:	STAR
	|	ident
	;

qualifier
	:	( propertyIdentifier | reservedNamespace )
	;

simpleQualifiedIdentifier
	:	propertyIdentifier
	|	qualifier DBL_COLON^
		(	propertyIdentifier
		| 	brackets
		)
	;

expressionQualifiedIdentifier
	:	encapsulatedExpression DBL_COLON^
		(	propertyIdentifier
		|	brackets
		)
	;

nonAttributeQualifiedIdentifier
	:	simpleQualifiedIdentifier
	|	expressionQualifiedIdentifier
	;

qualifiedIdentifier
	:	e4xAttributeIdentifier
	|	nonAttributeQualifiedIdentifier
	;

qualifiedIdent
	:	(namespaceName DBL_COLON^)? 
		{
			Token token = input.LT(1);
	                if (token.getType() == STAR) {                    
                	    token.setType(IDENT);
	                }
		}
		 ident
	;

namespaceName
	:	IDENT | reservedNamespace
	;

reservedNamespace
	:	PUBLIC
	|	PRIVATE
	|	PROTECTED
	|	INTERNAL
	;

identifierStar
	:	ident
		(	options{greedy=true;}
		:	DOT ident
		)* 
		(	DOT STAR
		)?
		-> ^(IDENTIFIER_STAR ident+ STAR?)
	;

annotations
	:	annotation*		
		-> ^(ANNOTATIONS annotation*)
	;
	
annotationsNotEmpty
	:	annotation+		
		-> ^(ANNOTATIONS annotation+)
	;

annotation
	:	
		LBRACK
		ident
		annotationParamList?
		RBRACK
		SEMI?
		-> ^(ANNOTATION ident annotationParamList?)
	;

annotationParamList
	:
		LPAREN
		(	annotationParam
			(COMMA annotationParam)*
		)?
		RPAREN
		-> ^(ANNOTATION_PARAMS annotationParam*)
	;

/*
[Inspectable(name="Icon Offset", verbose = 1,type=Boolean, defaultValue=true)]
[Inspectable(defaultValue="circular")]
[Bindable]
[ChangeEvent("event")]
[Inspectable("danger", 1, true, maybe)] 
[InspectableList("flavorStr","colorStr")]
[Event("click")]
[Collection (name="name", variable="varname", collectionClass="mx.utils.CollectionImpl", collectionItem="coll-item-classname", identifier="string")] 
*/

annotationParam
	:
		ident ASSIGN constant -> ^(ASSIGN ident constant)
	|	constant -> constant
	|	ident -> ident
	;

modifiers
	:	modifier*
		-> ^(MODIFIERS modifier*)
	;

modifier
	:	namespaceName
	|	NATIVE
	|	VIRTUAL	
	|	STATIC
	|	FINAL
	|	ENUMERABLE
	|	'explicit'
	|	OVERRIDE
	|	DYNAMIC
	|	'intrinsic'
	;

arguments
scope InOperator;
@init {
	$InOperator::allowed = true;
}
	:	LPAREN expressionList RPAREN
		-> ^(ARGUMENTS expressionList)
	|	LPAREN RPAREN
		-> ARGUMENTS
	;
// This is an initializer used to set up an array.
arrayLiteral
	:	LBRACK elementList? RBRACK
		-> ^(ARRAY_LITERAL elementList?)
	;
			
elementList
scope InOperator;
@init {
	$InOperator::allowed = true;
}
	:	 
	{
	if (input.LA(1)==COMMA) {
		adaptor.addChild(root_0, new LinkedListTree(new CommonToken(EMPTY_LIST_ITEM)));
	}
	}
	assignmentExpression? (COMMA!
	{
	if (input.LA(1)==COMMA||input.LA(1)==RBRACK) {
		adaptor.addChild(root_0, new LinkedListTree(new CommonToken(EMPTY_LIST_ITEM)));
	}
	}
	assignmentExpression?)* 
	;
	
/*		
elementList
	:	COMMA!
	|	nonemptyElementList
	;
nonemptyElementList
scope InOperator;
@init {
	$InOperator::allowed = true;
}
	:	assignmentExpression (COMMA! assignmentExpression)*
	;
*/

element
scope InOperator;
@init {
	$InOperator::allowed = true;
}
	:	assignmentExpression
	;

// This is an initializer used to set up an object.
objectLiteral
	:	LCURLY {lexer.objectLiteralFieldName = true;} fieldList? {lexer.objectLiteralFieldName = false;} RCURLY
		-> ^(OBJECT_LITERAL fieldList?)
	;
	
fieldList
	:	literalField (COMMA! literalField?)*
	;
	
literalField 
	: 	fieldName COLON {lexer.objectLiteralFieldName = false;} element {lexer.objectLiteralFieldName = true;}
		-> ^(OBJECT_FIELD fieldName element)
	;
	
fieldName 
	:	ident
	|	number
	|	STRING_LITERAL
	;

// same as expressionList, but ends with EOF
expressionOnly
	:	expressionList
		EOF!
	;

// the mother of all expressions
expression
scope InOperator;
@init {
	$InOperator::allowed = true;
}
	:	assignmentExpression
	;

// This is a list of expressions.
expressionList
	:	assignmentExpression (COMMA assignmentExpression)*
		-> assignmentExpression+
	;

// assignment expression (level 13)
assignmentExpression
	:	conditionalExpression
	(	(assignmentOperator)=> o=assignmentOperator^
		assignmentExpression
		{demarcate($o.tree);}
	)*
	;

assignmentOperator
options { k = 5 }
	:	ASSIGN
	| 	STAR ASSIGN -> STAR_ASSIGN
	|	DIV_ASSIGN
	|	MOD_ASSIGN
	|	PLUS_ASSIGN
	|	MINUS_ASSIGN
	|	SL_ASSIGN
	|	GT GT GT ASSIGN -> BSR_ASSIGN
	|	GT GT ASSIGN -> SR_ASSIGN
	|	GT ASSIGN -> GE
	|	GT GT GT -> BSR
	|	GT GT -> SR
	|	BAND_ASSIGN
	|	BXOR_ASSIGN
	|	BOR_ASSIGN
	|	LAND_ASSIGN
	|	LOR_ASSIGN
	;

// conditional test (level 12)
conditionalExpression
	:	(logicalOrExpression -> logicalOrExpression)
		(
			QUESTION
			conditionalSubExpression
			-> ^(QUESTION $conditionalExpression conditionalSubExpression)
		)?
	;
conditionalSubExpression
	:	assignmentExpression COLON! assignmentExpression
	;

// TODO: should 'and'/'or' have same precidence as '&&'/'||' ?

// logical or (||)  (level 11)
logicalOrExpression
	:	logicalAndExpression
		(o=logicalOrOperator^ logicalAndExpression {demarcate($o.tree);})*
	;

logicalOrOperator
	:	LOR
	;

// logical and (&&)  (level 10)
logicalAndExpression
	:	bitwiseOrExpression
		(o=logicalAndOperator^ bitwiseOrExpression {demarcate($o.tree);})*
	;

logicalAndOperator
	:	LAND
	;

// bitwise or non-short-circuiting or (|)  (level 9)
bitwiseOrExpression
	:	bitwiseXorExpression
		(o=BOR^ bitwiseXorExpression {demarcate($o.tree);})*
	;

// exclusive or (^)  (level 8)
bitwiseXorExpression
	:	bitwiseAndExpression
		(o=BXOR^ bitwiseAndExpression {demarcate($o.tree);})*
	;

// bitwise or non-short-circuiting and (&)  (level 7)
bitwiseAndExpression
	:	equalityExpression
		(o=BAND^ equalityExpression {demarcate($o.tree);})*
	;

// equality/inequality (==/!=) (level 6)
equalityExpression
	:	relationalExpression
	(	o=equalityOperator^
		relationalExpression
		{demarcate($o.tree);}
	)*
	;

equalityOperator
	:	STRICT_EQUAL | STRICT_NOT_EQUAL | NOT_EQUAL | EQUAL
	;
	
// boolean relational expressions (level 5)
relationalExpression
	:	shiftExpression
		((relationalOperator)=> o=relationalOperator^ shiftExpression {demarcate($o.tree);})*
	;

relationalOperator
	:	{$InOperator::allowed}? IN
	|	LT | GT | LE | GE | IS | AS | INSTANCEOF
	;

// bit shift expressions (level 4)
shiftExpression
	:	additiveExpression
		(o=shiftOperator^ additiveExpression {demarcate($o.tree);})*
	;

shiftOperator
	:	SL | SR | BSR
	;

// binary addition/subtraction (level 3)
additiveExpression
	:	multiplicativeExpression
		(o=additiveOperator^ multiplicativeExpression {demarcate($o.tree);})*
	;

additiveOperator
	:	PLUS | MINUS
	;

// multiplication/division/modulo (level 2)
multiplicativeExpression
	:	unaryExpression
		(	o=multiplicativeOperator^
			unaryExpression
			{demarcate($o.tree);}
		)*
	;

multiplicativeOperator
	:	s=STAR -> MULT[$s]
	|	DIV
	|	MOD
	;

//	(level 1)
unaryExpression
	:	in=INC unaryExpression -> ^(PRE_INC[$in] unaryExpression)
	|	de=DEC unaryExpression -> ^(PRE_DEC[$de] unaryExpression)
	|	MINUS unaryExpression -> ^(UNARY_MINUS unaryExpression)
	|	PLUS unaryExpression -> ^(UNARY_PLUS unaryExpression)
	|	unaryExpressionNotPlusMinus
	;

unaryExpressionNotPlusMinus
	:	DELETE postfixExpression -> ^(DELETE postfixExpression)
	|	VOID unaryExpression -> ^(VOID unaryExpression)
	|	TYPEOF unaryExpression -> ^(TYPEOF unaryExpression)
	|	LNOT unaryExpression -> ^(LNOT unaryExpression)
	|	BNOT unaryExpression -> ^(BNOT unaryExpression)
	|	postfixExpression
	;

// qualified names, array expressions, method invocation, post inc/dec
postfixExpression
	:	(
		
	
		primaryExpression -> primaryExpression)
			(	poi=propOrIdent[root_0, retval.start] -> $poi
			|	(LBRACK expression rb=RBRACK -> ^(ARRAY_ACC $postfixExpression expression)) {demarcate(root_0.getFirstChild()); root_0.getFirstChild().setStopToken((LinkedListToken)$rb);}
			|	(DOT LPAREN expressionList RPAREN -> ^(E4X_FILTER $postfixExpression expressionList)) {demarcate(root_0.getFirstChild());}
			|	(d=DOT e4xAttributeIdentifier -> ^(PROPERTY_OR_IDENTIFIER[$d] $postfixExpression e4xAttributeIdentifier)) {demarcate(root_0.getFirstChild());}
			|	(d=DOT STAR -> ^(PROPERTY_OR_IDENTIFIER[$d] $postfixExpression STAR)) {demarcate(root_0.getFirstChild());}
			|	(arguments -> ^(METHOD_CALL $postfixExpression arguments)) {demarcate(root_0.getFirstChild());}
			)*
		
		{ if (input.LA(1) == INC || input.LA(1) == DEC) promoteEOL(null); }

		( 	in=INC -> ^(POST_INC[$in] $postfixExpression)
	 	|	de=DEC -> ^(POST_DEC[$de] $postfixExpression)
		)?
 	;


e4xAttributeIdentifier
	:	E4X_ATTRI
		(	qualifiedIdent -> ^(E4X_ATTRI_PROPERTY qualifiedIdent)
		|	STAR -> ^(E4X_ATTRI_STAR)
		|	LBRACK expression RBRACK -> ^(E4X_ATTRI_EXPR expression)
		)
	;

primaryExpression
	:	UNDEFINED
	|	SUPER
	|	constant
	|	arrayLiteral
	|	objectLiteral
	|	functionExpression
	|	newExpression
	|	encapsulatedExpression
	|	e4xAttributeIdentifier
	|	qualifiedIdent
	|	vectorExpression
	;

vectorExpression
	:
		VECTOR^ genericType
	;

genericType
	:
		LT! typeIdent GT!
	;

//identPrimary
//	:	(qualifiedIdent -> qualifiedIdent)
//		(	options{greedy=true;}
//			/* yuk! tree building doesn't set the start/stop values
//			   we need without some hand-holding, */
//		:	(	poi=propOrIdent[root_0, retval.start] -> $poi
//			)
//		|	(	LBRACK expr=expression RBRACK
//				-> ^(ARRAY_ACC $identPrimary $expr)
//			)
//		)*
//		(	options {greedy=true;}
//		:	a=arguments
//			-> ^(METHOD_CALL $identPrimary $a)
//		)*
//    ;

propOrIdent[LinkedListTree identPrimary, Token startToken]
	:	
		{ retval.start = startToken; }
		d=DOT propId1=qualifiedIdent -> ^(PROPERTY_OR_IDENTIFIER[$d] {$identPrimary} $propId1)
		|
		d=E4X_DESC propId2=qualifiedIdentifier
		/* without further semantic analysis, we can't
		   tell if a.b is an access of the property 'b'
		   from the var 'a' or a reference to the type
		   'b' in the package 'a'.  (This could be
		   resolved in an AST post-processing step) */
		-> ^(PROPERTY_OR_IDENTIFIER[$d] {$identPrimary} $propId2)
	;

constant
	:	xmlLiteral
	|	regexpLiteral
	|	number
	|	STRING_LITERAL
	|	TRUE
	|	FALSE
	|	INFINITY
	|	NAN
	|	NULL
	;

number	:	HEX_LITERAL
	|	DECIMAL_LITERAL
	|	OCTAL_LITERAL
	|	FLOAT_LITERAL
	;

	
xmlLiteral
	@init {
		LinkedListTree xml = null;
	}
	:	// We have to have the LT in the outer grammar for lookahead
		// in AS3Parser to be able to predict that the xmlLiteral rule
		// should be used.
		LT
		{ xml=parseXMLLiteral(); }
		-> ^(XML_LITERAL {xml})
	;

regexpLiteral
	@init {
		LinkedListTree regexp = null;
	}
	:	s=DIV { regexp=parseRegexpLiteral((LinkedListToken)$s); }
		-> ^(REGEXP_LITERAL {regexp})
	;

newExpression
	:	NEW 
		(
		(LT) => genericType  ((LBRACK) => arrayLiteral)? -> ^(VECTOR_LITERAL genericType arrayLiteral?)
		|
		fullNewSubexpression ((LPAREN) => arguments)? -> ^(NEW fullNewSubexpression arguments?)
		)	
	;

fullNewSubexpression
	:	(	
			primaryExpression -> primaryExpression
		)
		(	(DOT)=> d=DOT qualifiedIdent -> ^(PROPERTY_OR_IDENTIFIER[$d] $fullNewSubexpression qualifiedIdent)
//		|	(LBRACK)=> brackets -> ^(ARRAY_ACC $fullNewSubexpression brackets)
		|	(LBRACK)=> LBRACK expressionList RBRACK -> ^(ARRAY_ACC $fullNewSubexpression expressionList)
		)*
	;

propertyOperator
	:	DOT^ qualifiedIdent
	|	brackets
	;

brackets
@init {
	$InOperator::allowed = true;
}
	:	LBRACK expressionList RBRACK
	;

encapsulatedExpression
scope InOperator;
@init {
	$InOperator::allowed = true;
}
	:	LPAREN expressionList RPAREN
		-> ^(ENCPS_EXPR expressionList)
	;

functionSignature
	:	parameterDeclarationList typeExpression?
	;

// TODO: block should be optional here,
functionCommon
	:	functionSignature block
	;

functionExpression
	:	FUNCTION IDENT? functionCommon
		-> ^(FUNC_DEF IDENT? functionCommon)
	;
	
localFunction
	:	FUNCTION IDENT functionCommon
		-> ^(FUNC_DEF IDENT functionCommon)
	;


ident
	:	IDENT
	|	i=USE -> IDENT[$i]
	|	i=XML -> IDENT[$i]
	|	i=DYNAMIC -> IDENT[$i]
	|	i=NAMESPACE -> IDENT[$i]
	|	i=IS -> IDENT[$i]
	|	i=AS -> IDENT[$i]
	|	i=GET -> IDENT[$i]
	|	i=SET -> IDENT[$i]
	|	i=NAN -> IDENT[$i]
	|	i=INTERNAL -> IDENT[$i]
	|	i=OVERRIDE -> IDENT[$i]
	|	i=STATIC -> IDENT[$i]
	|	i=NATIVE -> IDENT[$i]
	|	i=VIRTUAL -> IDENT[$i]
	|	i=EACH -> IDENT[$i]
	|	i=INFINITY -> IDENT[$i]
	|	i=UNDEFINED -> IDENT[$i]
	|	i=FINAL -> IDENT[$i]
	|	i=ENUMERABLE -> IDENT[$i]
	;


VECTOR		:	'Vector.';
PACKAGE		:	{!objectLiteralFieldName}?=> 'package';
PUBLIC		:	{!objectLiteralFieldName}?=> 'public';
PRIVATE		:	{!objectLiteralFieldName}?=> 'private';
PROTECTED	:	{!objectLiteralFieldName}?=> 'protected';
INTERNAL	:	{!objectLiteralFieldName}?=> 'internal';
OVERRIDE	:	{!objectLiteralFieldName}?=> 'override';
FUNCTION	:	{!objectLiteralFieldName}?=> 'function';
EXTENDS		:	{!objectLiteralFieldName}?=> 'extends';
IMPLEMENTS	:	{!objectLiteralFieldName}?=> 'implements';
VAR		:	{!objectLiteralFieldName}?=> 'var';
STATIC		:	{!objectLiteralFieldName}?=> 'static';
NATIVE		:	{!objectLiteralFieldName}?=> 'native';
VIRTUAL		:	{!objectLiteralFieldName}?=> 'virtual';
IF		:	{!objectLiteralFieldName}?=> 'if';
IMPORT		:	{!objectLiteralFieldName}?=> 'import';
FOR		:	{!objectLiteralFieldName}?=> 'for';
EACH		:	{!objectLiteralFieldName}?=> 'each';
IN		:	{!objectLiteralFieldName}?=> 'in';
WHILE		:	{!objectLiteralFieldName}?=> 'while';
DO		:	{!objectLiteralFieldName}?=> 'do';
SWITCH		:	{!objectLiteralFieldName}?=> 'switch';
CASE		:	{!objectLiteralFieldName}?=> 'case';
DEFAULT		:	{!objectLiteralFieldName}?=> 'default';
ELSE		:	{!objectLiteralFieldName}?=> 'else';
CONST		:	{!objectLiteralFieldName}?=> 'const';
CLASS		:	{!objectLiteralFieldName}?=> 'class';
INTERFACE	:	{!objectLiteralFieldName}?=> 'interface';
TRUE		:	{!objectLiteralFieldName}?=> 'true';
FALSE		:	{!objectLiteralFieldName}?=> 'false';
DYNAMIC		:	{!objectLiteralFieldName}?=> 'dynamic';
USE		:	{!objectLiteralFieldName}?=> 'use';
XML		:	{!objectLiteralFieldName}?=> 'xml';
NAMESPACE	:	{!objectLiteralFieldName}?=> 'namespace';
IS		:	{!objectLiteralFieldName}?=> 'is';
AS		:	{!objectLiteralFieldName}?=> 'as';
GET		:	{!objectLiteralFieldName}?=> 'get';
SET		:	{!objectLiteralFieldName}?=> 'set';
WITH		:	{!objectLiteralFieldName}?=> 'with';
RETURN		:	{!objectLiteralFieldName}?=> 'return';
CONTINUE	:	{!objectLiteralFieldName}?=> 'continue';
BREAK		:	{!objectLiteralFieldName}?=> 'break';
NAN		:	{!objectLiteralFieldName}?=> 'NaN';
INFINITY	:	{!objectLiteralFieldName}?=> 'Infinity';
NULL		:	{!objectLiteralFieldName}?=> 'null';
NEW		:	{!objectLiteralFieldName}?=> 'new';
SUPER		:	{!objectLiteralFieldName}?=> 'super';
INSTANCEOF	:	{!objectLiteralFieldName}?=> 'instanceof';
DELETE		:	{!objectLiteralFieldName}?=> 'delete';
VOID		:	{!objectLiteralFieldName}?=> 'void';
TYPEOF		:	{!objectLiteralFieldName}?=> 'typeof';
TRY		:	{!objectLiteralFieldName}?=> 'try';
CATCH		:	{!objectLiteralFieldName}?=> 'catch';
FINALLY		:	{!objectLiteralFieldName}?=> 'finally';
UNDEFINED	:	{!objectLiteralFieldName}?=> 'undefined';
THROW		:	{!objectLiteralFieldName}?=> 'throw';
FINAL		:	{!objectLiteralFieldName}?=> 'final';
ENUMERABLE	:	{!objectLiteralFieldName}?=> 'enumerable';
INCLUDE		:	{!objectLiteralFieldName}?=> 'include';

// OPERATORS
QUESTION		:	'?'	;
LPAREN			:	'('	;
RPAREN			:	')'	;
LBRACK			:	'['	;
RBRACK			:	']'	;
LCURLY			:	'{'	;
RCURLY			:	'}'	;
COLON			:	':'	;
DBL_COLON		:	'::'	;
COMMA			:	','	;
ASSIGN			:	'='	;
EQUAL			:	'=='	;
STRICT_EQUAL		:	'==='	;
LNOT			:	'!'	;
BNOT			:	'~'	;
NOT_EQUAL		:	'!='	;
STRICT_NOT_EQUAL	:	'!=='	;
DIV			:	'/'	;
DIV_ASSIGN		:	'/='	;
PLUS			:	'+'	;
PLUS_ASSIGN		:	'+='	;
INC			:	'++'	;
MINUS			:	'-'	;
MINUS_ASSIGN		:	'-='	;
DEC			:	'--'	;
STAR			:	'*'	;
MOD			:	'%'	;
MOD_ASSIGN		:	'%='	;
GT			:	'>'	;
SL			:	'<<'	;
SL_ASSIGN		:	'<<='	;
LE			:	'<='	;
LT			:	'<'	;
BXOR			:	'^'	;
BXOR_ASSIGN		:	'^='	;
BOR			:	'|'	;
BOR_ASSIGN		:	'|='	;
LOR			:	'||'	;
BAND			:	'&'	;
BAND_ASSIGN		:	'&='	;
LAND			:	'&&'	;
LAND_ASSIGN		:	'&&='	;
LOR_ASSIGN		:	'||='	;
E4X_ATTRI		:	'@'	; 
SEMI			:	';'	;


DOT		:	'.'	;
E4X_DESC	:	'..'	;
REST		:	'...'	;

IDENT
	:	UNICODE_IDENTIFIER_START
		UNICODE_IDENTIFIER_PART*
	;

// The UNICODE_IDENTIFIER_START and UNICODE_IDENTIFIER_PART rules where machine-
// generated by testing all characters from \0000 to \ffff using the methods
// of the UCharacter class from the ICU framework.

fragment UNICODE_IDENTIFIER_START
	:	'\u0041'..'\u005a' | '\u005f' | '\u0061'..'\u007a' |
		'\u00aa' | '\u00b5' | '\u00ba' | '\u00c0'..'\u00d6' |
		'\u00d8'..'\u00f6' | '\u00f8'..'\u02c1' | '\u02c6'..'\u02d1' |
		'\u02e0'..'\u02e4' | '\u02ee' | '\u037a'..'\u037d' | '\u0386' |
		'\u0388'..'\u038a' | '\u038c' | '\u038e'..'\u03a1' |
		'\u03a3'..'\u03ce' | '\u03d0'..'\u03f5' | '\u03f7'..'\u0481' |
		'\u048a'..'\u0513' | '\u0531'..'\u0556' | '\u0559' |
		'\u0561'..'\u0587' | '\u05d0'..'\u05ea' | '\u05f0'..'\u05f2' |
		'\u0621'..'\u063a' | '\u0640'..'\u064a' | '\u066e'..'\u066f' |
		'\u0671'..'\u06d3' | '\u06d5' | '\u06e5'..'\u06e6' |
		'\u06ee'..'\u06ef' | '\u06fa'..'\u06fc' | '\u06ff' | '\u0710' |
		'\u0712'..'\u072f' | '\u074d'..'\u076d' | '\u0780'..'\u07a5' |
		'\u07b1' | '\u07ca'..'\u07ea' | '\u07f4'..'\u07f5' | '\u07fa' |
		'\u0904'..'\u0939' | '\u093d' | '\u0950' | '\u0958'..'\u0961' |
		'\u097b'..'\u097f' | '\u0985'..'\u098c' | '\u098f'..'\u0990' |
		'\u0993'..'\u09a8' | '\u09aa'..'\u09b0' | '\u09b2' |
		'\u09b6'..'\u09b9' | '\u09bd' | '\u09ce' | '\u09dc'..'\u09dd' |
		'\u09df'..'\u09e1' | '\u09f0'..'\u09f1' | '\u0a05'..'\u0a0a' |
		'\u0a0f'..'\u0a10' | '\u0a13'..'\u0a28' | '\u0a2a'..'\u0a30' |
		'\u0a32'..'\u0a33' | '\u0a35'..'\u0a36' | '\u0a38'..'\u0a39' |
		'\u0a59'..'\u0a5c' | '\u0a5e' | '\u0a72'..'\u0a74' |
		'\u0a85'..'\u0a8d' | '\u0a8f'..'\u0a91' | '\u0a93'..'\u0aa8' |
		'\u0aaa'..'\u0ab0' | '\u0ab2'..'\u0ab3' | '\u0ab5'..'\u0ab9' |
		'\u0abd' | '\u0ad0' | '\u0ae0'..'\u0ae1' | '\u0b05'..'\u0b0c' |
		'\u0b0f'..'\u0b10' | '\u0b13'..'\u0b28' | '\u0b2a'..'\u0b30' |
		'\u0b32'..'\u0b33' | '\u0b35'..'\u0b39' | '\u0b3d' |
		'\u0b5c'..'\u0b5d' | '\u0b5f'..'\u0b61' | '\u0b71' | '\u0b83' |
		'\u0b85'..'\u0b8a' | '\u0b8e'..'\u0b90' | '\u0b92'..'\u0b95' |
		'\u0b99'..'\u0b9a' | '\u0b9c' | '\u0b9e'..'\u0b9f' |
		'\u0ba3'..'\u0ba4' | '\u0ba8'..'\u0baa' | '\u0bae'..'\u0bb9' |
		'\u0c05'..'\u0c0c' | '\u0c0e'..'\u0c10' | '\u0c12'..'\u0c28' |
		'\u0c2a'..'\u0c33' | '\u0c35'..'\u0c39' | '\u0c60'..'\u0c61' |
		'\u0c85'..'\u0c8c' | '\u0c8e'..'\u0c90' | '\u0c92'..'\u0ca8' |
		'\u0caa'..'\u0cb3' | '\u0cb5'..'\u0cb9' | '\u0cbd' | '\u0cde' |
		'\u0ce0'..'\u0ce1' | '\u0d05'..'\u0d0c' | '\u0d0e'..'\u0d10' |
		'\u0d12'..'\u0d28' | '\u0d2a'..'\u0d39' | '\u0d60'..'\u0d61' |
		'\u0d85'..'\u0d96' | '\u0d9a'..'\u0db1' | '\u0db3'..'\u0dbb' |
		'\u0dbd' | '\u0dc0'..'\u0dc6' | '\u0e01'..'\u0e30' |
		'\u0e32'..'\u0e33' | '\u0e40'..'\u0e46' | '\u0e81'..'\u0e82' |
		'\u0e84' | '\u0e87'..'\u0e88' | '\u0e8a' | '\u0e8d' |
		'\u0e94'..'\u0e97' | '\u0e99'..'\u0e9f' | '\u0ea1'..'\u0ea3' |
		'\u0ea5' | '\u0ea7' | '\u0eaa'..'\u0eab' | '\u0ead'..'\u0eb0' |
		'\u0eb2'..'\u0eb3' | '\u0ebd' | '\u0ec0'..'\u0ec4' | '\u0ec6' |
		'\u0edc'..'\u0edd' | '\u0f00' | '\u0f40'..'\u0f47' |
		'\u0f49'..'\u0f6a' | '\u0f88'..'\u0f8b' | '\u1000'..'\u1021' |
		'\u1023'..'\u1027' | '\u1029'..'\u102a' | '\u1050'..'\u1055' |
		'\u10a0'..'\u10c5' | '\u10d0'..'\u10fa' | '\u10fc' |
		'\u1100'..'\u1159' | '\u115f'..'\u11a2' | '\u11a8'..'\u11f9' |
		'\u1200'..'\u1248' | '\u124a'..'\u124d' | '\u1250'..'\u1256' |
		'\u1258' | '\u125a'..'\u125d' | '\u1260'..'\u1288' |
		'\u128a'..'\u128d' | '\u1290'..'\u12b0' | '\u12b2'..'\u12b5' |
		'\u12b8'..'\u12be' | '\u12c0' | '\u12c2'..'\u12c5' |
		'\u12c8'..'\u12d6' | '\u12d8'..'\u1310' | '\u1312'..'\u1315' |
		'\u1318'..'\u135a' | '\u1380'..'\u138f' | '\u13a0'..'\u13f4' |
		'\u1401'..'\u166c' | '\u166f'..'\u1676' | '\u1681'..'\u169a' |
		'\u16a0'..'\u16ea' | '\u16ee'..'\u16f0' | '\u1700'..'\u170c' |
		'\u170e'..'\u1711' | '\u1720'..'\u1731' | '\u1740'..'\u1751' |
		'\u1760'..'\u176c' | '\u176e'..'\u1770' | '\u1780'..'\u17b3' |
		'\u17d7' | '\u17dc' | '\u1820'..'\u1877' | '\u1880'..'\u18a8' |
		'\u1900'..'\u191c' | '\u1950'..'\u196d' | '\u1970'..'\u1974' |
		'\u1980'..'\u19a9' | '\u19c1'..'\u19c7' | '\u1a00'..'\u1a16' |
		'\u1b05'..'\u1b33' | '\u1b45'..'\u1b4b' | '\u1d00'..'\u1dbf' |
		'\u1e00'..'\u1e9b' | '\u1ea0'..'\u1ef9' | '\u1f00'..'\u1f15' |
		'\u1f18'..'\u1f1d' | '\u1f20'..'\u1f45' | '\u1f48'..'\u1f4d' |
		'\u1f50'..'\u1f57' | '\u1f59' | '\u1f5b' | '\u1f5d' |
		'\u1f5f'..'\u1f7d' | '\u1f80'..'\u1fb4' | '\u1fb6'..'\u1fbc' |
		'\u1fbe' | '\u1fc2'..'\u1fc4' | '\u1fc6'..'\u1fcc' |
		'\u1fd0'..'\u1fd3' | '\u1fd6'..'\u1fdb' | '\u1fe0'..'\u1fec' |
		'\u1ff2'..'\u1ff4' | '\u1ff6'..'\u1ffc' | '\u2071' | '\u207f' |
		'\u2090'..'\u2094' | '\u2102' | '\u2107' | '\u210a'..'\u2113' |
		'\u2115' | '\u2119'..'\u211d' | '\u2124' | '\u2126' | '\u2128'
		| '\u212a'..'\u212d' | '\u212f'..'\u2139' | '\u213c'..'\u213f'
		| '\u2145'..'\u2149' | '\u214e' | '\u2160'..'\u2184' |
		'\u2c00'..'\u2c2e' | '\u2c30'..'\u2c5e' | '\u2c60'..'\u2c6c' |
		'\u2c74'..'\u2c77' | '\u2c80'..'\u2ce4' | '\u2d00'..'\u2d25' |
		'\u2d30'..'\u2d65' | '\u2d6f' | '\u2d80'..'\u2d96' |
		'\u2da0'..'\u2da6' | '\u2da8'..'\u2dae' | '\u2db0'..'\u2db6' |
		'\u2db8'..'\u2dbe' | '\u2dc0'..'\u2dc6' | '\u2dc8'..'\u2dce' |
		'\u2dd0'..'\u2dd6' | '\u2dd8'..'\u2dde' | '\u3005'..'\u3007' |
		'\u3021'..'\u3029' | '\u3031'..'\u3035' | '\u3038'..'\u303c' |
		'\u3041'..'\u3096' | '\u309d'..'\u309f' | '\u30a1'..'\u30fa' |
		'\u30fc'..'\u30ff' | '\u3105'..'\u312c' | '\u3131'..'\u318e' |
		'\u31a0'..'\u31b7' | '\u31f0'..'\u31ff' | '\u3400'..'\u4db5' |
		'\u4e00'..'\u9fbb' | '\ua000'..'\ua48c' | '\ua717'..'\ua71a' |
		'\ua800'..'\ua801' | '\ua803'..'\ua805' | '\ua807'..'\ua80a' |
		'\ua80c'..'\ua822' | '\ua840'..'\ua873' | '\uac00'..'\ud7a3' |
		'\uf900'..'\ufa2d' | '\ufa30'..'\ufa6a' | '\ufa70'..'\ufad9' |
		'\ufb00'..'\ufb06' | '\ufb13'..'\ufb17' | '\ufb1d' |
		'\ufb1f'..'\ufb28' | '\ufb2a'..'\ufb36' | '\ufb38'..'\ufb3c' |
		'\ufb3e' | '\ufb40'..'\ufb41' | '\ufb43'..'\ufb44' |
		'\ufb46'..'\ufbb1' | '\ufbd3'..'\ufd3d' | '\ufd50'..'\ufd8f' |
		'\ufd92'..'\ufdc7' | '\ufdf0'..'\ufdfb' | '\ufe70'..'\ufe74' |
		'\ufe76'..'\ufefc' | '\uff21'..'\uff3a' | '\uff41'..'\uff5a' |
		'\uff66'..'\uffbe' | '\uffc2'..'\uffc7' | '\uffca'..'\uffcf' |
		'\uffd2'..'\uffd7' | '\uffda'..'\uffdc' | '$'
	;
fragment UNICODE_IDENTIFIER_PART
	:	'\u0000'..'\u0008' | '\u000e'..'\u001b' |
		'\u0030'..'\u0039' | '\u0041'..'\u005a' | '\u005f' |
		'\u0061'..'\u007a' | '\u007f'..'\u009f' | '\u00aa' | '\u00ad' |
		'\u00b5' | '\u00ba' | '\u00c0'..'\u00d6' | '\u00d8'..'\u00f6' |
		'\u00f8'..'\u02c1' | '\u02c6'..'\u02d1' | '\u02e0'..'\u02e4' |
		'\u02ee' | '\u0300'..'\u036f' | '\u037a'..'\u037d' | '\u0386' |
		'\u0388'..'\u038a' | '\u038c' | '\u038e'..'\u03a1' |
		'\u03a3'..'\u03ce' | '\u03d0'..'\u03f5' | '\u03f7'..'\u0481' |
		'\u0483'..'\u0486' | '\u048a'..'\u0513' | '\u0531'..'\u0556' |
		'\u0559' | '\u0561'..'\u0587' | '\u0591'..'\u05bd' | '\u05bf' |
		'\u05c1'..'\u05c2' | '\u05c4'..'\u05c5' | '\u05c7' |
		'\u05d0'..'\u05ea' | '\u05f0'..'\u05f2' | '\u0600'..'\u0603' |
		'\u0610'..'\u0615' | '\u0621'..'\u063a' | '\u0640'..'\u065e' |
		'\u0660'..'\u0669' | '\u066e'..'\u06d3' | '\u06d5'..'\u06dd' |
		'\u06df'..'\u06e8' | '\u06ea'..'\u06fc' | '\u06ff' |
		'\u070f'..'\u074a' | '\u074d'..'\u076d' | '\u0780'..'\u07b1' |
		'\u07c0'..'\u07f5' | '\u07fa' | '\u0901'..'\u0939' |
		'\u093c'..'\u094d' | '\u0950'..'\u0954' | '\u0958'..'\u0963' |
		'\u0966'..'\u096f' | '\u097b'..'\u097f' | '\u0981'..'\u0983' |
		'\u0985'..'\u098c' | '\u098f'..'\u0990' | '\u0993'..'\u09a8' |
		'\u09aa'..'\u09b0' | '\u09b2' | '\u09b6'..'\u09b9' |
		'\u09bc'..'\u09c4' | '\u09c7'..'\u09c8' | '\u09cb'..'\u09ce' |
		'\u09d7' | '\u09dc'..'\u09dd' | '\u09df'..'\u09e3' |
		'\u09e6'..'\u09f1' | '\u0a01'..'\u0a03' | '\u0a05'..'\u0a0a' |
		'\u0a0f'..'\u0a10' | '\u0a13'..'\u0a28' | '\u0a2a'..'\u0a30' |
		'\u0a32'..'\u0a33' | '\u0a35'..'\u0a36' | '\u0a38'..'\u0a39' |
		'\u0a3c' | '\u0a3e'..'\u0a42' | '\u0a47'..'\u0a48' |
		'\u0a4b'..'\u0a4d' | '\u0a59'..'\u0a5c' | '\u0a5e' |
		'\u0a66'..'\u0a74' | '\u0a81'..'\u0a83' | '\u0a85'..'\u0a8d' |
		'\u0a8f'..'\u0a91' | '\u0a93'..'\u0aa8' | '\u0aaa'..'\u0ab0' |
		'\u0ab2'..'\u0ab3' | '\u0ab5'..'\u0ab9' | '\u0abc'..'\u0ac5' |
		'\u0ac7'..'\u0ac9' | '\u0acb'..'\u0acd' | '\u0ad0' |
		'\u0ae0'..'\u0ae3' | '\u0ae6'..'\u0aef' | '\u0b01'..'\u0b03' |
		'\u0b05'..'\u0b0c' | '\u0b0f'..'\u0b10' | '\u0b13'..'\u0b28' |
		'\u0b2a'..'\u0b30' | '\u0b32'..'\u0b33' | '\u0b35'..'\u0b39' |
		'\u0b3c'..'\u0b43' | '\u0b47'..'\u0b48' | '\u0b4b'..'\u0b4d' |
		'\u0b56'..'\u0b57' | '\u0b5c'..'\u0b5d' | '\u0b5f'..'\u0b61' |
		'\u0b66'..'\u0b6f' | '\u0b71' | '\u0b82'..'\u0b83' |
		'\u0b85'..'\u0b8a' | '\u0b8e'..'\u0b90' | '\u0b92'..'\u0b95' |
		'\u0b99'..'\u0b9a' | '\u0b9c' | '\u0b9e'..'\u0b9f' |
		'\u0ba3'..'\u0ba4' | '\u0ba8'..'\u0baa' | '\u0bae'..'\u0bb9' |
		'\u0bbe'..'\u0bc2' | '\u0bc6'..'\u0bc8' | '\u0bca'..'\u0bcd' |
		'\u0bd7' | '\u0be6'..'\u0bef' | '\u0c01'..'\u0c03' |
		'\u0c05'..'\u0c0c' | '\u0c0e'..'\u0c10' | '\u0c12'..'\u0c28' |
		'\u0c2a'..'\u0c33' | '\u0c35'..'\u0c39' | '\u0c3e'..'\u0c44' |
		'\u0c46'..'\u0c48' | '\u0c4a'..'\u0c4d' | '\u0c55'..'\u0c56' |
		'\u0c60'..'\u0c61' | '\u0c66'..'\u0c6f' | '\u0c82'..'\u0c83' |
		'\u0c85'..'\u0c8c' | '\u0c8e'..'\u0c90' | '\u0c92'..'\u0ca8' |
		'\u0caa'..'\u0cb3' | '\u0cb5'..'\u0cb9' | '\u0cbc'..'\u0cc4' |
		'\u0cc6'..'\u0cc8' | '\u0cca'..'\u0ccd' | '\u0cd5'..'\u0cd6' |
		'\u0cde' | '\u0ce0'..'\u0ce3' | '\u0ce6'..'\u0cef' |
		'\u0d02'..'\u0d03' | '\u0d05'..'\u0d0c' | '\u0d0e'..'\u0d10' |
		'\u0d12'..'\u0d28' | '\u0d2a'..'\u0d39' | '\u0d3e'..'\u0d43' |
		'\u0d46'..'\u0d48' | '\u0d4a'..'\u0d4d' | '\u0d57' |
		'\u0d60'..'\u0d61' | '\u0d66'..'\u0d6f' | '\u0d82'..'\u0d83' |
		'\u0d85'..'\u0d96' | '\u0d9a'..'\u0db1' | '\u0db3'..'\u0dbb' |
		'\u0dbd' | '\u0dc0'..'\u0dc6' | '\u0dca' | '\u0dcf'..'\u0dd4' |
		'\u0dd6' | '\u0dd8'..'\u0ddf' | '\u0df2'..'\u0df3' |
		'\u0e01'..'\u0e3a' | '\u0e40'..'\u0e4e' | '\u0e50'..'\u0e59' |
		'\u0e81'..'\u0e82' | '\u0e84' | '\u0e87'..'\u0e88' | '\u0e8a' |
		'\u0e8d' | '\u0e94'..'\u0e97' | '\u0e99'..'\u0e9f' |
		'\u0ea1'..'\u0ea3' | '\u0ea5' | '\u0ea7' | '\u0eaa'..'\u0eab' |
		'\u0ead'..'\u0eb9' | '\u0ebb'..'\u0ebd' | '\u0ec0'..'\u0ec4' |
		'\u0ec6' | '\u0ec8'..'\u0ecd' | '\u0ed0'..'\u0ed9' |
		'\u0edc'..'\u0edd' | '\u0f00' | '\u0f18'..'\u0f19' |
		'\u0f20'..'\u0f29' | '\u0f35' | '\u0f37' | '\u0f39' |
		'\u0f3e'..'\u0f47' | '\u0f49'..'\u0f6a' | '\u0f71'..'\u0f84' |
		'\u0f86'..'\u0f8b' | '\u0f90'..'\u0f97' | '\u0f99'..'\u0fbc' |
		'\u0fc6' | '\u1000'..'\u1021' | '\u1023'..'\u1027' |
		'\u1029'..'\u102a' | '\u102c'..'\u1032' | '\u1036'..'\u1039' |
		'\u1040'..'\u1049' | '\u1050'..'\u1059' | '\u10a0'..'\u10c5' |
		'\u10d0'..'\u10fa' | '\u10fc' | '\u1100'..'\u1159' |
		'\u115f'..'\u11a2' | '\u11a8'..'\u11f9' | '\u1200'..'\u1248' |
		'\u124a'..'\u124d' | '\u1250'..'\u1256' | '\u1258' |
		'\u125a'..'\u125d' | '\u1260'..'\u1288' | '\u128a'..'\u128d' |
		'\u1290'..'\u12b0' | '\u12b2'..'\u12b5' | '\u12b8'..'\u12be' |
		'\u12c0' | '\u12c2'..'\u12c5' | '\u12c8'..'\u12d6' |
		'\u12d8'..'\u1310' | '\u1312'..'\u1315' | '\u1318'..'\u135a' |
		'\u135f' | '\u1380'..'\u138f' | '\u13a0'..'\u13f4' |
		'\u1401'..'\u166c' | '\u166f'..'\u1676' | '\u1681'..'\u169a' |
		'\u16a0'..'\u16ea' | '\u16ee'..'\u16f0' | '\u1700'..'\u170c' |
		'\u170e'..'\u1714' | '\u1720'..'\u1734' | '\u1740'..'\u1753' |
		'\u1760'..'\u176c' | '\u176e'..'\u1770' | '\u1772'..'\u1773' |
		'\u1780'..'\u17d3' | '\u17d7' | '\u17dc'..'\u17dd' |
		'\u17e0'..'\u17e9' | '\u180b'..'\u180d' | '\u1810'..'\u1819' |
		'\u1820'..'\u1877' | '\u1880'..'\u18a9' | '\u1900'..'\u191c' |
		'\u1920'..'\u192b' | '\u1930'..'\u193b' | '\u1946'..'\u196d' |
		'\u1970'..'\u1974' | '\u1980'..'\u19a9' | '\u19b0'..'\u19c9' |
		'\u19d0'..'\u19d9' | '\u1a00'..'\u1a1b' | '\u1b00'..'\u1b4b' |
		'\u1b50'..'\u1b59' | '\u1b6b'..'\u1b73' | '\u1d00'..'\u1dca' |
		'\u1dfe'..'\u1e9b' | '\u1ea0'..'\u1ef9' | '\u1f00'..'\u1f15' |
		'\u1f18'..'\u1f1d' | '\u1f20'..'\u1f45' | '\u1f48'..'\u1f4d' |
		'\u1f50'..'\u1f57' | '\u1f59' | '\u1f5b' | '\u1f5d' |
		'\u1f5f'..'\u1f7d' | '\u1f80'..'\u1fb4' | '\u1fb6'..'\u1fbc' |
		'\u1fbe' | '\u1fc2'..'\u1fc4' | '\u1fc6'..'\u1fcc' |
		'\u1fd0'..'\u1fd3' | '\u1fd6'..'\u1fdb' | '\u1fe0'..'\u1fec' |
		'\u1ff2'..'\u1ff4' | '\u1ff6'..'\u1ffc' | '\u200b'..'\u200f' |
		'\u202a'..'\u202e' | '\u203f'..'\u2040' | '\u2054' |
		'\u2060'..'\u2063' | '\u206a'..'\u206f' | '\u2071' | '\u207f' |
		'\u2090'..'\u2094' | '\u20d0'..'\u20dc' | '\u20e1' |
		'\u20e5'..'\u20ef' | '\u2102' | '\u2107' | '\u210a'..'\u2113' |
		'\u2115' | '\u2119'..'\u211d' | '\u2124' | '\u2126' | '\u2128'
		| '\u212a'..'\u212d' | '\u212f'..'\u2139' | '\u213c'..'\u213f'
		| '\u2145'..'\u2149' | '\u214e' | '\u2160'..'\u2184' |
		'\u2c00'..'\u2c2e' | '\u2c30'..'\u2c5e' | '\u2c60'..'\u2c6c' |
		'\u2c74'..'\u2c77' | '\u2c80'..'\u2ce4' | '\u2d00'..'\u2d25' |
		'\u2d30'..'\u2d65' | '\u2d6f' | '\u2d80'..'\u2d96' |
		'\u2da0'..'\u2da6' | '\u2da8'..'\u2dae' | '\u2db0'..'\u2db6' |
		'\u2db8'..'\u2dbe' | '\u2dc0'..'\u2dc6' | '\u2dc8'..'\u2dce' |
		'\u2dd0'..'\u2dd6' | '\u2dd8'..'\u2dde' | '\u3005'..'\u3007' |
		'\u3021'..'\u302f' | '\u3031'..'\u3035' | '\u3038'..'\u303c' |
		'\u3041'..'\u3096' | '\u3099'..'\u309a' | '\u309d'..'\u309f' |
		'\u30a1'..'\u30fa' | '\u30fc'..'\u30ff' | '\u3105'..'\u312c' |
		'\u3131'..'\u318e' | '\u31a0'..'\u31b7' | '\u31f0'..'\u31ff' |
		'\u3400'..'\u4db5' | '\u4e00'..'\u9fbb' | '\ua000'..'\ua48c' |
		'\ua717'..'\ua71a' | '\ua800'..'\ua827' | '\ua840'..'\ua873' |
		'\uac00'..'\ud7a3' | '\uf900'..'\ufa2d' | '\ufa30'..'\ufa6a' |
		'\ufa70'..'\ufad9' | '\ufb00'..'\ufb06' | '\ufb13'..'\ufb17' |
		'\ufb1d'..'\ufb28' | '\ufb2a'..'\ufb36' | '\ufb38'..'\ufb3c' |
		'\ufb3e' | '\ufb40'..'\ufb41' | '\ufb43'..'\ufb44' |
		'\ufb46'..'\ufbb1' | '\ufbd3'..'\ufd3d' | '\ufd50'..'\ufd8f' |
		'\ufd92'..'\ufdc7' | '\ufdf0'..'\ufdfb' | '\ufe00'..'\ufe0f' |
		'\ufe20'..'\ufe23' | '\ufe33'..'\ufe34' | '\ufe4d'..'\ufe4f' |
		'\ufe70'..'\ufe74' | '\ufe76'..'\ufefc' | '\ufeff' |
		'\uff10'..'\uff19' | '\uff21'..'\uff3a' | '\uff3f' |
		'\uff41'..'\uff5a' | '\uff66'..'\uffbe' | '\uffc2'..'\uffc7' |
		'\uffca'..'\uffcf' | '\uffd2'..'\uffd7' | '\uffda'..'\uffdc' |
		'\ufff9'..'\ufffb' | '$'
	;
	
/*
STRING_LITERAL
	:	'"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"'
	|	'\'' (ESC|~('\''|'\\'|'\n'|'\r'))* '\''
	;
*/

STRING_LITERAL
	:	'"' (~('"'|'\\')|ESC)* '"'
	|	'\'' (~('\''|'\\')|ESC)* '\''
	;
	
HEX_LITERAL	:	'0' ('x'|'X') HEX_DIGIT+ ;

DECIMAL_LITERAL	:	('0' | '1'..'9' '0'..'9'*) ;

OCTAL_LITERAL	:	'0' ('0'..'7')+ ;

FLOAT_LITERAL
    :   ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
    |   '.' ('0'..'9')+ EXPONENT?
    |   ('0'..'9')+ EXPONENT?
	;

WS
    :   (('\u0020'|'\u0009'|'\u000B'|'\u000C'|'\u00AC'|'\u00A0'|'\u2020'|'\u0412')|('\u001C'..'\u001F'))+  { $channel = HIDDEN; }
    ;        

NL	
	: ( ( '\r' '\n'? ) | '\n' | '\u2028' | '\u2029' ) { $channel = HIDDEN; }
	;
	
// skip BOM bytes
BOM	:	(	'\u00EF'  '\u00BB' '\u00BF'
		|	'\uFEFF'
		)
		{ $channel=HIDDEN; };

// might be better to filter this out as a preprocessing step
INCLUDE_DIRECTIVE
	:	'#include'
	;

// single-line comments
SL_COMMENT
	:	'//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)?
		{$channel=HIDDEN;}
	;
// multiple-line comments
ML_COMMENT
	:	'/*' ( options {greedy=false;} : . )* '*/'
		{$channel=HIDDEN;}
	;

fragment EXPONENT
	:	('e'|'E') ('+'|'-')? ('0'..'9')+
	;
fragment HEX_DIGIT
	:	('0'..'9'|'A'..'F'|'a'..'f')
	;

fragment OCT_DIGIT
	:	'0'..'7'
	;
	
fragment ESC
	: '\\' ('\u0000'..'\uFFFF')
	;	
	
/*	
fragment ESC
	:   CTRLCHAR_ESC
	|   UNICODE_ESC
	|   OCTAL_ESC
	;

fragment CTRLCHAR_ESC
	:	'\\' ('v'|'x'|'b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
	;

fragment OCTAL_ESC
	:   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
	|   '\\' ('0'..'7') ('0'..'7')
	|   '\\' ('0'..'7')
	;

fragment UNICODE_ESC
	:   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
	;
*/