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
 * Created on 17.02.2005
 *
 */
package de.tif.jacob.designer.editor.table;

import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.FieldModelTypeDecimal;
import de.tif.jacob.designer.util.Verify;

/**
 * @author andreas sonntag
 *
 */
public class FieldDetailsPageDecimal extends FieldDetailsPageCommon
{
  private Text scaleText;
  private Text defaultValueText;
  private Text minValueText;
  private Text maxValueText;

  /* (non-Javadoc)
   * @see de.tif.jacob.designer.editor.table.TableFieldDetailsPage#createDetailContents(org.eclipse.ui.forms.widgets.FormToolkit, org.eclipse.swt.widgets.Composite)
   */
  protected void createAdditionalContents(FormToolkit toolkit, Composite parent)
  {
    Section section = toolkit.createSection(parent, Section.DESCRIPTION);
    section.marginWidth = 10;
    section.setText("Decimal field details");
    section.setDescription("Set the detail properties of the selected decimal field.");
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
    // create scale text field
    toolkit.createLabel(client, "Scale:");
    this.scaleText = toolkit.createText(client, null, SWT.SINGLE | SWT.BORDER);
    this.scaleText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        try
        {
            getFieldModel().getDecimalFieldType().setScale(Short.parseShort(scaleText.getText()));
        }
        catch (NumberFormatException ex)
        {
          // ignore, could temporary happen
        }
      }
    });
    this.scaleText.addVerifyListener(Verify.INTEGER);
    GridData textGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    this.scaleText.setLayoutData(textGridData);
    
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
            getFieldModel().getDecimalFieldType().setDefaultValue(null);
          else
            getFieldModel().getDecimalFieldType().setDefaultValue(new BigDecimal(defaultValueText.getText()));
        }
        catch (NumberFormatException ex)
        {
          // ignore, could temporary happen
        }
      }
    });
    this.defaultValueText.addVerifyListener(Verify.DECIMAL);
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
            getFieldModel().getDecimalFieldType().setMinValue(null);
          else
            getFieldModel().getDecimalFieldType().setMinValue(new BigDecimal(minValueText.getText()));
        }
        catch (NumberFormatException ex)
        {
          // ignore, could temporary happen
        }
      }
    });
    this.minValueText.addVerifyListener(Verify.DECIMAL);
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
            getFieldModel().getDecimalFieldType().setMaxValue(null);
          else
            getFieldModel().getDecimalFieldType().setMaxValue(new BigDecimal(maxValueText.getText()));
        }
        catch (NumberFormatException ex)
        {
          // ignore, could temporary happen
        }
      }
    });
    this.maxValueText.addVerifyListener(Verify.DECIMAL);
    this.maxValueText.setLayoutData(textGridData);
    
    //
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
      if(evt.getNewValue()!=FieldModel.DBTYPE_DECIMAL)
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
    
    FieldModelTypeDecimal myType = getFieldModel().getDecimalFieldType();
    
    String scaleValue = Short.toString(myType.getScale());
    if (!this.scaleText.getText().equals(scaleValue))
      this.scaleText.setText(scaleValue);

    String defaultValue = myType.getDefaultValue() != null ? myType.getDefaultValue().toString() : "";
    if (!this.defaultValueText.getText().equals(defaultValue))
      this.defaultValueText.setText(defaultValue);

    String minValue = myType.getMinValue() != null ? myType.getMinValue().toString() : "";
    if (!this.minValueText.getText().equals(minValue))
      this.minValueText.setText(minValue);

    String maxValue = myType.getMaxValue() != null ? myType.getMaxValue().toString() : "";
    if (!this.maxValueText.getText().equals(maxValue))
      this.maxValueText.setText(maxValue);
  }
}
