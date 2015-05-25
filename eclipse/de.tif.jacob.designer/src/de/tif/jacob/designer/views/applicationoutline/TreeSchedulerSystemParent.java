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
 * Created on Sep 3, 2004
 *
 */
package de.tif.jacob.designer.views.applicationoutline;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertySource;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.JavaClassModel;
import de.tif.jacob.designer.model.ObjectModel;

public class TreeSchedulerSystemParent extends TreeParent
{
  /**
   * @param name
   */
  public TreeSchedulerSystemParent(TreeViewer viewer, TreeParent parent, JacobModel jacob) 
  {
    super(viewer, parent, jacob,jacob,  "System level");
  } 
  
  
  public void addChildren()
  {
		Iterator iter = jacobModel.getScheduledJobSystemModels().iterator();
		while (iter.hasNext())
    {
      JavaClassModel job = (JavaClassModel) iter.next();
      addChild(new TreeJavaClassModelObject(viewer, this, jacobModel, job));
    }
  }

	public void propertyChange(PropertyChangeEvent ev)
	{
    super.propertyChange(ev);

    if(ev.getPropertyName()==ObjectModel.PROPERTY_SYSTEMJOB_CREATED)
		  refreshVisual(true);
		if(ev.getPropertyName()==ObjectModel.PROPERTY_SYSTEMJOB_DELETED)
		  refreshVisual(true);
	}
}