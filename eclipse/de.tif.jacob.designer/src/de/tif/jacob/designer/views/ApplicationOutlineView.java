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
package de.tif.jacob.designer.views;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.internal.ui.search.OccurrencesSearchQuery;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.TreeSelectionObjectModelProvider;
import de.tif.jacob.designer.util.ModelTransfer;
import de.tif.jacob.designer.views.applicationoutline.DblClickObject;
import de.tif.jacob.designer.views.applicationoutline.TreeApplicationParent;
import de.tif.jacob.designer.views.applicationoutline.TreeObject;
import de.tif.jacob.designer.views.applicationoutline.TreeParent;
import de.tif.jacob.designer.views.applicationoutline.action.DeleteLinkedFormAction;
import de.tif.jacob.designer.views.applicationoutline.dnd.DragListener;
import de.tif.jacob.designer.views.applicationoutline.dnd.DropListener;
import de.tif.jacob.designer.views.applicationoutline.listener.TooltipListener;
import de.tif.jacob.designer.views.applicationoutline.listener.TreeInPlaceEditor;
import de.tif.jacob.designer.views.search.ReferenceSearchQueryFirstLevel;
import de.tif.jacob.util.StringUtil;

public class ApplicationOutlineView extends ViewPart implements ISelectionChangedListener, ISelectionListener
{
  private TreeViewer viewer;

  private Action doubleClickAction;
  private Action deleteItemAction;
  private Action expandAllAction;
  private Action collapseAllAction;
  private Action synchAction;
  private boolean inSync=false;
  
  class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider
  {
    private TreeParent invisibleRoot;

    public void inputChanged(Viewer v, Object oldInput, Object newInput)
    {
    }

    public void dispose()
    {
      if (this.invisibleRoot != null)
        invisibleRoot.dispose();
    }

    public Object[] getElements(Object parent)
    {
      if (parent.equals(getViewSite()))
      {
        if (invisibleRoot == null)
          initialize();
        return getChildren(invisibleRoot);
      }
      return getChildren(parent);
    }

    public Object getParent(Object child)
    {
      if (child instanceof TreeObject)
      {
        return ((TreeObject) child).getParent();
      }
      return null;
    }

    public Object[] getChildren(Object parent)
    {
      if (parent instanceof TreeParent)
      {
        return ((TreeParent) parent).getChildren();
      }
      return new Object[0];
    }

    public boolean hasChildren(Object parent)
    {
      if (parent instanceof TreeParent)
        return ((TreeParent) parent).hasChildren();
      return false;
    }

    /*
     * We will set up a dummy model to initialize tree heararchy. In a real
     * code, you will connect to a real model and expose its hierarchy.
     */
    private void initialize()
    {
      JacobModel jacob = JacobDesigner.getPlugin().getModel();

      if (jacob == null)
        return;

      invisibleRoot = new TreeParent(viewer,null, jacob, jacob, "");

      TreeParent root = new TreeApplicationParent(viewer,invisibleRoot,jacob);
      invisibleRoot.addChild(root);
    }
  }

  class ViewLabelProvider extends LabelProvider
  {
    public String getText(Object obj)
    {
      TreeObject treeObj = (TreeObject)obj;
      return treeObj.getLabel();
    }

    public Image getImage(Object obj)
    {
      if (obj instanceof TreeObject)
      {
        TreeObject treeObj = (TreeObject) obj;
        return treeObj.getImage();
      }
      return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
    }
  }

  class ViewSorter extends ViewerSorter
  {
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ViewerSorter#category(java.lang.Object)
     */
    public int category(Object element)
    {
      return ((TreeObject) element).getSortingCategory();
    }

