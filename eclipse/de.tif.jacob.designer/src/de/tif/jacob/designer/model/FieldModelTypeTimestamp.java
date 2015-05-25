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
 * Created on 11.11.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.model;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.TreeBidiMap;
import de.tif.jacob.core.definition.impl.jad.castor.TimestampField;
import de.tif.jacob.core.definition.impl.jad.castor.types.TimestampFieldResolutionType;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FieldModelTypeTimestamp implements FieldModelType
{
  public static final String PROPERTY_DEFAULT    = "DEFAULT";
  public static final String PROPERTY_RESOLUTION = "RESOLUTION";  
  
  public final static String RESOLUTION_MINBASE  =  "Minutes";//TimestampFieldResolutionType.MINBASE.toString();
  public final static String RESOLUTION_SECBASE  =  "Seconds";//TimestampFieldResolutionType.SECBASE.toString();
  public final static String RESOLUTION_MSECBASE =  "Milli seconds";//TimestampFieldResolutionType.MSECBASE.toString();
  
  private static BidiMap resolutionMap = new TreeBidiMap();

  public final static String[] RESOLUTIONS = 
  {
    RESOLUTION_MSECBASE, 
    RESOLUTION_SECBASE,
    RESOLUTION_MINBASE
  };
  static
  {
    resolutionMap.put(RESOLUTION_MINBASE,TimestampFieldResolutionType.MINBASE.toString());
    resolutionMap.put(RESOLUTION_SECBASE,TimestampFieldResolutionType.SECBASE.toString());
    resolutionMap.put(RESOLUTION_MSECBASE,TimestampFieldResolutionType.MSECBASE.toString());
  }

  private final TimestampField field;
  private final FieldModel parent;

  
  public FieldModelTypeTimestamp(FieldModel parent, TimestampField field)
  {
    if (null == field)
      throw new NullPointerException("[field] is a required parameter.");

    this.field=field;
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
  
  public String getResolution()
  {
    return (String)resolutionMap.getKey(field.getResolution().toString());
  }
  
  public void setDefault(String value)
  {
    String save =field.getDefault();
    if (StringUtil.saveEquals(value, save))
      return;
    
    this.field.setDefault(value);
    this.parent.firePropertyChange(PROPERTY_DEFAULT, save, value);
  }
  
  public void setResolution(String value)
  {
    String save =field.getResolution().toString();
    value = (String)resolutionMap.get(value);
    if (StringUtil.saveEquals(value, save))
      return;
    
    this.field.setResolution(TimestampFieldResolutionType.valueOf(value));
    this.parent.firePropertyChange(PROPERTY_RESOLUTION, save, value);
  }
  

}
