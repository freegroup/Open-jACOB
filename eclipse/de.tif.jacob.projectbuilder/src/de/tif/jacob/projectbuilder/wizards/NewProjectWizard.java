package de.tif.jacob.projectbuilder.wizards;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.perspective.Programming;
import de.tif.jacob.projectbuilder.JacobProject;

/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "txt". If a
 * sample multi-page editor (also available as a template) is registered for the
 * same extension, it will be able to open it.
 */
public abstract class NewProjectWizard extends Wizard implements INewWizard
{
  private NewProjectWizardPage page;

  public abstract String getPluginId();
  public abstract String getProjectDefaultName();

  /**
   * Constructor for NewProjectWizard.
   */
  public NewProjectWizard() 
  {
    super();
    setNeedsProgressMonitor(true);
  }

  /**
   * Adding the page to the wizard.
   */
  public void addPages()
  {
    page = new NewProjectWizardPage(getProjectDefaultName());
    addPage(page);
  }

  /**
   * This method is called when 'Finish' button is pressed in the wizard. We
   * will create an operation and run it using wizard as execution context.
   */
  public boolean performFinish()
  {
    final String fileName = page.getFileName();
    final JacobProject project = new JacobProject(getPluginId(), fileName);
    
    IRunnableWithProgress op = new IRunnableWithProgress()
    {
      public void run(IProgressMonitor monitor) throws InvocationTargetException
      {
        try
        {
          project.create(monitor);
        }
        catch (CoreException e)
        {
          throw new InvocationTargetException(e);
        }
        finally
        {
          monitor.done();
        }
      }
    };
    try
    {
      getContainer().run(true, false, op);
    }
    catch (InterruptedException e)
    {
      return false;
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
      Throwable realException = e.getTargetException();
      MessageDialog.openError(getShell(), "Error", realException.getMessage());
      return false;
    }
    // actuelles model laden
    //
    JacobDesigner.getPlugin().setModel(project.getProject());
    
    // jACOB Programming perspective anzeigen
    //
    Programming.open();
    
    // README �ffnen falls vorhanden
    //
    IFile file1 = (IFile)project.getProject().findMember("doc/readme.txt");
                
    if(file1!=null && file1.exists())
    {
      IWorkbench workbench = PlatformUI.getWorkbench();
      IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
      IWorkbenchPage page = workbenchWindow.getActivePage();
      try {
          page.openEditor(new FileEditorInput(file1), workbench.getEditorRegistry().getDefaultEditor(
                                      file1.getFullPath().toString()).getId());
      } catch (PartInitException exception) {
      }
    }
    return true;
  }

  /**
   * We will accept the selection in the workbench to see if we can initialize
   * from it.
   * 
   * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
   */
  public void init(IWorkbench workbench, IStructuredSelection selection)
  {
  }
}