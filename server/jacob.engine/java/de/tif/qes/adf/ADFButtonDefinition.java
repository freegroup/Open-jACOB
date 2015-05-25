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

package de.tif.qes.adf;

import java.util.Set;

import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.core.definition.guielements.ButtonDefinition;
import de.tif.jacob.core.definition.guielements.Dimension;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.qes.IQeScriptContainer;
import de.tif.qes.adf.castor.Events;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ADFButtonDefinition extends ButtonDefinition implements IQeScriptContainer
{
  static public transient final String RCS_ID = "$Id: ADFButtonDefinition.java,v 1.6 2009-12-06 13:14:13 sonntag Exp $";
  static public transient final String RCS_REV = "$Revision: 1.6 $";
  
  private final Set scripts;

	/**
	 * @param name
	 * @param label
	 * @param visible
	 * @param tabIndex
	 * @param actionType
	 * @param position
	 */
	public ADFButtonDefinition(String name, String label, boolean visible, int tabIndex, ActionType actionType, Dimension position, Events events)
	{
		super(name, null, null, label == null ? name : label, visible, false, null, tabIndex, 0, actionType, position, null, -1, null, null);

		if (label == null)
		{
		  System.err.println("### Warning: Button "+name+" has no label defined (using name as label)!");
		}
		
		this.scripts = ADFScript.fetchScripts(events);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.qes.IQeScriptContainer#getScripts()
	 */
	public Set getScripts()
	{
		return this.scripts;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.guielements.GUIElementDefinition#toJacob(de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement)
	 */
	protected void toJacob(CastorGuiElement jacobGuiElement, ConvertToJacobOptions options)
	{
		super.toJacob(jacobGuiElement, options);

    ADFScript.putScriptsToProperties(getScripts(), options, this);
    jacobGuiElement.setProperty(getCastorProperties());
  }

}
