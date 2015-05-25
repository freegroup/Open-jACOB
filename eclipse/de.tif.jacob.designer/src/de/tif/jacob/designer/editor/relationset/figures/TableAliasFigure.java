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
 * Created on Jul 13, 2004
 */
package de.tif.jacob.designer.editor.relationset.figures;

import java.util.List;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.util.GradientLabel;
import de.tif.jacob.designer.editor.util.ShadowBorder;

/**
 * Figure used to represent a table in the schema
 * @author Phil Zoio
 */
public class TableAliasFigure extends Figure
{
	private ColumnsFigure columnsFigure = new ColumnsFigure();
	private Label    aliasLabel;
	private Label    tableLabel;
	private TextFlow descLabel;
  private Clickable clickable;
  private Label    clickImage;
	public TableAliasFigure(String alias, String table, String desc, Image image)
	{
		this(alias, table,desc, image, null);
	}

 
	public TableAliasFigure(String alias, String table, String desc, Image image, List colums)
	{
		IFigure header = new GradientLabel();
		header.setLayoutManager(new BorderLayout());
		
		aliasLabel = new Label(alias);
		aliasLabel.setOpaque(false);
    aliasLabel.setIcon(image);

    clickImage = new Label(JacobDesigner.getImage("collapse.gif"));
    clickable = new Clickable(clickImage);
    clickable.setOpaque(false);
    
    header.add(aliasLabel,BorderLayout.LEFT);
    header.add(clickable,BorderLayout.RIGHT);

    tableLabel = new Label(" based on <"+table+"> ");
    tableLabel.setForegroundColor(Constants.COLOR_DISABLEFONT);
    tableLabel.setBorder(new LineBorder(ColorConstants.gray));
		
		ToolbarLayout layout = new ToolbarLayout();
		layout.setVertical(true);
		layout.setStretchMinorAxis(true);
		setLayoutManager(layout);
	  setBorder(new ShadowBorder());
		setBackgroundColor(Constants.COLOR_PANE);
		setForegroundColor(Constants.COLOR_FONT);
		setOpaque(true);

    
		add(header);
		add(tableLabel);
		add(columnsFigure);
		
  
    FlowPage flowPage = new FlowPage();
    descLabel = new TextFlow(desc);
    descLabel.setForegroundColor(Constants.COLOR_DISABLEFONT);
    descLabel.setLayoutManager(new ParagraphTextLayout(descLabel, ParagraphTextLayout.WORD_WRAP_SOFT));

//    flowPage.add(descLabel);
		add(flowPage);
	}

	public void addActionListener(ActionListener listener)
	{
		clickable.addActionListener(listener);
	}
	
	public void setImage(Image image )
	{
	  aliasLabel.setIcon(image);
	}
	
  
  public void clearColumns()
  {
      columnsFigure.removeAll();
  }

	public void setText(String alias, String desc )
	{
	  aliasLabel.setText(alias);
	  descLabel.setText(desc);
	}
	

  public void expand(boolean expand)
  {
    if(!expand && columnsFigure.getParent()==this)
    {
      clickImage.setIcon(JacobDesigner.getImage("expand.gif"));
      remove(columnsFigure);
    }
    else if(expand && columnsFigure.getParent()==null)
    {
      clickImage.setIcon(JacobDesigner.getImage("collapse.gif"));
      add(columnsFigure);
    }
    
    revalidate();
  }


	public ColumnsFigure getColumnsFigure() {
		return columnsFigure;
	}
}