/*
 * Created on May 1, 2004
 *  
 */
package jacob.security;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.Main;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.security.IRole;
import de.tif.jacob.security.IUser;
import de.tif.jacob.security.impl.AbstractUser;
/**
 * @author Andreas Herz / Mike Doering
 *  
 */
public class User extends AbstractUser
{
	final String loginId;
	final String fullName;
	final String key;
	final String email;
	
  private final List roles = new ArrayList();
  
	public User(IApplicationDefinition appDef, String loginId) throws GeneralSecurityException
	{
		loginId=loginId.toUpperCase();
		IDataAccessor dataAccessor = new IDataAccessor(appDef);
		IDataTable table = dataAccessor.getTable("employee");

    try
    {
    	// Daimler ist case insensitive
    	table.qbeSetValue("userid", "=" + loginId.toUpperCase() +"|="+loginId.toLowerCase());
    	
    	if (table.search() == 1)
      {
          IDataTableRecord userRecord = table.getTableRecord(0);
      		this.key      = userRecord.getStringValue("pkey");
      		this.fullName = userRecord.getStringValue("fullname");
          this.loginId  = userRecord.getStringValue("userid");
          this.email    = userRecord.getStringValue("emailcorr");
      }
      else
      {
        throw new GeneralSecurityException("Benutzer ist dem System nicht bekannt");  
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

	/* 
   * @see de.tif.jacob.security.IUser#getEMail()
   */
  public String getEMail()
  {
    return email;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.security.impl.AbstractUser#fetchRoles()
   */
  protected Iterator fetchRoles() throws GeneralSecurityException
  {
    return this.roles.iterator();
  }

}
