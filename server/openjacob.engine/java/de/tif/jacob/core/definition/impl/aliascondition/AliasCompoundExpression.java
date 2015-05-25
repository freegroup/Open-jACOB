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

package de.tif.jacob.core.definition.impl.aliascondition;

import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.core.definition.IDefinition;
import de.tif.jacob.core.definition.ITableAlias;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AliasCompoundExpression extends AliasExpression
{
  static public transient final String RCS_ID = "$Id: AliasCompoundExpression.java,v 1.1 2007/01/19 09:50:34 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";

  private final List children;

	/**
	 *  
	 */
	public AliasCompoundExpression()
	{
		this.children = new ArrayList();
	}
  
  public void add(AliasExpression expression)
  {
    this.children.add(expression);
  }

  public void add(String expression)
  {
    this.children.add(new AliasSimpleExpression(expression));
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.impl.aliascondition.AliasElement#print(de.tif.jacob.core.definition.impl.aliascondition.AliasConditionResult)
   */
  protected void print(AliasConditionResult result)
  {
    if (isObsolete())
      return;
    
		for (int i = 0; i < this.children.size(); i++)
		{
			((AliasExpression) this.children.get(i)).print(result);
		}
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.alias.AliasExpression#isObsolete()
	 */
	public boolean isObsolete()
	{
    for (int i = 0; i < this.children.size(); i++)
    {
      if (((AliasExpression) this.children.get(i)).isObsolete())
        return true;
    }
    return false;
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.alias.AliasExpression#isDynamic()
	 */
	public boolean isDynamic()
	{
    for (int i = 0; i < this.children.size(); i++)
    {
      if (((AliasExpression) this.children.get(i)).isDynamic())
        return true;
    }
    return false;
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.alias.AliasElement#childrenPostProcessing()
	 */
	protected void childrenPostProcessing(IDefinition definition, ITableAlias alias)
	{
    for (int i = 0; i < this.children.size(); i++)
    {
      ((AliasExpression) this.children.get(i)).childrenPostProcessing(definition, alias);
    }
  }

}
