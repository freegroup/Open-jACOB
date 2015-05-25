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
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.ui.IActionFilter;
import de.tif.jacob.designer.editor.jacobform.directedit.ExtendedDirectEditManager;
import de.tif.jacob.designer.editor.jacobform.directedit.LabelEditorLocator;
import de.tif.jacob.designer.editor.jacobform.editpolicies.ButtonDirectEditPolicy;
import de.tif.jacob.designer.editor.jacobform.figures.AbstractChartFigure;
import de.tif.jacob.designer.editor.jacobform.figures.ButtonFigure;
import de.tif.jacob.designer.editor.jacobform.figures.ObjectFigure;
import de.tif.jacob.designer.model.AbstractChartModel;
import de.tif.jacob.designer.model.I18NResourceModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.UIButtonModel;
import de.tif.jacob.util.StringUtil;

public class AbstractChartEditPart extends GroupElementEditPart
{
  
  public AbstractChartEditPart(Class figureClass)
  {
    super(figureClass);
  }
  

	public void propertyChange(PropertyChangeEvent ev)
	{
		if(ev.getSource()==getModel())
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
    AbstractChartFigure  figure = (AbstractChartFigure)getFigure();
    AbstractChartModel model  = (AbstractChartModel)getModel();
		
		
    figure.setBackground(model.getHasBackground());
    figure.setGrid(model.getHasGrid());
    figure.setLegendX(model.getHasLegendX());
    figure.setLegendY(model.getHasLegendY());
    figure.setTitle(model.getHasTitle());
	}
  

}
