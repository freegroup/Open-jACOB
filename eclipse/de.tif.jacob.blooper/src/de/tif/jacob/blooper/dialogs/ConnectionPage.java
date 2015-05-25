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
package de.tif.jacob.blooper.dialogs;

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
import de.tif.jacob.blooper.model.BlooperServer;
import de.tif.jacob.blooper.model.SOAPEndPoint;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (txt).
 */

public class ConnectionPage extends WizardPage
{
  Text       serverUrl;
  Text       userName;
  Text       password;
  
  /**
   * Constructor for SampleNewWizardPage.
   * @param wizard 
   * 
   * @param pageName
   */
  public ConnectionPage() 
  {
    super("wizardPage");
    setTitle("Bugtracker Server");
    setDescription("Configure the jacob.blooper bug tracker for your project.");
  }

  /**
   * @see IDialogPage#createControl(Composite)
   */
  public void createControl(Composite parent)
  {
    Composite container = new Composite(parent, SWT.NULL);
    GridLayout layout = new GridLayout();
    container.setLayout(layout);
    layout.numColumns = 2;
    layout.verticalSpacing = 9;

    Label label = new Label(container, SWT.NULL);
    label.setText("Server URL:");
    createServerUrlText(container);
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    serverUrl.setLayoutData(gd);
    
    label = new Label(container, SWT.NULL);
    label.setText("User name:");
    createUserNameText(container);
    gd = new GridData(GridData.FILL_HORIZONTAL);
    userName.setLayoutData(gd);
    
    label = new Label(container, SWT.NULL);
    label.setText("Password:");
    createPasswordText(container);
    gd = new GridData(GridData.FILL_HORIZONTAL);
    password.setLayoutData(gd);

    setControl(container);

    ((ConnectBugtrackerWizard)getWizard()).setServerUrl(serverUrl.getText());
    ((ConnectBugtrackerWizard)getWizard()).setUsername(userName.getText());
    ((ConnectBugtrackerWizard)getWizard()).setPassword(password.getText());
  }


  private Text createServerUrlText(Composite parent)
  {
    serverUrl = new Text(parent, SWT.BORDER|SWT.SINGLE);
    serverUrl.setText("http://www.tarragon-net.com:8080/blooper/services/blooper/");
//    serverUrl.setText("http://localhost:8080/blooper/services/blooper/");
    serverUrl.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        updateStatus();
      }
    });    
    return serverUrl;
  }

  private Text createUserNameText(Composite parent)
  {
    userName = new Text(parent, SWT.BORDER|SWT.SINGLE);
    userName.setText("demo");
//    userName.setText("admin");
    userName.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        updateStatus();
      }
    });    
    return userName;
  }
  
  private Text createPasswordText(Composite parent)
  {
    password = new Text(parent, SWT.BORDER|SWT.SINGLE);
    password.setText("urgs");
    password.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        updateStatus();
      }
    });    
    return password;
  }

  @Override
  public void setVisible(boolean visible)
  {
    if(serverUrl!=null && visible == false)
    {
      try
      {
        if(!BlooperServer.isValid(new SOAPEndPoint( serverUrl.getText(), userName.getText(), password.getText())))
        {
          getContainer().showPage(this);
          setErrorMessage("Unable to connect to Bugtracker");
          return;
        }
        else
        {
          setErrorMessage(null);
        }
      }
      catch (Exception e)
      {
        String error = e.getMessage();
        if(error==null || error.length()==0)
          error = "Unable to connect to jACOB Bugtracker";
        setErrorMessage(error);
        getContainer().showPage(this);
        return;
      }
    }
    
    super.setVisible(visible);
  }

  private void updateStatus()
  {
    if(serverUrl.getText()==null || serverUrl.getText().length()==0)
      setErrorMessage("Url for Bugtracker Server is required.");
    else if(userName.getText()==null || userName.getText().length()==0)
      setErrorMessage("User name for Bugtracker is required.");
    else
      setErrorMessage(null);
    ((ConnectBugtrackerWizard)getWizard()).setServerUrl(serverUrl.getText());
    ((ConnectBugtrackerWizard)getWizard()).setUsername(userName.getText());
    ((ConnectBugtrackerWizard)getWizard()).setPassword(password.getText());
  }
}