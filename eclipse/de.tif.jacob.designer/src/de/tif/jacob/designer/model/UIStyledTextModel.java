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
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.StyledText;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.preferences.I18NPreferences;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class UIStyledTextModel extends UIGroupElementModel
{
	public UIStyledTextModel()
	{
	  super(null, null, null, new CastorGuiElement());
	  
    StyledText label = new StyledText();
    label.setI18nkey("Enter your HTML styled text here or use a I18N Key.");
	  CastorGuiElementChoice choice = new CastorGuiElementChoice();
	  choice.setStyledText(label);
    label.setDimension(new CastorDimension());
	  getCastor().setVisible(true);
	  getCastor().setCastorGuiElementChoice(choice);
	}

	/**
   * 
   */
  protected UIStyledTextModel(JacobModel jacob, UIGroupContainer container, UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container,group, guiElement);
  }

  public String getDefaultNameSuffix()
  {
    return "StyledText";
  }

  @Override
  public CastorDimension getCastorDimension()
  {
    return getCastorStyledText().getDimension();
  }
	
	public void setSize(Dimension size)
	{
		Dimension save = getSize();
    getCastorStyledText().getDimension().setHeight(size.height);
    getCastorStyledText().getDimension().setWidth(size.width);
		firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
	}

	public Point getLocation()
	{
	  return new Point(getCastorStyledText().getDimension().getX(), getCastorStyledText().getDimension().getY());
	}
	
	public Dimension getSize()
	{
	  return new Dimension(getCastorStyledText().getDimension().getWidth(), getCastorStyledText().getDimension().getHeight());
	}


	public IPropertyDescriptor[] getPropertyDescriptors()
	{
	  IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
	  IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 2];
		for (int i = 0; i < superDescriptors.length; i++)
			descriptors[i] = superDescriptors[i];
		descriptors[superDescriptors.length] = new TextPropertyGroupingDescriptor(ID_PROPERTY_LABEL, "Label", PROPERTYGROUP_COMMON);
    descriptors[superDescriptors.length + 1] = new CheckboxPropertyDescriptor(ID_PROPERTY_POSTPROCESS, "Postprocess via Letter Engine", PROPERTYGROUP_COMMON);
		
		return descriptors;
	}
	
	public void setPropertyValue(Object propName, Object val)
	{
		if (propName ==ID_PROPERTY_LABEL)
			setLabel((String) val);
    else if (propName == ID_PROPERTY_POSTPROCESS)
      setPostProcess(((Boolean) val).booleanValue());
		else
			super.setPropertyValue(propName, val);
	}
	
  public Object getPropertyValue(Object propName)
	{
		if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_LABEL))
			return getLabel();
    if (propName == ID_PROPERTY_POSTPROCESS)
      return new Boolean(getPostProcess());

		return super.getPropertyValue(propName);
	}

  private void setPostProcess(boolean b)
  {
    this.getCastorStyledText().setPostProcessWithLetterEngine(b);
  }

  private boolean getPostProcess()
  {
    return this.getCastorStyledText().getPostProcessWithLetterEngine();
  }

  public void setLabel(String l)
	{
		String save = getLabel();
		if(save.equals(l))
		  return;
    getCastorStyledText().setI18nkey(l);
		firePropertyChange(PROPERTY_LABEL_CHANGED, save, l);
		getGroupModel().firePropertyChange(PROPERTY_ELEMENT_CHANGED,null,this);
		getGroupModel().getGroupContainerModel().firePropertyChange(PROPERTY_ELEMENT_CHANGED,null,this);
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
  
  /**
   * @param key the key to check WITH the % at first character
   * @return
   * 
   */
  @Override
  public boolean isI18NKeyInUse(String key)
  {
    return StringUtil.saveEquals(key,getLabel());
  }

  public String getLabel()
	{
	  return getCastorStyledText().getI18nkey();
	}
	

  protected StyledText getCastorStyledText()
  {
    return getCastor().getCastorGuiElementChoice().getStyledText();
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
