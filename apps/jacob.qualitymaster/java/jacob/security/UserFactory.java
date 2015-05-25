/*
 * Created on Mar 24, 2004
 *
 */
package jacob.security;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.AuthenticationException;
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
  /*
   * @see de.tif.jacob.security.IUserFactory#findUser(java.lang.String) @author
   *      Andreas Herz
   */
  public IUser findUser(String id) throws UserNotExistingException, GeneralSecurityException
  {
    return new User(this, id);
  }

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
      // ensure login for user 'admin', if no employee has been
      // created so far.
      //
      if ("admin".equals(userId))
      {
        IUser admin = createInitialAdministrator();
        if (admin != null)
          return admin;
      }
    }
    throw new InvalidUseridPasswordException();
  }

  /**
   * Creates an initial administrator, if no administrator exists so far.
   * 
   * @return the initial administrator or <code>null</code>, if at least one
   *         administrator exists already.
   */
  private IUser createInitialAdministrator()
  {
    try
    {
      // search for administrators, i.e. employees
      // which are not assigned to a organization
      //
      IDataAccessor dataAccessor = newDataAccessor();
      IDataTable table = dataAccessor.getTable("employee");
      table.qbeSetValue("organization_key", "null");
      if (table.exists())
      {
        // administrators are already existing
        return null;
      }

      // create initial administrator
      //
      IDataTransaction trans = dataAccessor.newTransaction();
      try
      {
        IDataTableRecord adminRecord = table.newRecord(trans);
        adminRecord.setValue(trans, "fullname", "Default Administrator");
        adminRecord.setValue(trans, "loginname", "admin");
        adminRecord.setValue(trans, "email", "please enter");
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

  /*
   * @see de.tif.jacob.security.IUserFactory#findByFullName(java.lang.String)
   */
  public List findByFullName(String fullNamePart) throws Exception
  {
    List result = new ArrayList();
    IDataAccessor dataAccessor = newDataAccessor();
    IDataTable table = dataAccessor.getTable("employee");

    table.qbeSetValue("fullname", fullNamePart);
    table.search();
    for (int i = 0; i < table.recordCount(); i++)
    {
      IDataTableRecord userRecord = table.getRecord(i);
      result.add(new User(this, userRecord));
    }
    return result;
  }

}
