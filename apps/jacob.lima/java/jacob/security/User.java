package jacob.security;

import jacob.model.Employee;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.InvalidNewPasswordException;
import de.tif.jacob.security.impl.AbstractUser;
import de.tif.jacob.util.StringUtil;

/**
 * @author andherz
 *  
 */
public class User extends AbstractUser
{
  static public final transient String RCS_ID = "$Id: User.java,v 1.2 2006/03/07 19:20:35 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  private final String loginId;
  private final String fullName;
  private String passwd;
  private final String key;
  private final String email;
  private final String mandatorid;

  private final List roles = new ArrayList();

  /**
   * Constructs an user instance from an employee table record.
   * 
   * @param factory
   *          the user factory
   * @param userRecord
   *          the employee table record
   * @throws GeneralSecurityException
   *           on any problem
   */
  protected User(UserFactory factory, IDataTableRecord userRecord) throws GeneralSecurityException
  {
    try
    {
      // set user attributes
      //
      this.key = userRecord.getStringValue(Employee.pkey);
      this.fullName = userRecord.getStringValue(Employee.fullname);
      this.loginId = userRecord.getStringValue(Employee.loginname);
      this.email = userRecord.getStringValue(Employee.email);
      this.passwd = userRecord.getStringValue(Employee.password);
      this.mandatorid = userRecord.getStringValue(Employee.organization_key);

      // determine user roles
      //
    }
    catch (Exception e)
    {
      throw new GeneralSecurityException(e.toString());
    }
  }

  /**
   * @return Returns the unique id of the user.
   */
  public String getLoginId()
  {
    return loginId;
  }

  /**
   * @return Returns the employee.fullname of the user
   */
  public String getFullName()
  {
    return fullName;
  }

  /**
   * @return Returns the employee.pkey of the user
   */
  public String getKey()
  {
    return key;
  }

  /**
   * @return Returns the passwd.
   */
  protected String getPasswd()
  {
    return passwd;
  }

  public void setPassword(String newPassword) throws InvalidNewPasswordException, Exception
  {
    if (null == newPassword || newPassword.length() == 0)
      throw new InvalidNewPasswordException();
    
    Context context = Context.getCurrent();
    IDataTable table = context.getDataTable(Employee.NAME);
    table.qbeClear();
    table.qbeSetKeyValue(Employee.pkey, this.key);
    table.search();
    if (table.recordCount() == 1)
    {
      IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
      try
      {
        String hashCode = createHashcode(newPassword);
        table.getRecord(0).setValue(currentTransaction, Employee.password, hashCode);
        currentTransaction.commit();
        this.passwd = hashCode;
      }
      finally
      {
        currentTransaction.close();
      }
    }
  }

  /* 
   * @see de.tif.jacob.security.IUser#getEMail()
   */
  public String getEMail()
  {
    return email;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.security.IUser#getMandatorId()
   */
  public String getMandatorId()
  {
    return this.mandatorid;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.impl.AbstractUser#fetchRoles()
   */
  protected Iterator fetchRoles() throws GeneralSecurityException
  {
    if (this.mandatorid == null)
    {
      // 		roles.add(new Role("admin"));
      // 		roles.add(new Role("cq_admin"));
    }
    return this.roles.iterator();
  }

  public boolean verifyAccess(String password)
  {
    return StringUtil.saveEquals(createHashcode(password), this.passwd);
  }

  public static String createHashcode(String password)
  {
    if (password == null || password.length() == 0)
      return null;

    try
    {
      MessageDigest md = MessageDigest.getInstance("MD5");
      return StringUtil.toHexString(md.digest(password.getBytes()));
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }

  private static final Set SUPPORTED_LANGUAGES = new HashSet();
  
  static
  {
    SUPPORTED_LANGUAGES.add(Locale.ENGLISH.getLanguage());
    SUPPORTED_LANGUAGES.add(Locale.GERMAN.getLanguage());
  }
  
  public Locale getLocale()
  {
    // get browser language (if existing) and check for englisch.
    // Note: This is for allowing US number/data format!
    //
    Locale httpLocale = super.getLocale();
    if (httpLocale != null && SUPPORTED_LANGUAGES.contains(httpLocale.getLanguage()))
      return httpLocale;
    
    return Locale.ENGLISH;
  }
  
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("User[");
    buffer.append("loginId = ").append(loginId);
    buffer.append(", fullName = ").append(fullName);
    buffer.append(", passwd = ").append(passwd);
    buffer.append(", key = ").append(key);
    buffer.append(", email = ").append(email);
    buffer.append(", mandatorid = ").append(mandatorid);
    buffer.append(", roles = ").append(roles);
    buffer.append("]");
    return buffer.toString();
  }
}
