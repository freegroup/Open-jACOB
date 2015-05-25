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
 * Created on 11.11.2004
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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.FieldModelTypeTimestamp;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class FieldDetailsPageTimestamp extends FieldDetailsPageCommon
{
  private static final String NOW = "NOW";

  private Button initializeWithNowCheckbox;
  private Combo  resolutionCombo;


  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.editor.table.TableFieldDetailsPage#createDetailContents(org.eclipse.ui.forms.widgets.FormToolkit,
   *      org.eclipse.swt.widgets.Composite)
   */
  protected void createAdditionalContents(FormToolkit toolkit, Composite parent)
  {
    Section section = toolkit.createSection(parent, Section.DESCRIPTION);
    section.marginWidth = 10;
    section.setText("Timestamp field details");
    section.setDescription("Set the detail properties of the selected timestamp field.");

    TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
    td.grabHorizontal = true;
    section.setLayoutData(td);

    toolkit.createCompositeSeparator(section);

    Composite client = toolkit.createComposite(section);
    section.setClient(client);
    GridLayout glayout = new GridLayout();
    glayout.marginWidth = glayout.marginHeight = 0;
    glayout.numColumns = 3;
    client.setLayout(glayout);

    //
    // create resolution radio buttons
    toolkit.createLabel(client, "Resolution:");
    createResolutionControls(toolkit, client);

    //
    // create case sensitive checkbox
    this.initializeWithNowCheckbox = toolkit.createButton(client, "Initialize with now", SWT.CHECK);
    this.initializeWithNowCheckbox.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        getFieldModel().getTimestampFieldType().setDefault(initializeWithNowCheckbox.getSelection() ? NOW : "");
      }
    });
  }


  private Control createResolutionControls(FormToolkit toolkit, Composite parent)
  {
    resolutionCombo = new Combo(parent, SWT.READ_ONLY);
    resolutionCombo.setItems(FieldModelTypeTimestamp.RESOLUTIONS);
    resolutionCombo.addSelectionListener(new SelectionListener()
    {
      public void widgetSelected(SelectionEvent e)
      {
        getFieldModel().getTimestampFieldType().setResolution(((Combo) e.widget).getText());
      }

      public void widgetDefaultSelected(SelectionEvent e)
      {
        throw new UnsupportedOperationException();
      }
    });
    toolkit.adapt(resolutionCombo);
    return this.resolutionCombo;
  }

  
  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    // do not refresh, if type changes since this page is invalid then
    if (evt.getPropertyName()==FieldModel.PROPERTY_FIELD_TYPE_CHANGED && evt.getSource()==getFieldModel())
    {
      if(evt.getNewValue()!=FieldModel.DBTYPE_TIMESTAMP)
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
    
    this.initializeWithNowCheckbox.setSelection(NOW.equalsIgnoreCase(getFieldModel().getTimestampFieldType().getDefault()));
    this.resolutionCombo.setText(getFieldModel().getTimestampFieldType().getResolution());
  }
}
