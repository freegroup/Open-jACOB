/*
 * Created on Jul 15, 2004
 */
package de.tif.jacob.rule.editor.rules.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import de.tif.jacob.rule.editor.rules.model.AnnotationModel;
import de.tif.jacob.rule.editor.rules.model.RulesetModel;

/**
 * Command to create a new table table
 * 
 * @author Phil Zoio
 */
public class AddStickyNoteCommand extends Command
{
	final private RulesetModel     ruleset;
	final private AnnotationModel  annotation;
	
  public AddStickyNoteCommand(RulesetModel parent, CreateRequest req)
  {
    this.ruleset    = parent;
    this.annotation = (AnnotationModel)req.getNewObject();
    if(req.getSize()==null)
      this.annotation.setBounds(new Rectangle(req.getLocation(), new Dimension(100,100)));
    else
      this.annotation.setBounds(new Rectangle(req.getLocation(), req.getSize()));
    this.annotation.setRulesetModel(parent);
    this.annotation.setRulesetModel(ruleset);
    
    setLabel("Annotation inserted");
  }

	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute()
	{
		ruleset.addElement(annotation);
	}

 
	public void undo()
	{
		ruleset.removeElement(annotation);
	}
}