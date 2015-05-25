/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
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

package de.tif.jacob.transformer.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Locale;

import de.tif.jacob.transformer.ITransformerRow;
import de.tif.jacob.util.StringUtil;


/**
 * This class converts the input data to an Excel sheet. 
 * This class is an hot deployment component. DON'T add any member
 * variables to this class!!!!
 * 
 * 
 * @author Andreas Herz
 *
 */
public class TextHtml extends AbstractTransformer
{
  static public final transient String RCS_ID = "$Id: TextHtml.java,v 1.4 2010/02/26 08:50:45 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  /**
   * Character encoding used.
   */
  public final static String ENCODING = "ISO-8859-1";

  
	/* 
	 * @see de.tif.jacob.report.transformer.ITransformer#transform(java.lang.Object[], java.lang.Object[][])
	 */
	public void transform(OutputStream out, String[] header, String[][] data) throws IOException
	{
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, ENCODING));

	  pw.println("<table>");
    // Create the header bar
    //
	  pw.print("<tr>");
    for (short i = 0; i < header.length; i++)
		{
      pw.print("<td>");
      pw.print(StringUtil.htmlEncode(header[i]));
      pw.print("</td>");
    }
    pw.println("</tr>");
    
    // Create the data itself
    //
    for (int r = 0; r < data.length; r++)
		{
      pw.print("<tr>");
      for (short c = 0; c < data[r].length; c++)
      {
        String value =data[r][c]==null?"":data[r][c];
        pw.print("<td>");
        pw.print(StringUtil.htmlEncode(value));
        pw.print("</td>");
      }
      pw.println("</tr>");
    }
    pw.println("</table>");

    pw.flush();
    pw.close();
	}
  
	/* 
	 * @see de.tif.jacob.report.transformer.ITransformer#getMimeType()
	 */
	public String getMimeType()
	{
		return "text/html";
	}
	
  /* (non-Javadoc)
   * @see de.tif.jacob.transformer.ITransformer#transform(java.io.OutputStream, java.lang.String[], de.tif.jacob.transformer.ITransformerRow[])
   */
  public void transform(OutputStream out, String[] header, ITransformerRow[] rows) throws IOException
  {
    transform(out, header, rows, null);
  }

  public void transform(OutputStream out, String[] header, ITransformerRow[] rows, Locale locale) throws IOException
  {
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, ENCODING));

    pw.println("<table>");
    // Create the header bar
    //
    pw.print("<tr>");
    for (short i = 0; i < header.length; i++)
    {
      pw.print("<td>");
      pw.print(StringUtil.htmlEncode(header[i]));
      pw.print("</td>");
    }
    pw.println("</tr>");

    // Create the data itself
    //
    for (int r = 0; r < rows.length; r++)
    {
      ITransformerRow dataRow = rows[r];
      pw.print("<tr>");
      for (short c = 0; c < dataRow.getCellNumber(); c++)
      {
        String value = dataRow.getCellStringValue(c, locale);
        pw.print("<td>");
        if (null != value)
          pw.print(StringUtil.htmlEncode(value));
        pw.print("</td>");
      }
      pw.println("</tr>");
    }
    pw.println("</table>");

    pw.flush();
    pw.close();
  }

  public void setName(String name)
  {
    // ignore
  }

}
