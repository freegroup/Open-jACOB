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
 * Created on Oct 18, 2004
 *
 */
package de.tif.jacob.designer.editor.i18n;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import de.tif.jacob.designer.model.JacobModel;

public final class I18NEditorInput implements IEditorInput, IPersistableElement
{
  JacobModel  jacobModel ;
  
  public I18NEditorInput( JacobModel jacobModel)
  {
    this.jacobModel=jacobModel;
  }

  public boolean exists()
  {
    return true;
  }

  public ImageDescriptor getImageDescriptor()
  {
    return null;
  }

  public String getName()
  {
    return jacobModel.getName();
  }

  public IPersistableElement getPersistable()
  {
    return null;
  }

  public String getToolTipText()
  {
    return "I18N Editor";
  }

  public Object getAdapter(Class adapter)
  {
    return null;
  }

  public String getFactoryId()
  {
    return null;
  }

  public void saveState(IMemento memento)
  {
//    memento.putString(MEMENTO_KEY, getId());
  }


  public final JacobModel getJacobModel()
  {
    return jacobModel;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj)
  {
    if (obj instanceof I18NEditorInput)
    {
    	I18NEditorInput other = (I18NEditorInput) obj;
      return this.jacobModel.equals(other.jacobModel);
    }
    
    return false;
  }
}