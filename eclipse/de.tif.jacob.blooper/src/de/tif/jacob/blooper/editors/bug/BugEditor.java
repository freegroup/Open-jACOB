package de.tif.jacob.blooper.editors.bug;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.EditorPart;
import de.tif.jacob.blooper.BlooperPlugin;
import de.tif.jacob.blooper.model.Bug;
import de.tif.jacob.blooper.views.BugView;
  
  /**
   * Internal web browser. This class is an <code>EditorPart</code> only so it
   * will show up in the editor view area. No actual editing can be done in the
   * browser.
   * 
   * @author Kyle
   * 
   */
public class BugEditor extends EditorPart 
{
    final public static String ID="de.tif.jacob.blooper.editors.defect.BugEditor";
    private Bug bug;

    private FormToolkit formToolkit = null;   //  @jve:decl-index=0:visual-constraint=""
    private Form form = null;
    
    private Label idLabel;
    private Text idText;
    
    private Label stateLabel;
    private Combo stateCombo;

    private Label ownerLabel;
    private Text ownerText;
    
    private Label creatorLabel;
    private Text creatorText;
    
    private Label gradeLabel;
    private Combo gradeCombo;
    
    private Label subjectLabel;
    private Text subjectText;
    private Text descriptionText = null;
    private Label descriptionLabel;
    private boolean dirty=false;

