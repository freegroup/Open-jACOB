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

/**
 * This exception indicates that a foreign key constraint violation has
 * occurred. This is the case, if a data table record is deleted which is still
 * referenced by another data table record.
 * 
 * @author Andreas Sonntag
 * @since 2.7.2
 */
public class ForeignConstraintViolationException extends UserException
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: ForeignConstraintViolationException.java,v 1.2 2008/03/26 15:56:54 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.2 $";

  /**
   * Constructs the <code>UniqueViolationException</code>.
   * 
   * @param details
   *          detailed information
   */
  public ForeignConstraintViolationException(String details)
  {
    super(new CoreMessage(CoreMessage.FOREIGN_CONSTRAINT_VIOLATION), details);
  }

}
