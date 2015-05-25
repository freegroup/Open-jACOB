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
package de.tif.jacob.components.progressbar.impl;

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
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.geometry.Rectangle;

import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.jacobform.figures.DecoratedLabelFigure;
import de.tif.jacob.designer.editor.jacobform.figures.Graphics2DRenderer;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ProgressbarFigure extends DecoratedLabelFigure
{
  final Graphics2DRenderer renderer = new Graphics2DRenderer();
  public static float[] BLUR = {0.10f, 0.10f, 0.10f, 0.10f, 0.30f, 0.10f, 0.10f, 0.10f, 0.10f};

 
  private Color buttonColor = Color.gray;
  private String text = "50%";
  private Color foregroundColor = Color.blue.darker().darker().darker();

  public ProgressbarFigure()
  {
    setLabelAlignment(PositionConstants.CENTER);

    setOpaque(true);
    setFont(Constants.FONT_NORMAL);
  }

  @Override
  protected void paintClientArea(Graphics g)
  {
    g.pushState();

    g.translate(getLocation());
    Rectangle rect = this.getBounds();
    renderer.prepareRendering(0,0,rect.width,rect.height); // prepares the Graphics2D renderer
    
    // gets the Graphics2D context
    Graphics2D g2 = renderer.getGraphics2D();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int vWidth = rect.width;
    int vHeight = rect.height;

    // Calculate the size of the button
    int vButtonHeight = vHeight;
    int vButtonWidth = vWidth;
    int vArcSize = vButtonHeight/2;

    BufferedImage vBuffer = new BufferedImage(vWidth, vHeight, BufferedImage.TYPE_INT_RGB);
    Graphics2D bg = vBuffer.createGraphics();
    bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Paint the background of the button
    bg.setColor(Color.WHITE);
    bg.fillRect(0, 0, vWidth, vHeight);

    // Create the gradient paint for the first layer of the button
    Color vGradientStartColor =  buttonColor.darker().darker().darker();
    Color vGradientEndColor = buttonColor.brighter().brighter().brighter();
    Paint vPaint = new GradientPaint(0, 0, vGradientStartColor, 0, vButtonHeight, vGradientEndColor, false);
    bg.setPaint(vPaint);

    // Paint the first layer of the button
    bg.fillRoundRect(0,0, vButtonWidth, vButtonHeight, vArcSize, vArcSize);

    
    // Calulate the size of the second layer of the button
    int vButtonGaugeHeight = vButtonHeight;
    int vButtonGaugeWidth = vButtonWidth -130;

    // Create the paint for the second layer of the button
    vPaint = new GradientPaint(0, 0, Color.BLACK, 0, vButtonHeight, foregroundColor.brighter().brighter(), false);

    // Paint the second layer of the button
    bg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.9f));
    bg.setPaint(vPaint);
    bg.setClip(new RoundRectangle2D.Float(0,0,vButtonWidth,vButtonGaugeHeight,vArcSize,vArcSize));
    bg.fillRect(0,0,vButtonGaugeWidth,vButtonGaugeHeight);
    
    
    // Calulate the size of the second layer of the button
    int vHighlightInset = 0;
    int vButtonHighlightHeight = vButtonHeight - (vHighlightInset * 2);
    int vButtonHighlightWidth = vButtonWidth - (vHighlightInset * 2);
    int vHighlightArcSize = vButtonHighlightHeight/2;

    // Create the paint for the second layer of the button
    vGradientStartColor = Color.WHITE;
    vGradientEndColor = buttonColor.brighter();
    vPaint = new GradientPaint(0,vHighlightInset,vGradientStartColor,0,vHighlightInset+(vButtonHighlightHeight), buttonColor.brighter(), false);

    // Paint the second layer of the button
    bg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.5f));
    bg.setPaint(vPaint);
    bg.setClip(new RoundRectangle2D.Float(vHighlightInset,vHighlightInset,vButtonHighlightWidth,vButtonHighlightHeight,vArcSize,vArcSize));
    bg.fillRoundRect(vHighlightInset,vHighlightInset,vButtonHighlightWidth,vButtonHighlightHeight,vHighlightArcSize,vHighlightArcSize);


    // Blur the button
    ConvolveOp vBlurOp = new ConvolveOp(new Kernel(3, 3, BLUR));
    BufferedImage vBlurredBase = vBlurOp.filter(vBuffer, null);

    // Draw our aqua button
    g2.drawImage(vBlurredBase, 0, 0, null);

    
    // Draw the text (if any)
     
    if (text != null)
    {
      g2.setColor(Color.RED);

      Font vFont = g2.getFont().deriveFont(Font.BOLD, (float)(((float)vButtonHeight) * .5));
    
      g2.setFont(vFont);

      FontMetrics vMetrics = g2.getFontMetrics();
      Rectangle2D vStringBounds = vMetrics.getStringBounds(text,g2);

      float x = (float)((vWidth / 2) - (vStringBounds.getWidth() / 2));
      float y = (float)((vHeight / 2) + (vStringBounds.getHeight() / 2)) - vMetrics.getDescent();

      g2.setClip(new RoundRectangle2D.Float(0,0,vButtonWidth,vButtonGaugeHeight,vArcSize,vArcSize));
      g2.setColor(Color.RED.darker().darker());
      g2.drawString(text,x,y);

      g2.setColor(Color.BLACK);
      g2.setClip(new RoundRectangle2D.Float(0,0,vButtonGaugeWidth,vButtonGaugeHeight,vArcSize,vArcSize));
      g2.drawString(text,x,y);
    }
    // now that we are done with Java 2D, renders Graphics2D operation
    // on the Draw2D graphics context
    renderer.render(g);
    
    g.popState();

    super.paintClientArea(g);
  }
  
}