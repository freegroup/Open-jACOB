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
 * Created on 10.12.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.editor.relationset.editpart;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.tools.MarqueeDragTracker;
import de.tif.jacob.designer.editor.relationset.figures.RelationsetFigure;
import de.tif.jacob.designer.editor.relationset.figures.TableAliasFigure;
import de.tif.jacob.designer.editor.relationset.layout.DelegatingLayoutManager;
import de.tif.jacob.designer.editor.relationset.policy.RelationsetEditPolicy;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.TreeSelectionObjectModelProvider;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.RelationsetStickyNoteModel;
import de.tif.jacob.designer.model.TableAliasModel;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class RelationsetEditPart extends PropertyAwareEditPart implements TreeSelectionObjectModelProvider
{
	private DelegatingLayoutManager delegatingLayoutManager;

	protected IFigure createFigure()
	{
		Figure f = new RelationsetFigure();
		delegatingLayoutManager = new DelegatingLayoutManager(this);
		f.setLayoutManager(delegatingLayoutManager);
		return f;
	}

	public RelationsetModel getRelationsetModel()
	{
		return (RelationsetModel) getModel();
	}

  public ObjectModel getTreeObjectModel()
  {
    return (ObjectModel) getModel();
  }

  /**
	 * @return List[TableAliasModel] the children Model objects
	 */
	protected List getModelChildren()
	{
	  List children = getRelationsetModel().getTableAliasModels();
	  children.addAll(getRelationsetModel().getNotes());
		return children;
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
		if(evt.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_ADDED)
		  refresh();
		else if(evt.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_REMOVED)
		  refresh();
		else
		  super.propertyChange(evt);
	}
	
	/**
	 * Creates EditPolicy objects for the EditPart. The LAYOUT_ROLE policy is
	 * left to the delegating layout manager
	 */
	protected void createEditPolicies()
	{
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new RelationsetEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, null);
	}

	/**
	 * Updates the table bounds in the model so that the same bounds can be
	 * restored after saving
	 * 
	 * @return whether the procedure execute successfully without any omissions.
	 *         The latter occurs if any TableFigure has no bounds set for any of
	 *         the Table model objects
	 */
	public boolean setTableModelBounds()
	{
		List tableParts = getChildren();

		for (Iterator iter = tableParts.iterator(); iter.hasNext();)
		{
		  Object obj = iter.next();
		  if(obj instanceof TableAliasEditPart)
		  {
				TableAliasEditPart tablePart = (TableAliasEditPart) obj;
				TableAliasFigure tableFigure = (TableAliasFigure) tablePart.getFigure();
	
				//if we don't find a node for one of the children then we should
				// continue
				if (tableFigure == null)
					continue;
	
				Rectangle bounds = tableFigure.getBounds().getCopy();
				TableAliasModel table = tablePart.getTableAliasModel();
				getRelationsetModel().setTableAliasBounds(table, bounds);
		  }
		}

		return true;
	}

	/**
	 * Updates the bounds of the table figure (without invoking any event
	 * handling), and sets layout constraint data
	 * 
	 * @return whether the procedure execute successfully without any omissions.
	 *         The latter occurs if any Table objects have no bounds set or if
	 *         no figure is available for the TablePart
	 */
	public boolean setTableFigureBounds(boolean updateConstraint)
  {
    List tableParts = getChildren();

    for (Iterator iter = tableParts.iterator(); iter.hasNext();)
    {
      PropertyAwareEditPart obj = (PropertyAwareEditPart)iter.next();
      Rectangle bounds=null;
      if(obj instanceof TableAliasEditPart)
      {
	      TableAliasEditPart tablePart = (TableAliasEditPart)obj;
	      TableAliasModel table = tablePart.getTableAliasModel();
	
	      //now check whether we can find an entry in the tableToNodesMap
	      bounds = getRelationsetModel().getTableAliasBounds(table);
	      // TODO: handle this better
	      if (bounds == null)
	        return false;
	
      }
      else if(obj instanceof StickyNoteEditPart)
      {
	      bounds = ((RelationsetStickyNoteModel)((StickyNoteEditPart)obj).getModel()).getBounds();
      }
      IFigure figure = obj.getFigure();
      if (figure == null)
      {
        return false;
      }
      else if (updateConstraint)
      {
        //pass the constraint information to the xy layout
        //setting the width and height so that the preferred size will be
        // applied
        
        delegatingLayoutManager.setXYLayoutConstraint(figure, new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height));
      }
    }
    return true;
  }

  /***
   * This method is not mandatory to implement, but if you do not implement
   * it, you will not have the ability to rectangle-selects several figures...
   */
   public DragTracker getDragTracker(Request req) 
   {
     // Unlike in Logical Diagram Editor example, I use a singleton because
     // this
     // method is called several time, so I prefer to save memory ; and it
     // works!
     if (m_dragTracker == null) 
     {
         m_dragTracker = new MarqueeDragTracker();
     }
     return m_dragTracker;
   }
   
       /*** Singleton instance of MarqueeDragTracker. */
       static DragTracker m_dragTracker = null;
  /**
	 * Passes on to the delegating layout manager that the layout type has
	 * changed. The delegating layout manager will then decide whether to
	 * delegate layout to the XY or Graph layout
	 */
	protected void handleLayoutChange(PropertyChangeEvent evt)
	{
		Boolean layoutType = (Boolean) evt.getNewValue();
		boolean isManualLayoutDesired = layoutType.booleanValue();
		getFigure().setLayoutManager(delegatingLayoutManager);
	}
}
