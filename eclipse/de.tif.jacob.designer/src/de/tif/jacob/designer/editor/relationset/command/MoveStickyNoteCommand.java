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
 * Created on Jul 20, 2004
 */
package de.tif.jacob.designer.editor.relationset.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import de.tif.jacob.designer.model.RelationsetStickyNoteModel;

/**
 * Command to move the bounds of an existing table. Only used with
 * XYLayoutEditPolicy (manual layout)
 * 
 * @author Phil Zoio
 */
public class MoveStickyNoteCommand extends Command
{
	final private RelationsetStickyNoteModel  note;
	final private Rectangle        oldBounds;
	final private Rectangle        newBounds;

	public MoveStickyNoteCommand(RelationsetStickyNoteModel note, Rectangle newBounds)
	{
		this.note       = note;
		this.oldBounds  = note.getBounds();
		this.newBounds  = newBounds;
	}
	
	public void execute()
	{
	  note.setBounds(newBounds);
	}

	public void undo()
	{
	  note.setBounds(oldBounds);
	}
}