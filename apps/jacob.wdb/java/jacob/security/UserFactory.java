/*
 * Created on Mar 24, 2004
 *
 */
package jacob.security;

import jacob.model.Account;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.AuthenticationException;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.InvalidUseridPasswordException;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.jacob.security.IUser;
import de.tif.jacob.security.IUserFactory;

/**
 * @author Andreas Herz
 *
 */
public class UserFactory extends IUserFactory
{
  static public final transient String RCS_ID = "$Id: UserFactory.java,v 1.2 2010-10-14 10:04:49 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  public static final String ADMIN_USER_ID = "admin";
  
  @Override
  public IUser findUser(String loginId) throws UserNotExistingException, GeneralSecurityException
  {
    return new User(this, getUserRecord(loginId));
  }

  @Override
  public IUser getAnonymous() throws GeneralSecurityException
  {
    IDataTableRecord anonymous= getAnonymousUserRecord();
    
    if(anonymous==null)
      return null;
    
    return new User(this, anonymous);
  }

  @Override
  public IUser getValid(String loginId, String passwd) throws AuthenticationException, GeneralSecurityException
  {
    try
    {
      User user = new User(this, getUserRecord(loginId));

      if (user.verifyAccess(passwd))
        return user;
    }
    catch (UserNotExistingException ex)
    {
      // ensure login for user 'admin', if no employee has been
      // created so far.
      //
      if (ADMIN_USER_ID.equals(loginId))
      {
        IUser admin = createInitialAdministrator();
        if (admin != null)
          return admin;
      }
    }
    throw new InvalidUseridPasswordException();
  }

  private IDataTableRecord getAnonymousUserRecord() throws GeneralSecurityException
  {
    try
    {
      IDataTable table = newDataAccessor().getTable(Account.NAME);
      table.qbeSetKeyValue(Account.role, Account.role_ENUM._anonymous);
      table.search();
      if (table.recordCount() > 0)
      {
        return table.getRecord(0);
      }
    }
    catch (Exception e)
    {
      throw new GeneralSecurityException(e.toString());
    }
    return null;
  }

  private IDataTableRecord getUserRecord(String loginId) throws UserNotExistingException, GeneralSecurityException
  {
    try
    {
      IDataTable table = newDataAccessor().getTable(Account.NAME);
      table.qbeSetKeyValue(Account.loginname, loginId);
      table.search();
      if (table.recordCount() == 1)
      {
        return table.getRecord(0);
      }
    }
    catch (Exception e)
    {
      throw new GeneralSecurityException(e.toString());
    }
    throw new UserNotExistingException(loginId);
  }

  /**
   * Creates an initial administrator, if no employee exists so far.
   * 
   * @return the initial administrator or <code>null</code>, if at least one
   *         employee exists already.
   */
  private IUser createInitialAdministrator()
  {
    try
    {
      // search for employees
      //
      IDataAccessor dataAccessor = newDataAccessor();
      IDataTable table = dataAccessor.getTable(Account.NAME);
      if (table.exists())
      {
        // employees are already existing
        return null;
      }

      // create initial administrator
      //
      IDataTransaction trans = dataAccessor.newTransaction();
      try
      {
        IDataTableRecord adminRecord = table.newRecord(trans);
        adminRecord.setValue(trans, Account.fullname, "Administrator");
        adminRecord.setValue(trans, Account.loginname, ADMIN_USER_ID);
        adminRecord.setValue(trans, Account.role, Account.role_ENUM._administrator);

        // Anonymous user gleich mit anlegen
        IDataTableRecord anonymousRecord = table.newRecord(trans);
        anonymousRecord.setValue(trans, Account.fullname, "Gast");
        anonymousRecord.setValue(trans, Account.loginname, "anonymous");
        anonymousRecord.setValue(trans, Account.role, Account.role_ENUM._anonymous);

        trans.commit();

        return new User(this, adminRecord);
      }
      finally
      {
        trans.close();
      }
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public List<IUser> findByFullName(String fullNamePart) throws GeneralSecurityException
  {
    IDataAccessor dataAccessor = newDataAccessor();
    IDataTable table = dataAccessor.getTable(Account.NAME);

    try
    {
      table.qbeSetValue(Account.fullname, fullNamePart);
      table.search();
      
      List<IUser> result = new ArrayList<IUser>(table.recordCount());
      for (int i = 0; i < table.recordCount(); i++)
      {
        IDataTableRecord userRecord = table.getRecord(i);
        result.add(new User(this, userRecord));
      }
      return result;
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
  }
}
