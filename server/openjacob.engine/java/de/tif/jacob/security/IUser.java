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

package de.tif.jacob.security;

import java.security.GeneralSecurityException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import de.tif.jacob.core.exception.InvalidNewPasswordException;

/**
 * User interface, which has to be implemented by an application developer to
 * provide application specific user information.
 * 
 * @author Andreas Herz
 */
public interface IUser
{
  /**
   * The internal revision control system id.
   */
  static public final String RCS_ID = "$Id: IUser.java,v 1.6 2010/08/19 09:33:22 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final String RCS_REV = "$Revision: 1.6 $";
  
  /**
   * Returns the login id of this user.
   *  
   * @return the login id of this user
   */
  public String getLoginId();

  /**
   * Returns the mandator id of this user.
   *   
   * @return the mandator id of this user if existing, otherwise <code>null</code>
   */
  public String getMandatorId();

  /**
   * The preferred language, i.e. locale, of this user.
   * 
   * @return The preferred locale of this user. 
   */
  public Locale getLocale();
  
  /**
   * The timezone offset of the client to the jACOB server in minutes 
   * 
   * @return The timezone offset of the client to the jACOB server in minutes 
   */
  public int getTimezoneOffset();
  
  /**
   * Sets a property value within the underlying user session.
   * 
   * @param key
   *          the property key
   * @param value
   *          the property value
   */
  public void setProperty(String key, Object value);

  /**
   * Retrieve a property value from the underlying user session.
   * 
   * @param key
   *          the property key
   * @return the property value or <code>null</code>, if no value exists for
   *         this property so far
   */
  public Object getProperty(String key);

  /**
   * Changes the password of this user.
   * 
   * @param password the new password to set
   * @throws InvalidNewPasswordException the password is not valid
   * @throws Exception any other problem
   */
  public void setPassword(String password) throws InvalidNewPasswordException, Exception;
  
  /**
   * Checks whether the given user is a system user or a real user.
   * 
   * @return <code>true</code> if system user, <code>false</code> if real
   *         user.
   */
  public boolean isSystem();
  
  /**
   * Check whether the user is an anonymous user. Anonymous user are used for automatic
   * login into application which supports this mechanism.
   * 
   * @since 2.10
   * @return <code>true</code> if anonymous user, <code>false</code> if known  user.
   */
  public boolean isAnonymous();
  
  /**
   * Returns the unique id of this user.
   * 
   * @return the unique id of the user
   */
  public String getKey();
  
  /**
   * Returns the full name of this user.
   * 
   * @return the fullname of this user
   */
  public String getFullName();

  /**
   * Returns the email address of this user.
   * 
   * @return The email address of the user or <code>null</code> if not existing.
   */
  public String getEMail();
  
  /**
   * Returns the phone number of this user.
   * 
   * @return The phone number of the user or <code>null</code> if not existing.
   */
  public String getPhone();

  /**
   * Returns the cell phone number of this user.
   * 
   * @return The cell phone number of the user or <code>null</code> if not existing.
   */
  public String getCellPhone();
  
  /**
   * Returns the fax number of this user.
   * 
   * @return The fax number of the user or <code>null</code> if not existing.
   */
  public String getFax();

  /**
   * Checks whether this user has the given role.
   * 
   * @param roleName the name of the role to check.
   * 
   * @return <code>true</code> if this user has the given role, otherwise <code>false</code>
   * @throws GeneralSecurityException any security concern
   */
  public boolean hasRole(String roleName) throws GeneralSecurityException;
 
  /**
   * Checks whether this user has the given role.
   * 
   * @param role the role to check.
   * 
   * @return <code>true</code> if this user has the given role, otherwise <code>false</code>
   * @throws GeneralSecurityException any security concern
   * @since 2.7.2
   */
  public boolean hasRole(IRole role) throws GeneralSecurityException;
 
  /**
   * Checks whether this user obtains one of the given roles.
   * 
   * @param roleNames
   *          a list of role names (<code>String</code>)
   * @return <code>true</code> if this user obtains at least one of the given
   *         roles, <code>false</code> otherwise.
   * @throws GeneralSecurityException
   */
  public boolean hasOneRoleOf(List roleNames) throws GeneralSecurityException;
  
  /**
   * Returns the role object for the given role name.
   * 
   * @param roleName the role name
   * @return the desired role object
   * @throws GeneralSecurityException if the role object of this user could not be fetched.
   */
  public IRole getRole(String roleName) throws GeneralSecurityException;
  
  /**
   * Returns all roles of this user.
   *  
   * @return The roles, i.e. <code>Iterator</code> of {@link IRole}.
   * @throws GeneralSecurityException if the roles of this user could not be fetched.
   */
  public Iterator getRoles() throws GeneralSecurityException;
}

