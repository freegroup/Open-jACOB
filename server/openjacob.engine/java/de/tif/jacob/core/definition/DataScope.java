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

package de.tif.jacob.core.definition;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationDataScopeType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorDomainDataScopeType;

/**
 * This class declares the different kinds of data scopes. A data scope is used to
 * determine whether groups, forms and domains should share data accessor instances or not.
 * 
 * @see IDataAccessor
 * 
 * @author Andreas Sonntag
 * @since 2.7.4
 */
public final class DataScope
{
	/**
	 * The internal revision control system id.
	 */
	public static transient final String RCS_ID = "$Id: DataScope.java,v 1.1 2008/06/27 19:39:31 ibissw Exp $";
	
	/**
	 * The internal revision control system id in short form.
	 */
	public static transient final String RCS_REV = "$Revision: 1.1 $";

	/**
   * The instance for indicating that the whole application should share a single {@link IDataAccessor} instance.
   */
  public static final DataScope APPLICATION = new DataScope(CastorApplicationDataScopeType.APPLICATION, null);
  
	/**
   * The instance for indicating that each domain should use its own {@link IDataAccessor} instance.
   */
  public static final DataScope DOMAIN = new DataScope(CastorApplicationDataScopeType.DOMAIN, CastorDomainDataScopeType.DOMAIN);
  
	/**
   * The instance for indicating that each form should use its own {@link IDataAccessor} instance.
   */
  public static final DataScope FORM = new DataScope(CastorApplicationDataScopeType.FORM, CastorDomainDataScopeType.FORM);
  
  private final CastorApplicationDataScopeType datascopeType;
  private final CastorDomainDataScopeType domainDatascopeType;
  
  /**
   * Internal method for converting a XML JAD type to this class.
   * 
   * @param jadType
   *          the XML JAD type
   * @return the resulting data scope instance or <code>null</code>
   */
  public static DataScope fromJad(CastorApplicationDataScopeType jadType)
  {
    if (jadType == CastorApplicationDataScopeType.APPLICATION)
      return APPLICATION;
    if (jadType == CastorApplicationDataScopeType.DOMAIN)
      return DOMAIN;
    if (jadType == CastorApplicationDataScopeType.FORM)
      return FORM;
    return null;
  }

  /**
   * Internal method for converting a XML JAD type to this class.
   * 
   * @param jadType
   *          the XML JAD type
   * @return the resulting data scope instance or <code>null</code>
   */
  public static DataScope fromJad(CastorDomainDataScopeType jadType)
  {
    if (jadType == CastorDomainDataScopeType.DOMAIN)
      return DOMAIN;
    if (jadType == CastorDomainDataScopeType.FORM)
      return FORM;
    return null;
  }

  /**
   * Private constructor
   */
	private DataScope(CastorApplicationDataScopeType datascopeType, CastorDomainDataScopeType domainDatascopeType)
	{
    this.datascopeType = datascopeType;
    this.domainDatascopeType = domainDatascopeType;
	}
  
  /**
   * Parses the hands over string to a valid data scope object.
   * 
   * @param dataScope
   *          the data scope string to parse
   * @return The data scope object or <code>null</code> if no valid mapping
   *         possible.
   */
  public static DataScope parseDataScope(String dataScope)
  {
    if(dataScope==null)
      return null;
    else if(CastorApplicationDataScopeType.FORM.toString().equalsIgnoreCase(dataScope))
      return FORM;
    else if(CastorApplicationDataScopeType.DOMAIN.toString().equalsIgnoreCase(dataScope))
      return DOMAIN;
    if(CastorApplicationDataScopeType.APPLICATION.toString().equalsIgnoreCase(dataScope))
      return APPLICATION;
    
    return null;
  }
  
	/**
   * Returns a string representation of this data scope, which is easy for a
   * person to read.
   * 
   * @return the string representation of this data scope.
	 */
	public String toString()
	{
		return this.datascopeType.toString();
	}
  
  /**
   * Internal method for converting this data scope to the corresponding XML JAD type.
   * 
   * @return the XML JAD type
   */
  public CastorApplicationDataScopeType toJad()
  {
    return this.datascopeType;
  }
  
  /**
   * Internal method for converting this data scope to the corresponding XML JAD type.
   * 
   * @return the XML JAD type
   */
  public CastorDomainDataScopeType toDomainJad()
  {
    return this.domainDatascopeType;
  }
}
