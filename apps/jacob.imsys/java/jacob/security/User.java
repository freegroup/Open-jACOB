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
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.InvalidNewPasswordException;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.jacob.security.impl.AbstractUser;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas Herz / Mike Doering
 * 
 */
public class User extends AbstractUser
{
    static public final transient String RCS_ID = "$Id: User.java,v 1.12 2005/11/02 12:48:25 mike Exp $";

    static public final transient String RCS_REV = "$Revision: 1.12 $";

    private String loginId;

    private String fullName;

    private String key;

    private String email;

    private String cellPhone;

    private String phone;

    private String fax;

    private String hashvalue;

    private boolean availability;
    
    private List roles = new ArrayList();

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

    protected User(IDataAccessor dataAccessor, String loginId) throws GeneralSecurityException, UserNotExistingException
    {
        try
        {
            IDataTable table = dataAccessor.getTable("employee");

            // Daimler ist case insensitive
            table.qbeSetKeyValue("loginname", loginId.toUpperCase());

            table.search();
            if (table.recordCount() == 1)
            {
                IDataTableRecord userRecord = table.getRecord(0);
                init(userRecord);
            }
            else
            {
                throw new UserNotExistingException(loginId);
            }
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

        if (null == newPassword || newPassword.length() == 0)
            throw new InvalidNewPasswordException();
        Context context = Context.getCurrent();
        IDataTable table = context.getDataTable("employee");
        table.qbeSetValue("pkey", this.key);

        table.search();
        if (table.recordCount() == 1)
        {

            IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
            try
            {
                String hashCode = createHashcode(newPassword);
                table.getRecord(0).setValue(currentTransaction, "passwdhash", hashCode);
                currentTransaction.commit();
                this.hashvalue = hashCode;
            }
            finally
            {
                currentTransaction.close();
            }
        }
    }

    /**
     * 
     * @param userRecord
     * @throws Exception
     */
    private void init(IDataTableRecord userRecord) throws Exception
    {
        this.key = userRecord.getStringValue("pkey");
        this.fullName = userRecord.getStringValue("fullname");
        this.loginId = userRecord.getStringValue("loginname");
        this.email = userRecord.getStringValue("emailcorr");
        this.cellPhone = userRecord.getStringValue("mobilecorr");
        this.phone = userRecord.getStringValue("phonecorr");
        this.fax = userRecord.getStringValue("faxcorr");
        this.hashvalue = userRecord.getStringValue("passwdhash");
        this.availability = (userRecord.getStringValue("availability").equals("1"));
        if (userRecord.getStringValue("admin_role").equals("1")) roles.add(new Role("cq_admin"));
        if (userRecord.getStringValue("ak_role").equals("1")) roles.add(new Role("cq_ak"));
        if (userRecord.getStringValue("supportrole").equals("1")) roles.add(new Role("cq_agent"));
        if (userRecord.getStringValue("pm_role").equals("1")) roles.add(new Role("cq_pm"));
        if (userRecord.getStringValue("warte_role").equals("1")) roles.add(new Role("cq_warte"));
        if (userRecord.getStringValue("sdadmin_role").equals("1")) roles.add(new Role("cq_sdadmin"));
        
        List userRecordFields =userRecord.getTable().getTableAlias().getTableDefinition().getTableFields();
        Iterator iter = userRecordFields.iterator();
        while (iter.hasNext())
        {
            ITableField field = (ITableField)iter.next();
            this.setProperty(field.getName(),userRecord.getSaveStringValue(field.getName()));
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
     * Lacy instantiation of the user roles. The roles are not neccessary for
     * the common usage of this class. At the moment only for the login
     * procedure.
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

        // Wenn der User nicht verfügbar ist, dann kein Zugriff gewähren
        if (!this.availability || null == password || password.length() == 0)
            return false;

        // Wenn hashvalue leer ist dann Defaultwert LoginID

        if (this.hashvalue == null)
            return password.toUpperCase().equals(this.loginId.toUpperCase());

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
