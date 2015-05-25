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

package de.tif.jacob.core.definition.impl;

import de.tif.jacob.core.definition.IManyToManyRelation;
import de.tif.jacob.core.definition.IOneToManyRelation;
import de.tif.jacob.core.definition.IRelation;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.impl.jad.castor.CastorManyToMany;
import de.tif.jacob.core.definition.impl.jad.castor.CastorOneToMany;
import de.tif.jacob.core.definition.impl.jad.castor.CastorRelation;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractRelation extends AbstractElement implements IRelation
{
  static public transient final String RCS_ID = "$Id: AbstractRelation.java,v 1.3 2010/11/12 20:53:54 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.3 $";
  
  private final String fromTableAliasName;
  private final String toTableAliasName;
  private ITableAlias fromTableAlias;
  private ITableAlias toTableAlias;
  
	public AbstractRelation(String name, String fromTableAliasName, String toTableAliasName)
	{
		super(name, null);
		this.fromTableAliasName = fromTableAliasName;
		this.toTableAliasName = toTableAliasName;
	}

  public final int hashCode()
  {
    return 31 * getName().hashCode() + 7 * this.fromTableAliasName.hashCode() + this.toTableAliasName.hashCode();
  }

  public final boolean equals(Object obj)
  {
    if (this == obj)
      return true;

    if (obj == null)
      return false;
    
    if (getClass() != obj.getClass())
      return false;
    
    AbstractRelation other = (AbstractRelation) obj;
    
    return getName().equals(other.getName()) && //
        this.fromTableAliasName.equals(other.fromTableAliasName) && //
        this.toTableAliasName.equals(this.toTableAliasName);
  }

  public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
  {
    this.fromTableAlias = definition.getTableAlias(this.fromTableAliasName);
    this.toTableAlias = definition.getTableAlias(this.toTableAliasName);
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IRelation#getFromTableAlias()
	 */
	public final ITableAlias getFromTableAlias()
	{
		return this.fromTableAlias;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IRelation#getToTableAlias()
	 */
	public final ITableAlias getToTableAlias()
	{
		return this.toTableAlias;
	}
  
  protected final void toJacob(CastorRelation jacobRelation)
  {
    jacobRelation.setName(getName());
    if (this instanceof IManyToManyRelation)
    {
      IManyToManyRelation manyToManyRelation = (IManyToManyRelation) this;
      CastorManyToMany jacobManyToManyRelation = new CastorManyToMany();
      jacobManyToManyRelation.setFromRelation(manyToManyRelation.getFromRelation().getName());
      jacobManyToManyRelation.setToRelation(manyToManyRelation.getToRelation().getName());
      jacobRelation.getCastorRelationChoice().setManyToMany(jacobManyToManyRelation);
    }
    else
    {
      IOneToManyRelation oneToManyRelation = (IOneToManyRelation) this;
      CastorOneToMany jacobOneToManyRelation = new CastorOneToMany();
      jacobOneToManyRelation.setFromAlias(oneToManyRelation.getFromTableAlias().getName());
      jacobOneToManyRelation.setToAlias(oneToManyRelation.getToTableAlias().getName());
      jacobOneToManyRelation.setToKey(oneToManyRelation.getToForeignKey().getName());
      jacobRelation.getCastorRelationChoice().setOneToMany(jacobOneToManyRelation);
    }
    
    // handle properties
    jacobRelation.setProperty(getCastorProperties());
  }
  
	/**
	 * @param fromTableAlias The fromTableAlias to set.
	 */
	protected final void setFromTableAlias(ITableAlias fromTableAlias)
	{
		this.fromTableAlias = fromTableAlias;
	}

	/**
	 * @param toTableAlias The toTableAlias to set.
	 */
	protected final void setToTableAlias(ITableAlias toTableAlias)
	{
		this.toTableAlias = toTableAlias;
	}

	/**
	 * @return Returns the fromTableAliasName.
	 */
	public final String getFromTableAliasName()
	{
		return fromTableAliasName;
	}

	/**
	 * @return Returns the toTableAliasName.
	 */
	public final String getToTableAliasName()
	{
		return toTableAliasName;
	}

}
