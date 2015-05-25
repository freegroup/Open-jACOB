package de.tif.jacob.rule.editor.rules.commands;

import org.eclipse.gef.commands.Command;

import de.tif.jacob.rule.editor.rules.model.AnnotationModel;

/**
 * Command to change the name field
 * 
 * @author Phil Zoio
 */
public class ChangeStickyNoteTextCommand extends Command
{
	private AnnotationModel note;
	private String label;
	private String oldLabel;

	public ChangeStickyNoteTextCommand(AnnotationModel note)
	{
	  this.note = note;
	}
	
	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute()
	{
	  note.setText(label);
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
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo()
	{
		note.setText(oldLabel);
	}
}