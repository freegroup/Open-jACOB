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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.tif.jacob.core.exception.AuthenticationException;

/**
 * Interface to implement/bind to a 3rd party user management. You can read the
 * client cookies and redirect them to an other AppServer for authenticate the
 * user.
 */
public interface HttpAuthentificator
{
  /**
   * Performs the user authentication by means of the given HTTP request and response.
   * 
   * @param request
   *          the HTTP request
   * @param response
   *          the HTTP response
   * @return The valid user object, if it is possible to determine them, or
   *         <code>null</code>, if authentication should be redirected to the
   *         login page.
   * @throws AuthenticationException
   *           if authentication fails
   * @throws GeneralSecurityException
   *           any other security concern
   */
  public IUser get(HttpServletRequest request, HttpServletResponse response) throws GeneralSecurityException, AuthenticationException;
}
