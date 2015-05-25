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
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.preferences.I18NPreferences;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;

/**
 *
 */
public class UIDBTextModel extends UITextModel implements UIDBLocalInputFieldModel
{
  public UIDBTextModel()
  {
    super();
  }

  protected UIDBTextModel(JacobModel jacob, UIGroupContainer container, UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container, group, guiElement);
  }
  
  public String getDefaultDbType()
  {
    return FieldModel.DBTYPE_TEXT;
  }

  public String getDefaultNameSuffix()
  {
    return StringUtils.capitalise(getFieldModel().getName());
  }
  
  public String getDefaultCaption()
  {
    return getFieldModel().getLabel();
  }

  public String suggestI18NKey()
  {
    String key = getFieldModel().getLabel();
    if (key.startsWith("%"))
      return key.substring(1);
    return (getFieldModel().getTableModel().getName() + getJacobModel().getSeparator() + getFieldModel().getName()).toUpperCase();
  }
  
  public IPropertyDescriptor[] getPropertyDescriptors()
	{
		  IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
		  IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
			for (int i = 0; i < superDescriptors.length; i++)
				descriptors[i] = superDescriptors[i];
			descriptors[superDescriptors.length] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_FIELD, "Field",(String[])getJacobModel().getTableAliasModel(getGroupModel().getTableAlias()).getFieldNames(this).toArray(new String[0]), PROPERTYGROUP_DB);
		return descriptors;
	}
	
	public void setPropertyValue(Object propName, Object val)
	{
		if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_FIELD))
		  setField(((Integer)val).intValue());
		else
			super.setPropertyValue(propName, val);
	}
	
	public Object getPropertyValue(Object propName)
	{
		if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_FIELD))
		  return new Integer(getJacobModel().getTableAliasModel(getGroupModel().getTableAlias()).getFieldNames(this).indexOf(getFieldModel().getName()));
		else
			return super.getPropertyValue(propName);
	}
	
	public FieldModel getFieldModel()
	{
	  if(getCastor().getCastorGuiElementChoice().getLocalInputField().getTableField()==null)
	  {
	    String defaultField = getGroupModel().getTableAliasModel().getFieldNames(this).get(0).toString();
	    getCastor().getCastorGuiElementChoice().getLocalInputField().setTableField(defaultField);
	  }
	  return getGroupModel().getTableAliasModel().getFieldModel(getCastor().getCastorGuiElementChoice().getLocalInputField().getTableField());
	}
	
	public void setField(int index)
	{
	  FieldHelper.setField(this, index);
	}

	public void setField(String fieldName)
	{
	  FieldHelper.setField(this, fieldName);
	}
	
	public void renameFieldReference(FieldModel field, String fromName, String toName)
	{
	  FieldHelper.renameFieldReference(this, field, fromName, toName);
	}

	public String getError()
  {
    if(getFieldModel() == getFieldModel().getTableModel().NULL_FIELD)
      return "Text input field ["+getName()+"] has not a valid reference to a column in the table ["+getFieldModel().getTableModel().getName()+"]";
    return null;
  }
  
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    if(model == getFieldModel())
      result.addReferences(this);
  }
}
