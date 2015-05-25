package de.tif.jacob.util.gif;
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
import java.awt.Color;
import java.awt.image.RGBImageFilter;


public class TransparentFilter extends RGBImageFilter
{
  private int rgb;  // transparente Farbe
  
  public TransparentFilter(Color color) {
    // Speichern der transparenten Farbe
    this.rgb = color.getRGB();
    // keine positionsabh�ngige Filterung
    canFilterIndexColorModel = false;
  }

  public int filterRGB(int x, int y, int rgb) {
    // Ist das zu filternde Pixel
    // gleich der transparenten Farbe?
    if (rgb == this.rgb)
      // Wenn ja, Alpha-Wert des Pixels 0 setzen
      rgb &=  0x00FFFFFF;
    return rgb;
  }
}

