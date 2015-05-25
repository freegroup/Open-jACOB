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
package de.tif.jacob.designer.editor.browser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.jface.contentassist.SubjectControlContentAssistant;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.util.LabelContentAssistant;
import de.tif.jacob.designer.model.BrowserFieldModel;
import de.tif.jacob.designer.model.ObjectModel;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ColumnDetailsPageLocal implements IDetailsPage, PropertyChangeListener
{
  private IManagedForm managedForm;
  private BrowserFieldModel input;

  private Text  columnLabel;
  private Text  nameText;
  private Text  labelText;

  private Button configureableCheckbox;
  private Button visibleCheckbox;
  
  private SubjectControlContentAssistant assistant;

  protected BrowserFieldModel getBrowserFieldModel()
  {
    return input;
  }

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
    layout.topMargin    = 5;
    layout.leftMargin   = 5;
    layout.rightMargin  = 2;
    layout.bottomMargin = 2;
    parent.setLayout(layout);
    
    FormToolkit toolkit = managedForm.getToolkit();
    createCommonContents(toolkit, parent);
    createDetailContents(toolkit, parent);
  }

  protected void createDetailContents(FormToolkit toolkit, Composite parent)
  {
  }
  
  private void createCommonContents(FormToolkit toolkit, Composite parent)
  {
    Section commonSection = toolkit.createSection(parent, Section.DESCRIPTION);
    commonSection.marginWidth = 10;
    commonSection.setText("Local browser column");
    commonSection.setDescription("Set the header label of the selected column.");
    TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
    td.grabHorizontal = true;
    commonSection.setLayoutData(td);
    toolkit.createCompositeSeparator(commonSection);
    Composite client = toolkit.createComposite(commonSection);
    GridLayout glayout = new GridLayout();
    glayout.marginWidth = glayout.marginHeight = 0;
    glayout.numColumns = 2;
    client.setLayout(glayout);

    GridData textGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    textGridData.widthHint = 10;
   
    // create label text field
    //
    toolkit.createLabel(client, "Name:");
    nameText = toolkit.createText(client, null, SWT.SINGLE | SWT.BORDER);
    nameText.setLayoutData(textGridData);
    nameText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        try
        {
          getBrowserFieldModel().setName(nameText.getText());
        }
        catch (Exception e1)
        {
          nameText.setText(getBrowserFieldModel().getName());
          JacobDesigner.showException(e1);
        }
      }
    });

    // create label text field
    //
    toolkit.createLabel(client, "Label:");
    labelText = toolkit.createText(client, null, SWT.SINGLE | SWT.BORDER);
    labelText.setLayoutData(textGridData);
    labelText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        try
        {
          getBrowserFieldModel().setLabel(labelText.getText());
        }
        catch (Exception e1)
        {
          labelText.setText(getBrowserFieldModel().getLabel());
          JacobDesigner.showException(e1);
        }
      }
    });
    
    
    createSpacer(toolkit, client, 2);


    // create dbname text field
    //
    toolkit.createLabel(client, "Column:");
    columnLabel = toolkit.createText(client,null, SWT.SINGLE|SWT.BORDER);
    columnLabel.setLayoutData(textGridData);
    columnLabel.setEditable(false);

    
    this.labelText.setLayoutData(textGridData);

    
    createSpacer(toolkit, client, 3);

    toolkit.createLabel(client, "Configureable:");
    configureableCheckbox = toolkit.createButton(client, null, SWT.CHECK);
    configureableCheckbox.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        getBrowserFieldModel().setConfigureable(configureableCheckbox.getSelection());
      }
    });

    toolkit.createLabel(client, "Visible:");
    visibleCheckbox = toolkit.createButton(client, null, SWT.CHECK);
    visibleCheckbox.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        getBrowserFieldModel().setVisible(visibleCheckbox.getSelection());
      }
    });

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
    if (getBrowserFieldModel() == null)
      throw new IllegalStateException();


    if(assistant==null)
      assistant= LabelContentAssistant.createContentAssistant(labelText, this.getBrowserFieldModel().getJacobModel());

    columnLabel.setText(getBrowserFieldModel().getFieldModel().getName());
    if (!getBrowserFieldModel().getLabel().equals(this.labelText.getText()))
      this.labelText.setText(getBrowserFieldModel().getLabel());

    if (!getBrowserFieldModel().getName().equals(this.nameText.getText()))
      this.nameText.setText(getBrowserFieldModel().getName());

    this.configureableCheckbox.setSelection(getBrowserFieldModel().getConfigureable());
    this.visibleCheckbox.setSelection(getBrowserFieldModel().getVisible());
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
      this.input = (BrowserFieldModel) ssel.getFirstElement();
      this.input.addPropertyChangeListener(this);
      this.input.getFieldModel().addPropertyChangeListener(this);
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
    labelText.setFocus();
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
      this.input.getFieldModel().removePropertyChangeListener(this);
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
    return false;
  }
    
  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    // do not refresh, if type changes since this page is invalid then
    if (evt.getPropertyName()==ObjectModel.PROPERTY_FIELD_TYPE_CHANGED)
      return;

    refresh();
  }
}
