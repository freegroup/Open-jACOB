/*
 * Created on Mar 24, 2004
 *
 */
package jacob.security;

import jacob.common.AppLogger;
import jacob.model.Employee;
import jacob.model.Organization;

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
 * 
 * @author Andreas Sonntag
 */
public class UserFactory extends IUserFactory
{
  static public final transient String RCS_ID = "$Id: UserFactory.java,v 1.2 2006/03/07 19:20:35 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

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
      // ensure login for user 'admin', if no employee has been
      // created so far.
      //
      if ("admin".equals(loginId))
      {
        IUser admin = createInitialAdministrator();
        if (admin != null)
          return admin;
      }
    }
    throw new InvalidUseridPasswordException();
  }

  private IDataTableRecord getUserRecord(String loginId) throws UserNotExistingException, GeneralSecurityException
  {
    try
    {
      IDataTable table = newDataAccessor().getTable(Employee.NAME);
      table.qbeSetKeyValue(Employee.loginname, loginId);
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
      IDataTable table = dataAccessor.getTable(Employee.NAME);
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
        logger.info("Creating initial administrator");
        
        final String INITIAL_EMAIL = "admin@initial.org";
        
        // create initial organization
        //
        IDataTable orgtable = dataAccessor.getTable(Organization.NAME);
        IDataTableRecord orgRecord = orgtable.newRecord(trans);
        orgRecord.setValue(trans, Organization.name, "Initial Organization");
        orgRecord.setValue(trans, Organization.email_contact, INITIAL_EMAIL);
        
        // create initial administrator
        //
        IDataTableRecord adminRecord = table.newRecord(trans);
        adminRecord.setValue(trans, Employee.firstname, "Default");
        adminRecord.setValue(trans, Employee.lastname, "Administrator");
        adminRecord.setValue(trans, Employee.loginname, "admin");
        adminRecord.setValue(trans, Employee.email, INITIAL_EMAIL);
        adminRecord.setValue(trans, Employee.organization_key, orgRecord.getValue(Organization.pkey));
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

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUserFactory#findByFullName(java.lang.String)
   */
  public List findByFullName(String fullNamePart) throws GeneralSecurityException
  {
    IDataAccessor dataAccessor = newDataAccessor();
    IDataTable table = dataAccessor.getTable(Employee.NAME);

    try
    {
      table.qbeSetValue(Employee.fullname, fullNamePart);
      table.qbeSetValue(Employee.loginname, "!null");
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
