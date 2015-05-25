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
package de.tif.jacob.designer.editor.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.apache.commons.validator.GenericValidator;
import org.eclipse.jface.contentassist.SubjectControlContentAssistant;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import de.tif.jacob.designer.editor.util.LabelContentAssistant;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.util.Verify;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FieldDetailsPageCommon implements IDetailsPage, PropertyChangeListener
{
  private IManagedForm managedForm;
  private FieldModel input;

  private Button requiredCheckbox;
  private Button readonlyCheckbox;
  private Button historyCheckbox;
  private Combo  typeCombo;
  private Text   nameText;
  private Text   descriptionText;
  private Text   dbNameText;
  private Text   labelText;
  private SubjectControlContentAssistant assistant;

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.IDetailsPage#initialize(org.eclipse.ui.forms.IManagedForm)
   */
  public void initialize(IManagedForm mform)
  {
    this.managedForm = mform;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.IDetailsPage#createContents(org.eclipse.swt.widgets.Composite)
   */
  public void createContents(Composite parent)
  {
    TableWrapLayout layout = new TableWrapLayout();
    layout.topMargin = 5;
    layout.leftMargin = 5;
    layout.rightMargin = 2;
    layout.bottomMargin = 2;
    parent.setLayout(layout);
    
    FormToolkit toolkit = managedForm.getToolkit();
    createCommonContents(toolkit, parent);
    createAdditionalContents(toolkit, parent);
  }

  protected void createAdditionalContents(FormToolkit toolkit, Composite parent)
  {
  }
  
  private void createCommonContents(FormToolkit toolkit, Composite parent)
  {
    Section commonSection = toolkit.createSection(parent, Section.DESCRIPTION);
    commonSection.marginWidth = 10;
    commonSection.setText("Common field details");
    commonSection.setDescription("Set the properties of the selected field type.");
    TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
    td.grabHorizontal = true;
    commonSection.setLayoutData(td);
    toolkit.createCompositeSeparator(commonSection);
    Composite client = toolkit.createComposite(commonSection);
    GridLayout glayout = new GridLayout();
    glayout.marginWidth = glayout.marginHeight = 0;
    glayout.numColumns = 2;
    client.setLayout(glayout);

    //
    // create name text field
    toolkit.createLabel(client, "Field name:");
    this.nameText = toolkit.createText(client, null, SWT.SINGLE | SWT.BORDER);
    this.nameText.addVerifyListener(Verify.FIELDNAME);
    this.nameText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        getFieldModel().setName(nameText.getText());
      }
    });
    GridData textGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    textGridData.widthHint = 10;
    this.nameText.setLayoutData(textGridData);
    
    
    //
    // create dbname text field
    toolkit.createLabel(client, "Column name:");
    this.dbNameText = toolkit.createText(client, null, SWT.SINGLE | SWT.BORDER);
    this.dbNameText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        getFieldModel().setDbName(dbNameText.getText());
      }
    });
    this.dbNameText.setLayoutData(textGridData);

    //
    // create description text field
    Label label =toolkit.createLabel(client, "Description:");
    label.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

    this.descriptionText = toolkit.createText(client,"",SWT.MULTI|SWT.WRAP|SWT.LEFT|SWT.BORDER|SWT.V_SCROLL);
    this.descriptionText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        getFieldModel().setDescription(descriptionText.getText());
      }
    });
    textGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    textGridData.widthHint = 10;
    textGridData.heightHint = 80;
    this.descriptionText.setLayoutData(textGridData);

    //
    // create label text field
    toolkit.createLabel(client, "Label:");
    this.labelText = toolkit.createText(client, null, SWT.SINGLE | SWT.BORDER);
    this.labelText.addVerifyListener(new VerifyListener()
        {
          public void verifyText(VerifyEvent e)
          {
            if(StringUtil.toSaveString(getFieldModel().getLabel()).startsWith("%") && Character.isLetter(e.character))
                e.text=e.text.toUpperCase();
          }
        });
    
    
    this.labelText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        getFieldModel().setLabel(labelText.getText());
      }
    });
    textGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    textGridData.widthHint = 10;
    this.labelText.setLayoutData(textGridData);

    //
    // create history checkbox
    toolkit.createLabel(client, "History:");
    historyCheckbox = toolkit.createButton(client, null, SWT.CHECK);
    historyCheckbox.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        getFieldModel().setHistory(historyCheckbox.getSelection());
      }
    });
    
    //
    // create readonly checkbox
    toolkit.createLabel(client, "Readonly:");
    readonlyCheckbox = toolkit.createButton(client, null, SWT.CHECK);
    readonlyCheckbox.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        getFieldModel().setReadonly(readonlyCheckbox.getSelection());
      }
    });
    
    //
    // create required checkbox
    toolkit.createLabel(client, "Required:");
    requiredCheckbox = toolkit.createButton(client, null, SWT.CHECK);
    requiredCheckbox.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        getFieldModel().setRequired(requiredCheckbox.getSelection());
      }
    });


    createSpacer(toolkit, client, 2);

    //
    // create type combobox
    toolkit.createLabel(client, "Type:");
    typeCombo = new Combo(client, SWT.READ_ONLY);
    typeCombo.setItems(FieldModel.DBTYPES);
    typeCombo.addSelectionListener(new SelectionListener(){
      public void widgetSelected(SelectionEvent e)
      {
        getFieldModel().setType(((Combo)e.widget).getText());
      }
      public void widgetDefaultSelected(SelectionEvent e)
      {
        throw new UnsupportedOperationException();
      }
    });
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    typeCombo.setLayoutData(gd);
    toolkit.adapt(typeCombo);
    
    createSpacer(toolkit, client, 2);

    toolkit.paintBordersFor(commonSection);
    commonSection.setClient(client);
  }

  private void createSpacer(FormToolkit toolkit, Composite parent, int span)
  {
    Label spacer = toolkit.createLabel(parent, "");
    GridData gd = new GridData();
    gd.horizontalSpan = span;
    spacer.setLayoutData(gd);
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.forms.IDetailsPage#refresh()
   */
  public void refresh()
  {
    if (getFieldModel() == null)
      throw new IllegalStateException();

    if(assistant==null)
      assistant= LabelContentAssistant.createContentAssistant(labelText, this.getFieldModel().getJacobModel());
    
    if (!getFieldModel().getName().equals(this.nameText.getText()))
      this.nameText.setText(getFieldModel().getName());

    if (!getFieldModel().getDbName().equals(this.dbNameText.getText()))
      this.dbNameText.setText(getFieldModel().getDbName());

    if (!getFieldModel().getDescription().equals(this.descriptionText.getText()))
      this.descriptionText.setText(getFieldModel().getDescription());

    if (!getFieldModel().getLabel().equals(this.labelText.getText()))
      this.labelText.setText(getFieldModel().getLabel());

    this.requiredCheckbox.setSelection(getFieldModel().getRequired());
    this.readonlyCheckbox.setSelection(getFieldModel().getReadonly());
    this.historyCheckbox.setSelection(getFieldModel().getHistory());

    if (!getFieldModel().getType().equals(this.typeCombo.getText()))
      this.typeCombo.setText(getFieldModel().getType());
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.IPartSelectionListener#selectionChanged(org.eclipse.ui.forms.IFormPart,
   *      org.eclipse.jface.viewers.ISelection)
   */
  public final void selectionChanged(IFormPart part, ISelection selection)
  {
    unregister();
    IStructuredSelection ssel = (IStructuredSelection) selection;
    if (ssel.size() == 1)
    {
      this.input = (FieldModel) ssel.getFirstElement();
      this.input.addPropertyChangeListener(this);
    }
    else
      throw new IllegalStateException();
    refresh();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.IDetailsPage#commit()
   */
  public void commit(boolean onSave)
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.IDetailsPage#setFocus()
   */
  public void setFocus()
  {
    this.nameText.setFocus();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.IDetailsPage#dispose()
   */
  public void dispose()
  {
    unregister();
  }
  
  private void unregister()
  {
    if (this.input != null)
    {
      this.input.removePropertyChangeListener(this);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.IDetailsPage#isDirty()
   */
  public boolean isDirty()
  {
    return false;
  }

  public boolean isStale()
  {
    return false;
  }

  public boolean setFormInput(Object input)
  {
    // FAKE: This function will never be called!!!!!!!!!!!!!!!!! Why?!
    System.out.println("Input object:"+input);
    return false;
  }
    
  protected FieldModel getFieldModel()
  {
    return this.input;
  }

  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    // do not refresh, if type changes since this page is invalid then
    if (evt.getPropertyName()==FieldModel.PROPERTY_FIELD_TYPE_CHANGED)
      return;

    refresh();
  }
  
  public static void main(String[] args)
  {
    System.out.println("isValid:"+GenericValidator.matchRegexp("a","^[a-zA-Z][a-zA-Z_0-9]*$"));
    System.out.println("isValid:"+GenericValidator.matchRegexp("a b","^[a-zA-Z][a-zA-Z_0-9]*$"));
    System.out.println("isValid:"+GenericValidator.matchRegexp("a_1","^[a-zA-Z][a-zA-Z_0-9]*$"));
    System.out.println("isValid:"+GenericValidator.matchRegexp("1a","^[a-zA-Z][a-zA-Z_0-9]*$"));
    System.out.println("isValid:"+GenericValidator.matchRegexp("a1a","^[a-zA-Z][a-zA-Z_0-9]*$"));
    System.out.println("isValid:"+GenericValidator.matchRegexp("Aa_1","^[a-zA-Z][a-zA-Z_0-9]*$"));
  }
}
