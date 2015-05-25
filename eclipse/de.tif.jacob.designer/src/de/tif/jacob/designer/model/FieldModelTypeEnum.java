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
 * Created on 09.11.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.model;

import java.util.Arrays;
import java.util.Iterator;
import de.tif.jacob.core.definition.impl.jad.castor.EnumerationField;
import de.tif.jacob.designer.exception.InvalidEnumValueException;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FieldModelTypeEnum implements FieldModelType
{
  public static final String PROPERTY_DEFAULT = "DEFAULT";
  
  private final EnumerationField field;
  private final FieldModel parent;
  private final JacobModel jacob;
  
  public FieldModelTypeEnum(JacobModel jacob, FieldModel parent, EnumerationField field)
  {
    if (null == field)
      throw new NullPointerException("[field] is a required parameter.");
    
    this.field = field;
    this.parent = parent;
    this.jacob  = jacob;
    if(field.getLabelCount() != field.getValueCount())
    {
      field.removeAllLabel();
/* do this on demand if the user change any label      
      for(int i=0;i<field.getValueCount();i++)
      {
        field.addLabel(field.getValue(i));
      }
*/      
    }
  }
  

  public FieldModel getFieldModel() 
  {
		return parent;
	}

  public String getDefault()
  {
    return StringUtil.toSaveString(this.field.getDefault());
  }
  
  public String[] getEnumValues()
  {
    return this.field.getValue();
  }
  
  
  public String getEnumValue(int index)
  {
    return this.field.getValue(index);
  }
  
  public String getLabel(int index)
  {
    // Falls keine Labels hinterlegt sind, wird als Label das Value
    // zurückgegeben
    if(this.field.getLabelCount()==0)
      return this.getEnumValue(index);
    
    return this.field.getLabel(index);
  }

  public String[] getLabels()
  {
    // Falls keine Labels hinterlegt sind, wird als Label das Value
    // zurückgegeben
    if(this.field.getLabelCount()==0)
      return this.getEnumValues();
    
    return this.field.getLabel();
  }
  
  protected void setEnumValues(String [] values)
  {
    String[] save= this.field.getValue();

    if(Arrays.equals(values,save))
      return;
    
    this.field.setValue(values);
//    this.parent.firePropertyChange(ObjectModel.PROPERTY_ENUMS_CHANGED, null, values);
  }

  public void setDefault(String value)
  {
    if(value!=null && value.length()==0)
      value=null;
    
    String save = field.getDefault();
    if (StringUtil.saveEquals(value, save))
      return;
    
    this.field.setDefault(value);
    this.parent.firePropertyChange(PROPERTY_DEFAULT, save, value);
  }

  /**
   * @param string
   */
  public void removeEnumValue(int index)
  {
    String value = this.field.removeValue(index);
    
    // reset default value if respective enum value is deleted
    if (value.equals(this.field.getDefault()))
    {
      setDefault(null);
    }

    // remove the enum value in all GUI references
    //
    Iterator iter =jacob.getJacobFormModels().iterator();
    while (iter.hasNext())
    {
      UIJacobFormModel obj = (UIJacobFormModel) iter.next();
      obj.removeEnumReference(parent, value);
    }
    
    this.parent.firePropertyChange(ObjectModel.PROPERTY_ENUMS_CHANGED, value, null);
  }
  
  /**
   * @param string
   */
  public void removeEnumValue(String value)
  {
    int index = getIndex(value);
    
    this.field.removeValue(index);
    
    // reset default value if respective enum value is deleted
    if (value.equals(this.field.getDefault()))
    {
      setDefault(null);
    }

    // remove the enum value in all GUI references
    //
    Iterator iter =jacob.getJacobFormModels().iterator();
    while (iter.hasNext())
    {
      UIJacobFormModel obj = (UIJacobFormModel) iter.next();
      obj.removeEnumReference(parent, value);
    }
    
    this.parent.firePropertyChange(ObjectModel.PROPERTY_ENUMS_CHANGED, value, null);
  }

  /**
   * @param index
   * @param newValue
   */
  public void setLabel(int index, String newLabel)
  {
    String save = getLabel(index);
    
    if (StringUtil.saveEquals(save, newLabel))
      return;

    if(this.field.getLabelCount()==0)
    {
      for(int i=0;i<field.getValueCount();i++)
      {
        this.field.addLabel(field.getValue(i));
      }
    }
    
    this.field.setLabel(index, newLabel);
    this.parent.firePropertyChange(ObjectModel.PROPERTY_LABEL_CHANGED, save, newLabel);
  }

  /**
   * @param index
   * @param newValue
   */
  public void setEnumValue(int index, String newValue)
  {
    String save = this.field.getValue(index);
    
    if (StringUtil.saveEquals(save, newValue))
      return;
    
    if(getIndex(newValue)!=-1)
      throw new RuntimeException("Enum value ["+newValue+"] already exist. Please use another one.");
    
    this.field.setValue(index, newValue);
    
    // change default value if respective enum value is modified
    if (save.equals(this.field.getDefault()))
    {
      setDefault(newValue);
    }

    for (UIJacobFormModel form : jacob.getJacobFormModels())
    {
      form.renameEnumReference(parent, save,newValue);
    }
    
    this.parent.firePropertyChange(ObjectModel.PROPERTY_ENUMS_CHANGED, save, newValue);
    
    // Label nachziehen
    String label = getLabel(index);
    if(StringUtil.saveEquals(label,save))
      setLabel(index,newValue);
  }

  /**
   * @param index
   * @param newValue
   */
  public void addEnumValue(String value)
  {
    if(getIndex(value)!=-1)
      throw new RuntimeException("Enum value ["+value+"] already exist. Please use another one.");
    
    this.field.addValue(value);
    this.field.addLabel(value);
    this.parent.firePropertyChange(ObjectModel.PROPERTY_ENUMS_CHANGED, null, value);
  }

  /**
   * 
   * @param value
   * @return
   */
  public int getIndex(String value)
  {
    for(int i=0;i<field.getValueCount();i++)
    {
      if(field.getValue(i).equals(value))
        return i;
    }
    return -1;
  }
}
