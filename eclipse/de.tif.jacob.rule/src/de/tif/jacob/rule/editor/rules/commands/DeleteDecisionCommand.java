package de.tif.jacob.rule.editor.rules.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;

import de.tif.jacob.rule.editor.rules.model.ConditionalExitModel;
import de.tif.jacob.rule.editor.rules.model.DecisionModel;
import de.tif.jacob.rule.editor.rules.model.RulesetModel;
import de.tif.jacob.rule.editor.rules.model.TransitionModel;

public class DeleteDecisionCommand extends Command
{
  private final DecisionModel model;
  private final RulesetModel parent;
  private final List                    inputTransition;
  private final List                    outputTransition;
  
  /**
   * Create a command that will remove the shape from its parent.
   * 
   * @param parent
   *          the ShapesDiagram containing the child
   * @param child
   *          the Shape to remove
   * @throws IllegalArgumentException
   *           if any parameter is null
   */
  public DeleteDecisionCommand(RulesetModel parent, DecisionModel model)
  {
    setLabel("Decision deletion");
    this.parent = parent;
    this.model  = model;

    this.inputTransition   = parent.getTargetTransitions(model);
    this.outputTransition = new ArrayList();
    Iterator iter = model.getConditionalExitModels().iterator();
    while(iter.hasNext())
    {
      ConditionalExitModel exit = (ConditionalExitModel)iter.next();
      if(exit.getTransistionModel()!=null)
        this.outputTransition.add(exit.getTransistionModel());
    }
  }

  /*
   * (non-Javadoc)model
   * 
   * @see org.eclipse.gef.commands.Command#canUndo()
   */
  public boolean canUndo()
  {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#execute()
   */
  public void execute()
  {
    redo();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#redo()
   */
  public void redo()
  {
    // remove the child and disconnect its connections
    parent.removeElement(model);
    for (Iterator iter = outputTransition.iterator(); iter.hasNext();)
    {
      TransitionModel conn = (TransitionModel) iter.next();
      parent.removeElement(conn);
    }
    for (Iterator iter = inputTransition.iterator(); iter.hasNext();)
    {
      TransitionModel conn = (TransitionModel) iter.next();
      parent.removeElement(conn);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#undo()
   */
  public void undo()
  {
    parent.addElement(model);
    for (Iterator iter = outputTransition.iterator(); iter.hasNext();)
    {
      TransitionModel conn = (TransitionModel) iter.next();
      parent.addElement(conn);
    }
    for (Iterator iter = inputTransition.iterator(); iter.hasNext();)
    {
      TransitionModel conn = (TransitionModel) iter.next();
      parent.addElement(conn);
    }
  }
}