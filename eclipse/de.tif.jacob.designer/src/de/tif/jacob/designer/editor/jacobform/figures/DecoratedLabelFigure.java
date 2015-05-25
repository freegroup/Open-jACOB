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

import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Rectangle;

import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.util.GradientLabel;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DecoratedLabelFigure extends Label implements ObjectFigure, IFontConsumerFigure
{
  public ImageFigure hookDecoration  = new ImageFigure(DECORATION_NONE);
  public ImageFigure errorDecoration = new ImageFigure(DECORATION_NONE);

  Locator hookLocator = new Locator() {
  	public void relocate(IFigure target) 
  	{
  		Rectangle parentBound = getBounds();
  		target.setBounds(new Rectangle(parentBound.x+1, parentBound.y+1, 8, 8));
  	}
  };
  
  Locator errorLocator = new Locator()
  {
    public void relocate(IFigure target)
    {
      Rectangle parentBound = getBounds();
      target.setBounds(new Rectangle(parentBound.x+parentBound.width-9, parentBound.y+1, 8, 8));
    }
  };

  public DecoratedLabelFigure()
  {
    LayoutManager layout = new DelegatingLayout();
    setLayoutManager(layout);
    setForegroundColor(Constants.COLOR_FIELDNAME);
    add(hookDecoration,hookLocator);
    add(errorDecoration, errorLocator);
  }

  public void setAlign(int align)
  {
    setLabelAlignment(align);
  }
  
  public void setHighlight(HighLightState value)
  {
     this.setForegroundColor(value.getColor());
  }

  public void setHook(boolean hasHook)
  {
    if(hasHook)
      hookDecoration.setImage(DECORATION_JAVA);
    else
      hookDecoration.setImage(DECORATION_NONE);
  }
  
  public void setInfo(String infoText)
  {
  }
  
  public void setWarning(String warningText)
  {
    if(warningText!=null)
    {
      errorDecoration.setImage(DECORATION_WARNING);
      errorDecoration.setToolTip(new Label(warningText));
    }
    else
    {
      errorDecoration.setImage(DECORATION_NONE);
      errorDecoration.setToolTip(null);
    }
  }

  public void setError(String errorText)
  {
    if(errorText!=null)
    {
      errorDecoration.setImage(DECORATION_ERROR);
      errorDecoration.setToolTip(new Label(errorText));
    }
    else
    {
      errorDecoration.setImage(DECORATION_NONE);
      errorDecoration.setToolTip(null);
    }
  }
  
  public void setTooltip(String text)
  {
    this.setToolTip(new GradientLabel(text));
  }
}