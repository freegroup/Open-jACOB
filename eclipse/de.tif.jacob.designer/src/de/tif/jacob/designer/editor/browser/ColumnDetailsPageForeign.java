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
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.contentassist.SubjectControlContentAssistant;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.util.LabelContentAssistant;
import de.tif.jacob.designer.model.BrowserFieldForeignModel;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ColumnDetailsPageForeign implements IDetailsPage, PropertyChangeListener
{
  private IManagedForm managedForm;
  private BrowserFieldForeignModel input;

  private Text  headerLabelText;
  private Text  nameText;
  private Label foreignTableLabel;
  private Combo foreignFieldCombo;
  private Combo browserToUseCombo;
  private Combo relationsetToUseCombo;
  private Combo filldirectionCombo;
  
  private Button configureableCheckbox;
  private Button visibleCheckbox;

  private SubjectControlContentAssistant assistant;

  protected BrowserFieldForeignModel getBrowserFieldModel()
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
    commonSection.setText("Foreign browser column");
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
    toolkit.createLabel(client, "Label: ");
    this.headerLabelText = toolkit.createText(client, null, SWT.SINGLE | SWT.BORDER);
    this.headerLabelText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        try
        {
          getBrowserFieldModel().setLabel(headerLabelText.getText());
        }
        catch (Exception e1)
        {
          headerLabelText.setText(getBrowserFieldModel().getLabel());
          JacobDesigner.showException(e1);
        }
      }
    });
    this.headerLabelText.setLayoutData(textGridData);

    createSpacer(toolkit, client, 2);

    // create foreign table label
    //
    toolkit.createLabel(client, "Foreign table: ");
    foreignTableLabel = toolkit.createLabel(client, "");

    // create browser name field
    //
    toolkit.createLabel(client, "Browser to use:");
    browserToUseCombo = new Combo(client, SWT.READ_ONLY);
    browserToUseCombo.setItems(new String[0]);
    browserToUseCombo.addSelectionListener(new SelectionListener(){
      public void widgetSelected(SelectionEvent e)
      {
        getBrowserFieldModel().setBrowserToUse(((Combo)e.widget).getText());
      }
      public void widgetDefaultSelected(SelectionEvent e)
      {
        throw new UnsupportedOperationException();
      }
    });
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    browserToUseCombo.setLayoutData(gd);
    toolkit.adapt(browserToUseCombo);
    
    // create filldirection field
    //
    toolkit.createLabel(client, "Fill direction :");
    filldirectionCombo = new Combo(client, SWT.READ_ONLY);
    filldirectionCombo.setItems(new String[0]);
    filldirectionCombo.addSelectionListener(new SelectionListener(){
      public void widgetSelected(SelectionEvent e)
      {
        getBrowserFieldModel().setFilldirection(((Combo)e.widget).getText());
      }
      public void widgetDefaultSelected(SelectionEvent e)
      {
        throw new UnsupportedOperationException();
      }
    });
    gd = new GridData(GridData.FILL_HORIZONTAL);
    filldirectionCombo.setLayoutData(gd);
    toolkit.adapt(filldirectionCombo);
    
    // create relationset field
    //
    toolkit.createLabel(client, "Relationset to use:");
    relationsetToUseCombo = new Combo(client, SWT.READ_ONLY);
    relationsetToUseCombo.setItems(new String[0]);
    relationsetToUseCombo.addSelectionListener(new SelectionListener(){
      public void widgetSelected(SelectionEvent e)
      {
        getBrowserFieldModel().setRelationset(((Combo)e.widget).getText());
      }
      public void widgetDefaultSelected(SelectionEvent e)
      {
        throw new UnsupportedOperationException();
      }
    });
    gd = new GridData(GridData.FILL_HORIZONTAL);
    relationsetToUseCombo.setLayoutData(gd);
    toolkit.adapt(relationsetToUseCombo);
    

    // create column name field
    //
    toolkit.createLabel(client, "Display column:");
    foreignFieldCombo = new Combo(client, SWT.READ_ONLY);
    foreignFieldCombo.setItems(new String[0]);
    foreignFieldCombo.addSelectionListener(new SelectionListener(){
      public void widgetSelected(SelectionEvent e)
      {
        getBrowserFieldModel().setFieldToDisplay(((Combo)e.widget).getText());
      }
      public void widgetDefaultSelected(SelectionEvent e)
      {
        throw new UnsupportedOperationException();
      }
    });
    gd = new GridData(GridData.FILL_HORIZONTAL);
    foreignFieldCombo.setLayoutData(gd);
    toolkit.adapt(foreignFieldCombo);
    
    createSpacer(toolkit, client, 2);


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
    if (getBrowserFieldModel() == null)
      throw new IllegalStateException();

    if(assistant==null)
      assistant= LabelContentAssistant.createContentAssistant(headerLabelText, this.getBrowserFieldModel().getJacobModel());

    JacobModel jacobModel = getBrowserFieldModel().getBrowserModel().getJacobModel();
    foreignTableLabel.setText(getBrowserFieldModel().getForeignTableAliasModel().getName());
    List possibleBrowserNames= new ArrayList(jacobModel.getBrowserNames(getBrowserFieldModel().getForeignTableAliasModel()));
    possibleBrowserNames.add(0,"");
    browserToUseCombo.setItems((String[])possibleBrowserNames.toArray(new String[0]));
    browserToUseCombo.setText(StringUtil.toSaveString(getBrowserFieldModel().getBrowserToUse()));
    
    List relationsetNames= new ArrayList(jacobModel.getRelationsetNames());
    relationsetNames.add(0,"");
    relationsetToUseCombo.setItems((String[])relationsetNames.toArray(new String[0]));
    relationsetToUseCombo.setText(StringUtil.toSaveString(getBrowserFieldModel().getRelationset()));

    List filldirections= new ArrayList(ObjectModel.getFilldirections());
    filldirections.add(0,"");
    filldirectionCombo.setItems((String[])filldirections.toArray(new String[0]));
    filldirectionCombo.setText(StringUtil.toSaveString(getBrowserFieldModel().getFilldirection()));


    foreignFieldCombo.setItems((String[])getBrowserFieldModel().getForeignTableModel().getFieldNames().toArray(new String[0]));
    foreignFieldCombo.setText(StringUtil.toSaveString(getBrowserFieldModel().getFieldModel().getName()));
    
    if (!getBrowserFieldModel().getLabel().equals(this.headerLabelText.getText()))
      this.headerLabelText.setText(StringUtil.toSaveString(getBrowserFieldModel().getLabel()));
    
    if (!getBrowserFieldModel().getName().equals(this.nameText.getText()))
      this.nameText.setText(StringUtil.toSaveString(getBrowserFieldModel().getName()));

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
      this.input = (BrowserFieldForeignModel) ssel.getFirstElement();
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
    nameText.setFocus();
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
//      System.out.println("#### FieldDetailsPage.unregister()");
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
    return false;
  }
    
  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    // do not refresh, if type changes since this page is invalid then
    if (evt.getPropertyName()==FieldModel.PROPERTY_TABLE_CHANGED)
      return;

    refresh();
  }
}
