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

package de.tif.qes;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.config.IConfig;
import de.tif.qes.adf.castor.Position;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * @author Andreas Sonntag
 */
public class QesLayoutAdjustment
{
  private static final Version V6_0 = new Version(6, 0);
  
  // skip invisible GUI elements (except foreign fields)
  private final boolean skipInvisible = true;
  
  private final long minButtonHeight = 300;
  private final long minIFBHeight = 1200;
  private final int verticalGridSize = 30;
  private final int horizontalGridSize = 30;
  private final int verticalGridOffset = 15;
  private final int horizontalGridOffset = 15;
  
  private final long iconElementWidthReduction;
  
  /**
   * @param config
   */
  public QesLayoutAdjustment(IConfig config, Version adlVersion)
  {
    boolean isAdl6OrHigher = adlVersion != null && adlVersion.compareTo(V6_0) >= 0;
    
    // We have to reduce the size of foreign fields, datetime by 16 effective pixels
    // because it seems like that upon ADL 6 the given width is including icon!
    this.iconElementWidthReduction = isAdl6OrHigher ? 16*15 : 0;
  }
  
  private static long grid(long value, int gridSize, int gridOffset)
  {
    value -= gridOffset;
    
    int x = (int) (value % gridSize);
    if (2 * x > gridSize)
      return gridOffset + gridSize * (1 + value / gridSize);
    return gridOffset + gridSize * (value / gridSize);
  }
  
  public boolean skipInvisible()
  {
    return this.skipInvisible;
  }
  
  public Position adjustGrid(Position position)
  {
    if (position.hasLeft())
      position.setLeft(grid(position.getLeft(), horizontalGridSize, horizontalGridOffset));
    if (position.hasTop())
      position.setTop(grid(position.getTop(), verticalGridSize, verticalGridOffset));

    return position;
  }

  public Position adjustButton(Position position)
  {
    if (position.hasHeight() && position.getHeight() < minButtonHeight)
      position.setHeight(minButtonHeight);
      
    return adjustGrid(position);
  }

  /**
   * @param position
   * @return
   */
  public Position adjustText(Position position)
  {
    return adjustGrid(position);
  }

  /**
   * @param position
   * @return
   */
  public Position adjustLongText(Position position)
  {
    return adjustGrid(position);
  }

  /**
   * @param position
   * @return
   */
  public Position adjustCheckBox(Position position)
  {
    return adjustGrid(position);
  }

  /**
   * @param position
   * @return
   */
  public Position adjustComboBox(Position position)
  {
    return adjustGrid(position);
  }

  /**
   * @param position
   * @return
   */
  public Position adjustForeign(Position position)
  {
    if (position.hasWidth())
      // ensure that width does not become negative
      position.setWidth(Math.max(0, position.getWidth() - this.iconElementWidthReduction));
      
    return adjustGrid(position);
  }

  /**
   * @param position
   * @return
   */
  public Position adjustIFB(Position position)
  {
    if (position.hasHeight() && position.getHeight() < this.minIFBHeight)
    {
      long diff = this.minIFBHeight - position.getHeight();
      if (position.hasTop() && position.getTop() > diff)
        position.setTop(position.getTop()-diff/2);
      position.setHeight(this.minIFBHeight);
    }
      
    return adjustGrid(position);
  }

  /**
   * @param position
   * @return
   */
  public Position adjustDateTime(Position position)
  {
    if (position.hasWidth())
      // ensure that width does not become negative
      position.setWidth(Math.max(0, position.getWidth() - this.iconElementWidthReduction));
      
    return adjustGrid(position);
  }

  /**
   * @param position
   * @return
   */
  public Position adjustGroup(Position position)
  {
    return adjustGrid(position);
  }

  /**
   * @param position
   * @return
   */
  public Position adjustCaption(Position position)
  {
    return adjustGrid(position);
  }
}
