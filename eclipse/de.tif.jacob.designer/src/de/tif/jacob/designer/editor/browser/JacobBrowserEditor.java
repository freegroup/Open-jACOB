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
package de.tif.jacob.designer.editor.browser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.ObjectModel;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class JacobBrowserEditor extends FormEditor implements PropertyChangeListener,  ISelectionProvider
{
  public static final String ID = "de.tif.jacob.designer.editor.browser.browsereditor";
  
  List listeners = new ArrayList();
  ISelection theSelection = StructuredSelection.EMPTY;

  private BrowserModel          browser;
  
  /**
   *  
   */
  public JacobBrowserEditor()
  {
  }

	public void propertyChange(PropertyChangeEvent ev)
	{
	  if(ev.getPropertyName()==ObjectModel.PROPERTY_BROWSER_DELETED && ev.getOldValue()==browser)
	    close(false);
		else if(ev.getPropertyName()==ObjectModel.PROPERTY_JACOBMODEL_CLOSED)
	    close(false);
		else if(ev.getSource() == browser)
		{
		  setTitleImage(browser.getImage());
		  setPartName(browser.getName());
		}
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
      addPage( new BrowserPropertyPage(this));
      addPage( new ColumnsPropertyPage(this));

      setActivePage(1);
    }
    catch (PartInitException e)
    {
      JacobDesigner.showException(e);
    }
  }


  /* (non-Javadoc)
   * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
   */
  public void init(IEditorSite site, IEditorInput input) throws PartInitException
  {
    super.init(site, input);
    
    browser =((JacobBrowserEditorInput) input).getBrowserModel();
    setPartName( browser.getName());
    setTitleImage(browser.getImage());
    browser.getJacobModel().addPropertyChangeListener(this);

    theSelection = new StructuredSelection(browser);
    // we're cooperative and also provide our selection
    // at least for the tableviewer
    this.getSite().setSelectionProvider(this);
    
  }
  


  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.ISaveablePart#doSaveAs()
   */
  public void doSaveAs()
  {
  }

  public boolean isDirty()
  {
    return false;
  }

  public boolean isSaveAsAllowed()
  {
    return false;
  }
  
  public void doSave(IProgressMonitor monitor)
  {
    JacobDesigner.getPlugin().saveCurrentModel();
  }
 
  
  public void dispose()
  {
    browser.getJacobModel().removePropertyChangeListener(this);
    super.dispose();
  }
  
  public void addSelectionChangedListener(ISelectionChangedListener listener) 
  {
    this.listeners.add(listener);
  }

  public ISelection getSelection() 
  {
    return this.theSelection;
  }

  public void removeSelectionChangedListener(ISelectionChangedListener listener) 
  {
    this.listeners.remove(listener);
  }

  public void setSelection(ISelection selection) 
  { 
    this.theSelection = selection;    
    final SelectionChangedEvent e = new SelectionChangedEvent(this, selection);
    Object[] listenersArray = this.listeners.toArray();
    
    for (Object element : listenersArray)
    {
        final ISelectionChangedListener l = (ISelectionChangedListener) element;
        Platform.run(new SafeRunnable() 
        {
            public void run() 
            {
                l.selectionChanged(e);
            }
        });
    }
  }
 }