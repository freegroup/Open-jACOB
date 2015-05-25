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
 * Created on 08.01.2006
 *
 */
package de.tif.jacob.designer.editor.util;
import org.eclipse.swt.graphics.Color;

import de.tif.jacob.designer.editor.Constants;
public class ColorUtil
{
  public static Color darker(Color color, float factor)
  {
    return new Color(null, Math.max((int) (color.getRed() * factor), 0), Math.max((int) (color.getGreen() * factor), 0), Math.max((int) (color.getBlue() * factor), 0));
  }

  public static Color brighter(Color color, float factor)
  {
    int r = color.getRed();
    int g = color.getGreen();
    int b = color.getBlue();
    /*
     * From 2D group: 1. black.brighter() should return grey 2. applying
     * brighter to blue will always return blue, brighter 3. non pure color (non
     * zero rgb) will eventually return white
     */
    int i = (int) (1.0 / (1.0 - factor));
    if (r == 0 && g == 0 && b == 0)
    {
      return new Color(null, i, i, i);
    }
    if (r > 0 && r < i)
      r = i;
    if (g > 0 && g < i)
      g = i;
    if (b > 0 && b < i)
      b = i;
    return new Color(null, Math.min((int) (r / factor), 255), Math.min((int) (g / factor), 255), Math.min((int) (b / factor), 255));
  }

  public static String toCSS(Color c)
  {
    String colorR = "0" + Integer.toHexString(c.getRed());
    colorR = colorR.substring(colorR.length() - 2);
    String colorG = "0" + Integer.toHexString(c.getGreen());
    colorG = colorG.substring(colorG.length() - 2);
    String colorB = "0" + Integer.toHexString(c.getBlue());
    colorB = colorB.substring(colorB.length() - 2);
    return "#" + colorR + colorG + colorB;
  }

  public static Color toColor(String colorstring)
  {
    if(colorstring.startsWith("#"))
    {
      return new Color(null,Integer.parseInt(colorstring.substring(1, 3),16),
          Integer.parseInt(colorstring.substring(3, 5),16),
          Integer.parseInt(colorstring.substring(5, 7),16));
    }
    else
    {
      return new Color(null,Integer.parseInt(colorstring.substring(0, 2),16),
        Integer.parseInt(colorstring.substring(2, 4),16),
        Integer.parseInt(colorstring.substring(4, 6),16));
    }
  }

  public static void main(String[] args)
  {
    System.out.println(toCSS(Constants.COLOR_BO_END));
    System.out.println(toColor("#FF0000"));
  }
}
