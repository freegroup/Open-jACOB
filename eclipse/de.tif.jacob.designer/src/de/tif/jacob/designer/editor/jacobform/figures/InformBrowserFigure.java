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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.util.GradientLabel;


public class InformBrowserFigure extends Figure implements ObjectFigure
{	
  public ImageFigure hookDecoration  = new ImageFigure(DECORATION_NONE);

  private Label label;
	private Figure columns;
	
  Locator hookLocator = new Locator() {
    public void relocate(IFigure target) 
    {
      Rectangle parentBound = getBounds();
      target.setBounds(new Rectangle(parentBound.x+1, parentBound.y+1, 8, 8));
    }
  };
  
  public InformBrowserFigure()
	{
		label = new GradientLabel();
	  label.setLabelAlignment(PositionConstants.LEFT);
  	label.setFont(Constants.FONT_BROWSERHEADER);
    label.setLayoutManager(new DelegatingLayout());
    label.add(hookDecoration,hookLocator);
    label.setText("Caption");
	  add(label);
	  
    add(new ShadowRulerFigure(Constants.COLOR_HEADER, Constants.COLOR_PANE));
    
	  // die Columns erzeugen
	  //
	  columns = new Figure();
    columns = new GradientLabel();
	  columns.setLayoutManager(new ToolbarLayout(true));
	  columns.setOpaque(true);
	  columns.setBackgroundColor(Constants.COLOR_PANE);
	  add(columns);
    add(new ShadowRulerFigure(Constants.COLOR_HEADER, Constants.COLOR_PANE));
	  
  	ScrollPane pane = new ScrollPane();
  	pane.setVerticalScrollBarVisibility(ScrollPane.ALWAYS);
  	pane.setPreferredSize(new Dimension(10000,10000));
  	add(pane);

    LineBorder border =new LineBorder();
    border.setColor(Constants.COLOR_BORDER);
	  setBorder(border);
		setBackgroundColor(Constants.COLOR_INPUTBACKGROUND);
  	
		ToolbarLayout contentsLayout = new ToolbarLayout(false);
		contentsLayout.setStretchMinorAxis(true);
  	setLayoutManager(contentsLayout);
	}
  
  
	public void setText(String label)
	{
		this.label.setText(label);
	
		revalidate();
		repaint();
	}
	
  
  public void setTooltip(String text)
  {
    this.setToolTip(new GradientLabel(text));
  }

  
  public void clearColumns()
  {
    	columns.removeAll();
  }
  
  public void addColumn(String label)
  {
  	Label column = new Label(label);
  	
  	column.setLabelAlignment(PositionConstants.LEFT);
  	column.setFont(Constants.FONT_NORMAL);
  	column.setBorder(new MarginBorder( new Insets(2,5,2,5)));
  	column.setOpaque(false);
  	columns.add(column);

    column = new Label("");
    LineBorder border =new LineBorder();
    border.setColor(Constants.COLOR_BORDER);
    column.setBorder(border);
    column.setFont(Constants.FONT_NORMAL);
    
    columns.add(column);
  }
  
  public void setHook(boolean hasHook)
  {
    if(hasHook)
      hookDecoration.setImage(DECORATION_JAVA);
    else
      hookDecoration.setImage(DECORATION_NONE);
  }

  public void setHighlight(HighLightState value)
  {
    if(value == HIGHLIGHT_I18NERROR || value == HIGHLIGHT_LABEL)
      setForegroundColor(value.getColor());
    else
      setBackgroundColor(value.getColor());
  }


  public void setError(String errorText)
  {
  }
  
  public void setInfo(String infoText)
  {
  }
  
  public void setWarning(String warningText)
  {
  }
}