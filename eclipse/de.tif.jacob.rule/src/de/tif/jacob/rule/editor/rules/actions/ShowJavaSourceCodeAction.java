/*
 * Created on 12.01.2005
 *
 */
package de.tif.jacob.rule.editor.rules.actions;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.rule.RulePlugin;
import de.tif.jacob.rule.editor.rules.editpart.RuleModelEditPart;

/**
 *
 */
public class ShowJavaSourceCodeAction implements IObjectActionDelegate
{

  String model ;
  /* 
   * @see de.tif.jacob.designer.actions.ShowObjectModelHookAction#getObjectModel()
   */
  public String getClassName()
  {
    return model;
  }

	public void selectionChanged(IAction action, ISelection selection) 
	{
    if(((IStructuredSelection)selection).getFirstElement()!=null)
    {
      RuleModelEditPart editPart = (RuleModelEditPart)((IStructuredSelection)selection).getFirstElement();
      model = editPart.getRuleModel().getImplementationClass();
    }
    else
      model=null;
	}
  /**
   * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
   */
  public void setActivePart(IAction action, IWorkbenchPart targetPart) 
  {
  }


  /**
   * @see IActionDelegate#run(IAction)
   */
  public final void run(IAction action)
  {
    try
    {
      String model=getClassName();
      if (model != null)
      {
        IJavaProject myJavaProject = JavaCore.create(RulePlugin.getDefault().getSelectedProject());
        IType type = myJavaProject.findType(model);
        if (type != null)
        {
          IJavaElement element = JavaCore.create(type.getResource());
          JavaUI.openInEditor(element);
        }
      }
    }
    catch (Exception e)
    {
      RulePlugin.showException(e);
    }
  }
}
