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
package de.tif.jacob.core.definition.impl.jad;

import java.util.List;
import de.tif.jacob.core.definition.guielements.Dimension;
import de.tif.jacob.core.definition.guielements.StackContainerDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode;

/**
 *
 * @since 2.8.3
 **/
public class JadStackContainerDefinition extends StackContainerDefinition
{
  static public transient final String RCS_ID = "$Id: JadStackContainerDefinition.java,v 1.3 2010/08/12 07:52:28 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.3 $";
  
  /**
   * @param name
   * @param description
   * @param eventHandler
   * @param visible
   * @param position
   * @param panes
   * @throws Exception
   */
  protected JadStackContainerDefinition(String name, String description, String eventHandler, boolean visible, Dimension position, List panes, int borderWith,  String borderColor, String backgroundColor, CastorResizeMode castorResizeMode) throws Exception
  {
    super(name, description, eventHandler, visible, position, panes, borderWith, borderColor, backgroundColor, castorResizeMode);
  }
}
