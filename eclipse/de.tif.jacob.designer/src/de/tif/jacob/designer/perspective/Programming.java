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
 * Created on Aug 25, 2004
 *
 */
package de.tif.jacob.designer.perspective;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 *
 */
public class Programming implements IPerspectiveFactory 
{
  public static final String ID="de.tif.jacob.designer.perspective.Programming";
  
  /* 
   * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
   */
  public void createInitialLayout(IPageLayout layout) 
  {
    // Editors are placed for free.
    String editorArea = layout.getEditorArea();

    IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, (float) 0.23, editorArea);
    left.addView("de.tif.jacob.designer.views.ApplicationOutlineView");
    
    IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, (float) 0.66, editorArea);
    bottom.addView("org.eclipse.ui.views.PropertySheet");
  }

  public static void open() 
  {
    // Get "Open Behavior" preference.
    AbstractUIPlugin plugin = (AbstractUIPlugin)Platform.getPlugin(PlatformUI.PLUGIN_ID);
    IPreferenceStore store = plugin.getPreferenceStore();
    String pref = store.getString(IWorkbenchPreferenceConstants.OPEN_NEW_PERSPECTIVE);
                    
    // Implement open behavior.
    try {
        if (pref.equals(IWorkbenchPreferenceConstants.OPEN_PERSPECTIVE_WINDOW))
          PlatformUI.getWorkbench().openWorkbenchWindow(ID, ResourcesPlugin.getWorkspace());
        else if (pref.equals(IWorkbenchPreferenceConstants.OPEN_PERSPECTIVE_PAGE))
          PlatformUI.getWorkbench().getActiveWorkbenchWindow().openPage(ID, ResourcesPlugin.getWorkspace());
        else if (pref.equals(IWorkbenchPreferenceConstants.OPEN_PERSPECTIVE_REPLACE)) 
        {
                IPerspectiveRegistry reg = PlatformUI.getWorkbench().getPerspectiveRegistry();
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().setPerspective(reg.findPerspectiveWithId(ID));
        }
    } 
    catch (Exception e) 
    {
           // can happen if no workbench are available at startup or shutdown time.
    }
}
}
