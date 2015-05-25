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

import java.util.Collections;
import java.util.List;

import de.tif.jacob.core.definition.impl.AbstractHtmlGroupDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlGroup;
import de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JadHtmlGroupDefinition extends AbstractHtmlGroupDefinition
{
  static public transient final String RCS_ID = "$Id: JadHtmlGroupDefinition.java,v 1.2 2009/03/18 11:33:52 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";
  
	/**
	 * @param name
	 * @param label
	 * @param tableAlias
	 * @param activeBrowserDefinition
	 * @param dimension
	 */
	public JadHtmlGroupDefinition(CastorHtmlGroup group, JadDefinition definition) throws Exception
	{
		super(group.getName(), group.getLabel(), group.getDescription(),group.getEventHandler(), definition.getTableAlias(group.getAlias()), definition.getBrowserDefinition(group.getBrowser()), 
        new SelectionActionEventHandler[0]);
    
    // handle properties
    if (group.getPropertyCount() > 0)
      putCastorProperties(group.getProperty());
    
	}

  public List getContextMenuEntries()
  {
    return Collections.EMPTY_LIST;
  }

  public List getSelectionActions()
  {
    return Collections.EMPTY_LIST;
  }

  public List getGUIElementDefinitions()
  {
    return Collections.EMPTY_LIST;
  }

  public boolean hasBorder()
  {
    return false;
  }
}
