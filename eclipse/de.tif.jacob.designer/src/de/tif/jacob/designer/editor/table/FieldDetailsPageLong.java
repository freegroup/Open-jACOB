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
/*
 * Created on 09.11.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.editor.table;

import java.beans.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.FieldModelTypeLong;
import de.tif.jacob.designer.util.Verify;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FieldDetailsPageLong extends FieldDetailsPageCommon
{
  private Text defaultValueText;
  private Text minValueText;
  private Text maxValueText;
  private Button autoincrementCheckbox;

  /* (non-Javadoc)
   * @see de.tif.jacob.designer.editor.table.TableFieldDetailsPage#createDetailContents(org.eclipse.ui.forms.widgets.FormToolkit, org.eclipse.swt.widgets.Composite)
   */
  protected void createAdditionalContents(FormToolkit toolkit, Composite parent)
  {
    Section section = toolkit.createSection(parent, Section.DESCRIPTION);
    section.marginWidth = 10;
    section.setText("Long field details");
    section.setDescription("Set the detail properties of the selected long field.");
    TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
    td.grabHorizontal = true;
    section.setLayoutData(td);
    
    toolkit.createCompositeSeparator(section);
    Composite client = toolkit.createComposite(section);
    GridLayout glayout = new GridLayout();
    glayout.marginWidth = glayout.marginHeight = 0;
    glayout.numColumns = 2;
    client.setLayout(glayout);
   
    //
    // create default value text field
    toolkit.createLabel(client, "Default value:");
    this.defaultValueText = toolkit.createText(client, null, SWT.SINGLE | SWT.BORDER);
    this.defaultValueText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        try
        {
          if (defaultValueText.getText().length() == 0)
            getFieldModel().getLongFieldType().deleteDefaultValue();
          else
            getFieldModel().getLongFieldType().setDefaultValue(Long.parseLong(defaultValueText.getText()));
        }
        catch (NumberFormatException ex)
        {
          // ignore, could temporary happen
        }
      }
    });
    this.defaultValueText.addVerifyListener(Verify.INTEGER);
    GridData textGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    this.defaultValueText.setLayoutData(textGridData);
    
    //
    // create min value text field
    toolkit.createLabel(client, "Min value:");
    this.minValueText = toolkit.createText(client, null, SWT.SINGLE | SWT.BORDER);
    this.minValueText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        try
        {
          if (minValueText.getText().length()==0)
            getFieldModel().getLongFieldType().deleteMinValue();
          else
            getFieldModel().getLongFieldType().setMinValue(Long.parseLong(minValueText.getText()));
        }
        catch (NumberFormatException ex)
        {
          // ignore, could temporary happen
        }
      }
    });
    this.minValueText.addVerifyListener(Verify.INTEGER);
    this.minValueText.setLayoutData(textGridData);
    
    //
    // create max value text field
    toolkit.createLabel(client, "Max value:");
    this.maxValueText = toolkit.createText(client, null, SWT.SINGLE | SWT.BORDER);
    this.maxValueText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        try
        {
          if (maxValueText.getText().length()==0)
            getFieldModel().getLongFieldType().deleteMaxValue();
          else
            getFieldModel().getLongFieldType().setMaxValue(Long.parseLong(maxValueText.getText()));
        }
        catch (NumberFormatException ex)
        {
          // ignore, could temporary happen
        }
      }
    });
    this.maxValueText.addVerifyListener(Verify.INTEGER);
    this.maxValueText.setLayoutData(textGridData);
    
    //
    // create case sensitive checkbox
    toolkit.createLabel(client, "Autoincrement:");
    this.autoincrementCheckbox = toolkit.createButton(client, null, SWT.CHECK);
    this.autoincrementCheckbox.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        getFieldModel().getLongFieldType().setAutoincrement(autoincrementCheckbox.getSelection());
      }
    });
    
    // register client
    toolkit.paintBordersFor(section);
    section.setClient(client);
  }
  
  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    // do not refresh, if type changes since this page is invalid then
    if (evt.getPropertyName()==FieldModel.PROPERTY_FIELD_TYPE_CHANGED && evt.getSource()==getFieldModel())
    {
      if(evt.getNewValue()!=FieldModel.DBTYPE_LONG)
        getFieldModel().removePropertyChangeListener(this);
    }

    super.propertyChange(evt);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.ui.forms.IFormPart#refresh()
   */
  public void refresh()
  {
    super.refresh();
    
    FieldModelTypeLong myType = getFieldModel().getLongFieldType();
    
    this.autoincrementCheckbox.setSelection(myType.getAutoincrement());
    
    String defaultValue = myType.hasDefaultValue()?Long.toString(myType.getDefaultValue()):"";
    if (!this.defaultValueText.getText().equals(defaultValue))
      this.defaultValueText.setText(defaultValue);

    String minValue = myType.hasMinValue()?Long.toString(myType.getMinValue()):"";
    if (!this.minValueText.getText().equals(minValue))
      this.minValueText.setText(minValue);

    String maxValue = myType.hasMaxValue()?Long.toString(myType.getMaxValue()):"";
    if (!this.maxValueText.getText().equals(maxValue))
      this.maxValueText.setText(maxValue);
  }
}
