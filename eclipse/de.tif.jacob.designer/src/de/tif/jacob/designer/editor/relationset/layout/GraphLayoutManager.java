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
package de.tif.jacob.designer.editor.relationset.layout;

import java.util.List;
import java.util.Map;
import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import de.tif.jacob.designer.editor.relationset.editpart.RelationsetEditPart;

/**
 * Uses the DirectedGraphLayoutVisitor to automatically lay out figures on diagram
 * @author Phil Zoio
 */
public class GraphLayoutManager extends AbstractLayout
{
	private RelationsetEditPart diagram;

	public GraphLayoutManager(RelationsetEditPart diagram)
	{
	  System.out.println("GraphLayoutManager.GraphLayoutManager(RelationsetEditPart diagram)");
		this.diagram = diagram;
	}

	
	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint)
	{		
	  System.out.println("GraphLayoutManager.calculatePreferredSize(IFigure container, int wHint, int hHint)");
		container.validate();
		List children = container.getChildren();
		Rectangle result = new Rectangle().setLocation(container.getClientArea().getLocation());
		for (int i = 0; i < children.size(); i++)
			result.union(((IFigure) children.get(i)).getBounds());
		result.resize(container.getInsets().getWidth(), container.getInsets().getHeight());
		return result.getSize();		
	}

	
	public void layout(IFigure container)
	{
	  System.out.println("GraphLayoutManager.layout(IFigure container)");
	
		new DirectedGraphLayoutVisitor().layoutDiagram(diagram);
		diagram.setTableModelBounds();
	}
}