/*
 * Javadoc.g
 * 
 * Copyright (c) 2007 David Holroyd
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

grammar Javadoc;


options {
	k = 4;
	output=AST;
	ASTLabelType=LinkedListTree;
}

tokens {
	JAVADOC;
	INLINE_TAG;
	DESCRIPTION;
	PARA_TAG;
	TEXT_LINE;
}

@parser::header {
package uk.co.badgersinfoil.metaas.impl.parser.javadoc;

import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.TokenBuilder;
}

@lexer::header {
package uk.co.badgersinfoil.metaas.impl.parser.javadoc;
}

// disable standard error handling; be strict
@rulecatch { }

@parser::members {
	protected void mismatch(IntStream input, int ttype, BitSet follow)
		throws RecognitionException
	{
		throw new MismatchedTokenException(ttype, input);
	}

	private void placeholder(LinkedListTree imaginary) {
		if (imaginary.getChildCount() > 0) {
			return;
		}

		LinkedListToken tok = (LinkedListToken)input.LT(1);
		LinkedListToken placeholder = TokenBuilder.newPlaceholder(imaginary);
		tok.beforeInsert(placeholder);
	}

}
@lexer::members {
	protected void mismatch(IntStream input, int ttype, BitSet follow)
		throws RecognitionException
	{
		throw new MismatchedTokenException(ttype, input);
	}
}


comment_body
	:	d=description {placeholder($d.tree);}
		paragraph_tag*
		EOF
		-> ^(JAVADOC description paragraph_tag*)
	;

description
	:	text_line*
		-> ^(DESCRIPTION text_line*)
	;

text_line
	:	text_line_start
		text_line_content*
		(NL | EOF!)
	|	NL
	;

text_line_start
	:	(LBRACE ATWORD)=> inline_tag
	|	WORD | STARS | WS | LBRACE | RBRACE | AT
	;

text_line_content
	:	(LBRACE ATWORD)=> inline_tag
	|	WORD | STARS | WS | LBRACE | RBRACE | AT | ATWORD
	;

inline_tag
	:	LBRACE ATWORD inline_tag_content* RBRACE
		-> ^(INLINE_TAG ATWORD inline_tag_content*)
	;

inline_tag_content
	:	WORD | STARS | WS | AT | NL
	;

paragraph_tag
	:	ATWORD paragraph_tag_tail
		-> ^(PARA_TAG ATWORD paragraph_tag_tail)
	;

paragraph_tag_tail
	:	text_line_content*
		(	NL text_line*
		|	EOF
		)
		-> text_line_content* NL? text_line*
	;

STARS:		'*'+;

LBRACE:		'{';
RBRACE:		'}';
AT:		'@';

WS:		(' ' | '\t')+;

NL
options {
	k=*;
}
	:		('\r\n' | '\r' | '\n') WS? (STARS WS?)?;

ATWORD:		'@' WORD WORD_TAIL;

WORD:		~('\n' | ' ' | '\r' | '\t' | '{' | '}' | '@')
		WORD_TAIL;

fragment
WORD_TAIL:	(~('\n' | ' ' | '\r' | '\t' | '{' | '}'))*;
