/*******************************************************************************
 *    This file is part of jACOB
 *    Copyright (C) 2005-2009 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package de.tif.qes.report.element;

import de.tif.jacob.report.impl.castor.CastorCaption;
import de.tif.jacob.report.impl.transformer.base.rte.IRTEFunctionResolver;
import de.tif.jacob.report.impl.transformer.base.rte.IRTEPrinter;
import de.tif.jacob.report.impl.transformer.base.rte.RTEParser;
import de.tif.jacob.report.impl.transformer.base.util.AliasFieldNameSplitter;

/**
 * @author Andreas Sonntag
 */
public final class QWRCaption
{
  static public final transient String RCS_ID = "$Id: QWRCaption.java,v 1.7 2009-12-22 03:33:01 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.7 $";

  private final String text;

  /**
   * Might be <code>null</code>
   */
  private final QWRFont font;

  /**
   * @param text
   * @param font
   */
  public QWRCaption(String text, QWRFont font)
  {
    this.text = text;
    this.font = font;
  }

  protected CastorCaption toCastor() throws Exception
  {
    CastorCaption castor = new CastorCaption();
    
    // evaluate and convert caption expression
    castor.setText(evaluate(text));
    
//    evaluate(castor.getText());

    if (this.font != null)
    {
      castor.setFont(this.font.toCastor());
      // castor.setColor(this.font.getColor());
    }
    return castor;
  }
  
  private static final IRTEFunctionResolver CONVERSION_RESOLVER = new IRTEFunctionResolver()
  {
    public String count() throws Exception
    {
      return "~count";
    }

    public String count(String field) throws Exception
    {
      // test for formal "alias.field"
      new AliasFieldNameSplitter(field);
      return "~count(" + field + ")";
    }

    public String average(String field) throws Exception
    {
      // test for formal "alias.field"
      new AliasFieldNameSplitter(field);
      return "~avg(" + field + ")";
    }

    public String max(String field) throws Exception
    {
      // test for formal "alias.field"
      new AliasFieldNameSplitter(field);
      return "~max(" + field + ")";
    }

    public String min(String field) throws Exception
    {
      // test for formal "alias.field"
      new AliasFieldNameSplitter(field);
      return "~min(" + field + ")";
    }

    public String sum(String field) throws Exception
    {
      // test for formal "alias.field"
      new AliasFieldNameSplitter(field);
      return "~sum(" + field + ")";
    }

    public String value(String field) throws Exception
    {
      // test for formal "alias.field"
      new AliasFieldNameSplitter(field);
      return "~value(" + field + ")";
    }

    public String date()
    {
      return "~date";
    }

    public String ignore()
    {
      return "~.";
    }

    public String tilde()
    {
      return "~~";
    }

    public String tab()
    {
      return "~tab";
    }

    public String tab(String num)
    {
      return "~tab(" + num + ")";
    }

    public String time()
    {
      return "~time";
    }

    public String name()
    {
      return "~name";
    }
  };
  
  private static final class ConversionRTEPrinter implements IRTEPrinter
  {
    private final StringBuffer buffer = new StringBuffer();
    
    public void markLeftAligned()
    {
      this.buffer.append("~l");
    }

    public void markCentered()
    {
      this.buffer.append("~c");
    }

    public void markRightAligned()
    {
      this.buffer.append("~r");
    }

    public void print(String text)
    {
      this.buffer.append(text);
    }

    public void printLinefeed()
    {
      this.buffer.append("~n");
    }

    public void printPagebreak()
    {
      this.buffer.append("~b");
    }

    public void printPageNumber()
    {
      this.buffer.append("~page");
    }

    public String toString()
    {
      return this.buffer.toString();
    }
  }

  /**
   * Evaluate expressions in caption text.
   * 
   * @param text
   * @throws RuntimeException
   */
  private static String evaluate(String text) throws Exception
  {
    // evaluate and convert text expression
    ConversionRTEPrinter printer = new ConversionRTEPrinter();
    RTEParser.printTextExpression(printer, CONVERSION_RESOLVER, text);
    return printer.toString();
  }
}
