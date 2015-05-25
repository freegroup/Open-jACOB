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

import java.util.List;
import org.eclipse.gef.commands.Command;
import de.tif.jacob.designer.model.RelationModel;
import de.tif.jacob.designer.model.TableAliasModel;

/**
 * Command to change the foreign key we are connecting to a particular primary
 * key
 * 
 * @author Phil Zoio
 */
 class ReconnectForeignKeyCommand extends Command
{

	/** source Table * */
	protected TableAliasModel sourceForeignKey;
	/** target Table * */
	protected TableAliasModel targetPrimaryKey;
	/** Relationship between source and target * */
	protected RelationModel relationship;
	/** previous source prior to command execution * */
	protected TableAliasModel oldSourceForeignKey;

	/**
	 * Makes sure that primary key doesn't reconnect to itself or try to create
	 * a relationship which already exists
	 */
	public boolean canExecute()
	{
		boolean returnVal = true;

		TableAliasModel primaryKeyTable = relationship.getFromTableAlias();

		//cannot connect to itself
		if (primaryKeyTable.equals(sourceForeignKey))
		{
			returnVal = false;
		}
		else
		{

			List relationships = sourceForeignKey.getForeignKeyRelationships();
			for (int i = 0; i < relationships.size(); i++)
			{

				RelationModel relation = ((RelationModel) (relationships.get(i)));
				if (relationship.getFromTableAlias().equals(targetPrimaryKey)
						&& relationship.getToTableAlias().equals(sourceForeignKey))
				{
					returnVal = false;
					break;
				}
			}
		}

		return returnVal;

	}

	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute()
	{
	  System.out.println("ReconnectForeignKeyCommand.execute()");
	  /*
		if (sourceForeignKey != null)
		{
			oldSourceForeignKey.removeForeignKeyRelationship(relationship);
			relationship.setForeignKeyTable(sourceForeignKey);
			sourceForeignKey.addForeignKeyRelationship(relationship);
		}
		*/
	}

	/**
	 * @return Returns the sourceForeignKey.
	 */
	public TableAliasModel getSourceForeignKey()
	{
		return sourceForeignKey;
	}

	/**
	 * @param sourceForeignKey
	 *            The sourceForeignKey to set.
	 */
	public void setSourceForeignKey(TableAliasModel sourceForeignKey)
	{
		this.sourceForeignKey = sourceForeignKey;
	}

	/**
	 * @return Returns the targetPrimaryKey.
	 */
	public TableAliasModel getTargetPrimaryKey()
	{
		return targetPrimaryKey;
	}

	/**
	 * @param targetPrimaryKey
	 *            The targetPrimaryKey to set.
	 */
	public void setTargetPrimaryKey(TableAliasModel targetPrimaryKey)
	{
		this.targetPrimaryKey = targetPrimaryKey;
	}

	/**
	 * @return Returns the relationship.
	 */
	public RelationModel getRelationship()
	{
		return relationship;
	}

	/**
	 * Sets the Relationship associated with this
	 * 
	 * @param relationship
	 *            the Relationship
	 */
	public void setRelationship(RelationModel relationship)
	{
		this.relationship = relationship;
		targetPrimaryKey = relationship.getFromTableAlias();
		oldSourceForeignKey = relationship.getToTableAlias();
	}

	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo()
	{
//		sourceForeignKey.removeForeignKeyRelationship(relationship);
//		relationship.setForeignKeyTable(oldSourceForeignKey);
//		oldSourceForeignKey.addForeignKeyRelationship(relationship);
	}
}