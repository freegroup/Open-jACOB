/*
 * Created on 20.05.2005 by mike
 * 
 *
 */
package jacob.reminder;

import java.util.Date;

import de.tif.jacob.core.data.IDataTableRecord;

/**
 * @author mike
 * 
 */
public abstract class IReminder
{
  public static final int WORKGROUP = -1;
  public static final int OWNER = 0;
  public static final int ALERT = 1;
  public static final int EMAIL = 2;
  public static final int FAX = 3;
  public static final int SMS = 4;

  IDataTableRecord contextRecord = null; // der Datensatz für die wiedervorlage

  int method = OWNER; // siehe Konstanten

  String msg = null; // die Nachricht selbst
  Date when = null; // wann wird der Reminder eskaliert
  IDataTableRecord recipient = null; // der Empfänger 

  /**
   * @return Returns the contextRecord.
   */
  public final IDataTableRecord getContextRecord()
  {
    return contextRecord;
  }

  /**
   * @param contextRecord The contextRecord to set.
   */
  public final void setContextRecord(IDataTableRecord contextRecord)
  {
    this.contextRecord = contextRecord;
  }

  /**
   * @return Returns the method.
   */
  public final int getMethod()
  {
    return method;
  }

  /**
   * @param method The method to set.
   */
  public final void setMethod(int method)
  {
    this.method = method;
  }

  /**
   * @return Returns the msg.
   */
  public final String getMsg()
  {
    return msg;
  }

  /**
   * @param msg The msg to set.
   */
  public final void setMsg(String msg)
  {
    this.msg = msg;
  }

  /**
   * @return Returns the recipient.
   */
  public final IDataTableRecord getRecipient()
  {
    return recipient;
  }

  /**
   * @param recipient The recipient to set.
   */
  public final void setRecipient(IDataTableRecord recipient)
  {
    this.recipient = recipient;
  }

  /**
   * @return Returns the when.
   */
  public final Date getWhen()
  {
    return when;
  }

  /**
   * @param when The when to set.
   */
  public final void setWhen(Date when)
  {
    this.when = when;
  }

  public abstract void schedule() throws Exception;

  public abstract void delete(IDataTableRecord contextRecord) throws Exception;
}
