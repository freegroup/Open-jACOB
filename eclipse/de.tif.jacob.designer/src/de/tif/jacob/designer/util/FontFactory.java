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
package de.tif.jacob.designer.util;

import java.util.HashMap;
import org.apache.commons.collections.set.UnmodifiableSortedSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.model.ObjectModel;

public class FontFactory
{
  private final static HashMap fonts=new HashMap();
  
  public static final String DEFAULT_FONT_KEY="sans-serif_normal_normal_8";
  static
  {
    fonts.put(DEFAULT_FONT_KEY,Constants.FONT_NORMAL);
  }
  
  
  public static String createFontKey(String family, String style, String weight, int size)
  {
    return family+"_"+style+"_"+weight+"_"+Integer.toString(size);
  }
  
  /**
   * DON'T dispose the fonts.
   * 
   * @param family
   * @param style
   * @param weight
   * @param size
   * @return
   */
  public static Font getFont(String family, String style, String weight, int size)
  {
    String key = createFontKey( family,  style,  weight,  size);
    
    Font font = (Font)fonts.get(key);
    if(font==null)
    {
      Display display = JacobDesigner.getPlugin().getWorkbench().getDisplay();
      int flag = SWT.NORMAL;
      if(style.equals(ObjectModel.FONTSTYLE_ITALIC))
        flag = flag | SWT.ITALIC;
      if(weight.equals(ObjectModel.FONTWEIGHT_BOLD))
        flag = flag | SWT.BOLD;
      font = new Font(display,family,size,flag);
      fonts.put(key,font);
    }
    return font;
  }
}
