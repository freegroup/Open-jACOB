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
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
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
import de.tif.jacob.designer.actions.DeleteKeyAction;
import de.tif.jacob.designer.model.KeyModel;
import de.tif.jacob.designer.model.TableModel;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class KeyMasterBlock extends MasterDetailsBlock implements PropertyChangeListener
{
  private static final ViewerSorter SORTER = new ViewerSorter()
  {
    public int compare(Viewer viewer, Object e1, Object e2)
    {
      return ((KeyModel) e1).getName().compareTo(((KeyModel) e2).getName());
    }
  };

  private FormPage page;
  private Table table;
  private TableViewer viewer;
  
  private Button primaryButton;
  private Button foreignButton;
  private Button uniqueButton;
  private Button indexButton;
  
  public KeyMasterBlock(FormPage page)
  {
    this.page = page;
  }

  protected void select(KeyModel key)
  {
    if(key==null)
    {
      if(viewer!=null)
        viewer.setSelection(null);
      return;
    }
    this.viewer.setSelection(new StructuredSelection(key), true);
    this.detailsPart.setFocus();
  }

  protected void updateButtons(TableModel table)
  {
    this.primaryButton.setEnabled(!table.hasPrimaryKey());
  }

  /**
   * @param id
   * @param title
   */
  class MasterContentProvider implements IStructuredContentProvider
  {
    public Object[] getElements(Object inputElement)
    {
//      if (inputElement instanceof JacobTableEditorInput)
//      {
        JacobTableEditorInput input = (JacobTableEditorInput) page.getEditor().getEditorInput();
        return input.getTableModel().getKeyModels().toArray();
//      }
//      return new Object[0];
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
  		KeyModel model = (KeyModel) obj;
  		switch (columnIndex) {
  			case 0:
  				result = model.getName();
  				break;
  			case 1 :
  				result = model.getType();
  				break;
  			default :
  				break; 	
  		}
  		return result;
    }

    public Image getColumnImage(Object obj, int index)
    {
      return index == 0 ? ((KeyModel)obj).getImage(): null;
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.forms.MasterDetailsBlock#createMasterPart(org.eclipse.ui.forms.IManagedForm, org.eclipse.swt.widgets.Composite)
   */
  protected void createMasterPart(final IManagedForm managedForm, Composite parent)
  {
    final ScrolledForm form = managedForm.getForm();
    FormToolkit toolkit = managedForm.getToolkit();
    Section section = toolkit.createSection(parent, Section.DESCRIPTION);
    section.setText("Key list");
    section.setDescription("Select an entry to inspect key details on the right");
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
		tlayout.addColumnData(new ColumnWeightData(60, 120));
		tlayout.addColumnData(new ColumnWeightData(40, 100));
		table.setLayout(tlayout);    
    GridData gd = new GridData(GridData.FILL_BOTH);
    table.setLayoutData(gd);
    
    // 1st column with column name
    TableColumn nameColumn = new TableColumn(table, SWT.LEFT, 0);
    nameColumn.setText("Name");

    // 2nd column with column type
    TableColumn column = new TableColumn(table, SWT.LEFT, 1);
    column.setText("Type");
    
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
                final KeyModel key = (KeyModel) iter.next();
                new DeleteKeyAction()
                {
                  public KeyModel getKeyModel()
                  {
                    return key;
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
    
    // create the Button container
    //
		Composite buttonContainer = toolkit.createComposite(mainContainer);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING);
		data.horizontalSpan = 1;
		buttonContainer.setLayoutData(data);
		layout = new GridLayout();
		buttonContainer.setLayout(layout);

    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL|GridData.GRAB_HORIZONTAL);
		// Add a primary key
    // 
    primaryButton = toolkit.createButton(buttonContainer, "Add Primary", SWT.PUSH);
    primaryButton.setLayoutData(gd);
    primaryButton.setEnabled(false);
    primaryButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        JacobTableEditorInput jacobInput = (JacobTableEditorInput) KeyMasterBlock.this.page.getEditorInput();
        jacobInput.getTableModel().createPrimaryKey();
      }
    });

    // Add a foreign key
    //
    foreignButton = toolkit.createButton(buttonContainer, "Add Foreign", SWT.PUSH);
    foreignButton.setLayoutData(gd);
    foreignButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        JacobTableEditorInput jacobInput = (JacobTableEditorInput) KeyMasterBlock.this.page.getEditorInput();
        jacobInput.getTableModel().createForeignKey();
      }
    });

    // Add a unique key
    //
    uniqueButton = toolkit.createButton(buttonContainer, "Add Unique", SWT.PUSH);
    uniqueButton.setLayoutData(gd);
    uniqueButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        JacobTableEditorInput jacobInput = (JacobTableEditorInput) KeyMasterBlock.this.page.getEditorInput();
        jacobInput.getTableModel().createUniqueKey();
      }
    });

    // add an index
    //
    indexButton = toolkit.createButton(buttonContainer, "Add Index", SWT.PUSH);
    indexButton.setLayoutData(gd);
    indexButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        JacobTableEditorInput jacobInput = (JacobTableEditorInput) KeyMasterBlock.this.page.getEditorInput();
        jacobInput.getTableModel().createIndexKey();
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
    viewer.setContentProvider(new MasterContentProvider());
    viewer.setLabelProvider(new MasterLabelProvider());
    viewer.setSorter(SORTER);
    viewer.setInput(page.getEditor().getEditorInput());
    
    nameColumn.pack();
  }
  /* (non-Javadoc)
   * @see org.eclipse.ui.forms.MasterDetailsBlock#createToolBarActions(org.eclipse.ui.forms.IManagedForm)
   */
  protected void createToolBarActions(IManagedForm managedForm)
  {
  }

  protected void registerPages(DetailsPart detailsPart)
  {
    detailsPart.setPageProvider(new IDetailsPageProvider()
    {
      public Object getPageKey(Object object)
      {
        return ((KeyModel) object).getType();
      }

      public IDetailsPage getPage(Object key)
      {
        return new KeyDetailsPage();
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
    
		if(evt.getPropertyName()==TableModel.PROPERTY_KEY_CHANGED)
		{
		  // refresh table
			viewer.update(evt.getNewValue(), null);
			viewer.refresh(true);
		}
    // Es wurde ein Feld aus der Tabelle entfern => kann auswirkung auf einen key haben
    else if(evt.getPropertyName()==TableModel.PROPERTY_FIELD_DELETED)
    {
      // alle inhalte refreshen
      viewer.update(((IStructuredContentProvider)viewer.getContentProvider()).getElements(null),null);
      viewer.refresh(true);
      // und den Detailview aktualisieren
      detailsPart.refresh();
    }
		else if(evt.getPropertyName()==TableModel.PROPERTY_KEY_ADDED)
		{
		  KeyModel key = (KeyModel) evt.getNewValue();
		  
		  // refresh table
			viewer.update(key, null);
			viewer.refresh();
			
			updateButtons(key.getTableModel());
			
			// and select entry
			select(key);
		}
		else if(evt.getPropertyName()==TableModel.PROPERTY_KEY_DELETED)
		{
		  KeyModel key = (KeyModel) evt.getNewValue();
		  
			updateButtons(key.getTableModel());
			
			viewer.remove(key);
		}
  }
}