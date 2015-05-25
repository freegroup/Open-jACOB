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

import de.tif.jacob.core.definition.guielements.Dimension;
import de.tif.jacob.core.definition.impl.AbstractGroupContainerDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGroup;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer;

/**
 * @author Andreas Herz
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JadGroupContainerDefinition extends AbstractGroupContainerDefinition
{
  static public transient final String RCS_ID = "$Id: JadGroupContainerDefinition.java,v 1.1 2008/11/22 12:05:28 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";
  
	/**
	 * @param name
	 * @param label
	 */
	public JadGroupContainerDefinition(CastorGroupContainer container, JadDefinition definition) throws Exception
	{
		super(container.getName(), container.getDescription(), container.getEventHandler(), new Dimension(container.getDimension()));
    
    // handle properties
    if (container.getPropertyCount() > 0)
      putCastorProperties(container.getProperty());
    
    for(int i=0; i<container.getGroupCount();i++)
    {
      CastorGroup group = container.getGroup(i);
      addGroup(new JadGroupDefinition(group, definition));
    }
  }

}
