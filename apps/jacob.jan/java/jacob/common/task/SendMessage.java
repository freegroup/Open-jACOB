package jacob.common.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import de.tif.jacob.messaging.Message;

public class SendMessage
{
  private final Date created;
  private final String text;
  private final String sender;
  private final String senderAddr;
  private final List recipientAddrs;
  private final String recipientInfo;
  private final List attachments;

  protected SendMessage(String url, String text, Date created, String sender, String senderAddr, List attachments)
  {
    // fetch recipient info from url
    //
    int idx = url.indexOf(Message.PROTOCOL_HEADER_SUFFIX);
    this.recipientInfo = url.substring(idx + Message.PROTOCOL_HEADER_SUFFIX.length());

    // check for recipient addresses in recipient info.
    // Assume multiple recipient addresses are comma separated
    this.recipientAddrs = getRecipientAddrs(recipientInfo);
    
    this.created = created;
    this.text = text;
    this.sender = sender;
    this.senderAddr = senderAddr;
    this.attachments = attachments;
  }
  
  private static List getRecipientAddrs(String recipientInfo)
  {
    List recipientAddrs = new ArrayList();
    StringTokenizer st = new StringTokenizer(recipientInfo, ",\"", true);
    boolean ignoreComma = false;
    StringBuffer recipientAddr = new StringBuffer();
    while (st.hasMoreTokens())
    {
      String token = st.nextToken();
      if (",".equals(token))
      {
        if (ignoreComma)
          // comma is part of the name
          recipientAddr.append(token);
        else
        {
          addRecipient(recipientAddrs, recipientAddr.toString());
          recipientAddr = new StringBuffer();
        }
      }
      else if ("\"".equals(token))
      {
        ignoreComma = !ignoreComma;
      }
      else
      {
        recipientAddr.append(token);
      }
    }
    addRecipient(recipientAddrs, recipientAddr.toString());
    
    return recipientAddrs;
  }
  
  private static void addRecipient(List recipientAddrs, String recipient)
  {
    if (recipient!=null)
    {
      recipient = recipient.trim();
      if (recipient.length()>0)
        recipientAddrs.add(recipient);
    }
  }
  
  private static final String[] testStrings = { //
    "hans@mail.de", //
    "hans@mail.de, Karin <karin@mail.de>",
    "hans@mail.de, ,Karin <karin@mail.de>",
    "\"Mustermann, Hans\" <hans@mail.de>, ,Karin <karin@mail.de>"
  };
  
  public static void main(String[] args)
  {
    for (int i = 0; i < testStrings.length; i++)
    {
      String test = testStrings[i];
      System.out.print(test + " -> ");
      List addrs = getRecipientAddrs(test);
      for (int j = 0; j < addrs.size(); j++)
      {
        System.out.print("[" + addrs.get(j) + "]");
      }
      System.out.println();
    }
  }
  
  /**
   * @return Returns the created.
   */
  public Date getCreated()
  {
    return created;
  }

  /**
   * Get recipient address.
   * 
   * @param index 
   * @return
   */
  public String getRecipientAddr(int index)
  {
    return (String) recipientAddrs.get(index);
  }

  public String getRecipientInfo()
  {
    return this.recipientInfo;
  }

  public int getRecipientCount()
  {
    return this.recipientAddrs.size();
  }

  public SendAttachment getAttachment(int index)
  {
    return (SendAttachment) this.attachments.get(index);
  }

  public int getAttachmentCount()
  {
    if (this.attachments == null)
      return 0;
    return this.attachments.size();
  }

  /**
   * @return Returns the sender.
   */
  public String getSender()
  {
    return sender;
  }

  /**
   * @return Returns the senderAddr.
   */
  public String getSenderAddr()
  {
    return senderAddr;
  }

  /**
   * @return Returns the text.
   */
  public String getText()
  {
    return text;
  }

}
