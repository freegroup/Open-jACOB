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
import java.util.Map;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IActionFilter;
import de.tif.jacob.designer.editor.jacobform.directedit.ExtendedDirectEditManager;
import de.tif.jacob.designer.editor.jacobform.directedit.LabelEditorLocator;
import de.tif.jacob.designer.editor.jacobform.editpolicies.ButtonDirectEditPolicy;
import de.tif.jacob.designer.editor.jacobform.figures.ButtonFigure;
import de.tif.jacob.designer.editor.jacobform.figures.ObjectFigure;
import de.tif.jacob.designer.model.I18NResourceModel;
import de.tif.jacob.designer.model.IStyleProvider;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.UIButtonModel;
import de.tif.jacob.designer.model.UIDBTextModel;
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.designer.model.UILabelModel;
import de.tif.jacob.util.StringUtil;

public class ButtonEditPart extends GroupElementEditPart implements IStyleProvider, IActionFilter
{
  
  public ButtonEditPart()
  {
    super(ButtonFigure.class);
  }
  
	public void createEditPolicies()
	{
		super.createEditPolicies();
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE , new ButtonDirectEditPolicy());
	}

	public void performRequest(Request request)
	{
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT && 
		    (getButtonModel().getAction()==ObjectModel.ACTION_GENERIC || getButtonModel().getAction()==ObjectModel.ACTION_SELECTED))
		{
		  // Falls ein Resourcebundle als Test-Resourcebundle eingestellt ist, wird der �bersetze
		  // Wert angezeigt. => Es muss das original Label in der Figur gesetzt werden, damit DirectEdit diesen 
		  // bearbeiten kann
//      String label = getButtonModel().getLabel();
//      getObjectFigure().setText(label);
		  new ExtendedDirectEditManager(this,TextCellEditor.class,new LabelEditorLocator((Label)getFigure()),(Label)getFigure()).show();
		}
		else
		  super.performRequest(request);
	}


  public void consumeStyle(Map<String, Object> style)
  {
    UIButtonModel model  = getButtonModel();

    Integer height = (Integer)style.get("element.height");
    Integer width = (Integer)style.get("element.width");
    Integer x = (Integer)style.get("element.x");
    Integer y = (Integer)style.get("element.y");

    if( x!=null && y !=null)
      model.setLocation(new Point(x,y));
    
    if (height != null || width != null)
    {
      Rectangle rect = model.getConstraint();
      
      if (height != null)
        rect.height = height;
      
      if (width != null)
        rect.width = width;
      
      model.setConstraint(rect);
    }
  }

  public void provideStyle(Map<String, Object> style, boolean withLocation)
  {
    UIButtonModel model  = getButtonModel();
    
    style.put("element.height",model.getConstraint().height);
    style.put("element.width",model.getConstraint().width);
    if(withLocation)
    {
      style.put("element.x",model.getConstraint().x);
      style.put("element.y",model.getConstraint().y);
    }
  }
  
  
  public void propertyChange(PropertyChangeEvent ev)
	{
		if(ev.getPropertyName()==ObjectModel.PROPERTY_RELATIONSET_CHANGED)
			refreshVisuals();
		if(ev.getPropertyName()==ObjectModel.PROPERTY_FILLDIRECTION_CHANGED)
			refreshVisuals();
    if(ev.getPropertyName()==ObjectModel.PROPERTY_LABEL_CHANGED)
      refreshVisuals();
    if(ev.getPropertyName()==ObjectModel.PROPERTY_EMPHASIZE_CHANGED)
      refreshVisuals();
    if(ev.getPropertyName()==ObjectModel.PROPERTY_ALIGN_CHANGED)
      refreshVisuals();
		else
			super.propertyChange(ev);
	}

	/**
	 * 
	 */
  public void refreshVisuals()
	{
		super.refreshVisuals();
		ButtonFigure  figure = (ButtonFigure)getFigure();
		UIButtonModel model  = getButtonModel();
		
		figure.setHighlight(ObjectFigure.HIGHLIGHT_LABEL);
		
		I18NResourceModel resourcebundle = model.getJacobModel().getTestResourcebundle();
		String label = model.getLabel();
		if(resourcebundle!=null && label.startsWith("%"))
		{
		  label = StringUtil.toSaveString(resourcebundle.getValue(label.substring(1)));
			if(label.length()==0)
			{
			  figure.setHighlight(ObjectFigure.HIGHLIGHT_I18NERROR);
			  label = model.getLabel();
			}
		}
    
    if(ObjectModel.ALIGN_LEFT.equals(model.getAlign()))
      figure.setAlign(PositionConstants.LEFT);
    else if(ObjectModel.ALIGN_RIGHT.equals(model.getAlign()))
      figure.setAlign(PositionConstants.RIGHT);
    else
      figure.setAlign(PositionConstants.CENTER);

    figure.setText(label);
		figure.setEmphasize(model.getEmphasize());
		if(model.getAction()==ObjectModel.ACTION_SEARCH || model.getAction()==ObjectModel.ACTION_SEARCH_UPDATE || model.getAction()==ObjectModel.ACTION_LOCALSEARCH)
		{
		  String filldirection= model.getFilldirection();
		  if(filldirection == ObjectModel.FILLDIRECTION_BACKWARD)
		  	figure.setFilldirectionDecoration(ButtonFigure.DECORATION_SEARCH_BACKWARD);
		  else if(filldirection == ObjectModel.FILLDIRECTION_FORWARD)
		  	figure.setFilldirectionDecoration(ButtonFigure.DECORATION_SEARCH_FORWARD);
      else if(filldirection == ObjectModel.FILLDIRECTION_BOTH)
        figure.setFilldirectionDecoration(ButtonFigure.DECORATION_SEARCH_BOTH);
		  else
		  	figure.setFilldirectionDecoration(ButtonFigure.DECORATION_SEARCH_NONE);
		}
	}
  
  /**
   * 
   * @return
   */
  public UIButtonModel getButtonModel()
  {
    return (UIButtonModel)getModel();
  }
  

  public boolean testAttribute(Object target, String name, String value)
  {
    if ("templateName".equals(name))
    {
      UIGroupElementModel model = ((GroupElementEditPart) target).getGroupElementModel();
      return StringUtil.saveEquals(model.getTemplateFileName(), value);
    }
    UIButtonModel button = ((ButtonEditPart) target).getButtonModel();
    return ((button.getAction().equals(ObjectModel.ACTION_SEARCH) || button.getAction().equals(ObjectModel.ACTION_SEARCH_UPDATE)) && !button.getRelationset().equals(RelationsetModel.RELATIONSET_DEFAULT) && !button.getRelationset()
        .equals(RelationsetModel.RELATIONSET_LOCAL));
  } 
}
