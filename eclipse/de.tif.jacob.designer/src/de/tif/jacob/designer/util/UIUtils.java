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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;

import de.tif.jacob.designer.JacobDesigner;
/**
 * Utility methods related to application UI.
 * 
 * @author Pascal Essiembre
 * @version $Author: freegroup $ $Revision: 1.2 $ $Date: 2010/10/07 06:23:45 $
 */
public final class UIUtils
{
  /**
   * Constructor.
   */
  private UIUtils()
  {
    super();
  }

  public static boolean isAltKeyPressed()
  {
    return JacobDesigner.getPlugin().isALTKeyPressed();
  }
  
  /**
   * Creates a font by altering the font associated with the given control and
   * applying the provided style (size is unaffected).
   * 
   * @param control
   *          control we base our font data on
   * @param style
   *          style to apply to the new font
   * @return newly created font
   */
  public static Font createFont(Control control, int style)
  {
    return createFont(control, style, 0);
  }

  /**
   * Creates a font by altering the font associated with the given control and
   * applying the provided style and relative size.
   * 
   * @param control
   *          control we base our font data on
   * @param style
   *          style to apply to the new font
   * @param relSize
   *          size to add or remove from the control size
   * @return newly created font
   */
  public static Font createFont(Control control, int style, int relSize)
  {
    FontData[] fontData = control.getFont().getFontData();
    for (int i = 0; i < fontData.length; i++)
    {
      fontData[i].setHeight(fontData[i].getHeight() + relSize);
      fontData[i].setStyle(SWT.BOLD);
    }
    return new Font(control.getDisplay(), fontData);
  }

  /**
   * Gets the approximate width required to display a given number of characters
   * in a control.
   * 
   * @param control
   *          the control on which to get width
   * @param widthInChars
   *          the number of chars
   * @return
   */
  public static int getWidthInChars(Control control, int widthInChars)
  {
    GC gc = new GC(control);
    Point extent = gc.textExtent("W");//$NON-NLS-1$
    gc.dispose();
    return widthInChars * extent.x;
  }
}
