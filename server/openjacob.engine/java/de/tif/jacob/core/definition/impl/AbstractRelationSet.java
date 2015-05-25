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

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import de.tif.jacob.core.data.impl.qbe.QBERelationGraph;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractRelationSet extends AbstractElement implements IRelationSet
{
  static public transient final String RCS_ID = "$Id: AbstractRelationSet.java,v 1.3 2010/11/12 20:53:54 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.3 $";
  
  private final Set relations;
  private final Set unmodifiableRelations;
  
  public AbstractRelationSet(String name)
  {
    super(name, null);
    // attention: use TreeSet for sorted output of toJacob()!
    this.relations = new TreeSet();
    this.unmodifiableRelations = Collections.unmodifiableSet(this.relations);
  }
  
  public final int hashCode()
  {
    return getName().hashCode();
  }

  public final boolean equals(Object obj)
  {
    if (this == obj)
      return true;

    if (obj == null)
      return false;
    
    if (getClass() != obj.getClass())
      return false;
    
    AbstractRelationSet other = (AbstractRelationSet) obj;
    
    return getName().equals(other.getName());
  }

  protected void addRelation(AbstractRelation relation)
  {
    this.relations.add(relation);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.IRelationSet#getRelations()
   */
  public final Set getRelations()
  {
    return this.unmodifiableRelations;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.IRelationSet#getRelatedTableAliases(de.tif.jacob.core.definition.ITableAlias)
   */
  public Set getRelatedTableAliases(ITableAlias initialTableAlias)
  {
    QBERelationGraph relationGraph = new QBERelationGraph(this, initialTableAlias); 
    return relationGraph.getConnectedTableAliases();
  }

  public void toJacob(CastorRelationset jacobRelationset)
  {
    jacobRelationset.setName(getName());
    
    Iterator iter = this.relations.iterator();
    while(iter.hasNext())
    {
      AbstractRelation relation = (AbstractRelation) iter.next();
      jacobRelationset.addRelation(relation.getName()); 
    }
    
    // handle properties
    jacobRelationset.setProperty(getCastorProperties());
  }
  
}
