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
package de.tif.jacob.designer.editor.jacobform;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GEFPlugin;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.rulers.RulerProvider;
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
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.rulers.RulerComposite;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;

import de.tif.jacob.components.plugin.IComponentPlugin;
import de.tif.jacob.components.plugin.PluginComponentManager;
import de.tif.jacob.components.plugin.PluginComponentManager.Group;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.dnd.DropListener;
import de.tif.jacob.designer.editor.jacobform.rulers.FormRulerProvider;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIBarChartModel;
import de.tif.jacob.designer.model.UIBreadCrumbModel;
import de.tif.jacob.designer.model.UIButtonModel;
import de.tif.jacob.designer.model.UICalendarModel;
import de.tif.jacob.designer.model.UICanvasModel;
import de.tif.jacob.designer.model.UICheckboxModel;
import de.tif.jacob.designer.model.UIComboboxModel;
import de.tif.jacob.designer.model.UIDBCheckboxModel;
import de.tif.jacob.designer.model.UIDBEnumComboboxModel;
import de.tif.jacob.designer.model.UIDBDateModel;
import de.tif.jacob.designer.model.UIDBDateTimeModel;
import de.tif.jacob.designer.model.UIDBDocumentModel;
import de.tif.jacob.designer.model.UIDBForeignFieldModel;
import de.tif.jacob.designer.model.UIDBImageModel;
import de.tif.jacob.designer.model.UIDBInformBrowserModel;
import de.tif.jacob.designer.model.UIDBInformLongTextModel;
import de.tif.jacob.designer.model.UIDBLabelModel;
import de.tif.jacob.designer.model.UIDBListboxModel;
import de.tif.jacob.designer.model.UIDBLongTextModel;
import de.tif.jacob.designer.model.UIDBPasswordModel;
import de.tif.jacob.designer.model.UIDBRadioButtonGroupModel;
import de.tif.jacob.designer.model.UIDBTableListBoxModel;
import de.tif.jacob.designer.model.UIDBTextModel;
import de.tif.jacob.designer.model.UIDBTextfieldComboboxModel;
import de.tif.jacob.designer.model.UIDBTimeModel;
import de.tif.jacob.designer.model.UIDateModel;
import de.tif.jacob.designer.model.UIDateTimeModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UIHeadingModel;
import de.tif.jacob.designer.model.UIHorizontalButtonBarModel;
import de.tif.jacob.designer.model.UIInformLongTextModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.designer.model.UILabelModel;
import de.tif.jacob.designer.model.UILineChartModel;
import de.tif.jacob.designer.model.UIListboxModel;
import de.tif.jacob.designer.model.UIPasswordModel;
import de.tif.jacob.designer.model.UIPluginComponentModel;
import de.tif.jacob.designer.model.UIStaticImageModel;
import de.tif.jacob.designer.model.UIStyledTextModel;
import de.tif.jacob.designer.model.UITabContainerModel;
import de.tif.jacob.designer.model.UITextModel;
import de.tif.jacob.designer.model.UITimeLineModel;
import de.tif.jacob.designer.model.UITimeModel;
import de.tif.jacob.designer.model.UIVerticalButtonBarModel;

public class JacobFormEditor extends GraphicalEditorWithPalette implements PropertyChangeListener
{
  public static final String ID    = "de.tif.jacob.designer.editor.jacobform.formeditor";

  private PropertySheetPage propPage;
  /** the overview outline page */
  private OverviewOutlinePage overviewOutlinePage;

  private UIJacobFormModel    content;
  private RulerComposite rulerComp;
  
  private PaletteDrawer groupDataControls;
  private PaletteDrawer groupChartControls;
  private PaletteDrawer groupNormalControls;
  private PaletteDrawer groupContainerControls;
  private PaletteDrawer groupButtonControls;
  
  private ToolEntry     toolGroup;
  
  public JacobFormEditor()
  {
    setEditDomain(new DefaultEditDomain(this));
  }

