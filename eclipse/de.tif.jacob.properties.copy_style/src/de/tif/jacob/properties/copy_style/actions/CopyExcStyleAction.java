/*
 * Created on 28.02.2010
 *
 */
package de.tif.jacob.properties.copy_style.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import de.tif.jacob.designer.model.IStyleProvider;
import de.tif.jacob.properties.copy_style.Activator;

/**
 * 
 */
public class CopyExcStyleAction implements IObjectActionDelegate 
{
  IStyleProvider obj;
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

    if(selection instanceof IStructuredSelection)
    {
      IStructuredSelection s = (IStructuredSelection)selection;
      obj = (IStyleProvider)s.getFirstElement();
    }
  }
  
  /**
   * 
   * 
   * @param action 
   */
  public final void run(IAction action)
  {
    if(obj!=null)
    {
     // PlatformUI.getWorkbench(). getActiveWorkbenchWindow().
      Activator.styleMap.clear();
      obj.provideStyle(Activator.styleMap, true);
    }
  }
}