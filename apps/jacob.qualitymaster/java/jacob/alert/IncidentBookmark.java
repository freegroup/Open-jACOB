/*
 * Created on Jul 23, 2004
 *
 */
package jacob.alert;

import jacob.entrypoint.gui.ShowIncident;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.SessionContext;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.entrypoint.EntryPointUrl;
import de.tif.jacob.messaging.alert.IAlertItem;
import de.tif.jacob.util.clazz.ClassUtil;


/**
 *
 */
public class IncidentBookmark extends IAlertItem
{
  static public final transient String RCS_ID = "$Id: IncidentBookmark.java,v 1.3 2005/09/11 12:03:24 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
  final private String   subject;
  final private String   pkey;
  final private Severity severity;
  final private Date     createDate;
  
  /**
   * 
   */
  public IncidentBookmark(Context context, IDataTableRecord alert) throws Exception
  {
    this.pkey       = alert.getSaveStringValue("pkey");
    this.subject    = alert.getSaveStringValue("subject");
    this.createDate = alert.getTimestampValue("create_date");

    long distance = System.currentTimeMillis()-createDate.getTime();
    int day = (int)(distance/(1000.0*60*60*24));
    switch (day)
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
 	  default:
  	  this.severity=HIGH;
    }  
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
    return pkey;
  }
  
  /* 
   * @see de.tif.jacob.alert.IAlertItem#getDate()
   */
  public Date getDate() throws Exception
  {
    return createDate;
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#getMessage()
   */
  public String getMessage() throws Exception
  {
    return "Incident "+pkey+": "+subject;
  }

  /* 
   * @see de.tif.jacob.alert.IAlertItem#getSender()
   */
  public String getSender() throws Exception
  {
    return "[Escalation]";
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

  /* 
   * @see de.tif.jacob.alert.IAlertItem#getDisplayUrl(de.tif.jacob.core.Context)
   */
  /* 
   * @see de.tif.jacob.alert.IAlertItem#getDisplayUrl(de.tif.jacob.core.Context)
   */
  public URL getDisplayUrl(SessionContext context)
  {
      try
      {
        Properties props=new Properties();
        props.put("pkey", pkey );
        return new URL(new EntryPointUrl(EntryPointUrl.ENTRYPOINT_GUI,ClassUtil.getShortClassName(ShowIncident.class),props).toURL(context));
      }
      catch (MalformedURLException e)
      {
        e.printStackTrace();
      }
      return null;
  }
}
