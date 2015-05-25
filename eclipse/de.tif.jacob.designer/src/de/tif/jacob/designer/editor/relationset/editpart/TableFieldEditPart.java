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
/*
 * Created on Jul 13, 2004
 */
package de.tif.jacob.designer.editor.relationset.editpart;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.ShowObjectModelHookAction;
import de.tif.jacob.designer.actions.ShowTableAliasEditorAction;
import de.tif.jacob.designer.actions.ShowTableFieldEditorAction;
import de.tif.jacob.designer.editor.jacobform.figures.InformBrowserFigure;
import de.tif.jacob.designer.editor.jacobform.figures.ObjectFigure;
import de.tif.jacob.designer.editor.relationset.figures.ColumnFigure;
import de.tif.jacob.designer.editor.relationset.figures.TableAliasFigure;
import de.tif.jacob.designer.editor.relationset.policy.TableAliasEditPolicy;
import de.tif.jacob.designer.editor.relationset.policy.TableContainerEditPolicy;
import de.tif.jacob.designer.editor.relationset.policy.TableLayoutEditPolicy;
import de.tif.jacob.designer.editor.relationset.policy.TableNodeEditPolicy;
import de.tif.jacob.designer.model.BrowserFieldModel;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.I18NResourceModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.TreeSelectionObjectModelProvider;
import de.tif.jacob.designer.model.RelationModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.UIDBInformBrowserModel;
import de.tif.jacob.util.StringUtil;

/**
 * Represents the editable/resizable table which can have columns added,
 * removed, renamed etc.
 * 
 * @author Phil Zoio
 */
public class TableFieldEditPart extends PropertyAwareEditPart implements TreeSelectionObjectModelProvider
{
	/**
	 * Returns the Table model object represented by this EditPart
	 */
	public FieldModel getFieldModel()
	{
		return (FieldModel) getModel();
	}
  
  /**
   * Liefert das Objekt Model zurück welches im ApplicationOutline selektiert werden
   * soll, wenn dieses Element selektiert wird (sync-Mode)
   */
  public ObjectModel getTreeObjectModel()
  {
    return (ObjectModel) getModel();
  }

  public void performRequest(Request req)
  {
		if(req.getType() == RequestConstants.REQ_OPEN)
		{
			final FieldModel model = getFieldModel();
			new ShowTableFieldEditorAction() {
				public FieldModel getFieldModel() {
					return model;
				}
			}.run(null);
		}
  }


	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent ev)
	{
    if (ev.getPropertyName()==ObjectModel.PROPERTY_NAME_CHANGED)
      refreshVisuals();
    else
      super.propertyChange(ev);
	}
	
	//******************* Editing related methods *********************/

	/**
	 * Creates edit policies and associates these with roles
	 */
	public void createEditPolicies()
	{
//		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new TableNodeEditPolicy());
//		installEditPolicy(EditPolicy.LAYOUT_ROLE, new TableLayoutEditPolicy());
//		installEditPolicy(EditPolicy.CONTAINER_ROLE, new TableContainerEditPolicy());
//		installEditPolicy(EditPolicy.COMPONENT_ROLE, new TableAliasEditPolicy());
	}


	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#toString()
	 */
	public String toString()
	{
		return getModel().toString();
	}

	/**
	 * Creates a figure which represents the table
	 */
	protected IFigure createFigure()
	{
//	  TableAliasModel alias = getTableAliasModel();
		return new ColumnFigure();
	}

	/**
	 * Reset the layout constraint, and revalidate the content pane
	 */
	protected void refreshVisuals()
	{
		ColumnFigure tableFigure = (ColumnFigure) getFigure();
		Point location = tableFigure.getLocation();
//		tableFigure.setImage(getFieldModel().getImage());
		tableFigure.setText(getFieldModel().getExtendedDescriptionLabel());
	}
}