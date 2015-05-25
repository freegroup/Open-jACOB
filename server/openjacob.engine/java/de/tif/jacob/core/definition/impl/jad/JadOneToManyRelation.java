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

import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.impl.AbstractDefinition;
import de.tif.jacob.core.definition.impl.AbstractElement;
import de.tif.jacob.core.definition.impl.AbstractOneToManyRelation;
import de.tif.jacob.core.definition.impl.jad.castor.CastorRelation;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JadOneToManyRelation extends AbstractOneToManyRelation
{
  static public transient final String RCS_ID = "$Id: JadOneToManyRelation.java,v 1.1 2007/01/19 09:50:38 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";
  
  private final String toKeyName;
  
	/**
	 * @param name
	 * @param fromTableAliasName
	 * @param toTableAliasName
	 */
	public JadOneToManyRelation(CastorRelation relation)
	{
		super(relation.getName(), relation.getCastorRelationChoice().getOneToMany().getFromAlias(), relation.getCastorRelationChoice().getOneToMany().getToAlias());
    this.toKeyName = relation.getCastorRelationChoice().getOneToMany().getToKey();
    
    // handle properties
    if (relation.getPropertyCount() > 0)
      putCastorProperties(relation.getProperty());
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition, de.tif.jacob.core.definition.impl.AbstractElement)
   */
  public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
  {
    super.postProcessing(definition, parent);
    IKey fromKey = getFromTableAlias().getTableDefinition().getPrimaryKey();
    if (null == fromKey)
    {
      throw new RuntimeException("No primary key existing for alias " + getFromTableAlias());
    }
    setFromKey(fromKey);
    setToKey(getToTableAlias().getTableDefinition().getKey(this.toKeyName));
  }
}
