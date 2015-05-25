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
package de.tif.qes;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.exception.InvalidNewPasswordException;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.jacob.security.impl.AbstractRole;
import de.tif.jacob.security.impl.AbstractUser;

/**
 * User implementation for default Quintus user management.
 *  
 * @author Andreas Sonntag
 */
public class QeSUser extends AbstractUser
{
	/**
	 * The internal revision control system id.
	 */
	public static transient final String RCS_ID = "$Id: QeSUser.java,v 1.1 2006-12-21 11:31:24 sonntag Exp $";
	
	/**
	 * The internal revision control system id in short form.
	 */
	public static transient final String RCS_REV = "$Revision: 1.1 $";

  /**
   * The corresponding user factory.
   */
  protected final QeSUserFactory userFactory;

  protected String loginId;
  protected String fullName;
  protected String key;
  protected String email;
  protected String cellPhone;
  protected String phone;
  protected String fax;

  /**
   * Constructs an user instance by means of the given user data table record.
   * 
   * @param userFactory
   *          the corresponding user factory
   * @param userRecord
   *          the user data table record
   * @throws GeneralSecurityException
   */
  protected QeSUser(QeSUserFactory userFactory, IDataTableRecord userRecord) throws GeneralSecurityException
  {
    this.userFactory = userFactory;
    try
    {
      init(userRecord);
    }
    catch (Exception e)
    {
      throw new GeneralSecurityException(e.getMessage());
    }
  }

  /**
   * Constructs an user instance by means of the given user login id.
   * 
   * @param userFactory
   *          the corresponding user factory
   * @param loginId
   *          the user login id
   * @throws GeneralSecurityException
   * @throws UserNotExistingException
   *           if no such user exits
   */
  protected QeSUser(QeSUserFactory userFactory, String loginId) throws GeneralSecurityException, UserNotExistingException
  {
    this.userFactory = userFactory;
    try
    {
      IDataTable table = userFactory.newDataAccessor().getTable(this.userFactory.getNameOfEmployeeTableAlias());

      table.qbeSetKeyValue(this.userFactory.getNameOfLoginnameTableField(), loginId);

      table.search();
      if (table.recordCount() == 1)
      {
        IDataTableRecord userRecord = table.getRecord(0);
        init(userRecord);
      }
      else
      {
        throw new UserNotExistingException(loginId);
      }
    }
    catch (UserNotExistingException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new GeneralSecurityException(e.getMessage());
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUser#setPassword(java.lang.String)
   */
  public void setPassword(String newPassword) throws InvalidNewPasswordException, Exception
  {
    SQLDataSource dataSource = this.userFactory.getEmployeeSQLDataSource();
    dataSource.changePassword(loginId, newPassword);
  }

  /**
   * 
   * @param userRecord
   * @throws Exception
   */
  protected void init(IDataTableRecord userRecord) throws Exception
  {
    this.key = userRecord.getStringValue(this.userFactory.getNameOfPkeyTableField());
    this.fullName = userRecord.getStringValue(this.userFactory.getNameOfFullnameTableField());
    this.loginId = userRecord.getStringValue(this.userFactory.getNameOfLoginnameTableField());
    if (null != this.userFactory.getNameOfEmailTableField())
      this.email = userRecord.getStringValue(this.userFactory.getNameOfEmailTableField());
    if (null != this.userFactory.getNameOfPhoneTableField())
      this.phone = userRecord.getStringValue(this.userFactory.getNameOfPhoneTableField());
    if (null != this.userFactory.getNameOfMobileTableField())
      this.cellPhone = userRecord.getStringValue(this.userFactory.getNameOfMobileTableField());
    if (null != this.userFactory.getNameOfFaxTableField())
      this.fax = userRecord.getStringValue(this.userFactory.getNameOfFaxTableField());
  }

  /**
   * @return Returns the unique id of the user.
   */
  public String getLoginId()
  {
    return loginId;
  }

  /**
   * @return Returns the employee.fullname of the user
   */
  public String getFullName()
  {
    return fullName;
  }

  /**
   * @return Returns the employee.pkey of the user
   */
  public String getKey()
  {
    return key;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.impl.AbstractUser#fetchRoles()
   */
  protected Iterator fetchRoles() throws GeneralSecurityException
  {
    try
    {
      SQLDataSource dataSource = this.userFactory.getEmployeeSQLDataSource();
      List roleNames = dataSource.getRoles(loginId);

      List roles = new ArrayList(roleNames.size());
      for (int i = 0; i < roleNames.size(); i++)
      {
        roles.add(new Role((String) roleNames.get(i)));
      }
      return roles.iterator();
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

  /*
   * @see de.tif.jacob.security.IUser#getEMail()
   */
  public String getEMail()
  {
    return email;
  }

  public String getPhone()
  {
    return phone;
  }

  public String getCellPhone()
  {
    return cellPhone;
  }

  public String getFax()
  {
    return fax;
  }
  
  protected static class Role extends AbstractRole
  {
    public Role(String roleName)
    {
      super(roleName);
    }
  }
}
