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

import java.util.StringTokenizer;

import de.tif.jacob.core.definition.fieldtypes.TimestampFieldType;
import de.tif.jacob.core.definition.fieldtypes.TimestampResolution;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ADLDatetime extends ADLFieldType
{
  static public final transient String RCS_ID = "$Id: ADLDatetime.java,v 1.1 2006-12-21 11:32:21 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  public ADLDatetime(String desc, Boolean required)
  {
    // use description to set default value
    super(new TimestampFieldType(getDefault(desc), getResolution(desc)), required);
  }
  
  private static String getDefault(String desc)
  {
    return containsToken(desc, "NOW") ? "now" : null;
  }
  
  private static TimestampResolution getResolution(String desc)
  {
    if (containsToken(desc, "SEC_BASE"))
      return TimestampResolution.SEC_BASE;
    if (containsToken(desc, "MSEC_BASE"))
      return TimestampResolution.MSEC_BASE;

    // min base is default
    // IBIS: hack to avoid secs which could not be entered by HTML calendar input control  
    return TimestampResolution.MIN_BASE;
  }
  
  public static boolean containsToken(String str, String token)
  {
    if (null != str)
    {
      StringTokenizer st = new StringTokenizer(str);
      while (st.hasMoreTokens())
      {
        if (st.nextToken().equals(token))
          return true;
      }
    }
    return false;
  }
 }
