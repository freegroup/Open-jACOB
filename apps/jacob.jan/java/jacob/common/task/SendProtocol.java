package jacob.common.task;

import jacob.common.AppLogger;
import jacob.model.Jan_attachment;
import jacob.model.Jan_queue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataRecordSet;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IAdhocBrowserDefinition;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.messaging.Message;

public abstract class SendProtocol
{
  static public final transient String RCS_ID = "$Id: SendProtocol.java,v 1.3 2010-03-24 15:13:19 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  static protected final transient Log logger = AppLogger.getLogger();
  
  /**
   * Map(protocolName->Class)
   */
  private static final Map protocolMap = new HashMap();
  
  private final String protocolName;
  
  protected SendProtocol(String protocolName)
  {
    this.protocolName = protocolName;

    synchronized (protocolMap)
    {
      protocolMap.put(protocolName, this.getClass());
    }
  }

  /**
   * Returns the implemenation of a specified message send protocol.
   * 
   * @param protocolName the protocol name
   * @return the desired message send protocol implementation
   * @throws UserException if no such protocol exists
   */
  private static SendProtocol get(String protocolName) throws UserException
  {
    synchronized (protocolMap)
    {
      Class clazz = (Class) protocolMap.get(protocolName);
      
      if (clazz == null)
        throw new UserException("Unknown protocol '" + protocolName + "'");
      
      try
      {
        return (SendProtocol) clazz.newInstance();
      }
      catch (InstantiationException ex)
      {
        throw new RuntimeException(ex);
      }
      catch (IllegalAccessException ex)
      {
        throw new RuntimeException(ex);
      }
    }
  }

  /**
   * The name of the protocol.
   * 
   * @return the protocol name
   */
  public final String getProtocolName()
  {
    return this.protocolName;
  }

  /**
   * This method has to be overwritten by concret implementations to initialise
   * configuration.
   * <p>
   * Note: This method has to be invoked before calling any other method except
   * {@link #getProtocolName()}.
   * 
   * @param context
   *          the working context
   * @return <code>true</code> protocol has been successfully initialised,
   *         i.e. protocol is active, otherwise <code>false</code> if the
   *         protocol is inactive
   * @throws Exception
   *           if the configuration or initialisation could not be performed
   */
  protected abstract boolean initialize(Context context) throws Exception;

  protected boolean repeatSending()
  {
    return repeatMaxRetries() > 0;
  }

  /**
   * Returns the timespan in minutes which has to be elapsed before a new retry
   * is performed to send an already failed message.
   * <p>
   * Note: Default is 10 minutes.
   * 
   * @return the timespan in minutes
   */
  protected int repeatTimespanInMins()
  {
    return 10;
  }

  /**
   * Returns the maximum time per task loop in seconds which is used to retry
   * sending already failed messages.
   * 
   * @return the maximum repeat time
   */
  protected int repeatTaskTimeInSecs()
  {
    return 30;
  }

  /**
   * Returns the maximum number of retries to send a message.
   * <p>
   * Note: Default is 2 retries.
   * 
   * @return number of retries or <code>0</code> if resending of failed
   *         messages should not be done.
   */
  protected int repeatMaxRetries()
  {
    return 2;
  }

  /**
   * The concret implementation to send a given message.
   * 
   * @param message the message to send
   * @throws Exception if the sending fails
   */
  protected abstract void send(SendMessage message) throws Exception;

