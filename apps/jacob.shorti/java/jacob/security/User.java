package jacob.security;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.jacob.security.impl.AbstractUser;

/**
  * @author andherz
 *  
 */
public class User extends AbstractUser
{
	String loginId;
	String fullName;
	String passwd;
	String key;
  String email;
	final String mandatorid;

  
  private final List roles = new ArrayList();
  
	protected User(IApplicationDefinition appDef, String id) throws UserNotExistingException
  {
    this.loginId = id;

    this.key = "100";
    this.fullName = "Default User";
    this.loginId = "guest";
    this.email = null;
    this.passwd = null;
    this.mandatorid = null;
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
  	if(this.mandatorid == null)
  	{
  		roles.add(new Role("admin"));
  		roles.add(new Role("cq_admin"));
  	}
    return this.roles.iterator();
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
