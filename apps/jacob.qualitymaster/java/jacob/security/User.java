/*
 * Created on Mar 1, 2004
 *  
 */
package jacob.security;

import jacob.model.Employee;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.InvalidNewPasswordException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.jacob.security.impl.AbstractUser;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas Herz / Mike Doering
 *  
 */
public class User extends AbstractUser
{
  private final UserFactory factory;
  private final String loginId;
  private final boolean login_enabled;
  private final String fullName;
  private final String passwdhash;
  private final String key;
  private final String email;
  private final String mandatorid;

  private final List roles = new ArrayList();

  protected User(UserFactory factory, IDataTableRecord userRecord) throws Exception
  {
    this.factory = factory;
    this.key = userRecord.getStringValue(Employee.pkey);
    this.fullName = userRecord.getStringValue(Employee.fullname);
    this.loginId = userRecord.getStringValue(Employee.loginname);
    this.login_enabled = userRecord.getbooleanValue(Employee.login_enabled);
    this.email = userRecord.getStringValue(Employee.email);
    this.passwdhash = userRecord.getStringValue(Employee.password);
    this.mandatorid = userRecord.getStringValue(Employee.organization_key);
  }
  
  protected User(UserFactory factory, String id) throws UserNotExistingException
  {
    this.factory = factory;
    IDataTableRecord userRecord = findUserRecord(id);
    try
    {
      this.key = userRecord.getStringValue(Employee.pkey);
      this.fullName = userRecord.getStringValue(Employee.fullname);
      this.loginId = userRecord.getStringValue(Employee.loginname);
      this.login_enabled = userRecord.getbooleanValue(Employee.login_enabled);
      this.email = userRecord.getStringValue(Employee.email);
      this.passwdhash = userRecord.getStringValue(Employee.password);
      this.mandatorid = userRecord.getStringValue(Employee.organization_key);
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
  
  private IDataTableRecord findUserRecord(String loginname) throws UserNotExistingException
  {
    IDataTable table = this.factory.newDataAccessor().getTable("employee");

    try
    {
      table.qbeSetKeyValue("loginname", loginname);

      table.search();
      if (table.recordCount() == 1)
      {
        return table.getRecord(0);
      }
      else if (table.recordCount() > 1)
      {
        throw new RuntimeException("Loginname is not unique in the employee table. Please inform the Administrator.");
      }
      else
      {
        throw new UserNotExistingException(loginname);
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

  public boolean verifyPasswd(String password)
  {
    if (this.login_enabled == false)
      return false;

    // no password hash existing -> login always succeeds
    if (this.passwdhash == null)
      return true;

    return this.passwdhash.equals(createHashcode(password));
  }

  private static String createHashcode(String password)
  {
    if (password == null || password.length() == 0)
      return null;

    try
    {
      MessageDigest md = MessageDigest.getInstance("MD5");
      return StringUtil.toHexString(md.digest(password.getBytes("ISO-8859-1")));
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

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.security.IUser#getMandatorId()
   */
  public String getMandatorId()
  {
    return this.mandatorid;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.security.impl.AbstractUser#fetchRoles()
   */
  protected Iterator fetchRoles() throws GeneralSecurityException
  {
    if (this.mandatorid == null)
      roles.add(Role.DEVELOPER);
    else if(this.loginId.equals("guest"))
      roles.add(Role.ANONYMOUS);
    else
      roles.add(Role.CUSTOMER);
    
    return this.roles.iterator();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUser#setPassword(java.lang.String)
   */
  public void setPassword(String newPassword) throws InvalidNewPasswordException, Exception
  {
  	if(this.loginId.equals("guest"))
  		throw new UserException("Unable to change password for anonymous user.");
  	
    // fetch user record of this user
    //
    IDataTableRecord userRecord = findUserRecord(this.loginId);

    // and set new password
    //
    IDataTransaction trans = userRecord.getAccessor().newTransaction();
    try
    {
      userRecord.setValue(trans, "password", createHashcode(newPassword));
      trans.commit();
    }
    finally
    {
      trans.close();
    }
  }
  
  private static final Set SUPPORTED_LANGUAGES = new HashSet();
  
  static
  {
    SUPPORTED_LANGUAGES.add(Locale.ENGLISH.getLanguage());
    SUPPORTED_LANGUAGES.add(Locale.GERMAN.getLanguage());
  }
  
  public Locale getLocale()
  {
    // get browser language (if existing) and check for englisch.
    // Note: This is for allowing US number/data format!
    //
    Locale httpLocale = super.getLocale();
    if (httpLocale != null && SUPPORTED_LANGUAGES.contains(httpLocale.getLanguage()))
      return httpLocale;
    
    return Locale.ENGLISH;
  }
  
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("User[");
    buffer.append("loginId = ").append(loginId);
    buffer.append(", fullName = ").append(fullName);
    buffer.append(", passwdhash = ").append(passwdhash);
    buffer.append(", key = ").append(key);
    buffer.append(", email = ").append(email);
    buffer.append(", mandatorid = ").append(mandatorid);
    buffer.append(", roles = ").append(roles);
    buffer.append("]");
    return buffer.toString();
  }
}
