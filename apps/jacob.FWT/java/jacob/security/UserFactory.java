/*
 * Created on Mar 24, 2004
 *
 */
package jacob.security;

import jacob.common.Access;
import jacob.common.AppLogger;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
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
  static public final transient String RCS_ID = "$Id: UserFactory.java,v 1.4 2006-04-20 16:28:00 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";
  static Log logger = AppLogger.getLogger();

  /*
   * @see de.tif.jacob.security.IUserFactory#findUser(java.lang.String) @author
   *      Andreas Herz
   */
  public IUser findUser(String loginId) throws UserNotExistingException, GeneralSecurityException
  {
    return new User(newDataAccessor(), loginId);
  }

  public IUser getValid(String loginId, String passwd) throws AuthenticationException, GeneralSecurityException
  {
    try
    {
   
      User user = new User(newDataAccessor(), loginId);
      // Passwort merken für späteres Login!

      if (user.verifyAccess(passwd))
      {
        user.setProperty("passwd", passwd);
        // Die Applikationen sammeln
        user.setProperty("applications", Access.getAccess(newDataAccessor(),user));
        return user;
      }
    }
    catch (UserNotExistingException ex)
    {
      // ignore
    }
    throw new InvalidUseridPasswordException();
  }

  /**
   * 
   */
  public List findByFullName(String fullNamePart) throws GeneralSecurityException
  {
    List result = new ArrayList();
    IDataAccessor dataAccessor = newDataAccessor();
    IDataTable table = dataAccessor.getTable("employee");

    try
    {
      // Daimler ist case insensitive
      table.qbeSetValue("fullname", fullNamePart);

      table.search();
      for (int i = 0; i < table.recordCount(); i++)
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
