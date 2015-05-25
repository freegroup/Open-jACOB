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
/*
 * Created on 21.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.exception;

import de.tif.jacob.i18n.Message;

/**
 * This exception indicates that an user authentication has failed due to any reason.
 * 
 * @author Andreas Sonntag
 */
public class AuthenticationException extends UserException
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: AuthenticationException.java,v 1.1 2007/01/19 09:50:40 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.1 $";

  /**
   * Constructs a new authentication exception with the specified internationalized detail
   * message.
   * 
   * @param message
   *          the internationalized detail message
   */
	public AuthenticationException(Message message)
	{
		super(message);
	}

	/**
   * Constructs a new authentication exception with the specified detail message.
   * <p>
   * Note: This constructor should only be used, if your application is not
   * designed for multi language support! To internationalize the exception use
   * {@link #AuthenticationException(Message)}.
   * 
   * @param message
   *          the detail message
	 */
	public AuthenticationException(String message)
	{
		super(message);
	}
}
