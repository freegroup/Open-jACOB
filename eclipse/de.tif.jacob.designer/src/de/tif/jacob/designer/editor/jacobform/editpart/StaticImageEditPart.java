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
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IActionFilter;
import de.tif.jacob.designer.editor.jacobform.directedit.ExtendedDirectEditManager;
import de.tif.jacob.designer.editor.jacobform.directedit.LabelEditorLocator;
import de.tif.jacob.designer.editor.jacobform.editpolicies.ButtonDirectEditPolicy;
import de.tif.jacob.designer.editor.jacobform.figures.AbstractChartFigure;
import de.tif.jacob.designer.editor.jacobform.figures.ButtonFigure;
import de.tif.jacob.designer.editor.jacobform.figures.ObjectFigure;
import de.tif.jacob.designer.editor.jacobform.figures.StaticImageFigure;
import de.tif.jacob.designer.model.AbstractChartModel;
import de.tif.jacob.designer.model.I18NResourceModel;
import de.tif.jacob.designer.model.IStyleProvider;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.UIButtonModel;
import de.tif.jacob.designer.model.UIStaticImageModel;
import de.tif.jacob.util.StringUtil;

public class StaticImageEditPart extends GroupElementEditPart implements IStyleProvider
{
  
  public StaticImageEditPart()
  {
    super(StaticImageFigure.class);
  }
  


	/**
	 * 
	 */
  public void refreshVisuals()
	{
		super.refreshVisuals();
    
    StaticImageFigure  figure = (StaticImageFigure)getFigure();
    UIStaticImageModel model  = (UIStaticImageModel)getModel();
		figure.setText("");
		Image img = model.getImg(); 
    figure.setIcon(img);
	}


  public void consumeStyle(Map<String, Object> style)
  {
    UIStaticImageModel model  = (UIStaticImageModel)getModel();

    Integer x = (Integer)style.get("element.x");
    Integer y = (Integer)style.get("element.y");

    if( x!=null && y !=null)
      model.setLocation(new Point(x,y));
  }

  public void provideStyle(Map<String, Object> style, boolean withLocation)
  {
    UIStaticImageModel model  = (UIStaticImageModel)getModel();
    
    if(withLocation)
    {
      style.put("element.x",model.getConstraint().x);
      style.put("element.y",model.getConstraint().y);
    }
  }
  

}
