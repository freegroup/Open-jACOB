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
package de.tif.jacob.designer.editor.diagram.activity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GEFPlugin;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.MatchHeightAction;
import org.eclipse.gef.ui.actions.MatchWidthAction;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ToggleRulerVisibilityAction;
import org.eclipse.gef.ui.actions.ToggleSnapToGeometryAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;
import de.tif.jacob.designer.editor.diagram.activity.editpart.DiagramElementEditPartFactory;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.diagram.activity.ActivityDiagramModel;

/**
 * A graphical editor with flyout palette that can edit .shapes files. The
 * binding between the .shapes file extension and this editor is done in
 * plugin.xml
 * 
 * @author Elias Volanakis
 */
public class ActivityDiagramEditor extends  GraphicalEditorWithPalette  implements PropertyChangeListener
{
  public static final String ID = "de.tif.jacob.designer.editor.diagram.activity.activiyDiagramEditor";

  private ActivityDiagramModel diagram;
  private PaletteRoot palette;
  private PropertySheetPage propPage;
//  private RulerComposite rulerComp;

  /** Create a new ShapesEditor instance. This is called by the Workspace. */
  public ActivityDiagramEditor()
  {
    setEditDomain(new DefaultEditDomain(this));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPaletteRoot()
   */
  protected PaletteRoot getPaletteRoot()
  {
    if (palette == null)
    {
      palette = ShapesEditorPaletteFactory.createPalette();
    }
    return palette;
  }

  /**
   * Configure the graphical viewer before it receives contents.
   * <p>
   * This is the place to choose an appropriate RootEditPart and EditPartFactory
   * for your editor. The RootEditPart determines the behavior of the editor's
   * "work-area". For example, GEF includes zoomable and scrollable root edit
   * parts. The EditPartFactory maps model elements to edit parts (controllers).
   * </p>
   * 
   * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
   */
  protected void configureGraphicalViewer()
  {
  	GraphicalViewer viewer = getGraphicalViewer();

  	ScalableRootEditPart rootEditPart = new ScalableRootEditPart();

		rootEditPart.getContentPane().setBackgroundColor(new Color(null,250,250,250));
		rootEditPart.getContentPane().setOpaque(true);
    viewer.setRootEditPart(rootEditPart);

    
    // configure the context menu provider
    ContextMenuProvider cmProvider = new ShapesEditorContextMenuProvider(viewer, getActionRegistry());
    viewer.setContextMenu(cmProvider);
    getSite().registerContextMenu(cmProvider, viewer);

    
    //keyhandler:
    KeyHandler keyHandler = new KeyHandler();
    keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), getActionRegistry().getAction(GEFActionConstants.DELETE));
		keyHandler.put(KeyStroke.getPressed(SWT.F2, 0),	getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
    viewer.setKeyHandler(new GraphicalViewerKeyHandler(getGraphicalViewer()).setParent(keyHandler));

  	// Actions
  	IAction showRulers = new ToggleRulerVisibilityAction(getGraphicalViewer());
  	getActionRegistry().registerAction(showRulers);
  	
  	IAction snapAction = new ToggleSnapToGeometryAction(getGraphicalViewer());
  	getActionRegistry().registerAction(snapAction);

  	IAction showGrid = new ToggleGridAction(getGraphicalViewer());
  	getActionRegistry().registerAction(showGrid);
  	
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
    getGraphicalViewer().setEditPartFactory(new DiagramElementEditPartFactory());
    getGraphicalViewer().setContents(getContents());
  }

  protected ActivityDiagramModel getContents()
  {
    if (diagram == null)
      diagram = createContents();
    return diagram;
  }

  protected ActivityDiagramModel createContents()
  {
    ActivityDiagramEditorInput jacobInput  = (ActivityDiagramEditorInput)getEditorInput();
    setPartName(jacobInput.getActivityDiagramModel().getName());

    jacobInput.getActivityDiagramModel().addPropertyChangeListener(this);
    jacobInput.getActivityDiagramModel().getJacobModel().addPropertyChangeListener(this);
    
    return jacobInput.getActivityDiagramModel();
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

  public Object getAdapter(Class aClass)
  {
      if (aClass == CommandStack.class)
        return getCommandStack();
      else if (aClass == IPropertySheetPage.class)
        return getPropertySheetPage();
      return super.getAdapter(aClass);
  }


  public PropertySheetPage getPropertySheetPage()
  {
    if (propPage == null)
    {
      propPage = new PropertySheetPage();
      propPage.setRootEntry(GEFPlugin.createUndoablePropertySheetEntry(getCommandStack()));
    }
    return propPage;
  }



  public void doSave(IProgressMonitor monitor){}
  public void doSaveAs(){}
    
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.ISaveablePart#isDirty()
   */
  public boolean isDirty()
  {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
   */
  public boolean isSaveAsAllowed()
  {
    return false;
  }
  
	public void propertyChange(PropertyChangeEvent ev)
	{
		if(ev.getPropertyName()==ObjectModel.PROPERTY_NAME_CHANGED)
		  setPartName((String)ev.getNewValue());
		
		if(ev.getPropertyName()==ObjectModel.PROPERTY_ACTIVITYDIAGRAM_DELETED)
		  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(this, false);

		if(ev.getPropertyName()==ObjectModel.PROPERTY_JACOBMODEL_CLOSED)
		  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(this, false);
	}
}


