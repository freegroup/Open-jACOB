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

/* QBE lexer specification */

package de.tif.jacob.core.data.impl.qbe;

import java_cup.runtime.*;
import de.tif.jacob.util.StringUtil;

%%

%public
%class QBEScanner
%implements QBESym

%unicode

%line
%column
	
%cup

%{
  static public final transient String RCS_ID = "$Id: qbe.flex,v 1.4 2010/08/06 15:07:00 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";
  
  private StringBuffer string = new StringBuffer();
  
  private int oldState;
  
  private Symbol symbol(int type) {
    return new QBESymbol(type, yyline+1, yycolumn+1);
  }

  private Symbol symbol(int type, Object value) {
    return new QBESymbol(type, yyline+1, yycolumn+1, value);
  }
  
  /**
   * Make one double quote from two double quotes.
   * @param str
   * @return
   */
  private String correctDoubleQuote(String str)
  {
    StringBuffer buf = null;
    for (int i = 0; i < str.length(); i++)
    {
      char c = str.charAt(i);
      switch (c)
      {
        case '"':
          if (buf == null)
          {
            buf = new StringBuffer(str.substring(0, i));
          }
          buf.append(c);
          i++;
          break;
        default:
          if (buf != null)
            buf.append(c);
          break;
      }
    }
    return buf == null ? str : buf.toString();
  }
%}

/* main character classes */
LineTerminator = \r|\n|\r\n

WhiteSpace = {LineTerminator} | [ \t\f]

PM = [pP][mM]
AM = [aA][mM]

NULL = [nN][uU][lL][lL]
TODAY = [tT][oO][dD][aA][yY]
NOW = [nN][oO][wW]

TRUE = [tT][rR][uU][eE]
FALSE = [fF][aA][lL][sS][eE]

YTD = [yY][tT][dD]
MTD = [mM][tT][dD]
QTD = [qQ][tT][dD]
WTD = [wW][tT][dD]

THISY = [tT][hH][iI][sS][yY]
THISQ = [tT][hH][iI][sS][qQ]
THISM = [tT][hH][iI][sS][mM]
THISW = [tT][hH][iI][sS][wW]

WEEK = [wW][eE][eE][kK]

/* datetime literals */
TwoDigitLiteral = [0-9]?[0-9]
FourDigitLiteral = [0-9]?[0-9][0-9][0-9] /* 1999, 2000, 105 (i.e. 105 AC) */
DateShortLiteral = [0-3][0-9][0-1][0-9][0-9][0-9] /* 010496, 310302 */
DateLongLiteral = [0-3][0-9][0-1][0-9][0-9][0-9][0-9][0-9] /* 01042003 */

/* time values */
Years = [yY]
Months = [mM]
Days = [dD]
Hours = [hH]
Minutes = [mM][iI][nN]
Seconds = [sS][eE][cC]

/* integer literals */
IntegerLiteral = 0 | [+-]? [1-9][0-9]*
UnsignedIntegerLiteral = 0 | [1-9][0-9]*

/* decimal literals */        
DotDecimalLiteral = {DotDecimalLiteral1}|{DotDecimalLiteral2}
DotDecimalLiteral1 = [+-]? [0-9]+ \. [0-9]* 
DotDecimalLiteral2 = \. [0-9]+ 

CommaDecimalLiteral = {CommaDecimalLiteral1}|{CommaDecimalLiteral2}
CommaDecimalLiteral1 = [+-]? [0-9]+ \, [0-9]* 
CommaDecimalLiteral2 = \, [0-9]+ 

