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

package de.tif.jacob.report.impl.transformer.base.rte;

import java_cup.runtime.Symbol;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class RTESymbol extends Symbol
{
  static public transient final String        RCS_ID = "$Id: RTESymbol.java,v 1.1 2009/12/07 03:36:16 ibissw Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.1 $";
  
  
  /**
   * @param arg0
   * @param arg1
   * @param arg2
   * @param arg3
   */
  public RTESymbol(int arg0, int arg1, int arg2, Object arg3)
  {
    super(arg0, arg1, arg2, arg3);
  }

  /**
   * @param arg0
   * @param arg1
   */
  public RTESymbol(int arg0, Object arg1)
  {
    super(arg0, arg1);
  }

  /**
   * @param arg0
   * @param arg1
   * @param arg2
   */
  public RTESymbol(int arg0, int arg1, int arg2)
  {
    super(arg0, arg1, arg2);
  }

  /**
   * @param arg0
   */
  public RTESymbol(int arg0)
  {
    super(arg0);
  }

  public String toString()
  {
    return "Symbol#" + sym + " at " + left+":"+right+" value '"+value+"'";
  }
  
}
