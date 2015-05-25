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
 * Created on Jul 21, 2004
 */
package de.tif.jacob.designer.editor.relationset.layout;

import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import de.tif.jacob.designer.editor.relationset.editpart.RelationsetEditPart;

/**
 * Subclass of XYLayout which can use the child figures actual bounds as a constraint
 * when doing manual layout (XYLayout)
 * @author Phil Zoio
 */
public class GraphXYLayout extends FreeformLayout
{
	
	public GraphXYLayout(RelationsetEditPart diagram)
	{
	}
	
	

	public Object getConstraint(IFigure child)
	{
		Object constraint = constraints.get(child);
		if (constraint != null || constraint instanceof Rectangle)
		{
			return (Rectangle)constraint;
		}
		else
		{
			Rectangle currentBounds = child.getBounds();
			return new Rectangle(currentBounds.x, currentBounds.y, -1,-1);
		}
	}
	
}
