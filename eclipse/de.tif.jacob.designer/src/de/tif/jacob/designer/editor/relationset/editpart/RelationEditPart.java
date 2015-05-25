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
import java.beans.PropertyChangeListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import de.tif.jacob.designer.editor.relationset.figures.RelationFigure;
import de.tif.jacob.designer.editor.relationset.policy.RelationEditPolicy;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.KeyModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationModel;
import de.tif.jacob.designer.model.TreeSelectionObjectModelProvider;

/**
 * Represents the editable primary key/foreign key relationship
 * 
 * @author Andreas Sonntag
 */
public class RelationEditPart extends AbstractConnectionEditPart implements PropertyChangeListener, TreeSelectionObjectModelProvider
{

	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate()
	{
		super.activate();
		getRelationModel().addPropertyChangeListener(this);
		getRelationModel().getJacobModel().addPropertyChangeListener(this);
	}

	/**
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate()
	{
		super.deactivate();
		getRelationModel().removePropertyChangeListener(this);
		getRelationModel().getJacobModel().removePropertyChangeListener(this);
	}

  /**
   * Wenn eine Relation selektiert wird, dann wird der entsprechende ForeingKey im ApplicationOutline
   * angezeigt.
   * 
   * @return
   */
  public ObjectModel getTreeObjectModel()
  {
    return getRelationModel().getToKey();
  }

  /**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
	    // Relations has been assigned to a relation set -> revalidate the end points of all relations
	    //
	    if(evt.getPropertyName() == ObjectModel.PROPERTY_ELEMENT_ASSIGNED)
	      ((GraphicalEditPart) (getViewer().getContents())).getFigure().revalidate();
	    
	    if(evt.getPropertyName() == ObjectModel.PROPERTY_ELEMENT_UNASSIGNED)
	      ((GraphicalEditPart) (getViewer().getContents())).getFigure().revalidate();

	    if(evt.getPropertyName() == ObjectModel.PROPERTY_PREFERENCES_CHANGED)
	     ((RelationFigure) getFigure()).layout();
	    
	    if(evt.getPropertyName() == ObjectModel.PROPERTY_KEY_CHANGED)
	    {
	      RelationFigure figure = (RelationFigure)getFigure();
	      KeyModel key = (KeyModel)evt.getNewValue();
        if(key.existsUniqueKeyWithSameFields())
        {
          if(((FieldModel)key.getFieldModels().get(0)).getRequired())
  	        figure.setType(RelationFigure.RELATION_1_TO_1);
   	     	else
   	     	  figure.setType(RelationFigure.RELATION_0_TO_1);
        }
        else
        {
          if(((FieldModel)key.getFieldModels().get(0)).getRequired())
            figure.setType(RelationFigure.RELATION_1_TO_MANY);
          else
            figure.setType(RelationFigure.RELATION_0_TO_MANY);
        }
	      figure.layout();
	    }
	}
	

  public RelationModel getRelationModel()
  {
    return (RelationModel) getModel();
  }


  /**
   * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
   */
  protected void createEditPolicies()
  {
    installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
    installEditPolicy(EditPolicy.COMPONENT_ROLE, new RelationEditPolicy());
  }

  /**
   * @see org.eclipse.gef.editparts.AbstractConnectionEditPart#createFigure()
   */
  protected IFigure createFigure()
  {
    KeyModel toKey = getRelationModel().getToKey();

    // 1-to-1 relation
    if(toKey.existsUniqueKeyWithSameFields())
    {
      if(((FieldModel)toKey.getFieldModels().get(0)).getRequired())
        return new RelationFigure(getRoot(),RelationFigure.RELATION_1_TO_1, getRelationModel().getName());
      else
        return new RelationFigure(getRoot(),RelationFigure.RELATION_0_TO_1, getRelationModel().getName());
    }
    else
    {
      if(((FieldModel)toKey.getFieldModels().get(0)).getRequired())
        return new RelationFigure(getRoot(),RelationFigure.RELATION_1_TO_MANY, getRelationModel().getName());
      else
        return new RelationFigure(getRoot(),RelationFigure.RELATION_0_TO_MANY, getRelationModel().getName());
    }
  }

  /**
   * Sets the width of the line when selected
   */
  public void setSelected(int value)
  {
    super.setSelected(value);
    if (value != EditPart.SELECTED_NONE)
      ((PolylineConnection) getFigure()).setLineWidth(2);
    else
      ((PolylineConnection) getFigure()).setLineWidth(1);
  }
}