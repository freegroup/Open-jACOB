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

import org.eclipse.draw2d.ColorConstants;
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
import org.eclipse.swt.graphics.Font;

import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.jacobform.directedit.ExtendedDirectEditManager;
import de.tif.jacob.designer.editor.jacobform.directedit.LabelEditorLocator;
import de.tif.jacob.designer.editor.jacobform.editpolicies.LabelDirectEditPolicy;
import de.tif.jacob.designer.editor.jacobform.figures.LabelFigure;
import de.tif.jacob.designer.model.I18NResourceModel;
import de.tif.jacob.designer.model.IStyleProvider;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UICaptionModel;
import de.tif.jacob.designer.model.UIDBLabelModel;
import de.tif.jacob.designer.model.UILabelModel;
import de.tif.jacob.designer.util.FontFactory;
import de.tif.jacob.util.StringUtil;

public class LabelEditPart extends GroupElementEditPart implements GraphicalEditPart , IStyleProvider
{
  public LabelEditPart()
  {
    super(LabelFigure.class);
  }

  protected LabelEditPart(Class figure)
  {
    super(figure);
  }
  
	public void createEditPolicies()
	{
		super.createEditPolicies();
    // Datenbank gebundenes Label darf nicht editiert werden
    if(!(getModel() instanceof UIDBLabelModel))
    	installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE , new LabelDirectEditPolicy());
	}

	public void performRequest(Request request)
	{
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
		{
	    if(!(getModel() instanceof UIDBLabelModel))
	    	new ExtendedDirectEditManager(this,TextCellEditor.class,new LabelEditorLocator((Label)getFigure()),(Label)getFigure()).show();
		}
	}

  public void consumeStyle(Map<String, Object> style)
  {
    UILabelModel model = (UILabelModel)getModel();

    String fontFamily = (String)style.get("font.family");
    String fontStyle = (String)style.get("font.style");
    String fontWeight = (String)style.get("font.weight");
    Integer fontSize = (Integer)style.get("font.size");
    Color color = (Color)style.get("element.color");
    Integer height = (Integer)style.get("element.height");
    Integer x = (Integer)style.get("element.x");
    Integer y = (Integer)style.get("element.y");

    if( x!=null && y !=null)
      model.setLocation(new Point(x,y));
    
    if(fontFamily!=null)
      model.setFontFamily(fontFamily);
    
    if(fontSize!=null)
      model.setFontSize(fontSize);

    if(fontStyle!=null)
      model.setFontStyle(fontStyle);
    
    if(fontWeight!=null)
      model.setFontWeight(fontWeight);

    if(color!=null)
      model.setColor(color);

    if(height!=null)
    {
      Rectangle rect = model.getConstraint();
      rect.height = height;
      model.setConstraint(rect);
    }
  }

  public void provideStyle(Map<String, Object> style, boolean withLocation)
  {
    UILabelModel model = (UILabelModel)getModel();
    style.put("font.family",model.getFontFamily());
    style.put("font.size",model.getFontSize());
    style.put("font.style",model.getFontStyle());
    style.put("font.weight",model.getFontWeight());
    style.put("element.color",model.getColor());
    style.put("element.height",model.getConstraint().height);

    if(withLocation)
    {
      style.put("element.x",model.getConstraint().x);
      style.put("element.y",model.getConstraint().y);
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
		LabelFigure figure = (LabelFigure)getFigure();
		UILabelModel model = (UILabelModel)getModel();

    Font font = FontFactory.getFont(model.getFontFamily(), model.getFontStyle(), model.getFontWeight(), model.getFontSize());
    figure.setFont(font);

    Color color = model.getColor();
    if(color == ColorConstants.black)
      color = Constants.COLOR_FONT;
    figure.setForegroundColor(color);

    if(ObjectModel.ALIGN_LEFT.equals(model.getAlign()))
      figure.setAlign(PositionConstants.LEFT);
    else if(ObjectModel.ALIGN_CENTER.equals(model.getAlign()))
      figure.setAlign(PositionConstants.CENTER);
    else
      figure.setAlign(PositionConstants.RIGHT);

    I18NResourceModel resourcebundle = model.getJacobModel().getTestResourcebundle();
		String label = model.getLabel();
		if(resourcebundle!=null && label.startsWith("%"))
		  label = StringUtil.toSaveString(resourcebundle.getValue(label.substring(1)));
		figure.setText(label);
	}
}
