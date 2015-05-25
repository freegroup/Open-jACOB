/*******************************************************************************
 * Copyright (c) 2004 Elias Volanakis.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 �* which accompanies this distribution, and is available at
 �* http://www.eclipse.org/legal/cpl-v10.html
 �*
 �* Contributors:
 �*����Elias Volanakis - initial API and implementation
 �*******************************************************************************/
package de.tif.jacob.rule.editor.rules;

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
import de.tif.jacob.rule.RulePlugin;
import de.tif.jacob.rule.editor.rules.model.AnnotationModel;
import de.tif.jacob.rule.editor.rules.model.BooleanDecisionModel;
import de.tif.jacob.rule.editor.rules.model.BusinessObjectModel;
import de.tif.jacob.rule.editor.rules.model.DecisionModel;
import de.tif.jacob.rule.editor.rules.model.EnumDecisionModel;
import de.tif.jacob.rule.editor.rules.model.ExceptionModel;
import de.tif.jacob.rule.editor.rules.model.FunnelModel;
import de.tif.jacob.rule.editor.rules.model.MessageAlertModel;
import de.tif.jacob.rule.editor.rules.model.MessageDialogModel;
import de.tif.jacob.rule.editor.rules.model.MessageEMailModel;
import de.tif.jacob.rule.editor.rules.model.TransitionModel;

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
 
    createToolsGroup(palette);
    createShapesDrawer(palette);
    return palette;
  }

  /** Create the "Tools" group. */
  private static void createToolsGroup(PaletteRoot palette)
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
    
    tool = new ConnectionCreationToolEntry(
        "Transition", 
        "Create a activity transition", 
        new SimpleFactory(TransitionModel.class),
        RulePlugin.getImageDescriptor("connection_s16.gif"), 
        RulePlugin.getImageDescriptor("connection_s24.gif"));
    toolGroup.add(tool);

    tool = new CombinedTemplateCreationEntry(
        "Annotation", 
        "Create an annotation", 
        AnnotationModel.class, 
        new SimpleFactory(AnnotationModel.class), 
        RulePlugin.getImageDescriptor("toolbar_annotation16.png"), 
        RulePlugin.getImageDescriptor("toolbar_annotation32.png"));
    toolGroup.add(tool);

    palette.add(toolGroup);
  }
  

  /**
   * 
   * @return
   */
  private static PaletteContainer createShapesDrawer(PaletteRoot palette)
  {
    PaletteDrawer group = new PaletteDrawer("Common");
    
    CombinedTemplateCreationEntry component;
    
    group = new PaletteDrawer("Decisions");
    palette.add(group);
    component = new CombinedTemplateCreationEntry(
                  "Common", 
                  "Create common decision", 
                  DecisionModel.class, 
                  new SimpleFactory(BooleanDecisionModel.class), 
                  RulePlugin.getImageDescriptor("toolbar_decision16.png"), 
                  RulePlugin.getImageDescriptor("toolbar_decision32.png"));
    group.add(component);
    
    component = new CombinedTemplateCreationEntry(
        "Enum", 
        "Create enumeration decision", 
        EnumDecisionModel.class, 
        new SimpleFactory(EnumDecisionModel.class), 
        RulePlugin.getImageDescriptor("toolbar_enumdecision16.png"), 
        RulePlugin.getImageDescriptor("toolbar_enumdecision32.png"));
    group.add(component);


    group = new PaletteDrawer("BusinessObjects");
    palette.add(group);
    component = new CombinedTemplateCreationEntry(
                   "Common", 
                   "Create a common business object node", 
                   BusinessObjectModel.class, 
                   new SimpleFactory(BusinessObjectModel.class), 
                   RulePlugin.getImageDescriptor("toolbar_businessobject16.png"), 
                   RulePlugin.getImageDescriptor("toolbar_businessobject32.png"));
    group.add(component);
    
    component = new CombinedTemplateCreationEntry(
        "Funnel", 
        "Create an funnel object", 
        FunnelModel.class, 
        new SimpleFactory(FunnelModel.class), 
        RulePlugin.getImageDescriptor("toolbar_funnel_bo16.png"), 
        RulePlugin.getImageDescriptor("toolbar_funnel_bo32.png"));
    group.add(component);

    component = new CombinedTemplateCreationEntry(
        "Abort", 
        "Abort the current flow", 
        ExceptionModel.class, 
        new SimpleFactory(ExceptionModel.class), 
        RulePlugin.getImageDescriptor("toolbar_abort_bo16.png"), 
        RulePlugin.getImageDescriptor("toolbar_abort_bo32.png"));
    group.add(component);

    group = new PaletteDrawer("Communication");
    palette.add(group);
    component = new CombinedTemplateCreationEntry(
                  "System Message", 
                  "Create a alert business object", 
                  MessageAlertModel.class, 
                  new SimpleFactory(MessageAlertModel.class), 
                  RulePlugin.getImageDescriptor("toolbar_message_alert16.png"), 
                  RulePlugin.getImageDescriptor("toolbar_message_alert32.png"));
    group.add(component);
    
    component = new CombinedTemplateCreationEntry(
        "eMail", 
        "Create an email object", 
        MessageEMailModel.class, 
        new SimpleFactory(MessageEMailModel.class), 
        RulePlugin.getImageDescriptor("toolbar_message_email16.png"), 
        RulePlugin.getImageDescriptor("toolbar_message_email32.png"));
    group.add(component);

    component = new CombinedTemplateCreationEntry(
        "Information Dialog", 
        "Create a information dialog", 
        MessageDialogModel.class, 
        new SimpleFactory(MessageDialogModel.class), 
        RulePlugin.getImageDescriptor("toolbar_message_dialog16.png"), 
        RulePlugin.getImageDescriptor("toolbar_message_dialog32.png"));
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
    return RulePlugin.getDefault().getPreferenceStore();
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