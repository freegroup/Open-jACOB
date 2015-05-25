package de.tif.jacob.module.email.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import de.tif.jacob.core.Version;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.UIDomainModel;
import de.tif.jacob.designer.model.UIExternalFormModel;
import de.tif.jacob.designer.model.UIHtmlFormModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.designer.views.applicationoutline.TreeDomainObject;
import de.tif.jacob.module.email.Activator;
import de.tif.jacob.module.popup.actions.AbstractModuleAction;

public class CreateEMailFormAction extends AbstractModuleAction
{
  UIDomainModel domain=null;

  /**
	 * Constructor for Action1.
	 */
	public CreateEMailFormAction() 
  {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) 
  {
	}

  public Version getRequiredJACOBVersion()
  {
    return new Version(2,8);
  }

  public String readTemplateFile(String filename)
  {
    // redirect the call the PluginOwned Activator!!!
    return Activator.readTemplateFile(filename);
  }

  public String getModuleTypeName()
  {
    return "EMAIL";
  }

  public String getModulePrefix()
  {
    return "email";
  }

  /**
   * @see IActionDelegate#run(IAction)
   */
  public String getPluginId() 
  {
    return Activator.PLUGIN_ID;
  }

  /**
   * @see IActionDelegate#selectionChanged(IAction, ISelection)
   */
  public void selectionChanged(IAction action, ISelection selection) 
  {
    if(((IStructuredSelection)selection).getFirstElement()!=null)
      domain =((TreeDomainObject)((IStructuredSelection)selection).getFirstElement()).getDomainModel();
    else
      domain=null;
  }

  public void modifyTargetModel(JacobModel sourceModel, JacobModel targetModel)
  {
    // Alle formen übertragen und diese dann auch an die Domain hängen
    //
    for(UIHtmlFormModel form : sourceModel.getHtmlFormModels())
    {
      form.setJacobModel(targetModel);
      targetModel.addElement(form);
      domain.addElement(form);
    }
    for(UIExternalFormModel form : sourceModel.getExternalFormModels())
    {
      form.setJacobModel(targetModel);
      targetModel.addElement(form);
      domain.addElement(form);
    }
    for(UIJacobFormModel form : sourceModel.getJacobFormModels())
    {
      form.setJacobModel(targetModel);
      targetModel.addElement(form);
      domain.addElement(form);
    }
  }
}
