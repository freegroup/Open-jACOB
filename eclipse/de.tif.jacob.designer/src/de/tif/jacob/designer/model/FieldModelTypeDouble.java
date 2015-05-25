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
 * Created on 18.02.2005
 *
 */
package de.tif.jacob.designer.model;

import de.tif.jacob.core.definition.impl.jad.castor.DoubleField;

/**
 * @author andreas sonntag
 *
 */
public class FieldModelTypeDouble implements FieldModelType
{
  public static final String PROPERTY_MIN_VALUE = "min_value";
  public static final String PROPERTY_MAX_VALUE = "max_value";
  
  private final DoubleField field;
  private final FieldModel parent;

  public FieldModelTypeDouble(FieldModel parent, DoubleField field)
  {
    if (null == field)
      throw new NullPointerException("Parameter [field] is required");
    this.field = field;
    this.parent = parent;
  }

  public FieldModel getFieldModel() 
  {
		return parent;
	}

  public void setMinValue(double value)
  {
    if (this.field.hasMin())
    {
      double save = this.field.getMin();
      if (value == save)
        return;

      this.field.setMin(value);
      this.parent.firePropertyChange(PROPERTY_MIN_VALUE, new Double(save), new Double(value));
    }
    else
    {
      this.field.setMin(value);
      this.parent.firePropertyChange(PROPERTY_MIN_VALUE, null, new Double(value));
    }
  }
  
  public void setMaxValue(double value)
  {
    if (this.field.hasMax())
    {
      double save = this.field.getMax();
      if (value == save)
        return;

      this.field.setMax(value);
      this.parent.firePropertyChange(PROPERTY_MAX_VALUE, new Double(save), new Double(value));
    }
    else
    {
      this.field.setMax(value);
      this.parent.firePropertyChange(PROPERTY_MAX_VALUE, null, new Double(value));
    }
  }
  
  public void setDefaultValue(double value)
  {
    if (this.field.hasDefault())
    {
      double save = this.field.getDefault();
      if (value == save)
        return;

      this.field.setDefault(value);
      this.parent.firePropertyChange(ObjectModel.PROPERTY_DEFAULT_CHANGED, new Double(save), new Double(value));
    }
    else
    {
      this.field.setDefault(value);
      this.parent.firePropertyChange(ObjectModel.PROPERTY_DEFAULT_CHANGED, null, new Double(value));
    }
  }
  
  public void deleteMinValue()
  {
    if (this.field.hasMin())
    {
      double save = this.field.getMin();
      this.field.deleteMin();
      this.parent.firePropertyChange(PROPERTY_MIN_VALUE, new Double(save), null);
    }
  }
  
  public void deleteMaxValue()
  {
    if (this.field.hasMax())
    {
      double save = this.field.getMax();
      this.field.deleteMax();
      this.parent.firePropertyChange(PROPERTY_MAX_VALUE, new Double(save), null);
    }
  }
  
  public void deleteDefaultValue()
  {
    if (this.field.hasDefault())
    {
      double save = this.field.getDefault();
      this.field.deleteDefault();
      this.parent.firePropertyChange(ObjectModel.PROPERTY_DEFAULT_CHANGED, new Double(save), null);
    }
  }
  
  public double getMinValue()
  {
    return field.getMin();
  }
  
  public double getMaxValue()
  {
    return field.getMax();
  }
  
  public double getDefaultValue()
  {
    return field.getDefault();
  }
  
  public boolean hasMinValue()
  {
    return field.hasMin();
  }
  
  public boolean hasMaxValue()
  {
    return field.hasMax();
  }
  
  public boolean hasDefaultValue()
  {
    return field.hasDefault();
  }
}
