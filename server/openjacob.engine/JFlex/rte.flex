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

package de.tif.jacob.report.impl.transformer.base.rte;

import java_cup.runtime.*;

%%
	
%public
%class RTEScanner
%implements RTESym

%unicode
	
%line
%column

%cup

%{
  static public final transient String RCS_ID = "$Id: rte.flex,v 1.6 2009/12/15 18:07:17 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.6 $";
  
  private String originalText;
  
  protected void setOriginalText(String originalText)
  {
    this.originalText = originalText;
  }
  
  private Symbol symbol(int type) {
    return new RTESymbol(type, yyline+1, yycolumn+1);
  }

  private Symbol symbol(int type, Object value) {
    return new RTESymbol(type, yyline+1, yycolumn+1, value);
  }

%}

/* string and character literals */
TextCharacter = [^\~\r\n\td]
FunctionArgCharacter = [^\~\,\(\)]

%state TILDE_FUNCTION, TILDE_FUNCTION_ARGS

%%

<YYINITIAL> {
  "~"                            { yybegin(TILDE_FUNCTION); return symbol(TILDE); }
  
  /* HACK to support db_field */
  "d"                            { return symbol(TEXT_DATA, yytext()); }
  
  "db_field("                    { yybegin(TILDE_FUNCTION_ARGS); return symbol(DB_FIELD); }
  
  /* special handling for some white space characters */
  "\r"                           { /* ignore carriage returns */ }
  "\t"                           { return symbol(TAB); }
  "\n"                           { return symbol(LF); }
  
  {TextCharacter}+               { return symbol(TEXT_DATA, yytext()); }
}

<TILDE_FUNCTION> {

  /* keywords */
  "n"                            { yybegin(YYINITIAL); return symbol(LF); }
  "b"                            { yybegin(YYINITIAL); return symbol(PAGEBREAK); }
  "p"                            { yybegin(YYINITIAL); return symbol(PAGE); }
  "page"                         { yybegin(YYINITIAL); return symbol(PAGE); }
  "d"                            { yybegin(YYINITIAL); return symbol(DATE); }
  "date"                         { yybegin(YYINITIAL); return symbol(DATE); }
  "t"                            { yybegin(YYINITIAL); return symbol(TIME); }
  "time"                         { yybegin(YYINITIAL); return symbol(TIME); }
  "name"                         { yybegin(YYINITIAL); return symbol(NAME); }
  "tab"                          { yybegin(YYINITIAL); return symbol(TAB); }
  "tab("                         { yybegin(TILDE_FUNCTION_ARGS); return symbol(TAB); }
  "count"                        { yybegin(YYINITIAL); return symbol(COUNT); }
  "count("                       { yybegin(TILDE_FUNCTION_ARGS); return symbol(COUNT); }
  "value("                       { yybegin(TILDE_FUNCTION_ARGS); return symbol(VALUE); }

  "avg("                         { yybegin(TILDE_FUNCTION_ARGS); return symbol(AVERAGE); }
  "sum("                         { yybegin(TILDE_FUNCTION_ARGS); return symbol(SUM); }
  "min("                         { yybegin(TILDE_FUNCTION_ARGS); return symbol(MIN); }
  "max("                         { yybegin(TILDE_FUNCTION_ARGS); return symbol(MAX); }
  
  "l"                            { yybegin(YYINITIAL); return symbol(LEFT); }
  "c"                            { yybegin(YYINITIAL); return symbol(CENTER); }
  "r"                            { yybegin(YYINITIAL); return symbol(RIGHT); }
  
  "~"                            { yybegin(YYINITIAL); return symbol(TILDE); }
  
  // to distinguish between "~name" and "~n~.ame"
  "."                            { yybegin(YYINITIAL); return symbol(IGNORE); }
}

<TILDE_FUNCTION_ARGS> {

  ")"                            { yybegin(YYINITIAL); }
  
  /* separators */
  ","                            { return symbol(COMMA); }
  
  {FunctionArgCharacter}+        { return symbol(ARG, yytext()); }
}

/* error fallback */
.|\n                             { throw new RuntimeException("Illegal character \""+yytext()+
                                                              "\" at line "+(yyline+1)+", column "+(yycolumn+1)+" of \""+originalText+"\""); }
<<EOF>>                          { return symbol(EOF); }