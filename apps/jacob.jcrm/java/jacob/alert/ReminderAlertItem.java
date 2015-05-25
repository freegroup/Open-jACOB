/*
 * Created on 26.09.2005
 *
 */
package jacob.alert;

import java.net.URL;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.SessionContext;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.entrypoint.EntryPointUrl;
import de.tif.jacob.messaging.alert.IAlertItem;

/**
 * @author andreas
 *
 */
public class ReminderAlertItem extends IAlertItem
{
  static public final transient String RCS_ID = "$Id: ReminderAlertItem.java,v 1.1 2005/10/12 15:19:21 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  final private String key;
  private final IDataTableRecord reminderalert;
  
  /**
   * 
   */
  public ReminderAlertItem(IDataTableRecord reminderalert) throws Exception
  {
    this.key = "Reminder" + reminderalert.getStringValue("pkey");
    this.reminderalert = reminderalert;
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#delete(de.tif.jacob.core.Context)
   */
  public void delete(Context context) throws Exception
  {
    IDataTable table = context.getDataAccessor().getTable(this.reminderalert.getTableAlias());
    table.qbeClear();
    table.qbeSetKeyValue(this.reminderalert.getTableAlias().getTableDefinition().getPrimaryKey(), this.reminderalert.getPrimaryKeyValue());
    IDataTransaction transaction = context.getDataAccessor().newTransaction();
    try
    {
      table.fastDelete(transaction);
      transaction.commit();
    }
    finally
    {
      transaction.close();
    }
  }

  /*
   * @see de.tif.jacob.alert.IAlertItem#getKey()
   */
  public String getKey()
  {
    return key;
  }
  
  /* 
   * @see de.tif.jacob.alert.IAlertItem#getDate()
   */
  public Date getDate() throws Exception
  {
    return reminderalert.getDateValue("dateposted");
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#getMessage()
   */
  public String getMessage() throws Exception
  {
    return reminderalert.getStringValue("message");
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#getSender()
   */
  public String getSender() throws Exception
  {
    return "none";
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#getSeverity()
   */
  public Severity getSeverity() throws Exception
  {
    return DEFAULT;
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#isDeleteable()
   */
  public boolean isDeleteable() throws Exception
  {
    return true;
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#getDisplayUrl(de.tif.jacob.core.Context)
   */
  public URL getDisplayUrl(SessionContext context)
  {
    try
    {
      Properties props = new Properties();
      props.put("tablekey", this.reminderalert.getStringValue("tablekey"));
      return new URL(new EntryPointUrl(EntryPointUrl.ENTRYPOINT_GUI, "Show" + StringUtils.capitalise(this.reminderalert.getStringValue("tablename")), props)
          .toURL(context));
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
}