/* a single character of a text literal */
TextCharacter = [^\"]

/* string and character literals */
/* TextLiteral = [^\!\=\^\$\?\*\|\&\>\<][^\!\$\?\*\|\&]* */
TextLiteral = {TextLiteral1}|{TextLiteral2}
TextLiteral1 = {TextLiteralChar1}{TextLiteralChar3}*
TextLiteral2 = {TextLiteralChar1}{TextLiteralChar2}* {TextLiteralChar3}
TextLiteralChar1 = [^\!\=\^\?\*\|\&\>\<\"] | {DoubleQuote}
TextLiteralChar2 = [^\?\*\|\&\"] | {DoubleQuote}
TextLiteralChar3 = [^\$\?\*\|\&\"] | {DoubleQuote}
/* Hack: Two double quotes will be corrected by means of correctDoubleQuote() */
DoubleQuote = \"\"


EnumLiteral = [^\!\|][^\|]*

%state TEXT, TEXTLITERAL, INTEGER, BOOLEAN, DOT_DECIMAL, COMMA_DECIMAL, INTERVAL, TIMESTAMP, DATE, TIME, ENUM
%state TIME_LAG, TIME_LAG_POST, TIME_LAG_FOR_VALUE, TIME_LAG_FOR_VALUE_POST, WEEK_NUM
%state TIMESTAMP_VALUE, DATE_VALUE, TIME_VALUE
%state NULL_CHECK

%%

<NULL_CHECK> {

  /* keywords */
  {NULL}                         { return symbol(NULL); }
  
  /* operators */
  "!"                            { return symbol(NOT); }
}

<TEXT, INTEGER, BOOLEAN, DOT_DECIMAL, COMMA_DECIMAL, INTERVAL, TIMESTAMP, DATE, TIME, ENUM> {

  /* keywords */
  {NULL}                         { return symbol(NULL); }
  
  /* operators */
  "!"                            { return symbol(NOT); }
  "|"                            { return symbol(OR); }
}

<ENUM> {

  /* string literal */
  {EnumLiteral}                  { string.append(yytext()); String result = string.toString(); string.setLength(0); return symbol(ENUM_LITERAL, result); }
}

<TEXT> {

  /* operators */
  "="                            { return symbol(EXACT_MATCH); }
  "^"                            { return symbol(LEFT_ANCHOR); }
  "$"                            { return symbol(RIGHT_ANCHOR); }
  "?"                            { return symbol(SINGLEWILDCARD); }
  "*"                            { return symbol(WILDCARD); }
  "&"                            { return symbol(AND); }
  ">"                            { return symbol(GREATER_THAN); }
  "<"                            { return symbol(LESS_THAN); }
  ">="                           { return symbol(GREATER_OR_EQUAL_THAN); }
  "<="                           { return symbol(LESS_OR_EQUAL_THAN); }
  
  /* start of escaped text literal */
  \"                             { yybegin(TEXTLITERAL); string.setLength(0); }

  /* text literal */
  {TextLiteral}                  { string.append(yytext()); String result = correctDoubleQuote(string.toString()); string.setLength(0); return symbol(TEXT_LITERAL, result); }
}

<TEXTLITERAL> {
  \"                             { yybegin(TEXT); String result = string.toString(); string.setLength(0); return symbol(TEXT_LITERAL, result); }
  \"\"                           { string.append('\"'); }
  
  {TextCharacter}+               { string.append(yytext()); }
}

<INTEGER, DOT_DECIMAL, COMMA_DECIMAL> {

  ".."                           { return symbol(RANGE); }
  "&"                            { return symbol(AND); }
  ">"                            { return symbol(GREATER_THAN); }
  "<"                            { return symbol(LESS_THAN); }
  ">="                           { return symbol(GREATER_OR_EQUAL_THAN); }
  "<="                           { return symbol(LESS_OR_EQUAL_THAN); }
  
  /* numeric literals */
  {IntegerLiteral}               { return symbol(BIGINT_LITERAL, new Long(yytext())); }
  
  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}

<BOOLEAN> {

  /* boolean literals */
  {TRUE}                         { return symbol(BOOLEAN_LITERAL, Boolean.TRUE); }
  {FALSE}                        { return symbol(BOOLEAN_LITERAL, Boolean.FALSE); }
  
  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}

<DOT_DECIMAL> {

  /* numeric literals */
  {DotDecimalLiteral}               { return symbol(DECIMAL_LITERAL, new java.math.BigDecimal(yytext())); }
}

<COMMA_DECIMAL> {

  /* numeric literals */
  {CommaDecimalLiteral}             { return symbol(DECIMAL_LITERAL, new java.math.BigDecimal(StringUtil.replace(yytext(), ",", "."))); }
}

<INTERVAL> {

  ".."                           { return symbol(RANGE); }
  ">"                            { return symbol(GREATER_THAN); }
  "<"                            { return symbol(LESS_THAN); }
  ">="                           { return symbol(GREATER_OR_EQUAL_THAN); }
  "<="                           { return symbol(LESS_OR_EQUAL_THAN); }
  
  ":"                            { return symbol(COLON); }
  
  /* numeric literals */
  {UnsignedIntegerLiteral}       { return symbol(INTERVAL_LITERAL, new Integer(yytext())); }
  {TwoDigitLiteral}              { return symbol(INTERVAL_LITERAL, new Integer(yytext())); }
  
  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}

<DATE_VALUE, TIME_VALUE, TIMESTAMP_VALUE> {

  /* datetime literals */
  {TwoDigitLiteral}              { return symbol(TWO_DIGIT_LITERAL, new Integer(yytext())); }
  {FourDigitLiteral}             { return symbol(FOUR_DIGIT_LITERAL, new Integer(yytext())); }
  
  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}

<DATE, TIME, TIMESTAMP> {

  /* operators */
  ".."                           { return symbol(RANGE); }
  ">"                            { return symbol(GREATER_THAN); }
  "<"                            { return symbol(LESS_THAN); }
  ">="                           { return symbol(GREATER_OR_EQUAL_THAN); }
  "<="                           { return symbol(LESS_OR_EQUAL_THAN); }
  
  /* datetime literals */
  {TwoDigitLiteral}              { return symbol(TWO_DIGIT_LITERAL, new Integer(yytext())); }
  {FourDigitLiteral}             { return symbol(FOUR_DIGIT_LITERAL, new Integer(yytext())); }
  
  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}

<TIME_LAG, TIME_LAG_POST> {

  /* operators leaving this state */
  "|"                            { yybegin(oldState); return symbol(OR); }
  ".."                           { yybegin(oldState); return symbol(RANGE); }
}

<TIME_LAG, TIME_LAG_FOR_VALUE> {

  /* operators to modify now, today, etc. 
     NOTE: these states are needed to distinguish MINUS from DASH!
     IBIS: Check whether this could be avoided. */
  "-"                            { yybegin(yystate() == TIME_LAG ? TIME_LAG_POST : TIME_LAG_FOR_VALUE_POST); return symbol(MINUS); }
  "+"                            { yybegin(yystate() == TIME_LAG ? TIME_LAG_POST : TIME_LAG_FOR_VALUE_POST); return symbol(PLUS); }
  
  /* leave this state */
  {TwoDigitLiteral}              { yybegin(oldState); return symbol(TWO_DIGIT_LITERAL, new Integer(yytext())); }  
  
  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}

<TIME_LAG_POST, TIME_LAG_FOR_VALUE_POST> {

  /* literals */
  {UnsignedIntegerLiteral}       { return symbol(BIGINT_LITERAL, new Long(yytext())); }
  
  /* time values */
  {Years}                        { yybegin(oldState); return symbol(YEARS); }
  {Months}                       { yybegin(oldState); return symbol(MONTHS); }
  {Days}                         { yybegin(oldState); return symbol(DAYS); }
  {Hours}                        { yybegin(oldState); return symbol(HOURS); }
  {Minutes}                      { yybegin(oldState); return symbol(MINS); }
  {Seconds}                      { yybegin(oldState); return symbol(SECS); }
  
  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}

<DATE, DATE_VALUE, TIMESTAMP, TIMESTAMP_VALUE> {

  "Jan"                          { return symbol(JANURAY); }
  "Januar"                       { return symbol(JANURAY); }
  "January"                      { return symbol(JANURAY); }
  "Feb"                          { return symbol(FEBRUARY); }
  "Februar"                      { return symbol(FEBRUARY); }
  "February"                     { return symbol(FEBRUARY); }
  "Mrz"                          { return symbol(MARCH); }
  "Mar"                          { return symbol(MARCH); }
  "März"                         { return symbol(MARCH); }
  "March"                        { return symbol(MARCH); }
  "Apr"                          { return symbol(APRIL); }
  "April"                        { return symbol(APRIL); }
  "Mai"                          { return symbol(MAI); }
  "Jun"                          { return symbol(JUNE); }
  "Juni"                         { return symbol(JUNE); }
  "June"                         { return symbol(JUNE); }
  "Jul"                          { return symbol(JULY); }
  "Juli"                         { return symbol(JULY); }
  "July"                         { return symbol(JULY); }
  "Aug"                          { return symbol(AUGUST); }
  "August"                       { return symbol(AUGUST); }
  "Sep"                          { return symbol(SEPTEMBER); }
  "September"                    { return symbol(SEPTEMBER); }
  "Okt"                          { return symbol(OCTOBER); }
  "Oct"                          { return symbol(OCTOBER); }
  "Oktober"                      { return symbol(OCTOBER); }
  "October"                      { return symbol(OCTOBER); }
  "Nov"                          { return symbol(NOVEMBER); }
  "November"                     { return symbol(NOVEMBER); }
  "Dez"                          { return symbol(DECEMBER); }
  "Dec"                          { return symbol(DECEMBER); }
  "Dezember"                     { return symbol(DECEMBER); }
  "December"                     { return symbol(DECEMBER); }

  "."                            { return symbol(DOT); }
  "/"                            { return symbol(SLASH); }
  "-"                            { return symbol(DASH); }  /* 2005-02-12 or 05-02-12 */
  
  /* date literals */
  {DateShortLiteral}             { return symbol(DATE_LITERAL, yytext()); }
  {DateLongLiteral}              { return symbol(DATE_LITERAL, yytext()); }
}

<DATE, TIMESTAMP> {

  /* keywords */
  {TODAY}                        { oldState = yystate(); yybegin(TIME_LAG); return symbol(TODAY); }

  {YTD}                          { oldState = yystate(); yybegin(TIME_LAG); return symbol(YTD); }
  {QTD}                          { oldState = yystate(); yybegin(TIME_LAG); return symbol(QTD); }
  {MTD}                          { oldState = yystate(); yybegin(TIME_LAG); return symbol(MTD); }
  {WTD}                          { oldState = yystate(); yybegin(TIME_LAG); return symbol(WTD); }
  
  {THISY}                        { oldState = yystate(); yybegin(TIME_LAG); return symbol(THISY); }
  {THISQ}                        { oldState = yystate(); yybegin(TIME_LAG); return symbol(THISQ); }
  {THISM}                        { oldState = yystate(); yybegin(TIME_LAG); return symbol(THISM); }
  {THISW}                        { oldState = yystate(); yybegin(TIME_LAG); return symbol(THISW); }
  
  {WEEK}                         { oldState = yystate(); yybegin(WEEK_NUM); return symbol(WEEK); }
  
}

<WEEK_NUM> {

  /* operators leaving this state */
  {UnsignedIntegerLiteral}       { yybegin(TIME_LAG); return symbol(BIGINT_LITERAL, new Long(yytext())); }
}

<DATE_VALUE, TIMESTAMP_VALUE> {

  /* keywords */
  {TODAY}                        { oldState = yystate(); yybegin(TIME_LAG_FOR_VALUE); return symbol(TODAY); }
  
}

<TIME, TIME_VALUE, TIMESTAMP, TIMESTAMP_VALUE> {

  ":"                            { return symbol(COLON); }

  {PM}                           { return symbol(PM); }
  {AM}                           { return symbol(AM); }  
}

<TIMESTAMP> {

  /* keywords */
  {NOW}                          { oldState = yystate(); yybegin(TIME_LAG); return symbol(NOW); }

}

<TIMESTAMP_VALUE> {

  /* keywords */
  {NOW}                          { oldState = yystate(); yybegin(TIME_LAG_FOR_VALUE); return symbol(NOW); }

}

/* error fallback */
.|\n                             { throw new RuntimeException("Illegal character \""+yytext()+
                                                              "\" at line "+(yyline+1)+", column "+(yycolumn+1)); }
                                                              
<<EOF>>                          { return symbol(EOF); }
                                                              
