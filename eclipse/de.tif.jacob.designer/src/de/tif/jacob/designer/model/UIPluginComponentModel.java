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

import de.tif.jacob.components.plugin.IComponentPlugin;
import de.tif.jacob.components.plugin.PluginComponentManager;
import de.tif.jacob.components.plugin.PluginComponentManager.Group;
import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;
import de.tif.jacob.core.definition.impl.jad.castor.OwnDrawElement;
import de.tif.jacob.core.definition.impl.jad.castor.PluginComponent;
import de.tif.jacob.designer.editor.jacobform.misc.IntegerPropertyGroupingDescriptor;

/**
 *
 */
public class UIPluginComponentModel extends UIGroupElementModel implements IButtonBarElementModel
{
  private UIButtonBarModel bar;

  public UIPluginComponentModel()
	{
	  super(null,null,null, new CastorGuiElement());
	  CastorGuiElementChoice choice  = new CastorGuiElementChoice();
	  PluginComponent element = new PluginComponent();
	  
	  CastorDimension dim = new CastorDimension();
	  dim.setWidth(200);
	  dim.setHeight(50);
    element.setDimension(dim);
	  choice.setPluginComponent(element);
	  getCastor().setCastorGuiElementChoice(choice);
	  getCastor().setVisible(true);
	}
	
  public UIPluginComponentModel(JacobModel jacob,  UIGroupContainer container,  UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container,group, guiElement);
  }


  public void setButtonBarModel(UIButtonBarModel object)
  {
    this.bar = object;
  }


  public UIButtonBarModel getButtonBarModel()
  {
      return this.bar;
  }

  @Override
	public void setSize(Dimension size)
	{
		Dimension save = getSize();
		
		if(save.equals(size))
		  return;
		
		getCastorPluginComponent().getDimension().setHeight(size.height);
		getCastorPluginComponent().getDimension().setWidth(size.width);
		firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
	}

  @Override
	public Point getLocation()
	{
	  return new Point(getCastorPluginComponent().getDimension().getX(), getCastorPluginComponent().getDimension().getY());
	}
  
  public void setJavaImplClass(String pluginId, String pluginVersion, String implClassName)
  {
    this.getCastorPluginComponent().setPluginImplClass(implClassName);
    this.getCastorPluginComponent().setPluginId(pluginId);
    this.getCastorPluginComponent().setPluginVersion(pluginVersion);
  }
	
  public String getJavaImplClass()
  {
    return this.getCastorPluginComponent().getPluginImplClass();
  }

  @Override
	public Dimension getSize()
	{
	  return new Dimension(getCastorPluginComponent().getDimension().getWidth(), getCastorPluginComponent().getDimension().getHeight());
	}

	private PluginComponent getCastorPluginComponent()
	{
	  return getCastor().getCastorGuiElementChoice().getPluginComponent();
	}
	
  @Override
  public CastorDimension getCastorDimension()
  {
    return getCastorPluginComponent().getDimension();
  }

  @Override
  public void renameI18NKey(String fromName, String toName)
  {
    // nothing to do
  }

  
  @Override
  public void renameRelationReference(String from, String to)
  {
    IComponentPlugin plugin= PluginComponentManager.getComponentPlugin(getJavaImplClass());
    plugin.renameRelationsetReference(this, from, to);
  }

  @Override
  public IPropertyDescriptor[] getPropertyDescriptors()
  {
    IComponentPlugin plugin= PluginComponentManager.getComponentPlugin(getJavaImplClass());
    
    return plugin.getPropertyDescriptors(this, super.getPropertyDescriptors());
  }

  /**
   * Redirect from the PropertySourceProvider interface
   */
  @Override
  public Object getPropertyValue(Object propName)
  {
    IComponentPlugin plugin= PluginComponentManager.getComponentPlugin(getJavaImplClass());
    Object value = plugin.getPropertyValue(this,propName);
    if(value ==null)
      return super.getPropertyValue(propName);
    return value;
  }

  /**
   * Redirect from the PropertySourceProvider interface
   */
  @Override
  public void setPropertyValue(Object propName, Object val)
  {
    IComponentPlugin plugin= PluginComponentManager.getComponentPlugin(getJavaImplClass());
    if(plugin.setPropertyValue(this, propName, val)==false)
      super.setPropertyValue(propName,val);
  }

  
  @Override
  public void setCastorProperty(String name, String value)
  {
    super.setCastorProperty(name, value);
    firePropertyChange(PROPERTY_ELEMENT_CHANGED, null, value);
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

  @Override
  public String getDefaultNameSuffix()
  {
    return "PluginComponent";
  }
    
  /**
   * Event handler creation not via template file.
   */
  @Override
  public String getTemplateFileName()
  {
    return null;
  }
  
  /**
   * Event handler creation via java interface/class template.
   */
  @Override
  public Class getTemplateClass()
  {
    IComponentPlugin plugin= PluginComponentManager.getComponentPlugin(getJavaImplClass());
    return plugin.getEventHandlerTemplateClass();
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

  public Object getAdapter(Class aClass)
  {
    IComponentPlugin plugin= PluginComponentManager.getComponentPlugin(getJavaImplClass());

    if (aClass == IButtonBarElementModel.class && plugin.getType().equals(Group.button))
        return this;
      
      return super.getAdapter(aClass);
  }
  
  /*
   * 
   * @see de.tif.jacob.designer.model.ObjectModel#getRequiredJacobVersion()
   */
  @Override
  public Version getRequiredJacobVersion()
  {
    IComponentPlugin plugin= PluginComponentManager.getComponentPlugin(getJavaImplClass());
    return plugin.getRequiredJacobVersion();
  }
}
