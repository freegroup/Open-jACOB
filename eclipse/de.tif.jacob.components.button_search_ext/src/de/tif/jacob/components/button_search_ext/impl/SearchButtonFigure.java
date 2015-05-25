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
package de.tif.jacob.components.button_search_ext.impl;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.geometry.Rectangle;

import de.tif.jacob.components.button_search_ext.SearchButton;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.jacobform.figures.ButtonFigure;
import de.tif.jacob.designer.editor.jacobform.figures.DecoratedLabelFigure;
import de.tif.jacob.designer.editor.jacobform.figures.Graphics2DRenderer;
import de.tif.jacob.designer.editor.jacobform.figures.IRefreshVisualAdapter;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIPluginComponentModel;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class SearchButtonFigure extends ButtonFigure implements IRefreshVisualAdapter
{

  public void refreshVisual(UIPluginComponentModel model)
  {
    String filldirection= model.getCastorStringProperty(SearchButton.PROPERTY_FILLDIRECTION);
    if(filldirection==null)
      filldirection= SearchButton.DEFAULT_FILLDIRECTION;
    
    if(filldirection.equals(ObjectModel.FILLDIRECTION_BACKWARD))
      setFilldirectionDecoration(ButtonFigure.DECORATION_SEARCH_BACKWARD);
    else if(filldirection.equals(ObjectModel.FILLDIRECTION_FORWARD))
      setFilldirectionDecoration(ButtonFigure.DECORATION_SEARCH_FORWARD);
    else if(filldirection.equals(ObjectModel.FILLDIRECTION_BOTH))
      setFilldirectionDecoration(ButtonFigure.DECORATION_SEARCH_BOTH);
    else
      setFilldirectionDecoration(ButtonFigure.DECORATION_SEARCH_NONE);
  }

  @Override
  public void setText(String s)
  {
    super.setText("Search (*)");
  }
  
}