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
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.StaticImage;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class UIStaticImageModel extends UIGroupElementModel implements UIInputElementModel
{
  
	public UIStaticImageModel()
	{
	  super(null, null, null,new CastorGuiElement());
	  CastorGuiElementChoice choice = new CastorGuiElementChoice();
    StaticImage image  = new StaticImage();
	  
	  image.setDimension(new CastorDimension());
	  CastorDimension dim = new CastorDimension();
	  dim.setWidth(200);
	  dim.setHeight(50);
	  choice.setStaticImage(image);
	  getCastor().setCastorGuiElementChoice(choice);
	  getCastor().setVisible(true);
	}
	
	/**
   * 
   */
  public UIStaticImageModel(JacobModel jacob, UIGroupContainer container,  UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container,group, guiElement);
  }


  public IPropertyDescriptor[] getPropertyDescriptors()
  {
    IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    descriptors[superDescriptors.length] = new TextPropertyGroupingDescriptor(ID_PROPERTY_TOOLTIP, "Tooltip", PROPERTYGROUP_COMMON);
    
    return descriptors;
  }
  
  public void setPropertyValue(Object propName, Object val)
  {
    if (propName ==ID_PROPERTY_TOOLTIP)
      setTooltip((String) val);
    else
      super.setPropertyValue(propName, val);
  }
  
  public Object getPropertyValue(Object propName)
  {
    if (propName == ID_PROPERTY_TOOLTIP)
      return getTooltip();

    return super.getPropertyValue(propName);
  }
  
  
  public void setTooltip(String tooltip)
  {
    if(tooltip!=null && tooltip.length()==0)
      tooltip=null;
    
    getCastorImage().setTooltip(tooltip);
  }
  
  public String getTooltip()
  {
    return StringUtil.toSaveString(getCastorImage().getTooltip());
  }

  
  public void setSrc(String filename) throws Exception
  {
    getCastorImage().setSrc(filename);
    Image img = getImg();
    getCastorImage().getDimension().setHeight(img.getBounds().height);
    getCastorImage().getDimension().setWidth(img.getBounds().width);
  }
  
  public String getSrc()
  {
    return getCastorImage().getSrc();
  }
  
  public Image getImg()
  {
    return getJacobModel().getImage(getSrc());
  }
  
  @Override
  public CastorDimension getCastorDimension()
  {
    return getCastorImage().getDimension();
  }


  @Override
  public String getDefaultNameSuffix()
  {
    return "StaticImage";
  }
    
  
  @Override
  public String getTemplateFileName()
  {
    return "IStaticImageEventHandler.java";
  }

  @Override
	public void setSize(Dimension size)
	{
    // do nothing. It is not possible to reisze a StaticImage
	}

  @Override
	public Point getLocation()
	{
	  return new Point(getCastorImage().getDimension().getX(), getCastorImage().getDimension().getY());
	}
	
  @Override
	public Dimension getSize()
	{
	  return new Dimension(getCastorImage().getDimension().getWidth(), getCastorImage().getDimension().getHeight());
	}

	
	private StaticImage getCastorImage()
	{
	  return getCastor().getCastorGuiElementChoice().getStaticImage();
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
