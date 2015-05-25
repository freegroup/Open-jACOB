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
package de.tif.jacob.designer.editor.domain;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.jface.contentassist.SubjectControlContentAssistant;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
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
import de.tif.jacob.designer.editor.util.LabelContentAssistant;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIDomainModel;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class DomainPropertiesPage extends FormPage implements PropertyChangeListener
{
  private   Text  nameText;
  private   Text  descriptionText;
  private   Text  labelText;
	private   Table table;
	protected TableViewer tableViewer;

	// Set the table column property names
	public final static String COMPLETED_COLUMN 		= "!";
	public final static String DESCRIPTION_COLUMN 	= "Role names";

  private SubjectControlContentAssistant assistant;
	// Set column names
	protected String[] columnNames = new String[] 
	                                      { 
																				COMPLETED_COLUMN, 
																				DESCRIPTION_COLUMN
																				};

	public DomainPropertiesPage(FormEditor editor)
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
    JacobDomainEditorInput jacobInput = (JacobDomainEditorInput) getEditorInput();
    form.setText("Domain: "+jacobInput.getDomainModel().getName());
    
    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    form.getBody().setLayout(layout);
    createDomainSection(form, toolkit);

    update();
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
   */
  public void init(IEditorSite site, IEditorInput input)
  {
    super.init(site, input);
    getDomainModel().addPropertyChangeListener(this);
    getDomainModel().getJacobModel().addPropertyChangeListener(this);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchPart#dispose()
   */
  public void dispose()
  {
    getDomainModel().removePropertyChangeListener(this);
    getDomainModel().getJacobModel().removePropertyChangeListener(this);
    super.dispose();
    
  }
  
  private void createDomainSection(final ScrolledForm form, FormToolkit toolkit)
  {
    Section section = toolkit.createSection(form.getBody(), /*Section.TWISTIE | */ Section.DESCRIPTION);
    section.setText("Common");
    section.setDescription("Set the common properties of the gui domain.");
    section.marginWidth = 10;
    section.marginHeight = 5;
    GridData gd = new GridData(GridData.FILL_BOTH);
    gd.grabExcessHorizontalSpace = true;
    section.setLayoutData(gd);
    toolkit.createCompositeSeparator(section);
    
    Composite client = toolkit.createComposite(section);
    GridLayout layout = new GridLayout(2,false);
    client.setLayout(layout);
    

    // create name text field
    //
    GridData textGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    toolkit.createLabel(client, "Name:");
    nameText = toolkit.createText(client, null, SWT.SINGLE | SWT.BORDER);
    nameText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        try
        {
          getDomainModel().setName(nameText.getText());
        }
        catch (Exception e1)
        {
          nameText.setText(getDomainModel().getName());
          JacobDesigner.showException(e1);
        }
      }
    });
    nameText.setLayoutData(textGridData);
    
    toolkit.createLabel(client, "Label:");
    labelText = toolkit.createText(client, null, SWT.SINGLE | SWT.BORDER);
    this.labelText.addVerifyListener(new VerifyListener()
        {
          public void verifyText(VerifyEvent e)
          {
            if(StringUtil.toSaveString(getDomainModel().getLabel()).startsWith("%") && Character.isLetter(e.character))
                e.text=(""+e.character).toUpperCase();
          }
        });
    labelText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        try
        {
          getDomainModel().setLabel(labelText.getText());
        }
        catch (Exception e1)
        {
          labelText.setText(getDomainModel().getLabel());
          JacobDesigner.showException(e1);
        }
      }
    });
    labelText.setLayoutData(textGridData);
    
    toolkit.createLabel(client, "Description:");
    descriptionText = toolkit.createText(client, null, SWT.SINGLE | SWT.BORDER);
    descriptionText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        try
        {
          getDomainModel().setDescription(descriptionText.getText());
        }
        catch (Exception e1)
        {
          descriptionText.setText(getDomainModel().getDescription());
          JacobDesigner.showException(e1);
        }
      }
    });
    descriptionText.setLayoutData(textGridData);
    
    createSpacer(toolkit,client,2);
    
    Label spacer = toolkit.createLabel(client, "Roles");
    gd = new GridData();
    gd.horizontalSpan = 2;
    spacer.setLayoutData(gd);

    
    createTable(client);
		tableViewer.setContentProvider(new RoleContentProvider(getDomainModel()));
		tableViewer.setLabelProvider(new RoleLabelProvider(getDomainModel()));
		// The input for the table viewer is the instance of ExampleTaskList
		// Hier kann man irgenwie alles 'reinstecken'....der eigentliche Hinhalt kommt vom
		// ContentProvider.....falls man den Aufruf aber nicht macht, dann bekommt der view 
		// kein refresh und man sieht nichts....??? *mmmh*
		//
		tableViewer.setInput(getDomainInput().getDomainModel().getJacobModel());
    
    section.setClient(client);
  }

	/**
	 * Create the Table
	 */
	private void createTable(Composite parent) 
	{
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

		final int NUMBER_COLUMNS = 2;

		table = new Table(parent, style);
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan=2;
	
		table.setLayoutData(gridData);		
					
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		// 1st column with image/checkboxes - NOTE: The SWT.CENTER has no effect!!
		TableColumn column = new TableColumn(table, SWT.CENTER, 0);		
		column.setText(columnNames[0]);
		column.setWidth(20);
		
		// 2nd column with task Description
		column = new TableColumn(table, SWT.LEFT, 1);
		column.setText(columnNames[1]);
		column.setWidth(400);

		// Create the view for the table
		//
		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		
		tableViewer.setColumnProperties(columnNames);

		// Create the cell editors
		CellEditor[] editors = new CellEditor[columnNames.length];

		// Column 1 : is set (Checkbox)
		editors[0] = new CheckboxCellEditor(table);

		// Column 2 : Role name (Free text)
//		TextCellEditor textEditor = new TextCellEditor(table);
//		((Text) textEditor.getControl()).setTextLimit(60);
//		editors[1] = textEditor;

		// Assign the cell editors to the viewer 
		tableViewer.setCellEditors(editors);
		// Set the cell modifier for the viewer
		tableViewer.setCellModifier(new RoleCellModifier(getDomainModel()));
	}
	
	
	private void createSpacer(FormToolkit toolkit, Composite parent, int span)
  {
    Label spacer = toolkit.createLabel(parent, "");
    GridData gd = new GridData();
    gd.horizontalSpan = span;
    spacer.setLayoutData(gd);
  }
  
  private void update()
  {
    UIDomainModel domain = getDomainModel();
    
    if(assistant==null)
      assistant= LabelContentAssistant.createContentAssistant(labelText, this.getDomainModel().getJacobModel());
    
    if (domain != null)
    {
      if (!domain.getName().equals(nameText.getText()))
        nameText.setText(domain.getName());
      
      if (!domain.getLabel().equals(labelText.getText()))
        labelText.setText(domain.getLabel());
      
      if (!domain.getDescription().equals(descriptionText.getText()))
        descriptionText.setText(domain.getDescription());
      
    }
    else
    {
      throw new IllegalStateException();
    }
    
  }
  
  private JacobDomainEditorInput getDomainInput()
  {
    return (JacobDomainEditorInput) getEditorInput();
  }
  
  private UIDomainModel getDomainModel()
  {
    return getDomainInput().getDomainModel();
  }
  
  
  public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName() == ObjectModel.PROPERTY_USERROLE_CHANGED)
      tableViewer.refresh(true);
    if(evt.getPropertyName() == ObjectModel.PROPERTY_USERROLE_ASSIGNED)
      tableViewer.update(evt.getNewValue(),null);
    if(evt.getPropertyName() == ObjectModel.PROPERTY_USERROLE_UNASSIGNED)
      tableViewer.update(evt.getOldValue(),null);
    if(evt.getPropertyName() == ObjectModel.PROPERTY_USERROLE_CREATED)
      tableViewer.add(evt.getNewValue());
    if(evt.getPropertyName() == ObjectModel.PROPERTY_USERROLE_DELETED)
      tableViewer.add(evt.getOldValue());
  }
}