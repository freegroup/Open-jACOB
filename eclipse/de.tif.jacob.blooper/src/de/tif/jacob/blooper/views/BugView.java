package de.tif.jacob.blooper.views;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.ffxml.swtforms.builder.PanelBuilder;
import net.ffxml.swtforms.layout.CellConstraints;
import net.ffxml.swtforms.layout.FormLayout;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import de.tif.jacob.blooper.BlooperPlugin;
import de.tif.jacob.blooper.actions.ShowDefectEditorAction;
import de.tif.jacob.blooper.model.Bug;

public class BugView extends ViewPart implements ISelectionListener
{
  public static final String ID ="de.tif.jacob.blooper.views.BugView";

  private final class RefreshAction extends Action
  {
    public void run()
    {
      IProject project =BlooperPlugin.getDefault().getSelectedProject();
      if(project==null)
        return;
      try
      {
        String appName = project.getPersistentProperty(BlooperPlugin.PROPERTY_APPLICATION);
        cache.remove(appName);
      }
      catch (CoreException e)
      {
        cache.clear();
        BlooperPlugin.showException(e);
      }
      updateView();
    }
  }

  private final class NewAction extends Action
  {
    public void run()
    {
      IProject project =BlooperPlugin.getDefault().getSelectedProject();
      if(project==null)
        return;
      try
      {
        String appName = project.getPersistentProperty(BlooperPlugin.PROPERTY_APPLICATION);
        cache.remove(appName);
        final Bug bug = Bug.create(project,appName);
        new ShowDefectEditorAction()
        {
          public Bug getBug() throws Exception
          {
            return bug;
          }
        }.run(null);
      }
      catch (Exception e)
      {
        cache.clear();
        BlooperPlugin.showException(e);
      }
      updateView();
    }
  }
  
  Table    parameterTable;
  Label    statusMessage;
  Label    statusIcon;
  
  Action   refreshAction;
  IProject currentProject;
  
  Map<String, List<Bug>> cache = new HashMap<String, List<Bug>>();
  private NewAction newAction;
    
	public BugView() 
	{
    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(this);
	}

	public void dispose() 
	{
    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().removeSelectionListener(this);
		super.dispose();
	}

	public void createPartControl(Composite parent) 
	{
		FormLayout layout =	new FormLayout(
				"pref,4dlu,pref:grow,pref",
				"pref,10dlu:grow");
		
		PanelBuilder builder = new PanelBuilder(parent, layout);
		
		CellConstraints cc = new CellConstraints();

    statusIcon = new Label(parent,SWT.NORMAL);
    statusIcon.setBackground(null);
    builder.add(statusIcon,cc.xy(1,1,cc.FILL,cc.BOTTOM));

    statusMessage = new Label(parent,SWT.NORMAL);
    statusMessage.setBackground(null);
		builder.add(statusMessage,cc.xy(3,1,cc.FILL,cc.BOTTOM));
    
		// the parameter edit part
		builder.add(createTable(parent),cc.xywh(1,2,4,1,cc.FILL,cc.FILL));
		
		updateView();
    
    createToolbar();    
	}
	
  private Control createTable(Composite parent)
  {
  	parameterTable = new Table (parent,  SWT.BORDER | SWT.SINGLE| SWT.FULL_SELECTION);
  	parameterTable.setLinesVisible (true);
  	parameterTable.setHeaderVisible(true);
  	parameterTable.addMouseListener(new MouseListener()
    {
      public void mouseUp(MouseEvent e){}
      public void mouseDown(MouseEvent e){}
      public void mouseDoubleClick(MouseEvent e)
      {
        // open an editor
        new ShowDefectEditorAction()
        {
          public de.tif.jacob.blooper.model.Bug getBug() throws Exception
          {
          	TableItem[] rows = parameterTable.getSelection();
          	if(rows==null || rows.length<1)
          		return null;
          	
            return Bug.findByPkey(currentProject, rows[0].getText(0));
          }
        }.run(null);
      }
    });
    
		TableColumn column = new TableColumn(parameterTable, SWT.LEFT);
		column.setText("Id");
		column.setWidth (100);
		
		column = new TableColumn(parameterTable, SWT.LEFT);
		column.setText("Grade");
		column.setWidth (100);
		
		column = new TableColumn(parameterTable, SWT.LEFT);
		column.setText("State");
		column.setWidth (100);
		
		column = new TableColumn(parameterTable, SWT.LEFT);
		column.setText("Subject");
		column.setWidth (100);
		
  	return parameterTable;
  }

	public void selectionChanged(IWorkbenchPart part, ISelection selection)
  {
    updateView();
  }


	public void setFocus() 
	{
	}

  public void updateView(Bug bug)
  {
    List<Bug> bugs = cache.get(bug.getApplication());
    if(bugs!=null)
    {
      for (Bug bug2 : bugs)
      {
        if(bug2.getPkey().equals(bug.getPkey()))
        {
          bugs.remove(bug2);
          bugs.add(bug);
          break;
        }
      }
    }
    updateView();
  }
  
	public void updateView()
	{
    // view wird noch nicht angezeigt und hat von der Workbench ein
    // selectionEvent bekommen
    if(parameterTable==null)
      return;
    
    parameterTable.removeAll();
    currentProject =BlooperPlugin.getDefault().getSelectedProject();
    if(currentProject==null)
      return;
		try 
		{
      String appName = currentProject.getPersistentProperty(BlooperPlugin.PROPERTY_APPLICATION);
      List<Bug> bugs = cache.get(appName);
      if(bugs==null)
      {
        bugs = Bug.findByApplicationName(currentProject,appName);
        cache.put(appName,bugs);
      }
      for (Bug bug : bugs)
      {
				TableItem typeItem= new TableItem (parameterTable, SWT.NONE);
				String id      = bug.getPkey();
				String grade   = bug.getGrade();
				String state   = bug.getState();
				String subject = bug.getTitle();
				typeItem.setText(new String[]{id, grade, state, subject});
			}
      setError("Application: ["+appName+"]");
		} 
		catch (Exception e) 
		{
      setError(e.getMessage());
		}
	}
  
  /**
   * Create toolbar.
   */
  private void createToolbar() 
  {
      IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
      
      refreshAction = new RefreshAction();
      refreshAction.setImageDescriptor(BlooperPlugin.getImageDescriptor("refresh_tab.gif"));
      refreshAction.setToolTipText("Refresh");
      mgr.add(refreshAction);

      newAction = new NewAction();
      newAction.setImageDescriptor(BlooperPlugin.getImageDescriptor("new.png"));
      newAction.setToolTipText("Report a Bug");
      mgr.add(newAction);
  }

  
  private void setError(String message)
  {
    if(message==null)
    {
      statusIcon.setImage(null);
      statusMessage.setText("");
    }
    else
    {
      statusIcon.setImage(BlooperPlugin.getImage("errorwarning.gif"));
      statusMessage.setText(message);
    }
  }
}
