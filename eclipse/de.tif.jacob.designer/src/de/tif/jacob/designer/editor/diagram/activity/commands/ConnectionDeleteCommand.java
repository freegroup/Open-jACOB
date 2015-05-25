/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/
package de.tif.jacob.designer.editor.diagram.activity.commands;

import org.eclipse.gef.commands.Command;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.diagram.activity.ActivityDiagramModel;
import de.tif.jacob.designer.model.diagram.activity.TransitionModel;

/**
 * A command to disconnect (remove) a connection from its endpoints. The command
 * can be undone or redone.
 * 
 * @author Elias Volanakis
 */
public class ConnectionDeleteCommand extends Command
{
  private final TransitionModel      connection;
//  private final JacobModel           jacobModel;
  private final ActivityDiagramModel diagram;
  
  /**
   * Create a command that will disconnect a connection from its endpoints.
   * 
   * @param conn
   *          the connection instance to disconnect (non-null)
   * @throws IllegalArgumentException
   *           if conn is null
   */
  public ConnectionDeleteCommand(JacobModel jacobModel, ActivityDiagramModel diagram, TransitionModel conn)
  {
    if (conn == null)
    {
      throw new IllegalArgumentException();
    }
    setLabel("connection deletion");
    this.connection = conn;
//    this.jacobModel = jacobModel;
    this.diagram    = diagram;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#execute()
   */
  public void execute()
  {
    diagram.removeElement(connection);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#undo()
   */
  public void undo()
  {
    diagram.addElement(connection);
    connection.connect(connection.getSource(), connection.getTarget());
  }
}
