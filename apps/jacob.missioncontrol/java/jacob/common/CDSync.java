/*
 * Created on 30.08.2005
 */
package jacob.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.naming.SizeLimitExceededException;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.core.exception.UserRuntimeException;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;

/**
 * Helper class which provides sync with DCX corporate directory.
 * @author Andreas Sonntag
 */
public class CDSync
{
  public static void showDetails (IClientContext context, String uid) throws Exception
  {
    IDataAccessor accessor = context.getDataAccessor().newAccessor();
    IDataTable cdPersonTable = accessor.getTable("cdperson");
    cdPersonTable.qbeSetKeyValue("uid", uid);
    cdPersonTable.search();
    IDataTableRecord personRecord = cdPersonTable.getSelectedRecord();
    if (personRecord == null)
      throw new UserException("User '"+uid+"' not found in corporate directory");
    
    
    FormLayout layout = new FormLayout("10dlu,10dlu,p,2dlu,p,10dlu", // 3 columns
    "10dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,10dlu"); // 5 rows
  		CellConstraints c=new CellConstraints();
  		

  	  IFormDialog dialog=context.createFormDialog("Title",layout,null);
	dialog.addHeader("Organizational data:",c.xywh(1,1,4,1));
		int y = 3;
		dialog.addLabel("Organization:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("o"),c.xy(4,y)); y+=2;
		dialog.addLabel("Organizational Unit:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("ou"),c.xy(4,y)); y+=2;
		dialog.addLabel("Business Field:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("dcxBusinessAllocationLevel1"),c.xy(4,y)); y+=2;
		dialog.addLabel("Business Unit:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("businessCategory"),c.xy(4,y)); y+=2;
		dialog.addLabel("Company identifier:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("dcxCompanyID"),c.xy(4,y)); y+=2;
		dialog.addLabel("Cost center:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("dcxCostCenter"),c.xy(4,y)); y+=2;
		dialog.addLabel("Department:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("department"),c.xy(4,y)); y+=2;
		dialog.addLabel("Department description:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("dcxDepartmentName"),c.xy(4,y)); y+=2;
		
	dialog.addHeader("Personal data:",c.xywh(1,y,4,1)); y+=2;
		dialog.addLabel("Common name:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("cn"),c.xy(4,y)); y+=2;
		dialog.addLabel("CD User ID:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("uid"),c.xy(4,y)); y+=2;
		dialog.addLabel("DC internal postoffice code:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("dcxInternalPostcode"),c.xy(4,y)); y+=2;
		dialog.addLabel("Postal Address:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("dcxTmpVisitorsAddress"),c.xy(4,y)); y+=2;
		dialog.addLabel("Visitors Address:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("dcxVisitorsAddress"),c.xy(4,y)); y+=2;
		dialog.addLabel("Building:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("dcxBuildingName"),c.xy(4,y)); y+=2;
		dialog.addLabel("Room:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("roomNumber"),c.xy(4,y)); y+=2;
		dialog.addLabel("Phone:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("phone"),c.xy(4,y)); y+=2;
		dialog.addLabel("Mobile:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("mobile"),c.xy(4,y)); y+=2;
		dialog.addLabel("Department fax:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("dcxFaxNumber"),c.xy(4,y)); y+=2;
		dialog.addLabel("Personal fax:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("dcxPersonalFaxNumber"),c.xy(4,y)); y+=2;
		dialog.addLabel("Email:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("email"),c.xy(4,y)); y+=2;
		dialog.addLabel("Free text memo:",c.xy(2,y)); dialog.addLabel(personRecord.getSaveStringValue("dcxNote"),c.xy(4,y)); y+=2;
  	  dialog.setCancelButton(I18N.getLocalized("BUTTON_COMMON_CLOSE", context));
//  	  dialog.setDebug(true);
  	  dialog.show();
  }
  
  public static boolean sync(IClientContext context, String firstname, String lastname, String department, String phone, String mobile, String plant, String plant2, String email,
      String cuid, IDataTableRecord orgRec) throws Exception
  {
    IDataAccessor accessor = context.getDataAccessor().newAccessor();
    IDataTable cdPersonTable = accessor.getTable("cdperson");
    IDataTable personTable = accessor.getTable("person");
    cdPersonTable.qbeClear();
    personTable.qbeClear();

    boolean constraint = false;
    if (!"".equals(firstname))
    {
      cdPersonTable.qbeSetValue("firstname", firstname);
      constraint = true;
    }
    if (!"".equals(lastname))
    {
      cdPersonTable.qbeSetValue("lastname", lastname);
      constraint = true;
    }
    if (!"".equals(department))
    {
      cdPersonTable.qbeSetValue("department", department);
      constraint = true;
    }
    if (!"".equals(phone))
    {
      cdPersonTable.qbeSetValue("phone", phone);
      constraint = true;
    }
    if (!"".equals(mobile))
    {
      cdPersonTable.qbeSetValue("mobile", mobile);
      constraint = true;
    }
    if (!"".equals(plant))
    {
      cdPersonTable.qbeSetValue("plant", plant);
      constraint = true;
    }
    if (!"".equals(plant2))
    {
      cdPersonTable.qbeSetValue("plant2", plant2);
      constraint = true;
    }
    if (!"".equals(email))
    {
      cdPersonTable.qbeSetValue("email", email);
      constraint = true;
    }
    if (!"".equals(cuid))
    {
      cdPersonTable.qbeSetValue("uid", cuid);
      constraint = true;
    }
    // an organisation is backfilled and the organisation is mapped to a ldap organisation?
    if (orgRec != null && orgRec.getValue("cdorgid") !=null)
    {
      cdPersonTable.qbeSetKeyValue("o", orgRec.getValue("cdorgid"));
      constraint = true;
    }

    // do not perform unconstrained searches on CD
    //
    if (!constraint)
    {
      context.createMessageDialog("Insufficient search constraints: No synchronization with corporate directory has been performed").show();
      return false;
    }

    // always limit resultset to 100!
    cdPersonTable.setMaxRecords(100);

    // search on corporate directory
    try
    {
      cdPersonTable.search();
    }
    catch (Exception ex)
    {
      Throwable cause = ex.getCause();
      if (cause != null && cause instanceof SizeLimitExceededException)
      {
        throw new UserRuntimeException("Too many records: Please perform a more constrained search");
      }
      context.createMessageDialog("Access to CD failed: No synchronization with corporate directory has been performed").show();
      ExceptionHandler.handle(ex);
      return false;
    }

    Map cdPersonMap = new HashMap();
    final int COMBINED_UID_NUM = 20;
    int j = 0;
    IDataTransaction transaction = accessor.newTransaction();
    try
    {
      for (int i = 0; i < cdPersonTable.recordCount(); i++)
      {
        IDataTableRecord cdPerson = cdPersonTable.getRecord(i);
        String uid = cdPerson.getStringValue("uid").toUpperCase();
        cdPersonMap.put(uid, cdPerson);

        personTable.qbeSetKeyValue("uid", uid);
        j++;

        if (j >= COMBINED_UID_NUM)
        {
          // search locally
          personTable.search();
          
          // update already existing
          //
          for (int n = 0; n < personTable.recordCount(); n++)
          {
            IDataTableRecord person = personTable.getRecord(n);
            uid = person.getStringValue("uid").toUpperCase();
            cdPerson = (IDataTableRecord) cdPersonMap.remove(uid);
            sync(transaction, cdPerson, person);
          }
          
          // insert not existing
          //
          Iterator iter =  cdPersonMap.values().iterator();
          while (iter.hasNext())
          {
            IDataTableRecord person = personTable.newRecord(transaction);
            cdPerson = (IDataTableRecord) iter.next();
            sync(transaction, cdPerson, person);
          }
          
          // commit this bundle
          transaction.commit();
          
          transaction = accessor.newTransaction();
          cdPersonMap.clear();
          personTable.qbeClear();
          j = 0;
        }
      }

      // handle remaining
      //
      if (j > 0)
      {
        // search locally
        personTable.search();
        
        // update already existing
        //
        for (int n = 0; n < personTable.recordCount(); n++)
        {
          IDataTableRecord person = personTable.getRecord(n);
          String uid = person.getStringValue("uid").toUpperCase();
          IDataTableRecord cdPerson = (IDataTableRecord) cdPersonMap.remove(uid);
          sync(transaction, cdPerson, person);
        }
        
        // insert not existing
        //
        Iterator iter =  cdPersonMap.values().iterator();
        while (iter.hasNext())
        {
          IDataTableRecord person = personTable.newRecord(transaction);
          IDataTableRecord cdPerson = (IDataTableRecord) iter.next();
          sync(transaction, cdPerson, person);
        }
        
        // commit remaining
        transaction.commit();
      }
    }
    finally
    {
      transaction.close();
    }

    return true;
  }

  private static void sync(IDataTransaction transaction, IDataTableRecord cdPerson, IDataTableRecord person) throws Exception
  {
    person.setStringValue(transaction, "uid", cdPerson.getStringValue("uid").toUpperCase());
    person.setStringValueWithTruncation(transaction, "department", cdPerson.getStringValue("department"));
    person.setStringValueWithTruncation(transaction, "email", cdPerson.getStringValue("email"));
    person.setStringValueWithTruncation(transaction, "firstname", cdPerson.getStringValue("firstname"));
    person.setStringValueWithTruncation(transaction, "lastname", cdPerson.getStringValue("lastname"));
    person.setStringValueWithTruncation(transaction, "mobile", cdPerson.getStringValue("mobile"));
    person.setStringValueWithTruncation(transaction, "phone", cdPerson.getStringValue("phone"));
    person.setStringValueWithTruncation(transaction, "plant", cdPerson.getStringValue("plant"));
    person.setStringValueWithTruncation(transaction, "plant2", cdPerson.getStringValue("plant2"));
    boolean internal = cdPerson.getStringValue("objectClass").indexOf("dcxInternalEmployee") != -1;
    person.setIntValue(transaction, "internal", internal ? 1 : 0);
    
    String cdorg = cdPerson.getStringValue("o");
    if (cdorg != null)
    {
      // fetch appropriate organisation entry, if not already fetched
      //
      IDataTable orgTable = cdPerson.getAccessor().getTable("organization");
      IDataTableRecord orgRec = orgTable.getSelectedRecord();
      if (orgRec == null || !cdorg.equals(orgRec.getStringValue("cdorgid")))
      {
        orgTable.qbeClear();
        orgTable.qbeSetKeyValue("cdorgid", cdorg);
        orgTable.search();
        orgRec = orgTable.getSelectedRecord();
      }
      
      // appropriate organisation entry existing?
      if (orgRec != null)
      {
        boolean orgdcinternal = orgRec.getintValue("dcinternal")==1;
        
        // only link if both are internal or both are external
        if (!(orgdcinternal ^ internal))
        {
          // link person with organisation
          person.setValue(transaction, "organization_key", orgRec.getValue("pkey"));
        }
      }
    }
    
  }

}
