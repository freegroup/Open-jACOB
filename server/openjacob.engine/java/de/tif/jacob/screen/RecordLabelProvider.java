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

package de.tif.jacob.screen;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.ITableField;

/**
 * Default implementation for <code>IDataTableRecord</code>.<br>
 * Provide the text and/or image for the label of a given record. 
 * Used by IRecordTReeDialog.
 */
public class RecordLabelProvider implements ILabelProvider
{
  /**
   * Returns the resource name of the image for the label of the given element. <br>
   * The image <b>must</b> be stored in the java package
   * <code>jacob.resources.*</code><br>
   * 
   * Example: if you return <code>Task</code> you must provide
   * an image named <code>Task.png</code> or <code>Task.gif</code>
   * in the java package <code>jacob.resources.*</code><br>
   * 
   * @param context
   *          the current client context
   * @param record the record for which to provide the label image
   * @return the resource name of the image, or <code>null</code>
   *   if there is no image for the given object
   */
  public String getImage(IClientContext context, Object record)
  {
    return null;
  }

  /**
   * Returns the text for the label of the given <code>IDataTableRecord</code>.
   *
   * @param context
   *          the current client context
   * @param record the record for which to provide the label text
   * @return the text string used to label the element, or <code>null</code>
   *   if there is no text label for the given object
   */
  public String getText(IClientContext context, Object record)
  {
    IDataTableRecord tableRecord = (IDataTableRecord)record;
    ITableField representative = tableRecord.getTableAlias().getTableDefinition().getRepresentativeField();
    if(representative==null)
      return tableRecord.getStringValue(0);
    else
      return tableRecord.getStringValue(representative.getFieldIndex());
  }

}