  private void updatePalletteState()
  {
    if(content.getElements().size()==0)
    {
      toolGroup.setVisible(true);
      groupDataControls.setVisible(false);      
      groupChartControls.setVisible(false);      
      groupNormalControls.setVisible(false);      
      groupContainerControls.setVisible(false);      
      groupButtonControls.setVisible(false);      
    }
    else
    {
      toolGroup.setVisible(false);
      groupDataControls.setVisible(true);      
      groupChartControls.setVisible(true);      
      groupNormalControls.setVisible(true);      
      groupContainerControls.setVisible(true);      
      groupButtonControls.setVisible(true);      
    }
  }

  protected PaletteRoot getPaletteRoot()
  {
    
    PaletteRoot root = new PaletteRoot();

    ToolEntry tool=null;
    PaletteGroup tools = new PaletteGroup("edit tools");
    
    tool =new SelectionToolEntry();
    tools.add(tool);
    root.setDefaultEntry(tool);
    
    tool = new MarqueeToolEntry();
    tools.add(tool);

    toolGroup = new CreationToolEntry("Group", "Create a UI representation of an table alias", new SimpleFactory(UIGroupModel.class), JacobDesigner.getImageDescriptor("toolbar_group16.png"), JacobDesigner.getImageDescriptor("toolbar_group32.png"));
    tools.add(toolGroup);

    root.add(tools);

    groupDataControls = new PaletteDrawer("Data Controls");

    root.add(groupDataControls);
    tool = new CreationToolEntry("Group", "Create a UI representation of an table alias", new SimpleFactory(UIGroupModel.class), JacobDesigner.getImageDescriptor("toolbar_group16.png"), JacobDesigner.getImageDescriptor("toolbar_group32.png"));
    groupDataControls.add(tool);
    
    tool = new CreationToolEntry("Text", "Create a text input field", new SimpleFactory(UIDBTextModel.class), JacobDesigner.getImageDescriptor("toolbar_text16.gif"), JacobDesigner.getImageDescriptor("toolbar_text32.gif"));
    groupDataControls.add(tool);

    tool = new CreationToolEntry("Password", "Create a password input field", new SimpleFactory(UIDBPasswordModel.class), JacobDesigner.getImageDescriptor("toolbar_password16.gif"), JacobDesigner.getImageDescriptor("toolbar_password32.gif"));
    groupDataControls.add(tool);

    tool = new CreationToolEntry("Long text", "Create a long text input field", new SimpleFactory(UIDBInformLongTextModel.class), JacobDesigner.getImageDescriptor("toolbar_informlongtext16.gif"), JacobDesigner.getImageDescriptor("toolbar_informlongtext32.gif"));
    groupDataControls.add(tool);

    tool = new CreationToolEntry("Long text Dialog", "Create a long text dialog", new SimpleFactory(UIDBLongTextModel.class), JacobDesigner.getImageDescriptor("toolbar_longtext16.gif"), JacobDesigner.getImageDescriptor("toolbar_longtext32.gif"));
    groupDataControls.add(tool);

    tool = new CreationToolEntry("Checkbox", "Create a check box", new SimpleFactory(UIDBCheckboxModel.class), JacobDesigner.getImageDescriptor("toolbar_checkbox16.gif"), JacobDesigner.getImageDescriptor("toolbar_checkbox32.gif"));
    groupDataControls.add(tool);
    
    tool = new CreationToolEntry("Combobox", "Create a combobox", new SimpleFactory(UIDBEnumComboboxModel.class), JacobDesigner.getImageDescriptor("toolbar_combobox16.gif"), JacobDesigner.getImageDescriptor("toolbar_combobox32.gif"));
    groupDataControls.add(tool);
    
    tool = new CreationToolEntry("Textfield Combobox", "Create a textfield linked combobox", new SimpleFactory(UIDBTextfieldComboboxModel.class), JacobDesigner.getImageDescriptor("toolbar_combobox16.gif"), JacobDesigner.getImageDescriptor("toolbar_combobox32.gif"));
    groupDataControls.add(tool);
    
    tool = new CreationToolEntry("Radiobutton", "Create a radio button group", new SimpleFactory(UIDBRadioButtonGroupModel.class), JacobDesigner.getImageDescriptor("toolbar_radio16.gif"), JacobDesigner.getImageDescriptor("toolbar_radio32.gif"));
    groupDataControls.add(tool);
    
    tool = new CreationToolEntry("Enum Listbox", "Create a listbox", new SimpleFactory(UIDBListboxModel.class), JacobDesigner.getImageDescriptor("toolbar_listbox16.gif"), JacobDesigner.getImageDescriptor("toolbar_listbox32.gif"));
    groupDataControls.add(tool);
    
    tool = new CreationToolEntry("Table Listbox", "Create a listbox", new SimpleFactory(UIDBTableListBoxModel.class), JacobDesigner.getImageDescriptor("toolbar_listbox16.gif"), JacobDesigner.getImageDescriptor("toolbar_listbox32.gif"));
    groupDataControls.add(tool);
    
    tool = new CreationToolEntry("Time", "Create a time input field", new SimpleFactory(UIDBTimeModel.class), JacobDesigner.getImageDescriptor("toolbar_time16.gif"), JacobDesigner.getImageDescriptor("toolbar_time32.gif"));
    groupDataControls.add(tool);

    tool = new CreationToolEntry("Date", "Create a date input field", new SimpleFactory(UIDBDateModel.class), JacobDesigner.getImageDescriptor("toolbar_date16.gif"), JacobDesigner.getImageDescriptor("toolbar_date32.gif"));
    groupDataControls.add(tool);

    tool = new CreationToolEntry("Timestamp", "Create a time stamp input field", new SimpleFactory(UIDBDateTimeModel.class), JacobDesigner.getImageDescriptor("toolbar_datetime16.png"), JacobDesigner.getImageDescriptor("toolbar_datetime32.png"));
    groupDataControls.add(tool);
    
    tool = new CreationToolEntry("Label", "Create a label", new SimpleFactory(UIDBLabelModel.class), JacobDesigner.getImageDescriptor("toolbar_label16.gif"), JacobDesigner.getImageDescriptor("toolbar_label32.gif"));
    groupDataControls.add(tool);
    
    tool = new CreationToolEntry("Table view", "Insert a foreign table", new SimpleFactory(UIDBInformBrowserModel.class), JacobDesigner.getImageDescriptor("toolbar_informbrowser16.gif"), JacobDesigner.getImageDescriptor("toolbar_informbrowser32.gif"));
    groupDataControls.add(tool);
    
    tool = new CreationToolEntry("ForeignField", "Create a field to foreign table", new SimpleFactory(UIDBForeignFieldModel.class), JacobDesigner.getImageDescriptor("toolbar_foreignfield16.gif"), JacobDesigner.getImageDescriptor("toolbar_foreingfield32.gif"));
    groupDataControls.add(tool);

    tool = new CreationToolEntry("Image", "Create a Image object", new SimpleFactory(UIDBImageModel.class), JacobDesigner.getImageDescriptor("toolbar_image16.png"), JacobDesigner.getImageDescriptor("toolbar_image32.png"));
    groupDataControls.add(tool);

    tool = new CreationToolEntry("Document", "Create a Document object", new SimpleFactory(UIDBDocumentModel.class), JacobDesigner.getImageDescriptor("toolbar_document16.gif"), JacobDesigner.getImageDescriptor("toolbar_document32.gif"));
    groupDataControls.add(tool);
    

    groupNormalControls = new PaletteDrawer("Normal Controls");
    groupNormalControls.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
    root.add(groupNormalControls);
    
    tool = new CreationToolEntry("Text", "Create a text input field", new SimpleFactory(UITextModel.class), JacobDesigner.getImageDescriptor("toolbar_text16.gif"), JacobDesigner.getImageDescriptor("toolbar_text32.gif"));
    groupNormalControls.add(tool);
    
    tool = new CreationToolEntry("Password", "Create a password input field", new SimpleFactory(UIPasswordModel.class), JacobDesigner.getImageDescriptor("toolbar_password16.gif"), JacobDesigner.getImageDescriptor("toolbar_password32.gif"));
    groupNormalControls.add(tool);
    
    tool = new CreationToolEntry("Long text", "Create a longtext input field", new SimpleFactory(UIInformLongTextModel.class), JacobDesigner.getImageDescriptor("toolbar_informlongtext16.gif"), JacobDesigner.getImageDescriptor("toolbar_informlongtext32.gif"));
    groupNormalControls.add(tool);

    tool = new CreationToolEntry("Checkbox", "Create a check box", new SimpleFactory(UICheckboxModel.class), JacobDesigner.getImageDescriptor("toolbar_checkbox16.gif"), JacobDesigner.getImageDescriptor("toolbar_checkbox32.gif"));
    groupNormalControls.add(tool);
    
    tool = new CreationToolEntry("Combobox", "Create a combobox", new SimpleFactory(UIComboboxModel.class), JacobDesigner.getImageDescriptor("toolbar_combobox16.gif"), JacobDesigner.getImageDescriptor("toolbar_combobox32.gif"));
    groupNormalControls.add(tool);
    
    tool = new CreationToolEntry("Listbox", "Create a listbox", new SimpleFactory(UIListboxModel.class), JacobDesigner.getImageDescriptor("toolbar_listbox16.gif"), JacobDesigner.getImageDescriptor("toolbar_listbox32.gif"));
    groupNormalControls.add(tool);
    
    tool = new CreationToolEntry("Calendar", "Create a embedded calendar", new SimpleFactory(UICalendarModel.class), JacobDesigner.getImageDescriptor("toolbar_calendar16.gif"), JacobDesigner.getImageDescriptor("toolbar_calendar32.gif"));
    groupNormalControls.add(tool);

    tool = new CreationToolEntry("Time", "Create a time input field", new SimpleFactory(UITimeModel.class), JacobDesigner.getImageDescriptor("toolbar_time16.gif"), JacobDesigner.getImageDescriptor("toolbar_time32.gif"));
    groupNormalControls.add(tool);
    
    tool = new CreationToolEntry("Date", "Create a date input field", new SimpleFactory(UIDateModel.class), JacobDesigner.getImageDescriptor("toolbar_date16.gif"), JacobDesigner.getImageDescriptor("toolbar_date32.gif"));
    groupNormalControls.add(tool);
    
    tool = new CreationToolEntry("Timestamp", "Create a time stamp input field", new SimpleFactory(UIDateTimeModel.class), JacobDesigner.getImageDescriptor("toolbar_datetime16.png"), JacobDesigner.getImageDescriptor("toolbar_datetime32.png"));
    groupNormalControls.add(tool);
    
    tool = new CreationToolEntry("Heading", "Create a heading caption", new SimpleFactory(UIHeadingModel.class), JacobDesigner.getImageDescriptor("toolbar_heading16.png"), JacobDesigner.getImageDescriptor("toolbar_heading32.png"));
    groupNormalControls.add(tool);
    
    tool = new CreationToolEntry("Label", "Create a label", new SimpleFactory(UILabelModel.class), JacobDesigner.getImageDescriptor("toolbar_label16.gif"), JacobDesigner.getImageDescriptor("toolbar_label32.gif"));
    groupNormalControls.add(tool);
    
    tool = new CreationToolEntry("Bread Crumb", "Create a BreadCrumb", new SimpleFactory(UIBreadCrumbModel.class), JacobDesigner.getImageDescriptor("toolbar_breadcrumb16.png"), JacobDesigner.getImageDescriptor("toolbar_breadcrumb32.png"));
    groupNormalControls.add(tool);
    
    tool = new CreationToolEntry("Styled Text", "Create a Styled Text", new SimpleFactory(UIStyledTextModel.class), JacobDesigner.getImageDescriptor("toolbar_styledtext16.png"), JacobDesigner.getImageDescriptor("toolbar_styledtext32.png"));
    groupNormalControls.add(tool);
    
    tool = new CreationToolEntry("Canvas", "Create a new canvas element", new SimpleFactory(UICanvasModel.class), JacobDesigner.getImageDescriptor("toolbar_canvas16.gif"), JacobDesigner.getImageDescriptor("toolbar_canvas32.gif"));
    groupNormalControls.add(tool);

    tool = new CreationToolEntry("Image", "Create a Image object", new SimpleFactory(UIStaticImageModel.class), JacobDesigner.getImageDescriptor("toolbar_staticimage16.png"), JacobDesigner.getImageDescriptor("toolbar_staticimage32.png"));
    groupNormalControls.add(tool);

    Collection<IComponentPlugin> components = PluginComponentManager.createComponentPlugin(Group.normal);
    for (IComponentPlugin component : components)
    {
      if(component.getPaletteImage()!=null)
      {
        tool = new CreationToolEntry(component.getName(), component.getName(), 
            new PluginComponentFactory(
                UIPluginComponentModel.class, 
                component.getPluginId(),
                component.getPluginVersion(),
                component.getJavaImplClass()
                ), component.getPaletteImage(), component.getPaletteImage());
        groupNormalControls.add(tool);
      }
    }
    
    groupButtonControls = new PaletteDrawer("Buttons");
    groupButtonControls.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
    root.add(groupButtonControls);

    tool = new CreationToolEntry("New", "Create a 'new record' button", new ButtonFactory(UIButtonModel.class,ObjectModel.ACTION_NEWRECORD), JacobDesigner.getImageDescriptor("toolbar_button16.png"), JacobDesigner.getImageDescriptor("toolbar_button32.png"));
    groupButtonControls.add(tool);
    
    tool = new CreationToolEntry("Update", "Create an update button", new ButtonFactory(UIButtonModel.class,ObjectModel.ACTION_UPDATERECORD), JacobDesigner.getImageDescriptor("toolbar_button16.png"), JacobDesigner.getImageDescriptor("toolbar_button32.png"));
    groupButtonControls.add(tool);
    
    tool = new CreationToolEntry("Clear", "Create a clear group button", new ButtonFactory(UIButtonModel.class,ObjectModel.ACTION_CLEARGROUP), JacobDesigner.getImageDescriptor("toolbar_button16.png"), JacobDesigner.getImageDescriptor("toolbar_button32.png"));
    groupButtonControls.add(tool);

    tool = new CreationToolEntry("Delete", "Create a delete record button", new ButtonFactory(UIButtonModel.class,ObjectModel.ACTION_DELETERECORD), JacobDesigner.getPlugin().getImageDescriptor("toolbar_button16.png"), JacobDesigner.getImageDescriptor("toolbar_button32.png"));
    groupButtonControls.add(tool);
    
    tool = new CreationToolEntry("Search", "Create a search button", new ButtonFactory(UIButtonModel.class,ObjectModel.ACTION_SEARCH), JacobDesigner.getImageDescriptor("toolbar_button16.png"), JacobDesigner.getImageDescriptor("toolbar_button32.png"));
    groupButtonControls.add(tool);
    
    tool = new CreationToolEntry("SearchUpdate", "Create a search/update button", new ButtonFactory(UIButtonModel.class,ObjectModel.ACTION_SEARCH_UPDATE), JacobDesigner.getImageDescriptor("toolbar_button16.png"), JacobDesigner.getImageDescriptor("toolbar_button32.png"));
    groupButtonControls.add(tool);
    
    tool = new CreationToolEntry("Selected", "Create a generic button which is only enabled if a record in this group is selected", new ButtonFactory(UIButtonModel.class,ObjectModel.ACTION_SELECTED), JacobDesigner.getImageDescriptor("toolbar_button16.png"), JacobDesigner.getImageDescriptor("toolbar_button32.png"));
    groupButtonControls.add(tool);
    
    tool = new CreationToolEntry("Generic", "Create a generic button", new ButtonFactory(UIButtonModel.class,ObjectModel.ACTION_GENERIC), JacobDesigner.getImageDescriptor("toolbar_button16.png"), JacobDesigner.getImageDescriptor("toolbar_button32.png"));
    groupButtonControls.add(tool);
    components = PluginComponentManager.createComponentPlugin(Group.button);
    for (IComponentPlugin component : components)
    {
      if(component.getPaletteImage()!=null)
      {
        tool = new CreationToolEntry(component.getName(), component.getName(), 
            new PluginComponentFactory(
                UIPluginComponentModel.class, 
                component.getPluginId(),
                component.getPluginVersion(),
                component.getJavaImplClass()
                ), component.getPaletteImage(), component.getPaletteImage());
        groupButtonControls.add(tool);
      }
    }
       

    groupContainerControls = new PaletteDrawer("Container");
    groupContainerControls.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
    root.add(groupContainerControls);

    tool = new CreationToolEntry("TabPane", "Create a tabpane input field", new SimpleFactory(UITabContainerModel.class), JacobDesigner.getImageDescriptor("toolbar_tabcontainer16.gif"), JacobDesigner.getImageDescriptor("toolbar_tabcontainer32.gif"));
    groupContainerControls.add(tool);
    
    tool = new CreationToolEntry("Toolbar", "Create a Horizontal Toolbar", new SimpleFactory(UIHorizontalButtonBarModel.class), JacobDesigner.getImageDescriptor("toolbar_hbuttonbar16.png"), JacobDesigner.getImageDescriptor("toolbar_hbuttonbar32.png"));
    groupContainerControls.add(tool);
    
    tool = new CreationToolEntry("Toolbar", "Create a Vertical Toolbar", new SimpleFactory(UIVerticalButtonBarModel.class), JacobDesigner.getImageDescriptor("toolbar_vbuttonbar16.png"), JacobDesigner.getImageDescriptor("toolbar_vbuttonbar32.png"));
    groupContainerControls.add(tool);

    groupChartControls = new PaletteDrawer("Charts");
    groupChartControls.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
    root.add(groupChartControls);
    
    tool = new CreationToolEntry("Line Chart", "Create a line chart diagram", new SimpleFactory(UILineChartModel.class), JacobDesigner.getImageDescriptor("toolbar_linechart16.png"), JacobDesigner.getImageDescriptor("toolbar_linechart32.png"));
    groupChartControls.add(tool);
    
    tool = new CreationToolEntry("Bar Chart", "Create a bar chart diagram", new SimpleFactory(UIBarChartModel.class), JacobDesigner.getImageDescriptor("toolbar_barchart16.png"), JacobDesigner.getImageDescriptor("toolbar_barchart32.png"));
    groupChartControls.add(tool);
    
    tool = new CreationToolEntry("Time Line", "Create a time line diagram", new SimpleFactory(UITimeLineModel.class), JacobDesigner.getImageDescriptor("toolbar_timeline16.png"), JacobDesigner.getImageDescriptor("toolbar_timeline32.png"));
    groupChartControls.add(tool);

    components = PluginComponentManager.createComponentPlugin(Group.chart);
    for (IComponentPlugin component : components)
    {
      if(component.getPaletteImage()!=null)
      {
        tool = new CreationToolEntry(component.getName(), component.getName(), 
            new PluginComponentFactory(
                UIPluginComponentModel.class, 
                component.getPluginId(),
                component.getPluginVersion(),
                component.getJavaImplClass()
                ), component.getPaletteImage(), component.getPaletteImage());
        groupChartControls.add(tool);
      }
    }
    
    return root;
  }

