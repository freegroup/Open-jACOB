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
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode;
/**
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 * 
 * @author Andreas Sonntag
 */
public final class ResizeMode
{
  static public final transient String RCS_ID = "$Id: ResizeMode.java,v 1.2 2010/09/20 14:54:26 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  public static final ResizeMode BOOTH = new ResizeMode(CastorResizeMode.BOOTH);
  public static final ResizeMode NONE = new ResizeMode(CastorResizeMode.NONE);
  public static final ResizeMode WIDTH = new ResizeMode(CastorResizeMode.WIDTH);
  public static final ResizeMode HEIGHT = new ResizeMode(CastorResizeMode.HEIGHT);
  private CastorResizeMode jacobType;

  public static ResizeMode fromJacob(CastorResizeMode jacobType)
  {
    if (jacobType == CastorResizeMode.BOOTH)
      return BOOTH;
    if (jacobType == CastorResizeMode.NONE)
      return NONE;
    if (jacobType == CastorResizeMode.WIDTH)
      return WIDTH;
    if (jacobType == CastorResizeMode.HEIGHT)
      return HEIGHT;
    return NONE;
  }

  private ResizeMode(CastorResizeMode jacobType)
  {
    this.jacobType = jacobType;
  }
  
  public String getName()
  {
    return this.jacobType.toString();
  }
}
