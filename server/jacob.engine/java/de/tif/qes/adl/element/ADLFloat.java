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

package de.tif.qes.adl.element;

import de.tif.jacob.core.definition.fieldtypes.FloatFieldType;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ADLFloat extends ADLFieldType
{
  static public final transient String RCS_ID = "$Id: ADLFloat.java,v 1.1 2006-12-21 11:32:19 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  public ADLFloat(String max, String min, String desc, Double def, Boolean required)
  {
    super(new FloatFieldType(def == null ? null : new Float(def.floatValue()), toFloat(min), toFloat(max)), required);
  }
  
  protected static Float toFloat(String value)
  {
    if (value != null)
    {
      value = value.trim();
      if (value.length() > 0)
      {
        return new Float(value);
      }
    }
    return null;
  }
}
