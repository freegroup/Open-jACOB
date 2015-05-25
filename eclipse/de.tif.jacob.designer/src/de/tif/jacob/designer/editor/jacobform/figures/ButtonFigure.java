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

//import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

import de.tif.jacob.designer.editor.Constants;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ButtonFigure extends DecoratedLabelFigure
{
  public ImageFigure searchdirectionDecoration  = new ImageFigure(DECORATION_SEARCH_NONE);

  private Dimension myMinSize= null;
  
  Locator searchdirectionLocator = new Locator() {
  	public void relocate(IFigure target) 
  	{
  		Rectangle parentBound = target.getParent().getBounds();
//  		Dimension imageDim = searchdirectionDecoration.getSize();
  		int x = parentBound.x+parentBound.width-14-1;
  		int y = parentBound.y+parentBound.height-14-1;
  		target.setBounds(new Rectangle(x, y, 14, 14));
  	}
  };
  

  public ButtonFigure() 
  { 
    setBackgroundColor(Constants.COLOR_HEADER);
    setLabelAlignment(PositionConstants.CENTER);
    setBorder(Constants.BORDER_BUTTON); 
    setForegroundColor(Constants.COLOR_FONT);
  
    setOpaque(false); 
    setFont(Constants.FONT_NORMAL); 

    add(searchdirectionDecoration, searchdirectionLocator);
  }

  @Override
  public Dimension getPreferredSize(int wHint, int hHint)
  {
    // What's a hack :-/
    // Required to set a propper height for the VerticalToolbar
    if(myMinSize!=null)
      return new Dimension(super.getPreferredSize(wHint, hHint).width,myMinSize.height);
    return super.getPreferredSize(wHint, hHint);
  }
  
  @Override
  public Dimension getMinimumSize(int wHint, int hHint)
  {
    // What's a hack :-/
    // Required to set a propper height for the VerticalToolbar
    if(myMinSize!=null)
      return new Dimension(super.getMinimumSize(wHint, hHint).width,myMinSize.height);
    return super.getMinimumSize(wHint, hHint);
  }



  @Override
  public void setMinimumSize(Dimension arg0)
  {
    super.setMinimumSize(arg0);
    this.myMinSize = arg0;
  }

  public void setFilldirectionDecoration(Image deco)
  {
    searchdirectionDecoration.setImage(deco);
  }
  
  protected void paintFigure(Graphics graphics) 
  {
    graphics.pushState();
    graphics.setForegroundColor(Constants.COLOR_BUTTON_START);
    graphics.setBackgroundColor(Constants.COLOR_BUTTON_END);
    graphics.fillGradient(getBounds(),true);
    graphics.popState();
    graphics.setForegroundColor(Constants.COLOR_FONT);
    super.paintFigure(graphics);
  }

  public void setEmphasize(boolean emphasize)
  {
    setFont(emphasize?Constants.FONT_NORMAL_BOLD: Constants.FONT_NORMAL); 
  }
  
  public void setAlign(int align)
  {
    setLabelAlignment(align);
  }
}

