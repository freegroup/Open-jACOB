/*******************************************************************************
 *    This file is part of jACOB
 *    Copyright (C) 2005-2009 Tarragon GmbH
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

package de.tif.qes.report.element;

import de.tif.jacob.report.impl.castor.types.CastorLayoutColumnJustificationType;

/**
 * @author Andreas Sonntag
 */
public abstract class QWRFieldJustification
{
  static public final transient String RCS_ID = "$Id: QWRFieldJustification.java,v 1.2 2009-12-07 03:36:09 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  private QWRFieldJustification()
  {
  }

  protected abstract CastorLayoutColumnJustificationType toCastor();
  
  public static final QWRFieldJustification LEFT = new QWRFieldJustification()
  {
    protected CastorLayoutColumnJustificationType toCastor()
    {
      return CastorLayoutColumnJustificationType.LEFT;
    }
  };
  
  public static final QWRFieldJustification RIGHT = new QWRFieldJustification()
  {
    protected CastorLayoutColumnJustificationType toCastor()
    {
      return CastorLayoutColumnJustificationType.RIGHT;
    }
  };
  
  public static final QWRFieldJustification CENTER = new QWRFieldJustification()
  {
    protected CastorLayoutColumnJustificationType toCastor()
    {
      return CastorLayoutColumnJustificationType.CENTER;
    }
  };
}