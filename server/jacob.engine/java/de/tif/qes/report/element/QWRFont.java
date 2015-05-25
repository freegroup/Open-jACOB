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

import de.tif.jacob.report.impl.castor.CastorFont;


/**
 * @author Andreas Sonntag
 */
public final class QWRFont
{
  static public final transient String RCS_ID = "$Id: QWRFont.java,v 1.2 2009-12-07 03:36:09 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  private final String fontName;
  private final String str1;
  private final Integer size;
  
  /**
   * Might be <code>null</code>
   */
  private final String color;
  
  /**
   * @param fontName like "Courier New"
   * @param str1 like "R". What is the meaning?
   * @param size like +3. What is the meaning?
   * @param color like "#800000".
   */
  public QWRFont(String fontName, String str1, Integer size, String color)
  {
    this.fontName = fontName;
    this.str1 = str1;
    this.size = size;
    this.color = color;
  }
	
  /**
   * @return the color
   */
  protected String getColor()
  {
    // TODO: Check to remove #
    return color;
  }

  protected CastorFont toCastor()
  {
    CastorFont castor = new CastorFont();
    castor.setFamily(this.fontName);
    return castor;
  }
}
