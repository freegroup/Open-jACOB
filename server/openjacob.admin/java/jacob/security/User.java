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
import java.security.MessageDigest;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.InvalidNewPasswordException;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.jacob.security.impl.AbstractUser;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas Herz / Mike Doering
 *  
 */
public class User extends AbstractUser
{
	static public transient final String RCS_ID = "$Id: User.java,v 1.1 2007/01/19 07:44:33 freegroup Exp $";
	static public transient final String RCS_REV = "$Revision: 1.1 $";

  private final UserFactory factory;
	private String loginId;
	private String fullName;
	private String key;
	private String email;
	private String hashvalue;

	protected User(UserFactory factory, String id) throws UserNotExistingException
	{
    this.factory = factory;
		IDataAccessor dataAccessor = this.factory.newDataAccessor();
		IDataTable table = dataAccessor.getTable(Administrator.NAME);

		try
		{
			// use exact match operator for login name
			table.qbeSetKeyValue(Administrator.loginid, id);

			table.search();
			if (table.recordCount() == 1)
			{
				IDataTableRecord userRecord = table.getRecord(0);
        init(userRecord);
			}
			else if (table.recordCount() > 1)
			{
			  // should never occur because of unique index
				throw new RuntimeException("LoginId '" + id + "' is not unique");
			}
			else
			{
				throw new UserNotExistingException(id);
			}
		}
		catch (UserNotExistingException e)
		{
			throw e;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

  protected User(UserFactory factory, IDataTableRecord userRecord) throws Exception
  {
    this.factory = factory;
    init(userRecord);
  }

  /**
   * Internal initialization method to fetch user attributes from user record.
   * 
   * @param userRecord
   * @throws Exception
   */
  private void init(IDataTableRecord userRecord) throws Exception
  {
    this.loginId = userRecord.getStringValue(Administrator.loginid);
		this.key = userRecord.getStringValue(Administrator.id);
		this.fullName = userRecord.getStringValue(Administrator.fullname);
		this.email = userRecord.getStringValue(Administrator.email);
		this.hashvalue = userRecord.getStringValue(Administrator.passwdhash);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUser#getLocale()
   */
  public Locale getLocale()
  {
    // get browser language (if existing) and check for englisch.
    // Note: This is for allowing US number/data format!
    //
    Locale httpLocale = super.getLocale();
    if (httpLocale != null && "en".equals(httpLocale.getLanguage()))
      return httpLocale;
    
    // IBIS: Change this if other languages are supported
    return Locale.ENGLISH;
  }
  
	/**
	 * @return Returns the unique id of the user.
	 */
	public String getLoginId()
	{
		return this.loginId;
	}

	/**
	 * @return Returns the employee.fullname of the user
	 */
	public String getFullName()
	{
		return this.fullName;
	}

	/**
	 * @return Returns the employee.pkey of the user
	 */
	public String getKey()
	{
		return this.key;
	}

	/**
	 * @return Returns the passwd.
	 */
	public boolean verifyPasswd(String password)
	{
		if (null == password || password.length() == 0)
			return this.hashvalue == null;

		return createHashcode(password).equals(this.hashvalue);
	}

	/* (non-Javadoc)
   * @see de.tif.jacob.security.impl.AbstractUser#setPassword(java.lang.String)
   */
  public void setPassword(String newPassword) throws InvalidNewPasswordException, Exception
  {
    // fetch user record
    //
    IDataAccessor accessor = this.factory.newDataAccessor();
    IDataTable table = accessor.getTable(Administrator.NAME);
    table.qbeSetKeyValue(Administrator.id, this.key);
    table.search();
    if (table.recordCount() != 1)
    {
      throw new Exception("Cannot find administrator with id " + this.key);
    }

    // determine password hash
    String passwdhash = createHashcode(newPassword);

    // store new password hash
    //
    IDataTransaction trans = accessor.newTransaction();
    try
    {
      table.getRecord(0).setValue(trans, Administrator.passwdhash, passwdhash);
      trans.commit();
    }
    finally
    {
      trans.close();
    }

    // set new password hash after success
    this.hashvalue = passwdhash;
  }

  /*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.security.IUser#getEMail()
	 */
	public String getEMail()
	{
		return this.email;
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

	public static String createHashcode(String password)
	{
		if (password == null || password.length() == 0)
			return null;

		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			return StringUtil.toHexString(md.digest(password.getBytes()));
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.security.impl.AbstractUser#fetchRoles()
   */
  protected Iterator fetchRoles() throws GeneralSecurityException
  {
		return Collections.EMPTY_SET.iterator();
  }
}
