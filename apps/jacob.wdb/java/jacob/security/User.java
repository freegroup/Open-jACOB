/*
 * Created on Mar 1, 2004
 *  
 */
package jacob.security;

import jacob.model.Account;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.InvalidNewPasswordException;
import de.tif.jacob.security.impl.AbstractUser;

/**
 * @author andreas
 *
 */
public class User extends AbstractUser
{
  static public final transient String RCS_ID = "$Id: User.java,v 1.3 2010-10-14 10:04:49 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  private final String loginId;
  private final String fullName;
  private final String key;
  private final String email;
  private final String mandatorId;
  private final boolean anonymous;
  
  private String password;
  
  private final List<Role> roles = new ArrayList<Role>();
  private final UserFactory factory;
  
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
      String role = userRecord.getStringValue(Account.role);
      this.anonymous = Account.role_ENUM._anonymous.equals(role);
      this.key = userRecord.getStringValue(Account.pkey);
      this.fullName = userRecord.getStringValue(Account.fullname);
      this.loginId = userRecord.getStringValue(Account.loginname);
      this.email = null;
      this.password = userRecord.getSaveStringValue(Account.password);

      // determine user roles
      //
      if(this.password.length()==0 && this.anonymous==false)
      {
        this.roles.add(Role.PASSWORD);
        this.mandatorId=loginId;
      }
      else if (loginId.equals(UserFactory.ADMIN_USER_ID))
      {
        this.roles.add(Role.ADMIN);
        this.roles.add(Role.DEBUG);
        this.mandatorId=null;
      }
      else if(Account.role_ENUM._administrator.equals(role))
      {
        this.roles.add(Role.ADMIN);
        this.mandatorId=null;
      }
      else
      {
        this.roles.add(Role.USER);
        this.mandatorId=loginId;
      }
    }
    catch (Exception e)
    {
      throw new GeneralSecurityException(e.toString());
    }
  }

  
  @Override
  public boolean isAnonymous()
  {
    return anonymous;
  }


  public String getMandatorId() 
  {
		return mandatorId;
	}


	/* (non-Javadoc)
   * @see de.tif.jacob.security.IUser#setPassword(java.lang.String)
   */
  public void setPassword(String newPassword) throws InvalidNewPasswordException, Exception
  {
    // do not allow empty passwords
    if (null == newPassword || newPassword.length() == 0)
      throw new InvalidNewPasswordException();

    if (this.isAnonymous())
      throw new Exception("Unable to change password for anonymous user");
    
    // fetch user record
    //
    IDataAccessor accessor = this.factory.newDataAccessor();
    IDataTable table = accessor.getTable(Account.NAME);
    table.qbeSetKeyValue(Account.pkey, this.key);
    table.search();
    if (table.recordCount() != 1)
    {
      throw new Exception("Cannot find user with key " + this.key);
    }

    // determine password hash
    // store new password hash
    //
    IDataTransaction trans = accessor.newTransaction();
    try
    {
      table.getRecord(0).setValue(trans, Account.password, newPassword);
      trans.commit();
    }
    finally
    {
      trans.close();
    }

    // set new password hash after success
    this.password = newPassword;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUser#getLoginId()
   */
  public String getLoginId()
  {
    return loginId;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUser#getFullName()
   */
  public String getFullName()
  {
    return fullName;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUser#getKey()
   */
  public String getKey()
  {
    return key;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.impl.AbstractUser#fetchRoles()
   */
  protected Iterator<Role> fetchRoles() throws GeneralSecurityException
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


  /**
   * Verifies whether the given password is valid.
   * 
   * @param password
   *          the password
   * @return <code>true</code> on success, otherwise <code>false</code>
   */
  protected boolean verifyAccess(String password)
  {
    if (isAnonymous())
      return true;
    
    // otherwise compare password hashs
    return this.password==null || this.password.equals(password);
  }
}
