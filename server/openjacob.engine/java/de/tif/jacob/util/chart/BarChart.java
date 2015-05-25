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

public class BarChart extends GenericChart
{
  public BarChart(int width, int height)
  {
    super(width, height);
  }

  public int getFulcrumWidth()
  {
    // Falls keine Daten vorhanden sind, dann ist die Breite des Stützpunktes == 0
    // 
    if(getData().length==0)
      return 0;
    
    float xInc = (getWidth() - paddingLeft-paddingRight) / (getData().length);
    // die Breite der Balken ist 80% der Y-Schrittweite
    //
    return (int)(xInc/100f*90f);
  }

  public void drawFulcrumDecoration(Graphics2D g)
  {
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.util.chart.GenericChart#getPaddingLeftOffset()
   */
  protected int getPaddingLeftOffset()
  {
    return 2;
  }

  public void drawData(Graphics2D g)
  {
    // Falls keine Daten vorhanden sind, dann braucht auch nicht gezeichnet zu werden
    //
    if(getData().length==0)
      return;
    
    double diagramHeight = getHeight() - paddingTop-paddingBottom;
    // plot data

    double xInc = (getWidth() - paddingLeft-paddingRight-(double)getFulcrumWidth()) / (double)Math.max(1,getData().length-1);
    // die Breite der Balken ist 80% der Y-Schrittweite
    //
    double barWidth = getFulcrumWidth();
    
    double x = paddingLeft + getPaddingLeftOffset();
    int height=0;
    double[] dataVals = getDataVals();
    double yScale = diagramHeight / dataVals[2];
    for(int j = 0; j < getData().length; j++)
    {
        height = (int)((getData()[j] - dataVals[0]) * yScale);
        g.setColor(getColor());
        g.fillRoundRect((int)x,(int)(getHeight()-paddingBottom-height),(int)barWidth,(int)height,5,5);
        g.setColor(Color.black);
        g.drawRoundRect((int)x,(int)(getHeight()-paddingBottom-height),(int)barWidth,(int)height,5,5);
        x += xInc;
    }
  }
  
  public Rectangle[] getFulcrumBounds()
  {
    if(getData().length==0)
      return new Rectangle[0];

    Rectangle[] result = new Rectangle[getData().length];
    // plot data
    double xInc = (getWidth() - paddingLeft-paddingRight-(double)getFulcrumWidth()) / (double)Math.max(1,getData().length-1);

    // die Breite der Balken ist 80% der Y-Schrittweite
    //
    double barWidth = getFulcrumWidth();
    double x = paddingLeft+2;
    for(int j = 0; j < getData().length; j++)
    {
        result[j] = new Rectangle((int)x, paddingTop ,(int)barWidth,getHeight()-paddingBottom-paddingTop );
        x += xInc;
    }
    return result;
  }
}
