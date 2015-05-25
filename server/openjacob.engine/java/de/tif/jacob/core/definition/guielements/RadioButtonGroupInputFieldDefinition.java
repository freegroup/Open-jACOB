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
import de.tif.jacob.core.definition.impl.jad.castor.LocalInputField;
import de.tif.jacob.core.definition.impl.jad.castor.RadioButtonGroup;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.IApplicationFactory;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class RadioButtonGroupInputFieldDefinition extends EnumInputFieldDefinition
{
	static public final transient String RCS_ID = "$Id: RadioButtonGroupInputFieldDefinition.java,v 1.8 2010/02/24 14:33:51 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.8 $";

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
	public RadioButtonGroupInputFieldDefinition(String name, String description, String inputHint, String eventHandler, String[] visibleValues, boolean callHookOnSelect, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex,
      Caption caption, ITableAlias localTableAlias, ITableField localTableField)
	{
		super(name, description, inputHint, eventHandler, visibleValues, callHookOnSelect, false, position, visible, readonly, tabIndex, paneIndex, caption, localTableAlias, localTableField, null);
	}

	public IGuiElement createRepresentation(IApplicationFactory factory, IApplication app, IGuiElement parent)
	{
		return factory.createRadioButtonGroup(app, parent, this);
	}

	protected void toJacob(LocalInputField jacobLocalInputField)
	{
    RadioButtonGroup jacobRadioButtonGroup = new RadioButtonGroup();
    
    jacobLocalInputField.setRadioButtonGroup(jacobRadioButtonGroup);
    
    // add all possible enum values to the combo box
    //
    for (int i = 0; i < getEnumCount(); i++)
      jacobRadioButtonGroup.addValue(this.getEnumEntry(i));
    
    jacobRadioButtonGroup.setDimension(getDimension().toJacob());
    if (null != getCaption())
      jacobRadioButtonGroup.setCaption(getCaption().toJacob());
	}

}
