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

package de.tif.jacob.core.data.impl.misc;

import java.util.Locale;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.ITableFieldException;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IClientContext;

/**
 * This exception is used only to "append" the responsible field, which "caused"
 * the original <code>InvalidExpressionException</code>, to the exception.
 * 
 * @author Andreas Sonntag
 */
public class InvalidFieldExpressionException extends InvalidExpressionException implements ITableFieldException
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: InvalidFieldExpressionException.java,v 1.3 2008/02/21 17:33:23 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.3 $";

  private final ITableField tableField;

  /**
   * Constructs the <code>InvalidFieldExpressionException</code>.
   * 
   * @param tableField
   *          the table field causing the invalid expression
   * @param cause
   *          the cause
   * @param cause
   *          a further cause
   */
  public InvalidFieldExpressionException(ITableField tableField, InvalidExpressionException cause)
  {
    super(new CoreMessage("MSG_FIELD_INFO", determineLabel(tableField)), cause.getExpression(), cause);
    this.tableField = tableField;
  }

  private static String determineLabel(ITableField tableField)
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

  /**
   * Returns the causing table field.
   * 
   * @return the causing table field
   */
  public final ITableField getTableField()
  {
    return tableField;
  }
  
  public String getLocalizedMessage(Locale locale)
  {
    String fieldInfo = ((InvalidExpressionException) getCause()).getLocalizedMessage(locale);
    return new CoreMessage("MSG_FIELD_INFO", determineLabel(tableField), fieldInfo).print(locale);
  }
  
  public String getMessage()
  {
    String fieldInfo = ((InvalidExpressionException) getCause()).getLocalizedMessage();
    return new CoreMessage("MSG_FIELD_INFO", determineLabel(tableField), fieldInfo).print();
  }
}
