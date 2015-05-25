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

package de.tif.jacob.report.impl.transformer.base;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.tif.jacob.report.impl.castor.CastorFunction;
import de.tif.jacob.report.impl.castor.CastorLayoutColumn;
import de.tif.jacob.report.impl.castor.CastorSubstring;
import de.tif.jacob.report.impl.transformer.IReportDataRecord;
import de.tif.jacob.report.impl.transformer.base.BaseTransformer.ITransformerPrinter;
import de.tif.jacob.report.impl.transformer.base.BaseTransformer.Part;
import de.tif.jacob.util.StringUtil;

/**
 * Abstract base transformer printer
 * 
 * @author Andreas Sonntag
 */
public abstract class BaseTransformerPrinter implements ITransformerPrinter
{
  private final Locale locale;
  private final Map autoLayoutColumnWidthMap = new HashMap();

  protected BaseTransformerPrinter(Locale locale)
  {
    this.locale = locale;
  }

  protected final Locale getLocale()
  {
    return this.locale;
  }
  
  protected String getSaveStringValue(IReportDataRecord record, CastorLayoutColumn layoutColumn)
  {
    String value = record.getStringValue(layoutColumn.getTableAlias(), layoutColumn.getField(), this.locale);
    return StringUtil.toSaveString(applyFunction(value, layoutColumn.getFunction()));
  }
  
  protected String truncateValue(String value, CastorLayoutColumn layoutColumn, int columnWidth)
  {
    if (value.length() > columnWidth)
    {
      // Quintus: Value wird abgeschnitten!

      String truncationMark = layoutColumn.getTruncationMark();
      if (truncationMark != null)
      {
        return value.substring(0, columnWidth - truncationMark.length()) + truncationMark;
      }
      return value.substring(0, columnWidth);
    }
    return value;
  }
  
  /**
   * Calculate the minimum required width to show a column without truncating label and values.
   * 
   * @param part
   * @param layoutColumn
   * @return
   * @throws Exception
   */
  protected int getAutoLayoutColumnWidth(Part part, CastorLayoutColumn layoutColumn) throws Exception
  {
    Integer autoWidth = (Integer) this.autoLayoutColumnWidthMap.get(layoutColumn);
    if (autoWidth == null)
    {
      RecordColumnWidthProcessor processor = new RecordColumnWidthProcessor(layoutColumn);
      part.getRootPart().processAllRecords(processor, getLocale());
      autoWidth = new Integer(processor.columnWidth);
      this.autoLayoutColumnWidthMap.put(layoutColumn, autoWidth);
    }
    return autoWidth.intValue();
  }
  
  private static final class RecordColumnWidthProcessor implements IReportDataRecordProcessor
  {
    private final CastorLayoutColumn layoutColumn;
    private int columnWidth;

    private RecordColumnWidthProcessor(CastorLayoutColumn layoutColumn)
    {
      this.layoutColumn = layoutColumn;
      // do not forget to consider label width, but always set to 1 as minimum!
      this.columnWidth = Math.max(1, StringUtil.toSaveString(layoutColumn.getLabel()).length());
    }

    public void process(IReportDataRecord record, Locale locale)
    {
      String value = record.getStringValue(layoutColumn.getTableAlias(), layoutColumn.getField(), locale);
      value = StringUtil.toSaveString(applyFunction(value, layoutColumn.getFunction()));
      if (columnWidth < value.length())
        columnWidth = value.length();
    }
  }
  
  public static String applyFunction(String value, CastorFunction function)
  {
    if (value != null && function != null)
    {
      CastorSubstring substring = function.getSubstring();
      int beginIndex = substring.getBeginIndex();
      if (beginIndex >= value.length())
        value = null;
      else
      {
        if (substring.hasEndIndex())
        {
          int endIndex = substring.getEndIndex();
          if (endIndex > value.length())
            endIndex = value.length();
          value = value.substring(beginIndex, endIndex);
        }
        else
        {
          value = value.substring(beginIndex);
        }
      }
    }
    return value;
  }

}
