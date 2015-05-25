package jacob.security;

import java.security.GeneralSecurityException;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.qes.QeSUser;
import de.tif.qes.QeSUserFactory;

/**
 */
public class User extends QeSUser
{
  /**
   * @param userFactory
   * @param userRecord
   * @throws java.security.GeneralSecurityException
   */
  protected User(QeSUserFactory userFactory, IDataTableRecord userRecord) throws GeneralSecurityException
  {
    super(userFactory, userRecord);
  }
  
  /**
   * @param userFactory
   * @param loginId
   * @throws java.security.GeneralSecurityException
   * @throws de.tif.jacob.core.exception.UserNotExistingException
   */
  protected User(QeSUserFactory userFactory, String loginId) throws GeneralSecurityException, UserNotExistingException
  {
    super(userFactory, loginId);
  }
}
