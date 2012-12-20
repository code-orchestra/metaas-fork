/*
 * E4X.g
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

grammar E4X;

options {
	k = 2;
	output=AST;
	ASTLabelType=LinkedListTree;
}

tokens {
	XML_EMPTY_ELEMENT;
	XML_ELEMENT;
	XML_ATTRIBUTE;
	XML_LIST;
	XML_EXPRESSION;
	XML_TEXT_NODE;
	XML_PI;
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

// see http://www.ecma-international.org/publications/standards/Ecma-357.htm


xmlMarkup
	:	XML_COMMENT
	|	XML_CDATA
	|	xmlProcessingInstruction
	;


xmlPrimary
	:	
	xmlListInitialiser
	|		xmlInitialiser
	;

xmlInitialiser
	:	xmlMarkup
	|	xmlElement
	;

xmlElement
	:	(XML_LCHEVRON xmlTagContent XML_WS? -> xmlTagContent)
		(
			'/>'
			-> ^(XML_EMPTY_ELEMENT $xmlElement)

		|	 '>' xmlElementContent* '</' xmlTagName XML_WS? '>'
			-> ^(XML_ELEMENT $xmlElement xmlTagName xmlElementContent *)
		)
	;

xmlTagContent
	:	xmlTagName xmlAttributes?
	;

xmlEmbeddedExpression
	:	'{' expression? '}' -> ^(XML_EXPRESSION expression?)
	;

// TODO: embed the as3 parser here!

expression
	:	(
			xmlText
		|	xmlAttributeValue
		)+
	;

xmlText
	:	XML_TEXT | XML_ASSIGN | XML_APOSTROPHE | XML_QUOTE | XML_NAME | XML_WS
	;

xmlTagName
	:	xmlEmbeddedExpression
	|	XML_NAME
	;

xmlAttributes
	:	(
			xmlAttribute
		)+
	;

xmlAttribute
	:	XML_WS xmlAttributeName XML_WS? XML_ASSIGN XML_WS? xmlAttributeValue
		-> ^(XML_ATTRIBUTE xmlAttributeName xmlAttributeValue)
	;

xmlAttributeName
	:	xmlEmbeddedExpression
	|	XML_NAME
	;

xmlAttributeValue
	:	xmlEmbeddedExpression
	|	XML_APOSTROPHE! (~(XML_LCHEVRON | XML_APOSTROPHE))* XML_APOSTROPHE!
	|	XML_QUOTE! (~(XML_LCHEVRON | XML_QUOTE))* XML_QUOTE!
	;

xmlElementContent
	:	xmlEmbeddedExpression
	|	xmlMarkup
	|	xmlText -> ^(XML_TEXT_NODE xmlText)
	|	xmlElement
	;

xmlListInitialiser
	:	XML_LCHEVRON '>' xmlElementContent* '</>'
		-> ^(XML_LIST xmlElementContent*)
	;
	
xmlProcessingInstruction
	:	XML_PI_START xmlTagContent XML_PI_END -> ^(XML_PI xmlTagContent)
	;

XML_LCHEVRON		:	'<';

XML_ASSIGN		:	'=';

XML_WS			:	(' ' | '\t' | '\n' | '\r')+;

XML_NAME		:	XML_NAME_START XML_NAME_PART*;


//XML_ATTRIBUTE_VALUE
//	:	'\'' ( options {greedy=false;} : . )* '\''
//	|	'"' ( options {greedy=false;} : . )* '"'
//	;

XML_APOSTROPHE		: '\'';

XML_QUOTE		: '"';

XML_PI_START		: '<?';
XML_PI_END		: '?>';

// TODO: exclude the sequence '--' from being allowed,
XML_COMMENT		:	'<!--' ( options {greedy=false;} : . )* '-->';

XML_CDATA		:	'<![CDATA[' ( options {greedy=false;} : . )* ']]>';

fragment XML_NAME_START	:	UNICODE_LETTER | '_' | ':';
fragment XML_NAME_PART	:	UNICODE_LETTER | UNICODE_DIGIT | '.' | '-' | '_' | ':';

// TODO: and the rest of unicode?
fragment UNICODE_LETTER	:	'a'..'z' | 'A'..'Z';
fragment UNICODE_DIGIT	:	'0'..'9';

XML_TEXT
	:	(~(XML_LCHEVRON | '{'))
	;
