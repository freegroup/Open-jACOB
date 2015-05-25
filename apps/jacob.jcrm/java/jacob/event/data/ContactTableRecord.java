/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import java.util.Locale;

import jacob.exception.BusinessException;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.i18n.ApplicationMessage;

/**
 *
 * @author andreas
 */
public class ContactTableRecord extends DataTableRecordEventHandler
{

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
   */
  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
   */
  public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    if (tableRecord.isDeleted())
      return;

    // Check whether the email is unique
    //
    if (tableRecord.hasChangedValue("email"))
    {
      // normalize email
      tableRecord.setValue(transaction, "email", tableRecord.getSaveStringValue("email").trim().toLowerCase());
      
      String email = tableRecord.getStringValue("email");
      if (email != null && tableRecord.getTable().exists("email", email))
      {
        throw new BusinessException(new ApplicationMessage("EMAIL_NOT_UNIQUE", email));
      }
    }

    String firstName = tableRecord.getSaveStringValue("firstname");
    String lastName = tableRecord.getSaveStringValue("lastname");
    String fullName;

    // Set the fullname from first and last name
    //
    if (firstName.length() > 0)
    {
      fullName = firstName + " " + lastName;
      tableRecord.setValue(transaction, "fullname", fullName);
    }
    else
    {
      fullName = lastName;
      tableRecord.setValue(transaction, "fullname", fullName);
    }

    // Build salutation fields
    //
    String mr_ms = tableRecord.getSaveStringValue("mr_ms");
    String salutationHeaderKey;
    String salutationKey;
    if ("Mr.".equals(mr_ms))
    {
      salutationHeaderKey = "SalutationHeader.Male";
      salutationKey = "Salutation.Male";
    }
    else if ("Mrs.".equals(mr_ms))
    {
      salutationHeaderKey = "SalutationHeader.Female";
      salutationKey = "Salutation.Female";
    }
    else
    {
      salutationHeaderKey = "SalutationHeader.Unknown";
      salutationKey = "Salutation.Unknown";
    }
    
    String title = tableRecord.getSaveStringValue("title");
    if (title.length() > 0)
      title = title + " ";
    
    // Since letter templates should all be within the same language, i.e. which should be
    // conform with the setup application locale
    Locale applicationLocale = Context.getCurrent().getApplicationLocale();
    
    tableRecord.setValue(transaction, "salutationheader", ApplicationMessage.getLocalized(salutationHeaderKey, fullName, title, applicationLocale));
    tableRecord.setValue(transaction, "salutation", ApplicationMessage.getLocalized(salutationKey, lastName, title, applicationLocale));

    // Set modification time stamp
    //
    tableRecord.setValue(transaction, "datemodified", "now");
  }
}
