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
 * Created on 08.11.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.editor.selectionaction;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.ISelectionActionProvider;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIGroupModel;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class SelectionActionEditor extends FormEditor implements PropertyChangeListener
{
  public static final String ID = "de.tif.jacob.designer.editor.selectionaction.SelectionActionEditor";
  
  private ISelectionActionProvider actionProvider;
  
  /**
   *  
   */
  public SelectionActionEditor()
  {
  }

	public void propertyChange(PropertyChangeEvent ev)
	{
	  if(ev.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_REMOVED && ev.getOldValue()==actionProvider)
	    close(false);
		else if(ev.getPropertyName()==ObjectModel.PROPERTY_JACOBMODEL_CLOSED)
	    close(false);
		else if(ev.getSource() == actionProvider)
		  setPartName(actionProvider.getName());
	}
	
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.editor.FormEditor#createToolkit(org.eclipse.swt.widgets.Display)
   */
  protected FormToolkit createToolkit(Display display)
  {
    // Create a toolkit that shares colors between editors.
    return new FormToolkit(JacobDesigner.getPlugin().getFormColors(display));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
   */
  protected void addPages()
  {
    try
    {
      addPage( new ActionPropertiesPage(this));
    }
    catch (PartInitException e)
    {
      e.printStackTrace();
    }
  }


  /* (non-Javadoc)
   * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
   */
  public void init(IEditorSite site, IEditorInput input) throws PartInitException
  {
    super.init(site, input);
    
    actionProvider =((EditorInput) input).getActionProvider();
    setPartName( actionProvider.getName());
    actionProvider.getJacobModel().addPropertyChangeListener(this);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
   */
  public void doSave(IProgressMonitor monitor)
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.ISaveablePart#doSaveAs()
   */
  public void doSaveAs()
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
   */
  public boolean isSaveAsAllowed()
  {
    return false;
  }
  
  public void dispose()
  {
    actionProvider.getJacobModel().removePropertyChangeListener(this);
    super.dispose();
  }
}