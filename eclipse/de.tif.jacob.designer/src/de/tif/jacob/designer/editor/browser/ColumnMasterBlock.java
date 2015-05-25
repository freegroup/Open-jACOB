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
package de.tif.jacob.designer.editor.browser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
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
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.BrowserFieldCalculatedModel;
import de.tif.jacob.designer.model.BrowserFieldForeignModel;
import de.tif.jacob.designer.model.BrowserFieldLocalModel;
import de.tif.jacob.designer.model.BrowserFieldModel;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class ColumnMasterBlock extends MasterDetailsBlock implements PropertyChangeListener
{
	protected final static String ICON_COLUMN  = "";
	protected final static String ORDER_COLUMN = "Order";
	protected final static String NAME_COLUMN  = "Name";
	protected final static String LABEL_COLUMN = "Label";
	protected final static String TYPE_COLUMN  = "Type";
	protected final static String[] columnNames = new String[] { ICON_COLUMN, ORDER_COLUMN,NAME_COLUMN, LABEL_COLUMN, TYPE_COLUMN};

	private ColumnsPropertyPage  page;
  private Table                  table;
  private TableViewer            viewer;
  
  private Button  addLocalButton;
  private Button  addForeignButton;
  private Button  removeButton;
  private Button  upButton;
  private Button  downButton;
  
  public ColumnMasterBlock(ColumnsPropertyPage page)
  {
    this.page = page;
  }

  protected void select(BrowserFieldModel field)
  {
    if(field==null)
      return;
    
    this.viewer.setSelection(new StructuredSelection(field), true);
    this.detailsPart.setFocus();
  }

  /**
   * @param id
   * @param title
   */
  class ContentProvider implements IStructuredContentProvider
  {
    public Object[] getElements(Object inputElement)
    {
      if (inputElement instanceof JacobBrowserEditorInput)
      {
        JacobBrowserEditorInput input = (JacobBrowserEditorInput) page.getEditor().getEditorInput();
        return input.getBrowserModel().getBrowserFieldModels().toArray();
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

  class LabelProvider extends org.eclipse.jface.viewers.LabelProvider implements ITableLabelProvider
  {
    public String getColumnText(Object obj, int columnIndex)
    {
  		String result = "";
  		BrowserFieldModel model = (BrowserFieldModel) obj;
  		switch (columnIndex) {
				case 0:
				  break;
  			case 1:
  				//result = StringUtil.toSaveString(model.getSortOrder());
  			  break;
  			case 2 :
  				result = model.getName();
  				break;
  			case 3:
  				result = model.getLabel();
  				break;
  			case 4 :
  			  if(model instanceof BrowserFieldCalculatedModel)
  			    return "calculated";
  			  if(model instanceof BrowserFieldForeignModel)
  			    return "foreign";
  			  if(model instanceof BrowserFieldLocalModel)
  			    return "local";
  				break;
  			case 5 :
  				break;
  			default :
  				break; 	
  		}
  		return result;
    }

    public Image getColumnImage(Object obj, int index)
    {
      if(index == 0 )
        return ((ObjectModel) obj).getImage();
      if(index == 1 )
        return JacobDesigner.getImage("sortorder_"+((BrowserFieldModel) obj).getSortOrder()+".png");
      return null;
    }
  }

  protected void createMasterPart(final IManagedForm managedForm, Composite parent)
  {
    final ScrolledForm form = managedForm.getForm();
    FormToolkit toolkit = managedForm.getToolkit();
    Section section = toolkit.createSection(parent, Section.DESCRIPTION);
    section.setText("Column list");
 //   section.setDescription("Select an entry to inspect column details on the right");
    section.marginWidth = 10;
    section.marginHeight = 5;
    toolkit.createCompositeSeparator(section);
    Composite mainContainer = toolkit.createComposite(section, SWT.WRAP);
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    layout.marginWidth = 2;
    layout.marginHeight = 2;
    mainContainer.setLayout(layout);
    
		this.table = toolkit.createTable(mainContainer, SWT.FULL_SELECTION | SWT.BORDER);
    table.setHeaderVisible(true);
		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(10));
		tlayout.addColumnData(new ColumnWeightData(50));
		tlayout.addColumnData(new ColumnWeightData(150));
		tlayout.addColumnData(new ColumnWeightData(150));
		tlayout.addColumnData(new ColumnWeightData(20));
		table.setLayout(tlayout);    
    GridData gd = new GridData(GridData.FILL_BOTH);
    gd.grabExcessHorizontalSpace = true;
    gd.heightHint = 100;
    gd.widthHint = 150;
    table.setLayoutData(gd);
    
    // icon column with column name
    TableColumn iconColumn = new TableColumn(table, SWT.LEFT);
    iconColumn.setText(columnNames[0]);

    // 0st column with column name
    TableColumn orderColumn = new TableColumn(table, SWT.LEFT);
    orderColumn.setText(columnNames[1]);

    // 1st column with column name
    TableColumn nameColumn = new TableColumn(table, SWT.LEFT);
    nameColumn.setText(columnNames[2]);

    // 2nd column with column type
    TableColumn typeColumn = new TableColumn(table, SWT.LEFT);
    typeColumn.setText(columnNames[3]);
    
    TableColumn lengthColumn = new TableColumn(table, SWT.LEFT);
    lengthColumn.setText(columnNames[4]);
    
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
          manager.add(new Action("Remove", Action.AS_DROP_DOWN_MENU)
          {
            public void run()
            {
              // delete all selected entries
              IStructuredSelection ssel = (IStructuredSelection) viewer.getSelection();
              Iterator iter = ssel.iterator();
              while (iter.hasNext())
              {
                BrowserFieldModel field = (BrowserFieldModel) iter.next();
                field.getBrowserModel().removeElement(field);
              }
            }
          });
        }
      }
    });
    Menu menu = menuManager.createContextMenu(table);
    table.setMenu(menu);
    
    
    // create the Button container
    //
		Composite buttonContainer = toolkit.createComposite(mainContainer);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING);
		data.horizontalSpan = 1;
		buttonContainer.setLayoutData(data);
		layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		buttonContainer.setLayout(layout);

    // add local button
    //
		addLocalButton = toolkit.createButton(buttonContainer, "Add column", SWT.PUSH);
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    addLocalButton.setLayoutData(gd);
    addLocalButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        JacobBrowserEditorInput jacobInput = (JacobBrowserEditorInput) ColumnMasterBlock.this.page.getEditorInput();
        BrowserColumnSelectionDialog dialog = new BrowserColumnSelectionDialog(ColumnMasterBlock.this.table.getShell(),jacobInput.getBrowserModel());  
        dialog.create();
        if (Window.OK == dialog.open())
        {
          Object[] results=dialog.getResult();
          for (int i = 0; results!=null&&i < results.length; i++)
          {
            page.getBrowserModel().addElement((FieldModel)results[i]);        
          }
        }
      }
    });

    // add foreign button
    //
		addForeignButton = toolkit.createButton(buttonContainer, "Add table", SWT.PUSH);
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    addForeignButton.setLayoutData(gd);
    addForeignButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        JacobBrowserEditorInput jacobInput = (JacobBrowserEditorInput) ColumnMasterBlock.this.page.getEditorInput();
        BrowserForeignColumnSelectionDialog dialog = new BrowserForeignColumnSelectionDialog(ColumnMasterBlock.this.table.getShell(),jacobInput.getBrowserModel());  
        dialog.create();
        if (Window.OK == dialog.open())
        {
          TableAliasModel model = (TableAliasModel) dialog.getFirstResult();
          
          page.getBrowserModel().addElement(model, (FieldModel)model.getFieldModels().get(0));        
        }
      }
    });

    // remove button
    //
    removeButton = toolkit.createButton(buttonContainer, "Remove", SWT.PUSH);
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    removeButton.setLayoutData(gd);
    removeButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        BrowserFieldModel fieldModel = getSelectedBrowserFieldModel();
        if(fieldModel!=null)
          fieldModel.getBrowserModel().removeElement(fieldModel);
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
        getSelectedBrowserFieldModel().getBrowserModel().upElement(getSelectedBrowserFieldModel());
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
        getSelectedBrowserFieldModel().getBrowserModel().downElement(getSelectedBrowserFieldModel());
      }
    });
    
    
    
    section.setClient(mainContainer);
    final SectionPart spart = new SectionPart(section);
    managedForm.addPart(spart);
    
    this.viewer = new TableViewer(table);
    viewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        managedForm.fireSelectionChanged(spart, event.getSelection());
      }
    });
		viewer.addPostSelectionChangedListener(new ISelectionChangedListener() 
				{
					public void selectionChanged(SelectionChangedEvent e) 
					{
					  BrowserFieldModel fieldModel = getSelectedBrowserFieldModel();
						removeButton.setEnabled(fieldModel!=null);
						if(fieldModel!=null)
						{
						  List fields = fieldModel.getBrowserModel().getBrowserFieldModels();
						  upButton.setEnabled(fields.indexOf(fieldModel)!=0);
						  downButton.setEnabled(fields.indexOf(fieldModel)<(fields.size()-1));
						}
						else
						{
						  upButton.setEnabled(false);
						  downButton.setEnabled(false);
						}
					}
				});

    viewer.setContentProvider(new ContentProvider());
    viewer.setLabelProvider(new LabelProvider());
		viewer.setUseHashlookup(true);
		viewer.setColumnProperties(columnNames);
		
		// Create the cell editors
		CellEditor[] editors = new CellEditor[columnNames.length];

		// Column 1 : is set (Checkbox)
