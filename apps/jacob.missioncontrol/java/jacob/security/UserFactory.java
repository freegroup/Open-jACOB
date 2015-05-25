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
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.InvalidUseridPasswordException;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.jacob.security.IUser;
import de.tif.jacob.security.IUserFactory;

/**
 * @author andherz
 *
 */
public class UserFactory extends IUserFactory
{
	/* 
	 * @see de.tif.jacob.security.IUserFactory#findUser(java.lang.String)
	 */
	public IUser findUser(String id) throws UserNotExistingException, GeneralSecurityException
	{
		return new User(newDataAccessor(), id);
	}
  
  public IUser getValid(String userId, String passwd) throws AuthenticationException, GeneralSecurityException
	{
		try
		{
		  // Search the corresponding user in the LDAP/Database.... and return
		  // the corresponding User object
		  //
			return new User(newDataAccessor(), userId);
		  
		  // ... or return always a guest user.
		  //
	//		return new User(getApplicationDefinition(), "guest");
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
      IDataTable table = dataAccessor.getTable("person");
      if (table.exists())
      {
        // persons are already existing
        return null;
      }

      // create initial administrator
      //
      IDataTransaction trans = dataAccessor.newTransaction();
      try
      {
        IDataTableRecord adminRecord = table.newRecord(trans);
        adminRecord.setValue(trans, "lastname", "Administrator");
        adminRecord.setValue(trans, "uid", "admin");
        adminRecord.setValue(trans, "adminflag", "1");
        trans.commit();

        return new User(adminRecord);
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
      public List findByFullName(String fullNamePart) throws GeneralSecurityException
      {
        List result = new ArrayList();
            IDataAccessor dataAccessor = newDataAccessor();
            IDataTable table = dataAccessor.getTable("person");

        try
        {
            // Daimler ist case insensitive
            table.qbeSetValue("fullname",  fullNamePart);
            table.qbeSetKeyValue("uid","!NULL");
            table.search();
          for(int i=0;i<table.recordCount();i++)
          {
              IDataTableRecord userRecord = table.getRecord(i);
                result.add(new User(userRecord));
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

}
