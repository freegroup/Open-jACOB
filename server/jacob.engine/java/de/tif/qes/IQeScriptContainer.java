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

package de.tif.qes;

import java.util.Set;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IQeScriptContainer
{
  public final static String SCRIPTS_PROPERTY = "QES_SCRIPTS";
  
  // properties of first script of an script container
  // info of further scripts only in SCRIPTS_PROPERTY
  public final static String SCRIPT_FILE_PROPERTY = "QES_SCRIPT_FILE";
  public final static String SCRIPT_NAME_PROPERTY = "QES_SCRIPT_NAME";
  public final static String SCRIPT_TYPE_PROPERTY = "QES_SCRIPT_TYPE";
  
  /**
   * Returns the scripts contained within this object (container).
   * 
   * @return Set{IQeScript}
   */
  public Set getScripts();
  
}