//		editors[1] = new ComboBoxCellEditor(table,(String[])ObjectModel.SORTORDERS.toArray(new String[0]));
		editors[1] = new SortorderCellEditor(table);

		// Column 2 : Role name (Free text)
//		TextCellEditor textEditor = new TextCellEditor(table);
//		((Text) textEditor.getControl()).setTextLimit(60);
//		editors[1] = textEditor;

		// Assign the cell editors to the viewer 
		viewer.setCellEditors(editors);
		// Set the cell modifier for the viewer
		viewer.setCellModifier(new SortorderCellModifier(page.getBrowserModel()));
   
    viewer.setInput(page.getEditor().getEditorInput());
    
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
        return ((BrowserFieldModel) object);
      }

      public IDetailsPage getPage(Object key) throws RuntimeException
      {
        if (key instanceof BrowserFieldCalculatedModel)
          return new ColumnDetailsPageCalculated();
        
        if (key instanceof BrowserFieldLocalModel)
          return new ColumnDetailsPageLocal();
        
        if (key instanceof BrowserFieldForeignModel)
          return new ColumnDetailsPageForeign();
        
        throw new RuntimeException("Invalid type of browser object");
      }
    });
  }
  
  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
		if(evt.getPropertyName()==ObjectModel.PROPERTY_BROWSERCOLUMN_CHANGED)
		{
		  // refresh table
			viewer.update(evt.getNewValue(), null);
			viewer.refresh();
		}
		else if(evt.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_ADDED)
		{
		  BrowserFieldModel field = (BrowserFieldModel) evt.getNewValue();
		  viewer.setContentProvider(new ContentProvider());
			// and select entry
			select(field);
		}
		else if(evt.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_REMOVED)
		{
		  BrowserFieldModel field = (BrowserFieldModel) evt.getOldValue();		  
			viewer.remove(field);
		}
  }
  
  private BrowserFieldModel getSelectedBrowserFieldModel()
  {
    if(table.getSelectionCount()>0)
      return (BrowserFieldModel)table.getSelection()[0].getData();
    return null;
  }
  
  private void createSpacer(FormToolkit toolkit, Composite parent, int span)
  {
    Label spacer = toolkit.createLabel(parent, "");
    GridData gd = new GridData();
    gd.horizontalSpan = span;
    spacer.setLayoutData(gd);
  }
}
