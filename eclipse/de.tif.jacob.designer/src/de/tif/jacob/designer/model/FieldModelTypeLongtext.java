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

import de.tif.jacob.core.definition.impl.jad.castor.LongTextField;
import de.tif.jacob.core.definition.impl.jad.castor.TextField;
import de.tif.jacob.core.definition.impl.jad.castor.types.LongTextEditModeType;
import de.tif.jacob.core.definition.impl.jad.castor.types.TextFieldSearchModeType;
import de.tif.jacob.screen.impl.html.LongText;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FieldModelTypeLongtext implements FieldModelType
{
  public final static String EDITMODE_APPEND    = LongTextEditModeType.APPEND.toString();
  public final static String EDITMODE_FULLEDIT  = LongTextEditModeType.FULLEDIT.toString();
  public final static String EDITMODE_PREPEND   = LongTextEditModeType.PREPEND.toString();
  
  public final static String[] EDITMODES = { EDITMODE_APPEND, EDITMODE_FULLEDIT, EDITMODE_PREPEND };
  
  private final LongTextField field;
  private final FieldModel parent;

  public FieldModelTypeLongtext(FieldModel parent, LongTextField field)
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

  
  public String getEditMode()
  {
    if(this.field.getEditMode()!=null)
      return this.field.getEditMode().toString();
    return "";
  }
  
  
  public void setEditMode(String value)
  {
    String save =field.getEditMode().toString();
    if (StringUtil.saveEquals(value, save))
      return;
    
    if(StringUtil.toSaveString(value).length()==0)
      this.field.setEditMode(LongTextEditModeType.valueOf(value));
    else
      this.field.setEditMode(LongTextEditModeType.valueOf(value));
    
    this.parent.firePropertyChange(ObjectModel.PROPERTY_EDIT_MODE_CHANGED, save, value);
  }

  public boolean hasChangeHeader()
  {
    return this.field.getChangeHeader();
  }
  
  public void setChangeHeader(boolean flag)
  {
    boolean save =field.getChangeHeader();
    if (save ==flag)
      return;
    
    this.field.setChangeHeader(flag);
    
    this.parent.firePropertyChange(ObjectModel.PROPERTY_ELEMENT_CHANGED, save, flag);
  }
}
