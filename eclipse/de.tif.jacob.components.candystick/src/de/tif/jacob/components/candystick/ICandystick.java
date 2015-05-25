/*
 * Created on 24.02.2009
 *
 */
package de.tif.jacob.components.candystick;

import java.awt.Color;

public interface ICandystick
{
  final static Color BLUE = new Color(54,103,217);
  final static Color GREEN = new Color(158,222,124  );
  final static Color PURPLE = new Color(158,65,238);
  final static Color ORANGE = new Color(236,116,17);
  final static Color CYAN = new Color(0,170,170);
  final static Color RED = new Color(204,0,51);
  final static Color OLIVE = new Color(170,170,0);
  final static Color DARK_GREEN = new Color(0,128,0);
  final static Color YELLOW = new Color(255,255,0);

  /**
   * 
   * @param value between [0.0 - 1.0]
   * @param color
   */
  public void addValue(double value, Color color);
  
  /**
   * 
   * @param value between [0.0 - 1.0]
   * @param color
   */
  public void addValue(float value, Color color);

  public void clear();
  
  public void setVisible(boolean flag);
}
