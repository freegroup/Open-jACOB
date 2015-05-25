package de.tif.jacob.rule.editor.rules.commands;

import org.eclipse.gef.commands.Command;

import de.tif.jacob.rule.editor.rules.model.AbstractTargetModel;

public abstract class AbstractConnectionCreateCommand extends Command
{
  AbstractTargetModel target;


  /**
   * Set the target endpoint for the connection.
   * 
   * @param target
   *          that target endpoint (a non-null Shape instance)
   * @throws IllegalArgumentException
   *           if target is null
   */
  public void setTarget(AbstractTargetModel target)
  {
    this.target = target;
  }

}
