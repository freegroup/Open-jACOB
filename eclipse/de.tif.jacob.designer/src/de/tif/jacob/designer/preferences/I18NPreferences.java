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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import de.tif.jacob.designer.JacobDesigner;
/**
 */
public final class I18NPreferences
{
  private static final String SEPARATOR   = "i18n_separator";
  private static final String HIERACHICAL = "i18n_hirachical";
  private static final String USE_I18N    = "i18n_enabled";

  private IPreferenceStore preferenceStore;
  
  /**
   * Constructor.
   */
  public I18NPreferences(IProject project)
  {
    preferenceStore = new ScopedPreferenceStore(new ProjectScope(project),ProjectScope.SCOPE);
  }

  public boolean useI18N()
  {
    return preferenceStore.getBoolean(USE_I18N);
  }
  
  public void setUseI18N(boolean flag)
  {
    preferenceStore.setValue(USE_I18N,flag);
  }

  public boolean getShowHirachical()
  {
    return preferenceStore.getBoolean(HIERACHICAL);
  }
  
  public void setShowHirachical(boolean flag)
  {
    preferenceStore.setValue(HIERACHICAL,flag);
  }
 
  /**
   * Gets key group separator.
   * 
   * @return key group separator.
   */
  public String getSeparator()
  {
    String sep= preferenceStore.getString(SEPARATOR);
    if(sep==null || sep.length()==0)
    {
      sep = ".";
      preferenceStore.setValue(SEPARATOR,sep);
    }
    return sep;
  }
  
  public void setSeparator(String sep)
  {
    preferenceStore.setValue(SEPARATOR,sep);
  }
  
   
}
