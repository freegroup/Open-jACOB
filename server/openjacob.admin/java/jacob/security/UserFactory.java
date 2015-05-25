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
package jacob.security;

import jacob.model.Administrator;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.AuthenticationException;
import de.tif.jacob.core.exception.InvalidUseridPasswordException;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.jacob.security.ISecurityClass;
import de.tif.jacob.security.IUser;
import de.tif.jacob.security.IUserFactory;

/**
 * User factory implementation for jACOB administrators
 * 
 * @author Andreas Herz
 * @author Andreas Sonntag
 */
public class UserFactory extends IUserFactory implements ISecurityClass
{
	static public transient final String RCS_ID = "$Id: UserFactory.java,v 1.1 2007/01/19 07:44:33 freegroup Exp $";
	static public transient final String RCS_REV = "$Revision: 1.1 $";

	/* 
	 * @see de.tif.jacob.security.IUserFactory#findUser(java.lang.String)
	 * @author Andreas Herz
	 */
	public IUser findUser(String id) throws UserNotExistingException, GeneralSecurityException
	{
		return new User(this, id);
	}
  
  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUserFactory#getValid(java.lang.String, java.lang.String)
   */
  public IUser getValid(String userId, String passwd) throws AuthenticationException, GeneralSecurityException
	{
  	try
  	{
  		User user = new User(this, userId);

  		if (user.verifyPasswd(passwd))
  			return user;
  	}
  	catch (UserNotExistingException ex)
  	{
  		// ignore
  	}
  	throw new InvalidUseridPasswordException();
  }
  
  /* 
   * @see de.tif.jacob.security.IUserFactory#findByFullName(java.lang.String)
   */
  public List findByFullName(String fullNamePart) throws Exception
  {
    List result = new ArrayList();
    IDataAccessor dataAccessor = newDataAccessor();
    IDataTable table = dataAccessor.getTable(Administrator.NAME);

    table.qbeSetValue(Administrator.fullname, fullNamePart);
    table.search();
    for (int i = 0; i < table.recordCount(); i++)
    {
      IDataTableRecord userRecord = table.getRecord(i);
      result.add(new User(this, userRecord));
    }
    return result;
  }
}
