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

import de.tif.jacob.core.definition.impl.jad.castor.TextField;
import de.tif.jacob.core.definition.impl.jad.castor.types.TextFieldSearchModeType;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FieldModelTypeText implements FieldModelType
{
  public final static String SEARCHMODE_UNBOUND    = TextFieldSearchModeType.UNBOUND.toString();
  public final static String SEARCHMODE_LEFTBOUND  = TextFieldSearchModeType.LEFTBOUND.toString();
  public final static String SEARCHMODE_RIGHTBOUND = TextFieldSearchModeType.RIGHTBOUND.toString();
  
  public final static String[] SEARCHMODES = {
    SEARCHMODE_UNBOUND, SEARCHMODE_LEFTBOUND, SEARCHMODE_RIGHTBOUND };
  
  private final TextField field;
  private final FieldModel parent;

  public FieldModelTypeText(FieldModel parent, TextField field)
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

  public String getDefault()
  {
    return StringUtil.toSaveString(this.field.getDefault());
  }
  
  public boolean getCaseSensitive()
  {
    return this.field.getCaseSensitive();
  }
  
  public int getMaxLength()
  {
    return this.field.getMaxLength();
  }
  
  public String getSearchMode()
  {
    return this.field.getSearchMode().toString();
  }
  
  public boolean getFixeLength()
  {
    if(!field.hasFixedLength())
      return false;
    
    return this.field.getFixedLength();
  }
  

  public void setFixLength(boolean value)
  {
    boolean save =field.getFixedLength();
    if (value == save)
      return;
    
    field.setFixedLength(value);
    parent.firePropertyChange(ObjectModel.PROPERTY_FIX_LENGTH_CHANGED, new Boolean(save), new Boolean(value));
  }

  public void setDefault(String value)
  {
    String save= field.getDefault();
    if (StringUtil.saveEquals(value,save))
      return;
    
    this.field.setDefault(value);
    this.parent.firePropertyChange(ObjectModel.PROPERTY_DEFAULT_CHANGED, save, value);
  }
  
  public void setCaseSensitive(boolean value)
  {
    boolean save =field.getCaseSensitive();
    if (value == save)
      return;

    field.setCaseSensitive(value);
    parent.firePropertyChange(ObjectModel.PROPERTY_CASESENSITIVE_CHANGED, new Boolean(save), new Boolean(value));
  }

  public void setMaxLength(int value)
  {
    int save = field.getMaxLength();
    if (value == save)
      return;

    field.setMaxLength(value);
    parent.firePropertyChange(ObjectModel.PROPERTY_MAX_LENGTH_CHANGED, new Integer(save),new Integer(value));
  }
  
  public void setSearchMode(String value)
  {
    String save =field.getSearchMode().toString();
    if (StringUtil.saveEquals(value, save))
      return;
    
    this.field.setSearchMode(TextFieldSearchModeType.valueOf(value));
    this.parent.firePropertyChange(ObjectModel.PROPERTY_SEARCH_MODE_CHANGED, save, value);
  }
}
