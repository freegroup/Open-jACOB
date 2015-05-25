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
package de.tif.jacob.designer.editor.alias;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.contentassist.SubjectControlContentAssistant;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.ShowObjectModelHookAction;
import de.tif.jacob.designer.actions.ShowTableEditorAction;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.TableModel;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class AliasPropertiesPage extends FormPage implements ISelectionProvider
{
  List listeners = new ArrayList();
  ISelection theSelection = StructuredSelection.EMPTY;

  private Text      aliasText;
  private Text      aliasDescription;
  private Text      aliasCondition;
  private Hyperlink tableHyperlink;
  private Hyperlink eventHandlerHyperlink;

  public AliasPropertiesPage(FormEditor editor)
  {
    super(editor, "Common", "Common");
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
   */
  @Override
  protected void createFormContent(IManagedForm managedForm)
  {
    ScrolledForm form = managedForm.getForm();
    FormToolkit toolkit = managedForm.getToolkit();
    JacobAliasEditorInput jacobInput = (JacobAliasEditorInput) this.getEditorInput();
    form.setText("Table alias: "+jacobInput.getTableAliasModel().getName());
    
    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    form.getBody().setLayout(layout);
    this.createTableAliasSection(form, toolkit);

    // TODO: Gehört nicht hier hin! Oder?
    this.update();

  
    // we're cooperative and also provide our selection
    // at least for the tableviewer
    this.getSite().setSelectionProvider(this);
  }
  

  private void createTableAliasSection(final ScrolledForm form, FormToolkit toolkit)
  {
    Section section = toolkit.createSection(form.getBody(), /*Section.TWISTIE | */ Section.DESCRIPTION);
    section.setText("Common");
    section.setDescription("Set the common properties of the selected table.");
    section.marginWidth = 10;
    section.marginHeight = 5;
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.grabExcessHorizontalSpace = true;
    section.setLayoutData(gd);
    toolkit.createCompositeSeparator(section);
    
    Composite client = toolkit.createComposite(section);
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    client.setLayout(layout);
    
    GridData textGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);

    // create dbname text field
    //
    toolkit.createLabel(client, "DB Table:");
    this.tableHyperlink = toolkit.createHyperlink(client,"<unset>", SWT.NORMAL);
    this.tableHyperlink.addHyperlinkListener(new HyperlinkAdapter()
    {
      @Override
      public void linkActivated(HyperlinkEvent e)
      {
        new ShowTableEditorAction()
        {
          @Override
          public TableModel getTableModel()
          {
            return AliasPropertiesPage.this.getJacobInput().getTableAliasModel().getTableModel();
          }
        }.run(null);
      }
    });

    //
    // create name text field
    toolkit.createLabel(client, "Alias Name:");
    this.aliasText = toolkit.createText(client, null, SWT.SINGLE | SWT.BORDER);
    this.aliasText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        try
        {
          AliasPropertiesPage.this.getJacobInput().getTableAliasModel().setName(AliasPropertiesPage.this.aliasText.getText());
        }
        catch (Exception e1)
        {
          AliasPropertiesPage.this.aliasText.setText(AliasPropertiesPage.this.getJacobInput().getTableAliasModel().getName());
          JacobDesigner.showException(e1);
        }
      }
    });
    this.aliasText.setLayoutData(textGridData);
    
    toolkit.createLabel(client, "Condition:");
    this.aliasCondition = toolkit.createText(client,  null, SWT.SINGLE | SWT.BORDER);
    this.aliasCondition.addModifyListener(new ModifyListener()
        {
          public void modifyText(ModifyEvent e)
          {
            try
            {
              AliasPropertiesPage.this.getJacobInput().getTableAliasModel().setCondition(AliasPropertiesPage.this.aliasCondition.getText());
            }
            catch (Exception e1)
            {
              AliasPropertiesPage.this.aliasCondition.setText(AliasPropertiesPage.this.getJacobInput().getTableAliasModel().getCondition());
              JacobDesigner.showException(e1);
            }
          }
        });
    
    this.aliasCondition.setLayoutData(textGridData);
    
    final SubjectControlContentAssistant asistant= ContentAssistant.createContentAssistant(this.aliasCondition, this.getJacobInput().getTableAliasModel());
   
    
     
    toolkit.createLabel(client, "Description:");
    this.aliasDescription = toolkit.createText(client,  null, SWT.SINGLE | SWT.BORDER);
    this.aliasDescription.addModifyListener(new ModifyListener()
        {
          public void modifyText(ModifyEvent e)
          {
            try
            {
              AliasPropertiesPage.this.getJacobInput().getTableAliasModel().setDescription(AliasPropertiesPage.this.aliasDescription.getText());
            }
            catch (Exception e1)
            {
              AliasPropertiesPage.this.aliasDescription.setText(AliasPropertiesPage.this.getJacobInput().getTableAliasModel().getDescription());
              JacobDesigner.showException(e1);
            }
          }
        });
    this.aliasDescription.setLayoutData(textGridData);
    

    // Eventhandler for the Form
    //
    toolkit.createLabel(client, "Event Handler:");
    this.eventHandlerHyperlink = toolkit.createHyperlink(client,"<unset>", SWT.NORMAL);
    this.eventHandlerHyperlink.addHyperlinkListener(new HyperlinkAdapter()
    {
      @Override
      public void linkActivated(HyperlinkEvent e)
      {
        new ShowObjectModelHookAction()
        {
          @Override
          public ObjectModel getObjectModel()
          {
            return AliasPropertiesPage.this.getTableAliasModel();
          }
        }.run(null);
        AliasPropertiesPage.this.update();
      }
    });

    
    section.setClient(client);
  }

  @Override
  public void init(IEditorSite site, IEditorInput input)
  {
    super.init(site, input);
    theSelection = new StructuredSelection(getTableAliasModel());
  }

  protected TableAliasModel getTableAliasModel()
  {
    return this.getJacobInput().getTableAliasModel();
  }
  
  protected void update()
  {
    TableAliasModel model = this.getTableAliasModel();
    
    if (model != null)
    {
      if (!model.getName().equals(this.aliasText.getText()))
        this.aliasText.setText(model.getName());
      
      if (!model.getTableModel().getName().equals(this.tableHyperlink.getText()))
        this.tableHyperlink.setText(model.getTableModel().getDatasourceModel().getName()+ " : "+ model.getTableModel().getName());
      
      if (!model.getDescription().equals(this.aliasDescription.getText()))
        this.aliasDescription.setText(model.getDescription());
      
      if (!model.getCondition().equals(this.aliasCondition.getText()))
        this.aliasCondition.setText(model.getCondition());

      this.eventHandlerHyperlink.setText(this.getTableAliasModel().getHookClassName()==null?"<unset>":this.getTableAliasModel().getHookClassName());
      this.eventHandlerHyperlink.layout(true);

    }
    else
    {
      throw new IllegalStateException();
    }
    
  }
  
  private JacobAliasEditorInput getJacobInput()
  {
    return (JacobAliasEditorInput) this.getEditorInput();
  }


  public void addSelectionChangedListener(ISelectionChangedListener listener) 
  {
    this.listeners.add(listener);
  }

  public ISelection getSelection() 
  {
    return this.theSelection;
  }

  public void removeSelectionChangedListener(ISelectionChangedListener listener) 
  {
    this.listeners.remove(listener);
  }

  public void setSelection(ISelection selection) 
  { 
    this.theSelection = selection;    
    final SelectionChangedEvent e = new SelectionChangedEvent(this, selection);
    Object[] listenersArray = this.listeners.toArray();
    
    for (Object element : listenersArray)
    {
        final ISelectionChangedListener l = (ISelectionChangedListener) element;
        Platform.run(new SafeRunnable() 
        {
            public void run() 
            {
                l.selectionChanged(e);
            }
        });
    }
  }
}