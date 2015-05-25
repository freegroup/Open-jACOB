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

package de.tif.jacob.core.definition.guielements;

import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class EnumInputFieldDefinition extends LocalInputFieldDefinition
{
	static public final transient String RCS_ID = "$Id: EnumInputFieldDefinition.java,v 1.7 2010/02/24 14:33:51 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.7 $";

  private final String[] values;
	private final boolean  callHookOnSelect;
  private final boolean allowUserDefinedValue;
  
	/**
	 * @param name
	 * @param eventHandler
	 * @param values
	 * @param position
	 * @param visible
	 * @param readonly
	 * @param tabIndex
	 * @param caption
	 * @param localTableAlias
	 * @param localTableField
	 */
	public EnumInputFieldDefinition(String name, String description,String inputHint, String eventHandler, String[] visibleValues, boolean callHookOnSelect, boolean allowUserDefinedValues, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex,
      Caption caption, ITableAlias localTableAlias, ITableField localTableField, FontDefinition font)
	{
		super(name, description,inputHint, eventHandler, position, visible, readonly, tabIndex, paneIndex, caption, localTableAlias, localTableField, font, null);
		this.values=visibleValues;
    this.callHookOnSelect = callHookOnSelect;
    this.allowUserDefinedValue = allowUserDefinedValues;

  }

	public final int getEnumCount()
	{
	  return values.length;
	}


  public final String getEnumEntry(int index)
	{
	  return values[index];
	}
  
  public final boolean getCallHookOnSelect()
  {
    return callHookOnSelect;
  }
  
  public boolean getAllowUserDefinedValue()
  {
    return allowUserDefinedValue;
  }
}
