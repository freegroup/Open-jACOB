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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.definition.DataScope;
import de.tif.jacob.core.definition.impl.AbstractApplicationInfo;
import de.tif.jacob.core.definition.impl.jad.castor.CastorApplication;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationEventHandlerLookupMethodType;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JadApplicationInfo extends AbstractApplicationInfo
{
  static public transient final String RCS_ID = "$Id: JadApplicationInfo.java,v 1.2 2008/06/27 19:39:31 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";
  
  private final List domainNames;
  private final String eventHandler;
  private final boolean lookupByReference;
  private final DataScope dataScope;
  
	/**
	 * @param name
	 * @param title
	 */
	public JadApplicationInfo(CastorApplication application)
	{
		super(application.getName(), application.getTitle(), application.getDescription());
    this.domainNames = new ArrayList(application.getDomainCount());
    this.eventHandler = application.getEventHandler();
    this.dataScope = DataScope.fromJad(application.getDataScope());
    this.lookupByReference = CastorApplicationEventHandlerLookupMethodType.REFERENCE == application.getEventHandlerLookupMethod();
    for (int i=0; i < application.getDomainCount();i++)
    {
    	this.domainNames.add(application.getDomain(i).getContent()); 
    }
	}

	public Iterator getDomainNames()
	{
		return this.domainNames.iterator();
	}

  public String getEventHandler()
  {
    return this.eventHandler;
  }
  
  public DataScope getDataScope()
  {
    return this.dataScope;
  }

  public boolean lookupEventHandlerByReference()
  {
    return this.lookupByReference;
  }
}
