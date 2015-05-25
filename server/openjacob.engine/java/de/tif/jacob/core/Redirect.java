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
package de.tif.jacob.core;

import javax.servlet.http.HttpServletRequest;

/**
 * Class to check whether a redirect has to be performed.
 * 
 * @author Andreas Sonntag
 */
public class Redirect
{
  /**
   * Checks whether a redirect has to be performed.
   * 
   * @param request
   *          the HTTP request
   * @return the URL to redirect or <code>null</code> if no redirection should
   *         be performed
   */
  public static synchronized String getUrl(HttpServletRequest request)
  {
    if ("on".equalsIgnoreCase(Property.BALANCER_SWITCH.getValue()))
    {
      return de.tif.jacob.balancer.Balancer.getRedirectUrl(request);
    }
    return null;
  }
}
