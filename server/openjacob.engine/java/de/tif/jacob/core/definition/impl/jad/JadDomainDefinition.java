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

import de.tif.jacob.core.definition.DataScope;
import de.tif.jacob.core.definition.impl.AbstractDomainDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDomain;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class JadDomainDefinition extends AbstractDomainDefinition
{
  static public transient final String RCS_ID = "$Id: JadDomainDefinition.java,v 1.5 2008/06/27 19:39:31 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.5 $";
  
  private final DataScope dataScope;
  
	/**
	 * @param name
	 */
	public JadDomainDefinition(CastorDomain domain, JadDefinition definition)
  {
    super(domain.getName(), domain.getTitle(), domain.getDescription(), domain.getCanCollapse(), domain.getVisible(), domain.getEventHandler());
    
    // handle properties
    if (domain.getPropertyCount() > 0)
      putCastorProperties(domain.getProperty());
    
    for (int i = 0; i < domain.getFormCount(); i++)
    {
      addForm(definition.getForm(domain.getForm(i)));
    }
    for (int i = 0; i < domain.getFormGroupCount(); i++)
    {
      addFormGroup(new JadFormGroupDefinition(domain.getFormGroup(i), definition));
    }
    for (int i = 0; i < domain.getRoleCount(); i++)
    {
      addRoleName(domain.getRole(i));
    }
    
    this.dataScope = DataScope.fromJad(domain.getDataScope());
  }

  public DataScope getDataScope()
  {
    return this.dataScope;
  }
}