    public int compare(Viewer viewer, Object e1, Object e2)
    {
      int cat1 = category(e1);
      int cat2 = category(e2);

      if (cat1 != cat2)
        return cat1 - cat2;

      // cat1 == cat2

      String name1 = StringUtil.toSaveString(((TreeObject) e1).getSortingString());
      String name2 = StringUtil.toSaveString(((TreeObject) e2).getSortingString());

      return collator.compare(name1, name2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ViewerSorter#sort(org.eclipse.jface.viewers.Viewer,
     *      java.lang.Object[])
     */
    public void sort(Viewer viewer, Object[] elements)
    {
      // if one object has sorting enabled -> perform sort
      if (elements.length > 0 && ((TreeObject) elements[0]).sortingEnabled())
      {
        super.sort(viewer, elements);
      }
    }
  }

  /**
   * The constructor.
   */
  public ApplicationOutlineView() 
  {
    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(this);
  }

  public void dispose() 
  {
    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().removeSelectionListener(this);
    super.dispose();
  }


  /**
   * This is a callback that will allow us to create the viewer and initialize
   * it.
   */
  public void createPartControl(Composite parent)
  {
    try
    {
      viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
      viewer.setContentProvider(new ViewContentProvider());
      viewer.setLabelProvider(new ViewLabelProvider());
      viewer.setSorter(new ViewSorter());
      viewer.setInput(getViewSite());

      int ops = DND.DROP_COPY | DND.DROP_MOVE|DND.DROP_DEFAULT;

      Transfer[] transfers = new Transfer[] { TextTransfer.getInstance()/*, ModelTransfer.getInstance() */};

      viewer.addDragSupport(ops, transfers, new DragListener(viewer));
      viewer.addDropSupport(ops, transfers, new DropListener(viewer));

      makeActions();
      createContextMenu();
      createToolbar();
      hookTreeActions();
      hookGlobalActions();
      // man muss erst alle aufklappen, damit TreeItem.getItems() ALLE Kinder vom TreeItem zurückliefert.
      // Hack?!
      viewer.expandAll();
      viewer.collapseAll();
      viewer.expandToLevel(2);
      getSite().setSelectionProvider(viewer);
      JacobDesigner.getPlugin().addSelectionChangedListener(this);
    }
    catch (Throwable e)
    {
      JacobDesigner.showException(e);
    }
  }


  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
   */
  public void selectionChanged(SelectionChangedEvent event)
  {
    if (event !=null && event.getSelection() instanceof IStructuredSelection)
    {
      Object first = ((IStructuredSelection) event.getSelection()).getFirstElement();
      // Es wurde ein neues JacobModel selektiert. Dieses wird jetzt in dem
      // Tree dargestellt.
      //
      if (first instanceof JacobModel)
      {
        viewer.setContentProvider(new ViewContentProvider());
        // man muss erst alle aufklappen, damit TreeItem.getItems() ALLE Kinder vom TreeItem zurückliefert.
        // Hack?!
        viewer.expandAll();
        viewer.collapseAll();
        viewer.expandToLevel(2);
      }
    }
    else
    {
      viewer.refresh();
    }
  }

  
  public void selectionChanged(IWorkbenchPart part, ISelection selection)
  {
    

    if(inSync==false)
      return;
    if(selection instanceof IStructuredSelection)
    {
      try
      {
        Object obj = ((IStructuredSelection)selection).getFirstElement(); 
        // try to find the corresponding element in the tree
        // and display it
        //
        if(!(obj instanceof TreeSelectionObjectModelProvider))
          return;
        obj = ((TreeSelectionObjectModelProvider)obj).getTreeObjectModel();
        
        TreeItem[] items = viewer.getTree().getItems();
        for (int i = 0; i < items.length; i++)
        {
          ObjectModel model = ((TreeObject)items[i].getData()).getModel();
          if(model == obj)
          {
            viewer.setSelection(new StructuredSelection(items[i].getData()));
            viewer.getTree().showSelection();
            return;
          }
          if(checkSelection(items[i],obj))
            return;
        }
      }
      catch(Exception exc)
      {
        exc.printStackTrace();
      }
    }
  }

