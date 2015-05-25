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
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.transformer.ITransformer;
import de.tif.jacob.transformer.ITransformerRow;


/**
 * This class converts the input data to an Excel sheet. 
 * This class is an hot deployment component. DON'T add any member
 * variables to this class!!!!
 * 
 * 
 * @author Andreas Herz
 *
 */
public class TextPlain extends AbstractTransformer
{
  static public final transient String RCS_ID = "$Id: TextPlain.java,v 1.3 2009/01/12 11:30:18 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  static private final String filler=" | ";
  
	/* 
	 * @see de.tif.jacob.report.transformer.ITransformer#transform(java.lang.Object[], java.lang.Object[][])
	 */
	public void transform(OutputStream out, String[] header, String[][] data) throws IOException
	{
    int[] maxLen=new int[header.length];
    for (short i = 0; i < header.length; i++)
    {
      maxLen[i]=header[i].length();
    }
    // Create the data itself
    //
    for (int r = 0; r < data.length; r++)
    {
      for (int c = 0; c < data[r].length; c++)
      {
        if(data[r][c]!=null)
          maxLen[c]=Math.max(maxLen[c],data[r][c].length());
      }
    }
    
    
    // Create the header bar
    //
    String headerBar="";
    for (short i = 0; i < header.length; i++)
		{
      if((i+1<header.length))
        headerBar+=StringUtils.rightPad(header[i],maxLen[i])+filler;
      else
        headerBar+=header[i];
    }
    out.write(headerBar.getBytes());
    out.write("\n".getBytes());
    headerBar=StringUtils.rightPad("",headerBar.length(),"-");
    out.write(headerBar.getBytes());
    
    // Create the data itself
    //
    for (int r = 0; r < data.length; r++)
		{
      out.write(("\n").getBytes());
      for (short c = 0; c < data[r].length; c++)
      {
        String value =data[r][c]==null?"":data[r][c];
        if((c+1)<data[r].length)
          out.write((StringUtils.rightPad(value,maxLen[c])+filler).getBytes());
        else
          out.write(value.getBytes());
      }
    }
  }
  
  
	/* 
	 * @see de.tif.jacob.report.transformer.ITransformer#getMimeType()
	 */
	public String getMimeType()
	{
		return "text/plain";
	}
	
  /* (non-Javadoc)
   * @see de.tif.jacob.transformer.ITransformer#transform(java.io.OutputStream, java.lang.String[], de.tif.jacob.transformer.ITransformerRow[])
   */
  public void transform(OutputStream out, String[] header, ITransformerRow[] rows) throws IOException
  {
    transform(out, header, rows, null);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.transformer.ITransformer#transform(java.io.OutputStream,
   *      java.lang.String[], de.tif.jacob.transformer.ITransformerRow[],
   *      java.util.Locale)
   */
  public void transform(OutputStream out, String[] header, ITransformerRow[] trows, Locale locale) throws IOException
  {
    // fetch all string values
    String[][] rows = new String[trows.length][];
    for (int row = 0; row < trows.length; row++)
    {
      ITransformerRow trow = trows[row];

      rows[row] = new String[header.length];
      for (int column = 0; column < header.length; column++)
      {
        String value = trow.getCellStringValue(column, locale);
        if (value != null)
          rows[row][column] = value;
      }
    }
    
    // and call other transformer method
    transform(out, header, rows);
  }


  public void setName(String name)
  {
    // ignore
  }

}
