/*
 * Created on Mar 24, 2004
 *
 */
package jacob.security;

import jacob.common.AppLogger;
import jacob.common.Constants;
import jacob.model.Account;
import jacob.model.Room;
import jacob.model.Room_admin;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.AuthenticationException;
import de.tif.jacob.core.exception.ExceptionHandler;
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
  static public final transient String RCS_ID = "$Id: UserFactory.java,v 1.1 2007/02/02 22:26:45 freegroup Exp $";
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

      // erster Start der Application
      // Jetzt werden erstmal ein paar Stammdaten angelegt
      //
  		Context context = Context.getCurrent();
  		IDataTransaction trans = context.getDataAccessor().newTransaction();
  		try
  		{
  			// Es muss immer ein Configuration Record existieren
  			//
  			IDataTable emailTable = context.getDataTable(jacob.model.Email.NAME);
 				IDataTableRecord configRecord = emailTable.newRecord(trans);
  			
        // Es muss der default "Raum" existieren
  			//
        IDataTable roomTable = context.getDataTable(jacob.model.Room.NAME);
				IDataTableRecord roomRecord = roomTable.newRecord(trans);
				roomRecord.setStringValue(trans, Room.name, Constants.DEFAULT_ROOM_NAME);
				String roomPkey = roomRecord.getStringValue(Room.pkey);

				IUser admin = createInitialUser(trans, ADMIN_USER_ID,"Administrator",roomPkey);
        IUser guest = createInitialUser(trans, GUEST_USER_ID,"Guest User",roomPkey);
        IUser demo  = createInitialUser(trans, DEMO_USER_ID,"Demo User",roomPkey);
        
        
  			
  			trans.commit();
  			trans.close();
  			
				// Der Default Raum hat immer den "Admin" als RoomAdmin
				// (Muss in einer eigenen Transaction laufen, da der User-Record vorhanden sein muss
  			//
  			trans = context.getDataAccessor().newTransaction();
				roomRecord.setValue(trans, Room.room_admin_key, admin.getKey());
  			trans.commit();
  			
  			if (ADMIN_USER_ID.equals(loginId) && admin!=null)
            return admin;

        if (GUEST_USER_ID.equals(loginId) && guest!=null)
          return guest;

  		}
  		catch(Exception exc)
  		{
  			ExceptionHandler.handle(exc);
  		}
  		finally
  		{
  			trans.close();
  		}
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
   * @param roomPkey 
   * @param trans2 
   * 
   * @return the initial administrator or <code>null</code>, if at least one
   *         employee exists already.
   */
  private IUser createInitialUser(IDataTransaction trans, String loginId, String name, String roomPkey)
  {
    try
    {
      // search for employees
      //
      IDataAccessor dataAccessor = newDataAccessor();
      IDataTable table = dataAccessor.getTable(Account.NAME);
      
      // create initial user
      //
      IDataTableRecord userRecord = table.newRecord(trans);
      userRecord.setValue(trans, Account.fullname,  name);
      userRecord.setValue(trans, Account.loginname, loginId);
      userRecord.setValue(trans, Account.room_key, roomPkey);
      return new User(this, userRecord);
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
