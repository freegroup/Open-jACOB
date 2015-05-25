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
package de.tif.jacob.designer.editor.jacobform.figures;

import java.awt.font.LineMetrics;
import java.awt.geom.Line2D;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.util.GradientLabel;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class AbstractChartFigure extends Figure implements ObjectFigure
{
  protected final Graphics2DRenderer renderer = new Graphics2DRenderer();

  public ImageFigure errorDecoration = new ImageFigure(DECORATION_NONE);
  
  private Label label = new Label();
  private String text="";
  private boolean title;

  private boolean grid;
  private boolean legendX;
  private boolean legendY;
  private boolean background;
  private int     modelWidth;
  private int     modelHeight;
  
  private boolean dirty=true;
  
  Locator errorLocator = new Locator()
  {
    public void relocate(IFigure target)
    {
      Rectangle parentBound = getBounds();
      target.setBounds(new Rectangle(parentBound.x+parentBound.width-13, parentBound.y+3, 8, 8));
    }
  };
  
  
  public AbstractChartFigure()
  {
    setBorder(new ShadowBorder(Constants.COLOR_PANE));
    
    label.setFont(Constants.FONT_SMALL);
    LayoutManager layout = new DelegatingLayout();
    setLayoutManager(layout);
    add(errorDecoration, errorLocator);
  }

  protected void paintClientArea(Graphics g)
  {
    g.pushState();

    g.translate(getLocation());
    drawData(g);

    g.popState();
    super.paintClientArea(g);
  }

  public void drawData(Graphics g)
  {
  }
  
  public void setHighlight(HighLightState value)
  {
  }

  public void setHook(boolean hasHook)
  {
  }

  public void setInfo(String infoText)
  {
  }

  public void setText(String text)
  {
    this.text = text;
    this.repaint();
  }

  public void setWarning(String warningText)
  {
    if(warningText!=null)
    {
      errorDecoration.setImage(DECORATION_WARNING);
      setToolTip(new Label(warningText));
    }
    else
    {
      errorDecoration.setImage(DECORATION_NONE);
      setToolTip(null);
    }
  }

  public void setError(String errorText)
  {
    if(errorText!=null)
    {
      errorDecoration.setImage(DECORATION_ERROR);
      setToolTip(new Label(errorText));
    }
    else
    {
      errorDecoration.setImage(DECORATION_NONE);
      setToolTip(null);
    }
  }

  public void setBackground(boolean background)
  {
    this.background = background;
    dirty=true;
    this.repaint();
  }

  public void setGrid(boolean grid)
  {
    this.grid = grid;
    dirty=true;
    this.repaint();
  }

  public void setLegendX(boolean legendX)
  {
    this.legendX = legendX;
    dirty=true;
    this.repaint();
  }

  public void setLegendY(boolean legendY)
  {
    this.legendY = legendY;
    dirty=true;
    this.repaint();
  }

  public void setTitle(boolean title)
  {
    this.title = title;
    dirty=true;
    this.repaint();
  }
  public Label getLabel()
  {
    return label;
  }

  
  public void setTooltip(String text)
  {
    this.setToolTip(new GradientLabel(text));
  }

  public void setLabel(Label label)
  {
    this.label = label;
  }

  public int getModelWidth()
  {
    return modelWidth;
  }

  public void setModelWidth(int modelWidth)
  {
    this.modelWidth = modelWidth;
  }

  public int getModelHeight()
  {
    return modelHeight;
  }

  public void setModelHeight(int modelHeight)
  {
    this.modelHeight = modelHeight;
  }

  public boolean isDirty()
  {
    return dirty;
  }

  public void setDirty(boolean dirty)
  {
    this.dirty = dirty;
  }

  public String getText()
  {
    return text;
  }

  public boolean isTitle()
  {
    return title;
  }

  public boolean isGrid()
  {
    return grid;
  }

  public boolean isLegendX()
  {
    return legendX;
  }

  public boolean isLegendY()
  {
    return legendY;
  }

  public boolean hasBackground()
  {
    return background;
  }

}