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
import de.tif.jacob.i18n.Message;

/**
 * This exception indicates that an authorization problem has occured, i.e.
 * insufficient rights or no rights at all exist to perform the desired
 * operation.
 * 
 * @author andreas
 */
public class AuthorizationException extends UserException
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: AuthorizationException.java,v 1.1 2007/01/19 09:50:40 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.1 $";

	/**
   * Constructs the <code>AuthorizationException</code>.
	 */
	public AuthorizationException()
	{
		super(new CoreMessage(CoreMessage.INSUFFICIENT_ACCESS_RIGHTS));
	}

	/**
   * Constructs the <code>AuthorizationException</code>.
   * 
   * @param message
   *          the internationalized detail message
	 */
	public AuthorizationException(Message message)
	{
		super(message);
	}

	/**
   * Constructs the <code>AuthorizationException</code>.
   * <p>
   * Note: This constructor should only be used, if your application is not
   * designed for multi language support! To internationalize the exception use
   * {@link #AuthorizationException(Message)}.
   * 
   * @param message
   *          the detail message
	 */
	public AuthorizationException(String message)
	{
		super(message);
	}

}
