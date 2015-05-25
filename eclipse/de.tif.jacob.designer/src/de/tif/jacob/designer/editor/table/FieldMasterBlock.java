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
 * Created on 08.11.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.editor.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.DeleteFieldAction;
import de.tif.jacob.designer.model.BrowserFieldModel;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.TableModel;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class FieldMasterBlock extends MasterDetailsBlock implements PropertyChangeListener
{
  private static final ViewerSorter SORTER = new ViewerSorter()
  {
    public int compare(Viewer viewer, Object e1, Object e2)
    {
      return ((FieldModel) e1).getName().compareTo(((FieldModel) e2).getName());
    }
  };

  public static final List COLUMNS=new ArrayList();

  private FormPage page;
  private Table table;
  private TableViewer viewer;
  private FieldModel currentField=null;
  
  private Button  removeButton;
  private Button  upButton;
  private Button  downButton;

  static
  {
    COLUMNS.add("Name");
    COLUMNS.add("Type");
    COLUMNS.add("Req.");
    COLUMNS.add("Read.");
    COLUMNS.add("His.");
    COLUMNS.add("Length");
  }
  public FieldMasterBlock(FormPage page)
  {
    this.page = page;
  }

  
  protected void select(FieldModel field)
  {
    if(viewer==null)
      return;
    
    if(currentField!=null)
    {
      currentField.removePropertyChangeListener(this);
      currentField.getJacobModel().removePropertyChangeListener(this);
    }
    currentField= field;

    if(currentField==null)
      return;
    
    currentField.addPropertyChangeListener(this);
    currentField.getJacobModel().addPropertyChangeListener(this);
    
    this.viewer.setSelection(new StructuredSelection(currentField), true);
    this.detailsPart.setFocus();
  }

  /**
   * @param id
   * @param title
   */
  class MasterContentProvider implements IStructuredContentProvider
  {
    public Object[] getElements(Object inputElement)
    {
      if (inputElement instanceof JacobTableEditorInput)
      {
        JacobTableEditorInput input = (JacobTableEditorInput) page.getEditor().getEditorInput();
        return input.getTableModel().getFieldModels().toArray();
      }
      return new Object[0];
    }

    public void dispose()
    {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }
  }

  class MasterLabelProvider extends LabelProvider implements ITableLabelProvider
  {
    public String getColumnText(Object obj, int columnIndex)
    {
  		String result = "";
  		FieldModel model = (FieldModel) obj;
  		switch (columnIndex) {
  			case 0:
  				result = model.getName();
  				break;
  			case 1 :
  				result = model.getType();
  				break;
  			case 2 : // Required
  				break;
  			case 3 : // Readonly
  				break;
  			case 4 : // History
  				break;
  			case 5 :
  				result = model.getLengthAsString();
  				break;
  			default :
  				break; 	
  		}
  		return result;
    }

    public Image getColumnImage(Object obj, int index)
    {
  		FieldModel model = (FieldModel) obj;
      if (index == 0)
      {
        return model.getImage();
      }
      if(index == 2) 
        return JacobDesigner.getPlugin().getImage(model.getRequired() ? "checked.gif" : "unchecked.gif");
      if(index == 3) 
        return JacobDesigner.getPlugin().getImage(model.getReadonly() ? "checked.gif" : "unchecked.gif");
      if(index == 4) 
        return JacobDesigner.getPlugin().getImage(model.getHistory() ? "checked.gif" : "unchecked.gif");
      return null;
    }
  }

  protected void createMasterPart(final IManagedForm managedForm, Composite parent)
  {
    final ScrolledForm form = managedForm.getForm();
    FormToolkit toolkit = managedForm.getToolkit();
    Section section = toolkit.createSection(parent, Section.DESCRIPTION);
    section.setText("Field list");
    section.setDescription("Select an entry to inspect field details on the right");
    section.marginWidth = 10;
    section.marginHeight = 5;
    toolkit.createCompositeSeparator(section);
    Composite client = toolkit.createComposite(section, SWT.WRAP);
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    layout.marginWidth = 2;
    layout.marginHeight = 2;
    client.setLayout(layout);
    
    this.table = toolkit.createTable(client, SWT.FULL_SELECTION | SWT.BORDER);
    table.setHeaderVisible(true);
		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(50, 120));
		tlayout.addColumnData(new ColumnWeightData(30, 100));
		tlayout.addColumnData(new ColumnWeightData(10, 40));
		tlayout.addColumnData(new ColumnWeightData(10, 40));
		tlayout.addColumnData(new ColumnWeightData(10, 40));
		tlayout.addColumnData(new ColumnWeightData(10, 60));
		table.setLayout(tlayout);    
    GridData gd = new GridData(GridData.FILL_BOTH);
    gd.grabExcessHorizontalSpace = true;
    gd.heightHint = 100;
    gd.widthHint = 150;
    table.setLayoutData(gd);
    
    // 1st column with column name
    TableColumn nameColumn = new TableColumn(table, SWT.LEFT, 0);
    nameColumn.setText((String)COLUMNS.get(0));

    // 2nd column with column type
    TableColumn typeColumn = new TableColumn(table, SWT.LEFT, 1);
    typeColumn.setText((String)COLUMNS.get(1));
    
    TableColumn requiredColumn = new TableColumn(table, SWT.CENTER, 2);
    requiredColumn.setText((String)COLUMNS.get(2));
    
    TableColumn readonlyColumn = new TableColumn(table, SWT.CENTER, 3);
    readonlyColumn.setText((String)COLUMNS.get(3));
    
    TableColumn historyColumn = new TableColumn(table, SWT.CENTER, 4);
    historyColumn.setText((String)COLUMNS.get(4));
    
    TableColumn lengthColumn = new TableColumn(table, SWT.LEFT, 5);
    lengthColumn.setText((String)COLUMNS.get(5));
    
    //
    // create context menu
    MenuManager menuManager = new MenuManager(null);
    menuManager.setRemoveAllWhenShown(true);
    menuManager.addMenuListener(new IMenuListener()
    {
      public void menuAboutToShow(IMenuManager manager)
      {
        // only add a menu, if at least one entry is selected
        IStructuredSelection ssel = (IStructuredSelection) viewer.getSelection();
        if (ssel.size() > 0)
        {
          manager.add(new Action("Delete", Action.AS_UNSPECIFIED)
          {
            public void run()
            {
              // delete all selected entries
              IStructuredSelection ssel = (IStructuredSelection) viewer.getSelection();
              Iterator iter = ssel.iterator();
              while (iter.hasNext())
              {
                final FieldModel field = (FieldModel) iter.next();
                new DeleteFieldAction()
                {
                  public FieldModel getFieldModel()
                  {
                    return field;
                  }
                }.run(null);
              }
            }
          });
        }
      }
    });
    Menu menu = menuManager.createContextMenu(table);
    table.setMenu(menu);
    
    // create Button container
		Composite buttonContainer = toolkit.createComposite(client);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING);
		data.horizontalSpan = 1;
		buttonContainer.setLayoutData(data);
		layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		buttonContainer.setLayout(layout);

		// create ADD buttons
    Button addButton = toolkit.createButton(buttonContainer, "Add", SWT.PUSH);
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    addButton.setLayoutData(gd);
    addButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        JacobTableEditorInput jacobInput = (JacobTableEditorInput) FieldMasterBlock.this.page.getEditorInput();
        jacobInput.getTableModel().createField();
      }
    });
    
    // create DELETE buttons
    removeButton = toolkit.createButton(buttonContainer, "Delete", SWT.PUSH);
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    removeButton.setLayoutData(gd);
    removeButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        JacobTableEditorInput jacobInput = (JacobTableEditorInput) FieldMasterBlock.this.page.getEditorInput();
        new DeleteFieldAction()
        {
          public FieldModel getFieldModel()
          {
            return getSelectedFieldModel();
          }
        }.run(null);
      }
    });
    
    createSpacer(toolkit, buttonContainer,1);

    // up button
    //
    upButton = toolkit.createButton(buttonContainer, "Up", SWT.PUSH);
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    upButton.setLayoutData(gd);
    upButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
      	FieldModel field= getSelectedFieldModel();
      	int index =field.getTableModel().getFieldModels().indexOf(field);
      	field.getTableModel().setFieldPosition(field,index-1);
      	select(field);
      }
    });
    
    // down button
    //
    downButton = toolkit.createButton(buttonContainer, "Down", SWT.PUSH);
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    downButton.setLayoutData(gd);
    downButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
      	FieldModel field= getSelectedFieldModel();
      	int index =field.getTableModel().getFieldModels().indexOf(field);
      	field.getTableModel().setFieldPosition(field,index+1);
      	select(field);
      }
    });
        
    section.setClient(client);
    final SectionPart spart = new SectionPart(section);
    managedForm.addPart(spart);
    
    this.viewer = new TableViewer(table);
    viewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        if(currentField!=null)
        {
          currentField.removePropertyChangeListener(FieldMasterBlock.this);
          currentField.getJacobModel().removePropertyChangeListener(FieldMasterBlock.this);
        }
        currentField = (FieldModel)((IStructuredSelection) event.getSelection()).getFirstElement();
        
        if(currentField!=null)
        {
          currentField.addPropertyChangeListener(FieldMasterBlock.this);
          currentField.getJacobModel().addPropertyChangeListener(FieldMasterBlock.this);
        }
        managedForm.getParts();
        managedForm.fireSelectionChanged(spart, event.getSelection());
      }
    });
		viewer.addPostSelectionChangedListener(new ISelectionChangedListener() 
				{
					public void selectionChanged(SelectionChangedEvent e) 
					{
					  FieldModel fieldModel = getSelectedFieldModel();

						if(fieldModel!=null)
						{
						  List fields = fieldModel.getTableModel().getFieldModels();
						  upButton.setEnabled(fields.indexOf(fieldModel)!=0);
						  downButton.setEnabled(fields.indexOf(fieldModel)<(fields.size()-1));
							removeButton.setEnabled(true);
						}
						else
						{
						  upButton.setEnabled(false);
						  downButton.setEnabled(false);
							removeButton.setEnabled(false);
						}
					}
				});


		viewer.setContentProvider(new MasterContentProvider());
    viewer.setLabelProvider(new MasterLabelProvider());
