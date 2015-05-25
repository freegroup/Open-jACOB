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

package de.tif.qes.adl.element;

import java.util.List;

import de.tif.jacob.core.definition.fieldtypes.EnumerationFieldType;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ADLEnum extends ADLFieldType
{
  static public final transient String RCS_ID = "$Id: ADLEnum.java,v 1.2 2009-02-03 10:23:27 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  public ADLEnum(List values, String desc, String def, Boolean required)
  {
    super(new EnumerationFieldType(values,values, def), required);
  }

}
