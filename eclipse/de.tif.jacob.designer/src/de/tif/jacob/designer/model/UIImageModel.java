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
import org.eclipse.draw2d.geometry.Rectangle;
import de.tif.jacob.core.definition.impl.jad.castor.CastorCaption;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.Image;
import de.tif.jacob.core.definition.impl.jad.castor.LocalInputField;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class UIImageModel extends UIGroupElementModel implements UIInputElementModel, UIICaptionProviderModel
{
	private UICaptionModel captionModel ;
	public UIImageModel()
	{
	  super(null, null, null,new CastorGuiElement());
	  CastorGuiElementChoice choice = new CastorGuiElementChoice();
	  LocalInputField        input  = new LocalInputField();
	  Image                  image  = new Image();
	  CastorCaption          caption= new CastorCaption();
	  caption.setHalign(CastorHorizontalAlignment.valueOf(JacobDesigner.getPluginProperty(JacobDesigner.LABEL_ALIGN, CastorHorizontalAlignment.LEFT.toString())));
	  
	  image.setDimension(new CastorDimension());
	  CastorDimension dim = new CastorDimension();
	  dim.setWidth(200);
	  dim.setHeight(50);
	  caption.setDimension(dim);
	  image.setCaption(caption);
	  image.setInForm(true);
	  caption.setLabel("Caption");
	  input.setImage(image);
	  choice.setLocalInputField(input);
	  getCastor().setCastorGuiElementChoice(choice);
	  getCastor().setVisible(true);
	}
	
	/**
   * 
   */
  public UIImageModel(JacobModel jacob, UIGroupContainer container,  UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container,group, guiElement);

    captionModel = new UICaptionModel(jacob,container, this);
  }

	/**
	 * true  => Die �berschrift wird als eigenst�ndiges Element im Formlayout gezeichnet
	 * false => Das Model zeichnet die Caption selbst.
	 */
  public boolean   isCaptionExtern()
  {
    return true;
  }

  public void setCastorCaption(CastorCaption caption)
  {
    getCastorImage().setCaption(caption);
  }
  
  
  public CastorCaption getCastorCaption()
  {
    return getCastorImage().getCaption();
  }
  
  @Override
  public CastorDimension getCastorDimension()
  {
    return getCastorImage().getDimension();
  }
  

  public String suggestI18NKey()
  {
    return "IMAGE";
  }


  @Override
  public void renameI18NKey(String fromName, String toName)
  {
    if (getCastorCaption() != null && getCastorCaption().getLabel().equals(fromName))
      setCaption(toName); // firePropertyChangeEvent(!!!) to update the GUI-Editor
  }
  
  /**
   * @param key the key to check WITH the % at first character
   * @return
   * 
   */
  @Override
  public boolean isI18NKeyInUse(String key)
  {
    return StringUtil.saveEquals(key,getCaption());
  }
    
  public String getDefaultNameSuffix()
  {
    return "Image";
  }
    
  /**
   *
   */
  public String getDefaultCaptionAlign()
  {
    return ALIGN_LEFT;
  }
  
  public String getDefaultCaption()
  {
    return "Caption";
  }
  
  @Override
  public Rectangle getDefaultCaptionConstraint(Rectangle parentConstraint)
  {
    Rectangle result = new Rectangle();
    result.x      = parentConstraint.x;
    result.y      = parentConstraint.y-DEFAULT_CAPTION_HEIGHT;
    result.width  = DEFAULT_CAPTION_WIDTH;
    result.height = DEFAULT_CAPTION_HEIGHT;
    
    return result;
  }
  
  public String getTemplateFileName()
  {
//    return "IImageEventHandler.java";
    return null;
  }

	public void setSize(Dimension size)
	{
		Dimension save = getSize();
		// Falls der ale Wert eine Dimension von [0,0] hat, wird die Caption
		// des Elements auf die neue Gr��e angepasst.
		//
		if(save.width==0 && save.height==0)
		{
		  getCastorCaption().getDimension().setHeight(DEFAULT_CAPTION_HEIGHT);
		  getCastorCaption().getDimension().setWidth(DEFAULT_CAPTION_WIDTH);
		}
		getCastorImage().getDimension().setHeight(size.height);
		getCastorImage().getDimension().setWidth(size.width);
		firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
	}

	public Point getLocation()
	{
	  return new Point(getCastorImage().getDimension().getX(), getCastorImage().getDimension().getY());
	}
	
	public Dimension getSize()
	{
	  return new Dimension(getCastorImage().getDimension().getWidth(), getCastorImage().getDimension().getHeight());
	}

	public UICaptionModel getCaptionModel()
	{
	  if(captionModel==null)
		  captionModel = new UICaptionModel(getJacobModel(), getGroupContainerModel(), this);
	  return captionModel;
	}
	
	private Image getCastorImage()
	{
	  return getCastor().getCastorGuiElementChoice().getLocalInputField().getImage();
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
