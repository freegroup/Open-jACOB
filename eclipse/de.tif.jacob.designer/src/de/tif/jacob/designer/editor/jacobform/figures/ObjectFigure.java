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
 * Created on 31.01.2005
 *
 */
package de.tif.jacob.designer.editor.jacobform.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.Constants;

/**
 *
 */
public interface ObjectFigure extends IFigure
{ 
  public final static Image DECORATION_SEARCH_FORWARD  = JacobDesigner.getImage("searchdirection_forward.png"); 
  public final static Image DECORATION_SEARCH_BACKWARD = JacobDesigner.getImage("searchdirection_backward.png"); 
  public final static Image DECORATION_SEARCH_BOTH     = JacobDesigner.getImage("searchdirection_both.png"); 
  public final static Image DECORATION_SEARCH_NONE     = JacobDesigner.getImage("searchdirection_none.png"); 

  public final static Image DECORATION_NONE     = JacobDesigner.getImage("deco_none.png"); 
  public final static Image DECORATION_QUESTION = JacobDesigner.getImage("deco_questionmark.png"); 
  public final static Image DECORATION_WARNING  = JacobDesigner.getImage("deco_warning.png"); 
  public final static Image DECORATION_ERROR    = JacobDesigner.getImage("deco_error.png"); 
  public final static Image DECORATION_JAVA     = JacobDesigner.getImage("deco_java.png"); 

  static class HighLightState {private String label;private Color color; public String toString(){return "HighLightState:"+label;} public Color getColor(){return this.color;}  private HighLightState(Color color, String label)  {this.label=label;this.color=color;}};
  
	public static final HighLightState HIGHLIGHT_TRUE  = new HighLightState(new Color(null,220,255,220),"HIGHLIGHT_TRUE");
  public static final HighLightState HIGHLIGHT_FALSE = new HighLightState(new Color(null,255,220,220),"HIGHLIGHT_FALSE");
  public static final HighLightState HIGHLIGHT_NONE  = new HighLightState(Constants.COLOR_PANE,"HIGHLIGHT_NONE");
  public static final HighLightState HIGHLIGHT_I18NERROR  = new HighLightState(new Color(null,255,100,100),"HIGHLIGHT_I18NERROR");
  public static final HighLightState HIGHLIGHT_LABEL  = new HighLightState(Constants.COLOR_FONT,"HIGHLIGHT_LABEL");
  
  public void setHighlight(HighLightState value);
  public void setHook(boolean hasHook);
  public void setInfo(String infoText);
  public void setWarning(String warningText);
  public void setError(String errorText);
  public void setText(String text);
  public void setTooltip(String text);
  public Rectangle getBounds();
}
