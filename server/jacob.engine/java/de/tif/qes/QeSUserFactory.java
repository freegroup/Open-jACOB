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
import java.util.List;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.exception.AuthenticationException;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.InvalidUseridPasswordException;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.jacob.security.IUser;
import de.tif.jacob.security.IUserFactory;

/**
 * User factory implementation for default Quintus user management.
 *  
 * @author Andreas Sonntag
 */
public abstract class QeSUserFactory extends IUserFactory
{
	/**
	 * The internal revision control system id.
	 */
	public static transient final String RCS_ID = "$Id: QeSUserFactory.java,v 1.1 2006-12-21 11:31:24 sonntag Exp $";
	
	/**
	 * The internal revision control system id in short form.
	 */
	public static transient final String RCS_REV = "$Revision: 1.1 $";

  protected IUser newUser(QeSUserFactory userFactory, IDataTableRecord userRecord) throws GeneralSecurityException
  {
    return new QeSUser(userFactory, userRecord);
  }
  
  protected IUser newUser(QeSUserFactory userFactory, String loginId) throws GeneralSecurityException, UserNotExistingException
  {
    return new QeSUser(userFactory, loginId);
  }
  
  /** 
	 * @see de.tif.jacob.security.IUserFactory#findUser(java.lang.String)
	 */
	public IUser findUser(String loginId) throws UserNotExistingException, GeneralSecurityException
	{
		return newUser(this, loginId);
	}
  	
  /**
   * @see de.tif.jacob.security.IUserFactory#getValid(java.lang.String, java.lang.String)
   */
  public IUser getValid(String loginId, String passwd) throws AuthenticationException, GeneralSecurityException
  {
  	try
    {
  	  // try to connect to the JDBC datasource with the user & password
  	  // If we have success, the user/password combination is valid
  	  //
  	  SQLDataSource dataSource = getEmployeeSQLDataSource();
  	  
  	  // check the user and password against the ORACLE user...
    	dataSource.getConnection(loginId, passwd).close();
    	
    	// ...on ok return the corresponding caretaker user
    	return findUser(loginId); 
    }
  	catch (UserNotExistingException exc)
  	{
      throw new InvalidUseridPasswordException();
  	}
  	catch (AuthenticationException exc)
  	{
  	  throw exc;
  	}
    catch (Exception exc )
    {
      throw new GeneralSecurityException(exc.getClass().getName()+":"+exc.getMessage());
    }
  }
  
  /**
   * @see de.tif.jacob.security.IUserFactory#findByFullName(java.lang.String)
   */
  public List findByFullName(String fullNamePart) throws GeneralSecurityException
  {
    List result = new ArrayList();
    
		IDataAccessor dataAccessor = newDataAccessor();
		IDataTable table = dataAccessor.getTable(getNameOfEmployeeTableAlias());

    try
    {
    	// Daimler ist case insensitive
    	table.qbeSetValue(getNameOfFullnameTableField(),  fullNamePart);
    	
    	table.search();
      for(int i=0;i<table.recordCount();i++)
      {
          IDataTableRecord userRecord = table.getRecord(i);
      		result.add(newUser(this, userRecord));
      }
    }
    catch (InvalidExpressionException e)
    {
      throw new GeneralSecurityException(e.getMessage());
    }
    catch (IndexOutOfBoundsException e)
    {
      throw new GeneralSecurityException(e.getMessage());
    }
    catch (NoSuchFieldException e)
    {
      throw new GeneralSecurityException(e.getMessage());
    }
    return result;
  }
  
  public SQLDataSource getEmployeeSQLDataSource()
  {
    // get datasource of employee table and assume that it is a SQL datasource!
    //
    return (SQLDataSource) DataSource.get(getApplicationDefinition().getTableAlias(getNameOfEmployeeTableAlias()).getTableDefinition().getDataSourceName());
  }
  
  /**
   * Returns the name of the employee table alias, which is by default
   * "employee".
   * <p>
   * Note that sub classes should override this method if required.
   * 
   * @return employee table alias name
   */
  public String getNameOfEmployeeTableAlias()
  {
    return "employee";
  }
  
  /**
   * Returns the name of the fullname table field, which is by default
   * "fullname".
   * <p>
   * Note that sub classes should override this method if required.
   * 
   * @return the fullname table field name
   */
  public String getNameOfFullnameTableField()
  {
    return "fullname";
  }

  /**
   * Returns the name of the loginname table field, which is by default
   * "loginname".
   * <p>
   * Note that sub classes should override this method if required.
   * 
   * @return the loginname table field name
   */
  public String getNameOfLoginnameTableField()
  {
    return "loginname";
  }

  /**
   * Returns the name of the pkey table field, which is by default
   * "pkey".
   * <p>
   * Note that sub classes should override this method if required.
   * 
   * @return the pkey table field name
   */
  public String getNameOfPkeyTableField()
  {
    return "pkey";
  }

  /**
   * Returns the name of the email table field, which is by default
   * "email".
   * <p>
   * Note that sub classes should override this method if required.
   * 
   * @return the email table field name or <code>null</code> if not existing
   */
  public String getNameOfEmailTableField()
  {
    return "email";
  }

  /**
   * Returns the name of the phone table field, which is by default
   * "phone".
   * <p>
   * Note that sub classes should override this method if required.
   * 
   * @return the phone table field name or <code>null</code> if not existing
   */
  public String getNameOfPhoneTableField()
  {
    return "phone";
  }

  /**
   * Returns the name of the mobile table field, which is by default
   * <code>null</code>.
   * <p>
   * Note that sub classes should override this method if required.
   * 
   * @return the mobile table field name or <code>null</code> if not existing
   */
  public String getNameOfMobileTableField()
  {
    return null;
  }

  /**
   * Returns the name of the fax table field, which is by default
   * <code>null</code>.
   * <p>
   * Note that sub classes should override this method if required.
   * 
   * @return the fax table field name or <code>null</code> if not existing
   */
  public String getNameOfFaxTableField()
  {
    return null;
  }
}
