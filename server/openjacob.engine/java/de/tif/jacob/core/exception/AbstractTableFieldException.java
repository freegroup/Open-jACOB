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

package de.tif.jacob.core.exception;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.i18n.Message;
import de.tif.jacob.screen.IClientContext;

/**
 * 
 * @author Andreas Herz
 * @since 2.7
 */
public  class AbstractTableFieldException extends UserException implements ITableFieldException 
{
  private final ITableField tableField;
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: AbstractTableFieldException.java,v 1.5 2010/10/08 08:03:16 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.5 $";


  public AbstractTableFieldException(ITableField tableField, Message message, Throwable cause)
  {
    super(message, cause);
    this.tableField = tableField;
  }

  public AbstractTableFieldException(ITableField tableField, Message message)
  {
    super(message);
    this.tableField = tableField;
  }

  public AbstractTableFieldException(ITableField tableField, String message)
  {
    super(message);
    this.tableField = tableField;
  }

  /**
   */
  public final ITableField getTableField()
  {
    return tableField;
  }
  
  protected static String determineLabel(ITableField tableField)
  {
    String res;
    Context context = Context.getCurrent();
    if (context instanceof IClientContext)
      res = I18N.localizeLabel(tableField.getLabel(), (IClientContext) context);
    else
      res = I18N.localizeLabel(tableField.getLabel(), context, context.getApplicationLocale());
    
    // to detect I18N programmer error
    return res == null || res.length() == 0 ? tableField.getLabel() : res;
  }
}
