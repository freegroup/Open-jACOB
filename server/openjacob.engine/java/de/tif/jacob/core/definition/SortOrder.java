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

package de.tif.jacob.core.definition;

import de.tif.jacob.core.definition.impl.jad.castor.types.CastorSortOrder;

/**
 * This class declares the different kinds of sort orders.
 * 
 * @see IBrowserTableField#getSortorder()
 * @see IAdhocBrowserDefinition#addBrowserField(int, String, String, String, SortOrder)
 * @see IAdhocBrowserDefinition#addBrowserField(String, String, String, SortOrder)
 * 
 * @author Andreas Sonntag
 */
public final class SortOrder
{
	/**
	 * The internal revision control system id.
	 */
	public static transient final String RCS_ID = "$Id: SortOrder.java,v 1.1 2007/01/19 09:50:37 freegroup Exp $";
	
	/**
	 * The internal revision control system id in short form.
	 */
	public static transient final String RCS_REV = "$Revision: 1.1 $";

	// Anmerkung: Name (NONE, ASCENDING,...) haben eine Reference in reportdefinition.xsd -> bitte nicht ändern!!!!
	//

	/**
   * The instance for indicating that no sort order is used.
   */
	public static final SortOrder NONE = new SortOrder("NONE", null, null);
	
	/**
   * The instance for indicating that ascending sort order is used.
   */
	public static final SortOrder ASCENDING = new SortOrder("ASCENDING", "ASC", CastorSortOrder.ASCENDING);
	
	/**
   * The instance for indicating that descending sort order is used.
   */
	public static final SortOrder DESCENDING = new SortOrder("DESCENDING", "DESC", CastorSortOrder.DESCENDING);

	private final String sqlKeyword;
	private final String name;
	private final CastorSortOrder jacobType;

	/**
   * Internal method for converting a XML JAD type to this class.
   * 
   * @param jadType
   *          the XML JAD type
   * @return the resulting sort order instance
   */
	public static SortOrder fromJad(CastorSortOrder jadType)
	{
		if (jadType == CastorSortOrder.ASCENDING)
			return ASCENDING;
		if (jadType == CastorSortOrder.DESCENDING)
			return DESCENDING;
		return NONE;
	}

  /**
   * Private constructor
   */
	private SortOrder(String name, String sqlKeyword, CastorSortOrder jacobType)
	{
		this.name = name;
		this.sqlKeyword = sqlKeyword;
		this.jacobType = jacobType;
	}

	/**
   * Internal method for determining the corresponding SQL sort order keyword.
   * 
   * @return the SQL sort order keyword or <code>null</code> if unsorted.
   */
	public String getSqlKeyword()
	{
		return this.sqlKeyword;
	}

	/**
   * Internal method for converting this sort order to the corresponding XML JAD type.
   * 
   * @return the XML JAD type
	 */
	public CastorSortOrder toJad()
	{
		return this.jacobType;
	}

  /**
   * Returns a string representation of this sort order, which is easy for a
   * person to read.
   * 
   * @return the string representation of this sort order.
   */
	public String toString()
	{
		return this.name;
	}
	
	/**
   * Parses the hands over string to a valid SortOrder object.
   * 
   * @param orderString
   *          the order string to parse
   * @return The SortOrder object or SortOrder.NONE if no valid mapping
   *         possible.
   */
	public static SortOrder parse(String orderString)
	{
	  if(orderString==null)
	    return NONE;
	  else if(orderString.equalsIgnoreCase(NONE.name))
	    return NONE;
	  else if(orderString.equalsIgnoreCase(ASCENDING.name))
	    return ASCENDING;
	  else if(orderString.equalsIgnoreCase(DESCENDING.name))
	    return DESCENDING;
	  return NONE;
	}
}
