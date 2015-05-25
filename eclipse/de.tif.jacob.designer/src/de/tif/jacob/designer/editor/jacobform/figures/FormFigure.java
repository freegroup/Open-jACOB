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
 * Created on Oct 29, 2004
 *
 */
package de.tif.jacob.designer.editor.jacobform.figures;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.GridLayer;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.util.GradientLabel;

/**
 *
 */
public class FormFigure extends FreeformLayer implements ObjectFigure
{
  public FormFigure()
  {
    setBorder(new MarginBorder(3));
    setLayoutManager(new FreeformLayout());
  }
  
  public void setHighlight(HighLightState value)
  {
  }

  
  public void setTooltip(String text)
  {
    this.setToolTip(new GradientLabel(text));
  }

  public void setText(String text)
  {
  }
  
  public void setHook(boolean hasHook)
  {
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
