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

import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IOneToManyRelation;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.guielements.Caption;
import de.tif.jacob.core.definition.guielements.Dimension;
import de.tif.jacob.core.definition.guielements.ForeignInputFieldDefinition;
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
public class ADFForeignInputFieldDefinition extends ForeignInputFieldDefinition implements IQeScriptContainer
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
	 * @param relationSet
	 * @param fillDirection
	 * @param foreignTableField
	 */
	public ADFForeignInputFieldDefinition(
		String name,
		Dimension dimension,
		boolean visible,
		int tabIndex,
		Caption caption,
		IBrowserDefinition browserToUse,
		IOneToManyRelation relationToUse,
		IRelationSet relationSet,
		Filldirection fillDirection,
		ITableField foreignTableField, Events events)
	{
		super(name, null, null, null, dimension, visible, false,false, tabIndex, 0, caption, browserToUse, relationToUse, relationSet, fillDirection, foreignTableField, null);

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
