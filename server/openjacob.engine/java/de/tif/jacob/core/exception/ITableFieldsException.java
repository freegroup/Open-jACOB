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

import java.util.Collection;
import java.util.Locale;

import de.tif.jacob.core.definition.ITableField;

/**
 * Internal interface to recognize exceptions referenzing multiple table fields.
 * 
 * @author Andreas Sonntag
 * @since 2.7.2
 */
public interface ITableFieldsException
{
  /**
   * Returns the list of table fields which are affected by this exception.
   * 
   * @return List of {@link ITableField}
   */
  public Collection getTableFields();
  
  public String getLocalizedMessage(Locale locale);

  public String getMessage();
  
  public String getDetails();
}
