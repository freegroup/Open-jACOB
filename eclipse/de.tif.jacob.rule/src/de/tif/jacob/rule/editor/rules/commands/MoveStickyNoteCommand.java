/*
 * Created on Jul 20, 2004
 */
package de.tif.jacob.rule.editor.rules.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import de.tif.jacob.rule.editor.rules.model.AnnotationModel;

/**
 * Command to move the bounds of an existing table. Only used with
 * XYLayoutEditPolicy (manual layout)
 * 
 * @author Phil Zoio
 */
public class MoveStickyNoteCommand extends Command
{
	final private AnnotationModel  note;
	final private Rectangle        oldBounds;
	final private Rectangle        newBounds;

	public MoveStickyNoteCommand(AnnotationModel note, Rectangle newBounds)
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