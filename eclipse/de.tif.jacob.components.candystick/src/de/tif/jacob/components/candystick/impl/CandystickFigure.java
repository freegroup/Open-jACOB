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
package de.tif.jacob.components.candystick.impl;

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
import java.util.Properties;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.geometry.Rectangle;

import de.tif.jacob.components.candystick.Candystick;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.jacobform.figures.DecoratedLabelFigure;
import de.tif.jacob.designer.editor.jacobform.figures.Graphics2DRenderer;
import de.tif.jacob.designer.editor.jacobform.figures.IPropertyConsumerFigure;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class CandystickFigure extends DecoratedLabelFigure implements IPropertyConsumerFigure
{
  final Graphics2DRenderer renderer = new Graphics2DRenderer();
  public static float[] BLUR = {0.10f, 0.10f, 0.10f, 0.10f, 0.30f, 0.10f, 0.10f, 0.10f, 0.10f};
 
  private Color buttonColor = Color.gray;
  private Color foregroundColor = Color.blue.darker().darker().darker();
  
  private boolean vertical = true;
  private float radius=10;
  
  public CandystickFigure()
  {
    setLabelAlignment(PositionConstants.CENTER);

    setOpaque(false);
  }

  public void setProperties(Properties props)
  {
    String orientation = props.getProperty(Candystick.PROPERTY_ORIENTATION,Candystick.orientations[0]);
    radius = Float.parseFloat(props.getProperty(Candystick.PROPERTY_RADIUS,Candystick.radius[0]));
    
    vertical = orientation.equals(Candystick.orientations[0]);
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

    BufferedImage vBuffer = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = vBuffer.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
    if(vertical)
      renderVertical(g2d, rect.width, rect.height);
    else
      renderHorizontal(g2d, rect.width, rect.height);
    
    // Blur the button
//    ConvolveOp vBlurOp = new ConvolveOp(new Kernel(3, 3, BLUR));
//    BufferedImage vBlurredBase = vBlurOp.filter(vBuffer, null);

    // Draw our aqua button
//    g2.drawImage(vBlurredBase, 0, 0, null);
    g2.drawImage(vBuffer,0,0,null);

    // now that we are done with Java 2D, renders Graphics2D operation
    // on the Draw2D graphics context
    renderer.render(g);
    
    g.popState();

    super.paintClientArea(g);
  }
  
  private void renderHorizontal(Graphics2D bg,  int width, int height)
  {
    int arc = (int)(height/1.0*radius);
    
    // Paint the background of the button
    bg.setColor(new Color(Constants.COLOR_PANE.getRed(),Constants.COLOR_PANE.getGreen(),Constants.COLOR_PANE.getBlue()));
    bg.fillRect(0, 0, width, height);

    // Create the gradient paint for the first layer of the button
    Color vGradientStartColor =  buttonColor.darker().darker().darker();
    Color vGradientEndColor = buttonColor.brighter().brighter().brighter();
    Paint vPaint = new GradientPaint(0, 0, vGradientStartColor, 0, height, vGradientEndColor, false);
    bg.setPaint(vPaint);

    // Paint the first layer of the button
    bg.fillRoundRect(0,0, width, height, arc, arc);
    
    // Calulate the size of the second layer of the button
    int gaugeHeight = height;
    int gaugeWidth = (int)(width/100.0*70.0);

    // Create the paint for the second layer of the button
    vPaint = new GradientPaint(0, 0, Color.BLACK, 0, height, foregroundColor.brighter().brighter(), false);

    // Paint the second layer of the button
    bg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.9f));
    bg.setPaint(vPaint);
    bg.setClip(new RoundRectangle2D.Float(0,0,width,gaugeHeight,arc, arc));
    bg.fillRect(0,0,gaugeWidth,gaugeHeight);
    
    // Calulate the size of the second layer of the button
    int vHighlightInset = 0;
    int vButtonHighlightHeight = height - (vHighlightInset * 2);
    int vButtonHighlightWidth = width - (vHighlightInset * 2);

    // Create the paint for the second layer of the button
    vGradientStartColor = Color.WHITE;
    vGradientEndColor = buttonColor.brighter();
    vPaint = new GradientPaint(0,vHighlightInset,vGradientStartColor,0,vHighlightInset+(vButtonHighlightHeight), buttonColor.brighter(), false);

    // Paint the second layer of the button
    bg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.5f));
    bg.setPaint(vPaint);
    bg.setClip(new RoundRectangle2D.Float(vHighlightInset,vHighlightInset,vButtonHighlightWidth,vButtonHighlightHeight,arc, arc));
    bg.fillRoundRect(vHighlightInset,vHighlightInset,vButtonHighlightWidth,vButtonHighlightHeight,arc,arc);
  }

  
  private void renderVertical(Graphics2D bg, int width, int height)
  {
    int arc = (int)(width/1.0*radius);
    
    // Paint the background of the button
    bg.setColor(new Color(Constants.COLOR_PANE.getRed(),Constants.COLOR_PANE.getGreen(),Constants.COLOR_PANE.getBlue()));
    bg.fillRect(0, 0, width, height);
    
   // Create the gradient paint for the first layer of the button
    Color vGradientStartColor =  buttonColor.darker().darker().darker();
    Color vGradientEndColor = buttonColor.brighter().brighter().brighter();
    Paint vPaint = new GradientPaint(0, 0, vGradientStartColor, width, 0, vGradientEndColor, false);
    bg.setPaint(vPaint);
    
    // Paint the first layer of the button
    bg.fillRoundRect(0,0, width, height, arc, arc);
    
    // Calulate the size of the second layer of the button
    int gaugeHeight = (int)(height/100.0*70.0);

    // Create the paint for the second layer of the button
    vPaint = new GradientPaint(0, 0, Color.BLACK, width, 0, foregroundColor.brighter().brighter(), false);

    // Paint the second layer of the button
    bg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.9f));
    bg.setPaint(vPaint);
    bg.setClip(new RoundRectangle2D.Float(0,0,width,height,arc, arc));
    bg.fillRect(0,height-gaugeHeight,width,gaugeHeight);
    
    // Calulate the size of the second layer of the button
    int vHighlightInset = 0;
    int vButtonHighlightHeight = height - (vHighlightInset * 2);
    int vButtonHighlightWidth = width - (vHighlightInset * 2);

    // Create the paint for the second layer of the button
    vGradientStartColor = Color.WHITE;
    vGradientEndColor = buttonColor.brighter();
    vPaint = new GradientPaint(0,vHighlightInset,vGradientStartColor,0,vHighlightInset+(vButtonHighlightHeight), buttonColor.brighter(), false);

    // Paint the second layer of the button
    bg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.5f));
    bg.setPaint(vPaint);
    bg.setClip(new RoundRectangle2D.Float(vHighlightInset,vHighlightInset,vButtonHighlightWidth,vButtonHighlightHeight,arc, arc));
    bg.fillRoundRect(vHighlightInset,vHighlightInset,vButtonHighlightWidth,vButtonHighlightHeight,arc,arc);
  }
}


