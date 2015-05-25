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

import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection;

/**
 * This class declares the different kinds of fill directions. A fill direction is used to
 * determine in which direction(s) a record propagation should be executed.
 * 
 * @see de.tif.jacob.core.data.IDataAccessor#propagateRecord(IDataRecord, Filldirection)
 * @see de.tif.jacob.core.data.IDataAccessor#propagateRecord(IDataRecord, IRelationSet, Filldirection)
 * 
 * @author Andreas Sonntag
 */
public final class Filldirection
{
	/**
	 * The internal revision control system id.
	 */
	public static transient final String RCS_ID = "$Id: Filldirection.java,v 1.1 2007/01/19 09:50:37 freegroup Exp $";
	
	/**
	 * The internal revision control system id in short form.
	 */
	public static transient final String RCS_REV = "$Revision: 1.1 $";

	/**
   * The instance for indicating that no fill direction exists.
   */
  public static final Filldirection NONE = new Filldirection(false, false, CastorFilldirection.NONE);
  
	/**
   * The instance for indicating that either forward and backward should be used as fill directions.
   */
  public static final Filldirection BOTH = new Filldirection(true, true, CastorFilldirection.BOTH);
  
	/**
   * The instance for forward fill direction.
   */
  public static final Filldirection FORWARD = new Filldirection(true, false, CastorFilldirection.FORWARD);
  
	/**
   * The instance for backward fill direction.
   */
  public static final Filldirection BACKWARD = new Filldirection(false, true, CastorFilldirection.BACKWARD);
  
  private final boolean forward;
  private final boolean backward;
  private final CastorFilldirection filldirectionType;
  
	/**
   * Internal method for converting a XML JAD type to this class.
   * 
   * @param jadType
   *          the XML JAD type
   * @return the resulting fill direction instance
   */
  public static Filldirection fromJad(CastorFilldirection jadType)
  {
    if (jadType == CastorFilldirection.BACKWARD)
      return BACKWARD;
    if (jadType == CastorFilldirection.FORWARD)
      return FORWARD;
    if (jadType == CastorFilldirection.BOTH)
      return BOTH;
    return NONE;
  }

  /**
   * Private constructor
   */
	private Filldirection( boolean forward, boolean backward, CastorFilldirection filldirectionType)
	{
    this.forward = forward;
    this.backward = backward;
    this.filldirectionType = filldirectionType;
	}
  
  /**
   * Checks whether this fill direction includes forward direction.
   * 
   * @return <code>true</code> if forward direction is included,
   *         otherwise <code>false</code>
   */
  public boolean isForward()
  {
    return this.forward; 
  }
  
  /**
   * Checks whether this fill direction includes backward direction.
   * 
   * @return <code>true</code> if backward direction is included,
   *         otherwise <code>false</code>
   */
  public boolean isBackward()
  {
    return this.backward; 
  }
  
  /**
   * Parses the hands over string to a valid Filldirection object.
   * 
   * @param direction
   *          the fill direction string to parse
   * @return The Filldirection object or {@link #BACKWARD} if no valid mapping
   *         possible.
   */
  public static Filldirection parseFilldirection(String direction)
  {
    if(direction==null)
      return BACKWARD;
    if(CastorFilldirection.BACKWARD.toString().equalsIgnoreCase(direction))
      return BACKWARD;
    else if(CastorFilldirection.BOTH.toString().equalsIgnoreCase(direction))
      return BOTH;
    else if(CastorFilldirection.FORWARD.toString().equalsIgnoreCase(direction))
      return FORWARD;
    
    return BACKWARD;
  }
  
	/**
   * Returns a string representation of this fill direction, which is easy for a
   * person to read.
   * 
   * @return the string representation of this fill direction.
	 */
	public String toString()
	{
		return this.filldirectionType.toString();
	}
  
	/**
   * Internal method for converting this fill direction to the corresponding XML JAD type.
   * 
   * @return the XML JAD type
	 */
  public CastorFilldirection toJad()
  {
  	return this.filldirectionType;
  }
}
