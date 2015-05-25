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

import de.tif.jacob.core.definition.IManyToManyRelation;
import de.tif.jacob.core.definition.IOneToManyRelation;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.impl.AbstractDefinition;
import de.tif.jacob.core.definition.impl.AbstractElement;
import de.tif.jacob.core.definition.impl.AbstractRelation;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ADLManyToManyRelation extends AbstractRelation implements IManyToManyRelation
{
  static public final transient String RCS_ID = "$Id: ADLManyToManyRelation.java,v 1.1 2006-12-21 11:32:20 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  private final String desc;
  
  private final String intermediateTableAliasName;
  private final String fromRelationName;
  private final String toRelationName;
  
  private ITableAlias intermediateTableAlias;
  private IOneToManyRelation fromRelation;
  private IOneToManyRelation toRelation;
  
	/**
	 * @param name
	 * @param fromTableAlias
	 * @param toTableAlias
	 * @param intermediateTableAlias
	 * @param intermediateFromField
	 * @param intermediateToField
	 */
	public ADLManyToManyRelation(String name, String desc, String fromTableAliasName, String toTableAliasName, String intermediateTableAliasName, String fromRelationName, String toRelationName, String sameas)
	{
	  // IBIS: How to handle sameas?
	  super(name, fromTableAliasName, toTableAliasName);
    
		this.desc = desc;
		this.intermediateTableAliasName = intermediateTableAliasName;
    this.fromRelationName = fromRelationName;
    this.toRelationName = toRelationName;
  }

  public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
  {
    super.postProcessing(definition, parent);
    
    this.intermediateTableAlias = definition.getTableAlias(this.intermediateTableAliasName);
    this.toRelation = (IOneToManyRelation) definition.getRelation(this.toRelationName);
    this.fromRelation = (IOneToManyRelation) definition.getRelation(this.fromRelationName);
  }
  
	/**
	 * @return Returns the intermediateTableAlias.
	 */
	public String getIntermediateTableAliasName()
	{
		return intermediateTableAliasName;
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
