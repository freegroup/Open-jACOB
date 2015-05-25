package de.tif.jacob.blooper.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import de.tif.jacob.blooper.BlooperPlugin;
import de.tif.jacob.blooper.dialogs.ConnectBugtrackerWizard;
import de.tif.jacob.blooper.views.BugView;

public class ConnectToBugtracker implements IObjectActionDelegate 
{
  IProject project = null;
  
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
      if(isConnected(project))
      {
        project.setPersistentProperty(BlooperPlugin.PROPERTY_CONNECTED,null);
        project.setPersistentProperty(BlooperPlugin.PROPERTY_APPLICATION,null);
        project.setPersistentProperty(BlooperPlugin.PROPERTY_PASSWORD,null);
        project.setPersistentProperty(BlooperPlugin.PROPERTY_URL,null);
        project.setPersistentProperty(BlooperPlugin.PROPERTY_USER,null);
        BugView view =(BugView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(BugView.ID);
        view.updateView();
      }
      else
      {
        Wizard wizard = new ConnectBugtrackerWizard(project);
        // Instantiates the wizard container with the wizard and opens it
        WizardDialog d = new WizardDialog(null, wizard);
        d.setMinimumPageSize(650,350);
        d.create();
        if(d.open()==Window.OK)
        {
          project.setPersistentProperty(BlooperPlugin.PROPERTY_CONNECTED,"true");
          PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(BugView.ID);
        }
      }
    }
    catch(Exception exc)
    {
      BlooperPlugin.showException(exc);
    }
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) 
  {
    if(selection instanceof IStructuredSelection)
    {
      IStructuredSelection s = (IStructuredSelection)selection;
      if(s.size()!=1)
      {
        action.setEnabled(false);
        return;
      }
      
      Object obj = (Object) s.getFirstElement();
      if(obj instanceof IProject)
      {
        project = (IProject)obj;
        action.setText(isConnected(project)?"Disconnect from Bugtracker":"Connect to Bucktracker");
      }
    }
	}


  private static boolean isConnected(IProject project)
  {
    try
    {
      if(project==null)
        return false;
      String versionNumber = project.getPersistentProperty(BlooperPlugin.PROPERTY_CONNECTED);
      if("true".equals(versionNumber))
      {
        return true;
      }
    }
    catch (CoreException e)
    {
    }
    return false;
  }
}
