/*
 * Created on Jul 23, 2004
 *
 */
package jacob.alert;

import jacob.common.ENUM;
import jacob.entrypoint.gui.ShowCall;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import de.tif.jacob.messaging.alert.*;
import de.tif.jacob.util.clazz.ClassUtil;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.SessionContext;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.entrypoint.EntryPointUrl;

/**
 *
 */
public class UserAlertItem extends IAlertItem
{
  static public final transient String RCS_ID = "$Id: UserAlertItem.java,v 1.2 2005/09/09 16:55:40 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  final private String message;
  final private Date   date;
  final private String sender;
  final private String key;
  final private Severity severity;
  
  /**
   * 
   */
  public UserAlertItem(IDataTableRecord call) throws Exception
  {
    this.message   = "Meldung "+ call.getSaveStringValue("pkey")+ " " + call.getSaveStringValue("problem");
    this.date      = call.getTimestampValue("datereported");
    this.sender    = "jACOB";
    this.key     = call.getStringValue("pkey");
    String priority = call.getStringValue("priority");
   
  	if (ENUM.CALLPRIORITY_P1.equals(priority))
  	  this.severity=LOW;
    else if (ENUM.CALLPRIORITY_P2.equals(priority))
  	  this.severity=MEDIUM;
    else if (ENUM.CALLPRIORITY_P3.equals(priority))
  	  this.severity=HIGH;
    else if (ENUM.CALLPRIORITY_P4.equals(priority))
  	  this.severity=HIGH;
    else if (ENUM.CALLPRIORITY_P5.equals(priority))
  	  this.severity=DEFAULT;
    else
 	    this.severity=DEFAULT;
    
  }


  /* 
   * @see de.tif.jacob.alert.IAlertItem#delete(de.tif.jacob.core.Context)
   */
  public void delete(Context context) throws Exception
  {
 
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
    return false;
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
    buffer.append(", qwkey = ").append(key);
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
        props.put("pkey", key );
        return new URL(new EntryPointUrl(EntryPointUrl.ENTRYPOINT_GUI,ClassUtil.getShortClassName(ShowCall.class),props).toURL(context));
      }
      catch (MalformedURLException e)
      {
        e.printStackTrace();
      }
      return null;
  }
}
