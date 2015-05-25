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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.KeyModel;
import de.tif.jacob.designer.model.RelationModel;
import de.tif.jacob.designer.model.TableModel;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class BrowserPropertyPage extends FormPage implements PropertyChangeListener
{
  private Text nameText;
  private Text descText;
  private Combo connectByKeyCombo;
  
  public BrowserPropertyPage(FormEditor editor)
  {
    super(editor, "first", "Common");
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
   */
  protected void createFormContent(IManagedForm managedForm)
  {
    ScrolledForm form = managedForm.getForm();
    FormToolkit toolkit = managedForm.getToolkit();
    
    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    form.getBody().setLayout(layout);
    createTableSection(form, toolkit);

    // TODO: Gehört nicht hier hin! Oder?
    update();
  }
  
  private JacobBrowserEditorInput getJacobInput()
  {
    return (JacobBrowserEditorInput) getEditorInput();
  }

  private void createTableSection(final ScrolledForm form, FormToolkit toolkit)
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
    
    //
    // create name text field
    toolkit.createLabel(client, "Table name:");
    this.nameText = toolkit.createText(client, null, SWT.SINGLE | SWT.BORDER);
    this.nameText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        try
        {
          getJacobInput().getBrowserModel().setName(nameText.getText());
        }
        catch (Exception e1)
        {
          nameText.setText(getJacobInput().getBrowserModel().getName());
          JacobDesigner.showException(e1);
        }
      }
    });
    GridData textGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    this.nameText.setLayoutData(textGridData);
    
    // create description text field
    //
    Label label =toolkit.createLabel(client, "Description:");
    label.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

    this.descText = toolkit.createText(client,"",SWT.MULTI|SWT.WRAP|SWT.LEFT|SWT.BORDER|SWT.V_SCROLL);
    this.descText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        getJacobInput().getBrowserModel().setDescription(descText.getText());
      }
    });
    textGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    textGridData.widthHint = 10;
    textGridData.heightHint = 80;
    this.descText.setLayoutData(textGridData);

    
    // create representative field combobox
    //
    toolkit.createLabel(client, "Connect By:");
    this.connectByKeyCombo = new Combo(client, SWT.READ_ONLY);
    this.connectByKeyCombo.addSelectionListener(new SelectionListener()
    {
      public void widgetSelected(SelectionEvent e)
      {
        String name = ((Combo) e.widget).getText();
        if(name.length()>0)
        {
          KeyModel key = getJacobInput().getBrowserModel().getTableAliasModel().getKeyModel(name);
          getJacobInput().getBrowserModel().setConnectByKey(key);
        }
        else
        {
          getJacobInput().getBrowserModel().setConnectByKey(null);
        }
      }

      public void widgetDefaultSelected(SelectionEvent e)
      {
        throw new UnsupportedOperationException();
      }
    });
    this.connectByKeyCombo.setLayoutData(gd);
    toolkit.adapt(this.connectByKeyCombo);
    
    section.setClient(client);
  }

  private void update()
  {
    BrowserModel input = getJacobInput().getBrowserModel();
    // Page wurde in den Editor eingefügt aber noch nicht angezeigt.
    // => namedText ist NULL obwohl der Editor schon Events empfangen kann.
    //
    if (input != null && this.nameText!=null)
    {
      if (!input.getName().equals(this.nameText.getText()))
        this.nameText.setText(input.getName());
      
      if (!input.getDescription().equals(this.descText.getText()))
        this.descText.setText(input.getDescription());

      TableModel browserTable = input.getTableAliasModel().getTableModel();
      // Von der Tabelle alle Keys holen welche auf sich selbst zeigen
      //
      List<String> ownKeyNames = new ArrayList<String>();
      ownKeyNames.add("");
      for (RelationModel r : input.getJacobModel().getRelationModelsTo(input.getTableAliasModel()))
      {
        if(r.getToTableAlias().getTableModel()==browserTable && r.getFromTableAlias().getTableModel()==browserTable)
          ownKeyNames.add(r.getToKey().getName());
      }
      this.connectByKeyCombo.setItems((String[])ownKeyNames.toArray(new String[0]));
      if(input.getConnectByKey()!=null)
        this.connectByKeyCombo.setText(input.getConnectByKey().getName());
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchPart#dispose()
   */
  public void dispose()
  {
    super.dispose();
    getJacobInput().getBrowserModel().removePropertyChangeListener(this);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
   */
  public void init(IEditorSite site, IEditorInput input)
  {
    super.init(site, input);
    getJacobInput().getBrowserModel().addPropertyChangeListener(this);
  }
  
  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    update();
  }
}
