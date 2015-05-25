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

package de.tif.jacob.screen.event;

import java.awt.Color;

/**
 * 
 * @author Andreas Herz
 */
public class TimeLineSlot
{
  public static final SlotColor COLOR_BLACK   = new SlotColor("black", new Color(125,120,117));
  public static final SlotColor COLOR_BLUE    = new SlotColor("blue", new Color(0,163,229));
  public static final SlotColor COLOR_BROWN   = new SlotColor("brown", new Color(202,151,94));
  public static final SlotColor COLOR_GREEN   = new SlotColor("green", new Color(204,237,70));
  public static final SlotColor COLOR_PURPLE  = new SlotColor("purple", new Color(133,77,112));
  public static final SlotColor COLOR_RED     = new SlotColor("red", new Color(225,79,64));
  public static final SlotColor COLOR_YELLOW  = new SlotColor("yellow",new Color(251,227,64));
  public static final SlotColor COLOR_WHITE   = new SlotColor("white",new Color(255,255,255));
  
  public static final SlotColor COLOR_DEFAULT = new SlotColor("black",null);
  
  public static class SlotColor
  {
    public final String name;
    public final Color  color;
    private SlotColor(String name, Color color)
    {
      this.name = name;
      this.color = color;
    }
  }
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: TimeLineSlot.java,v 1.6 2009/07/09 16:05:11 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.6 $";

  private final java.util.Date startDate;
  private final java.util.Date endDate;
  private final String label;
  private final String tooltip;
  private final SlotColor color;
  
  public TimeLineSlot( java.util.Date startDate, java.util.Date endDate, String label,String tooltip,  SlotColor color)
  {
    this.startDate = new java.util.Date(startDate.getTime());
    this.endDate = new java.util.Date(endDate.getTime());
    this.label = label;
    this.tooltip = tooltip;
    if(color!=null)
      this.color = color;
    else
      this.color=COLOR_DEFAULT;
  }

  public java.util.Date getStartDate()
  {
    return startDate;
  }
  
  public java.util.Date getEndDate()
  {
    return endDate;
  }
  
  public String getLabel()
  {
    return label;
  }
  
  public String getTooltip()
  {
    return tooltip;
  }
  
  public String getColorName()
  {
    return color.name;
  }
  
  public Color getColor()
  {
    return color.color;
  }
}
