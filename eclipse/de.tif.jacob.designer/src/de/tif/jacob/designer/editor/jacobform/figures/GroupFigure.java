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

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.util.GradientLabel;
import de.tif.jacob.designer.editor.util.ShadowBorder;


public class GroupFigure extends Figure implements ObjectFigure
{	
	private Label label;
  private ImageFigure hookDecoration;
  private ImageFigure i18nDecoration;
  private ImageFigure contextmenuDecoration;
  private Clickable clickable;
  private Figure header;  

  private Rectangle rectangle;
  private boolean   drawTop;
  private boolean   drawLeft;
  
	protected void paintFigure(Graphics graphics)
  {
    super.paintFigure(graphics);
    if(rectangle!=null)
    {
      graphics.pushState();
      graphics.setForegroundColor(Constants.COLOR_PANE_DARK);
      graphics.translate(getLocation());
      // rahmen um die buttons zeichnen
      //
      graphics.drawRectangle(rectangle);
      
      Point center = rectangle.getCenter();
      // Linie zu der oberen (oder unteren) Kante zeichnen.
      if(drawTop)
        graphics.drawLine(center.x,0, center.x, rectangle.y);
      else
        graphics.drawLine(center.x, rectangle.getBottom().y, center.x, getSize().height);

      // Linie zu der linken (oder rechten) Kante zeichnen
      if(drawLeft)
        graphics.drawLine(0,center.y, rectangle.x, center.y);
      else
        graphics.drawLine(rectangle.getRight().x,center.y, getSize().width, center.y);
      
      graphics.popState();
    }
  }

  public GroupFigure()
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
        Rectangle parentBound = label.getTextBounds();
        target.setBounds(new Rectangle(parentBound.x+parentBound.width-8, parentBound.y, 8, 8));
      }
    };
    Locator contextmenuLocator = new Locator() {
      public void relocate(IFigure target) 
      {
        Rectangle parentBound = getBounds();
        target.setBounds(new Rectangle(parentBound.x+parentBound.width-23, parentBound.y+3, 16, 16));
      }
    };

    header = new Figure();
    ToolbarLayout headerLayout = new ToolbarLayout(false);
    headerLayout.setStretchMinorAxis(true);
    header.setBackgroundColor(Constants.COLOR_HEADER);
    header.setOpaque(true);
    header.setLayoutManager(headerLayout);

    label = new GradientLabel();
	  label.setLabelAlignment(PositionConstants.LEFT);
	  label.setOpaque(false);
  	label.setFont(Constants.FONT_GROUPHEADER);
    label.setLayoutManager(new DelegatingLayout());
    i18nDecoration = new ImageFigure(JacobDesigner.getImage("deco_none.png"));
    label.add(i18nDecoration, i18nLocator);

    hookDecoration = new ImageFigure(JacobDesigner.getImage("deco_none.png"));
    label.add(hookDecoration,hookLocator);
  
    contextmenuDecoration = new ImageFigure(JacobDesigner.getImage("deco_contextmenu.png"));
    contextmenuDecoration.setToolTip(new Label("Edit Context Menu"));
    clickable = new Clickable(contextmenuDecoration);
    label.add(clickable,contextmenuLocator);

    header.add(label);
	  header.add(new ShadowRulerFigure(Constants.COLOR_HEADER, Constants.COLOR_PANE));
    
    add(header);
    
  
  	setOpaque(true);
	  setBorder(new ShadowBorder());
		setBackgroundColor(Constants.COLOR_PANE);
  	
  	XYLayout contentsLayout = new XYLayout();
  	setLayoutManager(contentsLayout);
	}
	
  
  public void setTooltip(String text)
  {
    this.setToolTip(new GradientLabel(text));
  }

  public void setText(String label)
	{
		this.label.setText(label);
	
		revalidate();
		repaint();
	}
	
  public void setBounds(Rectangle rect)
  {
    label.setSize(rect.width,label.getMinimumSize().height+2);
    header.setSize(rect.width,header.getMinimumSize().height);
    super.setBounds(rect);
  }
  
	public Label getLabel()
  {
    return label;
  }
  
  public void setHighlight(HighLightState value)
  {
 		setBackgroundColor(value.getColor());
  }

  public void setBorder(boolean flag)
  {
    header.setVisible(flag);
    setBorder(flag?new ShadowBorder():null);
  }

  public void addActionListener(ActionListener listener)
  {
    clickable.addActionListener(listener);
  }
  
  public void setDecoration(Rectangle boundingRect, boolean drawLeft, boolean drawTop)
  {
    this.drawLeft = drawLeft;
    this.drawTop  = drawTop;
    
    if(boundingRect!=null)
    {
      boundingRect = boundingRect.getCopy();
      boundingRect.translate(-1,-1);
      boundingRect.resize(2,2);
      
    }
    rectangle = boundingRect;
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