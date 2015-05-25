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
import org.eclipse.draw2d.IFigure;
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
import org.eclipse.ui.IActionFilter;

import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.jacobform.directedit.ExtendedDirectEditManager;
import de.tif.jacob.designer.editor.jacobform.directedit.LabelEditorLocator;
import de.tif.jacob.designer.editor.jacobform.editpolicies.CaptionDirectEditPolicy;
import de.tif.jacob.designer.editor.jacobform.figures.CaptionFigure;
import de.tif.jacob.designer.editor.jacobform.figures.ObjectFigure;
import de.tif.jacob.designer.model.I18NResourceModel;
import de.tif.jacob.designer.model.IStyleProvider;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UICaptionModel;
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.designer.util.FontFactory;
import de.tif.jacob.util.StringUtil;

public class CaptionEditPart extends GroupElementEditPart implements IStyleProvider, IActionFilter
{
  
  /**
   * @param figureClass
   */
  public CaptionEditPart()
  {
    super(CaptionFigure.class);
  }

  public void openDBEditor()
  {
  }
  
	public void createEditPolicies()
	{
		super.createEditPolicies();
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE , new CaptionDirectEditPolicy());
	}

	public void performRequest(Request request)
	{
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
		{
		  // Falls ein Resourcebundle als Test-Resourcebundle eingestellt ist, wird der �bersetze
		  // Wert angezeigt. => Es muss das original Label in der Figur gesetzt werden, damit DirectEdit diesen 
		  // bearbeiten kann
//      String label = getCaptionModel().getCaption();
//      getObjectFigure().setText(label);
		  new ExtendedDirectEditManager(this,TextCellEditor.class,new LabelEditorLocator((Label)getFigure()),(Label)getFigure()).show();
		}
	}

	public void propertyChange(PropertyChangeEvent ev)
	{
		super.propertyChange(ev);

    if(ev.getPropertyName()==ObjectModel.PROPERTY_LABEL_CHANGED)
      refreshVisuals();
    else if(ev.getPropertyName()==ObjectModel.PROPERTY_ALIGN_CHANGED)
      refreshVisuals();
    else if(ev.getPropertyName()==ObjectModel.PROPERTY_FONT_CHANGED)
      refreshVisuals();
    else if(ev.getPropertyName()==ObjectModel.PROPERTY_COLOR_CHANGED)
      refreshVisuals();
	}
	

  public void consumeStyle(Map<String, Object> style)
  {
    UICaptionModel model = (UICaptionModel)getModel();

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
    UICaptionModel model = (UICaptionModel)getModel();
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

  public void refreshVisuals()
	{
		super.refreshVisuals();
		CaptionFigure figure = (CaptionFigure)getFigure();
		UICaptionModel model = (UICaptionModel)getModel();

    if(ObjectModel.ALIGN_LEFT.equals(model.getAlign()))
      figure.setAlign(PositionConstants.LEFT);
    else if(ObjectModel.ALIGN_CENTER.equals(model.getAlign()))
      figure.setAlign(PositionConstants.CENTER);
    else
      figure.setAlign(PositionConstants.RIGHT);
    
		figure.setHighlight(ObjectFigure.HIGHLIGHT_LABEL);
		
    Font font = FontFactory.getFont(model.getFontFamily(), model.getFontStyle(), model.getFontWeight(), model.getFontSize());
    figure.setFont(font);
    
    Color color = model.getColor();
    if(color == ColorConstants.black)
      color = Constants.COLOR_FONT;
    figure.setForegroundColor(color);
    
		I18NResourceModel resourcebundle = model.getJacobModel().getTestResourcebundle();
		String label = model.getCaption();
		if(resourcebundle!=null && label.startsWith("%"))
		{
		  label = StringUtil.toSaveString(resourcebundle.getValue(label.substring(1)));
			if(label.length()==0)
			{
			  figure.setHighlight(ObjectFigure.HIGHLIGHT_I18NERROR);
			  label = model.getCaption();
			}
		}
		figure.setText(label);
	}
  

  public UICaptionModel getCaptionModel()
  {
    return (UICaptionModel)getModel();
  }
  
  public boolean testAttribute(Object target, String name, String value)
  {
    UICaptionModel caption = ((CaptionEditPart) target).getCaptionModel();
    if (name.equals("caption"))
      return caption.getCaption() != null && caption.getCaption().startsWith(value);
    else if (name.equals("exists"))
      return caption.getJacobModel().hasI18NKey(caption.getCaption().substring(1)) == new Boolean(value).booleanValue();
    else if ("templateName".equals(name))
    {
      UIGroupElementModel model = ((GroupElementEditPart) target).getGroupElementModel();
      return StringUtil.saveEquals(model.getTemplateFileName(), value);
    }
    
    return super.testAttribute(target, name,value);
  }
}
