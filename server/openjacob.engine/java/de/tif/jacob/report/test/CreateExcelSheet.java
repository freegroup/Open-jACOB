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

package de.tif.jacob.report.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author Andreas Herz
 *
 */
public class CreateExcelSheet
{
  static public final transient String RCS_ID = "$Id: CreateExcelSheet.java,v 1.1 2007/01/19 09:50:50 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  public static void main(String[] args)
	{
    try
		{
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("new sheet");

			// Create a row and put some cells in it. Rows are 0 based.
			HSSFRow row = sheet.createRow((short)0);
			// Create a cell and put a value in it.
			HSSFCell cell = row.createCell((short)0);
			cell.setCellValue(1);

			// Or do it on one line.
			row.createCell((short)1).setCellValue(1.2);
			row.createCell((short)2).setCellValue("This is a string");
			row.createCell((short)3).setCellValue(true);

			// Write the output to a file
			FileOutputStream fileOut = new FileOutputStream("workbook.xls");
			wb.write(fileOut);
			fileOut.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}		
	}
}
