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

package de.tif.jacob.core.definition.impl.jad;

import de.tif.jacob.core.definition.guielements.InputFieldDefinition;
import de.tif.jacob.core.definition.guielements.TextInputFieldDefinition;
import de.tif.jacob.core.definition.impl.AbstractBrowserField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JadBrowserRuntimeField extends AbstractBrowserField
{
  static public transient final String RCS_ID = "$Id: JadBrowserRuntimeField.java,v 1.4 2009/02/11 12:18:16 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.4 $";
  
  
	/**
	 * @param name
	 * @param label
	 * @param visible
	 */
	public JadBrowserRuntimeField(CastorBrowserField field)
	{
		super(field.getName(), field.getLabel(), field.getVisible(), field.getReadonly(), field.getConfigureable());
    
    // handle properties
    if (field.getPropertyCount() > 0)
      putCastorProperties(field.getProperty());
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractBrowserField#createInputFieldDefinition(java.lang.String)
	 */
	protected InputFieldDefinition createInputFieldDefinition(String inputFieldName)
	{
    return new TextInputFieldDefinition(inputFieldName, null,null, null, null, true, isReadonly(), false, -1, 0, null, null, null, null, null);
  }

}
