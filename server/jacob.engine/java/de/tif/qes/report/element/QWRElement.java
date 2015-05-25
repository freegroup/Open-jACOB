/*******************************************************************************
 *    This file is part of jACOB
 *    Copyright (C) 2005-2009 Tarragon GmbH
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

package de.tif.qes.report.element;


/**
 * @author Andreas Sonntag
 */
public abstract class QWRElement
{
  protected static String[] splitAliasFieldName(String aliasFieldName)
  {
    int index = aliasFieldName.indexOf('.');
    if (index < 0)
      throw new RuntimeException("Field name '" + aliasFieldName + "' does not contain alias name");
    return new String[] {
        aliasFieldName.substring(0, index), aliasFieldName.substring(index + 1) };
  }
	
}
