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

package de.tif.jacob.util.chart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class LineChart extends GenericChart
{
  public LineChart(int width, int height)
  {
    super(width,height);
  }
  
  
  public int getFulcrumWidth()
  {
    return 2;
  }

  public void drawFulcrumDecoration(Graphics2D g)
  {
    // Keine Daten => nichts zum zeichnen
    //
    if(getData().length==0)
      return;
    
    g.setFont(font);
    double x1 = paddingLeft;
    double y1=0;
    double xInc = (getWidth() - paddingLeft - paddingRight) / Math.max(1,getData().length-1);
    double[] dataVals = getDataVals();
    double yScale = (double)(getHeight() - paddingTop-paddingBottom) / (float)dataVals[2];
    for(int j = 0; j < getData().length; j++)
    {
        y1 = paddingTop + (getHeight() - paddingBottom-paddingTop) - (getData()[j] - dataVals[0]) * yScale;
        g.setColor(Color.magenta);
        g.fillOval((int)x1-2, (int)y1-2, 4, 4);
      //  g.setColor(labelColor);
      //  g.drawString(Double.toString(getData()[j]),(float)x1+2,(float)y1);
        x1 += xInc;
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.util.chart.GenericChart#getPaddingLeftOffset()
   */
  protected int getPaddingLeftOffset()
  {
    return 0;
  }

  public void drawData(Graphics2D g)
  {
    // Keine Daten => nichts zum zeichnen
    //
    if( getData().length==0)
      return;

    // plot data
    double x1 = paddingLeft+2;
    double x2=0;
    double y1=0;
    double y2=0;
    double xInc = (getWidth() - paddingLeft - paddingRight) / Math.max(1,getData().length-1);
    double[] dataVals = getDataVals();
    double yScale = (double)(getHeight() - paddingTop-paddingBottom) / (double)dataVals[2];
    g.setColor(backgroundColor1.darker());

    for(int j = 0; j < getData().length; j++)
    {
        y1 = paddingTop + (getHeight() - paddingBottom-paddingTop+2) - (getData()[j] - dataVals[0]) * yScale;
        if(j > 0)
            g.draw(new Line2D.Double(x1, y1, x2, y2));
        x2 = x1;
        y2 = y1;
        x1 += xInc;
    }
    x1 = paddingLeft;
    x2=0;
    y1=0;
    y2=0;
    g.setColor(getColor());
    for(int j = 0; j < getData().length; j++)
    {
        y1 = paddingTop + (getHeight() - paddingBottom-paddingTop) - (getData()[j] - dataVals[0]) * yScale;
        if(j > 0)
            g.draw(new Line2D.Double(x1, y1, x2, y2));
        x2 = x1;
        y2 = y1;
        x1 += xInc;
    }
  }
  
  public Rectangle[] getFulcrumBounds()
  {
    if(getData().length==0)
      return new Rectangle[0];
    
    Rectangle[] result = new Rectangle[getData().length];
    // plot data
    double xInc = (getWidth() - paddingLeft-paddingRight-getFulcrumWidth()) / Math.max(1,getData().length-1);

    // die Breite der Balken ist 80% der Y-Schrittweite
    //
    double barWidth = getFulcrumWidth();
    double x = paddingLeft+2;
    for(int j = 0; j < getData().length; j++)
    {
        result[j] = new Rectangle((int)x, paddingTop ,(int)barWidth,getHeight()-paddingBottom );
        x += xInc;
    }
    return result;
  }
}
