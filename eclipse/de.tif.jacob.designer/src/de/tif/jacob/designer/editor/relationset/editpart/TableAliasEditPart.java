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

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;

import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.ShowObjectModelHookAction;
import de.tif.jacob.designer.actions.ShowTableAliasEditorAction;
import de.tif.jacob.designer.editor.diagram.activity.editpart.anchor.TopBottomAnchor;
import de.tif.jacob.designer.editor.jacobform.figures.InformBrowserFigure;
import de.tif.jacob.designer.editor.jacobform.figures.ObjectFigure;
import de.tif.jacob.designer.editor.relationset.anchor.LeftRightAnchor;
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
import de.tif.jacob.designer.model.diagram.activity.BranchMergeModel;
import de.tif.jacob.designer.model.diagram.activity.EndModel;
import de.tif.jacob.designer.model.diagram.activity.StartModel;
import de.tif.jacob.util.StringUtil;

/**
 * Represents the editable/resizable table which can have columns added,
 * removed, renamed etc.
 * 
 * @author Phil Zoio
 */
public class TableAliasEditPart extends PropertyAwareEditPart implements NodeEditPart, TreeSelectionObjectModelProvider
{
	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate()
	{
		super.activate();
		getTableAliasModel().getTableModel().addPropertyChangeListener(this);
	}

	/**
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate()
	{
		super.deactivate();
		getTableAliasModel().getTableModel().removePropertyChangeListener(this);
	}

	/**
	 * Returns the Table model object represented by this EditPart
	 */
	public TableAliasModel getTableAliasModel()
	{
		return (TableAliasModel) getModel();
	}
  
  public ObjectModel getTreeObjectModel()
  {
    return (ObjectModel) getModel();
  }

  public void expand(boolean flag)
  {
    TableAliasFigure fig = (TableAliasFigure)getFigure();
    fig.expand(flag);
  }

  public void performRequest(Request req)
  {
		if(req.getType() == RequestConstants.REQ_OPEN)
		{
		  if(JacobDesigner.getPlugin().getPreferenceStore().getString(JacobDesigner.DOUBLECLICK_DB).equals("dbDefinition"))
		  {
		    // reuse the object selection contribution to use a common method to oopen
		    // the table editor.
		    //
		    new ShowTableAliasEditorAction()
		    {
		      public TableAliasModel getTableAliasModel() {return TableAliasEditPart.this.getTableAliasModel(); }
		    }.run(null);		    
		  }
		  else
		  {
		    new ShowObjectModelHookAction()
        {
          public ObjectModel getObjectModel(){return getTableAliasModel(); }
        }.run(null);
		  }
		}
  }


  /**
	 * @return the children Model objects as a new ArrayList
	 */
	protected List getModelChildren()
	{
		return getTableAliasModel().getFieldModels();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
	 */
	protected List getModelSourceConnections()
	{
	  RelationsetModel relationset=(RelationsetModel)(getParent().getModel());
	  List result = new ArrayList();

	  Iterator iter = getTableAliasModel().getForeignKeyRelationships().iterator();
	  while (iter.hasNext())
    {
      RelationModel relation = (RelationModel) iter.next();
      if(relationset.contains(relation))
        result.add(relation);
    }

	  return result;
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
	 */
	protected List getModelTargetConnections()
	{
	  RelationsetModel relationset=(RelationsetModel)(getParent().getModel());
	  List result = new ArrayList();

	  Iterator iter = getTableAliasModel().getPrimaryKeyRelationships().iterator();
	  while (iter.hasNext())
    {
      RelationModel relation = (RelationModel) iter.next();
      if(relationset.contains(relation))
        result.add(relation);
    }
	  
	  return result;
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent ev)
	{
    if (ev.getPropertyName()==ObjectModel.PROPERTY_DESCRIPTION_CHANGED)
      refreshVisuals();
    if (ev.getPropertyName()==ObjectModel.PROPERTY_NAME_CHANGED)
      refreshVisuals();
    else if (ev.getPropertyName()==ObjectModel.PROPERTY_CONDITION_CHANGED)
      refreshVisuals();
    else if (ev.getPropertyName()==ObjectModel.PROPERTY_BROWSER_CREATED)
      refreshVisuals();
    else if (ev.getPropertyName()==ObjectModel.PROPERTY_BROWSER_DELETED)
      refreshVisuals();
    else if (ev.getPropertyName()==ObjectModel.PROPERTY_FIELD_ADDED)
      refreshChildren();
    else if (ev.getPropertyName()==ObjectModel.PROPERTY_FIELD_DELETED)
      refreshChildren();
    else if (ev.getPropertyName()==ObjectModel.PROPERTY_TABLE_CHANGED)
      refreshChildren();
    else
      super.propertyChange(ev);
	}
	
	//******************* Editing related methods *********************/

	/**
	 * Creates edit policies and associates these with roles
	 */
	public void createEditPolicies()
	{
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new TableNodeEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new TableLayoutEditPolicy());
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new TableContainerEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new TableAliasEditPolicy());
	}


	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#toString()
	 */
	public String toString()
	{
		return getModel().toString();
	}


