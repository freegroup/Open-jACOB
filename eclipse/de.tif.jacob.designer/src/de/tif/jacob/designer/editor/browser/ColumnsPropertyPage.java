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

import org.eclipse.jface.contentassist.SubjectControlContentAssistant;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
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
import de.tif.jacob.designer.actions.ShowTableAliasEditorAction;
import de.tif.jacob.designer.actions.ShowTableEditorAction;
import de.tif.jacob.designer.editor.alias.ContentAssistant;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.TableModel;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class ColumnsPropertyPage extends FormPage
{
  private final ColumnMasterBlock block;

  public ColumnsPropertyPage(FormEditor editor)
  {
    super(editor, "Columns", "Columns");
    block = new ColumnMasterBlock(this);
  }
  
  
  /* (non-Javadoc)
   * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
   */
  protected void createFormContent(IManagedForm managedForm)
  {
    ScrolledForm form = managedForm.getForm();
    FormToolkit toolkit = managedForm.getToolkit();
    JacobBrowserEditorInput jacobInput = (JacobBrowserEditorInput) getEditorInput();
    form.setText("Browser: "+jacobInput.getBrowserModel().getName());

    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    form.getBody().setLayout(layout);
    createBrowserSection(form, toolkit);
    
    block.createContent(managedForm);
  }
  
  private void createBrowserSection(final ScrolledForm form, FormToolkit toolkit)
  {
    Section section = toolkit.createSection(form.getBody(), /*Section.TWISTIE | */ Section.DESCRIPTION);
    section.setText("Common");
//    section.setDescription("Set the common properties of the selected table.");
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
    
    // create dbname text field
    //
    toolkit.createLabel(client, "DB Table:");
    Hyperlink tableHyperlink = toolkit.createHyperlink(client,getBrowserModel().getTableAliasModel().getTableModel().getDatasourceModel().getName()+ " : "+ getBrowserModel().getTableAliasModel().getTableModel().getName(), SWT.NORMAL);
    tableHyperlink.addHyperlinkListener(new HyperlinkAdapter()
    {
      public void linkActivated(HyperlinkEvent e)
      {
        new ShowTableEditorAction()
        {
          public TableModel getTableModel()
          {
            return getBrowserModel().getTableAliasModel().getTableModel();
          }
        }.run(null);
      }
    });

    // create dbname text field
    //
    toolkit.createLabel(client, "DB Table Alias:");
    Hyperlink aliasHyperlink = toolkit.createHyperlink(client,getBrowserModel().getTableAliasModel().getName(), SWT.NORMAL);
    aliasHyperlink.addHyperlinkListener(new HyperlinkAdapter()
    {
      public void linkActivated(HyperlinkEvent e)
      {
        new ShowTableAliasEditorAction()
        {
          public TableAliasModel getTableAliasModel()
          {
            return getBrowserModel().getTableAliasModel();
          }
        }.run(null);
      }
    });

    section.setClient(client);
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
   */
  public void init(IEditorSite site, IEditorInput input)
  {
    super.init(site, input);
    
    JacobBrowserEditorInput jacobInput = (JacobBrowserEditorInput) input;
    jacobInput.getBrowserModel().addPropertyChangeListener(this.block);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchPart#dispose()
   */
  public void dispose()
  {
    super.dispose();
    
    JacobBrowserEditorInput jacobInput = (JacobBrowserEditorInput) getEditorInput();
    jacobInput.getBrowserModel().removePropertyChangeListener(this.block);

    // select a 'null' object. This will remove the old propertyChangeListener from the previous selection
    //
    block.select(null);
  }
  
  protected TableAliasModel  getTableAliasModel()
  {
    return ((JacobBrowserEditorInput)getEditorInput()).getBrowserModel().getTableAliasModel();
  }
  
  protected BrowserModel  getBrowserModel()
  {
    return ((JacobBrowserEditorInput)getEditorInput()).getBrowserModel();
  }
}