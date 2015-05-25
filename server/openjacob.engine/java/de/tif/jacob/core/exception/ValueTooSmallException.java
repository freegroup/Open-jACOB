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

import de.tif.jacob.i18n.CoreMessage;

/**
 * This exception indicates that a given value is too small.
 * 
 * @author Andreas Sonntag
 */
public class ValueTooSmallException extends InvalidExpressionException
{
	/**
	 * The internal revision control system id.
	 */
	public static transient final String RCS_ID = "$Id: ValueTooSmallException.java,v 1.1 2007/01/19 09:50:40 freegroup Exp $";
	
	/**
	 * The internal revision control system id in short form.
	 */
	public static transient final String RCS_REV = "$Revision: 1.1 $";

  /**
   * Constructs the <code>ValueTooSmallException</code>.
   * 
   * @param value
   *          the value which is too small
   * @param min
   *          the minimum value
   */
  public ValueTooSmallException(Object value, Object min)
  {
    super(new CoreMessage(CoreMessage.VALUE_TOO_SMALL_EXPRESSION, value, min), value.toString());
  }
}
