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
package de.tif.jacob.designer.editor.jacobform.editpart;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.rulers.RulerProvider;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.jacobform.editpolicies.XYLayoutEditPolicyFormImpl;
import de.tif.jacob.designer.editor.jacobform.figures.FormFigure;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.TreeSelectionObjectModelProvider;
import de.tif.jacob.designer.model.UIJacobFormModel;

public class FormEditPart extends ObjectEditPart implements TreeSelectionObjectModelProvider
{
	public IFigure createFigure()
	{
		return new FormFigure();
	}
	
	public List getModelChildren()
	{
		UIJacobFormModel model = (UIJacobFormModel) getModel();
		return model.getElements();
	}

  
  /**
   * Liefert das Objekt Model zurück welches im ApplicationOutline selektiert werden
   * soll, wenn dieses Element selektiert wird (sync-Mode)
   */
  public ObjectModel getTreeObjectModel()
  {
    return getObjectModel();
  }

  public void createEditPolicies()
	{
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicyFormImpl());
		installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
	}
	
	public void propertyChange(PropertyChangeEvent ev)
	{
		super.propertyChange(ev);
		if (ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_ADDED)
    {
			refreshChildren();
    }
		else if (ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_REMOVED)
    {
			refreshChildren();
    }
	}

	
  public Object getAdapter(Class adapter) 
  {
  	if (adapter == SnapToHelper.class) 
  	{
  		List snapStrategies = new ArrayList();
  		Boolean val = (Boolean)getViewer().getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY);
  		if (val != null && val.booleanValue())
  			snapStrategies.add(new SnapToGuides(this));
  		val = (Boolean)getViewer().getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
  		if (val != null && val.booleanValue())
  			snapStrategies.add(new SnapToGeometry(this));
  		val = (Boolean)getViewer().getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
  		if (val != null && val.booleanValue())
  			snapStrategies.add(new SnapToGrid(this));
  		
  		if (snapStrategies.size() == 0)
  			return null;
  		if (snapStrategies.size() == 1)
  			return (SnapToHelper)snapStrategies.get(0);

  		SnapToHelper ss[] = new SnapToHelper[snapStrategies.size()];
  		for (int i = 0; i < snapStrategies.size(); i++)
  			ss[i] = (SnapToHelper)snapStrategies.get(i);
  		return new CompoundSnapToHelper(ss);
  	}
  	return super.getAdapter(adapter);
  }

  
}
