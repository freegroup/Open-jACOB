/*
 * Created on 27.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.dialogs.decision._boolean;

import net.ffxml.swtforms.builder.PanelBuilder;
import net.ffxml.swtforms.layout.CellConstraints;
import net.ffxml.swtforms.layout.FormLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.rule.editor.rules.dialogs.ContentAssistant;
import de.tif.jacob.rule.editor.rules.model.DecisionModel;
import de.tif.jacob.rule.util.ASTHelper;

public class SelectDecisionMethodDialog extends Dialog
{
  protected DecisionModel model;
  protected Shell shell;

  Text       description;
  Text[]     parameterEditors;
  TreeViewer treeViewer;
  Table      parameterTable;
  Button     okButton;
  Button     cancelButton;
  
  public static void main(String[] args) 
  {
		Display display = new Display();
		Shell  shell = new Shell(display);
		
		new SelectDecisionMethodDialog(shell,null).open();
	}
  
  /**
   * Create the dialog
   * @param parent
   */
  public SelectDecisionMethodDialog(Shell parent, DecisionModel model)
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
				"5dlu,100dlu:grow,10dlu,5dlu,5dlu,80dlu:grow,50dlu,5dlu,50dlu,5dlu",
				"pref,5dlu,pref,5dlu,fill:50dlu:grow,5dlu,pref,5dlu,fill:50dlu:grow,5dlu,max(20dlu;pref)");
		
		PanelBuilder builder = new PanelBuilder(shell, layout);
		
		CellConstraints cc = new CellConstraints();

		// the top image BusinessObjectClassDialog.gif
	  Image img = JacobDesigner.getImage("BusinessObjectClassDialog.gif",JacobDesigner.DECORATION_NONE);
	  Label x =new Label(shell,SWT.NORMAL);
	  x.setImage(img);
		builder.add(x,cc.xy(9,1,cc.RIGHT,cc.BOTTOM));
		Control c = new Label(shell,SWT.NORMAL);
		c.setBackground(ColorConstants.white);
		builder.add(c,cc.xywh(1,1,8,1,cc.FILL,cc.FILL));
		
		// the name part
		builder.addSeparator("Description",cc.xywh(5,3,5,1));
		builder.add(createDescription(shell),cc.xywh(6,5,4,1));
		
		// the parameter edit part
		builder.addSeparator("Parameters",cc.xywh(5,7,5,1));
		builder.add(createTable(shell),cc.xywh(6,9,4,1));
		
		// the button bar
		builder.add(createOkButton(shell), cc.xy(7,11));
		builder.add(createCancelButton(shell), cc.xy(9,11));
	
		// the left side tree
		// Tree mus all letztes angelegt werden, da dieser ein Event auslöst
		// und die GUI sich dadurch aktualisiert
		builder.add(createTree(shell), cc.xywh(2,3,1,7) );
		

//		FormDebugUtils.debugLayout(builder.getComposite());
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
		
		/*
  	for (int i=0; i<112; i++) 
  	{
  		new TableItem (table, SWT.LEFT);
  	}
  	*/
		
  	return parameterTable;
  }
  
  private Control createOkButton(Shell parent)
  {
  	okButton =new Button(parent, SWT.PUSH);
  	okButton.setText("Ok");
  	okButton.setEnabled(false);
  	okButton.addSelectionListener(new SelectionAdapter() 
		{
			public void widgetSelected(SelectionEvent e) 
			{
        if(treeViewer.getSelection() instanceof IStructuredSelection)
        {
        	IStructuredSelection ss = (IStructuredSelection)treeViewer.getSelection();
        	if(ss.getFirstElement() instanceof DecisionMethodTreeNode)
        	{
        		updateModel((DecisionMethodTreeNode)ss.getFirstElement());
        		shell.dispose();
        	}
        }
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
  
  private Control createTree(Shell parent)
  {
    treeViewer = new TreeViewer(shell, SWT.BORDER);
    treeViewer.addSelectionChangedListener(new ISelectionChangedListener() 
    		{
			public void selectionChanged(SelectionChangedEvent event) 
			{
        if(treeViewer.getSelection() instanceof IStructuredSelection)
        {
        	IStructuredSelection ss = (IStructuredSelection)treeViewer.getSelection();
        	if(ss.getFirstElement() instanceof DecisionMethodTreeNode)
        	{
        		updateView((DecisionMethodTreeNode)ss.getFirstElement());
        	}
        	else
        	{
        		updateView(null);
        	}
        }
        else
        {
      		updateView(null);
        }

			}
		});
    
    SelectDecisionMethodContentProvider contentProvider = new SelectDecisionMethodContentProvider(treeViewer, model);
    treeViewer.setContentProvider(contentProvider);
    treeViewer.setLabelProvider(new MethodLabelProvider());
    treeViewer.setInput(new Object());
    if(contentProvider.methodNode!=null)
    	treeViewer.setSelection(new StructuredSelection(contentProvider.methodNode),true);  	

    return treeViewer.getTree();
  }
  
  protected void updateModel(DecisionMethodTreeNode node)
  {
  	TableItem items[] = parameterTable.getItems();
  	String[] params = new String[items.length];
  	for(int i=0; i<parameterEditors.length;i++)
  	{
  		params[i]=parameterEditors[i].getText();
  	}
  	model.setParameters(params);
  	model.setDecisionClass(node.getBusinessObjectClassName());
  	model.setDecisionMethod(node.getBusinessObjectMethod());
  }
  
  protected void updateView(DecisionMethodTreeNode node)
  {
  	if(node==null)
  	{
  		description.setText("");
  		okButton.setEnabled(false);
  		parameterTable.removeAll();
  	}
  	else
  	{
  		IMethod method = node.getIMethod();
  		description.setText(ASTHelper.getJavadoc(method));
  		okButton.setEnabled(true);
  		
  		// alle Parameter der Methode in die Tabelle eintragen
  		//
  		parameterTable.removeAll();
  		for(int i = 0;parameterEditors!=null &&i<parameterEditors.length;i++)
  		{
  			parameterEditors[i].dispose();
  		}
  		try 
  		{
				String[] names=method.getParameterNames();
				String[] types=method.getParameterTypes();
				for (int i=0; i<types.length; i++) 
				{
					TableItem typeItem= new TableItem (parameterTable, SWT.NONE);
					String type = Signature.toString(types[i]);
					typeItem.setText(new String[]{type, names[i],"unset"});
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
  		catch (JavaModelException e) 
  		{
				e.printStackTrace();
			}
  	}
  }
}
