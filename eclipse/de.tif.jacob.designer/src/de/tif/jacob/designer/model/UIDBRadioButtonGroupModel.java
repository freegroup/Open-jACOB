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

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.preferences.I18NPreferences;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;

/**
 *
 */
public class UIDBRadioButtonGroupModel extends UIRadioButtonGroupModel implements UIDBLocalInputFieldModel
{
  public UIDBRadioButtonGroupModel()
  {
    super();
  }

  protected UIDBRadioButtonGroupModel(JacobModel jacob, UIGroupContainer container,  UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container, group, guiElement);
  }
  
  public String getDefaultDbType()
  {
    return FieldModel.DBTYPE_ENUM;
  }

  public String getDefaultNameSuffix()
  {
    return StringUtils.capitalise(getFieldModel().getName());
  }
  	
  public String getDefaultCaption()
  {
    return getFieldModel().getLabel();
  }
  

  /**
   * 
   */
  public String getTemplateFileName()
  {
    return "IRadioButtonGroupEventHandler.java";
  }
  
  
	public void renameFieldReference(FieldModel field, String fromName, String toName)
	{
	  FieldHelper.renameFieldReference(this, field, fromName, toName);
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
		  String[] enumValues=getFieldModel().getEnumFieldType().getEnumValues();
		  int enumCount = enumValues.length;
		  IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length+enumCount+1];
			for (int i = 0; i < superDescriptors.length; i++)
				descriptors[i] = superDescriptors[i];
			
			descriptors[superDescriptors.length] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_FIELD, "Field",(String[])getJacobModel().getTableAliasModel(getGroupModel().getTableAlias()).getFieldNames(this).toArray(new String[0]), PROPERTYGROUP_DB);
			
			for (int i = 0; i < enumCount; i++)
				descriptors[superDescriptors.length+i+1] = new CheckboxPropertyDescriptor(ID_PROPERTY_ENUMVALUE+i, enumValues[i], PROPERTYGROUP_ENUM);
			
			return descriptors;
	}
	
	public void setPropertyValue(Object propName, Object val)
	{
		if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_FIELD))
		  setField(((Integer)val).intValue());
		else if (propName instanceof String && ((String) propName).startsWith(ID_PROPERTY_ENUMVALUE))
		{
		  String[] enumValues=getFieldModel().getEnumFieldType().getEnumValues();
		  for(int i=0;i<100;i++)
		  {
		    if( (((String) propName)).equals(ID_PROPERTY_ENUMVALUE+i))
		    {
		      setEnumValueVisible(enumValues[i] ,((Boolean)val).booleanValue());
		      return;
		    }
		  }
		}
		else
			super.setPropertyValue(propName, val);
	}
	
	public Object getPropertyValue(Object propName)
	{
		if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_FIELD))
		  return new Integer(getJacobModel().getTableAliasModel(getGroupModel().getTableAlias()).getFieldNames(this).indexOf(getFieldModel().getName()));
		if (propName instanceof String && ((String) propName).startsWith(ID_PROPERTY_ENUMVALUE))
		{
		  String[] enumValues=getFieldModel().getEnumFieldType().getEnumValues();
		  for(int i=0;i<enumValues.length;i++)
		  {
		    if( (((String) propName)).equals(ID_PROPERTY_ENUMVALUE+i))
		    {
		      return isVisible(enumValues[i])?Boolean.TRUE:Boolean.FALSE;
		    }
		  }
		  return Boolean.FALSE;
		}
		else
			return super.getPropertyValue(propName);
	}
	
	public FieldModel getFieldModel()
	{
	  FieldModel fieldModel = null;
	  if(getCastor().getCastorGuiElementChoice().getLocalInputField().getTableField()==null)
	  {
	    String defaultField = getGroupModel().getTableAliasModel().getFieldNames(this).get(0).toString();
	    getCastor().getCastorGuiElementChoice().getLocalInputField().setTableField(defaultField);
		  fieldModel=getGroupModel().getTableAliasModel().getFieldModel(getCastor().getCastorGuiElementChoice().getLocalInputField().getTableField());
		  String[] enumValues=fieldModel.getEnumFieldType().getEnumValues();
		  for(int i=0;i<enumValues.length;i++)
		  {
		    setEnumValueVisible(enumValues[i],true);
		  }
	  }
	  else
	    fieldModel=getGroupModel().getTableAliasModel().getFieldModel(getCastor().getCastorGuiElementChoice().getLocalInputField().getTableField());
	  
	  return fieldModel;
	}
	
	public void setField(int index)
	{
	  FieldHelper.setField(this, index);
	}
	
	public void setField(String fieldName)
	{
	  FieldHelper.setField(this, fieldName);
	  
    getCastorRadionButtonGroup().removeAllValue();
	  String[] enumValues=getFieldModel().getEnumFieldType().getEnumValues();
	  for(int i=0;i<enumValues.length;i++)
	  {
	    setEnumValueVisible(enumValues[i],true);
	  }
	}

	public boolean isVisible(String enumValue)
	{
	  for(int i=0;i<getCastorRadionButtonGroup().getValueCount();i++)
	  {
	    if(getCastorRadionButtonGroup().getValue(i).equals(enumValue))
	      return true;
	  }
	  return false;
	}
	
	public void setEnumValueVisible(String enumValue, boolean visibleFlag)
	{
	  for(int i=0;i<getCastorRadionButtonGroup().getValueCount();i++)
	  {
	    if(getCastorRadionButtonGroup().getValue(i).equals(enumValue))
	    {
	      if(visibleFlag==false)
          getCastorRadionButtonGroup().removeValue(i);
//        return;
	    }
	  }
	  if(visibleFlag==true)
	    getCastorRadionButtonGroup().addValue(enumValue);
    
    // Wichtig: Sortierreihenfolge an das Datenbankfeld anpassen
    //
    List values = Arrays.asList(getCastorRadionButtonGroup().getValue());
    String dbValues[] = getFieldModel().getEnumFieldType().getEnumValues();
    getCastorRadionButtonGroup().removeAllValue();
    for (String string : dbValues)
    {
      if(values.contains(string))
        getCastorRadionButtonGroup().addValue(string);
    }
    
    if(visibleFlag==true)
      firePropertyChange(PROPERTY_ENUMS_CHANGED, null, enumValue);
    else
      firePropertyChange(PROPERTY_ENUMS_CHANGED, enumValue, null);
	}
	
  public String getError()
  {
    if(getFieldModel() == getFieldModel().getTableModel().NULL_FIELD)
      return "Combobox ["+getName()+"] has not a valid reference to a column in the table ["+getFieldModel().getTableModel().getName()+"]";
    return null;
  }

  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    if(model == getFieldModel())
      result.addReferences(this);
  }
}
