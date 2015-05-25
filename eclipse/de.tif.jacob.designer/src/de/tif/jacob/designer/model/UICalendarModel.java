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
import de.tif.jacob.core.definition.impl.jad.castor.Calendar;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;

/**
 *
 */
public class UICalendarModel extends UIGroupElementModel implements UIInputElementModel
{
  
	public UICalendarModel()
	{
	  super(null, null, null,new CastorGuiElement());
	  CastorGuiElementChoice choice = new CastorGuiElementChoice();
    Calendar image  = new Calendar();
	  
	  image.setDimension(new CastorDimension());
	  choice.setCalendar(image);
	  getCastor().setCastorGuiElementChoice(choice);
	  getCastor().setVisible(true);
	}
	
	/**
   * 
   */
  public UICalendarModel(JacobModel jacob, UIGroupContainer container,  UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container,group, guiElement);
  }

  
  @Override
  public CastorDimension getCastorDimension()
  {
    return getCastorCalendar().getDimension();
  }


  @Override
  public String getDefaultNameSuffix()
  {
    return "Calendar";
  }
    
  
  @Override
  public String getTemplateFileName()
  {
    return "ICalendarEventHandler.java";
  }

  @Override
  public void setSize(Dimension size)
  {
    Dimension save = getSize();
    getCastorCalendar().getDimension().setHeight(size.height);
    getCastorCalendar().getDimension().setWidth(size.width);
    firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
  }

  @Override
	public Point getLocation()
	{
	  return new Point(getCastorCalendar().getDimension().getX(), getCastorCalendar().getDimension().getY());
	}
	
  @Override
	public Dimension getSize()
	{
	  return new Dimension(getCastorCalendar().getDimension().getWidth(), getCastorCalendar().getDimension().getHeight());
	}

	
	private Calendar getCastorCalendar()
	{
	  return getCastor().getCastorGuiElementChoice().getCalendar();
	}
	
  @Override
  public String getError()
  {
    return null;
  }
  
  @Override
  public String getWarning()
  {
    return null;
  }
  
  @Override
  public String getInfo()
  {
    return null;
  }
  
  @Override
  public boolean isInUse()
  {
    return true;
  }

  @Override
  public boolean isI18NKeyInUse(String key)
  {
    return false;
  }

  @Override
  public void renameI18NKey(String fromName, String toName)
  {
  }
}
