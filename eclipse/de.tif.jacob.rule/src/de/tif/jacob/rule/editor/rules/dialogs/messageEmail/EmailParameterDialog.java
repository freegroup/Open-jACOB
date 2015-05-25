/*
 * Created on 27.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.dialogs.messageEmail;

import net.ffxml.swtforms.builder.PanelBuilder;
import net.ffxml.swtforms.layout.CellConstraints;
import net.ffxml.swtforms.layout.FormLayout;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.rule.editor.rules.model.MessageEMailModel;
import de.tif.jacob.rule.util.letter.LetterSourceViewer;

public class EmailParameterDialog extends TitleAreaDialog
{
	protected MessageEMailModel model;

  LetterSourceViewer to;
  LetterSourceViewer subject;
  LetterSourceViewer message;
  Button     okButton;
  Button     cancelButton;
  
  public static void main(String[] args) 
  {
		Display display = new Display();
		Shell  shell = new Shell(display);
		
		new EmailParameterDialog(shell,null).open();
	}
  
  /**
   * Create the dialog
   * @param parent
   */
  public EmailParameterDialog(Shell parent, MessageEMailModel model)
  {
    super(parent);
    setShellStyle(getShellStyle()|SWT.RESIZE);
    
    this.model = model;
  }


  /** dialogheader_message_email.gif
   * Create contents of the dialog
   */
  protected Control createDialogArea(Composite ancestor) 
  {      
    setTitle("eMail Message");
    setMessage("Sending a aMail to another user.");
    setTitleImage(JacobDesigner.getImage("dialogheader_message_email.png"));

    FormLayout layout =	new FormLayout(
				"5dlu,pref,5dlu,180dlu:grow,50dlu,5dlu,50dlu,5dlu",
				"pref,5dlu,p,5dlu,fill:100dlu:grow,5dlu,max(20dlu;pref)");
		
		PanelBuilder builder = new PanelBuilder(ancestor, layout);
		
		CellConstraints cc = new CellConstraints();

    builder.addLabel("To:", cc.xy(2,1));
    builder.add(createTo(ancestor),cc.xywh(4,1,4,1));

    builder.addLabel("Subject:", cc.xy(2,3));
    builder.add(createSubject(ancestor),cc.xywh(4,3,4,1));

    // the name part
    builder.addLabel("Message:", cc.xy(2,5));
		builder.add(createDescription(ancestor),cc.xywh(4,5,4,1));
	
		// the button bar
		builder.add(createOkButton(ancestor), cc.xy(5,7));
		builder.add(createCancelButton(ancestor), cc.xy(7,7));

//		FormDebugUtils.debugLayout(builder.getComposite());    
    updateView();
    
    return builder.getComposite();
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
  
  private Control createTo(Composite parent)
  {
    JacobModel model = JacobDesigner.getPlugin().getModel();
    to = new LetterSourceViewer(model,parent,null,  SWT.BORDER|SWT.SINGLE);
    return to.getControl();
  }
  
  private Control createSubject(Composite parent)
  {
    JacobModel model = JacobDesigner.getPlugin().getModel();
    subject = new LetterSourceViewer(model,parent,null,  SWT.BORDER|SWT.SINGLE);
    return subject.getControl();
  }
  
  private Control createDescription(Composite parent)
  {
    JacobModel model = JacobDesigner.getPlugin().getModel();
    message = new LetterSourceViewer(model,parent,null,   SWT.H_SCROLL + SWT.V_SCROLL|SWT.BORDER);
    return message.getControl();
  }

  protected void updateModel()
  {
    model.setTo(to.getText());
    model.setSubject(subject.getText());
    model.setMessage(message.getText());
  }

  protected void updateView()
  {
  	
    to.setText(model.getTo());
    subject.setText(model.getSubject());
    message.setText(model.getMessage());
 	}
}
