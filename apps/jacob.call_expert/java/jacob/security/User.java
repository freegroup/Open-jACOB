/*
 * Created on Mar 1, 2004
 *
 */
package jacob.security;

import jacob.model.Person;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.InvalidNewPasswordException;
import de.tif.jacob.security.impl.AbstractUser;

/**
 * @author andreas
 *
 */
public class User extends AbstractUser
{
    static public final transient String RCS_ID = "$Id: User.java,v 1.3 2009/11/23 11:33:43 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.3 $";

    private final String loginId;
    private final String fullName;
    private final String key;
    private final String email;
    private final String mandatorId;

    private String password;

    private final List roles = new ArrayList();
    UserFactory factory;
    /**
     * Constructs a user instance from an employee table record.
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
        this.factory = factory;
        try
        {
            // set user attributes
            //
            this.key = userRecord.getStringValue(Person.loginname);
            this.fullName = userRecord.getStringValue(Person.fullname);
            this.loginId = userRecord.getStringValue(Person.loginname);
            this.email = userRecord.getStringValue(Person.email);
            this.password = userRecord.getStringValue(Person.password);

            if(userRecord.getbooleanValue(Person.has_administration))
                this.roles.add(Role.ADMIN);

            if(userRecord.getbooleanValue(Person.has_configuration))
                this.roles.add(Role.CONFIGURATION);

            if(userRecord.getbooleanValue(Person.has_debugging))
                this.roles.add(Role.DEBUGGING);

            // determine user roles
            //
            if (loginId.equals(UserFactory.ADMIN_USER_ID))
            {
                this.roles.add(Role.ADMIN);
                this.mandatorId=null;
            }
            else
            {
                this.roles.add(Role.USER);
                this.mandatorId=loginId;
            }
        }
        catch (Exception e)
        {
            throw new GeneralSecurityException(e.toString());
        }
    }


    public String getMandatorId()
    {
        return mandatorId;
    }


    /* (non-Javadoc)
     * @see de.tif.jacob.security.IUser#setPassword(java.lang.String)
     */
    public void setPassword(String newPassword) throws InvalidNewPasswordException, Exception
    {
        // do not allow empty passwords
        if (null == newPassword || newPassword.length() == 0)
            throw new InvalidNewPasswordException();

        // fetch user record
        //
        IDataAccessor accessor = this.factory.newDataAccessor();
        IDataTable table = accessor.getTable(Person.NAME);
        table.qbeSetKeyValue(Person.loginname, this.key);
        table.search();
        if (table.recordCount() != 1)
        {
            throw new Exception("Cannot find user with key " + this.key);
        }

        // determine password hash
        // store new password hash
        //
        IDataTransaction trans = accessor.newTransaction();
        try
        {
            table.getRecord(0).setValue(trans, Person.password, newPassword);
            trans.commit();
        }
        finally
        {
            trans.close();
        }

        // set new password hash after success
        this.password = newPassword;
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.security.IUser#getLoginId()
     */
    public String getLoginId()
    {
        return loginId;
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.security.IUser#getFullName()
     */
    public String getFullName()
    {
        return fullName;
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.security.IUser#getKey()
     */
    public String getKey()
    {
        return key;
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.security.impl.AbstractUser#fetchRoles()
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


    /**
     * Verifies whether the given password is valid.
     *
     * @param password
     *          the password
     * @return <code>true</code> on success, otherwise <code>false</code>
     */
    protected boolean verifyAccess(String password)
    {
        // otherwise compare password hashs
        return this.password==null || this.password.equals(password);
    }
}
