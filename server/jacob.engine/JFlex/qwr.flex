/*
     This file is part of jACOB
     Copyright (C) 2005-2009 Tarragon GmbH
  
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

package de.tif.qes.report;

import java_cup.runtime.*;

%%
	
%public
%class QWRScanner
%implements QWRSym

%unicode
	
%line
%column

%cup

%{
  static public final transient String RCS_ID = "$Id: qwr.flex,v 1.1 2009-11-25 08:17:39 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  private final StringBuffer string = new StringBuffer();
  
  private boolean multiline = false;
  
  private Symbol symbol(int type) {
    return new QWRSymbol(type, yyline+1, yycolumn+1);
  }

  private Symbol symbol(int type, Object value) {
    return new QWRSymbol(type, yyline+1, yycolumn+1, value);
  }
  
  private static String adjustDecimal(String s) {
    if (s.startsWith("+")) {
      return s.substring(1);
    }

    return s;
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
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment = "/*" [^*] ~"*/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/**" ~"*/"

/* identifiers */
Identifier = [:jletter:][:jletterdigit:]*

/* integer literals */
DecIntegerLiteral = 0 | [+-]? [1-9] [0-9]*
DecLongLiteral    = {DecIntegerLiteral} [lL]

HexIntegerLiteral = 0 [xX] 0* {HexDigit} {1,8}
HexLongLiteral    = 0 [xX] 0* {HexDigit} {1,16} [lL]
HexDigit          = [0-9a-fA-F]

OctIntegerLiteral = 0+ [1-3]? {OctDigit} {1,15}
OctLongLiteral    = 0+ 1? {OctDigit} {1,21} [lL]
OctDigit          = [0-7]
    
/* floating point literals */        
FloatLiteral  = ({FLit1}|{FLit2}|{FLit3}) {Exponent}? [fF]
DoubleLiteral = ({FLit1}|{FLit2}|{FLit3}) {Exponent}?

FLit1    = [0-9]+ \. [0-9]* 
FLit2    = \. [0-9]+ 
FLit3    = [0-9]+ 
Exponent = [eE] [+-]? [0-9]+

