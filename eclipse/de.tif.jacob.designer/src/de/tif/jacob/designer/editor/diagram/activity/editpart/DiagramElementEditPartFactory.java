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
package de.tif.jacob.designer.editor.diagram.activity.editpart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import de.tif.jacob.designer.model.diagram.activity.ActivityDiagramModel;
import de.tif.jacob.designer.model.diagram.activity.DiagramElementModel;
import de.tif.jacob.designer.model.diagram.activity.TransitionModel;

/**
 * Factory that maps model elements to edit parts.
 * 
 * @author Elias Volanakis
 */
public class DiagramElementEditPartFactory implements EditPartFactory
{
  public EditPart createEditPart(EditPart context, Object modelElement)
  {
    EditPart part = getPartForElement(modelElement);
    part.setModel(modelElement);
    return part;
  }

  /**
   * Maps an object to an EditPart.
   * 
   * @throws RuntimeException
   *           if no match was found (programming error)
   */
  private EditPart getPartForElement(Object modelElement)
  {
    if (modelElement instanceof ActivityDiagramModel)
      return new ActivityDiagramEditPart();
    
    if (modelElement instanceof DiagramElementModel)
      return new DiagramElementEditPart();
    
    if (modelElement instanceof TransitionModel)
      return new TransitionEditPart();

    throw new RuntimeException("Can't create part for model element: " + ((modelElement != null) ? modelElement.getClass().getName() : "null"));
  }
}