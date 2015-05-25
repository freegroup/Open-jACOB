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

import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.core.definition.IToolbarButtonDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ToolbarButtonDefinition implements IToolbarButtonDefinition 
{
  static public final transient String RCS_ID = "$Id: ToolbarButtonDefinition.java,v 1.1 2007/01/19 09:50:28 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private final ActionType actionType;
  private final String label;
  private final String name;
  private final boolean visible;

  /**
	 * @param name
	 * @param caption
	 * @param top
	 * @param left
	 * @param width
	 * @param height
	 */
	public ToolbarButtonDefinition(String name, String label, boolean visible, ActionType actionType)
	{
    this.name = name;
    this.visible = visible;
    this.actionType = actionType;
    this.label = label;
	}

	/**
	 * @return Returns the actionType.
	 */
	public final ActionType getActionType()
	{
		return actionType;
	}

	/**
	 * @return Returns the label.
	 */
	public final String getLabel()
	{
		return label;
	}


  // IBIS: Frage:was muss hier getan werden?
  protected final void toJacob(CastorGuiElement jacobGuiElement)
  {
    throw new RuntimeException("function [de.tif.jacob.core.definition.guielements.ToolbarButtonDefinition.toJacob(CastorGuiElement jacobGuiElement)] not implemented at the moment.");
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IToolbarButtonDefinition#getName()
	 */
	public String getName()
	{
		return this.name;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IToolbarButtonDefinition#isVisible()
	 */
	public boolean isVisible()
	{
		return this.visible;
	}

}
