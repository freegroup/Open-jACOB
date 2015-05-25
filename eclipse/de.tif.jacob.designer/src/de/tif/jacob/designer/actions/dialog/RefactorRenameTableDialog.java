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
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.TableModel;

/**
 * 
 */
public class RefactorRenameTableDialog extends Dialog
{
  
  /**
   * 
   */
  class Validator implements IInputValidator
  {
    
    /**
     * 
     * 
     * @param newText 
     * 
     * @return 
     */
    public String isValid(String newText)
    {
      if(!StringUtils.containsOnly(newText,"abcdefghijklmnopqrstuvwxyzABCDEFGHJIKLMNOPQRSTUVWXYZ_1234567890".toCharArray()))
        return "Invalid character in table name.";

      if(model.getJacobModel().getTableModel(newText)!=null)
        return "Table already exists. Please use another name,";
      
      if(renameAliasButton!=null && renameAliasButton.getSelection() && model.getJacobModel().getTableAliasModel(newText)!=null)
        return "TableAlias already exists. Please use another name,";
      
      if(renameBrowserButton!=null && renameBrowserButton.getSelection() && model.getJacobModel().getBrowserModel(newText+"Browser")!=null)
        return "Browser already exists. Please use another name,";

      return null;
    }
  };

  private final TableModel model;
  
  private String title;
  private String message;
  private String value = "";//$NON-NLS-1$
  
  private final IInputValidator validator;
  
  private Button okButton;

  private Button renameAliasButton;
  private Button renameBrowserButton;
 
  private boolean renameAlias=false;
  private boolean renameBrowser=false;
  private Text text;
  private Text errorMessageText;

  /**
   * 
   * 
   * @param parentShell 
   * @param model 
   */
  public RefactorRenameTableDialog(Shell parentShell,TableModel model)
  {
    super(parentShell);
    this.model = model;
    this.title     = "Rename Table";
    this.message   = "Enter the new name for the table";
    this.value     = model.getName();
    this.validator = new Validator();
  }

  /**
   * 
   * 
   * @return 
   */
  public boolean renameStandartAlias()
  {
    return renameAlias;
  }
  
  public boolean renameStandartBrowser()
  {
    return renameBrowser;
  }
  
  /*
   * (non-Javadoc) Method declared on Dialog.
   */
  /**
   * 
   * 
   * @param buttonId 
   */
  protected void buttonPressed(int buttonId)
  {
    if (buttonId == IDialogConstants.OK_ID)
    {
      value = text.getText();
      renameAlias = renameAliasButton.getSelection();
      renameBrowser = renameBrowserButton.getSelection();
    }
    else
    {
      value = null;
      renameAlias=false;
      renameBrowser=false;
    }
    super.buttonPressed(buttonId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
   */
  /**
   * 
   * 
   * @param shell 
   */
  protected void configureShell(Shell shell)
  {
    super.configureShell(shell);
    if (title != null)
      shell.setText(title);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
   */
  /**
   * 
   * 
   * @param parent 
   */
  protected void createButtonsForButtonBar(Composite parent)
  {
    // create OK and Cancel buttons by default
    okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    //do this here because setting the text will set enablement on the ok
    // button
    text.setFocus();
    if (value != null)
    {
      text.setText(value);
      text.selectAll();
    }
  }

  /*
   * (non-Javadoc) Method declared on Dialog.
   */
  /**
   * 
   * 
   * @param parent 
   * 
   * @return 
   */
  protected Control createDialogArea(Composite parent)
  {
    // create composite
    Composite composite = (Composite) super.createDialogArea(parent);
    // create message
    if (message != null)
    {
      Label label = new Label(composite, SWT.WRAP);
      label.setText(message);
      GridData data = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
      data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
      label.setLayoutData(data);
      label.setFont(parent.getFont());
    }
    text = new Text(composite, SWT.SINGLE | SWT.BORDER);
    text.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
    text.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        validateInput();
      }
    });
    errorMessageText = new Text(composite, SWT.READ_ONLY);
    errorMessageText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
    errorMessageText.setBackground(errorMessageText.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
    
    TableAliasModel alias = model.getJacobModel().getTableAliasModel(model.getName());

    // Falls es einen Standard TableAlias gibt, kann dieser gleich mit umbenannt werden.
    //
    if(alias!=null)
    {
	    renameAliasButton = new Button(composite, SWT.CHECK);
	    renameAliasButton.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
	    renameAliasButton.setText("Rename table alias.... to ....");
	    renameAliasButton.setSelection(true);
    }
    
    // Falls es einen Standardbrowser gibt, kann dieser gleich mit umbenannt werden
    //
    if(model.getJacobModel().getBrowserModel(alias.getName()+"Browser")!=null)
    {
      renameBrowserButton = new Button(composite, SWT.CHECK);
      renameBrowserButton.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
      renameBrowserButton.setText("Rename browser.... to ....");
      renameBrowserButton.setSelection(true);
    }
 
    applyDialogFont(composite);
    return composite;
  }

  /**
   * 
   * 
   * @return 
   */
  protected Label getErrorMessageLabel()
  {
    return null;
  }

  /**
   * 
   * 
   * @return 
   */
  protected Button getOkButton()
  {
    return okButton;
  }

  /**
   * 
   * 
   * @return 
   */
  protected Text getText()
  {
    return text;
  }

  /**
   * 
   * 
   * @return 
   */
  protected IInputValidator getValidator()
  {
    return validator;
  }

  /**
   * 
   * 
   * @return 
   */
  public String getValue()
  {
    return value;
  }

  /**
   * 
   */
  protected void validateInput()
  {
    String errorMessage = null;
    if (validator != null)
    {
      errorMessage = validator.isValid(text.getText());
    }
    // Bug 16256: important not to treat "" (blank error) the same as null
    // (no error)
    setErrorMessage(errorMessage);
    
    // Falls es ein TableAlias gibt welcher gleich wie die Tabelle heist, dann wird dieser
    // mit umbenannt.
    //
    TableAliasModel alias = model.getJacobModel().getTableAliasModel(model.getName());
    if(renameAliasButton!=null)
    {
      renameAliasButton.setText("Rename Default Table Alias ["+alias.getName()+"] to ["+text.getText()+"]");
    }

    if(renameBrowserButton!=null)
    {
      renameBrowserButton.setText("Rename Default Browser ["+alias.getName()+"Browser] to ["+text.getText()+"Browser]");
    }
  }

  /**
   * 
   * 
   * @param errorMessage 
   */
  public void setErrorMessage(String errorMessage)
  {
    errorMessageText.setText(errorMessage == null ? "" : errorMessage); //$NON-NLS-1$
    okButton.setEnabled(errorMessage == null);
    errorMessageText.getParent().update();
  }
}
