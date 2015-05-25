/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
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

package de.tif.jacob.screen.impl.theme;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.BootstrapEntry;
import de.tif.jacob.screen.impl.theme.castor.CastorTheme;
import de.tif.jacob.util.file.Directory;

public class ThemeManager extends BootstrapEntry
{
  private static final String DEFAULT_THEME ="jACOB";
  
  private static Map location2theme;
  
  public void init() throws Throwable
  {
    location2theme = new HashMap();
    String themeRootPath = Bootstrap.getThemeRootPath();
    ArrayList fileArray = Directory.getAll(new File(themeRootPath), true);
    for (Iterator iter = fileArray.iterator(); iter.hasNext();)
    {
      File file = (File) iter.next();
      if(file.getName().equals("theme.xml"))
      {
        CastorTheme castorTheme = (CastorTheme)CastorTheme.unmarshalCastorTheme(new FileReader(file));
        String location = file.getParentFile().getName();
        Theme theme = new Theme(location, location,castorTheme);
        
        location2theme.put(location, theme);
     }
    }
  }
  
  private static void checkInitialized()
  {
    if (location2theme == null)
      throw new IllegalStateException("ThemeManager has not been initialized");
  }

  synchronized public static Theme[] getThemes()
  {
    checkInitialized();
    return (Theme[]) location2theme.values().toArray(new Theme[0]);
  }

  synchronized public static Theme getTheme(String id)
  {
    checkInitialized();
    Theme theme = (Theme) location2theme.get(id);
    if (theme == null)
      theme = (Theme) location2theme.get(DEFAULT_THEME);
    return theme;
  }

  public void destroy() throws Throwable
  {
    // nothing to do
  }
}
