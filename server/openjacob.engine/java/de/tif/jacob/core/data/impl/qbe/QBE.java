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

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class QBE
{
  static public transient final String RCS_ID = "$Id: QBE.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
	static public transient final String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Checks whether the given expression is a so-called null-expression, i.e.
	 * the expression is "NULL" or "!NULL". White space will be ignored.
	 * 
	 * @param expression
	 *          expression to check
	 * @return <code>true</code> if expression is a null-expression otherwise
	 *         <code>false</code>
	 */
	public static boolean isNullExpression(String expression)
	{
		// for performance reasons check in advance
		if (null != expression && expression.length() > 0)
		{
			try
			{
				QBEExpression expr = QBEExpression.parse(QBEScanner.NULL_CHECK, expression);
				return expr != null;
			}
			catch (Exception e)
			{
				// ignore
			}
		}
		return false;
	}
}
