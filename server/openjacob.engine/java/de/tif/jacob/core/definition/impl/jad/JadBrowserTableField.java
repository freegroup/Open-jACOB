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

import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.definition.impl.AbstractBrowserTableField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JadBrowserTableField extends AbstractBrowserTableField
{
  static public transient final String RCS_ID = "$Id: JadBrowserTableField.java,v 1.2 2007/10/26 11:21:46 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";
  
	/**
	 * @param name
	 * @param label
	 * @param visible
	 */
	public JadBrowserTableField(CastorBrowserField field)
	{
    super(field.getName(), field.getCastorBrowserFieldChoice().getTableField().getTableField(), field.getLabel(), SortOrder.fromJad(field.getCastorBrowserFieldChoice().getTableField().getSortOrder()), field.getVisible(), field.getReadonly(), field.getConfigureable());
    
    // handle properties
    if (field.getPropertyCount() > 0)
      putCastorProperties(field.getProperty());
  }

}