	/**
	 * handles change in bounds, to be overridden by subclass
	 */
	protected void handleBoundsChange(PropertyChangeEvent evt)
	{
		TableAliasFigure tableFigure = (TableAliasFigure) getFigure();
		RelationsetModel relationset = (RelationsetModel) evt.getNewValue();
		RelationsetEditPart parent = (RelationsetEditPart) getParent();
		RelationsetModel thisRelationset = parent.getRelationsetModel();
		if(thisRelationset==relationset)
		  parent.setLayoutConstraint(this, tableFigure, thisRelationset.getTableAliasBounds(getTableAliasModel()) );

		expand(relationset.getTableAliasExpanded(getTableAliasModel()));
	}

	//******************* Layout related methods *********************/

	/**
	 * Creates a figure which represents the table
	 */
	protected IFigure createFigure()
	{
	  TableAliasModel alias = getTableAliasModel();
		RelationsetModel relationset = (RelationsetModel) getParent().getModel();
	  
	  TableAliasFigure figure= new TableAliasFigure(alias.getName(), alias.getTableModel().getName(), alias.getDescription(), alias.getImage());
		figure.expand(relationset.getTableAliasExpanded(alias));
	  figure.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent event) {
				RelationsetEditPart relationsetEditPart = (RelationsetEditPart)getParent();
				TableAliasModel alias = getTableAliasModel();
				boolean flag = relationsetEditPart.getRelationsetModel().getTableAliasExpanded(alias);
				relationsetEditPart.getRelationsetModel().setTableAliasExpanded(alias,!flag);
			}
		
		});
	  
	  return figure;
	}

	/**
	 * Reset the layout constraint, and revalidate the content pane
	 */
	protected void refreshVisuals()
	{
		TableAliasFigure tableFigure = (TableAliasFigure) getFigure();
//		Point location = tableFigure.getLocation();
		tableFigure.setImage(getTableAliasModel().getImage());
		tableFigure.setText(getTableAliasModel().getName(), getTableAliasModel().getDescription());
	}

	/**
	 * @return the Content pane for adding or removing child figures
	 */
	public IFigure getContentPane()
	{
		TableAliasFigure figure = (TableAliasFigure) getFigure();
		return figure.getColumnsFigure();
	}
	
  
	/**
	 * @see NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection)
	{
		return new LeftRightAnchor(getFigure());
	}

	/**
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request)
	{
		return new LeftRightAnchor(getFigure());
	}

	/**
	 * @see NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection)
	{
		RelationModel model = (RelationModel)connection.getModel();
		FieldModel field =(FieldModel) model.getToKey().getFieldModels().get(0);
		
		return new LeftRightAnchor(getFigure());
	}

	/**
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
	 */

	public ConnectionAnchor getTargetConnectionAnchor(Request request)
	{
		return new LeftRightAnchor(getFigure());
	}
	
}