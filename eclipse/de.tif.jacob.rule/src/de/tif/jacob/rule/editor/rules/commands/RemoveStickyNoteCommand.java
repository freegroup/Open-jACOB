/*
 * Created on Jul 17, 2004
 */
package de.tif.jacob.rule.editor.rules.commands;

import org.eclipse.gef.commands.Command;

import de.tif.jacob.rule.editor.rules.model.AnnotationModel;
import de.tif.jacob.rule.editor.rules.model.RulesetModel;

/**
 * Command to delete tables from the schema
 * 
 * @author Phil Zoio
 */
public class RemoveStickyNoteCommand extends Command
{
	final private AnnotationModel note;
	final private RulesetModel    ruleset;
	

	public RemoveStickyNoteCommand(RulesetModel ruleset,AnnotationModel note)
	{
	  this.note    = note;
	  this.ruleset = ruleset;
	}
	
	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute()
	{
    ruleset.removeElement(note);
	}


	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo()
	{
    ruleset.addElement(note);
	}
}

