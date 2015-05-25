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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.SimpleLoweredBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.util.GradientLabel;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ComboBoxFigure extends DecoratedLabelFigure
{
  Label input;
  
  public ComboBoxFigure()
  {
    input = new Label();
    input.setBackgroundColor(Constants.COLOR_INPUTBACKGROUND);
    input.setLabelAlignment(PositionConstants.LEFT);
    input.setBorder(new SchemeBorder(SchemeBorder.SCHEMES.LOWERED));
    input.setOpaque(true);
    input.setFont(new Font(null, "Arial", 7, SWT.NORMAL));
    
    Locator imageLocator = new Locator() {
    	public void relocate(IFigure target) {
    		Rectangle parentBound = getBounds();
    		target.setBounds(new Rectangle(parentBound.x+parentBound.width-16, parentBound.y, 16, 20));
    	}
    };
    Locator inputLocator = new Locator() {
    	public void relocate(IFigure target) {
    		Rectangle parentBound = getBounds();
    		target.setBounds(new Rectangle(parentBound.x, parentBound.y, parentBound.width-16, parentBound.height));
    	}
    };

    add(input,inputLocator,0);
    add(new ImageFigure(JacobDesigner.getImage("pfeil.png")),imageLocator,0);
  }
  
  public void setText(String text)
  {
    input.setText(" "+text);
  }
  
  public void setTooltip(String text)
  {
    input.setToolTip(new GradientLabel(text));
  }

  @Override
  public void setFont(Font arg0)
  {
    input.setFont(arg0);
  }
}