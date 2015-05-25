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

import java.util.List;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.FilteredList;
import de.tif.jacob.blooper.model.Application;
import de.tif.jacob.blooper.model.SOAPEndPoint;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (txt).
 */

public class ApplicationSelectionPage extends WizardPage
{
  FilteredList list;
  Text         ownApplicationName;
  Button       ownButton;
  Button       existingButton;
  
  /**
   * 
   */
  static class NameLabelProvider extends LabelProvider
  {
    public String getText(Object element)
    {
      return ((Application)element).getName();
    }
  }
    
  /**
   * Constructor for SampleNewWizardPage.
   * @param wizard 
   * 
   * @param pageName
   */
  public ApplicationSelectionPage() 
  {
    super("wizardPage");
    setTitle("Application selection");
    setDescription("Select the application name or create a new one.");
  }


  /**
   * @see IDialogPage#createControl(Composite)
   */
  public void createControl(Composite parent)
  {
    ConnectBugtrackerWizard wizard = ((ConnectBugtrackerWizard)getWizard());

    Composite container = new Composite(parent, SWT.NULL);
    GridLayout layout = new GridLayout();
    container.setLayout(layout);
    layout.numColumns = 1;
    layout.verticalSpacing = 9;

    ownButton = new Button(container,SWT.CHECK);
    ownButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        setNewApplication(true);
      }
    });
    ownButton.setText("Create new software modul on bugtracker.");
    
    ownApplicationName = new Text(container,SWT.NORMAL|SWT.BORDER);
    ownApplicationName.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        ConnectBugtrackerWizard wizard = ((ConnectBugtrackerWizard)getWizard());
        wizard.updateButtons();
      }
    });
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    ownApplicationName.setLayoutData(data);
    ownApplicationName.setText(wizard.getProject().getName());

    existingButton = new Button(container,SWT.CHECK);
    existingButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        setNewApplication(false);
      }
    });
    existingButton.setText("Use existing software modul.");
    
    int flags = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;
    list = new FilteredList(container, flags, new NameLabelProvider(),false, true, true);
    list.addSelectionListener(new SelectionListener()
    {
      public void widgetSelected(SelectionEvent e)
      {
        ConnectBugtrackerWizard wizard = ((ConnectBugtrackerWizard)getWizard());
        wizard.updateButtons();
      }

      public void widgetDefaultSelected(SelectionEvent e)
      {
        ConnectBugtrackerWizard wizard = ((ConnectBugtrackerWizard)getWizard());
        wizard.updateButtons();
      }
    });

    data = new GridData();
    data.heightHint = 150;
    data.widthHint = 250;
    data.grabExcessVerticalSpace = true;
    data.grabExcessHorizontalSpace = true;
    data.horizontalAlignment = GridData.FILL;
    data.verticalAlignment = GridData.FILL;
    list.setLayoutData(data);
    list.setFont(parent.getFont());

    ownApplicationName.setFocus();
    setControl(container);
  }

  public void setNewApplication(boolean flag)
  {
    list.setEnabled(!flag);
    list.setSelection((Object[])null);
    ownButton.setSelection(flag);
    existingButton.setSelection(!flag);
    ownApplicationName.setEnabled(flag);
    if(flag)
    {
      ownApplicationName.setFocus();/* geht nicht in einem wizard.....keine Ahnung warum*/
    }
  }
  
  protected String getApplicationName()
  {
    if(ownButton.getSelection())
    {
      return ownApplicationName.getText();
    }
    else
    {
      if(list.getSelection().length>0)
        return (((Application)list.getSelection()[0]).getName());
      else
        return "";
    }
  }
  
  @Override
  public void setVisible(boolean visible)
  {
    try
    {
      if(visible==true)
      {
        ConnectBugtrackerWizard wizard = ((ConnectBugtrackerWizard)getWizard());
        List<Application> apps = Application.getAll(new SOAPEndPoint(wizard.getServerUrl(),wizard.getUsername(), wizard.getPassword()));
        list.setElements(apps.toArray(new Application[0]));
        setNewApplication(true);
      }
    }
    catch (Exception e)
    {
//      QualitymasterPlugin.showException(e);
      // ignore
    }
    super.setVisible(visible);
  }


  private void updateStatus(String message)
  {
    setErrorMessage(message);
    setPageComplete(message == null);
  }
}