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

package de.tif.jacob.report.impl.transformer.base.util;

import de.tif.jacob.core.exception.UserRuntimeException;

/**
 * Helper class to split an alias field name such like "" into alias name and
 * field name.
 * 
 * @author Andreas Sonntag
 * @since 2.9
 */
public final class AliasFieldNameSplitter
{
  static public transient final String RCS_ID = "$Id: AliasFieldNameSplitter.java,v 1.2 2009/12/09 09:46:43 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";

  public final String aliasName;

  public final String fieldName;

  public AliasFieldNameSplitter(String aliasFieldName)
  {
    int index = aliasFieldName.indexOf('.');
    if (index < 0)
      throw new UserRuntimeException("Field name '" + aliasFieldName + "' does not contain alias name");
    this.aliasName = aliasFieldName.substring(0, index);
    this.fieldName = aliasFieldName.substring(index + 1);
  }
}
