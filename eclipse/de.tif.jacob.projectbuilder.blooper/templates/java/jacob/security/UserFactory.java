/*
 * Created on Mar 24, 2004
 *
 */
package jacob.security;

import jacob.common.AppLogger;
import jacob.model.Account;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

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
  static public final transient String RCS_ID = "$Id: UserFactory.java,v 1.1 2007/11/25 22:10:32 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public static final String ADMIN_USER_ID = "admin";
  public static final String GUEST_USER_ID = "guest";
  public static final String DEMO_USER_ID  = "demo";
  
  private static Log logger = AppLogger.getLogger();

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUserFactory#findUser(java.lang.String)
   */
  public IUser findUser(String loginId) throws UserNotExistingException, GeneralSecurityException
  {
    return new User(this, getUserRecord(loginId));
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUserFactory#getValid(java.lang.String, java.lang.String)
   */
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
      IDataTable table = newDataAccessor().getTable(Account.NAME);
      try 
      {
				if (table.exists())
				{
				  // employees are already existing
				  return null;
				}
			} 
      catch (InvalidExpressionException e) {
      	return null;
			}

      IUser admin = createInitialUser(ADMIN_USER_ID,"Administrator");
      IUser guest = createInitialUser(GUEST_USER_ID,"Guest User");
      IUser demo  = createInitialUser(DEMO_USER_ID,"Demo User");
      
      if (ADMIN_USER_ID.equals(loginId) && admin!=null)
          return admin;

      if (GUEST_USER_ID.equals(loginId) && guest!=null)
        return guest;
    }
    throw new InvalidUseridPasswordException();
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
  private IUser createInitialUser(String loginId, String name)
  {
    try
    {
      // search for employees
      //
      IDataAccessor dataAccessor = newDataAccessor();
      IDataTable table = dataAccessor.getTable(Account.NAME);
      IDataTransaction trans = dataAccessor.newTransaction();
      try
      {
        // create initial user
        //
        IDataTableRecord userRecord = table.newRecord(trans);
        userRecord.setValue(trans, Account.fullname,  name);
        userRecord.setValue(trans, Account.loginname, loginId);
        
        trans.commit();

        return new User(this, userRecord);
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

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUserFactory#findByFullName(java.lang.String)
   */
  public List findByFullName(String fullNamePart) throws GeneralSecurityException
  {
    IDataAccessor dataAccessor = newDataAccessor();
    IDataTable table = dataAccessor.getTable(Account.NAME);

    try
    {
      table.qbeSetValue(Account.fullname, fullNamePart);
      table.search();
      
      List result = new ArrayList(table.recordCount());
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
