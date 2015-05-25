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
 * Created on 06.01.2006
 *
 */
package de.tif.jacob.designer.editor.jacobform.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.swt.graphics.Image;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.util.GradientLabel;

public class VerticalButtonBarFigure extends  Figure implements ObjectFigure
{
  private IFigure pane;
  public Image decoration  = JacobDesigner.getImage("toolbar_vbuttonbar16.png");

  public VerticalButtonBarFigure()
  {
    ToolbarLayout layoutThis = new ToolbarLayout();
    layoutThis.setStretchMinorAxis( true );
    layoutThis.setMinorAlignment( ToolbarLayout.ALIGN_TOPLEFT );
    layoutThis.setSpacing( 2 );
    layoutThis.setVertical( true );

    ScrollPane scrollpane = new ScrollPane();
    pane = new FreeformLayer();
    pane.setLayoutManager(layoutThis);
    setLayoutManager(new StackLayout());
    add(scrollpane);
    scrollpane.setViewport(new FreeformViewport());
    scrollpane.setContents(pane);
    scrollpane.setVerticalScrollBarVisibility(ScrollPane.AUTOMATIC);
    scrollpane.setHorizontalScrollBarVisibility(ScrollPane.NEVER);

    setOpaque(true);
    setBorder(new SchemeBorder(SchemeBorder.SCHEMES.RAISED));
  }

  protected void paintClientArea(Graphics g)
  {
    g.pushState();

    g.translate(getLocation());
    g.drawImage(decoration,0,0);

    g.popState();
    super.paintClientArea(g);
  }  
  
  
  public void setTooltip(String text)
  {
  }

  public void setError(String errorText)
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
  }

  public void setWarning(String warningText)
  {
  }
  
  public IFigure getContentsPane()
  {
    return pane;
  }
}
