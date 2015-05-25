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

import java.util.ArrayList;
import java.util.List;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorAction;
import de.tif.jacob.core.definition.impl.jad.castor.CastorButton;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.Generic;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class UIButtonModel extends AbstractActionEmitterModel implements IButtonBarElementModel
{
	private UIButtonBarModel buttonBar;

  public UIButtonModel()
	{
	  super(null, null,null,new CastorGuiElement());
	  CastorGuiElementChoice choice = new CastorGuiElementChoice();
	  
    CastorButton button = new CastorButton();
    button.setAction(new CastorAction());
    button.getAction().setGeneric(new Generic());
    button.setDimension(new CastorDimension());
    
    button.setLabel(DEFAULT_LABEL);
    choice.setButton(button);
    
    getCastor().setVisible(true);
    getCastor().setCastorGuiElementChoice(choice);
	}

	/**
   * 
   */
  protected UIButtonModel(JacobModel jacob,  UIGroupContainer container, UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container, group, guiElement);
  }
	
  public CastorAction getCastorAction()
  {
    return getCastorButton().getAction();
  }
  
  public String getCastorLabel()
  {
    return getCastorButton().getLabel();
  }
  
  public void setCastorLabel(String label)
  {
    getCastorButton().setLabel(label);
  }
  
  
  public String getDefaultNameSuffix()
  {
    return getAction()+"Button";
  }
  
  public int getDefaultWidth()
  {
    return ObjectModel.DEFAULT_BUTTON_WIDTH;
  }
  
  public int getDefaultHeight()
  {
    return ObjectModel.DEFAULT_BUTTON_HEIGHT;
  }

  protected CastorButton getCastorButton()
  {
    return getCastor().getCastorGuiElementChoice().getButton();
  }
  
  @Override
  public CastorDimension getCastorDimension()
  {
    return getCastorButton().getDimension();
  }

  
	public void setSize(Dimension size)
	{
		Dimension save = getSize();
		getCastorButton().getDimension().setHeight(size.height);
		getCastorButton().getDimension().setWidth(size.width);
		firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
	}

  public void setEmphasize(boolean emphasize)
  {
    boolean save = getEmphasize();
    if (emphasize==save)
      return;

    getCastorButton().setEmphasize(emphasize);
    firePropertyChange(PROPERTY_EMPHASIZE_CHANGED, new Boolean(save), new Boolean(emphasize));
  }
  
  
	public boolean getEmphasize()
  {
    return getCastorButton().getEmphasize();
  }

  public Point getLocation()
	{
	  return new Point(getCastorButton().getDimension().getX(), getCastorButton().getDimension().getY());
	}
	
	public Dimension getSize()
	{
	  return new Dimension(getCastorButton().getDimension().getWidth(), getCastorButton().getDimension().getHeight());
	}

	
  public void renameI18NKey(String fromName, String toName)
  {
    if (getLabel() != null && getLabel().equals(fromName))
      setLabel(toName);
  }

  public void createMissingI18NKey()
  {
    String label = getLabel();
    if(label !=null && label.startsWith("%") && !getJacobModel().hasI18NKey(label.substring(1)))
      getJacobModel().addI18N(label.substring(1),"",false);
  }

  public IPropertyDescriptor[] getPropertyDescriptors()
  {
    IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 2];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    
    descriptors[superDescriptors.length] = new CheckboxPropertyDescriptor(ID_PROPERTY_EMPHASIZE, "Emphasize", PROPERTYGROUP_COMMON);
    List<String> aligns_with_default_empty = new ArrayList<String>(ALIGNS);
    aligns_with_default_empty.add(0,"");
    descriptors[superDescriptors.length+1] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_ALIGN, "Align" ,(String[]) aligns_with_default_empty.toArray(new String[0]), PROPERTYGROUP_COMMON);

    return descriptors;
  }

  /**
   * 
   */
  public void setPropertyValue(Object propName, Object val)
  {
    if (propName == ID_PROPERTY_EMPHASIZE)
      setEmphasize(((Boolean) val).booleanValue());
    else if (propName == ID_PROPERTY_ALIGN)
    {
      int index = ((Integer) val).intValue();

      if(index==0)
        setAlign(null);
      else
        setAlign((String) ALIGNS.get(index-1));
    }
    else
    super.setPropertyValue(propName, val);
  }

  public Object getPropertyValue(Object propName)
  {
    if (propName == ID_PROPERTY_EMPHASIZE)
      return new Boolean(getEmphasize());
    if (propName == ID_PROPERTY_ALIGN)
    {
      if(getAlign()==null)
        return new Integer(0);
      else
        return new Integer(ALIGNS.indexOf(getAlign())+1); // +1 => es wurde in die Liste der ALIGNS ein leereintrag hinzugefügt
    }
    return super.getPropertyValue(propName);
  }  

  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#getAlign()
   */
  public String getAlign()
  {
    if(getCastorButton().getHalign()==null)
      return ""; // use stylesheet default
    
    return getCastorButton().getHalign().toString();
  }

  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#setAlign(java.lang.String)
   */
  public void setAlign(String align)
  {
    String save = getAlign();
    if(StringUtil.saveEquals(save,align))
      return;

    getCastorButton().setHalign((align!=null&&align.length()>0)?CastorHorizontalAlignment.valueOf(align):null);
    firePropertyChange(PROPERTY_ALIGN_CHANGED, save, align);
  }
    
  public String getError()
  {
    String action = getAction();
    if((action==ACTION_SEARCH || action==ACTION_SEARCH_UPDATE || action==ACTION_LOCALSEARCH) && getRelationsetModel()==null)
    {
      String relationset = getRelationset();
      if(!RELATIONSET_DEFAULT.equals(relationset) && !RELATIONSET_LOCAL.equals(relationset))
        return "Relationset ["+relationset+"] doesn't exist.";
    }
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

  public UIButtonBarModel getButtonBarModel()
  {
    return buttonBar;
  }
  
  public void setButtonBarModel(UIButtonBarModel bar)
  {
    this.buttonBar = bar;
  }
}
