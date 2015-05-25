/*
 * Created on 29.06.2007
 *
 */
package de.tif.jacob.designer.views.search;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.search.ui.IContextMenuConstants;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.ISearchResultPage;
import org.eclipse.search.ui.ISearchResultViewPart;
import org.eclipse.search.ui.SearchResultEvent;
import org.eclipse.search2.internal.ui.OpenSearchPreferencesAction;
import org.eclipse.search2.internal.ui.SearchMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.progress.UIJob;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.TreeSelectionObjectModelProvider;
import de.tif.jacob.designer.views.search.tree.DblClickObject;

public class ReferenceSearchResultPage extends Page implements ISearchResultPage
{
  private class UpdateUIJob extends UIJob {
    
    public UpdateUIJob() {
      super(SearchMessages.AbstractTextSearchViewPage_update_job_name); 
      setSystem(true);
    }
    
    public IStatus runInUIThread(IProgressMonitor monitor) {
      Control control= getControl();
      if (control == null || control.isDisposed()) {
        // disposed the control while the UI was posted.
        return Status.OK_STATUS;
      }
      if(searchResult!=null)
      {
        ReferenceSearchQuery query = (ReferenceSearchQuery)searchResult.getQuery();
        provider = new ReferenceSearchResultContentProvider(treeView, searchResult.getReferences(), query.getModel());
        treeView.setContentProvider(provider);
        treeView.setLabelProvider(new ReferenceSearchResultLabelProvider());
        treeView.setInput(viewPart.getViewSite());
        treeView.expandToLevel(3);
      }
      return Status.OK_STATUS;
    }
  }
  
  private ReferenceSearchResultContentProvider provider;
  TreeViewer treeView;
  private ISearchResultViewPart viewPart;
  private String fId;
  private ReferenceSearchResult searchResult;
  ISearchResultListener fListener;
  private Action doubleClickAction;
  
  public void createControl(Composite parent)
  {
    treeView = new TreeViewer(parent, SWT.NONE);
    fListener = new ISearchResultListener() {
      public void searchResultChanged(SearchResultEvent e) {
        new UpdateUIJob().schedule();
      }
    };

    makeActions();
    createContextMenu();
    hookTreeActions();
  }

  private void createContextMenu()
  {
     // Create menu manager.
     MenuManager menuMgr = new MenuManager("#PopupMenu");
     menuMgr.setRemoveAllWhenShown(true);
     menuMgr.addMenuListener(new IMenuListener()
     {
        public void menuAboutToShow(IMenuManager mgr)
        {
          mgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        }
     });
     // Create menu.
     Menu menu = menuMgr.createContextMenu(treeView.getControl());
     treeView.getControl().setMenu(menu);
     // Register menu for extension.
     getSite().registerContextMenu("#PopupMenu",menuMgr, treeView);
     getSite().setSelectionProvider(treeView);
  }

  public Control getControl()
  {
    return treeView.getTree();
  }

  public void setFocus()
  {
    if (treeView != null)
      treeView.getTree().setFocus();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.search2.ui.ISearchResultsPage#setInput(org.eclipse.search2.ui.ISearchResult,
   *      java.lang.Object)
   */
  public void setInput(ISearchResult search, Object viewState)
  {
    this.searchResult = (ReferenceSearchResult)search;
    if(this.searchResult!=null)
      this.searchResult.addListener(fListener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.search2.ui.ISearchResultsPage#setViewPart(org.eclipse.search2.ui.ISearchResultView)
   */
  public void setViewPart(ISearchResultViewPart part)
  {
    this.viewPart = part;
  }

  /**
   * {@inheritDoc}
   */
  public Object getUIState()
  {
    return treeView.getSelection();
  }

  public void init(IPageSite pageSite)
  {
    super.init(pageSite);
    getSite().setSelectionProvider(null);
    // add something to avoid the empty menu
    IMenuManager menuManager = pageSite.getActionBars().getMenuManager();
    menuManager.appendToGroup(IContextMenuConstants.GROUP_PROPERTIES, new OpenSearchPreferencesAction());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.search.ui.ISearchResultPage#saveState(org.eclipse.ui.IMemento)
   */
  public void saveState(IMemento memento)
  {
    // do nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.search.ui.ISearchResultPage#restoreState(org.eclipse.ui.IMemento)
   */
  public void restoreState(IMemento memento)
  {
    // do nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.search.ui.ISearchResultPage#setID(java.lang.String)
   */
  public void setID(String id)
  {
    fId = id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.search.ui.ISearchResultPage#getID()
   */
  public String getID()
  {
    return fId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.search.ui.ISearchResultPage#getLabel()
   */
  public String getLabel()
  {
    return "";
  }


  /**
   * 
   *
   */
  private void hookTreeActions()
  {
    treeView.addDoubleClickListener(new IDoubleClickListener()
    {
      public void doubleClick(DoubleClickEvent event)
      {
        doubleClickAction.run();
      }
    });
  }

  private void makeActions()
  {
    doubleClickAction = new Action()
    {
      public void run()
      {
        ISelection selection = treeView.getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        if (obj instanceof DblClickObject)
          ((DblClickObject) obj).dblClick();
      }
    };
  }

  public TreeViewer getViewer()
  {
    return treeView;
  }
}
