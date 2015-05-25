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
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Image;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.util.GradientLabel;

public class HorizontalButtonBarFigure extends Figure implements ObjectFigure
{
  public Image decoration  = JacobDesigner.getImage("toolbar_hbuttonbar16.png");

  public HorizontalButtonBarFigure()
  {
    ToolbarLayout layoutThis = new ToolbarLayout();
    layoutThis.setStretchMinorAxis( true );
    layoutThis.setMinorAlignment( ToolbarLayout.ALIGN_TOPLEFT );
    layoutThis.setSpacing( 5 );
    layoutThis.setVertical( false );
    this.setLayoutManager( layoutThis );
    
    setOpaque(false);
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
    // TODO Auto-generated method stub
  }

  public void setHighlight(HighLightState value)
  {
    // TODO Auto-generated method stub
    
  }

  public void setHook(boolean hasHook)
  {
    // TODO Auto-generated method stub
    
  }

  public void setInfo(String infoText)
  {
    // TODO Auto-generated method stub
    
  }

  public void setText(String text)
  {
    // TODO Auto-generated method stub
    
  }

  public void setWarning(String warningText)
  {
    // TODO Auto-generated method stub
    
  }
  
}
