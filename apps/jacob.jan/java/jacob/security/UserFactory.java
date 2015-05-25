package jacob.security;

import java.security.GeneralSecurityException;
import java.util.List;

import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.exception.AuthenticationException;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.jacob.security.IUser;
import de.tif.jacob.security.IUserFactory;
import de.tif.jacob.security.UserManagement;

/**
 * User factory which uses management of admin application
 * 
 * @author Andreas Sonntag
 */
public final class UserFactory extends IUserFactory
{
  private static IUserFactory getAdminUserFactory() throws GeneralSecurityException
  {
    return UserManagement.getUserFactory(AdminApplicationProvider.getApplication());
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUserFactory#findByFullName(java.lang.String)
   */
  public List findByFullName(String fullNamePart) throws Exception
  {
    return getAdminUserFactory().findByFullName(fullNamePart);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUserFactory#findUser(java.lang.String)
   */
  public IUser findUser(String id) throws UserNotExistingException, GeneralSecurityException
  {
    return getAdminUserFactory().findUser(id);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUserFactory#getValid(java.lang.String, java.lang.String)
   */
  public IUser getValid(String userId, String passwd) throws AuthenticationException, GeneralSecurityException
  {
    return getAdminUserFactory().getValid(userId, passwd);
  }
}
