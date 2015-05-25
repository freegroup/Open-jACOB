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

import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.definition.impl.AbstractBrowserForeignTableField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JadBrowserForeignTableField extends AbstractBrowserForeignTableField
{

	/**
	 * @param name
	 * @param tablealiasName
	 * @param tablefieldName
	 * @param label
	 * @param sortorder
	 * @param readonly
	 * @param visible
	 * @param foreignBrowserName
	 * @param relationsetName
	 * @param filldirection
	 * @param relationName
	 */
	public JadBrowserForeignTableField(CastorBrowserField field)
	{
		super(field.getName(), //
		field.getCastorBrowserFieldChoice().getTableField().getForeign().getForeignAlias(), //
		field.getCastorBrowserFieldChoice().getTableField().getTableField(), //
		field.getLabel(), //
		SortOrder.fromJad(field.getCastorBrowserFieldChoice().getTableField().getSortOrder()), //
		field.getVisible(), //
		field.getReadonly(), //
    field.getConfigureable(),
		field.getCastorBrowserFieldChoice().getTableField().getForeign().getBrowserToUse(), //
		field.getCastorBrowserFieldChoice().getTableField().getForeign().getRelationset(), //
		getFilldirection(field.getCastorBrowserFieldChoice().getTableField().getForeign().getFilldirection()), //
		field.getCastorBrowserFieldChoice().getTableField().getForeign().getRelationToUse());
    
    // handle properties
    if (field.getPropertyCount() > 0)
      putCastorProperties(field.getProperty());
  }

	private static Filldirection getFilldirection(CastorFilldirection castorFilldirection)
	{
		if (castorFilldirection == null)
			return null;
		return Filldirection.fromJad(castorFilldirection);
	}

}
