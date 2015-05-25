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
 * This exception indicates that the implementation (type) of a required data
 * source is not available. This is the case for jACOB demo installations, which
 * do not support commercial databases.
 * 
 * @author Andreas Sonntag
 */
public class UnavailableDatasourceException extends UserRuntimeException
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: UnavailableDatasourceException.java,v 1.2 2010/07/07 14:10:36 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.2 $";

  /**
   * Constructs the <code>UnavailableDatasourceException</code>.
   * 
   * @param name
   *          the name of the unavailable data source
   * @param type
   *          the type of the unavailable data source
   */
  public UnavailableDatasourceException(String name, String type)
  {
    super(new CoreMessage(CoreMessage.UNAVAILABLE_DATASOURCE_TYPE, name, type));
  }

}
