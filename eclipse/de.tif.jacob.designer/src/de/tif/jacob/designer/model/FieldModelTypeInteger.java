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

import de.tif.jacob.core.definition.impl.jad.castor.IntegerField;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FieldModelTypeInteger implements FieldModelType
{
  public static final String PROPERTY_AUTOINCREMENT = "autoincrement";
  public static final String PROPERTY_MIN_VALUE = "min_value";
  public static final String PROPERTY_MAX_VALUE = "max_value";
  
  private final IntegerField field;
  private final FieldModel parent;

  public FieldModelTypeInteger(FieldModel parent, IntegerField field)
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

  public boolean getAutoincrement()
  {
    return field.getAutoincrement();
  }
  
  public void setAutoincrement(boolean value)
  {
    boolean save =getAutoincrement();
    if (save==value)
      return;
    
    this.field.setAutoincrement(value);
    this.parent.firePropertyChange(PROPERTY_AUTOINCREMENT, new Boolean(save), new Boolean(value));
  }

  public void setMinValue(int value)
  {
    if (this.field.hasMin())
    {
      int save = this.field.getMin();
      if (value == save)
        return;

      this.field.setMin(value);
      this.parent.firePropertyChange(PROPERTY_MIN_VALUE, new Integer(save), new Integer(value));
    }
    else
    {
      this.field.setMin(value);
      this.parent.firePropertyChange(PROPERTY_MIN_VALUE, null, new Integer(value));
    }
  }
  
  public void setMaxValue(int value)
  {
    if (this.field.hasMax())
    {
      int save = this.field.getMax();
      if (value == save)
        return;

      this.field.setMax(value);
      this.parent.firePropertyChange(PROPERTY_MAX_VALUE, new Integer(save), new Integer(value));
    }
    else
    {
      this.field.setMax(value);
      this.parent.firePropertyChange(PROPERTY_MAX_VALUE, null, new Integer(value));
    }
  }
  
  public void setDefaultValue(int value)
  {
    if (this.field.hasDefault())
    {
      int save = this.field.getDefault();
      if (value == save)
        return;

      this.field.setDefault(value);
      this.parent.firePropertyChange(ObjectModel.PROPERTY_DEFAULT_CHANGED, new Integer(save), new Integer(value));
    }
    else
    {
      this.field.setDefault(value);
      this.parent.firePropertyChange(ObjectModel.PROPERTY_DEFAULT_CHANGED, null, new Integer(value));
    }
  }
  
  public void deleteMinValue()
  {
    if (this.field.hasMin())
    {
      int save = this.field.getMin();
      this.field.deleteMin();
      this.parent.firePropertyChange(PROPERTY_MIN_VALUE, new Integer(save), null);
    }
  }
  
  public void deleteMaxValue()
  {
    if (this.field.hasMax())
    {
      int save = this.field.getMax();
      this.field.deleteMax();
      this.parent.firePropertyChange(PROPERTY_MAX_VALUE, new Integer(save), null);
    }
  }
  
  public void deleteDefaultValue()
  {
    if (this.field.hasDefault())
    {
      int save = this.field.getDefault();
      this.field.deleteDefault();
      this.parent.firePropertyChange(ObjectModel.PROPERTY_DEFAULT_CHANGED, new Integer(save), null);
    }
  }
  
  public int getMinValue()
  {
    return field.getMin();
  }
  
  public int getMaxValue()
  {
    return field.getMax();
  }
  
  public int getDefaultValue()
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
