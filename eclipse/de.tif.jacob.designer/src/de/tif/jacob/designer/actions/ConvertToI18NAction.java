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
package de.tif.jacob.designer.actions;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.i18n.I18NEditor;
import de.tif.jacob.designer.editor.i18n.I18NEditorInput;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.UICaptionModel;
import de.tif.jacob.designer.preferences.I18NPreferences;
import de.tif.jacob.util.StringUtil;

/**
 * 
 */
public abstract class ConvertToI18NAction implements IObjectActionDelegate
{
  
  /**
   * 
   * 
   * @return 
   */
  public abstract JacobModel getJacobModel();

  /**
   * 
   * 
   * @return 
   */
  public abstract String getKeySuggestion();

  /**
   * 
   * 
   * @return 
   */
  public abstract String getCurrentValue();

  /**
   * 
   * 
   * @param value 
   */
  public abstract void setCaption(String value);

  /**
   * 
   * 
   * @return 
   */
  public boolean openEditor()
  {
    return true;
  }

  /**
   * 
   * 
   * @param action 
   * @param targetPart 
   */
  public void setActivePart(IAction action, IWorkbenchPart targetPart)
  {
  }

  /**
   * 
   * 
   * @param action 
   * @param selection 
   */
  public void selectionChanged(IAction action, ISelection selection)
  {
  }

  /**
   * 
   * 
   * @param action 
   */
  public final void run(IAction action)
  {
    try
    {
      String key = getKeySuggestion();
      // in dem key kein Leerzeichen/Komma.....vorhanden sein. Diese werden alle
      // ersetz oder gestrichen
      //
      String groupSep = getJacobModel().getSeparator();
      key = StringUtil.replace(key, " ", groupSep);
      String newKey = "";
      for (int i = 0; i < key.length(); i++)
      {
        char c = key.charAt(i);
        if (Character.isDigit(c) || Character.isLetterOrDigit(c) || ("" + c).equals(groupSep))
          newKey = newKey + c;
      }
      key = newKey;
      Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
      InputDialog dialog = new InputDialog(shell, "Create a new I18N entry", "Suggestion for the new resource bundle (I18N) key", key, new IInputValidator()
      {
        public String isValid(String newText)
        {
          if (!StringUtils.containsOnly(newText, "abcdefghijklmnopqrstuvwxyzABCDEFGHJIKLMNOPQRSTUVWXYZ_.1234567890".toCharArray()))
            return "Invalid character in key.";
          return null;
        }
      });
      dialog.create();
      if (dialog.open() == Window.OK && dialog.getValue() != null)
      {
        key = dialog.getValue();
        // Falls es den key nicht gibt, wird dieser kurzerhand angelegt
        if (getJacobModel().hasI18NKey(key) == false)
          getJacobModel().addI18N(key, getCurrentValue());
        setCaption("%" + key);
        if(openEditor()==true)
        {
	        IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	        IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
	        I18NEditorInput editorInput = new I18NEditorInput(getJacobModel());
	        I18NEditor editor = (I18NEditor) page.openEditor(editorInput, I18NEditor.ID, true);
	        editor.select(key);
        }
      }
    }
    catch (PartInitException e)
    {
      JacobDesigner.showException(e);
    }
  }
}
