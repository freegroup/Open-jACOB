package de.tif.jacob.rule.editor.rules.commands;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;

import de.tif.jacob.rule.editor.rules.model.AbstractSourceModel;
import de.tif.jacob.rule.editor.rules.model.AbstractTargetModel;
import de.tif.jacob.rule.editor.rules.model.AbstractTransitionModel;
import de.tif.jacob.rule.editor.rules.model.RuleModel;
import de.tif.jacob.rule.editor.rules.model.RulesetModel;

public class DeleteBusinessObjectCommand extends Command
{
  private final RuleModel model;
  private final RulesetModel parent;
  private List sourceConnections = Collections.EMPTY_LIST;
  private List targetConnections = Collections.EMPTY_LIST;
  private boolean wasRemoved=false;

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
  public DeleteBusinessObjectCommand(RulesetModel parent, RuleModel model)
  {
    setLabel("Business Object deletion");
    this.parent = parent;
    this.model  = model;
    if(model instanceof AbstractSourceModel)
    	this.sourceConnections = this.parent.getSourceTransitions((AbstractSourceModel)model);
    if(model instanceof AbstractTargetModel)
    	this.targetConnections = this.parent.getTargetTransitions((AbstractTargetModel)model);
  }

  /*
   * (non-Javadoc)model
   * 
   * @see org.eclipse.gef.commands.Command#canUndo()
   */
  public boolean canUndo()
  {
    return wasRemoved;
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
    wasRemoved = parent.removeElement(model);
    if (wasRemoved)
    {
      removeConnections(sourceConnections);
      removeConnections(targetConnections);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#undo()
   */
  public void undo()
  {
    // add the child and reconnect its connections
    if (parent.addElement(model))
    {
      addConnections(sourceConnections);
      addConnections(targetConnections);
    }
  }

  private void removeConnections(List connections)
  {
    for (Iterator iter = connections.iterator(); iter.hasNext();)
    {
      AbstractTransitionModel conn = (AbstractTransitionModel) iter.next();
      parent.removeElement(conn);
    }
  }

  private void addConnections(List connections)
  {
    for (Iterator iter = connections.iterator(); iter.hasNext();)
    {
      AbstractTransitionModel conn = (AbstractTransitionModel) iter.next();
      parent.addElement(conn);
    }
  }
}