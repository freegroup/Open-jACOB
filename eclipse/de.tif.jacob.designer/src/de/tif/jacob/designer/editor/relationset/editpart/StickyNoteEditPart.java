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
package de.tif.jacob.designer.editor.relationset.editpart;

import java.beans.PropertyChangeEvent;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import de.tif.jacob.designer.editor.relationset.directedit.LabelCellEditorLocator;
import de.tif.jacob.designer.editor.relationset.directedit.StickyNoteEditManager;
import de.tif.jacob.designer.editor.relationset.figures.StickyNoteFigure;
import de.tif.jacob.designer.editor.relationset.policy.StickyNodeEditPolicy;
import de.tif.jacob.designer.editor.relationset.policy.StickyNoteDirectEditPolicy;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationsetStickyNoteModel;

public class StickyNoteEditPart extends PropertyAwareEditPart
{
  private DirectEditManager manager;

  protected AccessibleEditPart createAccessible()
  {
    return new AccessibleGraphicalEditPart()
    {
      public void getValue(AccessibleControlEvent e)
      {
        System.out.println("AccessibleGraphicalEditPart.getValue");
        e.result = "juhu";//"getLogicLabel().getLabelContents();
      }

      public void getName(AccessibleEvent e)
      {
        System.out.println("AccessibleGraphicalEditPart.getName");
        e.result ="juhu";// LogicMessages.LogicPlugin_Tool_CreationTool_LogicLabel;
      }
    };
  }


  protected void createEditPolicies()
  {
    installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, null);
    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new StickyNoteDirectEditPolicy());
    installEditPolicy(EditPolicy.COMPONENT_ROLE, new StickyNodeEditPolicy());
  }

  protected IFigure createFigure()
  {
    return new StickyNoteFigure();
  }


	/**
	 * handles change in bounds, to be overridden by subclass
	 */
	protected void handleBoundsChange(PropertyChangeEvent evt)
	{
	  StickyNoteFigure tableFigure = (StickyNoteFigure) getFigure();
		Rectangle constraint = (Rectangle) evt.getNewValue();
		RelationsetEditPart parent = (RelationsetEditPart) getParent();
		parent.setLayoutConstraint(this, tableFigure, constraint);
	}

  private void performDirectEdit()
  {
    if (manager == null)
      manager = new StickyNoteEditManager(this, TextCellEditor.class, new LabelCellEditorLocator((StickyNoteFigure) getFigure()));
    manager.show();
  }

  public void performRequest(Request request)
  {
    if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
      performDirectEdit();
  }

  public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName() == ObjectModel.PROPERTY_LABEL_CHANGED)
      refreshVisuals();
    else
      super.propertyChange(evt);
  }

  protected void refreshVisuals()
  {
    StickyNoteFigure figure = (StickyNoteFigure) getFigure();
    
		Point location = figure.getLocation();
		RelationsetEditPart parent = (RelationsetEditPart) getParent();
		RelationsetStickyNoteModel note = (RelationsetStickyNoteModel)getModel();
		
    ((StickyNoteFigure) getFigure()).setText(note.getText());
    super.refreshVisuals();
  }

}
