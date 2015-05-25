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

import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.clazz.ClassUtil;

public class TreeParent extends TreeObject
{
  private ArrayList children;
  public TreeParent(TreeViewer viewer,TreeParent parent, JacobModel jacob, ObjectModel model, String name) 
  {
    super(viewer, parent, jacob, model, name);
    this.children   = new ArrayList();
    addChildren();
    updateErrorState();
  }
  
  public void addChildren()
  {
  }
  
	public void refreshVisual(boolean refreshChildrenToo )
	{
	  if(refreshChildrenToo)
	  {
	    TreeObject[] children = getChildren();
	    for(int i=0 ;i<children.length;i++)
	    {
	      //children[i].dispose();
	      removeChild(children[i]);
	    }
	    addChildren();
	  }
	  refreshVisual();
	}
  
	private void updateErrorState()
	{
    oldError   = error;
    oldWarning = warning;
    oldInfo    = info;
	  // Falls ein Kind ein Fehler hat, wird dieser auch angezeigt
	  //
	  error   = model.getError();
	  warning = model.getWarning();
	  info    = model.getInfo();
	  
	  Iterator iter = children.iterator();
    while (iter.hasNext())
    {
      TreeObject obj = (TreeObject) iter.next();
      if(obj.error!=null && this.error==null)
        this.error = obj.error;
      if(obj.warning!=null && this.warning==null)
        this.warning = obj.warning;
    }
	}

	public void refreshVisual()
	{
//	  super.refreshVisual();
	  updateErrorState();
	  viewer.refresh(this,true);

    // Wenn sich der error status nicht geändert hat, dann braucht man den parent
    // auch nicht aktualisieren
    //
    if(StringUtil.equals(oldError,error) && StringUtil.equals(oldWarning,warning)&&StringUtil.equals(oldInfo,info))
      return;
    
	  if(getParent()!=null)
	    getParent().refreshVisual();
	}
 
  public void dispose()
  {
    Iterator iter = children.iterator();
    while (iter.hasNext())
    {
      TreeObject obj = (TreeObject) iter.next();
      obj.dispose();
    }
    super.dispose();
  }

  public void addChild(TreeObject child)
  {
    children.add(child);
  }

  public void removeChild(TreeObject child)
  {
    children.remove(child);
    child.dispose();
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
    String name = model.getImageName();
    
    if(error!=null)
      return JacobDesigner.getImage(name,JacobDesigner.DECORATION_ERROR);
    if(warning!=null)
      return JacobDesigner.getImage(name,JacobDesigner.DECORATION_WARNING);
    if(info!=null)
      return JacobDesigner.getImage(name,JacobDesigner.DECORATION_INFO);
    
    return JacobDesigner.getImage(name,JacobDesigner.DECORATION_NONE);
  }
  
	/**
	 * No direct edit in the tree
	 */
	public boolean hasDirectEdit()
	{
	  return false;
	}
}