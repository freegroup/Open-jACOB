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

import de.tif.jacob.util.StringUtil;

/**
 * Messaging class to send email messages by means of the jAN application.
 * 
 * @since 2.9
 */
public class EmailMessage extends Message
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: EmailMessage.java,v 1.1 2009/10/29 20:31:56 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.1 $";

  private final String protocol;

  private String subject;

  private String body;

  private final StringBuffer recipients = new StringBuffer();

  /**
   * Email message constructor.
   */
  public EmailMessage()
  {
    this(EMAIL_PROTOCOL_NAME);
  }

  protected EmailMessage(String protocol)
  {
    this.protocol = protocol;
  }

  protected String getContent()
  {
    StringBuffer buffer = new StringBuffer(StringUtil.toSaveString(this.subject));
    if (this.body != null && this.body.length() > 0)
      buffer.append("\n").append(this.body);
    return buffer.toString();
  }

  protected String getRecipientUrl()
  {
    StringBuffer buffer = new StringBuffer(this.protocol);
    buffer.append(PROTOCOL_HEADER_SUFFIX);
    buffer.append(this.recipients);
    return buffer.toString();
  }

  /**
   * Adds an email recipient.
   * 
   * @param emailAddress the email address of the recipient
   */
  public void addRecipient(String emailAddress)
  {
    addRecipient(emailAddress, null);
  }

  /**
   * Adds an email recipient.
   * 
   * @param emailAddress
   *          the email address of the recipient
   * @param recipientName
   *          the clear text name of the recipient, i.e. the email address add
   *          on
   */
  public void addRecipient(String emailAddress, String recipientName)
  {
    if (emailAddress == null)
      throw new NullPointerException("emailAddress is null");

    if (this.recipients.length() > 0)
      this.recipients.append(",");
    if (recipientName != null && recipientName.trim().length() > 0)
    {
      this.recipients.append("\"").append(recipientName).append("\" <");
      this.recipients.append(emailAddress).append(">");
    }
    else
    {
      this.recipients.append(emailAddress);
    }
  }

  /**
   * Adds an email recipient on carbon copy (CC).
   * <p>
   * Note: Use jAN application 1.1 or above to use this feature!
   * 
   * @param emailAddress
   *          the email address of the recipient
   */
  public void addCcRecipient(String emailAddress)
  {
    addCcRecipient(emailAddress, null);
  }

  /**
   * Adds an email recipient on carbon copy (CC).
   * <p>
   * Note: Use jAN application 1.1 or above to use this feature!
   * 
   * @param emailAddress
   *          the email address of the recipient
   * @param recipientName
   *          the clear text name of the recipient, i.e. the email address add
   *          on
   */
  public void addCcRecipient(String emailAddress, String recipientName)
  {
    if (recipientName == null || recipientName.trim().length() == 0)
      recipientName = emailAddress;
    addRecipient(emailAddress, "CC:" + recipientName);
  }

  /**
   * Adds an email recipient on blind carbon copy (BCC).
   * <p>
   * Note: Use jAN application 1.1 or above to use this feature!
   * 
   * @param emailAddress the email address of the recipient
   */
  public void addBccRecipient(String emailAddress)
  {
    addBccRecipient(emailAddress, null);
  }

  /**
   * Adds an email recipient on blind carbon copy (BCC).
   * <p>
   * Note: Use jAN application 1.1 or above to use this feature!
   * 
   * @param emailAddress
   *          the email address of the recipient
   * @param recipientName
   *          the clear text name of the recipient, i.e. the email address add
   *          on
   */
  public void addBccRecipient(String emailAddress, String recipientName)
  {
    if (recipientName == null || recipientName.trim().length() == 0)
      recipientName = emailAddress;
    addRecipient(emailAddress, "BCC:" + recipientName);
  }

  /**
   * Sets the message subject of this email message.
   * 
   * @param subject
   *          the subject to set
   */
  public void setSubject(String subject)
  {
    this.subject = subject;
  }

  /**
   * Sets the message body of this email message.
   * 
   * @param message
   *          the message body to set
   */
  public void setMessage(String message)
  {
    this.body = message;
  }

}
