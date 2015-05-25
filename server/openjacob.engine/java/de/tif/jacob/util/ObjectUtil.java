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

package de.tif.jacob.util;

/**
 * @author Andreas Sonntag
 * 
 */
public class ObjectUtil
{
  static public final transient String RCS_ID = "$Id: ObjectUtil.java,v 1.1 2007/01/19 09:50:50 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * <p>Compares two objects for equality, where either one or both
	 * objects may be <code>null</code>.</p>
	 *
	 * <pre>
	 * ObjectUtils.equals(null, null)                  = <b>true</b>
	 * ObjectUtils.equals(null, "")                    = false
	 * ObjectUtils.equals("", null)                    = false
	 * ObjectUtils.equals("", "")                      = true
	 * ObjectUtils.equals(Boolean.TRUE, null)          = false
	 * ObjectUtils.equals(Boolean.TRUE, "true")        = false
	 * ObjectUtils.equals(Boolean.TRUE, Boolean.TRUE)  = true
	 * ObjectUtils.equals(Boolean.TRUE, Boolean.FALSE) = false
	 * </pre>
	 *
	 * @param object1  the first object, may be <code>null</code>
	 * @param object2  the second object, may be <code>null</code>
	 * @return <code>true</code> if the values of both objects are the same
	 */
	public static boolean equals(Object object1, Object object2)
	{
		if (object1 == object2)
		{
			return true;
		}
		if ((object1 == null) || (object2 == null))
		{
			return false;
		}
		return object1.equals(object2);
	}
	
	/**
	 * <p>Compares two objects for equality, where either one or both
	 * objects may be <code>null</code>.</p>
	 *
	 * <pre>
	 * ObjectUtils.equals(null, null)                  = <b>false</b>
	 * ObjectUtils.equals(null, "")                    = false
	 * ObjectUtils.equals("", null)                    = false
	 * ObjectUtils.equals("", "")                      = true
	 * ObjectUtils.equals(Boolean.TRUE, null)          = false
	 * ObjectUtils.equals(Boolean.TRUE, "true")        = false
	 * ObjectUtils.equals(Boolean.TRUE, Boolean.TRUE)  = true
	 * ObjectUtils.equals(Boolean.TRUE, Boolean.FALSE) = false
	 * </pre>
	 *
	 * @param object1  the first object, may be <code>null</code>
	 * @param object2  the second object, may be <code>null</code>
	 * @return <code>true</code> if the values of both objects are the same
	 */
	public static boolean equalsIgnoreNull(Object object1, Object object2)
	{
		if ((object1 == null) || (object2 == null))
		{
			return false;
		}
		if (object1 == object2)
		{
			return true;
		}
		return object1.equals(object2);
	}
	
}
