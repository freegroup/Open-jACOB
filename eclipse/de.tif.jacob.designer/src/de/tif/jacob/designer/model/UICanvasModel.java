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
 * Created on Oct 19, 2004
 *
 */
package de.tif.jacob.designer.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.OwnDrawElement;

/**
 *
 */
public class UICanvasModel extends UIGroupElementModel implements UIInputElementModel
{
	public UICanvasModel()
	{
	  super(null,null,null, new CastorGuiElement());
	  CastorGuiElementChoice choice  = new CastorGuiElementChoice();
	  OwnDrawElement         element = new OwnDrawElement();
	  
	  element.setDimension(new CastorDimension());
	  CastorDimension dim = new CastorDimension();
	  dim.setWidth(200);
	  dim.setHeight(50);
	  choice.setOwnDrawElement(element);
	  getCastor().setCastorGuiElementChoice(choice);
	  getCastor().setVisible(true);
	}
	
	@Override
  public void renameI18NKey(String fromName, String toName)
  {
    // nothing to do
  }

  
  /**
   * @param key the key to check WITH the % at first character
   * @return
   * 
   */
  @Override
  public boolean isI18NKeyInUse(String key)
  {
    return false;
  }

  /**
   * 
   */
  public UICanvasModel(JacobModel jacob,  UIGroupContainer container,  UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container,group, guiElement);
  }

  public String getDefaultNameSuffix()
  {
    return "OwnDraw";
  }
    
  
  public String getTemplateFileName()
  {
    return "IOwnDrawElementEventHandler.java";
  }

	public void setSize(Dimension size)
	{
		Dimension save = getSize();
		
		if(save.equals(size))
		  return;
		
		getCastorOwnDrawElement().getDimension().setHeight(size.height);
		getCastorOwnDrawElement().getDimension().setWidth(size.width);
		firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
	}

	public Point getLocation()
	{
	  return new Point(getCastorOwnDrawElement().getDimension().getX(), getCastorOwnDrawElement().getDimension().getY());
	}
	
	public Dimension getSize()
	{
	  return new Dimension(getCastorOwnDrawElement().getDimension().getWidth(), getCastorOwnDrawElement().getDimension().getHeight());
	}

	private OwnDrawElement getCastorOwnDrawElement()
	{
	  return getCastor().getCastorGuiElementChoice().getOwnDrawElement();
	}
	
  @Override
  public CastorDimension getCastorDimension()
  {
    return getCastorOwnDrawElement().getDimension();
  }

  public String getError()
  {
    return null;
  }
  
  public String getWarning()
  {
    return null;
  }
  
  public String getInfo()
  {
    return null;
  }
  
  public boolean isInUse()
  {
    return true;
  }
}
