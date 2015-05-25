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

package de.tif.jacob.core.definition.impl;

import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.core.definition.IContextMenuEntry;
import de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ContextMenuEntryDefinition extends AbstractGuiElement implements IContextMenuEntry
{
  static public transient final String RCS_ID = "$Id: ContextMenuEntryDefinition.java,v 1.1 2007/01/19 09:50:34 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";
  
  private final String label;
  private final ActionType actionType;

	/**
	 * 
	 */
	public ContextMenuEntryDefinition(String name, String label, String eventHandler, ActionType actionType)
	{
    super(name, null, eventHandler);
    this.label = label;
    this.actionType = actionType;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IContextMenuEntry#getLabel()
	 */
	public String getLabel()
	{
		return this.label;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IContextMenuEntry#getActionType()
	 */
	public ActionType getActionType()
	{
		return this.actionType;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IContextMenuEntry#isVisible()
	 */
	public boolean isVisible()
	{
		// always visible, i.e. within Quintus menu items can not be set invisible
		return true;
	}

  /**
   * @param jacobContextMenuEntry
   */
  protected void toJacob(ContextMenuEntry jacobContextMenuEntry, ConvertToJacobOptions options)
  {
    jacobContextMenuEntry.setName(getName());
    jacobContextMenuEntry.setLabel(getLabel());
    jacobContextMenuEntry.setVisible(isVisible());
    jacobContextMenuEntry.setAction(this.actionType.toJacob());
    
    // handle properties
    jacobContextMenuEntry.setProperty(getCastorProperties());
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition, de.tif.jacob.core.definition.impl.AbstractElement)
	 */
	public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
	{
    // nothing to do here
	}

}