  public boolean checkSelection(TreeItem item, Object obj)
  {
    TreeItem[] items = item.getItems();
    for (int i = 0; i < items.length; i++)
    {
      if(items[i].getData() instanceof TreeObject)
      {
        ObjectModel model = ((TreeObject)items[i].getData()).getModel();
        if(model == obj)
        {
          viewer.setSelection(new StructuredSelection(items[i].getData()));
          viewer.getTree().showSelection();
          return true;
        }
        if(checkSelection(items[i],obj))
          return true;
      }
    }
    return false;
  }

  /**
   * 
   *  
   */
  private void makeActions()
  {
    doubleClickAction = new Action()
    {
      public void run()
      {
        ISelection selection = viewer.getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        if (obj instanceof DblClickObject)
          ((DblClickObject) obj).dblClick();
      }
    };
    collapseAllAction = new Action()
    {
      public void run()
      {
        viewer.collapseAll();
        viewer.expandToLevel(2);

        // Falls der Baum im "sync" mode ist wird versucht das Model des aktuellen Editors im
        // Baum zu selektieren.
        //
        IEditorInput input = getSite().getWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput();
        if(input instanceof TreeSelectionObjectModelProvider)
          selectionChanged(null, new StructuredSelection(input));
      }
    };
    collapseAllAction.setImageDescriptor(JacobDesigner.getImageDescriptor("collapseall.gif"));

    synchAction = new Action(null, Action.AS_CHECK_BOX)
    {
      public void run()
      {
        inSync = !inSync;
        if(inSync)
        {
          // Aktuelles Fenster holen und versuchen das ObjectModel im Baum anzuzeigen
          //
          IEditorInput input = getSite().getWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput();
          if(input instanceof TreeSelectionObjectModelProvider)
            selectionChanged(null, new StructuredSelection(input));
          
        }
      }
    };
    synchAction.setImageDescriptor(JacobDesigner.getImageDescriptor("synced.gif"));

    expandAllAction = new Action()
    {
      public void run()
      {
        viewer.expandAll();
      }
    };
    expandAllAction.setImageDescriptor(JacobDesigner.getImageDescriptor("expandall.png"));
    
    deleteItemAction = new DeleteLinkedFormAction(viewer.getTree());
  }

  /**
   * 
   *
   */
  private void hookTreeActions()
  {
    viewer.getTree().addListener(SWT.Selection, new TreeInPlaceEditor(viewer.getTree()));

    TooltipListener tableListener = new TooltipListener(viewer.getTree());
    viewer.getTree().addListener (SWT.Dispose, tableListener);
    viewer.getTree().addListener (SWT.KeyDown, tableListener);
    viewer.getTree().addListener (SWT.MouseMove, tableListener);
    viewer.getTree().addListener (SWT.MouseHover, tableListener);    
    
    viewer.addDoubleClickListener(new IDoubleClickListener()
    {
      public void doubleClick(DoubleClickEvent event)
      {
        doubleClickAction.run();
      }
    });
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
     Menu menu = menuMgr.createContextMenu(viewer.getControl());
     viewer.getControl().setMenu(menu);
     // Register menu for extension.
     getSite().registerContextMenu(menuMgr, viewer);
     getSite().setSelectionProvider(viewer);
  }

  
  private void createToolbar() 
  {
    IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
    mgr.add(expandAllAction);
    mgr.add(collapseAllAction);
    mgr.add(synchAction);
  }
  /**
   * 
   *
   */
  private void hookGlobalActions()
  {
    IActionBars bars = getViewSite().getActionBars();

    //bars.setGlobalActionHandler(IWorkbenchActionConstants.SELECT_ALL, selectAllAction);
    bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteItemAction);
//    bars.updateActionBars();
  }
  
  /**
   * Passing the focus request to the viewer's control.
   */
  public void setFocus()
  {
    viewer.getControl().setFocus();
  }

  public TreeViewer getViewer()
  {
    return viewer;
  }
}