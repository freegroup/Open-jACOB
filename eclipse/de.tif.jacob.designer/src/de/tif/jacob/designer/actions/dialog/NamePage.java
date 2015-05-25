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

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import de.tif.jacob.designer.model.JacobModel;

/**
 * 
 */

public class NamePage extends WizardPage
{
  
  /**
   * 
   */
  private Text jobNameText;
  
  /**
   * 
   */
  private String defaultName;
  
  /**
   * 
   */
  private JacobModel jacobModel;
  
  /**
   * 
   */
  private NewScheduledTaskWizard.Type type;
  
  /**
   * 
   * 
   * @param jacobModel 
   * @param type 
   * @param defaultName 
   */
  public NamePage(JacobModel jacobModel , NewScheduledTaskWizard.Type type ,String defaultName) 
  {
    super("wizardPage");
    
    this.defaultName = defaultName;
    this.jacobModel  = jacobModel;
    this.type        = type;
    
    setTitle("Scheduler job creation");
    setDescription("This wizard creates a scheduled job from a selected template.");
  }

  /**
   * 
   * 
   * @param parent 
   */
  public void createControl(Composite parent)
  {
    Composite container = new Composite(parent, SWT.NULL);
    GridLayout layout = new GridLayout();
    container.setLayout(layout);
    layout.numColumns = 3;
    layout.verticalSpacing = 9;
    Label label = new Label(container, SWT.NULL);
    label.setText("&Job name:");

    jobNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    jobNameText.setLayoutData(gd);
    jobNameText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        dialogChanged();
      }
    });
    initialize();
    dialogChanged();
    setControl(container);
  }

  /**
   * 
   */
  private void initialize()
  {
    jobNameText.setText(defaultName);
  }

  /**
   * 
   */

  private void dialogChanged()
  {
    String fileName = getJobName();

    if (fileName.length() == 0)
    {
      updateStatus("Job name must be specified");
      return;
    }
    if(!StringUtils.containsOnly(fileName,"abcdefghijklmnopqrstuvwxyzABCDEFGHJIKLMNOPQRSTUVWXYZ_".toCharArray()))
    {
      updateStatus("Job name can only contain [a-zA-Z_] as character.");
      return;
    }
    if(type == NewScheduledTaskWizard.TYPE_USER && jacobModel.getScheduledJobUserMode(fileName)!=null)
    {
      updateStatus("Job with this name already exists. Please use another one");
      return;
    }
    if(type == NewScheduledTaskWizard.TYPE_SYSTEM && jacobModel.getScheduledJobSystemMode(fileName)!=null)
    {
      updateStatus("Job with this name already exists. Please use another one");
      return;
    }
    updateStatus(null);
  }

  /**
   * 
   * 
   * @param message 
   */
  private void updateStatus(String message)
  {
    setErrorMessage(message);
    setPageComplete(message == null);
  }

  /**
   * 
   * 
   * @return 
   */
  public String getJobName()
  {
    return jobNameText.getText();
  }
}
