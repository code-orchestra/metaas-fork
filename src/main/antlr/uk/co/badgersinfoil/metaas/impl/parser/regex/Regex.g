/*
 * Regex.g
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

// FIXME: this is supposed to be a full regular expression grammar, but it's
// not finished yet.

grammar E4X;

options {
	output=AST;
	ASTLabelType=LinkedListTree;
}

@parser::header {
package uk.co.badgersinfoil.metaas.impl.parser.e4x;

import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
}

@lexer::header {
package uk.co.badgersinfoil.metaas.impl.parser.e4x;
}

// disable standard error handling; be strict
@rulecatch { }

@parser::members {
	// disable standard error handling; be strict
	protected void mismatch(IntStream input, int ttype, BitSet follow)
		throws RecognitionException
	{
		throw new MismatchedTokenException(ttype, input);
	}

	private E4XLexer lexer;
	private CharStream cs;

	public void setInput(E4XLexer lexer, CharStream cs) {
		this.lexer = lexer;
		this.cs = cs;
	}

	/**
	 * Returns the input left unconsumed after the last parse operation.
	 * Because of lookahead in the parser, there is no guarantee that the
	 * lexer has not consumed input ahead of the current parse-point for
	 * any abritrary rule. This method is only intended to grab the
	 * remaining input after recognising 'xmlPrimary'.
	 */
	public String getInputTail() {
		return cs.substring(cs.index()-1, cs.size()-1);
	}
}

// see http://www.mozilla.org/js/language/es4/formal/lexer-grammar.html#N-RegExpLiteral


regexpLiteral
	:	regexpBody regexpFlags
	;

	
regexpBody
	:	disjunction
	;

disjunction
	:	term (ALT term)*
	;

term
	:	assertion
	|	atom quantifier?
	;

quantifier
	:	quantifierPrefix HOOK?
	;

quantifierPrefix
	:	STAR
	|	PLUS
	|	HOOK
	|	LBRACE DECIMAL_DIGITS (COMMA DECIMAL_DIGITS?)? RBRACE
	;

assertion
	:	HAT
	|	DOLAR
	|	ESC_LOWER_B
	|	ESC_UPPER_B
	;

atom
	:	PATTERN_CHARACTER
	|	DOT
	|	NULL_ESCAPE
	|	ATOM_ESCAPE
	|	characterClass
	|	grouping
	;

grouping
	:	LPAREN
		(	HOOK
			(COLON | EQUALS | BANG)?
		)?
		disjunction
		RPAREN
	;

characterClass
	:	LBRACK HAT? classRangeDash* RBRACK
	;

classRangeDash
	:	classAtomDash
	|	classAtomDash
	;


ALT	:	'|';
HOOK	:	'?';
STAR	:	'*';
PLUS	:	'+';
LBRACE	:	'{';
RBRACE	:	'}';
COMMA	:	',';
HAT	:	'^';
DOLAR	:	'$';
DOT	:	'.';
LPAREN	:	'(';
RPAREN	:	')';
COLON	:	':';
EQUALS	:	'=';
BANG	:	'!';
LBRACK	:	'[';
RBRACK	:	']';

DECIMAL_DIGITS
	:	DECIMAL_DIGIT+;

fragment DECIMAL_DIGIT
	:	'0'..'9';

ESC_LOWER_B
	:	'\\' 'b';
ESC_UPPER_B
	:	'\\' 'B';
NULL_ESCAPE
	:	'\\' '_';

ATOM_ESCAPE
	:	'\\'
		(	DECIMAL_DIGITS
		|	CHARACTER_ESCAPE
		|	CHARACTER_CLASS_ESCAPE
		)
	;

fragmenet CHARACTER_ESCAPE
	:	CONTROL_ESCAPE
	|	'c' CONTROL_LETTER
	|	HEX_ESCAPE
	|	IDENTITY_ESCAPE
	;

fragment CONTROL_LETTER
	:	'a'..'z' | 'A'..'Z';

fragment IDENTITY_ESCAPE
	:	~'_';

fragment CONTROL_ESCAPE
	:	'f'
	|	'n'
	|	'r'
	|	't'
	|	'v'
	;

fragment HEX_ESCAPE
	:	'x' HEX_DIGIT HEX_DIGIT
	|	'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
	;

fragment HEX_DIGIT
	: 	DECIMAL_DIGIT | 'a'..'f' | 'A'..'F';

fragment CHARACTER_CLASS_ESCAPE
	:	's'
	|	'S'
	|	'd'
	|	'D'
	|	'w'
	|	'W'
	;

PATTERN_CHARACTER
	:	~('^' | '$' | '\' | '.' | '*' | '+' | '?' | '(' | ')' | '[' | ']' | '{' | '}' | '|');