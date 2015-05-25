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

import de.tif.jacob.core.definition.impl.jad.castor.CastorCaption;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.Heading;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;

/**
 *
 */
public class UIHeadingModel extends UILabelModel
{
	public UIHeadingModel()
	{
	  Heading heading = new Heading();
	  CastorGuiElementChoice choice = new CastorGuiElementChoice();
	  CastorCaption caption = new CastorCaption();
	  
	  caption.setLabel("<unset>");
	  caption.setDimension(new CastorDimension());
	  caption.setHalign(CastorHorizontalAlignment.LEFT);
	  
	  heading.setCaption(caption);
	  choice.setHeading(heading);
	  
	  getCastor().setVisible(true);
	  getCastor().setCastorGuiElementChoice(choice);
	  }
	
	/**
   * 
   */
  protected UIHeadingModel(JacobModel jacob,UIGroupContainer container, UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob,container, group, guiElement);
  }

  public String getDefaultNameSuffix()
  {
    return "Heading";
  }
   
  protected CastorCaption getCastorCaption()
  {
    return getCastor().getCastorGuiElementChoice().getHeading().getCaption();
  }
}
