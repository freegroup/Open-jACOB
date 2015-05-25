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

import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import de.tif.jacob.designer.model.RelationModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.TableAliasModel;

/**
 * Command to create a new table table
 * 
 * @author Phil Zoio
 */
public class AddTableAliasCommand extends Command
{
	final private RelationsetModel relationSet;
	final private TableAliasModel  alias;
	final private Point            pos;
	private List addedRelations;
	
	public AddTableAliasCommand(RelationsetModel relationSet, TableAliasModel alias,Point pos)
	{
	  this.relationSet = relationSet;
	  this.alias       = alias;
	  this.pos         = pos;
	}
	
	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute()
	{
	  addedRelations = relationSet.addElement(alias);
		relationSet.setTableAliasBounds(alias,new Rectangle(pos.x,pos.y,-1,-1));
	}

 
	public void undo()
	{
	  Iterator iter = addedRelations.iterator();
	  while (iter.hasNext())
    {
      RelationModel relation = (RelationModel) iter.next();
      relationSet.removeElement(relation);
    }
    relationSet.removeTmpElement(alias);
	}
}