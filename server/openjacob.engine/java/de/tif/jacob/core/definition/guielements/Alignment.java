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

package de.tif.jacob.core.definition.guielements;

import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorVerticalAlignment;


/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * @author Andreas Sonntag
 */
public final class Alignment
{
  static public final transient String RCS_ID = "$Id: Alignment.java,v 1.3 2009/02/20 19:37:21 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  public static final Vertical TOP = new Vertical(CastorVerticalAlignment.TOP);
  public static final Vertical MIDDLE = new Vertical(CastorVerticalAlignment.MIDDLE);
  public static final Vertical BOTTOM = new Vertical(CastorVerticalAlignment.BOTTOM);
  
  public static final Horizontal LEFT    = new Horizontal(CastorHorizontalAlignment.LEFT);
  public static final Horizontal CENTER  = new Horizontal(CastorHorizontalAlignment.CENTER);
  public static final Horizontal RIGHT   = new Horizontal(CastorHorizontalAlignment.RIGHT);
  
  public static Horizontal fromJacob(CastorHorizontalAlignment jacobType)
  {
    if (jacobType == CastorHorizontalAlignment.LEFT)
      return LEFT;
    if (jacobType == CastorHorizontalAlignment.CENTER)
      return CENTER;
    if (jacobType == CastorHorizontalAlignment.RIGHT)
      return RIGHT;
    
    // default needed for old JAD designs
    return RIGHT;
  }

  public static Vertical fromJacob(CastorVerticalAlignment jacobType)
  {
    if (jacobType == CastorVerticalAlignment.TOP)
      return TOP;
    if (jacobType == CastorVerticalAlignment.MIDDLE)
      return MIDDLE;
    if (jacobType == CastorVerticalAlignment.BOTTOM)
      return BOTTOM;
    
    // default needed for old JAD designs
    return TOP;
  }

  private Alignment()
  {
    // avoid initialization
  }
  
  public static class Vertical
  {
    private final CastorVerticalAlignment jacobType;
    
    private Vertical(CastorVerticalAlignment jacobType)
    {
      this.jacobType = jacobType;
    }
    
    public CastorVerticalAlignment toJacob()
    {
    	return this.jacobType;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      // return html value as string
      return this.jacobType==null?null:this.jacobType.toString();
    }
  }
  
  public static class Horizontal
  {
    private final CastorHorizontalAlignment jacobType;
    
    private Horizontal(CastorHorizontalAlignment jacobType)
    {
      this.jacobType = jacobType;
    }
    
    public CastorHorizontalAlignment toJacob()
    {
    	return this.jacobType;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      // return html value as string
      return this.jacobType==null?null:this.jacobType.toString();
    }
  }
}
