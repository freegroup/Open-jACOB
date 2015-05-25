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
package de.tif.jacob.designer.views.search.tree;

import java.util.ArrayList;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertySource;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.IOpenable;
import de.tif.jacob.designer.model.ObjectModel;


public class TreeObject implements IAdaptable, DblClickObject
{
  private final TreeObject parent;
  private final TreeViewer viewer;
  private final ObjectModel model;
  private final ArrayList  children;
  private final String label;
  private final Image  image;
  
  public TreeObject(TreeViewer viewer,TreeObject parent, ObjectModel ref) 
	{
		this.viewer     = viewer;
    this.parent     = parent;
    this.children   = new ArrayList();
    this.model      = ref;
    this.label      = ref.getExtendedDescriptionLabel();
    this.image      = ref.getImage();
	}
  
  public TreeObject(TreeViewer viewer,TreeObject parent, String label,Image image)
  {
    this.viewer     = viewer;
    this.parent     = parent;
    this.children   = new ArrayList();
    this.model      = null;
    this.label      = label;
    this.image      = image;
  }

  public String getLabel()
	{
	  return label;
	}
	
	public TreeObject getParent() 
	{
		return parent;
	}
	
	public String toString() 
	{
		return getLabel();
	}
	
	/* 
   * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
   */
  public Object getAdapter(Class key)
  {
    // Provider for the property view
    //
    if(IPropertySource.class.equals(key) )
      return model;
    
    return null;
  }

  public void dblClick()
  {
    if(model instanceof IOpenable)
      ((IOpenable)model).openEditor();
  }  
  
  public void addChild(TreeObject child)
  {
    children.add(child);
    this.viewer.refresh(this,true);
  }

  public TreeObject[] getChildren()
  {
    return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
  }
  
  public boolean hasChildren()
  {
    return children.size() > 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#getImage()
   */
  public Image getImage()
  {
    return this.image;
  }
 }