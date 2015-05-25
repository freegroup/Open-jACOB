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

import de.tif.jacob.report.impl.castor.CastorGroup;
import de.tif.jacob.report.impl.castor.CastorLayoutColumn;
import de.tif.jacob.report.impl.castor.Column;


/**
 * @author Andreas Sonntag
 */
public final class QWRField extends QWRElement
{
  static public final transient String RCS_ID = "$Id: QWRField.java,v 1.4 2009-12-17 01:43:34 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";
  
  private int index;
  
  private final String aliasName;
  private final String fieldName;
  
  /**
   * Might be <code>null</code>.
   */
  private final String columnName;
  
  /**
   * Might be <code>null</code>.
   */
  private final QWRFont font;
  
  private final String label;
  private final Integer labelWidth;
  private final Integer labelSpaceBefore;
  
  /**
   * Might be <code>null</code>.
   */
  private final QWRFieldSortorder sortorder;
  
  private final boolean hidden;
  private final QWRFieldJustification justification;
  private final String truncationMark;
  private final boolean linebreak;
  
  /**
   * Might be <code>null</code>.
   */
  private final QWRFieldGrouping grouping;
  
  private final QWRFieldFunction function;

  public QWRField(String aliasFieldName, String columnName, QWRFont font, //
      String label, Integer labelWidth, Integer labelSpaceBefore, //
      QWRFieldSortorder sortorder, boolean hidden, QWRFieldJustification justification, //
      String truncationMark, boolean linebreak, QWRFieldGrouping grouping,
      QWRFieldFunction function)
  {
    String[] split = splitAliasFieldName(aliasFieldName);
    this.aliasName = split[0];
    this.fieldName = split[1];
    this.columnName = columnName;
    this.font = font;
    this.label = label;
    this.labelWidth = labelWidth;
    this.labelSpaceBefore = labelSpaceBefore;
    this.sortorder = sortorder;
    this.hidden = hidden;
    this.justification = justification;
    this.truncationMark = truncationMark;
    this.linebreak = linebreak;
    this.grouping = grouping;
    this.function = function;
  }
  
  protected void setIndex(int index)
  {
    this.index = index;
  }

  protected boolean isHidden()
  {
    return hidden;
  }

  protected boolean isGrouped()
  {
    return this.grouping != null;
  }

  protected int getSortNumberForComparator()
  {
    if (this.sortorder != null)
      if (this.grouping != null)
        return this.sortorder.getSortNumber();
      else
        // ensure that non-grouped fields have a lower sorting priority than
        // grouped fields
        return 1000000 + this.sortorder.getSortNumber();
    return Integer.MAX_VALUE - 1000000 + this.index;
  }
  
  protected int getGroupedNumberForComparator()
  {
    if (this.grouping!=null)
      return this.grouping.getGroupedNbr();
    return Integer.MAX_VALUE;
  }
  
  protected void toCastor(Column castor)
  {
    castor.setTableAlias(this.aliasName);
    castor.setField(this.fieldName);
    castor.setLabel(this.label);
    if (this.sortorder !=null)
      castor.setSortOrder(this.sortorder.toCastor());
  }
  
  protected void toCastor(CastorLayoutColumn castor)
  {
    if (this.font != null)
      castor.setColor(this.font.getColor());
    castor.setJustification(this.justification.toCastor());
    castor.setWidth(this.labelWidth.intValue());
    castor.setIdent(this.labelSpaceBefore.intValue());
    castor.setLinebreak(this.linebreak);
    if (this.truncationMark != null)
      castor.setTruncationMark(this.truncationMark);
    
    castor.setTableAlias(this.aliasName);
    castor.setField(this.fieldName);
    castor.setLabel(this.label);
    if (this.function !=null)
      castor.setFunction(this.function.toCastor());
    if (this.font !=null)
      castor.setFont(this.font.toCastor());
  }
  
  protected void toCastor(CastorGroup castor) throws Exception
  {
    castor.setTableAlias(this.aliasName);
    castor.setField(this.fieldName);
    this.grouping.toCastor(castor);
  }
}
