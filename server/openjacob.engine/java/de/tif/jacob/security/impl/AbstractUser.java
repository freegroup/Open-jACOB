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

package de.tif.jacob.security.impl;

import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.exception.InvalidNewPasswordException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.security.IRole;
import de.tif.jacob.security.ISecurityClass;
import de.tif.jacob.security.IUser;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractUser implements IUser, ISecurityClass
{
  
	static public final String RCS_ID = "$Id: AbstractUser.java,v 1.3 2010/07/02 14:37:25 freegroup Exp $";
	static public final String RCS_REV = "$Revision: 1.3 $";

  private Map properties = Collections.synchronizedMap(new HashMap());
  
  private Locale   myLocal    = null;
	private int      myTimezoneOffset = TimeZone.getDefault().getOffset(System.currentTimeMillis())/-(1000*60);
  private boolean  timezoneset=false;
  
	private Map roles;

	/**
	 *  
	 */
	protected AbstractUser()
	{
		this.roles = null;
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUser#isSystem()
   */
  public final boolean isSystem()
  {
    return false;
  }
  
	public boolean isAnonymous()
  {
     return false;
  }

  /*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.security.IUser#getMandatorId()
	 */
	public String getMandatorId()
	{
		return null;
	}

  public Locale getLocale()
  {
    if (this.myLocal != null)
      return myLocal;
    return Context.getCurrent().getApplicationLocale();
  }
  
  /**
   * Returns the default locale.
   * <p>
   * Attention: This method should be overwritten, if the language of the
   * default resource bundle set <code>applicationLabel.properties</code> is
   * not <code>Locale.ENGLISH</code>.
   * 
   * @return the default locale
   */
  public Locale getDefaultLocale()
  {
    return Locale.ENGLISH;
  }

  public final void setLocale(Locale locale)
  {
    this.myLocal = locale;
  }
  
  public final void setTimezoneOffset(int timezone)
  {
    this.timezoneset=true;
    this.myTimezoneOffset = timezone;
  }
  
  public int getTimezoneOffset()
  {
    return myTimezoneOffset;
  }
  
  public final boolean isTimezoneSet()
  {
    return timezoneset;
  }

  public final boolean isLocaleSet()
  {
    return this.myLocal != null;
  }

  /**
   * Add a property value to the IUser.<br>
   * This method is protected to avoid to use the IUser as a 'container' for runtime properties.<br>
   * Initialize you additional properties in the constructor of your IUser implementation.
   * Store application specific values of the user like address, phone, user weight....<br>
   * 
   * @param key
   * @param value
   */
  public final void setProperty(String key, Object value)
  {
    properties.put(key, value);
  }
  
  
  public final Object getProperty(String key)
  {
    return properties.get(key);
  }

  public void setPassword(String password) throws InvalidNewPasswordException, Exception
  {
    // default implementation
    throw new UserException(new CoreMessage("CHANGE_PASSWORD_NOT_SUPPORTED"));
  }
  
  public String getPhone()
  {
    return null;
  }

  public String getCellPhone()
  {
    return null;
  }
  
  public String getFax()
  {
    return null;
  }

  /**
   * Lacy instantiation of the user roles. The roles are not neccessary for the
   * common usage of this class. At the moment only for the login procedure.
   * 
   * @return <code>Iterator</code> of {@link IRole}
   * @throws GeneralSecurityException
   */
  protected abstract Iterator fetchRoles() throws GeneralSecurityException;

	private void setupRoles() throws GeneralSecurityException
	{
		if (this.roles == null)
		{
			this.roles = new HashMap();
			Iterator iter = fetchRoles();
			while (iter.hasNext())
			{
				IRole role = (IRole) iter.next();
				this.roles.put(role.getName().toLowerCase(), role);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.security.IUser#getRole(java.lang.String)
	 */
	public final synchronized IRole getRole(String role) throws GeneralSecurityException
	{
		setupRoles();
		return (IRole) this.roles.get(role.toLowerCase());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.security.IUser#getRoles()
	 */
	public final synchronized Iterator getRoles() throws GeneralSecurityException
	{
		setupRoles();
		return this.roles.values().iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.security.IUser#hasOneRoleOf(java.util.List)
	 */
	public final synchronized boolean hasOneRoleOf(List roleNames) throws GeneralSecurityException
	{
		setupRoles();
		Iterator iter = roleNames.iterator();
		while (iter.hasNext())
		{
			String roleName = ((String) iter.next()).toLowerCase();

			if (this.roles.containsKey(roleName))
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.security.IUser#hasRole(java.lang.String)
	 */
	public final synchronized boolean hasRole(String role) throws GeneralSecurityException
	{
		setupRoles();
		return this.roles.containsKey(role.toLowerCase());
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUser#hasRole(de.tif.jacob.security.IRole)
   */
  public final boolean hasRole(IRole role) throws GeneralSecurityException
  {
    return hasRole(role.getName());
  }

}
