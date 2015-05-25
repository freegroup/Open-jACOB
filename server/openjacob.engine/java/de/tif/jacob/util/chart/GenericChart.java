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
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Iterator;

public abstract class GenericChart
{
  private static final Color DEFAULT_COLOR = new Color(156, 154, 255);
  
  int  paddingLeft   = 50;
  int  paddingTop    = 50;
  int  paddingRight  = 50;
  int  paddingBottom = 40;
  
//  final int  Y_TICK_COUNT=10;
  
  protected final static Color backgroundColor1 = new Color(241,241,241);
  protected final static Color backgroundColor2 = new Color(211,207,211);
  
  protected final static Font font      = new Font("lucida sans regular", Font.PLAIN, 10);
  protected final static Font titleFont = new Font("lucida sans regular", Font.BOLD, 14);

  private double[] data= new double[0];
  private String[] yLabel = new String[0];
  private String title ="";
  private String titleX ="";
  private String titleY ="";
  private int width;
  private int height;
  private Color color = DEFAULT_COLOR;

  private boolean drawTitle=true;
  private boolean drawBackground=true;
  private boolean drawLegendX=true;
  private boolean drawLegendY=true;
  private boolean drawGrid=true;
  private double[] dataVals;
  private NiceNumberIterator yNiceNumberIterator;
  
  public abstract int  getFulcrumWidth(); // die Breite eines Datenpunktes
  public abstract void drawData(Graphics2D g);
  public abstract Rectangle[] getFulcrumBounds();
  public GenericChart(int width, int height)
  {
    this.width=width;
    this.height=height;
  }
  
  public final BufferedImage createImage()
  {
    BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D)image.getGraphics();
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,  RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

    if(drawBackground)
      drawBackground(g);
    if(drawGrid)
      drawGrid(g);
    if(drawTitle)
      drawTitle(g);
    
    drawData(g);
    drawFulcrumDecoration(g);
    drawAxisY(g);
    drawAxisX(g);
    
    if(drawLegendX)
      drawTitleX(g);
    if(drawLegendY)
      drawTitleY(g);
    
