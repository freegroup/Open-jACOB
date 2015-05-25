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

/**
 * This class declares the different types of table keys and their common
 * characteristics, e.g. unique or not, etc.
 * 
 * @see IKey#getType()
 * 
 * @author Andreas Sonntag
 */
public final class KeyType
{
	/**
	 * The internal revision control system id.
	 */
	public static transient final String RCS_ID = "$Id: KeyType.java,v 1.1 2007/01/19 09:50:37 freegroup Exp $";
	
	/**
	 * The internal revision control system id in short form.
	 */
	public static transient final String RCS_REV = "$Revision: 1.1 $";

  /**
   * The key type for primary keys.
   */
  public static final KeyType PRIMARY = new KeyType("PK", true, false);

  /**
   * The key type for foreign keys.
   */
  public static final KeyType FOREIGN = new KeyType("FK", false, false);

  /**
   * The key type for unique indices.
   * <p>
   * Note that indices are abstracted as keys as well.
   */
  public static final KeyType UNIQUE = new KeyType("UK", true, true);

  /**
   * The key type for non-unique indices.
   * <p>
   * Note that indices are abstracted as keys as well.
   */
  public static final KeyType INDEX = new KeyType("IK", false, true);

  private final String prefix;
  private final boolean unique;
  private final boolean index;

  /**
   * Private constructor
   */
  private KeyType(String prefix, boolean unique, boolean index)
  {
    this.prefix = prefix;
    this.unique = unique;
    this.index = index;
  }

  /**
   * Returns the prefix of this key type. The prefix is used to generate key
   * name, i.e. the key name prefix indicates the type of the key.
   * 
   * @return the key type prefix.
   */
  public String getPrefix()
  {
    return prefix;
  }

  /**
   * Checks whether this key type is an index.
   * 
   * @return <code>true</code> if this key type is an index, otherwise
   *         <code>false</code>
   */
  public boolean isIndex()
  {
    return index;
  }

  /**
   * Checks whether this key type is an unique constraint.
   * 
   * @return <code>true</code> if this key type is an unique constraint,
   *         otherwise <code>false</code>
   */
  public boolean isUnique()
  {
    return unique;
  }

  /**
   * Returns a string representation of this key type, which is easy for a
   * person to read.
   * 
   * @return the string representation of this key type.
   */
  public String toString()
  {
    // return prefix which should be ok
    return this.prefix;
  }
}
