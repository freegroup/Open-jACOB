/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package de.tif.jacob.messaging;

import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.model.Jan_attachment;
import de.tif.jacob.core.model.Jan_queue;
import de.tif.jacob.messaging.alert.AlertManager;
import de.tif.jacob.security.IUser;

/**
 * Common messaging class to either send
 * <li> alerts,
 * <li> (HTML) emails,
 * <li> SMS messages,
 * <li> FAX messages,
 * <li> or user-definied (i.e. other message protocol) messages.
 * <p>
 * Except of alerts, messages will first be queued and afterwards processed
 * (asynchronously) by means of the jAN application.
 * <p>
 * Note: By default jAN supports email and HTML email only. Nevertheless, the
 * jAN application can be extended accordingly to support further kinds of
 * message protocols.
 */
public class Message
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: Message.java,v 1.4 2010/03/21 22:58:32 ibissw Exp $";
  
  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.4 $";

  private static class Attachment
  {
    private final String mimeType;
    private final DataDocumentValue document;

    private Attachment(String mimeType, DataDocumentValue document)
    {
      this.document = document;
      this.mimeType = mimeType != null ? mimeType : getMimeType(document.getName());
    }
  }

  /**
   * The protocol header suffix to be used in recipient urls, i.e. <code>"://"<code>.
   * 
   * @see #Message(String, String)
   */
  static public final String PROTOCOL_HEADER_SUFFIX = "://";
  
  /**
   * Email protocol name, i.e. <code>"email"<code>.
   * <p>
   * Email recipient urls should be as follows:<br>
   * <code>email://my@email.com[,my2@email.com,my3@email.com]
   * 
   * @see #Message(String, String)
   */
  static public final String EMAIL_PROTOCOL_NAME = "email";
  
  /**
   * Html email protocol name, i.e. <code>"htmlemail"<code>.
   * <p>
   * Html email recipient urls should be as follows:<br>
   * <code>htmlemail://my@email.com[,my2@email.com,my3@email.com]
   * 
   * @see #Message(String, String)
   */
  static public final String EMAIL_HTML_PROTOCOL_NAME = "htmlemail";

  /**
   * SMS protocol name, i.e. <code>"sms"<code>.
   * <p>
   * SMS recipient urls should be as follows:<br>
   * <code>sms://phonenbr[,phonenbr2,phonenbr3]
   * 
   * @see #Message(String, String)
   */
  static public final String SMS_PROTOCOL_NAME = "sms";
  
  /**
   * FAX protocol name, i.e. <code>"fax"<code>.
   * <p>
   * FAX recipient urls should be as follows:<br>
   * <code>fax://phonenbr[,phonenbr2,phonenbr3]
   * 
   * @see #Message(String, String)
   */
  static public final String FAX_PROTOCOL_NAME = "fax";

  /**
   * Directory protocol name, i.e. <code>"directory"<code>.
   * <p>
   * Directory recipient urls should be as follows:<br>
   * <code>directory://directoryAbsoluteOrRelativePath
   * 
   * @since 2.9.3
   * @see #Message(String, String)
   */
  static public final String DIRECTORY_PROTOCOL_NAME = "directory";

  private final String recipientUrl;
  private final String message;
  private String sender;
  private String senderAddr;
  private List attachments;

  /**
   * Generic message constructor.
   * <p>
   * Use this constructor to send a jAN message via any type of protocol.
   * Nevertheless, the jAN application has to be extended accordingly.
   * 
   * @param recipientUrl
   *          the recipient URL of this message, which must be structured as
   *          follows:<br>
   *          protocol://recipientInfoPart
   * @param message
   *          the message content to send
   * @since 2.6
   */
  public Message(String recipientUrl, String message)
  {
    if (recipientUrl == null)
      throw new NullPointerException("recipientUrl is null");
    if (message == null)
      throw new NullPointerException("message is null");
    
    int idx = recipientUrl.indexOf(PROTOCOL_HEADER_SUFFIX);
    if (idx < 1 || idx >= recipientUrl.length() - PROTOCOL_HEADER_SUFFIX.length())
      throw new RuntimeException("Invalid recipient URL: " + recipientUrl);
    
    this.recipientUrl = recipientUrl;
    this.message = message;
  }

  protected Message()
  {
    this.recipientUrl = "";
    this.message = "";
  }

  protected String getRecipientUrl()
  {
    return recipientUrl;
  }

  protected String getContent()
  {
    return message;
  }

  /**
   * Sets the sender of the message.
   * <p>
   * Note: If the sender is not explicitly set by this method, the sender will
   * be set implicitly to the full name (@link IUser#getFullName()) of the
   * invoking user (@link Context#getUser()}.
   * 
   * @param sender
   *          the sender to set or <code>null</code> to let jAN determine the
   *          sender.
   * @since 2.6
   */
  public void setSender(String sender)
  {
    if (sender == null)
      sender = "";
    this.sender = sender;
  }

  /**
   * Sets the address of the sender of the message.
   * <p>
   * Note: If the sender address is not explicitly set by this method, the
   * sender address will be set implicitly to the email address (@link
   * IUser#getEMail()) of the invoking user (@link Context#getUser()}.
   * 
   * @param senderAddr
   *          the sender address to set or <code>null</code> to let jAN
   *          determine the sender address.
   * @since 2.6
   */
  public void setSenderAddr(String senderAddr)
  {
    if (senderAddr == null)
      senderAddr = "";
    this.senderAddr = senderAddr;
  }

  /**
   * Adds a document as attachment to this message.
   * 
   * @param document
   *          the document value to add as attachment
   * @see de.tif.jacob.core.data.IDataRecord#getDocumentValue(String)
   * @since 2.6
   */
  public void addAttachment(DataDocumentValue document)
  {
    addAttachment(null, document);
  }

  /**
   * Adds a document as attachment to this message.
   * 
   * @param mimeType
   *          the mime type to use or <code>null</code> for default mime type
   * @param document
   *          the document value to add as attachment
   * @see de.tif.jacob.core.data.IDataRecord#getDocumentValue(String)
   * @see #getMimeType(String)
   * @since 2.6
   */
  public void addAttachment(String mimeType, DataDocumentValue document)
  {
    if (this.attachments == null)
      this.attachments = new ArrayList();

    this.attachments.add(new Attachment(mimeType, document));
  }

  /**
   * Adds a file as attachment to this message.
   * 
   * @param mimeType
   *          the mime type to use or <code>null</code> for default mime type
   * @param fileName
   *          the file name
   * @param fileContent
   *          the file content
   * @since 2.6
   */
  public void addAttachment(String mimeType, String fileName, byte[] fileContent)
  {
    if (fileName == null)
      throw new NullPointerException("fileName is null");

    addAttachment(mimeType, DataDocumentValue.create(fileName, fileContent));
  }
  
  /**
   * Returns the default mime type of a given file name.
   * 
   * @param fileName
   *          the file name
   * @return the default mime type
   * @since 2.6
   */
  public static String getMimeType(String fileName)
  {
    if (fileName == null)
      throw new NullPointerException("fileName is null");
    
    String res = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(fileName);
    
    // Under Windows file names and their extensions are often upper case, which results
    // to default mime type application/octet-stream.
    //
    if ("application/octet-stream".equals(res))
      return MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(fileName.toLowerCase());
    
    return res;
  }

  /**
   * Sends the message, i.e. the message is queued for jAN processing.
   * 
   * @throws Exception
   *           if the message could not be queued
   * @since 2.6
   */
  public void send() throws Exception
  {
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTransaction trans = accessor.newTransaction();
    try
    {
      IDataTable queue = accessor.getTable(Jan_queue.NAME);
      IDataTableRecord msg = queue.newRecord(trans);

      Context context = Context.getCurrent();
      IUser user = context.getUser();
      msg.setValue(trans, Jan_queue.sourceapp, context.getApplicationDefinition().getName());
      msg.setValue(trans, Jan_queue.urlcomplete, getRecipientUrl());
      msg.setValue(trans, Jan_queue.message, getContent());
      msg.setValue(trans, Jan_queue.sender, this.sender == null && !user.isSystem() ? user.getFullName() : this.sender);
      msg.setValue(trans, Jan_queue.senderaddr, this.senderAddr == null && !user.isSystem() ? user.getEMail() : this.senderAddr);

      if (this.attachments != null)
      {
        IDataTable attachmentTable = accessor.getTable(Jan_attachment.NAME);
        for (int i = 0; i < this.attachments.size(); i++)
        {
          Attachment attachment = (Attachment) this.attachments.get(i);

          IDataTableRecord attachmentRecord = attachmentTable.newRecord(trans);
          attachmentRecord.setValue(trans, Jan_attachment.message_key, msg.getValue(Jan_queue.pkey));
          attachmentRecord.setIntValue(trans, Jan_attachment.attachnbr, i + 1);
          attachmentRecord.setDocumentValue(trans, Jan_attachment.document, attachment.document);
          attachmentRecord.setValue(trans, Jan_attachment.mimetype, attachment.mimeType);
        }
      }
      trans.commit();
    }
    finally
    {
      trans.close();
    }
  }

  /**
   * Sends a SMS message via jAN.
   * 
   * @param recipient
   *          the SMS recipient
   * @param message
   *          the SMS message
   * @throws Exception
   *           on any problem sending this message
   */
  public static void sendSMS(String recipient, String message) throws Exception
  {
    new Message(SMS_PROTOCOL_NAME + PROTOCOL_HEADER_SUFFIX + recipient, message).send();
  }

  /**
   * Sends an email message via jAN.
   * 
   * @param recipient
   *          the email recipient.
   *          <p>
   *          Note: To send an email message to multiple recipients
   *          <code>recipient</code> must be a comma separated list of email
   *          addresses.
   * @param message
   *          the email message.<br>
   *          Note: First line is treated as email subject.
   * @throws Exception
   *           on any problem sending this message
   * @deprecated Use {@link #createEMailMessage(String, String)} instead.
   */
  public static void sendEMail(String recipient, String message) throws Exception
  {
    createEMailMessage(recipient, message).send();
  }

  /**
   * Creates an email message.
   * <p>
   * Call {@link #send()} to really release the returned message.
   * 
   * @param recipient
   *          the email recipient.
   *          <p>
   *          Note: To send an email message to multiple recipients
   *          <code>recipient</code> must be a comma separated list of email
   *          addresses.
   * @param message
   *          the email content.<br>
   *          Note: First line is treated as email subject.
   * @return the email message
   * @since 2.6
   * @deprecated use {@link EmailMessage}
   */
  public static Message createEMailMessage(String recipient, String message)
  {
    return new Message(EMAIL_PROTOCOL_NAME + PROTOCOL_HEADER_SUFFIX + recipient, message);
  }

  /**
   * Sends a HTML formated email via the jAN.
   * 
   * @param recipient
   *          the email recipient.
   *          <p>
   *          Note: To send an email message to multiple recipients
   *          <code>recipient</code> must be a comma separated list of email
   *          addresses.
   * @param message
   *          the email message.<br>
   *          Note: First line is treated as email subject.
   * @throws Exception
   *           on any problem sending this message
   * @deprecated Use {@link #createHtmlEMailMessage(String, String)} instead.           
   */
  public static void sendHtmlEMail(String recipient, String message) throws Exception
  {
    createHtmlEMailMessage(recipient, message).send();
  }

  /**
   * Creates a HTML formated email message.
   * <p>
   * Call {@link #send()} to really release the returned message.
   * 
   * @param recipient
   *          the email recipient.
   *          <p>
   *          Note: To send an email message to multiple recipients
   *          <code>recipient</code> must be a comma separated list of email
   *          addresses.
   * @param message
   *          the email content.<br>
   *          Note: First line is treated as email subject.
   * @return the HTML formated email message
   * @since 2.6
   * @deprecated use {@link HtmlEmailMessage}
   */
  public static Message createHtmlEMailMessage(String recipient, String message)
  {
    return new Message(EMAIL_HTML_PROTOCOL_NAME + PROTOCOL_HEADER_SUFFIX + recipient, message);
  }

  /**
   * Sends a fax message via jAN.
   * 
   * @param recipient
   *          the FAX recipient
   * @param message
   *          the fax message
   * @throws Exception
   *           on any problem
   */
  public static void sendFAX(String recipient, String message) throws Exception
  {
    new Message(FAX_PROTOCOL_NAME + PROTOCOL_HEADER_SUFFIX + recipient, message).send();
  }

  /**
   * Sends an alert message.
   * 
   * @param recipient
   *          the alert recipient
   * @param message
   *          the alert message
   * @throws Exception
   *           on any problem
   */
  public static void sendAlert(String recipient, String message) throws Exception
  {
    sendAlert(recipient, message, null);
  }

  /**
   * Sends an alert message with an url.
   * <p>
   * 
   * Attention: This does not work for Quintus migration projects!
   * 
   * @param recipient
   *          the alert recipient
   * @param message
   *          the alert message
   * @param url
   *          the (entrypoint) url to be invoked, if the alert is selected by an
   *          user
   * @throws Exception
   *           on any problem
   */
  public static void sendAlert(String recipient, String message, String url) throws Exception
  {
    AlertManager.sendAlert(recipient, message, url);
  }
}
