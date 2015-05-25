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
package de.tif.jacob.designer.editor.mutableform;

import org.eclipse.jface.contentassist.SubjectControlContentAssistant;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
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
import de.tif.jacob.designer.editor.util.LabelContentAssistant;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.UIMutableFormModel;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class MutableFormPropertiesPage extends FormPage
{
  private Text   nameText;
  private Text   labelText;
  private Text   formDescription;
  private Text   formURL;
  private Combo  aliasCombo;
  private Combo  browserCombo;
  private Object assistant;
  
  private Hyperlink formEventHandlerHyperlink;
  private Hyperlink groupEventHandlerHyperlink;

  public MutableFormPropertiesPage(FormEditor editor)
  {
    super(editor, "Common", "Common");
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
   */
  protected void createFormContent(IManagedForm managedForm)
  {
    ScrolledForm form = managedForm.getForm();
    FormToolkit toolkit = managedForm.getToolkit();
    MutableFormEditorInput jacobInput = (MutableFormEditorInput) getEditorInput();
    form.setText("Own Draw Form: "+jacobInput.getFormModel().getName());
    
    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    form.getBody().setLayout(layout);
    createTableAliasSection(form, toolkit);

    // TODO: Gehört nicht hier hin! Oder?
    update();
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
   */
  public void init(IEditorSite site, IEditorInput input)
  {
    super.init(site, input);
    
    MutableFormEditorInput jacobInput = (MutableFormEditorInput) input;
  }
  
  
  private void createTableAliasSection(final ScrolledForm form, FormToolkit toolkit)
  {
    Section section = toolkit.createSection(form.getBody(), /*Section.TWISTIE | */ Section.DESCRIPTION);
    section.setText("Common");
    section.setDescription("Set the properties of the external form.");
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

    // create name text field
    //
    toolkit.createLabel(client, "Form Name:");
    nameText = toolkit.createText(client, null, SWT.SINGLE | SWT.BORDER);
    nameText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        try
        {
          getJacobInput().getFormModel().setName(nameText.getText());
        }
        catch (Exception e1)
        {
          nameText.setText(getJacobInput().getFormModel().getName());
          JacobDesigner.showException(e1);
        }
      }
    });
    nameText.setLayoutData(textGridData);
    
    // create name text field
    //
    toolkit.createLabel(client, "Label:");
    labelText = toolkit.createText(client, null, SWT.SINGLE | SWT.BORDER);
    labelText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        try
        {
          getJacobInput().getFormModel().setLabel(labelText.getText());
        }
        catch (Exception e1)
        {
          labelText.setText(getJacobInput().getFormModel().getLabel());
          JacobDesigner.showException(e1);
        }
      }
    });
    labelText.setLayoutData(textGridData);
    

    // create alias combobox
    //
    toolkit.createLabel(client, "DB Table:");
    aliasCombo = new Combo(client, SWT.READ_ONLY);
    aliasCombo.addSelectionListener(new SelectionListener(){
      public void widgetSelected(SelectionEvent e)
      {
        getOwnDrawFormModel().setTableAlias(((Combo)e.widget).getText());
      }
      public void widgetDefaultSelected(SelectionEvent e)
      {
        throw new UnsupportedOperationException();
      }
    });
    gd = new GridData(GridData.FILL_HORIZONTAL);
    aliasCombo.setLayoutData(gd);
    toolkit.adapt(aliasCombo);

    //
    // create type combobox
    toolkit.createLabel(client, "Data Browser:");
    browserCombo = new Combo(client, SWT.READ_ONLY);
    browserCombo.addSelectionListener(new SelectionListener(){
      public void widgetSelected(SelectionEvent e)
      {
        getOwnDrawFormModel().setBrowser(((Combo)e.widget).getText());
      }
      public void widgetDefaultSelected(SelectionEvent e)
      {
        throw new UnsupportedOperationException();
      }
    });
    gd = new GridData(GridData.FILL_HORIZONTAL);
    browserCombo.setLayoutData(gd);
    toolkit.adapt(browserCombo);

   
    
    // Eventhandler for the Form
    //
    toolkit.createLabel(client, "Form Event Handler:");
    formEventHandlerHyperlink = toolkit.createHyperlink(client,"<unset>", SWT.NORMAL);
    formEventHandlerHyperlink.addHyperlinkListener(new HyperlinkAdapter()
    {
      public void linkActivated(HyperlinkEvent e)
      {
        new ShowObjectModelHookAction()
        {
          public ObjectModel getObjectModel()
          {
            return getOwnDrawFormModel();
          }
        }.run(null);
        update();
      }
    });

    
    // Eventhandler for the internal GroupForm
    //
    toolkit.createLabel(client, "Rendering Event Handler:");
    groupEventHandlerHyperlink = toolkit.createHyperlink(client,"<unset>", SWT.NORMAL);
    groupEventHandlerHyperlink.addHyperlinkListener(new HyperlinkAdapter()
    {
      public void linkActivated(HyperlinkEvent e)
      {
        new ShowObjectModelHookAction()
        {
          public ObjectModel getObjectModel()
          {
            return getOwnDrawFormModel().getRenderGroup();
          }
        }.run(null);
        update();
      }
    });

    section.setClient(client);
  }

  protected void update()
  {
    UIMutableFormModel model = getJacobInput().getFormModel();
    
    if (model != null)
    {
      if (!model.getName().equals(nameText.getText()))
        nameText.setText(model.getName());

      if (!model.getLabel().equals(labelText.getText()))
        labelText.setText(model.getLabel());
      
      if(assistant==null)
        assistant= LabelContentAssistant.createContentAssistant(labelText, this.getOwnDrawFormModel().getJacobModel());

      
      aliasCombo.setItems(getOwnDrawFormModel().getJacobModel().getTableAliasNames().toArray(new String[0]));
      if (!StringUtil.saveEquals(getOwnDrawFormModel().getTableAlias(),this.aliasCombo.getText()))
        this.aliasCombo.setText(getOwnDrawFormModel().getTableAlias());

      browserCombo.setItems(getOwnDrawFormModel().getJacobModel().getBrowserNames(getOwnDrawFormModel().getTableAliasModel()).toArray(new String[0]));
      if (!StringUtil.saveEquals(getOwnDrawFormModel().getBrowser(),this.browserCombo.getText()))
        this.browserCombo.setText(getOwnDrawFormModel().getBrowser());
      
      formEventHandlerHyperlink.setText(getOwnDrawFormModel().getHookClassName()==null?"<unset>":getOwnDrawFormModel().getHookClassName());
      formEventHandlerHyperlink.layout(true);

      groupEventHandlerHyperlink.setText(getOwnDrawFormModel().getRenderGroup().getHookClassName()==null?"<unset>":getOwnDrawFormModel().getRenderGroup().getHookClassName());
      groupEventHandlerHyperlink.layout(true);
    }
    else
    {
      throw new IllegalStateException();
    }
    
  }
  
  protected UIMutableFormModel getOwnDrawFormModel()
  {
    return this.getJacobInput().getFormModel();
  }
  
  private MutableFormEditorInput getJacobInput()
  {
    return (MutableFormEditorInput) getEditorInput();
  }
}