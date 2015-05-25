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
package de.tif.jacob.designer.editor.jacobform;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.TreeSelectionObjectModelProvider;
import de.tif.jacob.designer.model.UIJacobFormModel;

public final class JacobFormEditorInput implements IEditorInput, IPersistableElement, TreeSelectionObjectModelProvider
{
  UIJacobFormModel  form ;
  
  public JacobFormEditorInput( UIJacobFormModel form)
  {
    this.form=form;
  }

  /**
   * Liefert das Objekt Model zurück welches im ApplicationOutline selektiert werden
   * soll, wenn dieses Element selektiert wird (sync-Mode)
   */
  public ObjectModel getTreeObjectModel()
  {
    return form;
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
    return form.getName();
  }

  public IPersistableElement getPersistable()
  {
    return null;
  }

  public String getToolTipText()
  {
    return "Form Editor";
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


  public final UIJacobFormModel getFormModel()
  {
    return form;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj)
  {
    if (obj instanceof JacobFormEditorInput)
    {
    	JacobFormEditorInput other = (JacobFormEditorInput) obj;
      return this.form.equals(other.form);
    }
    
    return false;
  }
}