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
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Color;

import de.tif.jacob.designer.editor.jacobform.directedit.ExtendedDirectEditManager;
import de.tif.jacob.designer.editor.jacobform.directedit.LabelEditorLocator;
import de.tif.jacob.designer.editor.jacobform.editpolicies.LabelDirectEditPolicy;
import de.tif.jacob.designer.editor.jacobform.editpolicies.StyledTextDirectEditPolicy;
import de.tif.jacob.designer.editor.jacobform.figures.StyledTextFigure;
import de.tif.jacob.designer.model.I18NResourceModel;
import de.tif.jacob.designer.model.IStyleProvider;
import de.tif.jacob.designer.model.UICaptionModel;
import de.tif.jacob.designer.model.UIDBListboxModel;
import de.tif.jacob.designer.model.UIStyledTextModel;
import de.tif.jacob.util.StringUtil;

public class StyledTextEditPart extends GroupElementEditPart implements GraphicalEditPart , IStyleProvider
{
  public StyledTextEditPart()
  {
    super(StyledTextFigure.class);
  }

  protected StyledTextEditPart(Class figure)
  {
    super(figure);
  }
  
	public void createEditPolicies()
	{
		super.createEditPolicies();
  	installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE , new StyledTextDirectEditPolicy());
	}

	public void performRequest(Request request)
	{
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
		{
      Label label =((StyledTextFigure)getFigure()).getLabel();
	    	new ExtendedDirectEditManager(this,TextCellEditor.class,new LabelEditorLocator(label),label).show();
		}
	}

	public void propertyChange(PropertyChangeEvent ev)
	{
		super.propertyChange(ev);

		if(ev.getSource()==getModel())
			refreshVisuals();
	}

  public void refreshVisuals()
	{
		super.refreshVisuals();
    StyledTextFigure figure = (StyledTextFigure)getFigure();
		UIStyledTextModel model = (UIStyledTextModel)getModel();

    I18NResourceModel resourcebundle = model.getJacobModel().getTestResourcebundle();
		String label = model.getLabel();
		if(resourcebundle!=null && label.startsWith("%"))
		  label = StringUtil.toSaveString(resourcebundle.getValue(label.substring(1)));
		figure.setText(label);
	}

  public void consumeStyle(Map<String, Object> style)
  {
    UIStyledTextModel model = (UIStyledTextModel)getModel();

    Integer height = (Integer)style.get("element.height");
    Integer width = (Integer)style.get("element.width");
    Integer x = (Integer)style.get("element.x");
    Integer y = (Integer)style.get("element.y");

    if( x!=null && y !=null)
      model.setLocation(new Point(x,y));

    if(height!=null || width!=null)
    {
      Rectangle rect = model.getConstraint();
      
      if(height!=null)
        rect.height = height;
      
      if(width!=null)
        rect.width = width;
      
      model.setConstraint(rect);
    }
  }

  public void provideStyle(Map<String, Object> style, boolean withLocation)
  {
    UIStyledTextModel model = (UIStyledTextModel)getModel();

    style.put("element.height",model.getConstraint().height);
    style.put("element.width",model.getConstraint().width);

    if(withLocation)
    {
      style.put("element.x",model.getConstraint().x);
      style.put("element.y",model.getConstraint().y);
    }
  }
 }
