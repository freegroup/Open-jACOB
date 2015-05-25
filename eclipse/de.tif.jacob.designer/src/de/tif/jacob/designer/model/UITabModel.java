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

import org.apache.commons.lang.StringUtils;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.ReadonlyTextPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class UITabModel extends ObjectModel
{
  UIGroupModel group;
  UITabContainerModel parent;
  
	public UITabModel(UITabContainerModel parent, UIGroupModel group)
	{
    super(parent.getJacobModel());
    this.group = group;
    this.parent = parent;
	}
	
  public UIGroupModel getUIGroupModel()
  {
    return group;
  }
  
  public UITabContainerModel getUITabContainerModel()
  {
    return parent;
  }
  
  public String getHookClassName()
  {
    return group.getHookClassName();
  }

  public void generateHookClassName() throws Exception
  {
    group.generateHookClassName();
  }

  public void setLabel(String label)
  {
    String save = getLabel();
    if(StringUtil.equals(label,save))
      return;
    
    group.setLabel(label);
    firePropertyChange(PROPERTY_LABEL_CHANGED, save, label);
  }

  public String getLabel()
  {
    return group.getLabel();
  }
  
  public String getDefaultNameSuffix()
  {
    return "Pane";
  }
  

  @Override
  public String getTemplateFileName()
  {
    return "ITabPaneEventHandler.java";
  }
  
  public void createMissingI18NKey()
  {
    String label = getLabel();
    if(label !=null && label.startsWith("%") && !getJacobModel().hasI18NKey(label.substring(1)))
      getJacobModel().addI18N(label.substring(1),"",false);
  }

  /**
   * 
   * @param from the orignal key WITH the % at first character
   * @param to the new key WITH the % at first character
   */
  public void renameI18NKey(String fromName, String toName)
  {
    if (getLabel() != null && getLabel().equals(fromName))
      setLabel(toName); // firePropertyChangeEvent(!!!) to update the GUI-Editor
  }

  /**
   * @param key the key to check WITH the % at first character
   * @return
   * 
   */
  public boolean isI18NKeyInUse(String key)
  {
    return StringUtil.saveEquals(key,getLabel());
  }  
  
  public String getError()
  {
    return group.getError();
  }
  
  public String getWarning()
  {
    return group.getWarning();
  }
  
  public String getInfo()
  {
    return group.getInfo();
  }
  
  public boolean isInUse()
  {
    return group.isInUse();
  }

  public IPropertyDescriptor[] getPropertyDescriptors()
  {
    IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 3];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    descriptors[superDescriptors.length ]    = new TextPropertyGroupingDescriptor(ID_PROPERTY_LABEL, "Label", PROPERTYGROUP_COMMON);
    descriptors[superDescriptors.length + 1]    = new TextPropertyGroupingDescriptor(ID_PROPERTY_NAME, "Name", PROPERTYGROUP_COMMON);
    descriptors[superDescriptors.length + 2] = new ReadonlyTextPropertyGroupingDescriptor(ID_PROPERTY_TABLEALIAS, "Table alias", PROPERTYGROUP_DB);
    return descriptors;
  }

  
  /**
   * 
   */
  public void setPropertyValue(Object propName, Object val)
  {
    try
    {
      if (propName ==ID_PROPERTY_LABEL)
        setLabel((String) val);
      else if (propName==ID_PROPERTY_NAME)
        setName((String) val);
      else
        super.setPropertyValue(propName, val);
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }
  }

  public Object getPropertyValue(Object propName)
  {
    if (propName==ID_PROPERTY_LABEL)
      return StringUtil.toSaveString(getLabel());
    if (propName==ID_PROPERTY_NAME)
      return this.group.getName();
    if (propName ==ID_PROPERTY_TABLEALIAS)
      return group.getTableAlias();
    return super.getPropertyValue(propName);
  }


  @Override
  public String getName()
  {
    return this.group.getName();
  }


  public void setName(String name) throws Exception
  {
     this.group.setName(name);
  }
  
  @Override
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
  }
  
  @Override
  public ObjectModel getParent()
  {
    return getUITabContainerModel();
  }
}
