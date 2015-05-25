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
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.model.UIDBLocalInputFieldModel.FieldHelper;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
/**
 *
 */
public class UIDBTextfieldComboboxModel extends UIComboboxModel implements UIDBLocalInputFieldModel
{
  public UIDBTextfieldComboboxModel()
  {
    super();
  }

  protected UIDBTextfieldComboboxModel(JacobModel jacob, UIGroupContainer container, UIGroupModel group, CastorGuiElement guiElement)
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

  /**
   * 
   */
  public String getTemplateFileName()
  {
    return "IComboBoxEventHandler.java";
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
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 3];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    descriptors[superDescriptors.length] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_FIELD, "Field", (String[]) getJacobModel().getTableAliasModel(getGroupModel().getTableAlias()).getFieldNames(this).toArray(new String[0]), PROPERTYGROUP_DB);
    descriptors[superDescriptors.length + 1] = new TextPropertyGroupingDescriptor(ID_PROPERTY_ENUMVALUE, "Comma Separated Enum Values", PROPERTYGROUP_ENUM);
    descriptors[superDescriptors.length + 2] = new CheckboxPropertyDescriptor(ID_PROPERTY_CUSTOM_ENUMVALUE, "Allow User Defined Values", PROPERTYGROUP_ENUM);
    return descriptors;
  }

  public void setPropertyValue(Object propName, Object val)
  {
    if (propName == ID_PROPERTY_FIELD)
      setField(((Integer) val).intValue());
    else if (propName == ID_PROPERTY_ENUMVALUE)
      setEnums(((String) val).split("[,]"));
    else if (propName == ID_PROPERTY_CUSTOM_ENUMVALUE)
      setAllowUserDefinedValue(((Boolean) val).booleanValue());
    else
      super.setPropertyValue(propName, val);
  }

  public Object getPropertyValue(Object propName)
  {
    if (propName == ID_PROPERTY_FIELD)
      return new Integer(getJacobModel().getTableAliasModel(getGroupModel().getTableAlias()).getFieldNames(this).indexOf(getFieldModel().getName()));
    if (propName == ID_PROPERTY_ENUMVALUE)
      return getEnumsAstString();
    if (propName == ID_PROPERTY_CUSTOM_ENUMVALUE)
      return new Boolean(getAllowUserDefinedValue());
    else
      return super.getPropertyValue(propName);
  }

  public FieldModel getFieldModel()
  {
    FieldModel fieldModel = null;
    if (getCastor().getCastorGuiElementChoice().getLocalInputField().getTableField() == null)
    {
      String defaultField = getGroupModel().getTableAliasModel().getFieldNames(this).get(0).toString();
      getCastor().getCastorGuiElementChoice().getLocalInputField().setTableField(defaultField);
      fieldModel = getGroupModel().getTableAliasModel().getFieldModel(getCastor().getCastorGuiElementChoice().getLocalInputField().getTableField());
    }
    else
      fieldModel = getGroupModel().getTableAliasModel().getFieldModel(getCastor().getCastorGuiElementChoice().getLocalInputField().getTableField());
    return fieldModel;
  }

  public void setField(int index)
  {
    FieldHelper.setField(this, index);
  }

  public void setField(String fieldName)
  {
    FieldHelper.setField(this, fieldName);
  }

  public void setAllowUserDefinedValue(boolean value)
  {
    this.getCastorComboBox().setAllowUserDefinedValue(value);
  }
  
  public boolean getAllowUserDefinedValue()
  {
    return this.getCastorComboBox().getAllowUserDefinedValue();
  }

  public void setEnums(String[] enums)
  {
    this.getCastorComboBox().setValue(enums);
    firePropertyChange(PROPERTY_ENUMS_CHANGED, null, enums);
  }

  public String getEnumsAstString()
  {
    String result = "";
    String[] enums = this.getCastorComboBox().getValue();
    for (int i = 0; enums != null && i < enums.length; i++)
    {
      if (result.length() == 0)
        result = enums[i];
      else
        result = result + ", " + enums[i];
    }
    return result;
  }

  public String getError()
  {
    return null;
  }

  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    if (model == getFieldModel())
      result.addReferences(this);
  }
}