  /**
   * Sends a given jAN message record.
   * 
   * @param context
   *          the working context
   * @param messageRecord
   *          the jAN message record to send
   * @return <code>true</code> sending was successful, otherwise
   *         <code>false</code>
   * @throws RecordLockedException
   *           if the record is currently locked, i.e. possibly send in the
   *           background
   * @throws RecordNotFoundException
   *           if the record does not exist, i.e. the message has possibly
   *           already been sent
   * @throws UserException
   *           if unknown or inactive message send protocol
   */
  public static boolean send(Context context, IDataTableRecord messageRecord) throws RecordLockedException, RecordNotFoundException, UserException
  {
    try
    {
      String url = getUrl(messageRecord);
      int idx = url.indexOf(Message.PROTOCOL_HEADER_SUFFIX);
      if (idx < 1)
        throw new RuntimeException("Invalid URL: " + url);
      SendProtocol protocol = get(url.substring(0, idx));
      if (!protocol.initialize(context))
      {
        throw new UserException("Protocol '" + protocol.getProtocolName() + "' is not active!");
      }
      int messageKey = messageRecord.getintValue(Jan_queue.pkey);
      return protocol.send(messageKey, messageRecord, getAttachmentBrowserDefinition(messageRecord.getAccessor().getApplication()));
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (RecordLockedException ex)
    {
      throw ex;
    }
    catch (RecordNotFoundException ex)
    {
      throw ex;
    }
    catch (UserException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }
  
  private static String getUrl(IDataTableRecord messageRecord) throws Exception
  {
    String url = messageRecord.getStringValue(Jan_queue.urlcomplete);
    return url != null ? url : messageRecord.getStringValue(Jan_queue.url);
  }
  
  final boolean send(IDataBrowserRecord browserMessageRec, IAdhocBrowserDefinition attachmentBrowserDef) throws Exception
  {
    int messageKey = browserMessageRec.getintValue(0);
    try
    {
      return send(messageKey, browserMessageRec, attachmentBrowserDef);
    }
    catch (RecordLockedException ex)
    {
      if (logger.isWarnEnabled())
        logger.warn("Could not send message " + messageKey + ": " + ex.toString());

      // message locked -> skip
      return false;
    }
    catch (RecordNotFoundException ex)
    {
      if (logger.isWarnEnabled())
        logger.warn("Could not send message " + messageKey + ": " + ex.toString());

      // message already deleted (i.e. send) -> skip
      return false;
    }
  }
  
  private boolean send(int messageKey, IDataRecord originMessageRec, IAdhocBrowserDefinition attachmentBrowserDef) throws Exception, RecordLockedException, RecordNotFoundException
  {
    IDataAccessor accessor = originMessageRec.getAccessor();

    IDataTransaction trans = accessor.newTransaction();
    try
    {
      // lock message first to ensure that a message is only processed by one
      // task!
      trans.lock(originMessageRec);

      // Get the reloaded message afterwards
      //
      IDataTableRecord messageRecord = accessor.getTable(originMessageRec.getTableAlias()).loadRecord(originMessageRec.getPrimaryKeyValue());
      
      boolean success = false;
      try
      {
        // Build message including its attachments
        //
        String url = getUrl(messageRecord);
        String text = messageRecord.getStringValue(Jan_queue.message);
        Date created = messageRecord.getDateValue(Jan_queue.created);
        String sender = messageRecord.getStringValue(Jan_queue.sender);
        String senderAddr = messageRecord.getStringValue(Jan_queue.senderaddr);
        List attachments = fetchAttachments(messageKey, accessor, attachmentBrowserDef);
        SendMessage message = new SendMessage(url, text, created, sender, senderAddr, attachments);

        // try sending
        send(message);

        // message has been successfully sent -> delete it
        messageRecord.delete(trans);
        
        if (logger.isDebugEnabled())
          logger.debug("Message " + messageKey + " successfully sent to: " + url);
        
        success = true;
      }
      catch (Exception ex)
      {
        // sending message failed

        // make log entry
        if (logger.isErrorEnabled())
          logger.error("Sending message " + messageKey + " failed", ex);
        
        StringBuffer errorMessage = new StringBuffer(ex.toString());
        Throwable cause =ex.getCause();
        if (cause!=null)
          errorMessage.append("\ncaused by: ").append(cause.toString());

        // and mark message as failure message
        messageRecord.setValue(trans, Jan_queue.state, Jan_queue.state_ENUM._Error);
        messageRecord.setStringValueWithTruncation(trans, Jan_queue.errormessage, errorMessage.toString());
        messageRecord.setValue(trans, Jan_queue.errorat, "now");
        messageRecord.setIntValue(trans, Jan_queue.tries, messageRecord.getintValue(Jan_queue.tries) + 1);

        // proceed, i.e. try next message
      }
      trans.commit();
      
      return success;
    }
    finally
    {
      trans.close();
    }
  }

  /**
   * Collect all attachments of a message from the message queue.
   * 
   * @param messageKey
   *          the message key
   * @param accessor
   *          the data accessor
   * @param attachmentBrowserDef
   * @return <code>List</code> of {@link SendAttachment}
   * @throws Exception
   */
  private static List fetchAttachments(int messageKey, IDataAccessor accessor, IAdhocBrowserDefinition attachmentBrowserDef) throws Exception
  {
    List result = new ArrayList();

    accessor.qbeClearAll();
    IDataTable attachmentTable = accessor.getTable(Jan_attachment.NAME);
    attachmentTable.qbeClear();
    attachmentTable.qbeSetValue(Jan_attachment.message_key, Integer.toString(messageKey));

    IDataBrowser attachmentBrowser = accessor.createBrowser(attachmentBrowserDef);
    attachmentBrowser.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);
    attachmentBrowser.search(accessor.getApplication().getLocalRelationSet());
    for (int i = 0; i < attachmentBrowser.recordCount(); i++)
    {
      IDataBrowserRecord attachmentRec = attachmentBrowser.getRecord(i);

      DataDocumentValue attachmentDoc = attachmentRec.getDocumentValue(1);
      String mimeType = attachmentRec.getStringValue(2);
      String filename = attachmentDoc.getName();
      byte[] content = attachmentDoc.getContent();
      result.add(new SendAttachment(mimeType, filename, content));
    }

    return result;
  }

  static IAdhocBrowserDefinition getAttachmentBrowserDefinition(IApplicationDefinition appDef) throws NoSuchFieldException
  {
    // create adhoc browser because we want to get message attachments in the
    // right order
    //
    IAdhocBrowserDefinition adhocBrowserDef = appDef.createAdhocBrowserDefinition(appDef.getTableAlias(Jan_attachment.NAME));
    adhocBrowserDef.addBrowserField(Jan_attachment.NAME, Jan_attachment.attachnbr, Jan_attachment.attachnbr, SortOrder.DESCENDING);
    adhocBrowserDef.addBrowserField(Jan_attachment.NAME, Jan_attachment.document, Jan_attachment.document, SortOrder.NONE);
    adhocBrowserDef.addBrowserField(Jan_attachment.NAME, Jan_attachment.mimetype, Jan_attachment.mimetype, SortOrder.NONE);

    return adhocBrowserDef;
  }
  
  /**
   * Collect all attachments of a message from the message queue.
   * 
   * @param messageKey
   *          the message key
   * @param accessor
   *          the data accessor
   * @return <code>List</code> of {@link SendAttachment}
   * @throws Exception
   */
  public static List getSendAttachments(int messageKey, IDataAccessor accessor) throws Exception
  {
    return fetchAttachments(messageKey, accessor, getAttachmentBrowserDefinition(accessor.getApplication()));
  }
}
