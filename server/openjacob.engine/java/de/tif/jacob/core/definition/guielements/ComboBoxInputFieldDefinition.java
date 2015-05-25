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
import de.tif.jacob.core.definition.impl.jad.castor.ComboBox;
import de.tif.jacob.core.definition.impl.jad.castor.LocalInputField;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.IApplicationFactory;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ComboBoxInputFieldDefinition extends EnumInputFieldDefinition
{
	static public final transient String RCS_ID = "$Id: ComboBoxInputFieldDefinition.java,v 1.9 2010/03/25 14:41:30 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.9 $";

  private final boolean allowNullSearch; 
  private final boolean allowNotNullSearch; 

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
	public ComboBoxInputFieldDefinition(String name, String description, String inputHint, String eventHandler, String[] visibleValues, boolean callHookOnSelect,boolean allowUserDefinedValues, boolean allowNullSearch, boolean allowNotNullSearch, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex,
      Caption caption, ITableAlias localTableAlias, ITableField localTableField, FontDefinition font)
	{
		super(name, description, inputHint, eventHandler, visibleValues,  callHookOnSelect, allowUserDefinedValues, position, visible, readonly, tabIndex, paneIndex, caption, localTableAlias, localTableField, font);

	
    this.allowNotNullSearch = allowNotNullSearch;
    this.allowNullSearch = allowNullSearch;
	}

	public IGuiElement createRepresentation(IApplicationFactory factory, IApplication app, IGuiElement parent)
	{
		return factory.createComboBox(app, parent, this);
	}


  public boolean getAllowNullSearch()
  {
    return allowNullSearch;
  }

  public boolean getAllowNotNullSearch()
  {
    return allowNotNullSearch;
  }
  

  /*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.guielements.LocalInputFieldDefinition#toJacob(de.tif.jacob.core.jad.castor.LocalInputField)
	 */
	protected void toJacob(LocalInputField jacobLocalInputField)
	{
		ComboBox jacobComboBox = new ComboBox();
		
		jacobLocalInputField.setComboBox(jacobComboBox);
		
		// add all possible enum values to the combo box
		//
		for (int i = 0; i < getEnumCount(); i++)
		  jacobComboBox.addValue(this.getEnumEntry(i));
		
    jacobComboBox.setDimension(getDimension().toJacob());
		if (null != getCaption())
			jacobComboBox.setCaption(getCaption().toJacob());
	}

}
