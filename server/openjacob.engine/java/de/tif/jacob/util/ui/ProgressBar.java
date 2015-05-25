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

package de.tif.jacob.util.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class ProgressBar
{
  private Color buttonColor = Color.BLACK;
//  private Color foregroundColor = new Color(0f,0f,0f,.6f);
  private Color gaugeColor = Color.GRAY.brighter();
  
  private final int height;
  private final int width;
  
  public ProgressBar(int width, int height)
  {
    this.width = width;
    this.height= height;
  }
  
  public final BufferedImage createImage(float percent /*0.0 - 1.0*/)
  {
    int vArcSize = height/2;
    int vButtonGaugeWidth = (int)(width *percent);
    String text = ""+((int)(percent*100))+"%";
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = (Graphics2D)image.getGraphics();

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Calculate the size of the button

    // Paint the background of the button
    g2.setColor(new Color(0,0,0,0));
    g2.fillRect(0, 0, width, height);
    
    // Create the gradient paint for the first layer of the button
    Color vGradientStartColor = buttonColor.darker().darker().darker();
    Color vGradientEndColor   = buttonColor.brighter().brighter().brighter();
    Paint vPaint = new GradientPaint(0, 0, vGradientStartColor, 0, height, vGradientEndColor, false);
    g2.setPaint(vPaint);

    // Paint the first layer of the button
    g2.fillRoundRect(0,0, width, height, vArcSize, vArcSize);

    // Create the paint for the second layer of the button
    vPaint = new GradientPaint(0, 0, Color.BLACK, 0, height, gaugeColor, false);

    // Paint the second layer of the button
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.9f));
    g2.setPaint(vPaint);
    g2.setClip(new RoundRectangle2D.Float(0,0,width,height,vArcSize,vArcSize));
    g2.fillRect(0,0,vButtonGaugeWidth,height);
    
    // Calulate the size of the second layer of the button
    int vHighlightInset = 0;
    int vButtonHighlightHeight = height - (vHighlightInset * 2);
    int vButtonHighlightWidth = width - (vHighlightInset * 2);
    int vHighlightArcSize = vButtonHighlightHeight/2;

    // Create the paint for the second layer of the button
    vGradientStartColor = Color.WHITE;
    vGradientEndColor = buttonColor.brighter();
    vPaint = new GradientPaint(0,vHighlightInset,vGradientStartColor,0,vHighlightInset+(vButtonHighlightHeight), buttonColor.brighter(), false);

    // Paint the second layer of the button
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.5f));
    g2.setPaint(vPaint);
    g2.setClip(new RoundRectangle2D.Float(vHighlightInset,vHighlightInset,vButtonHighlightWidth,vButtonHighlightHeight,vArcSize,vArcSize));
    g2.fillRoundRect(vHighlightInset,vHighlightInset,vButtonHighlightWidth,vButtonHighlightHeight,vHighlightArcSize,vHighlightArcSize);


    // Draw the text (if any)
     
    if (text != null)
    {
      g2.setColor(Color.RED);

      Font vFont = g2.getFont().deriveFont(Font.BOLD, (float)(((float)height) * .5));
    
      g2.setFont(vFont);

      FontMetrics vMetrics = g2.getFontMetrics();
      Rectangle2D vStringBounds = vMetrics.getStringBounds(text,g2);

      float x = (float)((width / 2) - (vStringBounds.getWidth() / 2));
      float y = (float)((height / 2) + (vStringBounds.getHeight() / 2)) - vMetrics.getDescent();

      g2.setClip(new RoundRectangle2D.Float(0,0,width,height,vArcSize,vArcSize));
      g2.setColor(Color.RED.darker().darker());
      g2.drawString(text,x,y);

      g2.setColor(Color.BLACK);
      g2.setClip(new RoundRectangle2D.Float(0,0,vButtonGaugeWidth,height,vArcSize,vArcSize));
      g2.drawString(text,x,y);
    }
    
    return image;
  }
}
