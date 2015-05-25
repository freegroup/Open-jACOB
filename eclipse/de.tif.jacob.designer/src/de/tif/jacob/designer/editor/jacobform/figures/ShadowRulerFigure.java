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
 * Created on Aug 24, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.tif.jacob.designer.editor.jacobform.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ShadowRulerFigure extends Figure
{
  private static float FACTOR=0.7f;
  
  private final Color colorTop;
  private final Color colorBottom;

  /**
   * @param top
   * @param bottom
   */
  public ShadowRulerFigure(Color top, Color bottom)
  {
    setOpaque(true);
    colorTop    = darkerColor(top);
    colorBottom = brighterColor(bottom);
  }


  /**
   * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
   */
  public Dimension getPreferredSize(int wHint, int hHint) 
  {
    return new Dimension(wHint, 2);
  }
  

  /**
   * 
   * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
   */
  protected void paintFigure(Graphics g) {
    Rectangle r = getBounds();//.getCropped(CROP);

    g.setForegroundColor(colorTop);
    g.drawLine(r.getTopLeft(), r.getTopRight());

    g.setForegroundColor(colorBottom);
    g.drawLine(r.getBottomLeft(), r.getBottomRight());
  }
  
  private static Color darkerColor(Color color)
  {
    return new Color(null,Math.max((int)(color.getRed()  *FACTOR), 0), 
       Math.max((int)(color.getGreen()*FACTOR), 0),
       Math.max((int)(color.getBlue() *FACTOR), 0));
    
  }
  
  public static Color brighterColor(Color color) {
    int r = color.getRed();
    int g = color.getGreen();
    int b = color.getBlue();

    /* From 2D group:
     * 1. black.brighter() should return grey
     * 2. applying brighter to blue will always return blue, brighter
     * 3. non pure color (non zero rgb) will eventually return white
     */
    int i = (int)(1.0/(1.0-FACTOR));
    if ( r == 0 && g == 0 && b == 0) {
       return new Color(null,i, i, i);
    }
    if ( r > 0 && r < i ) r = i;
    if ( g > 0 && g < i ) g = i;
    if ( b > 0 && b < i ) b = i;

    return new Color(null,Math.min((int)(r/FACTOR), 255),
                     Math.min((int)(g/FACTOR), 255),
                     Math.min((int)(b/FACTOR), 255));
}
}