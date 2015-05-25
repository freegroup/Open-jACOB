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
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import de.tif.jacob.transformer.ITransformer;
import de.tif.jacob.transformer.ITransformerRow;

/**
 * This class converts the input data to an Excel sheet. This class is an hot deployment component. DON'T add any member variables to this class!!!!
 * 
 * @author Andreas Herz
 *  
 */
public class Excel extends AbstractTransformer
{
  static public final transient String RCS_ID = "$Id: Excel.java,v 1.3 2009/01/12 11:30:18 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  String name = null;
  private HSSFWorkbook createWorkbook() throws IOException
  {
    InputStream input = getClass().getResourceAsStream("template.xls");
    try
    {
      POIFSFileSystem fs = new POIFSFileSystem(input);

      HSSFWorkbook wb = new HSSFWorkbook(fs);
      // HSSFWorkbook wb = new HSSFWorkbook();

      return wb;
    }
    finally
    {
      IOUtils.closeQuietly(input);
    }
  }

  private HSSFSheet createSheet(HSSFWorkbook wb, String[] header) throws IOException
  {
    HSSFSheet sheet = wb.getSheet("ReportData");

    if(name!=null)
      sheet.getRow(0).getCell((short)0).setCellValue(name);
    
    // Freeze the first 3 rows
    sheet.createFreezePane(0, 3, 0, 3);

    HSSFFont font = wb.createFont();
    font.setFontHeightInPoints((short) 12);
    font.setFontName("Arial");
    font.setBoldweight((short) 20);

    // Aqua background

    HSSFCellStyle style = wb.createCellStyle();
    style.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    style.setFont(font);

    // Create the header bar
    //
    HSSFRow row = sheet.createRow((short) 2);
    for (short i = 0; i < header.length; i++)
    {
      HSSFCell cell = row.createCell(i);
      cell.setCellValue(header[i]);
      cell.setCellStyle(style);
    }

    return sheet;
  }

  /*
	 * @see de.tif.jacob.report.transformer.ITransformer#transform(java.lang.Object[], java.lang.Object[][])
	 */
  public void transform(OutputStream out, String[] header, String[][] data) throws IOException
  {
    // Initialize sheet
    //
    HSSFWorkbook wb = createWorkbook();
    HSSFSheet sheet = createSheet(wb, header);

    // Create the data itself
    //
//    long lastLine = System.currentTimeMillis();
    for (int r = 0; r < data.length; r++)
    {
      HSSFRow row = sheet.createRow(r + 3);
      for (short c = 0; c < data[r].length; c++)
      {
        // Integer MUST be insert as int. Excel 2000/XP generates an error if you insert
        // integer values not as a number!!!!
        try
        {
          row.createCell(c).setCellValue(Integer.parseInt(data[r][c]));
        }
        catch (NumberFormatException e)
        {
          row.createCell(c).setCellValue(data[r][c]);
        }
      }
    }

    // and write to output stream
    //
    write(out, wb);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.transformer.ITransformer#transform(java.io.OutputStream, java.lang.String[], de.tif.jacob.transformer.ITransformerRow[], java.util.Locale)
   */
  public void transform(OutputStream out, String[] header, ITransformerRow[] rows, Locale locale) throws IOException
  {
    // ignore locale
    transform(out, header, rows);
  }

  /*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.transformer.ITransformer#transform(java.io.OutputStream, java.lang.String[], de.tif.jacob.transformer.ITransformerRow[])
	 */
  public void transform(OutputStream out, String[] header, ITransformerRow[] rows) throws IOException
  {
    // Initialize sheet
    //
    HSSFWorkbook wb = createWorkbook();
    HSSFSheet sheet = createSheet(wb, header);

    HSSFCellStyle timestampStyle = null;
    HSSFCellStyle timeStyle = null;
    HSSFCellStyle dateStyle = null;

    // Create the data itself
    //
//    long lastLine = System.currentTimeMillis();
    for (int r = 0; r < rows.length; r++)
    {
      HSSFRow row = sheet.createRow(r + 3);
      ITransformerRow dataRow = rows[r];
      for (short c = 0; c < dataRow.getCellNumber(); c++)
      {
        Object cellValue = dataRow.getCellValue(c);
        if (cellValue instanceof Number)
        {
          row.createCell(c).setCellValue(((Number) cellValue).doubleValue());
        }
        else if (cellValue instanceof Date)
        {
          // Note: use builtin-formats to be "language-independend", i.e. German-format in German Excel, 
          // US-format in US Excel, etc.
          HSSFCellStyle style;
          if (cellValue instanceof Timestamp)
          {
            if (timestampStyle == null)
            {
              timestampStyle = wb.createCellStyle();
              timestampStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
            }
            style = timestampStyle;
          }
          else if (cellValue instanceof Time)
          {
            if (timeStyle == null)
            {
              timeStyle = wb.createCellStyle();
              timeStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("h:mm"));
            }
            style = timeStyle;
          }
          else
          {
            if (dateStyle == null)
            {
              dateStyle = wb.createCellStyle();
              dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
            }
            style = dateStyle;
          }
          HSSFCell cell = row.createCell(c);
          cell.setCellStyle(style);
          cell.setCellValue((Date) cellValue);
        }
        else if (cellValue != null)
        {
          row.createCell(c).setCellValue(cellValue.toString());
        }
      }
    }

    // and write to output stream
    //
    write(out, wb);
  }

  private static void write(OutputStream out, HSSFWorkbook wb) throws IOException
  {
    wb.write(out);
    out.flush();
  }

  /*
	 * @see de.tif.jacob.report.transformer.ITransformer#getMimeType()
	 */
  public String getMimeType()
  {
    return "application/excel";
  }
  
  public void setName(String name)
  {
    this.name = name;
  }

}
