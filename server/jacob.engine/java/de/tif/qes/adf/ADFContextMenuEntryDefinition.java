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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.core.definition.impl.ContextMenuEntryDefinition;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry;
import de.tif.qes.IQeScriptContainer;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ADFContextMenuEntryDefinition extends ContextMenuEntryDefinition implements IQeScriptContainer
{
	private final Set scripts;
  
	/**
	 * @param name
	 * @param label
	 * @param actionType
	 */
	public ADFContextMenuEntryDefinition(String name, String label, ActionType actionType, ADFScript script)
	{
		super(name, label, null, actionType);
    
    Set scripts = new HashSet();
    if (script != null)
    	scripts.add(script);
    this.scripts = Collections.unmodifiableSet(scripts);
	}

	/* (non-Javadoc)
	 * @see de.tif.qes.IQeScriptContainer#getScripts()
	 */
	public Set getScripts()
	{
		return this.scripts;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.ContextMenuEntryDefinition#toJacob(de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry)
	 */
	protected void toJacob(ContextMenuEntry jacobContextMenuEntry, ConvertToJacobOptions options)
	{
		super.toJacob(jacobContextMenuEntry, options);
    
    ADFScript.putScriptsToProperties(getScripts(), options, this);
    jacobContextMenuEntry.setProperty(getCastorProperties());
  }

}
