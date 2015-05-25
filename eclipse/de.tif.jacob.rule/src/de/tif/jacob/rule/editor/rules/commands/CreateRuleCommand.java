package de.tif.jacob.rule.editor.rules.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import de.tif.jacob.rule.editor.rules.model.RuleModel;
import de.tif.jacob.rule.editor.rules.model.RulesetModel;

public class CreateRuleCommand extends Command
{
  private final RuleModel model;
  private final RulesetModel parent;
  private boolean shapeAdded;

  /**
   * Create a command that will add a new Shape to a ShapesDiagram.
   * 
   * @param parent
   *          the ShapesDiagram that will hold the new element
   * @param req
   *          a request to create a new Shape
   * @throws IllegalArgumentException
   *           if any parameter is null, or the request does not provide a new
   *           Shape instance
   */
  public CreateRuleCommand(RulesetModel parent, CreateRequest req)
  {
    this.parent = parent;
    this.model  = (RuleModel)req.getNewObject();
    this.model.setPosition(req.getLocation());
    this.model.setRulesetModel(parent);
    
    setLabel("Business Object inserted");
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#canUndo()
   */
  public boolean canUndo()
  {
    return shapeAdded;
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
    shapeAdded = parent.addElement(model);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#undo()
   */
  public void undo()
  {
    parent.removeElement(model);
  }
}
