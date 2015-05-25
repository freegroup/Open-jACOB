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
package de.tif.jacob.designer.editor.contextmenu;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import de.tif.jacob.designer.model.ContextMenuEntryModel;
import de.tif.jacob.designer.model.ObjectModel;


/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class MenuMasterBlock extends MasterDetailsBlock implements PropertyChangeListener
{
  private ContextMenuEntryPropertiesPage  page;
  private Table                  table;
  private TableViewer            viewer;
  
  private Button  addLocalButton;
//  private Button  addForeignButton;
  private Button  removeButton;
  private Button  upButton;
  private Button  downButton;
  
  public MenuMasterBlock(ContextMenuEntryPropertiesPage page)
  {
    this.page = page;
  }

  protected void select(ContextMenuEntryModel field)
  {
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
      if (inputElement instanceof ContextMenuEditorInput)
      {
        ContextMenuEditorInput input = (ContextMenuEditorInput) page.getEditor().getEditorInput();
        return input.getGroupModel().getContextMenuEntries().toArray();
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
  		ContextMenuEntryModel model = (ContextMenuEntryModel) obj;
  		switch (columnIndex) {
  			case 0:
  				result = model.getLabel();
  				break;
  			case 1 :
  				result = model.getName();
  				break;
  			case 2 :
 			    result = model.getAction();
  				break;
  			case 3 :
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

  protected void createMasterPart(final IManagedForm managedForm, Composite parent)
  {
    final ScrolledForm form = managedForm.getForm();
    FormToolkit toolkit = managedForm.getToolkit();
    Section section = toolkit.createSection(parent, Section.DESCRIPTION);
    section.setDescription("Select an entry to inspect context menu details on the right");
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
		tlayout.addColumnData(new ColumnWeightData(50, 130));
		tlayout.addColumnData(new ColumnWeightData(30, 130));
		tlayout.addColumnData(new ColumnWeightData(10, 40));
		table.setLayout(tlayout);    
    GridData gd = new GridData(GridData.FILL_BOTH);
    gd.grabExcessHorizontalSpace = true;
    gd.heightHint = 100;
    gd.widthHint = 150;
    table.setLayoutData(gd);
    
    // 1st column with column name
    TableColumn nameColumn = new TableColumn(table, SWT.LEFT, 0);
    nameColumn.setText("Label");

    // 2nd column with column type
    TableColumn typeColumn = new TableColumn(table, SWT.LEFT, 1);
    typeColumn.setText("Name");
    
    TableColumn lengthColumn = new TableColumn(table, SWT.LEFT, 2);
    lengthColumn.setText("Type");
    
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
		createActionButton(toolkit, buttonContainer, "Add Generic", ObjectModel.ACTION_GENERIC);
		createActionButton(toolkit, buttonContainer, "Add Selected", ObjectModel.ACTION_SELECTED);
		createActionButton(toolkit, buttonContainer, "Add Search", ObjectModel.ACTION_SEARCH);
		createActionButton(toolkit, buttonContainer, "Add Local Search", ObjectModel.ACTION_LOCALSEARCH);
		createActionButton(toolkit, buttonContainer, "Add Clear", ObjectModel.ACTION_CLEARGROUP);
		createActionButton(toolkit, buttonContainer, "Add Delete", ObjectModel.ACTION_DELETERECORD);
		createActionButton(toolkit, buttonContainer, "Add New", ObjectModel.ACTION_NEWRECORD);
		createActionButton(toolkit, buttonContainer, "Add Update", ObjectModel.ACTION_UPDATERECORD);


    createSpacer(toolkit, buttonContainer,1);

    // remove button
    //
    removeButton = toolkit.createButton(buttonContainer, "Remove", SWT.PUSH);
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    removeButton.setLayoutData(gd);
    removeButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        ContextMenuEntryModel fieldModel = getSelectedContextMenuEntryModel();
        if(fieldModel!=null)
          fieldModel.getGroupModel().removeElement(fieldModel);
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
        getSelectedContextMenuEntryModel().getGroupModel().upElement(getSelectedContextMenuEntryModel());
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
        getSelectedContextMenuEntryModel().getGroupModel().downElement(getSelectedContextMenuEntryModel());
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
					  ContextMenuEntryModel fieldModel = getSelectedContextMenuEntryModel();
						removeButton.setEnabled(fieldModel!=null);
						if(fieldModel!=null)
						{
						  List fields = fieldModel.getGroupModel().getContextMenuEntries();
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
    viewer.setInput(page.getEditor().getEditorInput());
    
    nameColumn.pack();
  }

  protected void createActionButton(FormToolkit toolkit,Composite buttonContainer, String label, final String actionType)
  {
		addLocalButton = toolkit.createButton(buttonContainer, label, SWT.PUSH);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    addLocalButton.setLayoutData(gd);
    addLocalButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        ContextMenuEntryModel action = new ContextMenuEntryModel(page.getGroupModel().getJacobModel(),page.getGroupModel(),actionType);
        page.getGroupModel().addElement(action);        
      }
    });
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
        return ((ContextMenuEntryModel) object);
      }

      public IDetailsPage getPage(Object key) throws RuntimeException
      {
        ContextMenuEntryModel model =(ContextMenuEntryModel)key;
        if(model.getAction()==ObjectModel.ACTION_CLEARGROUP)
          return new ContextMenuDetailsPage_Clear();
        if(model.getAction()==ObjectModel.ACTION_DELETERECORD)
          return new ContextMenuDetailsPage_Delete();
        if(model.getAction()==ObjectModel.ACTION_GENERIC)
          return new ContextMenuDetailsPage_Generic();
        if(model.getAction()==ObjectModel.ACTION_NEWRECORD)
          return new ContextMenuDetailsPage_New();
        if(model.getAction()==ObjectModel.ACTION_SEARCH)
          return new ContextMenuDetailsPage_Search();
        if(model.getAction()==ObjectModel.ACTION_LOCALSEARCH)
          return new ContextMenuDetailsPage_LocalSearch();
        if(model.getAction()==ObjectModel.ACTION_SELECTED)
          return new ContextMenuDetailsPage_Selected();
        if(model.getAction()==ObjectModel.ACTION_UPDATERECORD)
          return new ContextMenuDetailsPage_Update();

        throw new IllegalStateException("Action type ["+model.getAction()+"] is unknown.");
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
		else if(evt.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_ADDED && evt.getNewValue() instanceof ContextMenuEntryModel)
		{
		  ContextMenuEntryModel field = (ContextMenuEntryModel) evt.getNewValue();
		  viewer.setContentProvider(new ContentProvider());
			// and select entry
			select(field);
		}
		else if(evt.getPropertyName()==ObjectModel.PROPERTY_ELEMENT_REMOVED && evt.getOldValue() instanceof ContextMenuEntryModel)
		{
		  ContextMenuEntryModel field = (ContextMenuEntryModel) evt.getOldValue();		  
			viewer.remove(field);
		}
  }
  
  private ContextMenuEntryModel getSelectedContextMenuEntryModel()
  {
    if(table.getSelectionCount()>0)
      return (ContextMenuEntryModel)table.getSelection()[0].getData();
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
