/*
 * Created on 20.08.2009
 *
 */
package de.tif.jacob.refactoring.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.editpart.GroupEditPart;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.refactoring.dialogs.SelectGroupTableAliasDialog;

public class ChangeGroupTableAliasAction implements IObjectActionDelegate
{
  UIGroupModel group;

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
    if (((IStructuredSelection) selection).getFirstElement() != null)
      group = (UIGroupModel) ((GroupEditPart) ((IStructuredSelection) selection).getFirstElement()).getGroupModel();
    else
      group = null;
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
      SelectGroupTableAliasDialog dialog = new SelectGroupTableAliasDialog(group);
      if (dialog.open() == Window.OK)
      {
        TableAliasModel model = (TableAliasModel) dialog.getResult()[0];
        group.setTableAlias(model.getName(), dialog.getRenameChildren());
      }
    }
    catch (Exception exc)
    {
      JacobDesigner.showException(exc);
    }
  }
}