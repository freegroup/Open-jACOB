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

import de.tif.jacob.core.definition.IManyToManyRelation;
import de.tif.jacob.core.definition.IOneToManyRelation;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.impl.AbstractDefinition;
import de.tif.jacob.core.definition.impl.AbstractElement;
import de.tif.jacob.core.definition.impl.AbstractRelation;
import de.tif.jacob.core.definition.impl.jad.castor.CastorRelation;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JadManyToManyRelation extends AbstractRelation implements IManyToManyRelation
{
  static public transient final String RCS_ID = "$Id: JadManyToManyRelation.java,v 1.1 2007/01/19 09:50:38 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";
  
  private final String fromRelationName;
  private final String toRelationName;
  
  private ITableAlias intermediateTableAlias;
  private JadOneToManyRelation fromRelation;
  private JadOneToManyRelation toRelation;
  
	/**
	 * @param name
	 * @param fromTableAliasName
	 * @param toTableAliasName
	 */
	public JadManyToManyRelation(CastorRelation relation)
	{
		super(relation.getName(), null, null);
    this.fromRelationName = relation.getCastorRelationChoice().getManyToMany().getFromRelation();
    this.toRelationName = relation.getCastorRelationChoice().getManyToMany().getToRelation();
    
    // handle properties
    if (relation.getPropertyCount() > 0)
      putCastorProperties(relation.getProperty());
  }

  public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
  {
    this.toRelation = (JadOneToManyRelation) definition.getRelation(this.toRelationName);
    this.fromRelation = (JadOneToManyRelation) definition.getRelation(this.fromRelationName);
    
    // attention: do not use toRelation.getToTableAlias() since this might not be initialized!!!
    this.intermediateTableAlias = definition.getTableAlias(this.toRelation.getToTableAliasName());
    if (!this.intermediateTableAlias.getName().equals(this.fromRelation.getToTableAliasName()))
    {
    	// plausibility check
      throw new RuntimeException("Inconsistent ManyToManyRelation "+this);
    }
    
    // attention: do not use xyzRelation.getFromTableAlias() since this might not be initialized!!!
    super.setFromTableAlias(definition.getTableAlias(this.fromRelation.getFromTableAliasName()));
    super.setToTableAlias(definition.getTableAlias(this.toRelation.getFromTableAliasName()));
    
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IManyToManyRelation#getFromRelation()
	 */
	public IOneToManyRelation getFromRelation()
	{
		return this.fromRelation;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IManyToManyRelation#getIntermediateTableAlias()
	 */
	public ITableAlias getIntermediateTableAlias()
	{
		return this.intermediateTableAlias;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IManyToManyRelation#getToRelation()
	 */
	public IOneToManyRelation getToRelation()
	{
		return this.toRelation;
	}

}
