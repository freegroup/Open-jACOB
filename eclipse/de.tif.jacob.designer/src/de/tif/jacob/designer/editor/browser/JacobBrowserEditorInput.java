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
 * Created on 04.11.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.editor.browser;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.TreeSelectionObjectModelProvider;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class JacobBrowserEditorInput implements IEditorInput, IPersistableElement, TreeSelectionObjectModelProvider
{
  private final BrowserModel browserModel;
  
  public JacobBrowserEditorInput(BrowserModel browser)
  {
    this.browserModel = browser;
  }

  /**
   * Liefert das Objekt Model zurück welches im ApplicationOutline selektiert werden
   * soll, wenn dieses Element selektiert wird (sync-Mode)
   */
  public ObjectModel getTreeObjectModel()
  {
    return browserModel;
  }

  
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IEditorInput#exists()
   */
  public boolean exists()
  {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
   */
  public ImageDescriptor getImageDescriptor()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IEditorInput#getName()
   */
  public String getName()
  {
    return browserModel.getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IEditorInput#getPersistable()
   */
  public IPersistableElement getPersistable()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IEditorInput#getToolTipText()
   */
  public String getToolTipText()
  {
    return "Data browser [" + browserModel.getName()+"] based on Table ["+browserModel.getTableAliasModel().getName()+"]";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IPersistableElement#getFactoryId()
   */
  public String getFactoryId()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IPersistableElement#saveState(org.eclipse.ui.IMemento)
   */
  public void saveState(IMemento memento)
  {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
   */
  public Object getAdapter(Class adapter)
  {
    return null;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj)
  {
    if (obj instanceof JacobBrowserEditorInput)
    {
      JacobBrowserEditorInput other = (JacobBrowserEditorInput) obj;
      return browserModel.equals(other.browserModel);
    }
    
    return false;
  }
  
  public BrowserModel getBrowserModel()
  {
    return browserModel;
  }
}
