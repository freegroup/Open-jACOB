package de.tif.jacob.docgen.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class CreateWikiTableDocumentationAction implements IObjectActionDelegate 
{

	/**
	 * Constructor for Action1.
	 */
	public CreateWikiTableDocumentationAction() 
  {
		super();
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
	public void run(IAction action) 
  {
    try
    {
      new GenerateWikiTableDocumentJob().schedule();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) 
  {
	}

}
