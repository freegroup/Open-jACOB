/*
 * Created on 28.02.2010
 *
 */
package de.tif.jacob.properties.copy_style.actions;

import java.util.ArrayList;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.exolab.castor.util.List;

import de.tif.jacob.designer.model.IStyleProvider;
import de.tif.jacob.properties.copy_style.Activator;

/**
 * 
 */
public class ConsumeExcStyleAction implements IObjectActionDelegate 
{
  java.util.List<IStyleProvider> objs;
  
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
      objs = new ArrayList<IStyleProvider>();
      IStructuredSelection s = (IStructuredSelection)selection;
      for (Object obj : s.toList())
      {
        if(obj instanceof IStyleProvider)
          objs.add((IStyleProvider)obj);
      } 
    }
    else
    {
      objs = null;
    }
  }
  
  /**
   * 
   * 
   * @param action 
   */
  public final void run(IAction action)
  {
    if(objs!=null)
    {
      for (IStyleProvider obj : objs)
      {
         obj.consumeStyle(Activator.styleMap);
      }
    }
  }
}