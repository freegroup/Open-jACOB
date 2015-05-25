package de.tif.jacob.rule.editor.rules.commands;

import org.eclipse.gef.commands.Command;

import de.tif.jacob.rule.editor.rules.model.AbstractTransitionModel;

public class ConnectionDeleteCommand extends Command
{
  private final AbstractTransitionModel      connection;
  
  /**
   * Create a command that will disconnect a connection from its endpoints.
   * 
   * @param conn
   *          the connection instance to disconnect (non-null)
   * @throws IllegalArgumentException
   *           if conn is null
   */
  public ConnectionDeleteCommand(AbstractTransitionModel conn)
  {
    setLabel("connection deletion");
    this.connection = conn;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#execute()
   */
  public void execute()
  {
    connection.getRulesetModel().removeElement(connection);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#undo()
   */
  public void undo()
  {
    connection.getRulesetModel().addElement(connection);
  }
}
