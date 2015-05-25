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
import de.tif.jacob.core.definition.impl.jad.castor.CheckBox;
import de.tif.jacob.core.definition.impl.jad.castor.LocalInputField;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
import de.tif.jacob.designer.preferences.I18NPreferences;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class UICheckboxModel extends UIGroupElementModel implements UIInputElementModel, UIICaptionProviderModel
{
  private static int CHECKBOX_HEIGHT = 16;
  private static int CHECKBOX_WIDTH  = 16;
  
	private UICaptionModel captionModel ;
	
	public UICheckboxModel()
	{
	  super(null, null,null, new CastorGuiElement());
	  CastorGuiElementChoice choice   = new CastorGuiElementChoice();
	  LocalInputField        input    = new LocalInputField();
	  CheckBox               checkbox = new CheckBox();
	  CastorCaption          caption  = new CastorCaption();
	  caption.setHalign(CastorHorizontalAlignment.valueOf(getDefaultCaptionAlign()));
	  
	  checkbox.setDimension(new CastorDimension());
	  CastorDimension dim = new CastorDimension();
	  dim.setWidth(DEFAULT_CAPTION_WIDTH);
	  dim.setHeight(CHECKBOX_HEIGHT);
	  caption.setDimension(dim);
	  checkbox.setCaption(caption);
	  caption.setLabel("Caption");
	  input.setCheckBox(checkbox);
	  choice.setLocalInputField(input);
	  getCastor().setCastorGuiElementChoice(choice);
	  getCastor().setVisible(true);
		getCheckBox().getDimension().setHeight(CHECKBOX_HEIGHT);
		getCheckBox().getDimension().setWidth(CHECKBOX_WIDTH);
	}

	/**
	 * true  => Die Überschrift wird als eigenständiges Element im Formlayout gezeichnet
	 * false => Das Model zeichnet die Caption selbst.
	 */
  public boolean   isCaptionExtern()
  {
    return true;
  }

	/**
   * 
   */
  protected UICheckboxModel(JacobModel jacob, UIGroupContainer container, UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container,group, guiElement);
    captionModel = new UICaptionModel(jacob,container, this);
		getCheckBox().getDimension().setHeight(CHECKBOX_HEIGHT);
		getCheckBox().getDimension().setWidth(CHECKBOX_WIDTH);
  }

  public void setCastorCaption(CastorCaption caption)
  {
    getCheckBox().setCaption(caption);
  }
  
  public CastorCaption getCastorCaption()
  {
    return getCheckBox().getCaption();
  }

  @Override
  public CastorDimension getCastorDimension()
  {
    return getCheckBox().getDimension();
  }
  
  public String getDefaultNameSuffix()
  {
    return "Checkbox";
  }
  
  public String getDefaultCaption()
  {
    return "Caption";
  }
  
  /**
   * Per Default wird die Einstellung aus den Preferences genommen
   *
   */
  public String getDefaultCaptionAlign()
  {
    return ALIGN_LEFT;
  }

  @Override
  public Rectangle getDefaultCaptionConstraint(Rectangle parentConstraint)
  {
    Rectangle result = new Rectangle();
    result.x      = parentConstraint.x+parentConstraint.width+DEFAULT_CAPTION_SPACING;
    result.y      = parentConstraint.y;
    result.width  = DEFAULT_CAPTION_WIDTH;
    result.height = DEFAULT_CAPTION_HEIGHT;
    
    return result;
  }
    
  public String suggestI18NKey()
  {
    String key = getGroupModel().getLabel();
    if (key.startsWith("%"))
      key = key.substring(1);
    return (key + getJacobModel().getSeparator() + getCaption()).toUpperCase();
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
  
	public void setSize(Dimension size)
	{
	  // Checkbox has its fixed size.....no resize is possible
	}

	public Point getLocation()
	{
	  return new Point(getCheckBox().getDimension().getX(), getCheckBox().getDimension().getY());
	}
	
	public Dimension getSize()
	{
	  return new Dimension(getCheckBox().getDimension().getWidth(), getCheckBox().getDimension().getHeight());
	}

	
	public UICaptionModel getCaptionModel()
	{
	  if(captionModel==null)
	  {
		  captionModel = new UICaptionModel(getJacobModel(),getGroupContainerModel(),this);
      // und unabhängig der Einstellungen ist diese dann auch Links ausgerichtet.
      captionModel.setAlign(ALIGN_LEFT);
	  }
	  return captionModel;
	}
	

	private CheckBox getCheckBox()
	{
	  return getCastor().getCastorGuiElementChoice().getLocalInputField().getCheckBox();
	}
    
  /**
   * 
   */
  public String getTemplateFileName()
  {
    return "ICheckBoxEventHandler.java";
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
