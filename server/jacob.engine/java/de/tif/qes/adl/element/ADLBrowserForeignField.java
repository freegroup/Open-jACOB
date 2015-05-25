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

import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.definition.impl.AbstractBrowserForeignTableField;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ADLBrowserForeignField extends AbstractBrowserForeignTableField
{
	static public final transient String RCS_ID = "$Id: ADLBrowserForeignField.java,v 1.2 2007-10-26 19:34:30 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * @param name
	 * @param tablealias
	 * @param tablefield
	 * @param label
	 * @param sortorder
	 * @param readonly
	 * @param invisible
	 * @param foreignBrowserName
	 * @param relationsetName
	 * @param filldirection
	 * @param relationName
	 */
	public ADLBrowserForeignField(
		String name,
		String tablealias,
		String tablefield,
		String label,
		SortOrder sortorder,
		Boolean readonly,
		Boolean invisible,
		String foreignBrowserName,
		String relationsetName,
		Filldirection filldirection,
		String relationName)
	{
		super(name, tablealias, tablefield, label, sortorder, !invisible.booleanValue(), readonly.booleanValue(),false, foreignBrowserName, relationsetName, filldirection, relationName);
	}

}
