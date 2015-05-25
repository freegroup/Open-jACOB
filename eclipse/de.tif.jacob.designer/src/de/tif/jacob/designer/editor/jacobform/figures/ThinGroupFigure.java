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
package de.tif.jacob.designer.editor.jacobform.figures;

import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.util.ColorUtil;
import de.tif.jacob.designer.editor.util.GradientLabel;
import de.tif.jacob.designer.editor.util.ShadowBorder;


public class ThinGroupFigure extends Figure implements ObjectFigure
{	
  private ImageFigure hookDecoration;
  private ImageFigure i18nDecoration;

	public ThinGroupFigure()
	{
    Locator hookLocator = new Locator() {
    	public void relocate(IFigure target) 
    	{
    		Rectangle parentBound = getBounds();
    		target.setBounds(new Rectangle(parentBound.x+1, parentBound.y+1, 8, 8));
    	}
    };
    Locator i18nLocator = new Locator()
    {
      public void relocate(IFigure target)
      {
        Rectangle parentBound = getBounds();
        target.setBounds(new Rectangle(parentBound.x+parentBound.width-8, parentBound.y, 8, 8));
      }
    };

    i18nDecoration = new ImageFigure(JacobDesigner.getImage("deco_none.png"));
    add(i18nDecoration, i18nLocator);

    hookDecoration = new ImageFigure(JacobDesigner.getImage("deco_none.png"));
    add(hookDecoration,hookLocator);
  
    
  	setOpaque(true);
    setBorder(new ShadowBorder());
  	
  	XYLayout contentsLayout = new XYLayout();
  	setLayoutManager(contentsLayout);
	}
	
  
  public void setTooltip(String text)
  {
  }

  public void setText(String label)
	{
	}
	
  public Insets getInsets()
  {
    return new Insets(0,0,0,0);
  }
  
  public void setHighlight(HighLightState value)
  {
 		setBackgroundColor(ColorUtil.darker(value.getColor(),0.9f));
  }

  public void setHook(boolean hasHook)
  {
    if(hasHook)
      hookDecoration.setImage(DECORATION_JAVA);
    else
      hookDecoration.setImage(DECORATION_NONE);
  }
  
  public void setError(String errorText)
  {
    if(errorText!=null)
    {
      i18nDecoration.setImage(DECORATION_ERROR);
      i18nDecoration.setToolTip(new Label(errorText));
    }
    else
    {
      i18nDecoration.setImage(DECORATION_NONE);
      i18nDecoration.setToolTip(null);
    }
  }
  
  public void setInfo(String infoText)
  {
  }
  
  public void setWarning(String warningText)
  {
    if(warningText!=null)
    {
      i18nDecoration.setImage(DECORATION_WARNING);
      i18nDecoration.setToolTip(new Label(warningText));
    }
    else
    {
      i18nDecoration.setImage(DECORATION_NONE);
      i18nDecoration.setToolTip(null);
    }
  }
}