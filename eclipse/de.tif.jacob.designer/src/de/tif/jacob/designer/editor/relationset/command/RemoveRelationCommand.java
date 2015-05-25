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
 * Created on Jul 17, 2004
 */
package de.tif.jacob.designer.editor.relationset.command;

import org.eclipse.gef.commands.Command;
import de.tif.jacob.designer.model.RelationModel;
import de.tif.jacob.designer.model.RelationsetModel;

/**
 * Command to delete relationship
 * 
 * @author Phil Zoio
 */
public class RemoveRelationCommand extends Command
{
	private RelationModel    relation;
	private RelationsetModel relationset;
	private boolean          removedComplete;
	
	public RemoveRelationCommand(RelationsetModel relationset, RelationModel relation)
	{
		this.relation    = relation;
		this.relationset = relationset;
	}

	/**
	 * @see Removes the relationship
	 */
	public void execute()
	{
	  relationset.removeElement(relation);
	  // Eine Relation wird einzeln nicht in Scripten verwendet!
	  // Diese kann somit entfernt werden wenn keine Referenz im JAD mehr vorkommt.
	  //
	  if(!relation.isInUse())
	  {
	    relationset.getJacobModel().removeElement(relation);
	    removedComplete=true;
	  }
	}

	/**
	 * @see Restores the relationship
	 */
	public void undo()
	{
	  if(removedComplete)
	    relationset.getJacobModel().addElement(relation);
	  relationset.addElement(relation);
	}
}

