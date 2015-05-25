/*
 * Created on Mar 1, 2004
 *  
 */
package jacob.security;

import jacob.model.Employee;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.InvalidNewPasswordException;
import de.tif.jacob.security.impl.AbstractUser;
import de.tif.jacob.util.StringUtil;

/**
 * @author andreas
 * 
 */
public class User extends AbstractUser
{
  static public final transient String RCS_ID = "$Id: User.java,v 1.5 2006/11/14 10:23:39 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";

  private final UserFactory factory;
  private final String loginId;
  private final String fullName;
  private final String key;
  private final String email;
  private final String cellPhone;
  private final String phone;
  private final String fax;
  private final boolean availability;
  private String passwdhash;

  private final List roles = new ArrayList();

  /**
   * Constructs a user instance from an employee table record.
   * 
   * @param factory
   *          the user factory
   * @param userRecord
   *          the employee table record
   * @throws GeneralSecurityException
   *           on any problem
   */
  protected User(UserFactory factory, IDataTableRecord userRecord) throws GeneralSecurityException
  {
    this.factory = factory;

    try
    {
      // set user attributes
      //
      this.key = userRecord.getStringValue("pkey");
      this.fullName = userRecord.getStringValue("fullname");
      this.loginId = userRecord.getStringValue("loginname");
      this.email = userRecord.getStringValue("email");
      this.cellPhone = userRecord.getStringValue("mobile");
      this.phone = userRecord.getStringValue("phone");
      this.fax = userRecord.getStringValue("fax");
      this.passwdhash = userRecord.getStringValue("passwdhash");
      this.availability = userRecord.getintValue("availability") == 1;

      // determine user roles
      //
      if (userRecord.getbooleanValue(Employee.agentrole) == true)
        this.roles.add(Role.AGENT);      
      if (userRecord.getintValue("adminrole") == 1)
        this.roles.add(Role.ADMIN);
      if (userRecord.getintValue("ownerrole") == 1)
        this.roles.add(Role.OWNER);
      if (userRecord.getintValue("productmanagerrole") == 1)
        this.roles.add(Role.PRODUCTMANAGER);
      if (userRecord.getintValue("salesmanagerrole") == 1)
        this.roles.add(Role.SALESMANAGER);
      if (userRecord.getbooleanValue("salesagentrole") == true)
        this.roles.add(Role.SALESAGENT);
      if (userRecord.getbooleanValue(Employee.incidentrole) == true)
        this.roles.add(Role.INCIDENTAGENT);  
      if (userRecord.getbooleanValue(Employee.callrole) == true)
        this.roles.add(Role.CALLAGENT);
      
      // set user properties
      //
      this.setProperty("discount", userRecord.getSaveDecimalValue("discount_allowance"));
    }
    catch (Exception e)
    {
      throw new GeneralSecurityException(e.toString());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.security.IUser#setPassword(java.lang.String)
   */
  public void setPassword(String newPassword) throws InvalidNewPasswordException, Exception
  {
    // do not allow empty passwords
    if (null == newPassword || newPassword.length() == 0)
      throw new InvalidNewPasswordException();

    // fetch user record
    //
    IDataAccessor accessor = this.factory.newDataAccessor();
    IDataTable table = accessor.getTable("employee");
    table.qbeSetKeyValue("pkey", this.key);
    table.search();
    if (table.recordCount() != 1)
    {
      throw new Exception("Cannot find user with key " + this.key);
    }

    // determine password hash
    String passwdhash = createHashcode(newPassword);

    // store new password hash
    //
    IDataTransaction trans = accessor.newTransaction();
    try
    {
      table.getRecord(0).setValue(trans, "passwdhash", passwdhash);
      trans.commit();
    }
    finally
    {
      trans.close();
    }

    // set new password hash after success
    this.passwdhash = passwdhash;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.security.IUser#getLoginId()
   */
  public String getLoginId()
  {
    return loginId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.security.IUser#getFullName()
   */
  public String getFullName()
  {
    return fullName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.security.IUser#getKey()
   */
  public String getKey()
  {
    return key;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.security.impl.AbstractUser#fetchRoles()
   */
  protected Iterator fetchRoles() throws GeneralSecurityException
  {
    return roles.iterator();
  }

  /*
   * @see de.tif.jacob.security.IUser#getEMail()
   */
  public String getEMail()
  {
    return email;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.security.IUser#getPhone()
   */
  public String getPhone()
  {
    return phone;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.security.IUser#getCellPhone()
   */
  public String getCellPhone()
  {
    return cellPhone;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.security.IUser#getFax()
   */
  public String getFax()
  {
    return fax;
  }

  /**
   * Verifies whether the given password is valid.
   * 
   * @param password
   *          the password
   * @return <code>true</code> on success, otherwise <code>false</code>
   */
  protected boolean verifyAccess(String password)
  {
    // if user not available, do not allow access
    if (!this.availability)
      return false;

    // if password has not been set so far, access is allowed in any case
    if (this.passwdhash == null)
      return true;

    // otherwise compare password hashs
    return this.passwdhash.equals(createHashcode(password));
  }

  /**
   * Create a password hash by means of MD5 algorithm.
   * 
   * @param password
   *          the password
   * @return the password hash
   */
  private static String createHashcode(String password)
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
}
