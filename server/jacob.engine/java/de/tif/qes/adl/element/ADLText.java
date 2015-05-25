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

import de.tif.jacob.core.definition.fieldtypes.LongTextFieldType;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ADLText extends ADLFieldType
{
	static public final transient String RCS_ID = "$Id: ADLText.java,v 1.1 2006-12-21 11:32:20 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	private static final String DEFAULT_PREFIX = "DEFAULT:";

	public ADLText(String desc, Boolean required)
	{
		// Hack: for Quintus always enable change header
		super(new LongTextFieldType(getDefaultValue(desc), true), required);
	}

	/**
   * Quintus does not allow default values for TEXT (i.e. LongTextField).
   * This method performs a workaround solution to configure a default value
   * by means of the description field. In this case the description field
   * must be set as follows: DEFAULT:<default value>.
   * 
	 * @param description the description field
	 * @return the extracted default value or <code>null</code>
	 */
	private static String getDefaultValue(String description)
	{
		if (null != description && description.toUpperCase().startsWith(DEFAULT_PREFIX))
		{
			return description.substring(DEFAULT_PREFIX.length());
		}
		return null;
	}
}