/* string and character literals */
StringCharacter = [^\r\n\"\\]

%state STRINGLITERAL

%%

<YYINITIAL> {

  /* keywords */
  "ANCHOR"                       { return symbol(ANCHOR); }
  "ASCENDING"                    { return symbol(ASCENDING); }
  "ASCII"                        { return symbol(ASCII); }
  "CENTER"                       { return symbol(CENTER); }
  "COLOR"                        { return symbol(COLOR); }	// together with HTML
  "COLUMNNAME"                   { return symbol(COLUMNNAME); }	// together with FIELDSET ANCHOR, FIELDSET LABEL ?
  "CONSTRAINT"                   { return symbol(CONSTRAINT); }
  "DATABASE"                     { return symbol(DATABASE); }
  "DEFINE"                       { return symbol(DEFINE); }
  "DESCENDING"                   { return symbol(DESCENDING); }  // AS
  "DESCRIPTION"                  { return symbol(DESCRIPTION); }
  "DESTINATION"                  { return symbol(DESTINATION); }
  "EPILOGUE"                     { return symbol(EPILOGUE); }
  "FIELD"                        { return symbol(FIELD); }
  "FIELDSET"                     { return symbol(FIELDSET); }
  "FILE"                         { return symbol(FILE); }
  "FONT"                         { return symbol(FONT); }
  "FONTINFO"                     { return symbol(FONTINFO); }
  "FOOTER"                       { return symbol(FOOTER); }
  "FOR"                          { return symbol(FOR); }
  "FUNCTION"                     { return symbol(FUNCTION); }
  "GROUPED"                      { return symbol(GROUPED); }
  "HEADER"                       { return symbol(HEADER); }
  "HEIGHT"                       { return symbol(HEIGHT); }
  "HIDDEN"                       { return symbol(HIDDEN); }
  "HTML"                         { return symbol(HTML); }
  "JUSTIFICATION"                { return symbol(JUSTIFICATION); }
  "KEY"                          { return symbol(KEY); }
  "LABEL"                        { return symbol(LABEL); }
  "LEFT"                         { return symbol(LEFT); }
  "LINEBREAK"                    { return symbol(LINEBREAK); }
  "NONKEY"                       { return symbol(NONKEY); }
  "ON"                           { return symbol(ON); }
  "OUTPUTFORMAT"                 { return symbol(OUTPUTFORMAT); }
  "PAGE"                         { return symbol(PAGE); }
  "PRINTER"                      { return symbol(PRINTER); }
  "PROLOGUE"                     { return symbol(PROLOGUE); }
  "RECORD"                       { return symbol(RECORD); }
  "RELATION"                     { return symbol(RELATION); }
  "REPORT"                       { return symbol(REPORT); }
  "RIGHT"                        { return symbol(RIGHT); }  // AS
  "SCREEN"                       { return symbol(SCREEN); }
  "SET"                          { return symbol(SET); }
  "SORT"                         { return symbol(SORT); }
  "SPACE_BEFORE"                 { return symbol(SPACE_BEFORE); }
  "SPACING"                      { return symbol(SPACING); }
  "STRUCTURED"                   { return symbol(STRUCTURED); }
  "SUBSTRING"                    { return symbol(SUBSTRING); }
  "TABLE"                        { return symbol(TABLE); }
  "TABULAR"                      { return symbol(TABULAR); }
  "TRUNCATION_MARK"              { return symbol(TRUNCATION_MARK); }
  "TYPE"                         { return symbol(TYPE); }
  "USING"                        { return symbol(USING); }
  "VALUE_OF"                     { return symbol(VALUE_OF); }
  "VERSION"                      { return symbol(VERSION); }
  "WIDTH"                        { return symbol(WIDTH); }
    
  /* separators */
  "("                            { return symbol(LPAREN); }
  ")"                            { return symbol(RPAREN); }
  "{"                            { return symbol(LBRACE); }
  "}"                            { return symbol(RBRACE); }
  ";"                            { return symbol(SEMICOLON); }
  ","                            { return symbol(COMMA); }
  
  /* string literal */
  \"                             { yybegin(STRINGLITERAL); multiline = false; string.setLength(0); }

  /* numeric literals */

  {DecIntegerLiteral}            { return symbol(INTEGER_LITERAL, new Integer(adjustDecimal(yytext()))); }
  {DecLongLiteral}               { return symbol(INTEGER_LITERAL, new Long(adjustDecimal(yytext().substring(0,yylength()-1)))); }
  
  {HexIntegerLiteral}            { return symbol(INTEGER_LITERAL, new Integer((int) parseLong(yytext().substring(2),16))); }
  {HexLongLiteral}               { return symbol(INTEGER_LITERAL, new Long(parseLong(yytext().substring(2,yylength()-1),16))); }
 
  {OctIntegerLiteral}            { return symbol(INTEGER_LITERAL, new Integer((int) parseLong(yytext(),8))); }  
  {OctLongLiteral}               { return symbol(INTEGER_LITERAL, new Long(parseLong(yytext().substring(0,yylength()-1),8))); }
  
  {FloatLiteral}                 { return symbol(FLOATING_POINT_LITERAL, new Double(yytext().substring(0,yylength()-1))); }
  {DoubleLiteral}                { return symbol(FLOATING_POINT_LITERAL, new Double(yytext())); }
  {DoubleLiteral}[dD]            { return symbol(FLOATING_POINT_LITERAL, new Double(yytext().substring(0,yylength()-1))); }
  
  /* comments */
  {Comment}                      { /* ignore */ }

  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }

  /* identifiers */ 
  {Identifier}                   { return symbol(IDENTIFIER, yytext()); }  
}

<STRINGLITERAL> {
  \"                             { yybegin(YYINITIAL); return symbol(multiline ? /*MULTILINE_*/ STRING_LITERAL : STRING_LITERAL, string.toString()); }
  
  {StringCharacter}+             { string.append( yytext() ); }
  
  /* escape sequences */
  \\t                            { string.append( '\t' ); }
  \\n                            { string.append( '\n' ); }
  \\r                            { string.append( '\r' ); }
  \\\"                           { string.append( '\"' ); }
  \\                             { string.append( '\\' ); }
  
  /* error cases */
  {LineTerminator}               { multiline = true; string.append( yytext() ); } // throw new RuntimeException("Unterminated string at end of line "+yyline); }
}

/* error fallback */
.|\n                             { throw new RuntimeException("Illegal character \""+yytext()+
                                                              "\" at line "+(yyline+1)+", column "+(yycolumn+1)); }
<<EOF>>                          { return symbol(EOF); }