  protected void configureGraphicalViewer()
  {
  	super.configureGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();

		//root editpart:
    ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
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

  	// Actions
  	IAction showRulers = new ToggleRulerVisibilityAction(getGraphicalViewer());
  	getActionRegistry().registerAction(showRulers);
  	
  	IAction snapAction = new ToggleSnapToGeometryAction(getGraphicalViewer());
  	snapAction.setText(null);
  	getActionRegistry().registerAction(snapAction);
  	
  	IAction showGrid = new ToggleGridAction(getGraphicalViewer());
  	showGrid.setText(null);
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

		initializeRuler();
  }

  
  protected void createGraphicalViewer(Composite parent)
  {
  	rulerComp = new RulerComposite(parent, SWT.NONE);
  	super.createGraphicalViewer(rulerComp);
  	rulerComp.setGraphicalViewer((ScrollingGraphicalViewer)getGraphicalViewer());
  }
  
  protected void initializeRuler() 
  {
  	RulerProvider provider = new FormRulerProvider(this, false);
  	getGraphicalViewer().setProperty(RulerProvider.PROPERTY_VERTICAL_RULER, provider);

 		provider = new FormRulerProvider(this, true);
 		getGraphicalViewer().setProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER, provider);
 		
  	getGraphicalViewer().setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, Boolean.TRUE);
  	
  	// Snap to Geometry property
  	getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, Boolean.TRUE);
  	
  	// Grid properties
  	getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED,  Boolean.TRUE);
  	// We keep grid visibility and enablement in sync
  	getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE,  Boolean.TRUE);
  	getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING,  new Dimension(5,5));
  }
  
  protected void initializeGraphicalViewer()
  {
    getGraphicalViewer().setEditPartFactory(new EditPartFactoryImpl());
    getGraphicalViewer().setContents(getContents());
    getGraphicalViewer().addDropTargetListener(new DropListener(getGraphicalViewer(),getEditDomain().getCommandStack(), content));
    updatePalletteState();
 }

  public UIJacobFormModel getContents()
  {
    if (content == null)
      content = createContents();
    return content;
  }

  protected UIJacobFormModel createContents()
  {
    JacobFormEditorInput jacobInput  = (JacobFormEditorInput)getEditorInput();

    setPartName(jacobInput.getFormModel().getName());
    setTitleImage(jacobInput.getFormModel().getImage());

    jacobInput.getFormModel().addPropertyChangeListener(this);
    jacobInput.getFormModel().getJacobModel().addPropertyChangeListener(this);
    
    return jacobInput.getFormModel();
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
      // we need to handle common GEF elements we created
      if (aClass == IContentOutlinePage.class)
        return getOverviewOutlinePage();
      if (aClass == CommandStack.class)
        return getCommandStack();
      else if (aClass == IPropertySheetPage.class)
        return getPropertySheetPage();
      return super.getAdapter(aClass);
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
              overviewOutlinePage =new OverviewOutlinePage((ScalableFreeformRootEditPart) rootEditPart);
          }
      }

      return overviewOutlinePage;
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

  public void doSaveAs()
  {
  }

  public void gotoMarker(IMarker marker)
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
    JacobFormEditorInput jacobInput  = (JacobFormEditorInput)getEditorInput();
    jacobInput.getFormModel().removePropertyChangeListener(this);
    jacobInput.getFormModel().getJacobModel().removePropertyChangeListener(this);
    
		super.dispose();
	}
	
	public void propertyChange(PropertyChangeEvent ev)
	{
		if(ev.getPropertyName()==ObjectModel.PROPERTY_FORM_DELETED)
		  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(this, false);
		else if(ev.getPropertyName()==ObjectModel.PROPERTY_JACOBMODEL_CLOSED)
		  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(this, false);
		else
		{
		  // bei jedem event kann sich das Icon oder text des Editors �ndern. I18N, Elemente hinzugef�gt,
		  // Eventhandler angelegt......
		  //
		  setTitleImage(content.getImage());
		  setPartName(content.getName());
      updatePalletteState();
		}
	}
}
