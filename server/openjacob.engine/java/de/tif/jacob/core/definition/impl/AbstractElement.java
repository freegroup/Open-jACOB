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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.tif.jacob.core.definition.INamedObjectDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractElement implements INamedObjectDefinition, Comparable
{
  static public transient final String RCS_ID = "$Id: AbstractElement.java,v 1.6 2010/10/26 23:30:41 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.6 $";
  
  private final String name;
  private final String description;
  private Map properties;
  
  protected AbstractElement(String name, String description)
  {
    this.name = name;
    this.description = description; 
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.INamedObjectDefinition#getDescription()
   */
  public final String getDescription()
  {
    return this.description;
  }
  
  public final Map getProperties()
  {
     return this.properties;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.IDefinition#getProperty(java.lang.String)
   */
  public final String getProperty(String name)
	{
    if (null == name)
      throw new NullPointerException("name is null");
    
    if (this.properties == null)
		{
			return null;
		}
		return (String) this.properties.get(name);
	}

	public final void setProperty(String name, String value)
	{
    if (null == name)
      throw new NullPointerException("name is null");
    
		if (this.properties == null)
		{
			this.properties = new HashMap();
		}
		this.properties.put(name, value);
	}
  
  public CastorProperty[] getCastorProperties()
  {
    if (this.properties == null)
    {
      return new CastorProperty[0];
    }
    CastorProperty[] castorProperties = new CastorProperty[this.properties.size()];
    Iterator iter = this.properties.keySet().iterator();
    int i=0;
    while (iter.hasNext())
    {
      String key = (String) iter.next();
      CastorProperty castorProperty = new CastorProperty();
      castorProperty.setName(key);
      castorProperty.setValue((String) this.properties.get(key));
      castorProperties[i++] = castorProperty;
    }
    return castorProperties;
  }
  
  public void putCastorProperties(CastorProperty[] castorProperties)
  {
    for (int i=0; i< castorProperties.length; i++)
    {
      CastorProperty castorProperty = castorProperties[i];
      setProperty(castorProperty.getName(), castorProperty.getValue());
    }
  }
  
  /**
   * @param definition
   * @param parent
   * @throws Exception
   */
  public abstract void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception;
  
  /**
   * @param definition
   * @param children
   * @throws Exception
   */
  protected final void postProcessing(AbstractDefinition definition, Iterator children) throws Exception
  {
    while (children.hasNext())
    {
      ((AbstractElement) children.next()).postProcessing(definition, this);
    }
  }
  
	/**
	 * @return Returns the name.
	 */
	public final String getName()
	{
		return name;
	}

  public String toString()
  {
    return name;
  }

  public final int compareTo(Object o)
  {
    return getName().compareTo(((AbstractElement) o).getName());
  }
}
