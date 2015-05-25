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
package de.tif.jacob.designer.editor.i18n;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorPart;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
/**
 * Multi-page editor for editing resource bundles.
 */
public class I18NEditor extends MultiPageEditorPart implements PropertyChangeListener 
{
  public static  final String ID             = "de.tif.jacob.designer.editor.i18n.I18NEditor";
  private static final Image  BUNDLE_IMAGE   = JacobDesigner.getImage("resourcebundle.gif");

  private JacobModel jacobModel;
  private I18NPage i18nPage;


  /**
   * Creates a multi-page editor example.
   */
  public I18NEditor()
  {
    super();
  }

  /**
   * Creates the pages of the multi-page editor.
   */
  protected void createPages()
  {
    // Create I18N page
    i18nPage = new I18NPage(getContainer(), SWT.H_SCROLL | SWT.V_SCROLL, jacobModel);
    int index = addPage(i18nPage);
    setPageText(index, JacobDesigner.getResourceString("editor.properties"));
    setPageImage(index, BUNDLE_IMAGE);
  }


	/* 
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() 
	{
    jacobModel.removePropertyChangeListener(this);
    
		super.dispose();
	}


  /**
   * The <code>MultiPageEditorExample</code> implementation of this method
   * checks that the input is an instance of <code>IFileEditorInput</code>.
   */
  public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException
  {
    jacobModel = ((I18NEditorInput)editorInput).getJacobModel();
    jacobModel.addPropertyChangeListener(this);
    
    setTitleImage(BUNDLE_IMAGE);
    setPartName("Localization Editor");
    super.init(site, editorInput);
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
 
  /**
   * @see org.eclipse.ui.ISaveablePart#doSaveAs()
   */
  public void doSaveAs()
  {
  }



  /**
   * Notifies this multi-page editor that the page with the given id has been
   * activated. This method is called when the user selects a different tab.
   * <p>
   * The <code>MultiPageEditorPart</code> implementation of this method 
   * sets focus to the new page, and notifies the action bar contributor (if there is one).
   * This checks whether the action bar contributor is an instance of 
   * <code>MultiPageEditorActionBarContributor</code>, and, if so,
   * calls <code>setActivePage</code> with the active nested editor.
   * This also fires a selection change event if required.
   * </p>
   * <p>
   * Subclasses may extend this method.
   * </p>
   *
   * @param newPageIndex the index of the activated page 
   */
  protected void pageChange(int newPageIndex)
  {
    super.pageChange(newPageIndex);
    if (newPageIndex == 0)
    {
      i18nPage.refresh();
    }
  }
  
  public void select(String key)
  {
    i18nPage.select(key);
  }
  
	public void propertyChange(PropertyChangeEvent ev)
	{
		if(ev.getPropertyName()==ObjectModel.PROPERTY_JACOBMODEL_CLOSED)
		  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(this, false);
		else if(ev.getPropertyName()==ObjectModel.PROPERTY_I18NBUNDLE_CREATED)
		  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(this, false);
		else if(ev.getPropertyName()==ObjectModel.PROPERTY_I18NBUNDLE_DELETED)
		  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(this, false);
//		else if(ev.getPropertyName()==ObjectModel.PROPERTY_I18NKEY_CREATED)
//		  i18nPage.refresh();
		// Ein Eintrag kann auch in einem FormEditor geändert werden. Dies muss der I18NEditor auch mitbekommen
		// um seine Ansicht zu aktualisieren.
		else if(ev.getPropertyName()==ObjectModel.PROPERTY_I18NBUNDLE_CHANGED)
		  i18nPage.refresh();
//		else if(ev.getPropertyName()==ObjectModel.PROPERTY_I18NKEY_REMOVE)
//		  i18nPage.refresh();
	}
}
