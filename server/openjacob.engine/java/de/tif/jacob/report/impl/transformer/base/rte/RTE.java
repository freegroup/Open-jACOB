/*******************************************************************************
 *    This file is part of Open-jACOB
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

package de.tif.jacob.report.impl.transformer.base.rte;


/**
 * Report text expression
 * 
 * @author Andreas Sonntag
 */
public final class RTE
{
  static public transient final String RCS_ID = "$Id: RTE.java,v 1.4 2009/12/22 03:33:06 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.4 $";
  
  private RTE()
  {
  }
  
  private static final IRTEFunctionResolver TEST_RESOLVER = new IRTEFunctionResolver()
  {
    public String average(String field) throws Exception
    {
      return "$avg(" + field + ")$";
    }

    public String count() throws Exception
    {
      return "$count$";
    }

    public String count(String field) throws Exception
    {
      return "$count(" + field + ")$";
    }

    public String date()
    {
      return "$date$";
    }

    public String ignore()
    {
      return "$ignore$";
    }

    public String max(String field) throws Exception
    {
      return "$max(" + field + ")$";
    }

    public String min(String field) throws Exception
    {
      return "$min(" + field + ")$";
    }

    public String name()
    {
      return "$name$";
    }

    public String sum(String field) throws Exception
    {
      return "$sum(" + field + ")$";
    }

    public String tab()
    {
      return "$tab$";
    }

    public String tab(String num)
    {
      return "$tab(" + num + ")$";
    }

    public String tilde()
    {
      return "$tilde$";
    }

    public String time()
    {
      return "$time$";
    }

    public String value(String field) throws Exception
    {
      return "$value(" + field + ")$";
    }
  };
  
  private static final class TestPrinter implements IRTEPrinter
  {
    private final StringBuffer buffer = new StringBuffer();
    
    public void markLeftAligned()
    {
      this.buffer.append("$~left$");
    }

    public void markCentered()
    {
      this.buffer.append("$center$");
    }

    public void markRightAligned()
    {
      this.buffer.append("$right$");
    }

    public void print(String text)
    {
      this.buffer.append(text);
    }

    public void printLinefeed()
    {
      this.buffer.append("$newline$");
    }

    public void printPagebreak()
    {
      this.buffer.append("$pagebreak$");
    }

    public void printPageNumber()
    {
      this.buffer.append("$page$");
    }
  }

  private static final String[] textExpressions = new String[] {
      "db_field(defectsubmitter.department) ~count(defect.pkey) IP's to be validated for ~n", //
      " ~count(defect.pkey) IP's to be validated for db_field(defectsubmitter.department)~n", //
      "\n\r\r\thello"};

  public static void main(String[] args)
  {
    for (int i = 0; i < textExpressions.length; i++)
    {
      String textExpression = textExpressions[i];

      try
      {
        // test text expression
        TestPrinter printer = new TestPrinter();
        RTEParser.printTextExpression(printer, TEST_RESOLVER     , textExpression);

        System.out.println("\"" + textExpression + "\"\n\t-> \"" + printer.toString() + "\"");
      }
      catch (Exception ex)
      {
        System.err.println("\"" + textExpression + "\"\n\t-> ERROR: " + ex.toString() + "\"");
      }
    }
  }
}
