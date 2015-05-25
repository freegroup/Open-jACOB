package de.tif.jacob.blooper.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import de.tif.jacob.blooper.BlooperPlugin;
import de.tif.jacob.blooper.editors.bug.BugEditor;
import de.tif.jacob.blooper.editors.bug.BugEditorInput;
import de.tif.jacob.blooper.model.Bug;

public abstract class ShowDefectEditorAction implements IObjectActionDelegate 
{
  public abstract Bug getBug() throws Exception;
  
	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) 
	{
	}

	public void selectionChanged(IAction action, ISelection selection) 
	{
	}
	
	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public final void run(IAction action)
  {
    try
    {
      Bug defect = getBug();
    	if(defect==null)
    	{
    		throw new NullPointerException("No object [Bug] found");
    	}
      IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
      IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
      BugEditorInput editorInput = new BugEditorInput( defect);
      
      page.openEditor(editorInput, BugEditor.ID, true);
    }
    catch (Exception e)
    {
      BlooperPlugin.showException(e);
    }
  }
}
