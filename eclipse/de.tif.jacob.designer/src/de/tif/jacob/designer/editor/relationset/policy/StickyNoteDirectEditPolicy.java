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
package de.tif.jacob.designer.editor.relationset.policy;


import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.jface.viewers.CellEditor;
import de.tif.jacob.designer.editor.relationset.command.ChangeStickyNoteTextCommand;
import de.tif.jacob.designer.model.RelationsetStickyNoteModel;

/**
 * EditPolicy for the direct editing of table names
 * 
 * @author Phil Zoio
 */
public class StickyNoteDirectEditPolicy extends DirectEditPolicy
{
	private String oldValue;

	/**
	 * @see DirectEditPolicy#getDirectEditCommand(org.eclipse.gef.requests.DirectEditRequest)
	 */
	protected Command getDirectEditCommand(DirectEditRequest request)
	{
	  System.out.println("StickyNoteDirectEditPolicy.getDirectEditCommand");

	  String labelText = (String)request.getCellEditor().getValue();

	  System.out.println("NEW VALUE:"+labelText);
	  RelationsetStickyNoteModel note = (RelationsetStickyNoteModel) getHost().getModel();
	  ChangeStickyNoteTextCommand cmd = new ChangeStickyNoteTextCommand(note);
		cmd.setOldLabel(note.getText());
		CellEditor cellEditor = request.getCellEditor();
		cmd.setLabel((String) cellEditor.getValue());
		
		return cmd;
	}

	/**
	 * @see DirectEditPolicy#showCurrentEditValue(org.eclipse.gef.requests.DirectEditRequest)
	 */
	protected void showCurrentEditValue(DirectEditRequest request)
	{
	  System.out.println("StickyNoteDirectEditPolicy.showCurrentEditValue");
	  
		String value = (String) request.getCellEditor().getValue();
	}

	/**
	 * @param to
	 *            Saves the initial text value so that if the user's changes are not committed then 
	 */
	protected void storeOldEditValue(DirectEditRequest request)
	{
	  System.out.println("StickyNoteDirectEditPolicy.storeOldEditValue");
		
		CellEditor cellEditor = request.getCellEditor();
		oldValue = (String) cellEditor.getValue();
	}

	/**
	 * @param request
	 */
	protected void revertOldEditValue(DirectEditRequest request)
	{
	  System.out.println("StickyNoteDirectEditPolicy.revertOldEditValue");

	  CellEditor cellEditor = request.getCellEditor();
		cellEditor.setValue(oldValue);
	}
}