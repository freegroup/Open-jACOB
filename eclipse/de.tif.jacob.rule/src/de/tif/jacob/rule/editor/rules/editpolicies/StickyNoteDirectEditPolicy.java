/*
 * Created on Oct 20, 2004
 *
 */
package de.tif.jacob.rule.editor.rules.editpolicies;


import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.jface.viewers.CellEditor;

import de.tif.jacob.rule.editor.rules.commands.ChangeStickyNoteTextCommand;
import de.tif.jacob.rule.editor.rules.model.AnnotationModel;

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
    AnnotationModel note = (AnnotationModel) getHost().getModel();
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