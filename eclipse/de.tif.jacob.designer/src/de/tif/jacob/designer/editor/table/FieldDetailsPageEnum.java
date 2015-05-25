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

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.ListUtils;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import de.tif.jacob.designer.editor.table.enumfield.EnumCellModifier;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class FieldDetailsPageEnum extends FieldDetailsPageCommon
{
  private Combo defaultCombo;
  private TableViewer enumViewer;
  private Table       enumTable;
  public static final List<String> COLUMN_NAMES;
  
  static
  {
    List<String> l = new ArrayList<String>();
    l.add("Value");
    l.add("Label");
    COLUMN_NAMES = ListUtils.unmodifiableList(l);
  }
  
  /**
   * @param id
   * @param title
   */
  class EnumContentProvider implements IStructuredContentProvider
  {
    public Object[] getElements(Object inputElement)
    {
        return getFieldModel().getEnumFieldType().getEnumValues();
    }

    public void dispose()
    {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }
  }

  class EnumLabelProvider extends LabelProvider implements ITableLabelProvider
  {
    public String getColumnText(Object obj, int columnIndex)
    {
      String result = "";
      String model = (String) obj;
      switch (columnIndex) {
        case 0:
          result = model;
          break;
        case 1 :
          int index = getFieldModel().getEnumFieldType().getIndex(model);
          result = getFieldModel().getEnumFieldType().getLabel(index);
          break;
        default :
          break;  
      }
      return result;
    }

    public Image getColumnImage(Object obj, int index)
    {
      return null;
    }
  }

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
    section.setText("Enum field details");
    section.setDescription("Set the detail properties of the selected enum field.");
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

    // create search mode combobox
    toolkit.createLabel(client, "Default:");

    this.defaultCombo = new Combo(client, SWT.READ_ONLY);
    this.defaultCombo.addSelectionListener(new SelectionListener()
    {
      public void widgetSelected(SelectionEvent e)
      {
        getFieldModel().getEnumFieldType().setDefault(((Combo) e.widget).getText());
      }

      public void widgetDefaultSelected(SelectionEvent e)
      {
        throw new UnsupportedOperationException();
      }
    });
    
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.horizontalSpan=2;
    this.defaultCombo.setLayoutData(gd);
    toolkit.adapt(this.defaultCombo);

    
    enumTable=toolkit.createTable(client,SWT.MULTI | SWT.H_SCROLL| SWT.V_SCROLL | SWT.FULL_SELECTION);
    gd = new GridData(GridData.FILL_BOTH);
    gd.heightHint = 120;
    gd.widthHint = 50;
    gd.horizontalSpan=3;
    enumTable.setLayoutData(gd);
    enumTable.setLinesVisible(true);
    enumTable.setEnabled(true);
    enumTable.setHeaderVisible(true);
    
    TableColumn col1 = new TableColumn(enumTable, SWT.LEFT);
    col1.setText(COLUMN_NAMES.get(0));
    col1.setWidth(100);

    TableColumn col2 = new TableColumn(enumTable, SWT.LEFT);
    col2.setText(COLUMN_NAMES.get(1));
    col2.setWidth(100); 

    
    enumViewer = new TableViewer(enumTable);
    enumViewer.setUseHashlookup(true);
    
    enumViewer.setColumnProperties(COLUMN_NAMES.toArray(new String[0]));

    // Create the cell editors
    TextCellEditor[] editors = new TextCellEditor[COLUMN_NAMES.size()];
    editors[0] = new TextCellEditor(enumTable);
    editors[1] = new TextCellEditor(enumTable);

    // Assign the cell editors to the viewer 
    enumViewer.setCellEditors(editors);
    enumViewer.setCellModifier(new EnumCellModifier(enumViewer));
    enumViewer.setLabelProvider(new EnumLabelProvider());
    enumViewer.setContentProvider(new EnumContentProvider());
    enumViewer.setInput(getFieldModel());
    
    
    // Create and configure the "Add" button
    Button add = toolkit.createButton(client,"Add", SWT.PUSH | SWT.CENTER);
    gd = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
    gd.widthHint = 80;
    add.setLayoutData(gd);
    add.addSelectionListener(new SelectionAdapter() 
    {
      public void widgetSelected(SelectionEvent e) 
      {
        getFieldModel().getEnumFieldType().addEnumValue("newValue");
        enumViewer.setSelection(new StructuredSelection("newValue"));
      }
    });

    //  Create and configure the "Delete" button
    Button delete = toolkit.createButton(client,"Delete", SWT.PUSH | SWT.CENTER);
    gd = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
    gd.widthHint = 80; 
    delete.setLayoutData(gd); 
    delete.addSelectionListener(new SelectionAdapter() 
    {
      public void widgetSelected(SelectionEvent e) 
      {
        String value = (String) ((IStructuredSelection)enumViewer.getSelection()).getFirstElement();
        if (value != null) 
        {
          getFieldModel().getEnumFieldType().removeEnumValue(value);
        }         
      }
    });
   }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.IFormPart#refresh()
   */
  public void refresh()
  {
    super.refresh();

    this.defaultCombo.setItems(getFieldModel().getEnumFieldType().getEnumValues());
    this.defaultCombo.add("", 0);

    this.defaultCombo.setText(getFieldModel().getEnumFieldType().getDefault());

    enumViewer.setInput(getFieldModel());
  }
}
