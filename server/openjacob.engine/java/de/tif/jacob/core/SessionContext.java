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

import java.util.Locale;

import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.security.IUser;

/**
 * A Context implementation with session assosiation.
 * 
 */
public abstract class SessionContext extends Context
{
  public SessionContext(IApplicationDefinition appDef, IUser user)
  {
    super(appDef,user);
  }
  
  public abstract Session getSession();

  /**
   * A session context returns always the locale of the corresponding user.
   * 
   */
  public final Locale getLocale()
  {
    return getUser().getLocale();
  }
}
