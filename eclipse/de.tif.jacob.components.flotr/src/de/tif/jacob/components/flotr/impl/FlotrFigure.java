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
package de.tif.jacob.components.flotr.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.eclipse.draw2d.Graphics;

import de.tif.jacob.designer.editor.jacobform.figures.AbstractChartFigure;
import de.tif.jacob.designer.editor.jacobform.figures.ObjectFigure;
import de.tif.jacob.util.chart.GenericChart;
import de.tif.jacob.util.chart.LineChart;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class FlotrFigure extends AbstractChartFigure implements ObjectFigure
{
  int oldW;
  int oldH;

  public FlotrFigure()
  {
  }

  @Override
  public void drawData(Graphics g)
  {
    int w = getSize().width;
    int h = getSize().height;
    
    if(oldW!=w || oldH!=h || this.isDirty())
    {
      oldW=w;
      oldH=h;
      this.setDirty(false);
      GenericChart chart = new LineChart(w,h);
      chart.setData(new double[]{23,46,22,56,121,133,181,180,273,250,267,273});
      chart.setYLabel(new String[]{"J","F","M","A","M","J","J","A","S","O","N","D"});
      chart.setTitle("Sales Dashboard");
      chart.setTitleY("$ x 1000");
      chart.setTitleX("Month of Year");
      chart.setDrawBackground(hasBackground());
      chart.setDrawGrid(isGrid());
      chart.setDrawLegendX(isLegendX());
      chart.setDrawLegendY(isLegendY());
      chart.setDrawTitle(isTitle());

      BufferedImage img =  chart.createImage();
      renderer.prepareRendering(0,0,w,h); // prepares the Graphics2D renderer

      // gets the Graphics2D context
      Graphics2D g2d = renderer.getGraphics2D();

      g2d.drawImage(img,0,0,w,h,null);
    }
    // now that we are done with Java 2D, renders Graphics2D operation
    // on the Draw2D graphics context
    renderer.render(g);
  }
  
}