/*
 * Created on 27.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.dialogs.userInformation;

import net.ffxml.swtforms.builder.PanelBuilder;
import net.ffxml.swtforms.layout.CellConstraints;
import net.ffxml.swtforms.layout.FormLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import de.tif.jacob.rule.editor.rules.dialogs.ContentAssistant;
import de.tif.jacob.rule.editor.rules.model.MessageDialogModel;

public class DialogParameterDialog extends Dialog
{
  protected MessageDialogModel model;
  protected Shell shell;

  Text       description;
  Text[]     parameterEditors;
  Table      parameterTable;
  Button     okButton;
  Button     cancelButton;
  
  public static void main(String[] args) 
  {
		Display display = new Display();
		Shell  shell = new Shell(display);
		
		new DialogParameterDialog(shell,null).open();
	}
  
  /**
   * Create the dialog
   * @param parent
   */
  public DialogParameterDialog(Shell parent, MessageDialogModel model)
  {
    super(parent, SWT.RESIZE);
    
    this.shell = new Shell(parent, SWT.RESIZE|SWT.BORDER|SWT.TITLE|SWT.CLOSE);
    this.model = model;
  }

  /**
   * Open the dialog
   * @return the result
   */
  public void open()
  {
    createContents(shell);
    
    Display display = getParent().getDisplay();

    shell.setSize(800,550);
  	Monitor primary = display.getPrimaryMonitor ();
  	Rectangle bounds = primary.getBounds ();
  	Rectangle rect = shell.getBounds ();
  	int x = bounds.x + (bounds.width - rect.width) / 2;
  	int y = bounds.y + (bounds.height - rect.height) / 2;
  	shell.setLocation (x, y);
    shell.layout();
    shell.open();
    
    
    while (!shell.isDisposed())
    {
      if (!display.readAndDispatch())
        display.sleep();
    }
  }

  /**
   * Create contents of the dialog
   */
  protected void createContents(Shell shell)
  {
		FormLayout layout =	new FormLayout(
				"5dlu,80dlu:grow,50dlu,5dlu,50dlu,5dlu",
				"pref,5dlu,pref,5dlu,fill:50dlu:grow,5dlu,pref,5dlu,fill:50dlu:grow,5dlu,max(20dlu;pref)");
		
		PanelBuilder builder = new PanelBuilder(shell, layout);
		
		CellConstraints cc = new CellConstraints();

		// the top image BusinessObjectClassDialog.gif
    /*
	  Image img = JacobDesigner.getImage("BusinessObjectClassDialog.gif",JacobDesigner.DECORATION_NONE);
	  Label x =new Label(shell,SWT.NORMAL);
	  x.setImage(img);
		builder.add(x,cc.xy(9,1,cc.RIGHT,cc.BOTTOM));
		Control c = new Label(shell,SWT.NORMAL);
		c.setBackground(ColorConstants.white);
		builder.add(c,cc.xywh(1,1,8,1,cc.FILL,cc.FILL));
		*/
		// the name part
		builder.addSeparator("Description",cc.xywh(1,3,5,1));
		builder.add(createDescription(shell),cc.xywh(2,5,4,1));
		
		// the parameter edit part
		builder.addSeparator("Parameters",cc.xywh(1,7,5,1));
		builder.add(createTable(shell),cc.xywh(2,9,4,1));
		
		// the button bar
		builder.add(createOkButton(shell), cc.xy(3,11));
		builder.add(createCancelButton(shell), cc.xy(5,11));
	
		

//		FormDebugUtils.debugLayout(builder.getComposite());
    
    updateView();
  }
  
  private Control createTable(Shell parent)
  {
  	parameterTable = new Table (shell, SWT.BORDER | SWT.MULTI);
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
  
  private Control createOkButton(Shell parent)
  {
  	okButton =new Button(parent, SWT.PUSH);
  	okButton.setText("Ok");
  	okButton.setEnabled(true);
    okButton.addSelectionListener(new SelectionAdapter() 
        {
          public void widgetSelected(SelectionEvent e) 
          {
                updateModel();
                shell.dispose();
          }
        
        });
  	return okButton;
  }
  
  private Control createCancelButton(Shell parent)
  {
  	cancelButton =new Button(parent, SWT.PUSH);
  	cancelButton.setText("Cancel");
    cancelButton.addSelectionListener(new SelectionAdapter() 
        {
          public void widgetSelected(SelectionEvent e) 
          {
            shell.dispose();
          }
        
        });
  	
  	return cancelButton;
  }
  
  private Control createDescription(Shell parent)
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
  		
			String[] names=new String[] {"message"};
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
