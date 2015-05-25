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

import java.util.Iterator;
import org.eclipse.gef.commands.Command;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.diagram.activity.DiagramElementModel;
import de.tif.jacob.designer.model.diagram.activity.TransitionModel;

/**
 * A command to create a connection between two shapes. The command can be
 * undone or redone.
 * <p>
 * This command is designed to be used together with a GraphicalNodeEditPolicy.
 * To use this command properly, following steps are necessary:
 * </p>
 * <ol>
 * <li>Create a subclass of GraphicalNodeEditPolicy.</li>
 * <li>Override the <tt>getConnectionCreateCommand(...)</tt> method, to
 * create a new instance of this class and put it into the
 * CreateConnectionRequest.</li>
 * <li>Override the <tt>getConnectionCompleteCommand(...)</tt> method, to
 * obtain the Command from the ConnectionRequest, call setTarget(...) to set the
 * target endpoint of the connection and return this command instance.</li>
 * </ol>
 * 
 * @see de.tif.jacob.designer.editor.diagram.activity.editpart.DiagramElementEditPart#createEditPolicies()
 *      for an example of the above procedure.
 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy
 * @author Elias Volanakis
 */
public class ConnectionCreateCommand extends Command
{
  private TransitionModel connection;
  private final DiagramElementModel source;
  private DiagramElementModel target;
//  private JacobModel jacobModel;
  
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
  public ConnectionCreateCommand(JacobModel jacobModel, DiagramElementModel source)
  {
    if (source == null)
    {
      throw new IllegalArgumentException();
    }
    setLabel("connection creation");
    this.source     = source;
//    this.jacobModel = jacobModel;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#canExecute()
   */
  public boolean canExecute()
  {
    // disallow source -> source connections
    if (source.equals(target))
    {
      return false;
    }
    // return false, if the source -> target connection exists already
    for (Iterator iter = source.getSourceConnections().iterator(); iter.hasNext();)
    {
      TransitionModel conn = (TransitionModel) iter.next();
      if (conn.getTarget().equals(target))
      {
        return false;
      }
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#execute()
   */
  public void execute()
  {
    // create a new connection between source and target
    connection = new TransitionModel(source.getJacobModel(), source, target);
    source.getActivityDiagramModel().addElement(connection);
    connection.connect(source,target); // hack
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#redo()
   */
  public void redo()
  {
    connection.connect(source,target);
    source.getActivityDiagramModel().addElement(connection);
  }

  /**
   * Set the target endpoint for the connection.
   * 
   * @param target
   *          that target endpoint (a non-null Shape instance)
   * @throws IllegalArgumentException
   *           if target is null
   */
  public void setTarget(DiagramElementModel target)
  {
    if (target == null)
    {
      throw new IllegalArgumentException();
    }
    this.target = target;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#undo()
   */
  public void undo()
  {
    source.getActivityDiagramModel().removeElement(connection);
  }
}
