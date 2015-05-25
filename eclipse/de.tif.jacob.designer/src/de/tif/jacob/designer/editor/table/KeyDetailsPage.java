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
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.KeyModel;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class KeyDetailsPage implements IDetailsPage, PropertyChangeListener
{
  private IManagedForm managedForm;
  private KeyModel input;

  private Text nameText;
  private TableViewer viewer;
  private TableColumn fieldColumn;
  private Button upButton;
  private Button downButton;
  private Table table;

  public KeyDetailsPage()
  {
  }

  protected KeyModel getKeyModel()
  {
    return this.input;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.IDetailsPage#initialize(org.eclipse.ui.forms.IManagedForm)
   */
  public void initialize(IManagedForm mform)
  {
    this.managedForm = mform;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.IDetailsPage#createContents(org.eclipse.swt.widgets.Composite)
   */
  public void createContents(Composite parent)
  {
    TableWrapLayout layout = new TableWrapLayout();
    layout.topMargin = 5;
    layout.leftMargin = 5;
    layout.rightMargin = 2;
    layout.bottomMargin = 2;
    parent.setLayout(layout);

    FormToolkit toolkit = managedForm.getToolkit();
    createCommonContents(toolkit, parent);
    createDetailContents(toolkit, parent);
  }

  protected void createDetailContents(FormToolkit toolkit, Composite parent)
  {
    // could be used for properties depending on key type in future
  }

  private void createCommonContents(FormToolkit toolkit, Composite parent)
  {
    Section commonSection = toolkit.createSection(parent, Section.DESCRIPTION);
    commonSection.marginWidth = 10;
    commonSection.setText("Key details");
    commonSection.setDescription("Set the properties of the selected key.");

    TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
    td.grabHorizontal = true;
    commonSection.setLayoutData(td);

    toolkit.createCompositeSeparator(commonSection);

    Composite client = toolkit.createComposite(commonSection);
    commonSection.setClient(client);

    GridLayout glayout = new GridLayout();
    glayout.marginWidth = glayout.marginHeight = 0;
    glayout.numColumns = 3;
    client.setLayout(glayout);

    //
    // create name text field
    toolkit.createLabel(client, "Name:");
    this.nameText = toolkit.createText(client, null, SWT.SINGLE | SWT.BORDER);
    this.nameText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        getKeyModel().setName(nameText.getText());
      }
    });
    GridData textGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    textGridData.widthHint = 10;
    this.nameText.setLayoutData(textGridData);

    // dummy label to switch to next row
    toolkit.createLabel(client, "");

    //
    // create key fields label and list
    Label label = toolkit.createLabel(client, "Key fields:");
    label.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

    this.table = toolkit.createTable(client, SWT.FULL_SELECTION | SWT.BORDER);
    TableLayout tlayout = new TableLayout();
    tlayout.addColumnData(new ColumnWeightData(100));
    this.table.setLayout(tlayout);
    GridData gd = new GridData(GridData.FILL_BOTH);
    gd.heightHint = 80;
    gd.widthHint = 50;
    this.table.setLayoutData(gd);
    this.fieldColumn = new TableColumn(this.table, SWT.LEFT, 0);

    createButtons(toolkit, client);

    this.viewer = new TableViewer(table);
    viewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        if (event.getSelection().isEmpty())
        {
          KeyDetailsPage.this.upButton.setEnabled(false);
          KeyDetailsPage.this.downButton.setEnabled(false);
        }
        else
        {
          KeyDetailsPage.this.upButton.setEnabled(KeyDetailsPage.this.table.getSelectionIndex() != 0);
          KeyDetailsPage.this.downButton.setEnabled(KeyDetailsPage.this.table.getSelectionIndex() + 1 != KeyDetailsPage.this.table.getItemCount());
        }
      }
    });
    viewer.setContentProvider(new IStructuredContentProvider()
    {
      public Object[] getElements(Object inputElement)
      {
        return (String[]) inputElement;
      }

      public void dispose()
      {
      }

      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }
    });
    viewer.setLabelProvider(new LabelProvider());
    
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
          manager.add(new Action("Delete", Action.AS_DROP_DOWN_MENU)
          {
            public void run()
            {
              getKeyModel().removeField(table.getSelectionIndex());
            }
          });
        }
      }
    });
    Menu menu = menuManager.createContextMenu(table);
    this.table.setMenu(menu);    
  }

  private void createButtons(FormToolkit toolkit, Composite parent)
  {
    Composite buttons = toolkit.createComposite(parent);
    GridLayout glayout = new GridLayout();
    glayout.marginWidth = glayout.marginHeight = 0;
    glayout.numColumns = 1;
    buttons.setLayout(glayout);

    GridData gd = new GridData(GridData.FILL_HORIZONTAL);

    //
    // create add button
    Button addButton = toolkit.createButton(buttons, "Add...", SWT.PUSH);
    addButton.setLayoutData(gd);
    addButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        TableFieldSelectionDialog dialog = new TableFieldSelectionDialog(KeyDetailsPage.this.table.getShell(), getKeyModel().getTableModel());
        dialog.filter(getKeyModel().getFields());
        dialog.create();
        if (Window.OK == dialog.open())
        {
          FieldModel model = (FieldModel) dialog.getFirstResult();
          getKeyModel().addElement(model);
        }
      }
    });

    //
    // create up button
    this.upButton = toolkit.createButton(buttons, "Up", SWT.PUSH);
    this.upButton.setLayoutData(gd);
    this.upButton.setEnabled(false);
    this.upButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        String field = getKeyModel().upField(KeyDetailsPage.this.table.getSelectionIndex());
        KeyDetailsPage.this.viewer.setSelection(new StructuredSelection(field), true);
      }
    });

    //
    // create down button
    this.downButton = toolkit.createButton(buttons, "Down", SWT.PUSH);
    this.downButton.setLayoutData(gd);
    this.downButton.setEnabled(false);
    this.downButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        String field = getKeyModel().downField(KeyDetailsPage.this.table.getSelectionIndex());
        KeyDetailsPage.this.viewer.setSelection(new StructuredSelection(field), true);
      }
    });
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.IDetailsPage#inputChanged(org.eclipse.jface.viewers.IStructuredSelection)
   */
  public void selectionChanged(IFormPart part, ISelection selection)
  {
    unregister();

    IStructuredSelection ssel = (IStructuredSelection) selection;
    if (ssel.size() == 1)
    {
      input = (KeyModel) ssel.getFirstElement();
      this.input.addPropertyChangeListener(this);
    }
    else
      throw new IllegalStateException();
    refresh();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.IDetailsPage#commit()
   */
  public void commit(boolean onSave)
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.IDetailsPage#setFocus()
   */
  public void setFocus()
  {
    this.nameText.setFocus();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.IDetailsPage#dispose()
   */
  public void dispose()
  {
    unregister();
  }

  private void unregister()
  {
    if (this.input != null)
    {
      this.input.removePropertyChangeListener(this);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.IDetailsPage#isDirty()
   */
  public boolean isDirty()
  {
    return false;
  }

  public boolean isStale()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.forms.IDetailsPage#refresh()
   */
  public void refresh()
  {
    if (getKeyModel() == null)
      throw new IllegalStateException();

    if (!getKeyModel().getName().equals(this.nameText.getText()))
      this.nameText.setText(getKeyModel().getName());

    this.viewer.setInput(getKeyModel().getFields());
    this.fieldColumn.pack();
  }

  public boolean setFormInput(Object input)
  {
    return false;
  }

  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    refresh();
  }
}
