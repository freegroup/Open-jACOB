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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import de.tif.jacob.core.definition.impl.jad.castor.types.LongTextEditModeType;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.FieldModelTypeLongtext;
import de.tif.jacob.designer.model.ObjectModel;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FieldDetailsPageLongtext extends FieldDetailsPageCommon
{
  private Combo editModeCombo;
  private Button changeheaderCheckbox;

  

  /* (non-Javadoc)
   * @see de.tif.jacob.designer.editor.table.TableFieldDetailsPage#createDetailContents(org.eclipse.ui.forms.widgets.FormToolkit, org.eclipse.swt.widgets.Composite)
   */
  protected void createAdditionalContents(FormToolkit toolkit, Composite parent)
  {
    Section section = toolkit.createSection(parent, Section.DESCRIPTION);
    section.marginWidth = 10;
    section.setText("Text field details");
    section.setDescription("Set the detail properties of the selected text field.");
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
    // create search mode combobox
    toolkit.createLabel(client, "Edit mode:");
    editModeCombo = new Combo(client, SWT.READ_ONLY);
    editModeCombo.setItems(FieldModelTypeLongtext.EDITMODES);
    editModeCombo.addSelectionListener(new SelectionListener(){
      public void widgetSelected(SelectionEvent e)
      {
        getFieldModel().getLongtextFieldType().setEditMode(((Combo)e.widget).getText());
        if(editModeCombo.getText().equals(LongTextEditModeType.FULLEDIT.toString()))
        {
          changeheaderCheckbox.setEnabled(false);
          changeheaderCheckbox.setSelection(false);
        }
        else
        {
          changeheaderCheckbox.setEnabled(true);
          changeheaderCheckbox.setSelection(getFieldModel().getLongtextFieldType().hasChangeHeader());
        }
      }
      public void widgetDefaultSelected(SelectionEvent e)
      {
        throw new UnsupportedOperationException();
      }
    });
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    editModeCombo.setLayoutData(gd);
    toolkit.adapt(editModeCombo);
    
    //
    // create history checkbox
    toolkit.createLabel(client, "Automatic Change Header:");
    changeheaderCheckbox = toolkit.createButton(client, null, SWT.CHECK);
    changeheaderCheckbox.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        getFieldModel().getLongtextFieldType().setChangeHeader(changeheaderCheckbox.getSelection());
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
    // do not refresh, if type changes since this page is invalid them
    if (evt.getPropertyName()==ObjectModel.PROPERTY_FIELD_TYPE_CHANGED && evt.getSource()==getFieldModel())
    {
      if(evt.getNewValue()!=FieldModel.DBTYPE_TEXT)
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
    
    
    this.editModeCombo.setText(getFieldModel().getLongtextFieldType().getEditMode());
    if(this.editModeCombo.getText().equals(LongTextEditModeType.FULLEDIT.toString()))
    {
      this.changeheaderCheckbox.setEnabled(false);
      this.changeheaderCheckbox.setSelection(false);
    }
    else
    {
      this.changeheaderCheckbox.setSelection(getFieldModel().getLongtextFieldType().hasChangeHeader());
    }
  }
}
