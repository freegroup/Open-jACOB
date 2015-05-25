/*
 * Created on Mar 1, 2004
 *  
 */
package jacob.security;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.jacob.security.impl.AbstractUser;
/**
 * @author Andreas Herz / Mike Doering
 *  
 */
public class User extends AbstractUser
{
    String loginId;
    String fullName;
    String passwd;
    String key;
    String email;
    String mandatorid;

  
  private final List roles = new ArrayList();
  
  protected User(IDataTableRecord userRecord) throws GeneralSecurityException
  {
      try
      {
          init(userRecord);
      }
      catch (Exception e)
      {
          throw new GeneralSecurityException(e.getMessage());
      }
  }
  
    protected User(IDataAccessor dataAccessor, String id) throws UserNotExistingException
    {
        this.loginId = id.toUpperCase();
    
        IDataTable table = dataAccessor.getTable("person");

        try
        {
      // use exact match operator for login name
      table.qbeSetKeyValue("uid", loginId);
      
      table.search();
            if (table.recordCount() == 1)
            {
                IDataTableRecord userRecord = table.getRecord(0);
                init(userRecord);

            }
            else if(table.recordCount() > 1)
            {
              throw new RuntimeException("Loginname is not unique in the employee table. Please inform the Administrator.");
            }
            else
            {
                throw new UserNotExistingException();
            }
        }
        catch (UserNotExistingException e)
        {
            throw e;
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private void init(IDataTableRecord userRecord) throws Exception
    {
        this.key  = userRecord.getStringValue("pkey");
        this.fullName = userRecord.getStringValue("fullname");
        this.loginId  = userRecord.getStringValue("uid").toUpperCase();
        this.email    = userRecord.getStringValue("email");
        this.passwd  = userRecord.getStringValue("uid");
        this.mandatorid = null;
        if (userRecord.getSaveStringValue("adminflag").equals("1")) roles.add(new Role("admin"));
        if (userRecord.getSaveStringValue("agentflag").equals("1")) roles.add(new Role("agent"));
        if (userRecord.getSaveStringValue("ownerflag").equals("1")) roles.add(new Role("owner"));
        this.setProperty("CTIAgentID",userRecord.getStringValue("daks_agentid"));       
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

      return roles.iterator();
  }
  
  /**
   * toString methode: creates a String representation of the object
   * @return the String representation
   * @author info.vancauwenberge.tostring plugin
  
   */
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
