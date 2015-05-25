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
 * Created on 17.02.2005
 *
 */
package de.tif.jacob.designer.model;

import java.math.BigDecimal;
import de.tif.jacob.core.definition.impl.jad.castor.DecimalField;
import de.tif.jacob.util.ObjectUtil;

/**
 * @author andreas
 *
 */
public class FieldModelTypeDecimal implements FieldModelType
{
  public static final String PROPERTY_MIN_VALUE = "min_value";
  public static final String PROPERTY_MAX_VALUE = "max_value";
  public static final String PROPERTY_SCALE = "scale";

  private final DecimalField field;
  private final FieldModel parent;

  public FieldModelTypeDecimal(FieldModel parent, DecimalField field)
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

	public void setScale(short value)
  {
    short save = this.field.getScale();
    if (save == value)
      return;

    this.field.setScale(value);
    this.parent.firePropertyChange(PROPERTY_SCALE, new Short(save), new Short(value));
  }

  public void setMinValue(BigDecimal value)
  {
    BigDecimal save = this.field.getMin();
    if (ObjectUtil.equals(save, value))
      return;

    this.field.setMin(value);
    this.parent.firePropertyChange(PROPERTY_MIN_VALUE, save, value);
  }

  public void setMaxValue(BigDecimal value)
  {
    BigDecimal save = this.field.getMax();
    if (ObjectUtil.equals(save, value))
      return;

    this.field.setMax(value);
    this.parent.firePropertyChange(PROPERTY_MAX_VALUE, save, value);
  }

  public void setDefaultValue(BigDecimal value)
  {
    BigDecimal save = this.field.getDefault();
    if (ObjectUtil.equals(save, value))
      return;

    this.field.setDefault(value);
    this.parent.firePropertyChange(ObjectModel.PROPERTY_DEFAULT_CHANGED, save, value);
  }

  public short getScale()
  {
    return field.getScale();
  }

  public BigDecimal getMinValue()
  {
    return field.getMin();
  }

  public BigDecimal getMaxValue()
  {
    return field.getMax();
  }

  public BigDecimal getDefaultValue()
  {
    return field.getDefault();
  }

}
