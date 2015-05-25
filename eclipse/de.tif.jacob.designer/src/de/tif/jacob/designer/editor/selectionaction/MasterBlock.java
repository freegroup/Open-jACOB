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
package de.tif.jacob.designer.editor.selectionaction;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.actions.OpenNewClassWizardAction;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.ApplicationSelectionActionModel;
import de.tif.jacob.designer.model.EngineSelectionActionModel;
import de.tif.jacob.designer.model.ISelectionActionProvider;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.SelectionActionModel;
import de.tif.jacob.designer.model.UIDBInformBrowserModel;
import de.tif.jacob.designer.util.ClassFinder;
import de.tif.jacob.screen.ISelectionAction;
import de.tif.jacob.screen.impl.GlobalMultipleUpdateSelectionAction;
import de.tif.jacob.screen.impl.IsolatedMultipleUpdateSelectionAction;
import de.tif.jacob.screen.impl.SelectionActionDelete;
import de.tif.jacob.screen.impl.SelectionActionDeleteAllOrNothing;
import de.tif.jacob.selectionactions.plugin.ISelectionActionPlugin;
import de.tif.jacob.selectionactions.plugin.SelectionActionPluginManager;
import de.tif.jacob.util.clazz.ClassUtil;
/**
 * @author Andreas
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class MasterBlock extends MasterDetailsBlock implements PropertyChangeListener
{
  private ActionPropertiesPage page;
  private Table table;
  private TableViewer viewer;
  /*
   * private Button addDeleteButton; private Button addIsolatedUpdateButton;
   * private Button addGlobalUpdateButton; private Button addGenericButton;
   */
  private Button removeButton;
  private Button upButton;
  private Button downButton;

  public MasterBlock(ActionPropertiesPage page)
  {
    this.page = page;
  }

  protected void select(SelectionActionModel action)
  {
    this.viewer.setSelection(new StructuredSelection(action), true);
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
      if (inputElement instanceof EditorInput)
      {
        EditorInput input = (EditorInput) page.getEditor().getEditorInput();
        return input.getActionProvider().getActions().toArray();
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
      SelectionActionModel model = (SelectionActionModel) obj;
      switch (columnIndex)
      {
      case 0:
        result = ClassUtil.getShortClassName(model.getHookClassName());
        break;
      default:
        break;
      }
      return result;
    }

    public Image getColumnImage(Object obj, int index)
    {
      return JacobDesigner.getImage("editor_java.png");
    }
  }

  protected void createMasterPart(final IManagedForm managedForm, Composite parent)
  {
    EditorInput input = (EditorInput) page.getEditor().getEditorInput();
    boolean informbrowser = (input.getActionProvider() instanceof UIDBInformBrowserModel);
    FormToolkit toolkit = managedForm.getToolkit();
    Section section = toolkit.createSection(parent, Section.DESCRIPTION);
    section.setDescription("Select an entry to inspect details on the right");
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
    table.setLayout(tlayout);
    GridData gd = new GridData(GridData.FILL_BOTH);
    gd.grabExcessHorizontalSpace = true;
    gd.heightHint = 100;
    gd.widthHint = 150;
    table.setLayoutData(gd);
    // 1st column with column name
    TableColumn nameColumn = new TableColumn(table, SWT.LEFT, 0);
    nameColumn.setText("Java Class");
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
    // add searchbrowser specific buttons
    //
    if (!informbrowser)
    {
      Button addIsolatedDeleteButton = toolkit.createButton(buttonContainer, "Delete Action (iso)", SWT.PUSH);
      gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
      addIsolatedDeleteButton.setLayoutData(gd);
      addIsolatedDeleteButton.addSelectionListener(new SelectionAdapter()
      {
        public void widgetSelected(SelectionEvent e)
        {
          EditorInput input = (EditorInput) page.getEditor().getEditorInput();
          input.getActionProvider().addElement(SelectionActionModel.createModelObject(input.getActionProvider(), SelectionActionDelete.class.getName()));
        }
      });
      
      Button addDeleteButton = toolkit.createButton(buttonContainer, "Delete Action (all)", SWT.PUSH);
      gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
      addDeleteButton.setLayoutData(gd);
      addDeleteButton.addSelectionListener(new SelectionAdapter()
      {
        public void widgetSelected(SelectionEvent e)
        {
          EditorInput input = (EditorInput) page.getEditor().getEditorInput();
          input.getActionProvider().addElement(SelectionActionModel.createModelObject(input.getActionProvider(), SelectionActionDeleteAllOrNothing.class.getName()));
        }
      });
      
      Button addIsolatedUpdateButton = toolkit.createButton(buttonContainer, "Update Action (iso)", SWT.PUSH);
      gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
      addIsolatedUpdateButton.setLayoutData(gd);
      addIsolatedUpdateButton.addSelectionListener(new SelectionAdapter()
      {
        public void widgetSelected(SelectionEvent e)
        {
          EditorInput input = (EditorInput) page.getEditor().getEditorInput();
          input.getActionProvider().addElement(SelectionActionModel.createModelObject(input.getActionProvider(), IsolatedMultipleUpdateSelectionAction.class.getName()));
        }
      });
      
      Button addGlobalUpdateButton = toolkit.createButton(buttonContainer, "Update Action (all)", SWT.PUSH);
      gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
      addGlobalUpdateButton.setLayoutData(gd);
      addGlobalUpdateButton.addSelectionListener(new SelectionAdapter()
      {
        public void widgetSelected(SelectionEvent e)
        {
          EditorInput input = (EditorInput) page.getEditor().getEditorInput();
          input.getActionProvider().addElement(SelectionActionModel.createModelObject(input.getActionProvider(), GlobalMultipleUpdateSelectionAction.class.getName()));
        }
      });
      
    }
    if (informbrowser)
    {
      Collection<ISelectionActionPlugin> plugins = SelectionActionPluginManager.getPlugins(SelectionActionPluginManager.Type.informbrowser);
      for (ISelectionActionPlugin plugin : plugins)
      {
        // generic action button
        //
        Button addGenericButton = toolkit.createButton(buttonContainer, plugin.getLabel(), SWT.PUSH);
        gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        addGenericButton.setLayoutData(gd);
        addGenericButton.setData("plugin", plugin);
        addGenericButton.addSelectionListener(new SelectionAdapter()
        {
          public void widgetSelected(SelectionEvent e)
          {
            ISelectionActionPlugin plugin = (ISelectionActionPlugin) ((Button) e.getSource()).getData("plugin");
            try
            {
              plugin.checkResources();
              EditorInput input = (EditorInput) page.getEditor().getEditorInput();
              SelectionActionModel model = SelectionActionModel.createModelObject(input.getActionProvider(), plugin.getJavaImplClass());
              model.setLabel(plugin.getLabel());
              input.getActionProvider().addElement(model);
            }
            catch(Exception exc)
            {
              JacobDesigner.showException(exc);
            }
          }
        });
      }
    }
    // generic action button
    //
    Button addGenericButton = toolkit.createButton(buttonContainer, "Generic Action", SWT.PUSH);
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    addGenericButton.setLayoutData(gd);
    addGenericButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        try
        {
          EditorInput input = (EditorInput) page.getEditor().getEditorInput();
          ISelectionActionProvider actionProvider = input.getActionProvider();
          String packagePreFilled = "jacob.event.ui." + actionProvider.getGroupTableAlias() + "." + actionProvider.getName();
          packagePreFilled = packagePreFilled.toLowerCase();
          IProject project = JacobDesigner.getPlugin().getSelectedProject();
          IJavaProject myJavaProject = JavaCore.create(project);
          IPackageFragmentRoot[] roots = myJavaProject.getPackageFragmentRoots();
          NewClassWizardPage wizardPage = new NewClassWizardPage();
          wizardPage.setTypeName("MySelectionAction", true); // classname
          wizardPage.setSuperClass(ISelectionAction.class.getName(), false);
          wizardPage.setPackageFragmentRoot(roots[0], false);
          wizardPage.setPackageFragment(roots[0].getPackageFragment(packagePreFilled), true);
          wizardPage.setAddComments(true, true);
          wizardPage.setMethodStubSelection(false, false, true, false);
          wizardPage.setModifiers(wizardPage.F_PUBLIC, false);
          OpenNewClassWizardAction action = new OpenNewClassWizardAction();
          action.setConfiguredWizardPage(wizardPage);
          action.run();
          if (action.getCreatedElement() != null)
          {
            IPackageFragment packageFragment = wizardPage.getPackageFragment();
            String className = packageFragment.getElementName() + "." + action.getCreatedElement().getElementName();
            input.getActionProvider().addElement(SelectionActionModel.createModelObject(input.getActionProvider(), className));
          }
        }
        catch (Exception exc)
        {
        }
      }
    });
    
    createSpacer(toolkit, buttonContainer, 1);

    // remove button
    //
    removeButton = toolkit.createButton(buttonContainer, "Remove", SWT.PUSH);
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    removeButton.setLayoutData(gd);
    removeButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        SelectionActionModel actionModel = getSelectedModel();
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        if (actionModel instanceof ApplicationSelectionActionModel)
        {
          if (MessageDialog.openQuestion(shell, "Question", "Delete the Java class of the action on filesystem too?"))
          {
            try
            {
              IProject project = JacobDesigner.getPlugin().getSelectedProject();
              IJavaProject myJavaProject = JavaCore.create(project);
              ClassFinder.deleteClass(actionModel.getHookClassName(), myJavaProject);
              actionModel.getProvider().removeElement(actionModel);
            }
            catch (Exception exc)
            {
              JacobDesigner.showException(exc);
            }
          }
          else
          {
            actionModel.getProvider().removeElement(actionModel);
          }
        }
        else
        {
          actionModel.getProvider().removeElement(actionModel);
        }
      }
    });
    createSpacer(toolkit, buttonContainer, 1);
    // up button
    //
    upButton = toolkit.createButton(buttonContainer, "Up", SWT.PUSH);
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    upButton.setLayoutData(gd);
    upButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        getSelectedModel().getProvider().upElement(getSelectedModel());
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
        getSelectedModel().getProvider().downElement(getSelectedModel());
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
        SelectionActionModel actionModel = getSelectedModel();
        removeButton.setEnabled(actionModel != null);
        if (actionModel != null)
        {
          List fields = actionModel.getProvider().getActions();
          upButton.setEnabled(fields.indexOf(actionModel) != 0);
          downButton.setEnabled(fields.indexOf(actionModel) < (fields.size() - 1));
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

  protected void createToolBarActions(IManagedForm managedForm)
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.forms.MasterDetailsBlock#registerPages(org.eclipse.ui.forms
   * .DetailsPart)
   */
  protected void registerPages(DetailsPart detailsPart)
  {
    detailsPart.setPageProvider(new IDetailsPageProvider()
    {
      public Object getPageKey(Object object)
      {
        return object;
      }

      public IDetailsPage getPage(Object key) throws RuntimeException
      {
        SelectionActionModel model = (SelectionActionModel) key;
        if (model instanceof ApplicationSelectionActionModel)
          return new ActionDetailsPage_Application();
        if (model instanceof EngineSelectionActionModel)
          return new ActionDetailsPage_Engine();
        throw new IllegalStateException("SelectionAction type [" + model.getClass().getName() + "] is unknown.");
      }
    });
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent
   * )
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName() == ObjectModel.PROPERTY_ELEMENT_ADDED && evt.getNewValue() instanceof SelectionActionModel)
    {
      SelectionActionModel field = (SelectionActionModel) evt.getNewValue();
      viewer.setContentProvider(new ContentProvider());
      // and select entry
      select(field);
    }
    else if (evt.getPropertyName() == ObjectModel.PROPERTY_ELEMENT_REMOVED && evt.getOldValue() instanceof SelectionActionModel)
    {
      SelectionActionModel field = (SelectionActionModel) evt.getOldValue();
      viewer.remove(field);
    }
  }

  private SelectionActionModel getSelectedModel()
  {
    if (table.getSelectionCount() > 0)
      return (SelectionActionModel) table.getSelection()[0].getData();
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
