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
package de.tif.jacob.designer.editor.diagram.activity.editpolicies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import de.tif.jacob.designer.editor.diagram.activity.commands.DeleteDiagramElementModelCommand;
import de.tif.jacob.designer.model.diagram.activity.ActivityDiagramModel;
import de.tif.jacob.designer.model.diagram.activity.DiagramElementModel;

/**
 * This edit policy enables the removal of a Shapes instance from its container.
 * 
 * @see BusinessObjectModelEditPart#createEditPolicies()
 * @see ShapeTreeEditPart#createEditPolicies()
 * @author Elias Volanakis
 */
public class ShapeComponentEditPolicy extends ComponentEditPolicy
{

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editpolicies.ComponentEditPolicy#createDeleteCommand(org.eclipse.gef.requests.GroupRequest)
   */
  protected Command createDeleteCommand(GroupRequest deleteRequest)
  {
    Object parent = getHost().getParent().getModel();
    Object child = getHost().getModel();
    if (parent instanceof ActivityDiagramModel && child instanceof DiagramElementModel)
    {
      return new DeleteDiagramElementModelCommand((ActivityDiagramModel) parent, (DiagramElementModel) child);
    }
    return super.createDeleteCommand(deleteRequest);
  }
}
