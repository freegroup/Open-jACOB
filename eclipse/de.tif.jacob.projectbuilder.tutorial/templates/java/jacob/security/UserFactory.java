/*
 * Created on Mar 24, 2004
 *
 */
package jacob.security;


import java.security.GeneralSecurityException;
import java.util.List;

import de.tif.jacob.core.exception.AuthenticationException;
import de.tif.jacob.core.exception.InvalidUseridPasswordException;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.jacob.security.ISecurityClass;
import de.tif.jacob.security.IUser;
import de.tif.jacob.security.IUserFactory;

/**
 * @author {author}
 *
 */
public class UserFactory extends IUserFactory
{
	/* 
	 * @see de.tif.jacob.security.IUserFactory#findUser(java.lang.String)
	 */
	public IUser findUser(String id) throws UserNotExistingException, GeneralSecurityException
	{
		return new User(getApplicationDefinition(), id);
	}
  
  public IUser getValid(String userId, String passwd) throws AuthenticationException, GeneralSecurityException
	{
		try
		{
		  // Search the corresponding user in the LDAP/Database.... and return
		  // the corresponding User object
		  //
//			return new User(getApplicationDefinition(), userId);
		  
		  // ... or return always a guest user.
		  //
			return new User(getApplicationDefinition(), "guest");
		}
		catch (UserNotExistingException ex)
		{
			// ignore
		}
		throw new InvalidUseridPasswordException();
	}
  
  /* 
   * @see de.tif.jacob.security.IUserFactory#findByFullName(java.lang.String)
   */
  public List findByFullName(String fullNamePart) throws Exception
  {
    // FREEGROUP: Suchfunktion für benutzer implementieren
    throw new Exception("function not implemented....TODO");
  }

}
