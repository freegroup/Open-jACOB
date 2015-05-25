package de.tif.jacob.rule.editor.rules.editpart;

import org.eclipse.draw2d.IFigure;

import de.tif.jacob.rule.editor.rules.figures.BooleanDecisionFigure;


public class BooleanDecisionModelEditPart extends DecisionModelEditPart
{
  public BooleanDecisionModelEditPart()
  {
  }
  
  /**
   * Return a IFigure depending on the instance of the current model element.
   * This allows this EditPart to be used for both sublasses of Shape.
   */
  public IFigure createFigureForModel()
  {
      return new BooleanDecisionFigure();
  }
}