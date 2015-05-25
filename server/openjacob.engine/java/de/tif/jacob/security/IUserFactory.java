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
import java.util.List;

import de.tif.jacob.cluster.ClusterManager;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.impl.DataAccessor;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.exception.AuthenticationException;
import de.tif.jacob.core.exception.UserNotExistingException;

/**
 * Abstract user factory class, which has to be implemented (i.e. extended) by
 * an application developer to provide application specific concerns regarding
 * authentication and authorization.
 * 
 * @author Andreas Herz
 */
public abstract class IUserFactory
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: IUserFactory.java,v 1.5 2010/08/19 09:33:22 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.5 $";
  
  private IApplicationDefinition applicationDefinition;
  
  /**
   * Returns the user object to the given user login id.<br>
   * 
   * @param userLoginId
   *          The user login ID of the user
   * @return The user which matches to the given user login ID
   * @throws UserNotExistingException
   *           will be thrown if no such user exists
   * @throws GeneralSecurityException
   *           on any other security related problem
   * 
   * @see IUser#getLoginId()
   */
  public abstract IUser findUser(String userLoginId) throws UserNotExistingException, GeneralSecurityException;
  
  /**
   * Searchs all users which will match to the given fullname part.<p>
   * Example: If 'Müller' is given, you will retrieve:<br>
   *  Martin Müller<br>
   *  A. Müller<br>
   *  Müller, Gert<br>
   *  .
   *  .
   * 
   * @param fullNamePart A part of the fullname of a user
   * @return <code>List</code> of {@link IUser} of all matching users.
   * @throws Exception on any problem
   */
  public abstract List findByFullName(String fullNamePart) throws Exception;
  
  /**
   * Returns a valid user if the application supports anonymous login.<br>
   * 
   * @return The application anonymous user or <code>null</code>, if the application doesn't support anynymous login
   * @throws GeneralSecurityException
   *           any other security concern
   * @since 2.10
   * 
   */
  public IUser getAnonymous() throws  GeneralSecurityException
  {
    return null;
  }
    
  /**
   * Returns the corresponding user with the user login id if the password to
   * the user matches.<br>
   * 
   * @param userLoginId
   *          The user login id of the user
   * @param passwd
   *          The corresponding password of the user
   * @return The valid user object to the user login id
   * @throws AuthenticationException
   *           if login id and/or password are not valid
   * @throws GeneralSecurityException
   *           any other security concern
   * 
   * @see IUser#getLoginId()
   */
  public abstract IUser getValid(String userLoginId, String passwd) throws AuthenticationException, GeneralSecurityException;
 
  /**
	 * Returns the application definition bound to this user factory
	 * 
	 * @return the application definition bound.
	 */
	public final IApplicationDefinition getApplicationDefinition()
	{
		return applicationDefinition;
	}

  /**
   * Creates a new data accessor for accessing data of the application bound to
   * this user factory
   * 
   * @return the created data accessor
   */
  public final IDataAccessor newDataAccessor()
  {
    return new DataAccessor(getApplicationDefinition());
  }
  
  /**
   * Terminate all user sessions of the given user id.
   * 
   * @param userLoginId
   *          The user login ID of the user
   * @since 2.7.2
   */
  public final void terminateUserSessions(String userLoginId)
  {
    try
    {
      ClusterManager.getProvider().notifyTerminateUserSessions(getApplicationDefinition().getName(), userLoginId);
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }
  
	/**
	 * Sets the application definition bound to this user factory.
	 * <p>
	 * Note: For internal use only!
	 * 
	 * @param applicationDefinition
	 *          application definition to set.
	 */
	final void setApplicationDefinition(IApplicationDefinition applicationDefinition)
	{
		this.applicationDefinition = applicationDefinition;
	}

}
