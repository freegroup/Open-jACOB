/*
     This file is part of Open-jACOB
     Copyright (C) 2005-2006 Tarragon GmbH
  
     This program is free software; you can redistribute it and/or modify
     it under the terms of the GNU General Public License as published by
     the Free Software Foundation; version 2 of the License.
  
     This program is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     GNU General Public License for more details.
  
     You should have received a copy of the GNU General Public License     
     along with this program; if not, write to the Free Software
     Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
     USA
*/

package de.tif.jacob.core.definition.impl.aliascondition;

import java_cup.runtime.*;

%%
	
%public
%class AliasConditionScanner
%implements AliasConditionSym

%unicode

%line
%column

%cup

%{
  static public final transient String RCS_ID = "$Id: aliascondition.flex,v 1.4 2009/12/14 12:00:43 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";
  
  private StringBuffer string = new StringBuffer();
  
  private Symbol symbol(int type) {
    return new AliasConditionSymbol(type, yyline+1, yycolumn+1);
  }

  private Symbol symbol(int type, Object value) {
    return new AliasConditionSymbol(type, yyline+1, yycolumn+1, value);
  }

  /* assumes correct representation of a long value for 
     specified radix in String s */
  private long parseLong(String s, int radix) {
    int  max = s.length();
    long result = 0;
    long digit;

    for (int i = 0; i < max; i++) {
      digit  = Character.digit(yy_buffer[i],radix);
      result*= radix;
      result+= digit;
    }

    return result;
  }
%}

/* main character classes */
LineTerminator = \r|\n|\r\n

WhiteSpace = {LineTerminator} | [ \t\f]

/* identifiers */
Identifier = [:jletter:][:jletterdigit:]*

/* numeric literals */
IntegerLiteral = 0 | [1-9][0-9]*
DecimalLiteral = {FLit1}|{FLit2}

FLit1    = [0-9]+ \. [0-9]* 
FLit2    = \. [0-9]+ 

/* string and character literals */
StringCharacter = [^\r\n\']

/* case insensitive keywords */
AND = [aA][nN][dD]
OR = [oO][rR]
IN = [iI][nN]
NOT = [nN][oO][tT]
IS = [iI][sS]
NULL = [nN][uU][lL][lL]
BETWEEN = [bB][eE][tT][wW][eE][eE][nN]
LIKE = [lL][iI][kK][eE]

/* combined operators */
NOT_IN = {NOT} {WhiteSpace}+ {IN}
NOT_BETWEEN = {NOT} {WhiteSpace}+ {BETWEEN}

/* property name */
PropertyCharacter = [:jletterdigit:]|[.]
Property = [:jletter:] {PropertyCharacter}*

AnyCharacter = [^]
%state STRINGLITERAL, PROPERTY_STATE, PROPERTY_STATE_QUOTED

%%

<YYINITIAL> {

  /* keywords */
  {AND}                          { return symbol(AND); }
  {OR}                           { return symbol(OR); }
  {IN}                           { return symbol(IN); }
  {NOT}                          { return symbol(NOT); }
  {NOT_IN}                       { return symbol(NOT_IN); }
  {IS}                           { return symbol(IS); }
  {NULL}                         { return symbol(NULL); }
  {BETWEEN}                      { return symbol(BETWEEN); }
  {NOT_BETWEEN}                  { return symbol(NOT_BETWEEN); }
  {LIKE}                         { return symbol(LIKE); }
  
  /* placeholders */
  "{MANDATORID}"                 { return symbol(MANDATORID); }
  "'{MANDATORID}'"               { return symbol(QUOTED_MANDATORID); }
  "{USERID}"                     { return symbol(USERID); }
  "'{USERID}'"                   { return symbol(QUOTED_USERID); }
  "{LOGINID}"                    { return symbol(LOGINID); }
  "'{LOGINID}'"                  { return symbol(LOGINID); }
  "{ROLES}"                      { return symbol(ROLES); }
  "{APPLICATIONNAME}"            { return symbol(APPLICATIONNAME); }
  "'{APPLICATIONNAME}'"          { return symbol(APPLICATIONNAME); }
  "{USERPROPERTY."               { yybegin(PROPERTY_STATE); return symbol(USERPROPERTY); }
  "'{USERPROPERTY."              { yybegin(PROPERTY_STATE_QUOTED); return symbol(QUOTED_USERPROPERTY); }
  
  /* separators */
  "("                            { return symbol(LPAREN); }
  ")"                            { return symbol(RPAREN); }
  ","                            { return symbol(COMMA); }
  "."                            { return symbol(DOT); }
  
  /* alias outer join markers */
  "["                            { return symbol(LBRACK); }
  "]"                            { return symbol(RBRACK); }
  
  /* operators */
  "="                            { return symbol(EQ); }
  "<>"                           { return symbol(NOTEQ); }  // database syntax
  "!="                           { return symbol(NOTEQ); }  // C-like syntax (used by Quintus)
  ">"                            { return symbol(GREATER_THAN); }
  "<"                            { return symbol(LESS_THAN); }
  ">="                           { return symbol(GREATER_OR_EQ_THAN); }
  "<="                           { return symbol(LESS_OR_EQ_THAN); }
  
  "*"                            { return symbol(MULT); }
  "/"                            { return symbol(DIV); }
  "+"                            { return symbol(PLUS); }
  "-"                            { return symbol(MINUS); }
  
  /* string literal */
  \'                             { yybegin(STRINGLITERAL); string.setLength(0); string.append('\''); }

  /* numeric literals */
  {IntegerLiteral}               { return symbol(LONG_LITERAL, new Long(yytext())); }  
  {DecimalLiteral}               { return symbol(DECIMAL_LITERAL, new java.math.BigDecimal(yytext())); }
  
  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }

  /* identifiers */ 
  {Identifier}                   { return symbol(IDENTIFIER, yytext()); }  
}

<STRINGLITERAL> {
  \'                             { yybegin(YYINITIAL); string.append('\''); return symbol(QUOTED_STRING_LITERAL, string.toString()); }
  
  {StringCharacter}+             { string.append(yytext()); }
  
  /* escape sequences */
  "''"                           { string.append("''"); }
  
  /* error cases */
  \\.                            { throw new RuntimeException("Illegal escape sequence \""+yytext()+"\""); }
  {LineTerminator}               { throw new RuntimeException("Unterminated string at end of line"); }
}

<PROPERTY_STATE> {
  "}"                            { yybegin(YYINITIAL); }
  {Property}                     { return symbol(PROPERTY, yytext()); }  
  {AnyCharacter}                 { throw new RuntimeException("Illegal character \""+yytext()+ "\" at column "+(yycolumn+1)); }
}

<PROPERTY_STATE_QUOTED> {
  "}'"                           { yybegin(YYINITIAL); }
  {Property}                     { return symbol(PROPERTY, yytext()); }  
  {AnyCharacter}                 { throw new RuntimeException("Illegal character \""+yytext()+ "\" at column "+(yycolumn+1)); }
}

/* error fallback */
.|\n                             { throw new RuntimeException("Illegal character \""+yytext()+
                                                              "\" at line "+(yyline+1)+", column "+(yycolumn+1)); }
<<EOF>>                          { return symbol(EOF); }