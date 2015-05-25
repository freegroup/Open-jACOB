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

/**
 * This exception indicates that an certain configuration property is missing.
 * 
 * @author Andreas Sonntag
 */
public class MissingPropertyException extends RuntimeException
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: MissingPropertyException.java,v 1.1 2007/01/19 09:50:40 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.1 $";

  /**
   * Constructs the <code>MissingPropertyException</code>.
   * 
   * @param message
   *          an additional message
   * @param propertyName
   *          the name of the missing property
   */
  public MissingPropertyException(String message, String propertyName)
  {
    super(message+": Missing property '"+propertyName+"'");
  }

  /**
   * Constructs the <code>MissingPropertyException</code>.
   * 
   * @param propertyName
   *          the name of the missing property
   */
  public MissingPropertyException(String propertyName)
  {
    super("Missing property '"+propertyName+"'");
  }
}
