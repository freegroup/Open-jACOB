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

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class QBEWildcardExpression extends QBEExpression
{
  static public transient final String RCS_ID = "$Id: QBEWildcardExpression.java,v 1.2 2008/01/24 14:38:20 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";
  
  private final LinkedList parts = new LinkedList();
  private final List readonlyParts = Collections.unmodifiableList(parts);
  private boolean isAnchored;

  public QBEWildcardExpression(String textFragment)
  {
    super();
    this.parts.add(textFragment);
  }

  public QBEWildcardExpression(String textFragment, QBEWildcard wildcard)
  {
    super();
    this.parts.add(textFragment);
    this.parts.add(wildcard);
  }

  public QBEWildcardExpression(QBEWildcard wildcard, String textFragment)
  {
    super();
    this.parts.add(wildcard);
    this.parts.add(textFragment);
  }
  
  public QBEWildcardExpression(QBEWildcard wildcard, String textFragment, QBEWildcard wildcard2)
  {
    super();
    this.parts.add(wildcard);
    this.parts.add(textFragment);
    this.parts.add(wildcard2);
  }
  
  /**
   * @return <code>true</code> if no wildcards at begin and no wildcards at
   *         end of wildcard expression (e.g. "Frank*MyCompany"), otherwise
   *         <code>false</code>
   */
  public boolean isUnbound()
  {
    return !isAnchored && parts.get(0) instanceof String && parts.get(parts.size() - 1) instanceof String;
  }
  
  public void setAnchored()
  {
    this.isAnchored = true;
  }
  
  public List getParts()
  {
    return this.readonlyParts;
  }

//  public QBEWildcardExpression()
//  {
//    super();
//    this.parts.add(QBEWildcard.MULTIPLE);
//  }

  public void addFirst(String textFragment)
	{
		this.parts.addFirst(textFragment);
	}

	public void addFirst(QBEWildcard wildcard)
	{
		this.parts.addFirst(wildcard);
	}

	public void addLast(String textFragment)
	{
		this.parts.addLast(textFragment);
	}

	public void addLast(QBEWildcard wildcard)
	{
		this.parts.addLast(wildcard);
	}

	public void print(PrintWriter writer)
	{
		Iterator iter = this.parts.iterator();
		while (iter.hasNext())
		{
			writer.print(iter.next());
		}
	}

  public void makeConstraint(QBEConstraintBuilder builder, boolean doNot) throws InvalidExpressionException
  {
    builder.appendWildcardExpression(this, doNot);
  }
}
