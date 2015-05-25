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

package de.tif.jacob.core.definition.guielements;

import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.BarChart;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.IApplicationFactory;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BarChartDefinition extends GUIElementDefinition
{
	static public final transient String RCS_ID = "$Id: BarChartDefinition.java,v 1.2 2009/07/27 15:06:11 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

  private final boolean drawTitle;
  private final boolean drawBackground;
  private final boolean drawLegendX;
  private final boolean drawLegendY;
  private final boolean drawGrid;

  /**
	 * @param name
	 * @param caption
	 * @param top
	 * @param left
	 * @param width
	 * @param height
	 */
	public BarChartDefinition(String name, String description, String eventHandler, boolean visible, boolean title, boolean background, boolean legendX,
      boolean legendY, boolean grid, Dimension position, int borderWith,  String borderColor, String backgroundColor)
  {
    super(name, description, eventHandler, position, visible, -1, 0, null, borderWith, borderColor, backgroundColor);
    this.drawBackground = background;
    this.drawGrid = grid;
    this.drawLegendX = legendX;
    this.drawLegendY = legendY;
    this.drawTitle = title;
  }

  public IGuiElement createRepresentation(IApplicationFactory factory, IApplication app, IGuiElement parent )
  {
   return factory.createBarChart(app, parent,this); 
  }
  
  protected void toJacob(CastorGuiElement jacobGuiElement, ConvertToJacobOptions options)
  {
    BarChart jacobBarchart = new BarChart();
    jacobBarchart.setDimension(getDimension().toJacob());
    jacobBarchart.setTitle(this.drawTitle);
    jacobBarchart.setBackground(this.drawBackground);
    jacobBarchart.setLegendX(this.drawLegendX);
    jacobBarchart.setLegendY(this.drawLegendY);
    jacobBarchart.setGrid(this.drawGrid);
    jacobGuiElement.getCastorGuiElementChoice().setBarChart(jacobBarchart);
  }

  public boolean isDrawBackground()
  {
    return drawBackground;
  }

  public boolean isDrawGrid()
  {
    return drawGrid;
  }

  public boolean isDrawLegendX()
  {
    return drawLegendX;
  }

  public boolean isDrawLegendY()
  {
    return drawLegendY;
  }

  public boolean isDrawTitle()
  {
    return drawTitle;
  }
}
