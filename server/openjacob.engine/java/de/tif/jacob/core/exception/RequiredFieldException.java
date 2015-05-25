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

import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.i18n.CoreMessage;

/**
 * This exception indicates that a required table field has not been set when
 * committing a data table record.
 * 
 * @author Andreas Sonntag
 */
public class RequiredFieldException extends AbstractTableFieldException
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: RequiredFieldException.java,v 1.5 2008/03/19 14:03:43 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.5 $";


  /**
   * Constructs the <code>RequiredFieldException</code> with the application specific user message.
   * 
   * @param tableField
   *          the required table field
   */
  public RequiredFieldException(ITableField tableField, String message)
  {
    super(tableField, message);
  }
  
  /**
   * Constructs the <code>RequiredFieldException</code> with the application specific user message.
   * 
   * @param tableField
   *          the required table field
   */
  public RequiredFieldException(ITableField tableField, ApplicationMessage message)
  {
    super(tableField, message);
  }
  
  /**
   * Constructs the <code>RequiredFieldException</code>.
   * 
   * @param tableField
   *          the required table field
   */
  public RequiredFieldException(ITableField tableField)
  {
    super(tableField, new CoreMessage(CoreMessage.REQUIRED_FIELD_MISSING, determineLabel(tableField)));
  }
}
