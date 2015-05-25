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
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import de.tif.jacob.designer.JacobDesigner;
/**
 * Initializes default preferences.
 * 
 * @author Pascal Essiembre
 * @version $Author: freegroup $ $Revision: 1.1 $ $Date: 2007/05/18 16:13:36 $
 */
public class ResourceBundlePreferenceInitializer extends AbstractPreferenceInitializer
{
  /**
   * Constructor.
   */
  public ResourceBundlePreferenceInitializer()
  {
    super();
  }

  /**
   * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer
   *      #initializeDefaultPreferences()
   */
  public void initializeDefaultPreferences()
  {
    Preferences prefs = JacobDesigner.getPlugin().getPluginPreferences();
    prefs.setDefault(ResourceBundlePreferences.KEY_GROUP_SEPARATOR, ".");
    prefs.setDefault(ResourceBundlePreferences.ALIGN_EQUAL_SIGNS, true);
    prefs.setDefault(ResourceBundlePreferences.SHOW_GENERATOR, true);
    prefs.setDefault(ResourceBundlePreferences.KEY_TREE_HIERARCHICAL, true);
    prefs.setDefault(ResourceBundlePreferences.GROUP_KEYS, true);
    prefs.setDefault(ResourceBundlePreferences.GROUP_LEVEL_DEEP, 1);
    prefs.setDefault(ResourceBundlePreferences.GROUP_LINE_BREAKS, 1);
    prefs.setDefault(ResourceBundlePreferences.GROUP_ALIGN_EQUAL_SIGNS, true);
    prefs.setDefault(ResourceBundlePreferences.WRAP_CHAR_LIMIT, 80);
    prefs.setDefault(ResourceBundlePreferences.WRAP_INDENT_SPACES, 8);
    prefs.setDefault(ResourceBundlePreferences.NEW_LINE_TYPE, ResourceBundlePreferences.NEW_LINE_UNIX);
  }
}
