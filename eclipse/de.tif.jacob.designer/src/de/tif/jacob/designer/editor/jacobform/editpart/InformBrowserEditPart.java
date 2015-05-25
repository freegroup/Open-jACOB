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
import java.util.Iterator;
import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import de.tif.jacob.designer.editor.jacobform.figures.InformBrowserFigure;
import de.tif.jacob.designer.editor.jacobform.figures.ObjectFigure;
import de.tif.jacob.designer.model.BrowserFieldModel;
import de.tif.jacob.designer.model.I18NResourceModel;
import de.tif.jacob.designer.model.IStyleProvider;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.UIDBInformBrowserModel;
import de.tif.jacob.designer.model.UIDBTableListBoxModel;
import de.tif.jacob.util.StringUtil;

public class InformBrowserEditPart extends GroupElementEditPart implements GraphicalEditPart, IStyleProvider
{
  public InformBrowserEditPart()
  {
    super(InformBrowserFigure.class);
  }
  
	public void propertyChange(PropertyChangeEvent ev)
	{
		super.propertyChange(ev);
		if(ev.getPropertyName()==ObjectModel.PROPERTY_BROWSER_CHANGED)
		  refreshVisuals();
		if(ev.getPropertyName()==ObjectModel.PROPERTY_CAPTION_CHANGED)
		  refreshVisuals();
	}


	public void setHighlight(RelationsetModel relationset)
	{
	  if(relationset==null)
	    getObjectFigure().setHighlight(ObjectFigure.HIGHLIGHT_NONE);
	  else if(relationset.contains(((UIDBInformBrowserModel)getModel()).getTableAliasModel()))
	    getObjectFigure().setHighlight(ObjectFigure.HIGHLIGHT_TRUE);
	  else
	    getObjectFigure().setHighlight(ObjectFigure.HIGHLIGHT_FALSE);
	}
	
	public void refreshVisuals()
	{
		super.refreshVisuals();

		InformBrowserFigure figure = (InformBrowserFigure)getFigure();
		UIDBInformBrowserModel model = (UIDBInformBrowserModel)getModel();
    I18NResourceModel resourcebundle = model.getJacobModel().getTestResourcebundle();

	  figure.setHighlight(ObjectFigure.HIGHLIGHT_LABEL);
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
		
		// 
		figure.clearColumns();
		Iterator iter =model.getBrowserModel().getBrowserFieldModels().iterator();
		while(iter.hasNext())
		{
			BrowserFieldModel field = (BrowserFieldModel)iter.next();
			label = field.getLabel();
			if(resourcebundle!=null && label.startsWith("%"))
			{
			  label = StringUtil.toSaveString(resourcebundle.getValue(label.substring(1)));
				if(label.length()==0)
				{
				  figure.setHighlight(ObjectFigure.HIGHLIGHT_I18NERROR);
				  label = field.getLabel();
				}
			}
			figure.addColumn(label);
		}
		setHighlight(model.getJacobModel().getTestRelationset());
	}


  public void consumeStyle(Map<String, Object> style)
  {
    UIDBInformBrowserModel model = (UIDBInformBrowserModel)getModel();

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
    UIDBInformBrowserModel model = (UIDBInformBrowserModel)getModel();

    style.put("element.height",model.getConstraint().height);
    style.put("element.width",model.getConstraint().width);
    if(withLocation)
    {
      style.put("element.x",model.getConstraint().x);
      style.put("element.y",model.getConstraint().y);
    }
  }
}
