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
 * Created on 27.11.2005
 *
 */
package de.tif.jacob.designer.actions.dialog;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import net.ffxml.swtforms.builder.PanelBuilder;
import net.ffxml.swtforms.debug.FormDebugUtils;
import net.ffxml.swtforms.layout.CellConstraints;
import net.ffxml.swtforms.layout.FormLayout;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.PhysicalDataModel;

public class NewHtmlFormDialog extends TitleAreaDialog
{
  Label      errorMessage;
  Text       name;
  List       tablealiases;
  Button     okButton;
  Button     cancelButton;
  JacobModel jacobModel;

  String formName="";
  String tablealiasName=null;
  
  public static void main(String[] args) 
  {
		Display display = new Display();
		Shell  shell = new Shell(display);
		
		new NewHtmlFormDialog(shell,null).open();
	}
  
  /**
   * Create the dialog
   * @param parent
   */
  public NewHtmlFormDialog(Shell parent, JacobModel jacob)
  {
    super(parent);
    this.jacobModel = jacob;
    setShellStyle(getShellStyle()|SWT.RESIZE);
  }


  /**
   * Create contents of the dialog
   */
  protected Control createDialogArea(Composite ancestor) 
  {      
    setTitle("Create a Html Form");
    setMessage("Enter the name of the new form and select the anchor DB Table Alias");
    setTitleImage(JacobDesigner.getImage("new_html_form_dialog.png"));
    
		FormLayout layout =	new FormLayout(
				"5dlu,280dlu:grow,50dlu,5dlu,50dlu,5dlu",
				"pref,5dlu,pref,5dlu,pref,5dlu,120dlu:grow,5dlu,max(20dlu;pref)");
		
		PanelBuilder builder = new PanelBuilder(ancestor, layout);
		
		CellConstraints cc = new CellConstraints();

    // the name part
		builder.addSeparator("Form Name",cc.xywh(2,1,4,1));
		builder.add(createName(ancestor),cc.xywh(2,3,4,1));
		
		// the parameter edit part
		builder.addSeparator("DB Table Alias",cc.xywh(2,5,4,1));
		builder.add(createTable(ancestor),cc.xywh(2,7,4,1));
		
    // the button bar
		builder.add(createOkButton(ancestor), cc.xy(3,9));
		builder.add(createCancelButton(ancestor), cc.xy(5,9));
	
//		FormDebugUtils.debugLayout(builder.getComposite());
    
    return builder.getComposite();
  }
  
  private Control createTable(Composite parent)
  {
    tablealiases = new List(parent, SWT.SINGLE|SWT.READ_ONLY|SWT.V_SCROLL);
    if(jacobModel!=null)
    {
      Iterator<String> iter = jacobModel.getTableAliasNames().iterator();
      while (iter.hasNext())
      {
        tablealiases.add(iter.next());
      }
    }
    if(tablealiases.getItemCount()>0)
    {
      tablealiases.select(0);
    }
  	return tablealiases;
  }
  
  public String getFormName()
  {
    return formName;
  }
  
  private Control createOkButton(Composite parent)
  {
  	okButton =new Button(parent, SWT.PUSH);
  	okButton.setText("Ok");
  	okButton.setEnabled(false);
    okButton.addSelectionListener(new SelectionAdapter() 
    {
      public void widgetSelected(SelectionEvent e) 
      {
        tablealiasName=tablealiases.getSelection()[0];
        okPressed();
      }
    
    });
    
  	return okButton;
  }
  
  private Control createCancelButton(Composite parent)
  {
  	cancelButton =new Button(parent, SWT.PUSH);
  	cancelButton.setText("Cancel");
    cancelButton.addSelectionListener(new SelectionAdapter() 
        {
          public void widgetSelected(SelectionEvent e) 
          {
            cancelPressed();
          }
        
        });
  	
  	return cancelButton;
  }
  
  private Control createName(Composite parent)
  {
  	name = new Text(parent, SWT.BORDER|SWT.SINGLE);
  	name.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        formName=name.getText();
        String error = isValid(formName);
        okButton.setEnabled(error==null);
        setErrorMessage(error);
      }
    });
    return name;
  }
  
  private String isValid(String newText)
  {
    if(!StringUtils.containsOnly(newText,"abcdefghijklmnopqrstuvwxyzABCDEFGHJIKLMNOPQRSTUVWXYZ_".toCharArray()))
      return "Invalid character in form name.";

    if(jacobModel.getFormModel(newText)!=null)
      return "Form already exists. Use another name";
    
    if(newText==null || newText.length()==0)
      return "Enter a valid form name";

    return null;
  }

  public String getTableAliasName()
  {
    return tablealiasName;
  }
}
