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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertySource;

import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.util.StringUtil;


public abstract class TreeObject implements IAdaptable,PropertyChangeListener
{
	private String name; // Das Label welches von dem TreeElement angezeigt wird.
	
  private final TreeParent parent;
	final TreeViewer viewer;

	protected final   ObjectModel model;
  protected final   JacobModel jacobModel;
	

	protected String error  = null;
	protected String warning= null;
	protected String info   = null;
  protected boolean hook  = false;
	
  String oldError;
  String oldWarning;
  String oldInfo;

  public abstract boolean hasDirectEdit();
  public abstract Image   getImage();

  public TreeObject(TreeViewer viewer,TreeParent parent, JacobModel jacob, ObjectModel model, String name) 
	{
		this.name       = name;
		this.model      = model;
		this.jacobModel = jacob;
		this.viewer     = viewer;
    this.parent     = parent;
    this.error      = model.getError();
    this.warning    = model.getWarning();
    this.info       = model.getInfo();
    this.hook       = model.getHookClassName()!=null;
    
    model.addPropertyChangeListener(this);
	}
	
  public void dispose()
  {
    model.removePropertyChangeListener(this);
  }
  
  public final ObjectModel getModel()
	{
	  return model;
	}
	
	public void refreshVisual()
	{
	  oldError   = error;
	  oldWarning = warning;
	  oldInfo    = info;
    
    error   = model.getError();
    warning = model.getWarning();
    info    = model.getInfo();
    hook    = model.getHookClassName()!=null;
    
    //viewer.refresh(this);
    viewer.update(this,null);
	  
    // Wenn sich der error status nicht geändert hat, dann braucht man den parent
    // auch nicht aktualisieren
    //
    if(StringUtil.equals(oldError,error) && StringUtil.equals(oldWarning,warning)&&StringUtil.equals(oldInfo,info))
      return;

    
	  if(parent!=null)
	    parent.refreshVisual();
	}
	
	
	public boolean sortingEnabled()
	{
	  return false;
	}
	
	public int getSortingCategory()
	{
	  return 0;
	}
  
  public String getSortingString()
  {
    // by default sorting should be done by comparing the labels
    // Nevertheless, this could be overwritten, e.g. for marking an entry with ">testrelationset"
    return getLabel();
  }
	
	public String getName()
	{
		return name;
	}
	
	public String getLabel()
	{
	  return getName();
	}
	
	public String getTooltip()
	{
	  if(error!=null)
	    return error;
	  if(warning!=null)
	    return warning;
	  if(info!=null)
	    return info;
    if(hook)
      return "Java code attached to ["+getName()+"]";
    
	  return null;
	}
	
	public void setName(String name) throws Exception
	{
	  // change the model attribute which matches to the Label of the tree element
    this.name = name;
	}

	public TreeParent getParent() 
	{
		return parent;
	}
	
	public String toString() 
	{
		return getName();
	}
	
	public void dragStart(DragSourceEvent event)
	{
	  // by default drag is disabled
	  event.doit = false;
	}

  
  public final JacobModel getJacobModel()
  {
    return jacobModel;
  }
  
  public void propertyChange(PropertyChangeEvent ev)
  {
    if(ev.getPropertyName()==ObjectModel.PROPERTY_NAME_CHANGED)
      this.name = (String)ev.getNewValue();
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
  
}