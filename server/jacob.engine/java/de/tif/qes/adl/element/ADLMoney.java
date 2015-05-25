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

import java.math.BigDecimal;

import de.tif.jacob.core.definition.fieldtypes.DecimalFieldType;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ADLMoney extends ADLFieldType
{
  static public final transient String RCS_ID = "$Id: ADLMoney.java,v 1.1 2006-12-21 11:32:20 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  public ADLMoney(String symbol, Integer scale, String max, String min, String desc, Double def, Boolean required)
	{
    // IBIS : What to do with currency symbol?
		super(new DecimalFieldType(scale == null ? 2 : scale.shortValue(), def == null ? null : new BigDecimal(def.doubleValue()), toDecimal(min), toDecimal(max)), required);
	}
  
  private static BigDecimal toDecimal(String value)
  {
    if (value != null)
    {
      value = value.trim();
      if (value.length() > 0)
      {
        return new BigDecimal(value);
      }
    }
    return null;
  }
}
