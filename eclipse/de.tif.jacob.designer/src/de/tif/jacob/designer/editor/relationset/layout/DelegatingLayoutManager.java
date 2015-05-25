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

import java.util.Map;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import de.tif.jacob.designer.editor.relationset.editpart.RelationsetEditPart;
import de.tif.jacob.designer.editor.relationset.policy.SchemaXYLayoutPolicy;
import de.tif.jacob.designer.model.RelationsetModel;

/**
 * Used to delegate between the GraphyLayoutManager and the GraphXYLayout classes
 * @author Phil Zoio
 */
public class DelegatingLayoutManager implements LayoutManager
{
	private RelationsetEditPart diagram;
//	private Map figureToBoundsMap;
//	private Map partsToNodeMap;

	private LayoutManager activeLayoutManager;
	private GraphLayoutManager graphLayoutManager;
	private GraphXYLayout xyLayoutManager;

	public DelegatingLayoutManager(RelationsetEditPart diagram)
	{
		this.diagram = diagram;
		this.graphLayoutManager = new GraphLayoutManager(diagram);
		this.xyLayoutManager = new GraphXYLayout(diagram);

		//use the graph layout manager as the initial delegate
		this.activeLayoutManager = this.graphLayoutManager;
	}

	//********************* layout manager methods methods
	// ****************************/

	public void layout(IFigure container)
	{
	  System.out.println("DelegatingLayoutManager.layout(IFigure container)");
		RelationsetModel schema = diagram.getRelationsetModel();

		if (schema.isLayoutManualDesired())
		{
			if (activeLayoutManager != xyLayoutManager)
			{
				if (schema.isLayoutManualAllowed())
				{
					//	yes we are okay to start populating the table bounds
					setLayoutManager(container, xyLayoutManager);
					activeLayoutManager.layout(container);
				}
				else if (diagram.setTableFigureBounds(true))
				{
						//we successfully set bounds for all the existing
						// tables so we can start using xyLayout immediately
						setLayoutManager(container, xyLayoutManager);
						activeLayoutManager.layout(container);
				}
				else
				{
						//we did not - we still need to run autolayout once
						// before we can set xyLayout
						activeLayoutManager.layout(container);

						//run this again so that it will work again next time
						setLayoutManager(container, xyLayoutManager);
				}
			}
			else
			{
				setLayoutManager(container, xyLayoutManager);
				activeLayoutManager.layout(container);
			}
		}
		else
		{
			setLayoutManager(container, graphLayoutManager);
			activeLayoutManager.layout(container);
		}
	}

	public Object getConstraint(IFigure child)
	{
	  System.out.println("DelegatingLayoutManager.getConstraint(IFigure child)");
		return activeLayoutManager.getConstraint(child);
	}

	public Dimension getMinimumSize(IFigure container, int wHint, int hHint)
	{
	  System.out.println("DelegatingLayoutManager.getMinimumSize(IFigure container, int wHint, int hHint)");
		return activeLayoutManager.getMinimumSize(container, wHint, hHint);
	}

	public Dimension getPreferredSize(IFigure container, int wHint, int hHint)
	{
	  System.out.println("DelegatingLayoutManager.getPreferredSize(IFigure container, int wHint, int hHint)");
		return activeLayoutManager.getPreferredSize(container, wHint, hHint);
	}

	public void invalidate()
	{
	  System.out.println("DelegatingLayoutManager.invalidate");
		activeLayoutManager.invalidate();
	}

	public void remove(IFigure child)
	{
	  System.out.println("DelegatingLayoutManager.remove(IFigure child)");
		activeLayoutManager.remove(child);
	}

	public void setConstraint(IFigure child, Object constraint)
	{
	  System.out.println("DelegatingLayoutManager.setConstraint(IFigure child, Object constraint)");
		activeLayoutManager.setConstraint(child, constraint);
	}

	public void setXYLayoutConstraint(IFigure child, Rectangle constraint)
	{
	  System.out.println("DelegatingLayoutManager.setXYLayoutConstraint(IFigure child, Rectangle constraint)");
		xyLayoutManager.setConstraint(child, constraint);
	}

	/**
	 * Sets the current active layout manager
	 */
	private void setLayoutManager(IFigure container, LayoutManager layoutManager)
	{
	  System.out.println("DelegatingLayoutManager.setLayoutManager(IFigure container, LayoutManager layoutManager)");
		container.setLayoutManager(layoutManager);
		this.activeLayoutManager = layoutManager;
		if (layoutManager == xyLayoutManager)
		{
			diagram.installEditPolicy(EditPolicy.LAYOUT_ROLE, new SchemaXYLayoutPolicy());
		}
		else
		{
			diagram.installEditPolicy(EditPolicy.LAYOUT_ROLE, null);
		}
	}
	
	public LayoutManager getActiveLayoutManager()
	{
	  System.out.println("DelegatingLayoutManager.getActiveLayoutManager");
		return activeLayoutManager;
	}

}