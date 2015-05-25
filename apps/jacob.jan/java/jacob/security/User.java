package jacob.security;

import java.security.GeneralSecurityException;
import java.util.Iterator;
import java.util.Locale;

import de.tif.jacob.core.exception.InvalidNewPasswordException;
import de.tif.jacob.security.IUser;
import de.tif.jacob.security.impl.AbstractUser;

/**
 * Encapsulated admin user.
 * 
 * @author Andreas Sonntag
 */
public final class User extends AbstractUser
{
  private final IUser embedded;

  protected User(IUser embedded)
  {
    this.embedded = embedded;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.impl.AbstractUser#fetchRoles()
   */
  protected Iterator fetchRoles() throws GeneralSecurityException
  {
    return this.embedded.getRoles();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUser#getEMail()
   */
  public String getEMail()
  {
    return this.embedded.getEMail();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUser#getFullName()
   */
  public String getFullName()
  {
    return this.embedded.getFullName();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUser#getKey()
   */
  public String getKey()
  {
    return this.embedded.getKey();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.IUser#getLoginId()
   */
  public String getLoginId()
  {
    return this.embedded.getLoginId();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.impl.AbstractUser#getCellPhone()
   */
  public String getCellPhone()
  {
    return this.embedded.getCellPhone();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.impl.AbstractUser#getDefaultLocale()
   */
  public Locale getDefaultLocale()
  {
    return this.embedded.getLocale();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.impl.AbstractUser#getFax()
   */
  public String getFax()
  {
    return this.embedded.getFax();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.impl.AbstractUser#getLocale()
   */
  public Locale getLocale()
  {
    return this.embedded.getLocale();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.impl.AbstractUser#getMandatorId()
   */
  public String getMandatorId()
  {
    return this.embedded.getMandatorId();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.impl.AbstractUser#getPhone()
   */
  public String getPhone()
  {
    return this.embedded.getPhone();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.impl.AbstractUser#getTimezoneOffset()
   */
  public int getTimezoneOffset()
  {
    return this.embedded.getTimezoneOffset();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.impl.AbstractUser#setPassword(java.lang.String)
   */
  public void setPassword(String arg0) throws InvalidNewPasswordException, Exception
  {
    this.embedded.setPassword(arg0);
  }
}
