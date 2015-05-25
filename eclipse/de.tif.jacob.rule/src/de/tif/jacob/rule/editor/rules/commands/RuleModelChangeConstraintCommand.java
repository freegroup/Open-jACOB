package de.tif.jacob.rule.editor.rules.commands;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import de.tif.jacob.rule.editor.rules.model.RuleModel;


public class RuleModelChangeConstraintCommand extends Command
{
	final private RuleModel element;
	final private Point oldConstraint;
	final private Point newConstraint;
	
	public RuleModelChangeConstraintCommand(RuleModel element, Point newConstraint)
	{
		this.newConstraint = newConstraint;
    this.oldConstraint = element.getPosition();
		this.element = element;		
	}
	
	public void execute()
	{
		element.setPosition(newConstraint);
	}
	
	public void redo()
	{
		element.setPosition(newConstraint);
	}
	
	public void undo()
	{
		element.setPosition(oldConstraint);
	}
}
