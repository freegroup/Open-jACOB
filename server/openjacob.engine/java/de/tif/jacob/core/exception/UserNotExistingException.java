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
 * Created on 22.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.exception;

import de.tif.jacob.i18n.CoreMessage;

/**
 * This exception indicates that a user with a given id does not exist.
 * 
 * @author Andreas Sonntag
 */
public class UserNotExistingException extends UserException
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: UserNotExistingException.java,v 1.1 2007/01/19 09:50:40 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.1 $";

	/**
   * Constructs the <code>UserNotExistingException</code>.
   * 
   * @deprecated use {@link #UserNotExistingException(String)}
	 */
	public UserNotExistingException()
	{
	  super(new CoreMessage(CoreMessage.USER_NOT_EXISTING));
	}
	
  /**
   * Constructs the <code>UserNotExistingException</code>.
   * 
   * @param userId
   *          the id of the user not existing
   */
  public UserNotExistingException(String userId)
  {
    super(new CoreMessage(CoreMessage.USER_NOT_EXISTING, userId));
  }

}
