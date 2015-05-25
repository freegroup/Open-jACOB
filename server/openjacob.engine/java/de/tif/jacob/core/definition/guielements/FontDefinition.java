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

import de.tif.jacob.core.definition.impl.jad.castor.CastorFont;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontFamilyType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontStyleType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontWeightType;

public class FontDefinition
{
  static public final transient String RCS_ID = "$Id: FontDefinition.java,v 1.1 2007/01/19 09:50:29 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  // Used in the JacobDesigner!!
  public static final String DEFAULT_FONT_FAMILY = new CastorFont().getFamily().toString();
  public static final String DEFAULT_FONT_STYLE  = new CastorFont().getStyle().toString();
  public static final String DEFAULT_FONT_WEIGHT = new CastorFont().getWeight().toString();
  public static final int    DEFAULT_FONT_SIZE = 8;

  
  private final String family;
  private final String style;
  private final String weight;
  private final int   size;
  
  public FontDefinition(String family, String style, String weight, int size)
  {
    this.family = family;
    this.style  = style;
    this.weight = weight;
    this.size   = size;
  }

  public String getFamily()
  {
    return family;
  }

  public int getSize()
  {
    return size;
  }

  public String getStyle()
  {
    return style;
  }

  public String getWeight()
  {
    return weight;
  }
  
  protected CastorFont toJacob()
  {
    CastorFont jacobFont = new CastorFont();
    if (this.family != null)
      jacobFont.setFamily(CastorFontFamilyType.valueOf(this.family));
    jacobFont.setSize(this.size);
    if (this.style != null)
      jacobFont.setStyle(CastorFontStyleType.valueOf(this.style));
    if (this.weight != null)
      jacobFont.setWeight(CastorFontWeightType.valueOf(this.weight));
    return jacobFont;
  }
}
