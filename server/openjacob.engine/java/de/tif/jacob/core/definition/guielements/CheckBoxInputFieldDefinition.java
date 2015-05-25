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
import de.tif.jacob.core.definition.impl.jad.castor.CheckBox;
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
public class CheckBoxInputFieldDefinition extends LocalInputFieldDefinition
{
  static public final transient String RCS_ID = "$Id: CheckBoxInputFieldDefinition.java,v 1.3 2009/02/11 12:18:04 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

	public CheckBoxInputFieldDefinition(String name, String description, String inputHint, String eventHandler, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex, Caption caption, ITableAlias localTableAlias, ITableField localTableField)
	{
		super(name, description,inputHint, eventHandler, position, visible, readonly, tabIndex, paneIndex, caption, localTableAlias, localTableField, null, null);
	}


  public IGuiElement createRepresentation(IApplicationFactory factory, IApplication app, IGuiElement parent )
  {
    return factory.createCheckBox(app, parent,this); 
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.guielements.LocalInputFieldDefinition#toJacob(de.tif.jacob.core.jad.castor.LocalInputField)
	 */
	protected void toJacob(LocalInputField jacobLocalInputField)
	{
    CheckBox jacobCheckBox = new CheckBox();
    jacobLocalInputField.setCheckBox(jacobCheckBox);
    jacobCheckBox.setDimension(getDimension().toJacob());
    if (null != getCaption())
    	jacobCheckBox.setCaption(getCaption().toJacob());
  }

}
