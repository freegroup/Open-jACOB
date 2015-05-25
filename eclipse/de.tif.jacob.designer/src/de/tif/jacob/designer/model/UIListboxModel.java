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

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;

/**
 *
 */
public class UIListboxModel extends UIAbstractListboxModel
{
  public UIListboxModel()
  {
    super();
  }

  protected UIListboxModel(JacobModel jacob, UIGroupContainer container,  UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container, group, guiElement);
  }
  

  public IPropertyDescriptor[] getPropertyDescriptors()
	{
		  IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
		  IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length+1];
			for (int i = 0; i < superDescriptors.length; i++)
				descriptors[i] = superDescriptors[i];
			
      descriptors[superDescriptors.length] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_MULTISELECT, "Multiselect", new String[]{"yes","no"}, PROPERTYGROUP_FEATURES);
			
			return descriptors;
	}
	
  public void setPropertyValue(Object propName, Object val)
  {
    try
    {
      if (propName == ID_PROPERTY_MULTISELECT)
        setMultiselectMode(((Integer)val).intValue()==0);
      else
        super.setPropertyValue(propName, val);
    }
    catch(Exception e)
    {
      JacobDesigner.showException(e);
    }
  }
  
  public Object getPropertyValue(Object propName)
  {
    if(propName == ID_PROPERTY_MULTISELECT)
      return new Integer(getMultiselectMode()?0:1);

    return super.getPropertyValue(propName);
  }
  
  private boolean getMultiselectMode()
  {
    return getCastorListBox().getMultiselect();
  }
  
  private void setMultiselectMode(boolean mode)
  {
    boolean save = getMultiselectMode();
    if(save ==mode)
      return;

    getCastorListBox().setMultiselect(mode);
    
    firePropertyChange(PROPERTY_MULTISELECT_CHANGED, new Boolean(save), new Boolean(mode));
  }

  /**
   * 
   */
  public String getTemplateFileName()
  {
    return "IMutableListBoxEventHandler.java";
  }
 
}
