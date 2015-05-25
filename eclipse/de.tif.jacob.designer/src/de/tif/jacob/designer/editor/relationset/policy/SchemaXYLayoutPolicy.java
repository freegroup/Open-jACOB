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
package de.tif.jacob.designer.editor.relationset.policy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import de.tif.jacob.designer.editor.relationset.command.AddStickyNoteCommand;
import de.tif.jacob.designer.editor.relationset.command.MoveStickyNoteCommand;
import de.tif.jacob.designer.editor.relationset.command.MoveTableAliasCommand;
import de.tif.jacob.designer.editor.relationset.editpart.RelationsetEditPart;
import de.tif.jacob.designer.editor.relationset.editpart.StickyNoteEditPart;
import de.tif.jacob.designer.editor.relationset.editpart.TableAliasEditPart;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.RelationsetStickyNoteModel;
import de.tif.jacob.designer.model.TableAliasModel;

/**
 * Handles manual layout editing for schema diagram. Only available for
 * XYLayoutManagers, not for automatic layout
 * 
 * @author Phil Zoio
 */
public class SchemaXYLayoutPolicy extends XYLayoutEditPolicy
{

	protected Command createAddCommand(EditPart child, Object constraint)
	{
		return null;
	}

	/**
	 * Creates command to move table. Does not allow table to be resized
	 */
	protected Command createChangeConstraintCommand(EditPart child, Object constraint)
	{
		if ((child instanceof TableAliasEditPart))
		{
			if (!(constraint instanceof Rectangle))
				return null;
	
			TableAliasEditPart aliasPart = (TableAliasEditPart) child;
			TableAliasModel    alias     = aliasPart.getTableAliasModel();
			Rectangle newBounds = (Rectangle) constraint;
	
			RelationsetEditPart schemaPart = (RelationsetEditPart) aliasPart.getParent();
			RelationsetModel relationSet = schemaPart.getRelationsetModel();
			
			return new MoveTableAliasCommand( relationSet,alias, newBounds.getCopy());
		}
		
		if ((child instanceof StickyNoteEditPart))
		{
			if (!(constraint instanceof Rectangle))
				return null;
	
			StickyNoteEditPart         notePart = (StickyNoteEditPart) child;
			RelationsetStickyNoteModel note     = (RelationsetStickyNoteModel)notePart.getModel();
			Rectangle newBounds = (Rectangle) constraint;
	
			return new MoveStickyNoteCommand( note, newBounds.getCopy());
		}
		return null;
	}


	/**
	 * Returns the current bounds as the constraint if none can be found in the
	 * figures Constraint object
	 */
	public Rectangle getCurrentConstraintFor(GraphicalEditPart child)
	{
		return child.getFigure().getBounds();
		/*
		IFigure fig = child.getFigure();
		Rectangle rectangle = (Rectangle) fig.getParent().getLayoutManager().getConstraint(fig);
		if (rectangle == null)
		{
			rectangle = fig.getBounds();
		}
		return rectangle;
		*/
	}

	protected Command getCreateCommand(CreateRequest request)
	{
		Object newObject = request.getNewObject();
		
		if (newObject instanceof RelationsetStickyNoteModel)
		{
			Point location = request.getLocation();
			Dimension dim  = request.getSize();
			if(dim==null)
			  dim = new Dimension(-1,-1);
			
			EditPart host = getTargetEditPart(request);
			RelationsetEditPart schemaPart = (RelationsetEditPart)getHost();
			RelationsetModel relationSet = schemaPart.getRelationsetModel();
			RelationsetStickyNoteModel  note       = (RelationsetStickyNoteModel) newObject;
			return  new AddStickyNoteCommand(relationSet, note,new Rectangle( location,  dim));
		}

		return null;
	}

	protected Command getDeleteDependantCommand(Request request)
	{
		return null;
	}

}