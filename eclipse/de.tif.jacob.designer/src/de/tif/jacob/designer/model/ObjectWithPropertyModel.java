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
 * Created on 13.12.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.properties.plugin.IPropertyPlugin;
import de.tif.jacob.properties.plugin.PropertyPluginManager;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public abstract class ObjectWithPropertyModel extends ObjectModel
{
  private final String DESC = "DESCRIPTION";
  
  abstract CastorProperty getCastorProperty(int index);
  abstract int  getCastorPropertyCount();
  abstract void addCastorProperty(CastorProperty property);
  abstract void removeCastorProperty(CastorProperty property);


  public ObjectWithPropertyModel()
  {
  }
  
  public ObjectWithPropertyModel(JacobModel jacobModel)
  {
    super(jacobModel);
  }
  
  public final void setDescription(String description)
  {
    if(description!=null && description.length()==0)
      description=null;
    
		String save = getDescription();
		
		if(StringUtil.saveEquals(save, description))
		  return ;

		setCastorProperty(DESC,description);
		firePropertyChange(PROPERTY_DESCRIPTION_CHANGED, save, description);
  }

  public String getDescription()
  {
    return StringUtil.toSaveString(getCastorStringProperty(DESC));
  }
  
  
	public IPropertyDescriptor[] getPropertyDescriptors()
	{
		IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
		for (int i = 0; i < superDescriptors.length; i++)
			descriptors[i] = superDescriptors[i];
		descriptors[superDescriptors.length]   = new TextPropertyGroupingDescriptor(ID_PROPERTY_DESCRIPTION,  "Description", PROPERTYGROUP_COMMON);

		return descriptors;
	}
	
	public void setPropertyValue(Object propName, Object val)
	{
		if (propName == ID_PROPERTY_DESCRIPTION)
			setDescription((String) val);
		else
		  super.setPropertyValue(propName, val);
	}
	
	public Object getPropertyValue(Object propName)
	{
		if (propName == ID_PROPERTY_DESCRIPTION)
			return getDescription();

		return super.getPropertyValue(propName);
	}

	public final int getCastorIntProperty(String name, int defvalue)
  {
    try
    {
      return Integer.parseInt( getCastorStringProperty(name));
    }
    catch (Exception ex)
    {
      // ignore
    }
    return defvalue;
  }

	public final boolean getCastorBooleanProperty(String name, boolean defvalue)
  {
    try
    {
      String value =  getCastorStringProperty(name);
      if(value==null)
        return defvalue;
      return new Boolean(value).booleanValue();
    }
    catch (Exception ex)
    {
      // ignore
    }
    return defvalue;
  }


  public final float getCastorFloatProperty(String name, float defvalue)
  {
    try
    {
      String value =  getCastorStringProperty(name);
      if(value==null)
        return defvalue;
      return Float.valueOf(value);
    }
    catch (Exception ex)
    {
      // ignore
    }
    return defvalue;
  }


  public String getCastorStringProperty(String name)
  {
    CastorProperty prop = getCastorProperty(name);
    if (prop == null)
      return null;

    return prop.getValue();
  }

  /**
   * Set Castor model properties
   * @param name
   * @param value
   */
  public void setCastorProperty(String name, String value)
  {
    CastorProperty prop = getCastorProperty(name);
    if(value==null)
    {
      if(prop!=null)
        removeCastorProperty(prop);
      return;
    }
    
    if (prop == null)
    {
      prop = new CastorProperty();
      prop.setName(name);
      this.addCastorProperty(prop);
    }
    
    prop.setValue(value);
  }

  protected final CastorProperty getCastorProperty(String propName)
  {
    for (int i = 0; i < getCastorPropertyCount(); i++)
    {
      CastorProperty prop = getCastorProperty(i);
      if(propName.equals(prop.getName()))
          return prop;
    }
    return null;
  }
  
  public final Properties getProperties()
  {
    Properties props = new Properties();
    for (int i = 0; i < getCastorPropertyCount(); i++)
    {
      CastorProperty prop = getCastorProperty(i);
      if(prop.getValue()!=null)
        props.put(prop.getName(), prop.getValue());
    }
    
    return props;
  }
}