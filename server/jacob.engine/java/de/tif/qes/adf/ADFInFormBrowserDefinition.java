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

import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IRelation;
import de.tif.jacob.core.definition.guielements.Caption;
import de.tif.jacob.core.definition.guielements.Dimension;
import de.tif.jacob.core.definition.guielements.InFormBrowserDefinition;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler;
import de.tif.qes.IQeScriptContainer;
import de.tif.qes.adf.castor.Events;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ADFInFormBrowserDefinition extends InFormBrowserDefinition implements IQeScriptContainer
{
  private final Set scripts;
  
	/**
	 * @param name
	 * @param dimension
	 * @param visible
	 * @param tabIndex
	 * @param caption
	 * @param browserToUse
	 * @param relationToUse
	 * @param newMode
	 * @param updateMode
	 * @param deleteMode
	 */
	public ADFInFormBrowserDefinition(String name, Dimension dimension, boolean visible, int tabIndex, Caption caption, IBrowserDefinition browserToUse, IRelation relationToUse, boolean newMode, boolean updateMode, boolean deleteMode, boolean searchEnabled, Events events)
	{
	  // use default event handler
		super(name, null, null, dimension, visible, tabIndex, 0, caption, browserToUse, newMode, updateMode, deleteMode, searchEnabled,new SelectionActionEventHandler[0]);

    this.scripts = ADFScript.fetchScripts(events);
  }

	/* (non-Javadoc)
	 * @see de.tif.qes.IQeScriptContainer#getScripts()
	 */
	public Set getScripts()
	{
		return this.scripts;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.guielements.GUIElementDefinition#toJacob(de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement, de.tif.jacob.core.definition.impl.ConvertToJacobOptions)
	 */
	protected void toJacob(CastorGuiElement jacobGuiElement, ConvertToJacobOptions options)
	{
		super.toJacob(jacobGuiElement, options);
    
    ADFScript.putScriptsToProperties(getScripts(), options, this);
    jacobGuiElement.setProperty(getCastorProperties());
  }

}
