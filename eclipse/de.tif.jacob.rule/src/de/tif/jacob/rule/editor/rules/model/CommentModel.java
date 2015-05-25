package de.tif.jacob.rule.editor.rules.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class CommentModel extends ObjectModel
{
	Point position = new Point();
	DecisionModel decision;
	public CommentModel(DecisionModel parent) 
	{
		super();
		this.decision=parent;
		position = parent.getPosition();
		position.translate(0,-10);
	}
	
	public Point getPosition() 
	{
		return position;
	}

	public DecisionModel getDecisionModel() 
  {
		return decision;
	}
}
