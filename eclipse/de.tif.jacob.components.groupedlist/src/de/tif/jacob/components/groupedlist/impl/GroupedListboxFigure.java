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
package de.tif.jacob.components.groupedlist.impl;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.jacobform.figures.DecoratedLabelFigure;
import de.tif.jacob.designer.editor.jacobform.figures.ImageFigure;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class GroupedListboxFigure extends DecoratedLabelFigure
{
  private final Locator barLocator = new Locator() {
    public void relocate(IFigure target) 
    {
      Rectangle parent = getBounds();
      target.setBounds(new Rectangle(parent.x, parent.y, parent.width, parent.height));
    }
  };
  private final ScrollPane pane;
  private final Label      label;
  
  public GroupedListboxFigure()
  {
      setBackgroundColor(Constants.COLOR_INPUTBACKGROUND);
      setLabelAlignment(PositionConstants.LEFT);
      setTextAlignment(PositionConstants.TOP);
      setBorder(new SchemeBorder(SchemeBorder.SCHEMES.LOWERED));
      setOpaque(true);

      label = new Label("");
      label.setFont(new Font(null, "Arial", 7, SWT.NORMAL));
      
      pane = new ScrollPane();
      pane.setHorizontalScrollBarVisibility(ScrollPane.NEVER);
      pane.setVerticalScrollBarVisibility(ScrollPane.ALWAYS);
      add(pane,barLocator,0);
      
      add(label,barLocator);
  }

  public String getText() 
  {
    return label.getText();
  }

  public void setText(String s) 
  {
    label.setText(s);
  }
  
  @Override
  public void setFont(Font arg0)
  {
    label.setFont(arg0);
  }
}

