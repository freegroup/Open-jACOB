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
package de.tif.jacob.designer.editor.relationset.action.dialog;

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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.PhysicalDataModel;

public class NewTableDialog extends TitleAreaDialog
{
  Label      errorMessage;
  Text       name;
  Combo      datasource;
  Button     okButton;
  Button     cancelButton;
  JacobModel jacobModel;

  String tableName="";
  // This should be static. So, it is possible to preselect the last
  // choosen datasource.
  //
  static String datasourceName="";
  
  public static void main(String[] args) 
  {
		Display display = new Display();
		Shell  shell = new Shell(display);
		
		new NewTableDialog(shell,null).open();
	}
  
  /**
   * Create the dialog
   * @param parent
   */
  public NewTableDialog(Shell parent, JacobModel jacob)
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
    setTitle("Create DB Table");
    setMessage("Select datasource and enter the name of the new table.");
    setTitleImage(JacobDesigner.getImage("new_table_dialog.gif"));
    
		FormLayout layout =	new FormLayout(
				"5dlu,80dlu:grow,50dlu,5dlu,50dlu,5dlu",
				"pref,5dlu,pref,5dlu,pref,5dlu,pref,5dlu,max(30dlu;pref),5dlu,max(20dlu;pref)");
		
		PanelBuilder builder = new PanelBuilder(ancestor, layout);
		
		CellConstraints cc = new CellConstraints();

		// the name part
		builder.addSeparator("Table Name",cc.xywh(2,1,4,1));
		builder.add(createName(ancestor),cc.xywh(2,3,4,1));
		
		// the parameter edit part
		builder.addSeparator("Database",cc.xywh(2,5,4,1));
		builder.add(createTable(ancestor),cc.xywh(2,7,4,1));
		
    builder.add(createErrorMessage(ancestor),cc.xywh(2,9,4,1));

    // the button bar
		builder.add(createOkButton(ancestor), cc.xy(3,11));
		builder.add(createCancelButton(ancestor), cc.xy(5,11));
	
//		FormDebugUtils.debugLayout(builder.getComposite());
    
    return builder.getComposite();
  }
  
  private Control createTable(Composite parent)
  {
    datasource = new Combo(parent, SWT.SINGLE|SWT.READ_ONLY);
    if(jacobModel!=null)
    {
      Iterator iter = jacobModel.getDatasourceModels().iterator();
      while (iter.hasNext())
      {
        PhysicalDataModel datasourceModel = (PhysicalDataModel) iter.next();
        datasource.add(datasourceModel.getName());
      }
    }
    if(datasource.getItemCount()>0)
    {
      datasource.select(0);
      // try to restore the last selection
      //
      if(datasourceName!=null)
      {
        int index = Arrays.asList(datasource.getItems()).indexOf(datasourceName);
        if(index!=-1)
          datasource.select(index);
      }
    }
    datasource.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        datasourceName=datasource.getText();
      }
    });
  	return datasource;
  }
  
  public String getTableName()
  {
    return tableName;
  }
  
  private Control createOkButton(Composite parent)
  {
  	okButton =new Button(parent, SWT.PUSH);
  	okButton.setText("Ok");
  	okButton.setEnabled(true);
    okButton.addSelectionListener(new SelectionAdapter() 
    {
      public void widgetSelected(SelectionEvent e) 
      {
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
        tableName=name.getText();
        String error = isValid(tableName);
        okButton.setEnabled(error==null);
        error = error==null?"":error;
        errorMessage.setText(error);
      }
    });
    return name;
  }
  
  private Control createErrorMessage(Composite parent)
  {
    errorMessage = new Label(parent,SWT.NORMAL);
    return errorMessage;
  }

  private String isValid(String newText)
  {
    if(!StringUtils.containsOnly(newText,"abcdefghijklmnopqrstuvwxyzABCDEFGHJIKLMNOPQRSTUVWXYZ_".toCharArray()))
      return "Invalid character in table name.";

    if(jacobModel.getTableModel(newText)!=null)
      return "Table already exists. Use another name";
    
    return null;
  }

  public String getDatasourceName()
  {
    return datasourceName;
  }
}
