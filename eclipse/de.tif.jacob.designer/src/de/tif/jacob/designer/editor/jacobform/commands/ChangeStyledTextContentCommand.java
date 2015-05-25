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
import de.tif.jacob.designer.model.UILabelModel;
import de.tif.jacob.designer.model.UIStyledTextModel;

/**
 * Command to change the name field
 * 
 * @author Phil Zoio
 */
public class ChangeStyledTextContentCommand extends Command
{
	private UIStyledTextModel text;
	private String label;
	private String oldLabel;

	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute()
	{
	  text.setLabel(label);
	}

	/**
	 * @return whether we can apply changes
	 */
	public boolean canExecute()
	{
		if (label != null)
		{
			return true;
		}
		else
		{
			label = oldLabel;
			return false;
		}
	}

	/**
	 * Sets the new Column name
	 * 
	 * @param string
	 *            the new name
	 */
	public void setLabel(String string)
	{
		this.label = string;
	}

	/**
	 * Sets the old Column name
	 * 
	 * @param string
	 *            the old name
	 */
	public void setOldLabel(String string)
	{
		oldLabel = string;
	}

	/**
	 * @param table
	 *            The table to set.
	 */
	public void setStyledTextModel(UIStyledTextModel text)
	{
		this.text = text;
	}

	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo()
	{
		text.setLabel(oldLabel);
	}

}