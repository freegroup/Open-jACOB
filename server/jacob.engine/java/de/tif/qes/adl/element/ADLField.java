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

import de.tif.jacob.core.definition.impl.AbstractTableField;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class ADLField extends AbstractTableField
{
  static public final transient String RCS_ID = "$Id: ADLField.java,v 1.1 2006-12-21 11:32:20 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  private final boolean systemField;
  private final boolean historyField;
  
	public ADLField(String name, String dbname, ADLFieldType type, String label, Boolean readonly, Boolean not_null, Boolean systemfield, Boolean history, Boolean historyfield)
	{
    // Note: not_null seams to be a ADL 5.0 feature 
    super(name, dbname, label, null, type.getCommonFieldType(), type.isRequired() || not_null.booleanValue(), readonly.booleanValue(), history.booleanValue());
    this.systemField = systemfield.booleanValue();
    this.historyField = historyfield.booleanValue(); 
	}

	/**
	 * @return Returns the historyField.
	 */
	public final boolean isHistoryField()
	{
		return historyField;
	}

	/**
	 * @return Returns the systemField.
	 */
	public final boolean isSystemField()
	{
		return systemField;
	}

}
