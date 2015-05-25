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
import java.util.List;
import org.eclipse.gef.commands.Command;
import de.tif.jacob.designer.model.diagram.activity.ActivityDiagramModel;
import de.tif.jacob.designer.model.diagram.activity.DiagramElementModel;
import de.tif.jacob.designer.model.diagram.activity.TransitionModel;

/**
 * A command to remove a shape from its parent. The command can be undone or
 * redone.
 * 
 * @author Elias Volanakis
 */
public class DeleteDiagramElementModelCommand extends Command
{
  private final DiagramElementModel child;
  private final ActivityDiagramModel parent;
  private List sourceConnections;
  private List targetConnections;
  private boolean wasRemoved;

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
  public DeleteDiagramElementModelCommand(ActivityDiagramModel parent, DiagramElementModel child)
  {
    if (parent == null || child == null)
    {
      throw new IllegalArgumentException();
    }
    setLabel("shape deletion");
    this.parent = parent;
    this.child = child;
  }

  /*
   * (non-Javadoc)
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
    // store a copy of incoming & outgoing connections before proceeding
    sourceConnections = child.getSourceConnections();
    targetConnections = child.getTargetConnections();
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
    wasRemoved = parent.removeElement(child);
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
    if (parent.addElement(child))
    {
      addConnections(sourceConnections);
      addConnections(targetConnections);
    }
  }

  /**
   * Disconnects a List of Connections from their endpoints.
   * 
   * @param connections
   *          a non-null List of connections
   */
  private void removeConnections(List connections)
  {
    for (Iterator iter = connections.iterator(); iter.hasNext();)
    {
      TransitionModel conn = (TransitionModel) iter.next();
      parent.removeElement(conn);
    }
  }
  
  private void addConnections(List connections)
  {
    for (Iterator iter = connections.iterator(); iter.hasNext();)
    {
      TransitionModel conn = (TransitionModel) iter.next();
      parent.addElement(conn);
      conn.connect(conn.getSource(), conn.getTarget());
    }
  }

}
