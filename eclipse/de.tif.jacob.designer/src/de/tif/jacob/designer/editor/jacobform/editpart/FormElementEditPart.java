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
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.swt.graphics.Font;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.ShowObjectModelHookAction;
import de.tif.jacob.designer.model.IFontProviderModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.UIFormElementModel;
import de.tif.jacob.designer.util.FontFactory;

public abstract class FormElementEditPart extends ObjectEditPart
{
  public abstract void openDBEditor();
  
  public  void openEventEditor()
  {
    new ShowObjectModelHookAction()
    {
      public ObjectModel getObjectModel()
      {
        return (ObjectModel)getModel();
      }
    }.run(null);
  }
  

  public UIFormElementModel getFormElementModel()
  {
    return (UIFormElementModel)getModel();
  }

  public void performRequest(Request req)
  {
		if(req.getType() == RequestConstants.REQ_OPEN)
		{
		  if(JacobDesigner.getPlugin().getPreferenceStore().getString(JacobDesigner.DOUBLECLICK_DB).equals("dbDefinition"))
		    openDBEditor();
		  else
		    openEventEditor();
		}
  }
  
	public void refreshVisuals()
	{
		super.refreshVisuals();
		refreshParentLayout();
		UIFormElementModel model = (UIFormElementModel)getModel();
		getObjectFigure().setText(model.getName());
		
		getObjectFigure().setHook( model.getJacobModel().getApplicationModel().isEventHandlerLookupByReference() && model.getHookClassName()!=null);

    // error/warning/info handling
		//
		String text=model.getError();
		getObjectFigure().setError(text);
		if(text!=null)
		  return;
		text = model.getWarning();
		getObjectFigure().setWarning(text);
		if(text!=null)
		  return;
		text = model.getInfo();
		getObjectFigure().setInfo(text);
	}

	public void setHighlight(RelationsetModel relationset)
	{
	}
	
	public void propertyChange(PropertyChangeEvent ev)
	{
		try
    {
      if(ev.getSource() instanceof JacobModel)
      {
      	if(ev.getPropertyName()==ObjectModel.PROPERTY_TESTRELATIONSET_CHANGED)
      	{
      	  setHighlight((RelationsetModel)ev.getNewValue());
      		refreshVisuals();
      	}
      	if(ev.getPropertyName()==ObjectModel.PROPERTY_TESTRESOURCEBUNDLE_CHANGED)
      		refreshVisuals();
      }
      else if(ev.getSource()==getModel())
      {
        refreshVisuals();
      }
      else
      {
      	if(ev.getPropertyName()==ObjectModel.PROPERTY_CONSTRAINT_CHANGED)
          refreshVisuals();
      	if(ev.getPropertyName()==ObjectModel.PROPERTY_LABEL_CHANGED)
      		refreshVisuals();
      	if(ev.getPropertyName()==ObjectModel.PROPERTY_NAME_CHANGED)
      		refreshVisuals();
      	if(ev.getPropertyName()==ObjectModel.PROPERTY_CAPTION_CHANGED)
      		refreshVisuals();
      	if(ev.getPropertyName()==ObjectModel.PROPERTY_EVENTHANDLER_CHANGED && ev.getSource()==getModel())
      		refreshVisuals();
      }
      super.propertyChange(ev);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
	}
	
	protected void refreshParentLayout()
	{
		UIFormElementModel model = (UIFormElementModel)getModel();
		Rectangle constraint = model.getConstraint();
			
		ObjectEditPart parent = (ObjectEditPart)getParent();
		IFigure parentContentPane = parent.getContentPane();
		if(parentContentPane!=null)
		{
		  LayoutManager parentLayoutManager = parentContentPane.getLayoutManager();
		  if(parentLayoutManager instanceof XYLayout)
		    parentLayoutManager.setConstraint(getFigure(), constraint);
		  parentContentPane.revalidate();
		}
	}
}
