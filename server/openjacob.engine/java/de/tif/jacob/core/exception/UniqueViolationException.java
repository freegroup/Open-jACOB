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
 * Created on 15.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.exception;

import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.i18n.Message;

/**
 * This exception indicates that a unique key violation has been occurred. This
 * is the case, if a data table record is going to be modified in such a way,
 * that a unique constraint is not fulfilled anymore.
 * 
 * @author Andreas Sonntag
 */
public class UniqueViolationException extends UserException
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: UniqueViolationException.java,v 1.4 2008/07/09 14:27:35 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.4 $";

	/**
   * Constructs the <code>UniqueViolationException</code>.
   * 
	 * @param details detailed information
	 */
  public UniqueViolationException(String details)
  {
    super(new CoreMessage(CoreMessage.UNIQUE_VIOLATION), details);
  }

  protected UniqueViolationException(Message message, String details)
  {
    super(message, details);
  }
}
