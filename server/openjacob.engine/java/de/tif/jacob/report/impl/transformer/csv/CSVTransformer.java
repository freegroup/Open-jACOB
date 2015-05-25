/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Tarragon GmbH
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

package de.tif.jacob.report.impl.transformer.csv;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Locale;

import de.tif.jacob.report.IReport.Column;
import de.tif.jacob.report.impl.ILayoutReport;
import de.tif.jacob.report.impl.castor.CastorLayout;
import de.tif.jacob.report.impl.transformer.IReportDataIterator;
import de.tif.jacob.report.impl.transformer.IReportDataRecord;
import de.tif.jacob.util.CSVUtil;
import de.tif.jacob.util.StringUtil;

/**
 * CSV report rendering transformer.
 * <p>
 * Note: This transformer does not consider report layouts!
 * 
 * @author Andreas Sonntag
 */
public final class CSVTransformer extends de.tif.jacob.report.impl.transformer.Transformer
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: CSVTransformer.java,v 1.2 2010/03/21 22:46:01 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.2 $";

  public static final String CSV_MIME_TYPE = "text/csv";
  /**
   * Character encoding used.
   */
  public final static String ENCODING = "ISO-8859-1";

  /**
   * @param out
   * @param report
   * @param layout not considered!
   * @param reportData
   * @param locale
   * @throws Exception
   */
  public void transform(OutputStream out, ILayoutReport report, CastorLayout layout, IReportDataIterator reportData, Locale locale) throws Exception
  {
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, ENCODING));

    CSVUtil csvStringUtil = new CSVUtil();

    // print header line
    //
    Column[] columns = report.getColumns();
    for (int i = 0; i < columns.length; i++)
    {
      if (i != 0)
        pw.print(csvStringUtil.getDelimiter());
      pw.print(csvStringUtil.toCSV(columns[i].label));
    }
    pw.println();
    
    // print the data
    //
    while (reportData.hasNext())
    {
      IReportDataRecord record = reportData.next();
      
      for (int i = 0; i < columns.length; i++)
      {
        if (i != 0)
          pw.print(csvStringUtil.getDelimiter());
        pw.print(csvStringUtil.toCSV(StringUtil.toSaveString(record.getStringValue(columns[i].table, columns[i].field, locale))));
      }
      pw.println();
    }

    pw.flush();
    pw.close();
  }
}
