/*
 * Created on 27.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.dialogs.decision._enum;

import net.ffxml.swtforms.builder.PanelBuilder;
import net.ffxml.swtforms.layout.CellConstraints;
import net.ffxml.swtforms.layout.FormLayout;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Text;

import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.FieldModelTypeEnum;
import de.tif.jacob.rule.editor.rules.model.EnumDecisionModel;

public class SelectEnumFieldDialog extends Dialog
{
  protected EnumDecisionModel model;
  protected Shell shell;

  Text       description;
  TreeViewer treeViewer;
  Button     okButton;
  Button     cancelButton;
  
  public static void main(String[] args) 
  {
		Display display = new Display();
		Shell  shell = new Shell(display);
		
		new SelectEnumFieldDialog(shell,null).open();
	}
  
  /**
   * Create the dialog
   * @param parent
   */
  public SelectEnumFieldDialog(Shell parent, EnumDecisionModel model)
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
				"pref,5dlu,pref,5dlu,fill:50dlu:grow,5dlu,max(20dlu;pref)");
		
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
		
	
		// the button bar
		builder.add(createOkButton(shell), cc.xy(7,7));
		builder.add(createCancelButton(shell), cc.xy(9,7));
	
		// the left side tree
		// Tree mus all letztes angelegt werden, da dieser ein Event auslöst
		// und die GUI sich dadurch aktualisiert
    builder.addSeparator("DB Enumeration Field",cc.xy(2,3));
		builder.add(createTree(shell), cc.xy(2,5) );
		

//		FormDebugUtils.debugLayout(builder.getComposite());
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
        	if(ss.getFirstElement() instanceof EnumFieldTreeNode)
        	{
        		updateModel((EnumFieldTreeNode)ss.getFirstElement());
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
        	if(ss.getFirstElement() instanceof EnumFieldTreeNode)
        	{
        		updateView((EnumFieldTreeNode)ss.getFirstElement());
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
    
    SelectEnumFieldContentProvider contentProvider = new SelectEnumFieldContentProvider(treeViewer, model);
    treeViewer.setContentProvider(contentProvider);
    treeViewer.setLabelProvider(new EnumFieldLabelProvider());
    treeViewer.setInput(new Object());
    if(contentProvider.fieldNode!=null)
    	treeViewer.setSelection(new StructuredSelection(contentProvider.fieldNode),true);  	

    return treeViewer.getTree();
  }
  
  protected void updateModel(EnumFieldTreeNode node)
  {
    model.setField(node.getTableAliasModel(),  node.getFieldModeEnumType());
  }
  
  protected void updateView(EnumFieldTreeNode node)
  {    
  	if(node==null)
  	{
  		description.setText("");
  		okButton.setEnabled(false);
  	}
  	else
  	{
  		FieldModelTypeEnum field = node.getFieldModeEnumType();
  		description.setText(field.getFieldModel().getDescription());
  		okButton.setEnabled(true);
  	}
    
  }
}
