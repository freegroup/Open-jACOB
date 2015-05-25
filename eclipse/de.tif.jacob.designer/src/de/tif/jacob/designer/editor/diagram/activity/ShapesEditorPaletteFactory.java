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

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.jface.preference.IPreferenceStore;

import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.diagram.activity.ActivityModel;
import de.tif.jacob.designer.model.diagram.activity.BranchMergeModel;
import de.tif.jacob.designer.model.diagram.activity.EndModel;
import de.tif.jacob.designer.model.diagram.activity.ForkJoinModel;
import de.tif.jacob.designer.model.diagram.activity.StartModel;
import de.tif.jacob.designer.model.diagram.activity.TransitionModel;

/**
 * Utility class that can create a GEF Palette.
 * 
 * @see #createPalette()
 * @author Elias Volanakis
 */
final class ShapesEditorPaletteFactory
{
  private static final int DEFAULT_PALETTE_SIZE = 125;
  private static final String PALETTE_DOCK_LOCATION = "ShapesEditorPaletteFactory.Location";
  private static final String PALETTE_SIZE = "ShapesEditorPaletteFactory.Size";
  private static final String PALETTE_STATE = "ShapesEditorPaletteFactory.State";

  
  /** Utility class. */
  private ShapesEditorPaletteFactory()
  {
    // Utility class
  }


  /**
   * Creates the PaletteRoot and adds all palette elements. Use this factory
   * method to create a new palette for your graphical editor.
   * 
   * @return a new PaletteRoot
   */
  static PaletteRoot createPalette()
  {
    PaletteRoot palette = new PaletteRoot();
    palette.add(createToolsGroup(palette));
    palette.add(createShapesDrawer());
    return palette;
  }

  /** Create the "Tools" group. */
  private static PaletteContainer createToolsGroup(PaletteRoot palette)
  {
    PaletteGroup toolGroup = new PaletteGroup("Tools");

    // Add a selection tool to the group
    ToolEntry tool = new SelectionToolEntry();
    toolGroup.add(tool);
    palette.setDefaultEntry(tool);

    // Add a marquee tool to the group
    toolGroup.add(new MarqueeToolEntry());

    // Add a (unnamed) separator to the group
    toolGroup.add(new PaletteSeparator());

    // Add (solid-line) connection tool
    tool = new ConnectionCreationToolEntry("Transition", "Create a activity transition", new SimpleFactory(TransitionModel.class),
        JacobDesigner.getImageDescriptor("connection_s16.gif"), JacobDesigner.getImageDescriptor("connection_s24.gif"));
    toolGroup.add(tool);

    return toolGroup;
  }
  

  /**
   * 
   * @return
   */
  private static PaletteContainer createShapesDrawer()
  {
    PaletteDrawer group = new PaletteDrawer("Shapes");

    CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry("Start",
        "Create a start node",
        StartModel.class,
        new SimpleFactory(StartModel.class),
        JacobDesigner.getImageDescriptor("toolbar_start16.png"), 
        JacobDesigner.getImageDescriptor("toolbar_start32.png"));
		group.add(component);
		
		component = new CombinedTemplateCreationEntry("End",
		    "Create a end node",
		    EndModel.class,
		    new SimpleFactory(EndModel.class),
		    JacobDesigner.getImageDescriptor("toolbar_end16.png"), 
		    JacobDesigner.getImageDescriptor("toolbar_end32.png"));
		group.add(component);

		component = new CombinedTemplateCreationEntry("Activity",
        "Create a activity node",
        ActivityModel.class,
        new SimpleFactory(ActivityModel.class),
        JacobDesigner.getImageDescriptor("toolbar_activity16.png"), 
        JacobDesigner.getImageDescriptor("toolbar_activity32.png"));
    group.add(component);

    component = new CombinedTemplateCreationEntry("Branch/Merge",
        "Create a branch/merge node",
        BranchMergeModel.class,
        new SimpleFactory(BranchMergeModel.class),
        JacobDesigner.getImageDescriptor("toolbar_branchmerge16.png"), 
        JacobDesigner.getImageDescriptor("toolbar_branchmerge32.png"));
		group.add(component);
		
    component = new CombinedTemplateCreationEntry("Fork/Join",
        "Create a fork/join node",
        ForkJoinModel.class,
        new SimpleFactory(ForkJoinModel.class),
        JacobDesigner.getImageDescriptor("toolbar_forkjoin16.png"), 
        JacobDesigner.getImageDescriptor("toolbar_forkjoin32.png"));
		group.add(component);

		
	return group;
  }

  /**
   * Returns the preference store for the ShapesPlugin.
   * 
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#getPreferenceStore()
   */
  private static IPreferenceStore getPreferenceStore()
  {
    return JacobDesigner.getPlugin().getPreferenceStore();
  }

  /**
   * Return a FlyoutPreferences instance used to save/load the preferences of a
   * flyout palette.
   */
  static FlyoutPreferences createPalettePreferences()
  {
    // set default flyout palette preference values, in case the preference
    // store
    // does not hold stored values for the given preferences
    getPreferenceStore().setDefault(PALETTE_DOCK_LOCATION, -1);
    getPreferenceStore().setDefault(PALETTE_STATE, -1);
    getPreferenceStore().setDefault(PALETTE_SIZE, DEFAULT_PALETTE_SIZE);

    return new FlyoutPreferences()
    {
      public int getDockLocation()
      {
        return getPreferenceStore().getInt(PALETTE_DOCK_LOCATION);
      }

      public int getPaletteState()
      {
        return getPreferenceStore().getInt(PALETTE_STATE);
      }

      public int getPaletteWidth()
      {
        return getPreferenceStore().getInt(PALETTE_SIZE);
      }

      public void setDockLocation(int location)
      {
        getPreferenceStore().setValue(PALETTE_DOCK_LOCATION, location);
      }

      public void setPaletteState(int state)
      {
        getPreferenceStore().setValue(PALETTE_STATE, state);
      }

      public void setPaletteWidth(int width)
      {
        getPreferenceStore().setValue(PALETTE_SIZE, width);
      }
    };
  }

}
