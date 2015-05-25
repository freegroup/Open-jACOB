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

package de.tif.jacob.report.impl.transformer.classic;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import de.tif.jacob.report.IReport.Column;
import de.tif.jacob.report.impl.ILayoutReport;
import de.tif.jacob.report.impl.castor.CastorLayout;
import de.tif.jacob.report.impl.transformer.IReportDataIterator;
import de.tif.jacob.report.impl.transformer.IReportDataRecord;
import de.tif.jacob.transformer.ITransformer;
import de.tif.jacob.transformer.ITransformerRow;
import de.tif.jacob.transformer.TransformerFactory;

/**
 * Abstract transformer for classic report transformer implementation, i.e.
 * implementation before jACOB version 2.9.
 * 
 * @author Andreas Sonntag
 */
public abstract class ClassicTransformer extends de.tif.jacob.report.impl.transformer.Transformer
{
  /**
   * Adapter class to map IReportDataRecord to ITransformerRow.
   * 
   * @author Andreas Sonntag
   */
  private static final class TransformerRow implements ITransformerRow
  {
    private final IReportDataRecord reportRecord;
    private final Column[] columns;

    private TransformerRow(IReportDataRecord reportRecord, Column[] columns)
    {
      this.reportRecord = reportRecord;
      this.columns = columns;
    }

    public int getCellNumber()
    {
      return this.columns.length;
    }

    public String getCellStringValue(int cell, Locale locale)
    {
      Column column = this.columns[cell];
      return this.reportRecord.getStringValue(column.table, column.field, locale);
    }

    public Object getCellValue(int cell)
    {
      Column column = this.columns[cell];
      return this.reportRecord.getValue(column.table, column.field);
    }
  }

  public final void transform(OutputStream out, ILayoutReport report, CastorLayout layout, IReportDataIterator reportData, Locale locale) throws Exception
  {
    Column[] columns = report.getColumns();

    String[] header = new String[columns.length];
    for (int i = 0; i < columns.length; i++)
    {
      header[i] = columns[i].label;
    }

    // get the data itself.
    //
    ArrayList transformerRowList = new ArrayList();
    while (reportData.hasNext())
    {
      IReportDataRecord reportRecord = reportData.next();
      transformerRowList.add(new TransformerRow(reportRecord, columns));
    }

    ITransformerRow[] rows = new ITransformerRow[transformerRowList.size()];
    for (int row = 0; row < transformerRowList.size(); row++)
    {
      rows[row] = (ITransformerRow) transformerRowList.get(row);
    }
    
    // transform the data to the handsover mimeType
    //
    ITransformer trans = TransformerFactory.get(getMimeType());
    trans.setName(reportData.getApplication().getTitle());
    trans.transform(out, header, rows, locale);
  }

  protected abstract String getMimeType();
}
