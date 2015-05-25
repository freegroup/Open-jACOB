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

import de.tif.jacob.core.definition.impl.AbstractTableAlias;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class JadTableAlias extends AbstractTableAlias
{
  static public transient final String RCS_ID = "$Id: JadTableAlias.java,v 1.1 2007/01/19 09:50:38 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";
  
	/**
	 * 
	 */
	public JadTableAlias(CastorTableAlias castorTableAlias)
	{
    super(castorTableAlias.getName(), castorTableAlias.getTable(), castorTableAlias.getCondition());
    
    // handle properties
    if (castorTableAlias.getPropertyCount() > 0)
      putCastorProperties(castorTableAlias.getProperty());
  }

}
