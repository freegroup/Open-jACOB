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
 * 
 * Created on 09.11.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.editor.table;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.SystemUtils;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.ObjectModel;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class FieldDetailsPageBoolean extends FieldDetailsPageCommon
{
  class MasterLabelProvider extends LabelProvider implements ITableLabelProvider
  {
    public String getColumnText(Object obj, int columnIndex)
    {
      String result = "";
      FieldModel model = (FieldModel) obj;
      switch (columnIndex)
      {
        case 0:
          result = model.getName();
          break;
        case 1:
          result = model.getType();
          break;
        default:
          break;
      }
      return result;
    }

    public Image getColumnImage(Object obj, int index)
    {
      //      if (obj instanceof TypeOne)
      //      {
      //        return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
      //      }
      //      if (obj instanceof TypeTwo)
      //      {
      //        return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
      //      }
      return index == 0 ? ((FieldModel)obj).getImage() : null;
    }
  }

  private Combo defaultCombo;

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
    section.setText("Boolean field details");
    section.setDescription("Set detail properties of the selected field.");
    TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
    td.grabHorizontal = true;
    section.setLayoutData(td);

    toolkit.createCompositeSeparator(section);
    Composite client = toolkit.createComposite(section);
    //    toolkit.paintBordersFor(section);
    section.setClient(client);

    GridLayout glayout = new GridLayout();
    glayout.marginWidth = glayout.marginHeight = 0;
    glayout.numColumns = 3;
    client.setLayout(glayout);

    //
    // create search mode combobox
    toolkit.createLabel(client, "Default:");

    this.defaultCombo = new Combo(client, SWT.READ_ONLY);
    this.defaultCombo.addSelectionListener(new SelectionListener()
    {
      public void widgetSelected(SelectionEvent e)
      {
        switch(((Combo) e.widget).getSelectionIndex())
        {
        case 0:
          getFieldModel().getBooleanFieldType().setDefault(null);
          break;
        case 1:
          getFieldModel().getBooleanFieldType().setDefault(true);
          break;
        case 2:
          getFieldModel().getBooleanFieldType().setDefault(false);
          break;
        default:
          getFieldModel().getBooleanFieldType().setDefault(null);
          break;
        }
        
      }

      public void widgetDefaultSelected(SelectionEvent e)
      {
        throw new UnsupportedOperationException();
      }
    });
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    this.defaultCombo.setLayoutData(gd);
    toolkit.adapt(this.defaultCombo);
  }
  
  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    // do not refresh, if type changes since this page is invalid them
    if (evt.getPropertyName()==ObjectModel.PROPERTY_FIELD_TYPE_CHANGED && evt.getSource()==getFieldModel())
    {
      if(evt.getNewValue()!=FieldModel.DBTYPE_BOOLEAN)
        getFieldModel().removePropertyChangeListener(this);
    }

    super.propertyChange(evt);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.IFormPart#refresh()
   */
  public void refresh()
  {
    super.refresh();

    this.defaultCombo.setItems(new String[]{"","true","false"});
    if(Boolean.TRUE.equals(getFieldModel().getBooleanFieldType().getDefault()))
      this.defaultCombo.select(1);
    else if(Boolean.FALSE.equals(getFieldModel().getBooleanFieldType().getDefault()))
      this.defaultCombo.select(2);
    else
      this.defaultCombo.select(0);
  }
}