//    viewer.setSorter(SORTER);
    viewer.setInput(page.getEditor().getEditorInput());
    viewer.setColumnProperties((String[])COLUMNS.toArray(new String[0]));
    
		// Create the cell editors
		CellEditor[] editors = new CellEditor[COLUMNS.size()];

		// Column 0 : Field name
//		editors[0] = new TextCellEditor(table);
//		editors[0].setStyle(SWT.READ_ONLY);

		// Column 1 : Type
//		editors[1] = new TextCellEditor(table);
//		editors[1].setStyle(SWT.READ_ONLY);
		
		// Column 2 : Required
		editors[2] = new CheckboxCellEditor(table);
		
		// Column 3 : Readonly
		editors[3] = new CheckboxCellEditor(table);

		// Column 4 : History
		editors[4] = new CheckboxCellEditor(table);
		

		// Column 4 : Length
//		editors[5] = new TextCellEditor(table);
//		editors[5].setStyle(SWT.READ_ONLY);

		// Assign the cell editors to the viewer 
		viewer.setCellEditors(editors);
		// Set the cell modifier for the viewer
		viewer.setCellModifier(new FieldCellModifier());

		nameColumn.pack();
  }

  protected void createToolBarActions(IManagedForm managedForm)
  {
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.forms.MasterDetailsBlock#registerPages(org.eclipse.ui.forms.DetailsPart)
   */
  protected void registerPages(DetailsPart detailsPart)
  {
    detailsPart.setPageProvider(new IDetailsPageProvider()
    {
      public Object getPageKey(Object object)
      {
        return ((FieldModel) object).getType();
      }

      public IDetailsPage getPage(Object key)
      {
        if (FieldModel.DBTYPE_LONGTEXT.equals(key))
          return new FieldDetailsPageLongtext();
        if (FieldModel.DBTYPE_TEXT.equals(key))
          return new FieldDetailsPageText();
        if (FieldModel.DBTYPE_ENUM.equals(key))
          return new FieldDetailsPageEnum();
        if (FieldModel.DBTYPE_TIMESTAMP.equals(key))
          return new FieldDetailsPageTimestamp();
        if (FieldModel.DBTYPE_LONG.equals(key))
          return new FieldDetailsPageLong();
        if (FieldModel.DBTYPE_INTEGER.equals(key))
          return new FieldDetailsPageInteger();
        if (FieldModel.DBTYPE_FLOAT.equals(key))
          return new FieldDetailsPageFloat();
        if (FieldModel.DBTYPE_DOUBLE.equals(key))
          return new FieldDetailsPageDouble();
        if (FieldModel.DBTYPE_DECIMAL.equals(key))
          return new FieldDetailsPageDecimal();
        if (FieldModel.DBTYPE_DOCUMENT.equals(key))
          return new FieldDetailsPageDocument();
        if (FieldModel.DBTYPE_BOOLEAN.equals(key))
          return new FieldDetailsPageBoolean();
        
        return new FieldDetailsPageCommon();
      }
    });
  }
  
  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    if(viewer==null)
      return;
		if(evt.getPropertyName()==ObjectModel.PROPERTY_FIELD_CHANGED)
		{
			viewer.refresh();
		}
		else if(evt.getPropertyName()==ObjectModel.PROPERTY_I18NKEY_CHANGED)
		{
		  // select again to switch detailed page
		  viewer.setSelection(viewer.getSelection(), true);
		}
		else if(evt.getPropertyName()==ObjectModel.PROPERTY_FIELD_ADDED && evt.getSource()==getTableModel())
		{
		  FieldModel field = (FieldModel) evt.getNewValue();
		  
			viewer.update(field, null);
			viewer.refresh();
			
			// and select entry
			select(field);
		}
		else if(evt.getPropertyName()==ObjectModel.PROPERTY_TABLE_CHANGED && evt.getSource()==getTableModel())
		{
			viewer.refresh();
			
		}
		else if(evt.getPropertyName()==ObjectModel.PROPERTY_FIELD_DELETED)
		{
		  FieldModel field = (FieldModel) evt.getOldValue();		  
			viewer.remove(field);
		}
		else if(evt.getPropertyName()==ObjectModel.PROPERTY_FIELD_TYPE_CHANGED)
		{
		  // check whether the type of the selected field has changed
	    IStructuredSelection ssel = (IStructuredSelection) viewer.getSelection();
			if (ssel.size()==1 && ssel.getFirstElement().equals(evt.getSource()))
			{
			  // select again to switch detailed page
			  viewer.setSelection(viewer.getSelection(), true);
			}
		}
  }
  
  private FieldModel getSelectedFieldModel()
  {
    if(table.getSelectionCount()>0)
      return (FieldModel)table.getSelection()[0].getData();
    return null;
  }
  
  private TableModel getTableModel()
  {
    JacobTableEditorInput input = (JacobTableEditorInput) page.getEditor().getEditorInput();
    return input.getTableModel();
  }

  private void createSpacer(FormToolkit toolkit, Composite parent, int span)
  {
    Label spacer = toolkit.createLabel(parent, "");
    GridData gd = new GridData();
    gd.horizontalSpan = span;
    spacer.setLayoutData(gd);
  }

}
