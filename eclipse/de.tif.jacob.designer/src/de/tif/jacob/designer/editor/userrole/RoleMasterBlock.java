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
package de.tif.jacob.designer.editor.userrole;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import de.tif.jacob.designer.actions.NewUserRoleAction;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UserRoleModel;


/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class RoleMasterBlock extends MasterDetailsBlock implements PropertyChangeListener
{
  private RolePropertiesPage page;
  private Table              table;
  private TableViewer        viewer;
  
  private Button  addButton;
  private Button  removeButton;
  private UserRoleModel currentRole;
  
  public RoleMasterBlock(RolePropertiesPage page)
  {
    this.page = page;
  }

  protected void select(UserRoleModel role)
  {
    if(viewer==null)
      return;
    
    if(currentRole!=null)
      currentRole.removePropertyChangeListener(this);
    
    if(role==null)
      return;
    
    currentRole= role;

    currentRole.addPropertyChangeListener(this);
    
    this.viewer.setSelection(new StructuredSelection(role), true);
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
      if (inputElement instanceof RoleEditorInput)
      {
        RoleEditorInput input = (RoleEditorInput) page.getEditor().getEditorInput();
        return input.getJacobModel().getUserRoleModels().toArray();
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
  		UserRoleModel model = (UserRoleModel) obj;
  		return model.getName();
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
    section.setDescription("Select an entry to inspect the user role details on the right");
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
    nameColumn.setText("Name");

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

    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    // add local button
    //
    addButton = toolkit.createButton(buttonContainer, "Add", SWT.PUSH);
    addButton.setLayoutData(gd);
    addButton.addSelectionListener(new SelectionAdapter()
        {
          public void widgetSelected(SelectionEvent e)
          {
            new NewUserRoleAction()
            {
              public JacobModel getJacobModel()
              {
                return page.getJacobModel();
              }

              protected String getNewRoleName()
              {
                return "defaultName";
              }
            }.run(null);
          }
        });

    // remove button
    //
    removeButton = toolkit.createButton(buttonContainer, "Remove", SWT.PUSH);
    removeButton.setLayoutData(gd);
    removeButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        UserRoleModel roleModel = getSelectedUserRoleModel();
        if(roleModel!=null)
          roleModel.getJacobModel().removeElement(roleModel);
      }
    });
    
    createSpacer(toolkit, buttonContainer,1);
    
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
					  UserRoleModel fieldModel = getSelectedUserRoleModel();
						removeButton.setEnabled(fieldModel!=null);
					}
				});

    viewer.setContentProvider(new ContentProvider());
    viewer.setLabelProvider(new LabelProvider());
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
        return ((UserRoleModel) object);
      }

      public IDetailsPage getPage(Object key) throws RuntimeException
      {
        return new RoleDetailsPage();
      }
    });
  }
  
  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
		if(evt.getPropertyName()==ObjectModel.PROPERTY_USERROLE_CREATED)
		{
		  UserRoleModel field = (UserRoleModel) evt.getNewValue();
		  viewer.setContentProvider(new ContentProvider());
			select(field);
		}
		else if(evt.getPropertyName()==ObjectModel.PROPERTY_USERROLE_CHANGED)
		{
		  viewer.refresh(true);
		}
		else if(evt.getPropertyName()==ObjectModel.PROPERTY_USERROLE_DELETED)
		{
		  UserRoleModel field = (UserRoleModel) evt.getOldValue();		  
			viewer.remove(field);
		}
  }
  
  private UserRoleModel getSelectedUserRoleModel()
  {
    if(table.getSelectionCount()>0)
      return (UserRoleModel)table.getSelection()[0].getData();
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
