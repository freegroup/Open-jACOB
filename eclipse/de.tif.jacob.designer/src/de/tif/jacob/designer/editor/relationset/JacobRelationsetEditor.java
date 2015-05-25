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
 * Created on 10.12.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.editor.relationset;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GEFPlugin;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.MatchHeightAction;
import org.eclipse.gef.ui.actions.MatchWidthAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.jacobform.ContextMenuProviderImpl;
import de.tif.jacob.designer.editor.relationset.dnd.DropListener;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.RelationsetStickyNoteModel;


/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class JacobRelationsetEditor extends GraphicalEditorWithPalette implements PropertyChangeListener, ITabbedPropertySheetPageContributor 
{
  public static final String ID = "de.tif.jacob.designer.editor.relationset.relationseteditor";

  private RelationsetModel  content;
  private PropertySheetPage propPage;
  /** the overview outline page */
  private OverviewOutlinePage overviewOutlinePage;

  
  /**
   * No-arg constructor
   */
  public JacobRelationsetEditor()
  {
    setEditDomain(new DefaultEditDomain(this));
  }

  public CommandStack getCommandStack()
  {
    return super.getCommandStack();
  }
  

  public String getContributorId()
  {
    return getSite().getId();
  }

  /**
   * Returns the overview for the outline view.
   * 
   * @return the overview
   */
  protected OverviewOutlinePage getOverviewOutlinePage()
  {
      if (null == overviewOutlinePage && null != getGraphicalViewer())
      {
          RootEditPart rootEditPart = getGraphicalViewer().getRootEditPart();
          if (rootEditPart instanceof ScalableFreeformRootEditPart)
          {
              overviewOutlinePage = new OverviewOutlinePage((ScalableFreeformRootEditPart) rootEditPart);
          }
      }

      return overviewOutlinePage;
  }

  /**
	 * Returns the <code>PaletteRoot</code> this editor's palette uses.
	 * 
	 * @return the <code>PaletteRoot</code>
	 */
	public PaletteRoot getPaletteRoot()
	{
		// create root
	  PaletteRoot paletteRoot = new PaletteRoot();

		// a group of default control tools
		PaletteGroup controls = new PaletteGroup("Controls");
		paletteRoot.add(controls);

		// the selection tool
		ToolEntry tool = new SelectionToolEntry();
		controls.add(tool);

		// use selection tool as default entry
		paletteRoot.setDefaultEntry(tool);

		// the marquee selection tool
		controls.add(new MarqueeToolEntry());

		// a separator
		PaletteSeparator separator = new PaletteSeparator(JacobRelationsetEditor.ID + ".palette.seperator");
		separator.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);
		controls.add(separator);

		controls.add(new ConnectionCreationToolEntry("Relation", "Create a Relationship between two tables", null, 
		    JacobDesigner.getImageDescriptor("toolbar_relation32.png"),
		    JacobDesigner.getImageDescriptor("toolbar_relation32.png")));
		
		tool = new CreationToolEntry("Annotation", "Create an annotation", 
        new SimpleFactory(RelationsetStickyNoteModel.class), 
        JacobDesigner.getImageDescriptor("toolbar_annotation16.png"), 
        JacobDesigner.getImageDescriptor("toolbar_annotation32.png"));
    controls.add(tool);

		return paletteRoot;
	}

	
  protected void configureGraphicalViewer()
  {
		GraphicalViewer viewer = getGraphicalViewer();
    
		//root editpart:
    ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
		rootEditPart.getContentPane().setBackgroundColor(Constants.COLOR_WORKAREA);
		rootEditPart.getContentPane().setOpaque(true);
		viewer.setRootEditPart(rootEditPart);
		
    //menu contextuel:
		MenuManager menuManager = new ContextMenuProviderImpl(getActionRegistry(), getGraphicalViewer());
    viewer.setContextMenu(menuManager);
    getSite().registerContextMenu(menuManager, viewer);
    getSite().setSelectionProvider(viewer);
    
    //keyhandler:
    KeyHandler keyHandler = new KeyHandler();
    keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), getActionRegistry().getAction(GEFActionConstants.DELETE));
		keyHandler.put(KeyStroke.getPressed(SWT.F2, 0),	getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
    viewer.setKeyHandler(new GraphicalViewerKeyHandler(getGraphicalViewer()).setParent(keyHandler));

		// ZoomManager
		ZoomManager manager = rootEditPart.getZoomManager();
		
		double[] zoomLevels = new double[] {
		  0.25,0.5,0.75,1.0,1.5,2.0,2.5,3.0,4.0,5.0,10.0,20.0
		};
		manager.setZoomLevels(zoomLevels);
    
		ArrayList zoomContributions = new ArrayList();
		zoomContributions.add(ZoomManager.FIT_ALL);
		zoomContributions.add(ZoomManager.FIT_HEIGHT);
		zoomContributions.add(ZoomManager.FIT_WIDTH);
		manager.setZoomLevelContributions(zoomContributions);

		IAction action = new ZoomInAction(manager);
		getActionRegistry().registerAction(action);

		action = new ZoomOutAction(manager);
		getActionRegistry().registerAction(action);
  }


  protected void initializeGraphicalViewer()
  {
    getGraphicalViewer().setEditPartFactory(new EditPartFactoryImpl());
    getGraphicalViewer().setContents(getContents());
    getGraphicalViewer().addDropTargetListener(new DropListener(getGraphicalViewer(), content, getEditDomain().getCommandStack()));
  }

  protected RelationsetModel getContents()
  {
    if (content == null)
      content = createContents();
    return content;
  }

  protected RelationsetModel createContents()
  {
    JacobRelationsetEditorInput jacobInput  = (JacobRelationsetEditorInput)getEditorInput();
    setPartName(jacobInput.getRelationsetModel().getName());
    setTitleImage(jacobInput.getRelationsetModel().getImage());
    jacobInput.getRelationsetModel().addPropertyChangeListener(this);
    jacobInput.getRelationsetModel().getJacobModel().addPropertyChangeListener(this);
    
    return jacobInput.getRelationsetModel();
  }
  
  public void createActions()
  {
      super.createActions();
      ActionRegistry registry = getActionRegistry();
      IAction action;

      action = new MatchWidthAction(this);
      registry.registerAction(action);
      getSelectionActions().add(action.getId());
       
      action = new MatchHeightAction(this);
      registry.registerAction(action);
      getSelectionActions().add(action.getId());

      action =new AlignmentAction((IWorkbenchPart) this, PositionConstants.LEFT);
  		registry.registerAction(action);
  		getSelectionActions().add(action.getId());

  		action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.CENTER);
  		registry.registerAction(action);
  		getSelectionActions().add(action.getId());

  		action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.RIGHT);
  		registry.registerAction(action);
  		getSelectionActions().add(action.getId());

  		action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.TOP);
  		registry.registerAction(action);
  		getSelectionActions().add(action.getId());

  		action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.MIDDLE);
  		registry.registerAction(action);
  		getSelectionActions().add(action.getId());

  		action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.BOTTOM);
  		registry.registerAction(action);
  		getSelectionActions().add(action.getId());
  }


  public void createPartControl(Composite parent)
  {
    super.createPartControl(parent);
    getEditorSite().getActionBars().setGlobalActionHandler(IWorkbenchActionConstants.UNDO, getActionRegistry().getAction(GEFActionConstants.UNDO));
    getEditorSite().getActionBars().setGlobalActionHandler(IWorkbenchActionConstants.REDO, getActionRegistry().getAction(GEFActionConstants.REDO));
    getEditorSite().getActionBars().setGlobalActionHandler(IWorkbenchActionConstants.DELETE, getActionRegistry().getAction(GEFActionConstants.DELETE));
    
  }

  /**
   * Adaptable implementation for Editor
   */
  public Object getAdapter(Class adapter)
  {
    // we need to handle common GEF elements we created
    if (adapter == IContentOutlinePage.class)
      return getOverviewOutlinePage();
    if (adapter == CommandStack.class)
      return getCommandStack();
    else if (adapter == IPropertySheetPage.class)
      return getPropertySheetPage();

    return super.getAdapter(adapter);
  }


  public IPropertySheetPage getPropertySheetPage()
  {
    /*
    if (propPage == null)
    {
      propPage = new PropertySheetPage();
      propPage.setRootEntry(GEFPlugin.createUndoablePropertySheetEntry(getCommandStack()));
    }
    return propPage;
    */
    return new TabbedPropertySheetPage(this);
  }

  /**
   * Save as not allowed
   */
  public void doSaveAs()
  {
  }

  public boolean isDirty()
  {
    return false;
  }

  public boolean isSaveAsAllowed()
  {
    return false;
  }
  
  public void doSave(IProgressMonitor monitor)
  {
    JacobDesigner.getPlugin().saveCurrentModel();
  }
 

	/* 
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() 
	{
    JacobRelationsetEditorInput jacobInput  = (JacobRelationsetEditorInput)getEditorInput();
    jacobInput.getRelationsetModel().removePropertyChangeListener(this);
    jacobInput.getRelationsetModel().getJacobModel().removePropertyChangeListener(this);
    
		super.dispose();
	}

	public void propertyChange(PropertyChangeEvent ev)
	{
		if(ev.getSource() == content)
		{
		  setPartName(content.getName());
		  setTitleImage(content.getImage());
		}
		else if(ev.getPropertyName()==ObjectModel.PROPERTY_JACOBMODEL_CLOSED)
		  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(this, false);
    else if(ev.getPropertyName()==ObjectModel.PROPERTY_RELATIONSET_DELETED && ev.getOldValue()==this.getRelationsetModel())
      PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(this, false);
	}

	public RelationsetModel getRelationsetModel()
	{
	  return content;
	}
}