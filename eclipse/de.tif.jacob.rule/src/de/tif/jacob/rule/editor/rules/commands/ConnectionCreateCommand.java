package de.tif.jacob.rule.editor.rules.commands;

import de.tif.jacob.rule.editor.rules.model.AbstractSourceModel;
import de.tif.jacob.rule.editor.rules.model.RulesetModel;
import de.tif.jacob.rule.editor.rules.model.TransitionModel;

public class ConnectionCreateCommand extends AbstractConnectionCreateCommand
{
  RulesetModel        ruleset;
  AbstractSourceModel start;
  TransitionModel     connection;
  
  /**
   * Instantiate a command that can create a connection between two shapes.
   * 
   * @param source
   *          the source endpoint (a non-null Shape instance)
   * @param lineStyle
   *          the desired line style. See Connection#setLineStyle(int) for
   *          details
   * @throws IllegalArgumentException
   *           if source is null
   * @see TransitionModel#setLineStyle(int)
   */
  public ConnectionCreateCommand(RulesetModel ruleset, AbstractSourceModel start)
  {
    this.start   = start;
    this.ruleset = ruleset;
    
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#canExecute()
   */
  public boolean canExecute()
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
    connection = new TransitionModel(ruleset, start, target);
    ruleset.addElement(connection);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#redo()
   */
  public void redo()
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#undo()
   */
  public void undo()
  {
  }
}
