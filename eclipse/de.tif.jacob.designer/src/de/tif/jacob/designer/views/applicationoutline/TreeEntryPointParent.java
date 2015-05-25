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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertySource;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.JacobModel;

public class TreeEntryPointParent extends TreeParent
{
  /**
   * @param name
   */
  public TreeEntryPointParent(TreeViewer viewer, TreeParent parent, JacobModel jacob) 
  {
    super(viewer, parent,jacob,jacob, "Entry Points");
  } 
  
  
  public void addChildren()
  {
    addChild(new TreeEntryPointSOAPParent(viewer,this, jacobModel));
    addChild(new TreeEntryPointGUIParent(viewer, this,  jacobModel));
    addChild(new TreeEntryPointCMDParent(viewer, this, jacobModel));
  }
}