    return image;
  }

  /**
   * paint the background of the chart
   * 
   * @param g
   */
  public void drawBackground(Graphics2D g)
  {
    g.setPaint(new GradientPaint(0, 0, backgroundColor1, 0, getHeight(), backgroundColor2, false));
    g.fill(new Rectangle(0,0,getWidth(),getHeight()));    
  }
  
  /**
   * Zeichnen der Achsen des Diagramms
   * 
   * @param g
   */
  public void drawGrid(Graphics2D g)
  {
    g.setColor(backgroundColor1.darker());
    // tic marks
    int diagramHeight = getHeight() - paddingTop-paddingBottom;
    int x1 = paddingLeft;
    int y1 = getHeight()-paddingBottom;
    int x2 = getWidth()-paddingRight+5; // +5 => damit die linie ein bischen nach rechts aus dem diagram schaut
    
    Iterator iter = yNiceNumberIterator.iterator();
    while(iter.hasNext())
    {
      NiceNumber number =(NiceNumber)iter.next();
      if(number.getRank()==0)
      {
        y1 =(int)(getHeight()-(number.getPosition()*diagramHeight)-paddingBottom);
        g.drawLine(x1, y1, x2, y1);
      }
    }
  }

  public void drawFulcrumDecoration(Graphics2D g)
  {
    
  }
  
  /**
   * Zeichnen der Achsen des Diagramms
   * 
   * @param g
   */
  public void drawAxisY(Graphics2D g)
  {
    g.setColor(Color.black);
    // ordinate
    g.drawLine(paddingLeft, paddingTop, paddingLeft, getHeight() - paddingBottom);
    
    int diagramHeight = getHeight() - paddingTop-paddingBottom;
    // tic marks
    int x1 = paddingLeft;
    int y1 = getHeight()-paddingBottom;
    int x2 = paddingLeft - 3;
    boolean allNumbersAreInteger=true;
    
    Iterator iter = yNiceNumberIterator.iterator();
    while(iter.hasNext())
    {
      NiceNumber number =(NiceNumber)iter.next();
      if(number.getRank()==0)
      {
        y1 =(int)(getHeight()-(number.getPosition()*diagramHeight)-paddingBottom);
        g.drawLine(x1, y1, x2, y1);
      }
      allNumbersAreInteger= allNumbersAreInteger && number.getValue()== (int)number.getValue();
    }
    
    FontRenderContext frc = g.getFontRenderContext();
    // labels
    String text;
    LineMetrics lm;
    // labels of the Y-Axis
    //
    double xs, ys, textWidth, height;
    g.setFont(font);
    iter = yNiceNumberIterator.iterator();
    while(iter.hasNext())
    {
      NiceNumber number =(NiceNumber)iter.next();
      if(number.getRank()==0)
      {
        if(allNumbersAreInteger)
          text = Integer.toString((int)number.getValue());
        else
          text = Double.toString(number.getValue());
        textWidth = (double)font.getStringBounds(text, frc).getWidth();
        lm = font.getLineMetrics(text, frc);
        height = lm.getAscent();
        xs = paddingLeft - textWidth - 7d;
        ys = (int)(getHeight()-(number.getPosition()*diagramHeight)-paddingBottom+ height/2d);
        g.drawString(text, (float)xs, (float)ys);
      }
    }
  }
  
  /**
   * Zur Korrektur, da in Balkengrafik diese leicht eingerückt sind.
   * 
   * @return padding left offset
   */
  protected abstract int getPaddingLeftOffset();
  
  /**
   * Zeichnen der Achsen des Diagramms
   * 
   * @param g
   */
  public void drawAxisX(Graphics2D g)
  {
    g.setColor(Color.black);
    g.draw(new Line2D.Double(paddingLeft, getHeight() - paddingBottom, getWidth() - paddingRight+10, getHeight() - paddingBottom));
    // Die Ticks sind in der Mitte eines Messpunktes => ( + getFulcrumWidth()/2)
    double x1 = paddingLeft + getPaddingLeftOffset() + getFulcrumWidth() / 2d; 
    double y1 = getHeight() - paddingBottom; 
    double y2 = y1 + 3;
    double xInc = ((double)getWidth() - paddingLeft - paddingRight-getFulcrumWidth()) / (double)Math.max(1,data.length-1);
    
    FontRenderContext frc = g.getFontRenderContext();
    double widthMax=0;
    // bestimmen ob die Labels um 90° gedreht dargestellt werden müssen oder nicht
    //
    for(int j = 0; j < data.length; j++)
    {
        double width  = font.getStringBounds(yLabel[j], frc).getWidth();
        widthMax = Math.max(widthMax,width);
    }
    boolean rotate = (6+widthMax)>xInc; // es werden 6 punkte hinzugezählt, damit um der schrift noch ein bischen platz bleiben soll
    
    for(int j = 0; j < data.length; j++)
    {
        g.draw(new Line2D.Double(x1, y1, x1, y2));
        // Wert an den Tick schreiben
        //
        double width  = font.getStringBounds(yLabel[j], frc).getWidth();
        LineMetrics lm = font.getLineMetrics(yLabel[j], frc);
        double height = lm.getAscent();
        if(rotate)
        {
          final double degre90 = 90 * Math.PI / 180;
          final AffineTransform rot_normal = AffineTransform.getRotateInstance(-degre90);
          final AffineTransform rot_invers = AffineTransform.getRotateInstance(degre90);

          float labelY = (float)(y1+width+5d);
          float labelX = (float)(x1+(height/2d));
          g.transform(rot_normal); 
          g.drawString(yLabel[j],-labelY, labelX);
          g.transform (rot_invers);
        }
        else
        {
          float labelX = (float)(x1-(width/2d));
          float labelY = (float)(y1+height+5d);
          g.drawString(yLabel[j],labelX, labelY);
          
        }
        x1 += xInc;
    }
  }
  

  public void drawTitle(Graphics2D g)
  {
    g.setFont(titleFont);
    g.setColor(Color.black);
    FontRenderContext frc = g.getFontRenderContext();
    LineMetrics lm = font.getLineMetrics(title, frc);
    double width  = font.getStringBounds(title, frc).getWidth();
    double height = lm.getAscent();
    int x = (int)(paddingLeft+(getWidth() - paddingLeft - paddingRight)/2.0 - width/2);
    int y = (int)(2*height);//PADDING_TOP  + (j * yInc) + height/2;
    g.drawString(title, x, y);
  }
 
  protected void drawTitleX(Graphics2D g)
  {
    FontRenderContext frc = g.getFontRenderContext();
    double width  = font.getStringBounds(titleX, frc).getWidth();

    g.drawString(titleX,(int)((getWidth()-paddingLeft-paddingRight)/2+paddingLeft-width/2), getHeight()-3);
  }
  
  protected void drawTitleY(Graphics2D g)
  {
    final double degre90 = 90 * Math.PI / 180;
    final AffineTransform rot_normal = AffineTransform.getRotateInstance(-degre90);
    final AffineTransform rot_invers = AffineTransform.getRotateInstance(degre90);

    FontRenderContext frc = g.getFontRenderContext();
    double width  = font.getStringBounds(titleY, frc).getWidth();

    g.transform(rot_normal); 
    g.drawString(titleY,(int)-(width/2+paddingTop+(getHeight()-paddingBottom-paddingTop)/2), 10);
    g.transform (rot_invers);
  }
  
  protected double[] getDataVals()
  {
    
    if(dataVals==null)
    {
      double max = data.length==0?0:Integer.MIN_VALUE;
      double min = data.length==0?0:Integer.MAX_VALUE;
      for(int j = 0; j < data.length; j++)
      {
          if(data[j] < min)
              min = data[j];
          if(data[j] > max)
              max = data[j];
      }
      // Falls der minimale Wert !=0 ist, wird dieser nach unten korrigiert, damit
      // man auch eine ordentliche Darstellung des kleinsten Wertes erhält
      //
      if(min>0)
        min=0;

      if((max-min)>10)
        yNiceNumberIterator = new NiceNumberIterator(min,max,10);
      else
        yNiceNumberIterator = new NiceNumberIterator(min,max,(int)(max-min));
        
      max = yNiceNumberIterator.getLastValue();
      min = yNiceNumberIterator.getFirstValue();
      double span = max - min;
      dataVals =  new double[] { min, max, span };      
    }
     return dataVals;
  }
  
  public String getTitle()
  {
    return title;
  }
  
  public void setTitle(String title)
  {
    this.title = title;
  }
  
  public String[] getYLabel()
  {
    return yLabel;
  }
  
  public void setYLabel(String[] label)
  {
    if(label==null)
      label = new String[0];
    
    // Falls keine/falsche Anzahl von Labels übergeben werden, dann wird 
    // die Anzahl angepasst
    //
    if(label.length < data.length)
    {
      String[] temp = new String[data.length]; // jeder Messpunkt bekommt ein Label
      Arrays.fill(temp,""); 
      System.arraycopy(label,0,temp,0,label.length);
      label = temp;
    }
    // Falls man mehr Labels hat, werden diese gekürzt
    else if(label.length>data.length)
    {
      String[] temp = new String[data.length];
      System.arraycopy(label,0,temp,0,data.length);
      label = temp;
    }
    yLabel = label;
  }
  
  public int getHeight()
  {
    return height;
  }
  
  public void setHeight(int height)
  {
    this.height = height;
  }
  
  public int getWidth()
  {
    return width;
  }
  
  public void setWidth(int width)
  {
    this.width = width;
  }
  
  public double[] getData()
  {
    return data;
  }
  public void setData(double[] data)
  {
    this.data = data;
    if(this.data==null)
      this.data= new double[0];
    this.dataVals = null;
    this.yNiceNumberIterator=null;
    getDataVals();
    // reassign the labels to the chart to fit the count of labels
    //
    setYLabel(getYLabel());
  }
  
  public Color getColor()
  {
    return this.color;
  }

  public void setColor(Color color)
  {
    this.color = color == null ? DEFAULT_COLOR : color;
  }
  
  public String getTitleX()
  {
    return titleX;
  }
  public void setTitleX(String titleX)
  {
    this.titleX = titleX;
  }
  public String getTitleY()
  {
    return titleY;
  }
  public void setTitleY(String titleY)
  {
    this.titleY = titleY;
  }
  public boolean isDrawBackground()
  {
    return drawBackground;
  }
  public void setDrawBackground(boolean drawBackground)
  {
    this.drawBackground = drawBackground;
  }
  public boolean isDrawLegendX()
  {
    return drawLegendX;
  }
  public void setDrawLegendX(boolean drawLegendX)
  {
    this.drawLegendX = drawLegendX;
  }
  public boolean isDrawLegendY()
  {
    return drawLegendY;
  }
  public void setDrawLegendY(boolean drawLegendY)
  {
    this.drawLegendY = drawLegendY;
  }
  public boolean isDrawTitle()
  {
    return drawTitle;
  }
  public void setDrawTitle(boolean drawTitle)
  {
    this.drawTitle = drawTitle;
  }
  public boolean isDrawGrid()
  {
    return drawGrid;
  }
  public void setDrawGrid(boolean drawGrid)
  {
    this.drawGrid = drawGrid;
  }
  
  protected Iterator getYNiceNumberIterator()
  {
    if(yNiceNumberIterator==null)
      getDataVals();
    
    return yNiceNumberIterator;
  }
}
