/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
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

package de.tif.qes.adl.element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.definition.FieldType;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class ADLFieldType
{
  static public final transient String RCS_ID = "$Id: ADLFieldType.java,v 1.1 2006-12-21 11:32:21 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  static protected final transient Log logger = LogFactory.getLog(ADLFieldType.class);
  
  private final boolean required;
  private final FieldType commonType;
  
	protected ADLFieldType(FieldType commonType, Boolean required)
  {
    this.commonType = commonType;
		this.required = required.booleanValue();
  }
  
	/**
	 * @return Returns the required.
	 */
	public final boolean isRequired()
	{
		return required;
	}

  public final FieldType getCommonFieldType()
  {
  	return this.commonType;
  }
  
//  protected final void toJacob(CastorTableField tableField, ConvertToJacobOptions options)
//  {
//    tableField.setRequired(isRequired());
//    this.commonType.toJacob(tableField, options);
//  }
  
	/**
	 * toString methode: creates a String representation of the object
	 * @return the String representation
	 * @author info.vancauwenberge.tostring plugin
	
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("ADLFieldType[");
		buffer.append("required = ").append(required);
		buffer.append(", commonType = ").append(commonType);
		buffer.append("]");
		return buffer.toString();
	}
}
