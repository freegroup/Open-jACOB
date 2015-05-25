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
package de.tif.jacob.designer.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import de.tif.jacob.designer.JacobDesigner;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage </samp>,
 * we can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
  public PreferencePage() 
  {
    super(GRID);
    setPreferenceStore(JacobDesigner.getPlugin().getPreferenceStore());
    setDescription("Preferences for the jACOB Designer.");
    initializeDefaults();
  }

  /**
   * Sets the default values of the preferences.
   */
  private void initializeDefaults()
  {
    IPreferenceStore store = getPreferenceStore();
    store.setDefault(JacobDesigner.DOUBLECLICK_DB, "eventHandler");
  }

  /**
   * Creates the field editors. Field editors are abstractions of the common GUI
   * blocks needed to manipulate various types of preferences. Each field editor
   * knows how to save and restore itself.
   */

  public void createFieldEditors()
  {
    addField(new RadioGroupFieldEditor(
        JacobDesigner.DOUBLECLICK_DB, 
        "Double click on UI element opens", 1,
        new String[][] { 
        										{ "&Event handler", "eventHandler" }, 
        										{ "&DB Definition", "dbDefinition" } 
        							 },
        getFieldEditorParent()));
  }

  public void init(IWorkbench workbench)
  {
  }
}