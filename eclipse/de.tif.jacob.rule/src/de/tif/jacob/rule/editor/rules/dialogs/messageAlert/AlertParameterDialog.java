/*
 * Created on 27.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.dialogs.messageAlert;

import net.ffxml.swtforms.builder.PanelBuilder;
import net.ffxml.swtforms.layout.CellConstraints;
import net.ffxml.swtforms.layout.FormLayout;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.rule.editor.rules.dialogs.ContentAssistant;
import de.tif.jacob.rule.editor.rules.model.MessageAlertModel;

public class AlertParameterDialog extends TitleAreaDialog
{
  protected MessageAlertModel model;

  Text       description;
  Text[]     parameterEditors;
  Table      parameterTable;
  Button     okButton;
  Button     cancelButton;
  
  public static void main(String[] args) 
  {
		Display display = new Display();
		Shell  shell = new Shell(display);
		
		new AlertParameterDialog(shell,null).open();
	}
  
  /**
   * Create the dialog
   * @param parent
   */
  public AlertParameterDialog(Shell parent, MessageAlertModel model)
  {
    super(parent);
    setShellStyle(getShellStyle()|SWT.RESIZE);
    this.model = model;
  }



  /**
   * Create contents of the dialog
   */
  protected Control createDialogArea(Composite ancestor) 
  {      
    setTitle("System Message");
    setMessage("Send an message to another user.");
    setTitleImage(JacobDesigner.getImage("dialogheader_message_alert.gif"));
    
		FormLayout layout =	new FormLayout(
				"5dlu,80dlu:grow,50dlu,5dlu,50dlu,5dlu",
				"pref,5dlu,fill:50dlu:grow,5dlu,pref,5dlu,fill:50dlu:grow,5dlu,max(20dlu;pref)");
		
		PanelBuilder builder = new PanelBuilder(ancestor, layout);
		
		CellConstraints cc = new CellConstraints();


		// the name part
		builder.addSeparator("Description",cc.xywh(2,1,4,1));
		builder.add(createDescription(ancestor),cc.xywh(2,3,4,1));
		
		// the parameter edit part
		builder.addSeparator("Parameters",cc.xywh(2,5,4,1));
		builder.add(createTable(ancestor),cc.xywh(2,7,4,1));
		
		// the button bar
		builder.add(createOkButton(ancestor), cc.xy(3,9));
		builder.add(createCancelButton(ancestor), cc.xy(5,9));
	
//		FormDebugUtils.debugLayout(builder.getComposite());
    
    updateView();
    return builder.getComposite();
  }
  
  private Control createTable(Composite parent)
  {
  	parameterTable = new Table (parent, SWT.BORDER | SWT.MULTI);
  	parameterTable.setLinesVisible (true);
  	parameterTable.setHeaderVisible(true);
 
		TableColumn column = new TableColumn(parameterTable, SWT.LEFT);
		column.setText("Type");
		column.setWidth (100);
		
		column = new TableColumn(parameterTable, SWT.LEFT);
		column.setText("Name");
		column.setWidth (100);
		
		column = new TableColumn(parameterTable, SWT.LEFT);
		column.setText("Value");
		column.setWidth (100);
		
  	return parameterTable;
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
                updateModel();
                close();
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
            close();
          }
        
        });
  	
  	return cancelButton;
  }
  
  private Control createDescription(Composite parent)
  {
  	description = new Text(parent, SWT.BORDER|SWT.MULTI);
  	description.setEditable(false);
    //name.set
    return description;
  }
  
  protected void updateModel()
  {
    TableItem items[] = parameterTable.getItems();
    String[] params = new String[items.length];
    for(int i=0; i<parameterEditors.length;i++)
    {
      params[i]=parameterEditors[i].getText();
    }
    model.setParameters(params);
  }

  protected void updateView()
  {
  		description.setText("Documentation?!");
  		
			String[] names=new String[] {"userLoginId","message"};
			for (int i=0; i<names.length; i++) 
			{
				TableItem typeItem= new TableItem (parameterTable, SWT.NONE);
				typeItem.setText(new String[]{"String", names[i],"unset"});
			}

			TableItem [] items = parameterTable.getItems();
			parameterEditors = new Text[items.length];
			String[] parameters = model.getParameters();
			for (int i=0; i<items.length; i++) 
			{
        
				parameterEditors[i] = new Text (parameterTable, SWT.NONE);
				if(parameters.length>i)
					parameterEditors[i].setText(parameters[i]);
				
				ContentAssistant.createContentAssistant(parameterEditors[i]);
        TableEditor editor = new TableEditor (parameterTable);
				editor.grabHorizontal = true;
				editor.setEditor(parameterEditors[i], items[i], 2);
			}
  	}
    
}
