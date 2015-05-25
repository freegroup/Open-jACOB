/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package de.tif.jacob.rule.editor.rules.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;

import de.tif.jacob.rule.editor.rules.directedit.LabelCellEditorLocator;
import de.tif.jacob.rule.editor.rules.directedit.StickyNoteEditManager;
import de.tif.jacob.rule.editor.rules.editpolicies.StickyNoteDirectEditPolicy;
import de.tif.jacob.rule.editor.rules.figures.StickyNoteFigure;
import de.tif.jacob.rule.editor.rules.model.AnnotationModel;
import de.tif.jacob.rule.editor.rules.model.ObjectModel;

public class AnnotationModelEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener
{
  private DirectEditManager manager;

  /**
   * Upon activation, attach to the model element as a property change listener.
   */
  public void activate()
  {
    if (!isActive())
    {
      super.activate();
      ((AnnotationModel) getModel()).addPropertyChangeListener(this);
    }
  }

  public void deactivate()
  {
    if (isActive())
    {
      super.deactivate();
      ((AnnotationModel) getModel()).removePropertyChangeListener(this);
    }
  }

  protected AccessibleEditPart createAccessible()
  {
    return new AccessibleGraphicalEditPart()
    {
      public void getValue(AccessibleControlEvent e)
      {
        e.result = "juhu";//"getLogicLabel().getLabelContents();
      }

      public void getName(AccessibleEvent e)
      {
        e.result ="juhu";// LogicMessages.LogicPlugin_Tool_CreationTool_LogicLabel;
      }
    };
  }


  protected void createEditPolicies()
  {
    installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, null);
    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new StickyNoteDirectEditPolicy());
//    installEditPolicy(EditPolicy.COMPONENT_ROLE, new StickyNodeEditPolicy());
  }

  protected IFigure createFigure()
  {
    return new StickyNoteFigure();
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
    System.out.println("propertyChange:"+evt);
    if (evt.getPropertyName() == ObjectModel.PROPERTY_MODEL_CHANGED)
      refreshVisuals();
    if (evt.getPropertyName() == ObjectModel.PROPERTY_POSITION_CHANGED)
      refreshVisuals();
  }

  protected void refreshVisuals()
  {
    StickyNoteFigure figure = (StickyNoteFigure) getFigure();
		AnnotationModel note = (AnnotationModel)getModel();
		
    System.out.println("setText:"+note.getText());
    figure.setText(note.getText());
    figure.setBounds(note.getBounds());
    super.refreshVisuals();
  }

}