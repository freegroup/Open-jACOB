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

import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.util.GradientLabel;
import de.tif.jacob.designer.editor.util.ShadowBorder;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class StyledTextFigure extends DecoratedLabelFigure
{
  Label label;
  Locator headerLocator = new Locator() {
    public void relocate(IFigure target) 
    {
      Rectangle parentBound = getBounds();
      target.setBounds(new Rectangle(parentBound.x+1, parentBound.y+3, parentBound.width-3, 10));
    }
  };
  
  Locator labelLocator = new Locator()
  {
    public void relocate(IFigure target)
    {
      Rectangle parentBound = getBounds();
      target.setBounds(new Rectangle(parentBound.x+1, parentBound.y+1, parentBound.width, 14));
    }
  };

  public StyledTextFigure()
  {
    Label header = new Label();
    header.setFont(Constants.FONT_SMALL);
    header.setText("Styled Text");
    header.setBackgroundColor(Constants.COLOR_STICKYNOTE);
    header.setLabelAlignment(PositionConstants.RIGHT);
    add(header, headerLocator);
    
    label = new Label();
    label.setOpaque(false);
    label.setLabelAlignment(PositionConstants.LEFT);
    label.setForegroundColor(Constants.COLOR_FONT);
    label.setFont(Constants.FONT_GROUPHEADER);
    add(label, labelLocator);
    
    setBorder(new LineBorder(Constants.COLOR_BORDER_BRIGHT));
  }
  
  public String getText()
  {
    return label.getText();
  }
  
  public void setText(String arg0)
  {
    label.setText(arg0);
  }

  public void setTooltip(String text)
  {
    label.setToolTip(new GradientLabel(text));
  }

  public Label getLabel()
  {
    return label;
  }
  
}