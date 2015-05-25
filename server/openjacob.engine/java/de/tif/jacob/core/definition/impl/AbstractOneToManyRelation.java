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

import java.util.List;

import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.IOneToManyRelation;
import de.tif.jacob.core.definition.ITableField;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to Window>Preferences>Java>Code Generation>Code and
 * Comments
 */
public abstract class AbstractOneToManyRelation extends AbstractRelation implements IOneToManyRelation
{
  private IKey fromPrimaryKey;
  private IKey toForeignKey;
  private boolean mandatory;

  /**
   * @param name
   * @param fromTableAliasName
   * @param toTableAliasName
   */
  protected AbstractOneToManyRelation(String name, String fromTableAliasName, String toTableAliasName)
  {
    super(name, fromTableAliasName, toTableAliasName);
  }

  /**
   * Calculates whether this relation is mandatory, i.e. all fields of the foreign key must be required.
   * 
   * @return <code>true</code> if mandatory, otherwise <code>false</code>
   */
  private boolean calculateMandatory()
  {
    List fields = this.toForeignKey.getTableFields();
    for (int i = 0; i < fields.size(); i++)
    {
      if (!((ITableField) fields.get(i)).isRequired())
        return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IOneToManyRelation#getFromKey()
   */
  public final IKey getFromPrimaryKey()
  {
    return this.fromPrimaryKey;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IOneToManyRelation#getToKey()
   */
  public final IKey getToForeignKey()
  {
    return this.toForeignKey;
  }

  /**
   * @param fromKey
   *          The fromKey to set.
   */
  protected final void setFromKey(IKey fromKey)
  {
    this.fromPrimaryKey = fromKey;
  }

  /**
   * @param toKey
   *          The toKey to set.
   */
  protected final void setToKey(IKey toKey)
  {
    this.toForeignKey = toKey;
    ((AbstractKey) toKey).setForeignKeyRelation(this);

    this.mandatory = calculateMandatory();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IOneToManyRelation#isMandatory()
   */
  public boolean isMandatory()
  {
    return this.mandatory;
  }
}
