/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import jacob.common.license.Constants;
import jacob.common.license.LicenseGenerator;
import jacob.model.Engine;
import jacob.model.Licensee;
import jacob.model.Licensehistory;

import java.util.Date;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.license.License;

/**
 * 
 * @author andherz
 */
public class LicenseTableRecord extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: LicenseTableRecord.java,v 1.2 2006/03/07 19:20:35 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  /**
   * Returns the cached private key for the desired engine
   * 
   * @param enginePkey the id of the engine
   * @return the private key or <code>null</code>
   */
  public static byte[] getPrivateKey(long enginePkey)
  {
    return (byte[]) Context.getCurrent().getUser().getProperty(Constants.PRIVATE_KEY+enginePkey);
  }

  /**
   * Sets the private key within user cache.
   * 
   * @param enginePkey the id of the engine
   * @param privatekey the private key
   */
  public static void setPrivateKey(long enginePkey, byte[] privatekey)
  {
    if (privatekey == null)
      throw new NullPointerException("privatekey is null");
    
    Context.getCurrent().getUser().setProperty(Constants.PRIVATE_KEY+enginePkey, privatekey);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
   */
  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    IDataTableRecord engineRecord = tableRecord.getAccessor().getTable(Engine.NAME).getSelectedRecord();
    String type = engineRecord == null ? jacob.model.License.type_ENUM._simple : engineRecord.getStringValue(Engine.license_type);
    tableRecord.setValue(transaction, jacob.model.License.type, type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void beforeCommitAction(IDataTableRecord licenseRecord, IDataTransaction transaction) throws Exception
  {
    if (licenseRecord.isDeleted())
    {
      throw new UnsupportedOperationException("License cannot be deleted");
    }

    IDataTableRecord engineRecord = licenseRecord.getLinkedRecord(Engine.NAME);
    IDataTableRecord licenseeRecord = licenseRecord.getLinkedRecord(Licensee.NAME);
    if (licenseeRecord == null)
    {
      // commit will fail because this is a required association
      return;
    }

    String company = licenseeRecord.getStringValue(Licensee.name);
    String eMail = licenseeRecord.getStringValue(Licensee.email_contact);
    long licenseNbr = licenseRecord.getlongValue(jacob.model.License.pkey);
    String description = licenseRecord.getStringValue(jacob.model.License.description);
    Date expiration = licenseRecord.getDateValue(jacob.model.License.expiration_date);
    int userCount = licenseRecord.getintValue(jacob.model.License.user_count);
    boolean demo = licenseRecord.getintValue(jacob.model.License.is_demo) != 0;

    boolean enhanced = jacob.model.License.type_ENUM._enhanced.equals(licenseRecord.getValue(jacob.model.License.type));
    
    // just in case demo value is not set (might occur because of disabeling GUI field)
    if (licenseRecord.hasNullValue(jacob.model.License.is_demo))
    {
      licenseRecord.setValue(transaction, jacob.model.License.is_demo, "0");
    }

    // some validity checks
    //
    if (expiration == null)
    {
      if (!enhanced)
        throw new UserException(new ApplicationMessage("LicenseTableRecord.ExpirationdateMissing"));
    }
    else
    {
      if (expiration.getTime() <= System.currentTimeMillis())
        throw new UserException(new ApplicationMessage("LicenseTableRecord.ExpirationdateNotInFuture"));
    }
    if (userCount <= 0)
    {
      // commit will fail because of minium 1 setting
      return;
    }
    if (description == null)
    {
      // commit will fail because description is required
      return;
    }
    
    if (licenseRecord.isNew())
    {
      // NEW
      
      // create license object
      //
      License license;
      if (enhanced)
      {
        license = new License(licenseNbr, company, eMail, expiration, userCount, demo, description);
      }
      else
      {
        license = new License(company, eMail, expiration, userCount, demo);
      }

      // generate license key
      //
      byte[] privateKey = getPrivateKey(engineRecord.getlongValue(Engine.pkey));
      String licEnc = LicenseGenerator.generateLicense(license.getLicenseInfo(), privateKey);
      licenseRecord.setValue(transaction, jacob.model.License.hash_key, licEnc);
    }
    else
    {
      // UPDATE
      boolean expirationChanged = licenseRecord.hasChangedValue(jacob.model.License.expiration_date);
      boolean userCountChanged = licenseRecord.hasChangedValue(jacob.model.License.user_count);
      
      if (enhanced && (expirationChanged || userCountChanged))
      {
        // create license history record to store old license key
        //
        IDataTableRecord licensehistoryRecord = licenseRecord.getAccessor().getTable(Licensehistory.NAME).newRecord(transaction);
        Object oldModifyDate = licenseRecord.getOldValue(jacob.model.License.modify_date);
        licensehistoryRecord.setValue(transaction, Licensehistory.create_date, oldModifyDate != null ? oldModifyDate : licenseRecord.getOldValue(jacob.model.License.create_date));
        licensehistoryRecord.setValue(transaction, Licensehistory.user_count, licenseRecord.getOldValue(jacob.model.License.user_count));
        licensehistoryRecord.setValue(transaction, Licensehistory.expiration_date, licenseRecord.getOldValue(jacob.model.License.expiration_date));
        licensehistoryRecord.setValue(transaction, Licensehistory.indefinite_flag, licenseRecord.getOldValue(jacob.model.License.indefinite_flag));
        licensehistoryRecord.setValue(transaction, Licensehistory.is_demo, licenseRecord.getOldValue(jacob.model.License.is_demo));
        licensehistoryRecord.setValue(transaction, Licensehistory.hash_key, licenseRecord.getOldValue(jacob.model.License.hash_key));
        licensehistoryRecord.setValue(transaction, Licensehistory.license_key, licenseRecord.getValue(jacob.model.License.pkey));
        
        // create license object
        //
        License license = new License(licenseNbr, company, eMail, expiration, userCount, demo, description);
        
        // generate new license key
        //
        byte[] privateKey = getPrivateKey(engineRecord.getlongValue(Engine.pkey));
        String licEnc = LicenseGenerator.generateLicense(license.getLicenseInfo(), privateKey);
        licenseRecord.setValue(transaction, jacob.model.License.hash_key, licEnc);
        licenseRecord.setValue(transaction, jacob.model.License.modify_date, "now");
      }
    }
  }
}
