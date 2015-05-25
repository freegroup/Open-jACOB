/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/
package de.tif.jacob.designer.actions.dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import de.tif.jacob.designer.model.JacobModel;
/**
 * 
 */
public class NewScheduledTaskWizard extends Wizard implements INewWizard
{
  /**
   * 
   */
  static class Type
  {
    /**
     * 
     */
    final String templateDir;

    /**
     * 
     * 
     * @param templateDir
     */
    Type(String templateDir)
    {
      this.templateDir = templateDir;
    }

    /**
     * 
     * 
     * @return
     */
    public String toString()
    {
      return templateDir;
    }
  }
  /**
   * 
   */
  public final static Type TYPE_USER = new Type("schedulerTaskUser");
  /**
   * 
   */
  public final static Type TYPE_SYSTEM = new Type("schedulerTaskSystem");
  /**
   * 
   */
  private NamePage namePage;
  /**
   * 
   */
  private TemplatePage templatePage;
  /**
   * 
   */
  private final Type type;
  /**
   * 
   */
  private final JacobModel jacobModel;
  /**
   * 
   */
  String jobName;
  /**
   * 
   */
  String templateName;

  /**
   * 
   * 
   * @param jacobModel
   * @param type
   */
  public NewScheduledTaskWizard(JacobModel jacobModel, Type type)
  {
    super();
    this.jacobModel = jacobModel;
    this.type = type;
  }

  /**
   * 
   */
  public void addPages()
  {
    namePage = new NamePage(jacobModel, type, "DefaultName");
    templatePage = new TemplatePage(jacobModel, type);
    addPage(namePage);
    addPage(templatePage);
  }

  /**
   * 
   * 
   * @return
   */
  public String getJobName()
  {
    return jobName;
  }

  /**
   * 
   * 
   * @return
   */
  public String getTemplateName()
  {
    return templateName;
  }

  /**
   * 
   * 
   * @param workbench
   * @param selection
   */
  public void init(IWorkbench workbench, IStructuredSelection selection)
  {
  }

  /**
   * 
   * 
   * @return
   */
  public boolean canFinish()
  {
    // cannot completr the wizard from the first page
    if (this.namePage.getJobName() == null || namePage.getJobName().length() == 0)
      return false;
    // cannot completr the wizard from the first page
    if (jacobModel.getScheduledJobUserMode(this.namePage.getJobName()) != null)
      return false;
    return true;
  }

  /**
   * 
   * 
   * @return
   */
  public boolean performFinish()
  {
    // cannot completr the wizard from the first page
    if (jacobModel.getScheduledJobUserMode(this.namePage.getJobName()) != null)
      return false;
    jobName = namePage.getJobName();
    templateName = templatePage.getTemplateName();
    return true;
  }
}