    @Override
    public void doSave(IProgressMonitor monitor)
    {
      try
      {
        bug.commit();
        bug = Bug.findByPkey(bug.getEndpoint(),bug.getPkey());
        updateUI();
        dirty=false;
        BugView view = (BugView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(BugView.ID);
        view.updateView(bug);
      }
      catch (Exception e)
      {
        BlooperPlugin.showException(e);
      }
      firePropertyChange(IEditorPart.PROP_DIRTY);
    }

    @Override
    public void doSaveAs()
    {
      // TODO Auto-generated method stub
    }

    /**
     * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite,
     *      org.eclipse.ui.IEditorInput)
     */
    public void init(IEditorSite site, IEditorInput input) throws PartInitException 
    {
      setSite(site);
      setInput(input);
      BugEditorInput defInput = (BugEditorInput)input;
      bug = defInput.getBug();
      setPartName("Bug #"+bug.getPkey()+" for ["+bug.getApplication()+"]");
      setTitleToolTip("Bug: "+bug.getTitle());
    }


    @Override
    public boolean isDirty()
    {
      return dirty;
    }

    @Override
    public boolean isSaveAsAllowed()
    {
      return false;
    }


    @Override
    public void setFocus()
    {
    }

    /**
     * This method initializes formToolkit  
     *  
     * @return org.eclipse.ui.forms.widgets.FormToolkit 
     */
    private FormToolkit getFormToolkit()
    {
      if (formToolkit == null)
      {
        formToolkit = new FormToolkit(Display.getCurrent());
      }
      return formToolkit;
    }


    /**
     * This method initializes form 
     *
     */
    public void createPartControl(Composite parent)
    {
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 2;
      
      form = getFormToolkit().createForm(parent);
      form.getBody().setLayout(gridLayout);
      idLabel = getFormToolkit().createLabel(form.getBody(), "Id");
      idText = getFormToolkit().createText(form.getBody(), "", SWT.BORDER);
      GridData gd = new GridData();
      gd.widthHint = 200;
//      gd.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
      idText.setLayoutData(gd);
      idText.setEditable(false);
      
      ownerLabel = getFormToolkit().createLabel(form.getBody(),"Owner");
      ownerText = getFormToolkit().createText(form.getBody(),"", SWT.BORDER);
      gd = new GridData();
      gd.widthHint = 200;
      ownerText.setLayoutData(gd);
      ownerText.setEditable(false);
      
      creatorLabel = getFormToolkit().createLabel(form.getBody(),"Creator");
      gd = new GridData();
      gd.verticalSpan = 0;
      creatorLabel.setLayoutData(gd);
      creatorText = getFormToolkit().createText(form.getBody(),"", SWT.BORDER);
      gd = new GridData();
      gd.widthHint = 200;
      creatorText.setLayoutData(gd);
      creatorText.setEditable(false);

      gd = new GridData();
//      gd.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
      stateLabel = getFormToolkit().createLabel(form.getBody(), "State");
      stateCombo = new Combo(form.getBody(), SWT.NONE|SWT.READ_ONLY);
      stateCombo.setLayoutData(gd);
     
      stateCombo.addModifyListener(new ModifyListener()
      {
        public void modifyText(ModifyEvent e)
        {
          if(stateCombo.getText().length()>0 && !stateCombo.getText().equals(bug.getState()))
          {
            bug.setState(stateCombo.getText());
            setDirty();
          }
          
        }
      });
      
      gradeLabel = getFormToolkit().createLabel(form.getBody(), "Grade");
      gradeCombo = new Combo(form.getBody(), SWT.NONE|SWT.READ_ONLY);
      getFormToolkit().adapt(gradeCombo);
      gd = new GridData();
//      gd.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
      gradeCombo.setLayoutData(gd);
      gradeCombo.addModifyListener(new ModifyListener()
      {
        public void modifyText(ModifyEvent e)
        {
          if(gradeCombo.getText().length()>0 && !gradeCombo.getText().equals(bug.getGrade()))
          {
            bug.setGrade(gradeCombo.getText());
            setDirty();
          }
        }
      });
      

      subjectLabel = getFormToolkit().createLabel(form.getBody(), "Subject");
      gd = new GridData();
      gd.grabExcessHorizontalSpace = true;
      gd.verticalSpan = 0;
      gd.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
      subjectText = getFormToolkit().createText(form.getBody(),"", SWT.BORDER);
      subjectText.setLayoutData(gd);
      subjectText.addModifyListener(new ModifyListener()
      {
        public void modifyText(ModifyEvent e)
        {
          if(!subjectText.getText().equals(bug.getTitle()))
          {
            bug.setTitle(subjectText.getText());
            setDirty();
          }
        }
      });
      
      
      descriptionLabel = getFormToolkit().createLabel(form.getBody(), "Description");
      gd = new GridData();
      gd.verticalAlignment=SWT.TOP;
      descriptionLabel.setLayoutData(gd);
      
      descriptionText = new Text(form.getBody(), SWT.MULTI | SWT.WRAP | SWT.V_SCROLL| SWT.BORDER);
      gd = new GridData();
      gd.grabExcessHorizontalSpace = true;
      gd.grabExcessVerticalSpace = true;
      gd.verticalSpan = 0;
      gd.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
      gd.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
      descriptionText.setLayoutData(gd);
      descriptionText.addModifyListener(new ModifyListener()
      {
        public void modifyText(ModifyEvent e)
        {
          if(!descriptionText.getText().equals(bug.getDescription()))
          {
            bug.setDescription(descriptionText.getText());
            setDirty();
          }
        }
      });
      
      updateUI();
      
    }

    private void setDirty()
    {
      dirty = true;
      firePropertyChange(IEditorPart.PROP_DIRTY);
    }
    
    private void updateUI()
    {
      descriptionText.setText(bug.getDescription());
      subjectText.setText(bug.getTitle());

      gradeCombo.setItems(bug.getValidGrades());
      gradeCombo.setText(bug.getGrade());
      
      stateCombo.setItems(bug.getValidStates());
      stateCombo.setText(bug.getState());

      creatorText.setText(bug.getCreatorName());
      
      ownerText.setText(bug.getOwnerName());
      idText.setText(bug.getPkey());
      
      if(bug.getState().equals(Bug.CLOSED))
      {
        descriptionText.setEditable(false);
        subjectText.setEditable(false);
        creatorText.setEditable(false);
        ownerText.setEditable(false);
        gradeCombo.setEnabled(false);
        stateCombo.setEnabled(false);
      }
      dirty=false;
    }
}  //  @jve:decl-index=0:visual-constraint="10,10,631,429"
