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

package de.tif.qes.adl;

import java_cup.runtime.*;

%%
	
%public
%class ADLScanner
%implements ADLSym

%unicode
	
%line
%column

%cup

%{
  static public final transient String RCS_ID = "$Id: adl.flex,v 1.1 2006-12-21 11:35:34 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  private final StringBuffer string = new StringBuffer();
  
  private boolean multiline = false;
  
  private Symbol symbol(int type) {
    return new ADLSymbol(type, yyline+1, yycolumn+1);
  }

  private Symbol symbol(int type, Object value) {
    return new ADLSymbol(type, yyline+1, yycolumn+1, value);
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
  "VERSION"                      { return symbol(VERSION); }
  "APPVERSION"                   { return symbol(APPVERSION); }
  "SET"                          { return symbol(SET); }
  "DEFINE"                       { return symbol(DEFINE); }
  "TABLE"                        { return symbol(TABLE); }
  "WITH"                         { return symbol(WITH); }
  "DATASOURCE"                   { return symbol(DATASOURCE); }
  "SYSTEMTABLE"                  { return symbol(SYSTEMTABLE); }
  "SYSTEMTABLEKEY"               { return symbol(SYSTEMTABLEKEY); }
  "SYSTEMFIELD"                  { return symbol(SYSTEMFIELD); }
  "CACHE"                        { return symbol(CACHE); }
  "SIZE"                         { return symbol(SIZE); }
  "DESC"                         { return symbol(DESC); }
  "ORDER"                        { return symbol(ORDER); }
  "BY"                           { return symbol(BY); }
  "PRIMARY"                      { return symbol(PRIMARY); }
  "KEY"                          { return symbol(KEY); }
  "FOREIGN"                      { return symbol(FOREIGN); }
  "INDEX"                        { return symbol(INDEX); }
  "UNIQUE"                       { return symbol(UNIQUE); }
  "INFOFIELD"                    { return symbol(INFOFIELD); }
  "LABEL"                        { return symbol(LABEL); }
  "STRING"                       { return symbol(STRING); }
  "VSTRING"                      { return symbol(VSTRING); }
  "OF"                           { return symbol(OF); }
  "LENGTH"                       { return symbol(LENGTH); }
  "REQUIRED"                     { return symbol(REQUIRED); }
  "INTEGER"                      { return symbol(INTEGER); }
  "SHORT"                        { return symbol(SHORT); }
  "BYTE"                         { return symbol(BYTE); }
  "FLOAT"                        { return symbol(FLOAT); }
  "DOUBLE"                       { return symbol(DOUBLE); }
  "ENUM"                         { return symbol(ENUM); }
  "DEFAULT"                      { return symbol(DEFAULT); }
  "READONLY"                     { return symbol(READONLY); }
  "SERIAL"                       { return symbol(SERIAL); }
  "MIN"                          { return symbol(MIN); }
  "MAX"                          { return symbol(MAX); }
  "TEXT"                         { return symbol(TEXT); }
  "BINARY"                       { return symbol(BINARY); }
  "DATE"                         { return symbol(DATE); }
  "DATETIME"                     { return symbol(DATETIME); }
  "INTERVAL"                     { return symbol(INTERVAL); }
  "HISTORY"                      { return symbol(HISTORY); }
  "HISTORYFIELD"                 { return symbol(HISTORYFIELD); }
  "MONEY"                        { return symbol(MONEY); }
  "SYMBOL"                       { return symbol(SYMBOL); }
  "SCALE"                        { return symbol(SCALE); }
  "ANCHORLEFT"                   { return symbol(ANCHORLEFT); }
  "CASESENSITIVE"                { return symbol(CASESENSITIVE); }
  "TABLESET"                     { return symbol(TABLESET); }
  "SYSTEMTABLESET"               { return symbol(SYSTEMTABLESET); }
  "TABLES"                       { return symbol(TABLES); }
  "ALIAS"                        { return symbol(ALIAS); }
  "CONDITION"                    { return symbol(CONDITION); }
  "TABLERULE"                    { return symbol(TABLERULE); }
  "BEFOREUPDATE"                 { return symbol(BEFOREUPDATE); }
  "AFTERUPDATE"                  { return symbol(AFTERUPDATE); }
  "BEFORESEARCH"                 { return symbol(BEFORESEARCH); }
  "DELETE"                       { return symbol(DELETE); }
  "NEW"                          { return symbol(NEW); }
  "SCRIPT"                       { return symbol(SCRIPT); }
  "FROM"                         { return symbol(FROM); }
  "FILE"                         { return symbol(FILE); }
  "FORMULA"                      { return symbol(FORMULA); }
  "SUMMARYROW"                   { return symbol(SUMMARYROW); }
  "COLSUMMARY"                   { return symbol(COLSUMMARY); }
  "BROWSER"                      { return symbol(BROWSER); }
  "BROWSERS"                     { return symbol(BROWSERS); }
  "FOR"                          { return symbol(FOR); }
  "TYPE"                         { return symbol(TYPE); }
  "STATIC"                       { return symbol(STATIC); }
  "DYNAMIC"                      { return symbol(DYNAMIC); }
  "INFRAME"                      { return symbol(INFRAME); }
  "ASCENDING"                    { return symbol(ASCENDING); }
  "DESCENDING"                   { return symbol(DESCENDING); }
  "FILLDIRECTION"                { return symbol(FILLDIRECTION); }
  "FORWARD"                      { return symbol(FORWARD); }
  "BACKWARD"                     { return symbol(BACKWARD); }
  "RELATION"                     { return symbol(RELATION); }
  "RELATIONSET"                  { return symbol(RELATIONSET); }
  "USING"                        { return symbol(USING); }
  "USE"                          { return symbol(USE); }
  "INVISIBLE"                    { return symbol(INVISIBLE); }
  "INCLUDE"                      { return symbol(INCLUDE); }
  "XMLFORMS"                     { return symbol(XMLFORMS); }
  "MODULE"                       { return symbol(MODULE); }
  "MODULES"                      { return symbol(MODULES); }
  "TABLESETS"                    { return symbol(TABLESETS); }
  "AS"                           { return symbol(AS); }
  "TO"                           { return symbol(TO); }
  "ONETON"                       { return symbol(ONETON); }
  "MTON"                         { return symbol(MTON); }
  "WEBQ"                         { return symbol(WEBQ); }
  "DISABLED"                     { return symbol(DISABLED); }
  "FORMS"                        { return symbol(FORMS); }
  "FORMORDER"                    { return symbol(FORMORDER); }
  "DES"                          { return symbol(DES); }
  "SAME"                         { return symbol(SAME); }
  "NODES"                        { return symbol(NODES); }
  "EDGES"                        { return symbol(EDGES); }
  "PERMISSION"                   { return symbol(PERMISSION); }
  "READ"                         { return symbol(READ); }
  "WRITE"                        { return symbol(WRITE); }
  "REMOVE"                       { return symbol(REMOVE); }
  "APPLICATION"                  { return symbol(APPLICATION); }
  "TITLE"                        { return symbol(TITLE); }
  "RULE"                         { return symbol(RULE); }
  "RULESERVER"                   { return symbol(RULESERVER); }
  "CATEGORY"                     { return symbol(CATEGORY); }
  "REPEAT"                       { return symbol(REPEAT); }
  "ALL"                          { return symbol(ALL); }
  "NONE"                         { return symbol(NONE); }
  "NOT"                          { return symbol(NOT); }
  "NULL"                         { return symbol(NULL); }
    
  /* ADL 5.6 */
  "AFTERNETCREATE"               { return symbol(AFTERNETCREATE); }
  
  /* ADL 6.1 */
  "LOCKED"                       { return symbol(LOCKED); }
  "DIRECT"                       { return symbol(DIRECT); }

  
  /* separators */
  "("                            { return symbol(LPAREN); }
  ")"                            { return symbol(RPAREN); }
  "{"                            { return symbol(LBRACE); }
  "}"                            { return symbol(RBRACE); }
  ";"                            { return symbol(SEMICOLON); }
  ","                            { return symbol(COMMA); }
  "."                            { return symbol(DOT); }
  
  /* operators */
  "="                            { return symbol(EQ); }
  
  /* string literal */
  \"                             { yybegin(STRINGLITERAL); multiline = false; string.setLength(0); }

  /* numeric literals */

  {DecIntegerLiteral}            { return symbol(INTEGER_LITERAL, new Integer(yytext())); }
  {DecLongLiteral}               { return symbol(INTEGER_LITERAL, new Long(yytext().substring(0,yylength()-1))); }
  
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
  \"                             { yybegin(YYINITIAL); return symbol(multiline ? MULTILINE_STRING_LITERAL : STRING_LITERAL, string.toString()); }
  
  {StringCharacter}+             { string.append( yytext() ); }
  
  /* escape sequences */
  \\t                            { string.append( '\t' ); }
  \\n                            { string.append( '\n' ); }
  \\r                            { string.append( '\r' ); }
  \\                             { string.append( '\\' ); }
  
  /* error cases */
  {LineTerminator}               { multiline = true; string.append( yytext() ); } // throw new RuntimeException("Unterminated string at end of line "+yyline); }
}

/* error fallback */
.|\n                             { throw new RuntimeException("Illegal character \""+yytext()+
                                                              "\" at line "+(yyline+1)+", column "+(yycolumn+1)); }
<<EOF>>                          { return symbol(EOF); }