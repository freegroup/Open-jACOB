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
 * Created on Jul 15, 2004
 */
package de.tif.jacob.designer.editor.relationset.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.RelationsetStickyNoteModel;

/**
 * Command to create a new table table
 * 
 * @author Phil Zoio
 */
public class AddStickyNoteCommand extends Command
{
	final private RelationsetModel relationSet;
	final private RelationsetStickyNoteModel  note;
	final private Rectangle  rect;
	
	public AddStickyNoteCommand(RelationsetModel relationSet, RelationsetStickyNoteModel alias,Rectangle rect)
	{
	  this.relationSet = relationSet;
	  this.note        = alias;
	  this.rect        = rect;
	}
	
	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute()
	{
	  System.out.println("New bounds :"+rect);
	  note.setRelationsetModel(relationSet);
		relationSet.addElement(note);
	  note.setBounds(rect.getCopy());
	}

 
	public void undo()
	{
		relationSet.removeElement(note);
	}
}