/*
 * Created on Jul 23, 2004
 *
 */
package jacob.alert;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.SessionContext;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.entrypoint.EntryPointUrl;
import de.tif.jacob.messaging.alert.IAlertItem;


/**
 *
 */
public class Bookmark extends IAlertItem
{
  static public final transient String RCS_ID = "$Id: Bookmark.java,v 1.1 2005/06/06 12:53:05 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  final private String message;
  final private Date   date;
  final private String sender;
  final private String qwkey;
  final private String pkey;
  final private String table;
  final private Severity severity;
  
  /**
   * 
   */
  public Bookmark(IDataTableRecord alert) throws Exception
  {
    this.message= alert.getSaveStringValue("message");
    this.date   = alert.getTimestampValue("dateposted");
    this.sender = alert.getStringValue("sender");
    this.qwkey  = alert.getStringValue("qwkey");
    this.pkey= alert.getStringValue("tablekey");
    this.table= alert.getStringValue("tablename");
    switch (alert.getintValue("severity"))
    {
  	case 0: // 1-Normal
  	  this.severity=DEFAULT;
  	  break;
  	case 1: // 2-Kritisch
  	  this.severity=LOW;
  	  break;
  	case 2: // 3-Produktion
  	  this.severity=MEDIUM;
  	  break;
  	case 3: // 4-Notfall
  	  this.severity=HIGH;
  	  break;
  	case 4: // DEFAULT
  	  this.severity=DEFAULT;
  	  break;
 	  default:
 	    this.severity=DEFAULT;
    }  
  }


  /* 
   * @see de.tif.jacob.alert.IAlertItem#delete(de.tif.jacob.core.Context)
   */
  public void delete(Context context) throws Exception
  {
    IDataTransaction trans = context.getDataAccessor().newTransaction();
    
    try
    {
      IDataTable alerts=context.getDataTable("qw_alert");
      alerts.qbeClear();
      alerts.qbeSetValue("qwkey",qwkey);
      alerts.searchAndDelete(trans);
      trans.commit();
    }
    finally
    {
      trans.close();
    }
  }

  /*
   * @see de.tif.jacob.alert.IAlertItem#getKey()
   */
  public String getKey()
  {
    return qwkey;
  }
  
  /* 
   * @see de.tif.jacob.alert.IAlertItem#getDate()
   */
  public Date getDate() throws Exception
  {
    return date;
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#getMessage()
   */
  public String getMessage() throws Exception
  {
    return message;
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#getSender()
   */
  public String getSender() throws Exception
  {
    return sender;
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#getSeverity()
   */
  public Severity getSeverity() throws Exception
  {
    return severity;
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#isDeleteable()
   */
  public boolean isDeleteable() throws Exception
  {
    return true;
  }

  /**
   * toString methode: creates a String representation of the object
   * @return the String representation
   * @author info.vancauwenberge.tostring plugin
  
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("UserAlertItem[");
    buffer.append("message = ").append(message);
    buffer.append(", date = ").append(date);
    buffer.append(", sender = ").append(sender);
    buffer.append(", qwkey = ").append(qwkey);
    buffer.append("]");
    return buffer.toString();
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#getDisplayUrl(de.tif.jacob.core.Context)
   */
  public URL getDisplayUrl(SessionContext context)
  {
    try
    {
      Properties props=new Properties();
      props.put("pkey", pkey );
      if ("call".equals(table))
          return new URL(new EntryPointUrl(EntryPointUrl.ENTRYPOINT_GUI,"ShowCall",props).toURL(context));
      else
          return new URL(new EntryPointUrl(EntryPointUrl.ENTRYPOINT_GUI,"ShowTask",props).toURL(context));
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    return null;
  }
}
