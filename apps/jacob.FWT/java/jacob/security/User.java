/*
 * Created on Mar 1, 2004
 *  
 */
package jacob.security;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.InvalidNewPasswordException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.jacob.security.impl.AbstractUser;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas Herz / Mike Doering
 * 
 */
public class User extends AbstractUser
{
  static public final transient String RCS_ID = "$Id: User.java,v 1.3 2006-04-20 16:28:00 mike Exp $";

  static public final transient String RCS_REV = "$Revision: 1.3 $";

  private String loginId;

  private String fullName;

  private String key;

  private String email;

  private String cellPhone;

  private String phone;

  private String fax;

  private String hashvalue;

  public boolean availability;

  public String department;

  private List roles = new ArrayList();

  protected User(IDataTableRecord userRecord) throws GeneralSecurityException
  {
    try
    {
      init(userRecord, userRecord.getSaveStringValue("loginname"));
    }
    catch (Exception e)
    {
      throw new GeneralSecurityException(e.getMessage());
    }
  }

  private boolean isAdmin(IDataAccessor dataAccessor, String loginId) throws Exception
  {
    IDataTable table = dataAccessor.getTable("administrator");

    table.qbeSetKeyValue("loginId", loginId);

    table.search();
    if (table.recordCount() == 1)
    {
      IDataTableRecord rec = table.getRecord(0);
      this.key = "0";
      this.fullName = rec.getSaveStringValue("fullname");
      this.loginId = loginId;
      this.hashvalue = rec.getSaveStringValue("passwdhash");
      roles.add(new Role("Administrator"));
      return true;
    }
    return false;
  }

  protected User(IDataAccessor dataAccessor, String loginId) throws GeneralSecurityException, UserNotExistingException
  {

    try
    {
      if (loginId == null || loginId.length() == 0)
      {
        throw new UserNotExistingException("");
      }
      IDataTable table = dataAccessor.getTable("employee");

      // Wenn kein TTS Login, dann Portal ID
      table.qbeSetKeyValue("loginname", loginId);

      table.search();
      if (table.recordCount() != 1)
      {
        table.qbeClear();
        table.qbeSetKeyValue("userid", loginId.toUpperCase());

        table.search();
      }

      if (table.recordCount() != 1)
      {
        if (isAdmin(dataAccessor, loginId))
        {
          return;
        }
        else
        {
          throw new UserNotExistingException(loginId);
        }
      }
      IDataTableRecord userRecord = table.getRecord(0);
      init(userRecord, loginId);
    }
    catch (UserNotExistingException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new GeneralSecurityException(e.getMessage());
    }
  }

  /**
   * 
   */
  public void setPassword(String newPassword) throws InvalidNewPasswordException, Exception
  {

    throw new UserException(" Passwort ändern ist nicht möglich");
  }

  /**
   * 
   * @param userRecord
   * @throws Exception
   */
  private void init(IDataTableRecord userRecord, String loginId) throws Exception
  {
    this.key = userRecord.getStringValue("pkey");
    this.fullName = userRecord.getStringValue("fullname");
    this.loginId = loginId;
    this.email = userRecord.getStringValue("emailcorr");
    this.cellPhone = userRecord.getStringValue("mobilecorr");
    this.phone = userRecord.getStringValue("phonecorr");
    this.fax = userRecord.getStringValue("faxcorr");
    this.setProperty("lpr_ip", userRecord.getStringValue("lpr_ip"));
    this.hashvalue = userRecord.getStringValue("passwdhash");
    this.department = userRecord.getStringValue("department");
    this.availability = (userRecord.getStringValue("availability").equals("1"));
    if (userRecord.getStringValue("ak_role").equals("1"))
      roles.add(new Role("cq_ak"));
    if (userRecord.getStringValue("supportrole").equals("1"))
      roles.add(new Role("cq_agent"));
    if (userRecord.getStringValue("pm_role").equals("1"))
      roles.add(new Role("cq_pm"));
    if (userRecord.getStringValue("warte_role").equals("1"))
      roles.add(new Role("cq_warte"));
    if (userRecord.getStringValue("sdadmin_role").equals("1"))
      roles.add(new Role("cq_sdadmin"));
    if (userRecord.getStringValue("admin_role").equals("1"))
      roles.add(new Role("cq_admin"));
    if (userRecord.getStringValue("superak_role").equals("1"))
      roles.add(new Role("cq_superak"));
    if (userRecord.getStringValue("mbtech_role").equals("1"))
      roles.add(new Role("cq_mbtechagent"));
    if (userRecord.getStringValue("net_role").equals("1"))
      roles.add(new Role("net_role"));
    roles.add(new Role("User"));

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
   * Lacy instantiation of the user roles. The roles are not neccessary for the
   * common usage of this class. At the moment only for the login procedure.
   * 
   * @return
   * @throws GeneralSecurityException
   */
  protected Iterator fetchRoles() throws GeneralSecurityException
  {

    return roles.iterator();
  }

  /*
   * @see de.tif.jacob.security.IUser#getEMail()
   */
  public String getEMail()
  {
    return email;
  }

  public String getPhone()
  {
    return phone;
  }

  public String getCellPhone()
  {
    return cellPhone;
  }

  public String getFax()
  {
    return fax;
  }

  public boolean verifyAccess(String password)
  {

    // Zugriff gewähren wenn Password stimmt
    if (null == password || password.length() == 0)
      return false;

    // Wenn hashvalue leer ist dann Defaultwert LoginID

    if (this.hashvalue == null)
      return password.equals(this.loginId);

    return createHashcode(password).equals(this.hashvalue);
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
}
