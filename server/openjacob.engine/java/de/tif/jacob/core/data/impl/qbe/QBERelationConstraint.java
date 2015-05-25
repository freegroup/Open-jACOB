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

package de.tif.jacob.core.data.impl.qbe;

import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.IOneToManyRelation;
import de.tif.jacob.core.definition.ITableAlias;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class QBERelationConstraint
{
  static public transient final String        RCS_ID = "$Id: QBERelationConstraint.java,v 1.4 2009/08/21 13:49:19 ibissw Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.4 $";
  
  private boolean enforceInnerJoin = false;
  private final IOneToManyRelation relation;
  private boolean forwardFlag = false;
  private QBERelationConstraint previousBackwardRelationConstraint = null;
  
  protected QBERelationConstraint(IOneToManyRelation relation)
  {
    this.relation = relation;
  }
  
	/**
	 * @return
	 */
	public IKey getFromPrimaryKey()
	{
    return this.relation.getFromPrimaryKey();
	}

	/**
	 * @return Returns the fromTableAliasName.
	 */
	public ITableAlias getFromTableAlias()
	{
		return this.relation.getFromTableAlias();
	}

	/**
	 * @return
	 */
	public IKey getToForeignKey()
	{
    return this.relation.getToForeignKey();
  }

	/**
	 * @return Returns the toTableAliasName.
	 */
	public ITableAlias getToTableAlias()
	{
		return this.relation.getToTableAlias();
	}

	/**
	 * toString methode: creates a String representation of the object
	 * @return the String representation
	 * @author info.vancauwenberge.tostring plugin
	
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(getToForeignKey());
		buffer.append("->");
    buffer.append(getFromPrimaryKey());
    return buffer.toString();
	}
	
	/**
	 * @return Returns the innerJoin.
	 */
	public boolean isInnerJoin()
	{
	  if (this.forwardFlag)
	    return false;
	  
    // if the relation is mandatory and a backward relation -> always perform an inner join
		return enforceInnerJoin || isMandatory();
	}
  
  private boolean isMandatory()
  {
    if (relation.isMandatory())
    {
      // Browser-Search-Problem: Wenn wir schon einmal einen Outer-Join gemacht
      // haben, so müssen wir weitere Outer-Joins machen, da wir sonst zu wenige
      // Records zurückbekommen.
      // Beispiel:
      // Mitarbeiter 0..n----0..1 Manager 0..n-----1 Kostenstelle
      // Browser = Mitarbeiter.Fullname, Manager.Fullname, Kostenstelle.nr
      // würde nur die Mitarbeiter zurückliefern, welche einen Manager haben
      //
      if (this.previousBackwardRelationConstraint != null)
        return this.previousBackwardRelationConstraint.isMandatory();
      
      return true;
    }
    return false;
  }
  
  protected void setPreviousBackwardRelation(QBERelationConstraint previousBackwardRelationConstraint)
  {
    this.previousBackwardRelationConstraint = previousBackwardRelationConstraint;
  }

	/**
	 * @param innerJoin The innerJoin to set.
	 */
	protected void setEnforceInnerJoin()
	{
		this.enforceInnerJoin = true;
	}

	/**
	 * @return Returns the forwardFlag.
	 */
	public boolean isForwardFlag()
	{
		return forwardFlag;
	}

	/**
	 */
	protected void setForwardFlag()
	{
		this.forwardFlag = true;
	}

}
