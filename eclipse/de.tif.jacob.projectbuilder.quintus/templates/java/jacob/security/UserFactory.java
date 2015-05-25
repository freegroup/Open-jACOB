package jacob.security;

import java.security.GeneralSecurityException;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.jacob.security.IUser;
import de.tif.qes.QeSUserFactory;

/**
 */
public class UserFactory extends QeSUserFactory
{
  /* (non-Javadoc)
   * @see de.tif.qes.QeSUserFactory#newUser(de.tif.qes.QeSUserFactory, de.tif.jacob.core.data.IDataTableRecord)
   */
  protected IUser newUser(QeSUserFactory userFactory, IDataTableRecord userRecord) throws GeneralSecurityException
  {
    return new User(userFactory, userRecord);
  }
  
  /* (non-Javadoc)
   * @see de.tif.qes.QeSUserFactory#newUser(de.tif.qes.QeSUserFactory, java.lang.String)
   */
  protected IUser newUser(QeSUserFactory userFactory, String loginId) throws GeneralSecurityException, UserNotExistingException
  {
    return new User(userFactory, loginId);
  }
  
  /* (non-Javadoc)
   * @see de.tif.qes.QeSUserFactory#getNameOfEmployeeTableAlias()
   */
  public String getNameOfEmployeeTableAlias()
  {
    // could be changed if necessary
    return super.getNameOfEmployeeTableAlias();
  }
  
  /* (non-Javadoc)
   * @see de.tif.qes.QeSUserFactory#getNameOfEmailTableField()
   */
  public String getNameOfEmailTableField()
  {
    // could be changed if necessary
    return super.getNameOfEmailTableField();
  }
  
  /* (non-Javadoc)
   * @see de.tif.qes.QeSUserFactory#getNameOfFaxTableField()
   */
  public String getNameOfFaxTableField()
  {
    // could be changed if necessary
    return super.getNameOfFaxTableField();
  }
  
  /* (non-Javadoc)
   * @see de.tif.qes.QeSUserFactory#getNameOfFullnameTableField()
   */
  public String getNameOfFullnameTableField()
  {
    // could be changed if necessary
    return super.getNameOfFullnameTableField();
  }
  
  /* (non-Javadoc)
   * @see de.tif.qes.QeSUserFactory#getNameOfLoginnameTableField()
   */
  public String getNameOfLoginnameTableField()
  {
    // could be changed if necessary
    return super.getNameOfLoginnameTableField();
  }
  
  /* (non-Javadoc)
   * @see de.tif.qes.QeSUserFactory#getNameOfMobileTableField()
   */
  public String getNameOfMobileTableField()
  {
    // could be changed if necessary
    return super.getNameOfMobileTableField();
  }
  
  /* (non-Javadoc)
   * @see de.tif.qes.QeSUserFactory#getNameOfPhoneTableField()
   */
  public String getNameOfPhoneTableField()
  {
    // could be changed if necessary
    return super.getNameOfPhoneTableField();
  }

  /* (non-Javadoc)
   * @see de.tif.qes.QeSUserFactory#getNameOfPkeyTableField()
   */
  public String getNameOfPkeyTableField()
  {
    // could be changed if necessary
    return super.getNameOfPkeyTableField();
  }
}
