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

package de.tif.jacob.core.definition.impl;

import java.util.Iterator;

import de.tif.jacob.core.definition.DataScope;
import de.tif.jacob.core.definition.impl.jad.castor.CastorApplication;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationEventHandlerLookupMethodType;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractApplicationInfo extends AbstractElement
{
  static public transient final String RCS_ID = "$Id: AbstractApplicationInfo.java,v 1.2 2008/06/27 19:39:31 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";
  
  private final String title;
  
	/**
	 * 
	 */
	public AbstractApplicationInfo(String name, String title, String description)
	{
    super(name, description);
    this.title = title == null ? name : title;
  }

  /**
   * Returns the class name of the event handler of this application.
   * 
   * @return event handler class name or <code>null</code> if default method
   *         to determine the event handler should be used.
   */
  public abstract String getEventHandler();
  
  public DataScope getDataScope()
  {
    // by default use scope from administration property [dataaccessor.scope]
    return null;
  }
  
  /**
   * Checks whether event handlers should be looked up by reference.
   * 
   * @return <code>true</true> event handlers should be determined by reference, 
   *       if <code>false</code> event handlers are determined by name convention.
   */
  public abstract boolean lookupEventHandlerByReference();
  
  public abstract Iterator getDomainNames();
  
  protected void toJacob(CastorApplication jacobApplication, AbstractDefinition adlDefinition)
  {
    jacobApplication.setName(getName());
    jacobApplication.setTitle(getTitle());
    jacobApplication.setDescription(getDescription());
    jacobApplication.setEventHandler(getEventHandler());
    jacobApplication.setEventHandlerLookupMethod(lookupEventHandlerByReference() ? CastorApplicationEventHandlerLookupMethodType.REFERENCE
        : CastorApplicationEventHandlerLookupMethodType.NAME);
    jacobApplication.setDataScope(getDataScope() == null ? null : getDataScope().toJad());
    
    Iterator iter = getDomainNames();
    while (iter.hasNext())
    {
      CastorDomainRef domainRef = new CastorDomainRef();
      AbstractDomainDefinition domain = adlDefinition.getAbstractDomainDefinition((String) iter.next());
      domainRef.setContent(domain.getName());
      jacobApplication.addDomain(domainRef);
    }
  }
  
  /**
   * @return Returns the title.
   */
  public String getTitle()
  {
    return title;
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition, de.tif.jacob.core.definition.impl.AbstractElement)
	 */
	public final void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
	{
    // nothing to do here
	}

}
