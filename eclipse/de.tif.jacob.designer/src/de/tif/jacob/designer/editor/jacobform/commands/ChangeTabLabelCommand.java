/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
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
/*
 * Created on Oct 20, 2004
 *
 */
package de.tif.jacob.designer.editor.jacobform.commands;

import org.eclipse.gef.commands.Command;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UITabModel;

/**
 * Command to change the name field
 * 
 * @author Phil Zoio
 */
public class ChangeTabLabelCommand extends Command
{
	private final UITabModel tab;
	private final String newLabel;
	private final String oldLabel;

	public ChangeTabLabelCommand(UITabModel tab, String oldLabel, String newLabel)
	{
		this.tab = tab;
		this.newLabel = newLabel;
		this.oldLabel = oldLabel;
	}
	
	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute()
	{
		tab.setLabel(newLabel);
	}

	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo()
	{
		tab.setLabel(oldLabel);
	}

}