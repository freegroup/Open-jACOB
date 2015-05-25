/*
 * Created on 28.06.2007
 *
 */
package de.tif.jacob.designer.views.search.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.search.ui.ISearchResultViewPart;
import org.eclipse.search2.internal.ui.SearchView;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.views.search.ReferenceSearchResultPage;

public class ExpandTreeObjectChildren  implements IObjectActionDelegate
{
  Object node;
  ReferenceSearchResultPage view;
  
  /**
   * 
   * 
   * @param action 
   * @param targetPart 
   */
  public void setActivePart(IAction action, IWorkbenchPart targetPart) 
  {
    view =(ReferenceSearchResultPage) ((ISearchResultViewPart)targetPart).getActivePage();
  }

  /**
   * 
   * 
   * @param action 
   * @param selection 
   */
  public void selectionChanged(IAction action, ISelection selection) 
  {
    if(((IStructuredSelection)selection).getFirstElement()!=null)
      node =((IStructuredSelection)selection).getFirstElement();
    else
      node=null;
  }

  /**
   * 
   * 
   * @param action 
   */
  public final void run(IAction action)
  {
    if (node != null)
    {
      System.out.println(node.getClass().getName());
      view.getViewer().expandToLevel(node,AbstractTreeViewer.ALL_LEVELS);
  //    this.alias.getJacobModel().removeElement(alias);
    }
  }
}
