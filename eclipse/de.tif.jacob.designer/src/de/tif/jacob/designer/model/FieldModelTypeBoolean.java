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

import de.tif.jacob.core.definition.impl.jad.castor.BooleanField;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FieldModelTypeBoolean implements FieldModelType
{
  public static final String PROPERTY_DEFAULT = "DEFAULT";
  
  private final BooleanField field;
  private final FieldModel parent;
  private final JacobModel jacob;
  
  public FieldModelTypeBoolean(JacobModel jacob, FieldModel parent, BooleanField field)
  {
    if (null == field)
      throw new NullPointerException("[field] is a required parameter.");
    this.field = field;
    this.parent = parent;
    this.jacob  = jacob;
  }

  public FieldModel getFieldModel() 
  {
		return parent;
	}

  public Boolean getDefault()
  {
    if(this.field.hasDefault())
      return this.field.getDefault();
    return null;
  }
  
  
  public void setDefault(Boolean value)
  {
    Boolean save = getDefault();
    if(save==null)
    {
      if(value==null)
        return;
    }
    else if(save.equals(value))
    {
      return;
    }
    
    if(value==null)
      this.field.deleteDefault();
    else
      this.field.setDefault(value);
    this.parent.firePropertyChange(PROPERTY_DEFAULT, save, value);
  }
}
