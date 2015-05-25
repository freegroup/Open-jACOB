package jacob.scheduler.system;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import jacob.common.task.SendAttachment;
import jacob.common.task.SendMessage;
import jacob.common.task.SendMessageTask;
import jacob.common.task.SendProtocol;
import jacob.model.DirectoryProperties;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.messaging.Message;

/**
 * 
 * @author Andreas Sonntag
 */
public class SendToDirectoryTask extends SendMessageTask
{
  static public final transient String RCS_ID = "$Id: SendToDirectoryTask.java,v 1.1 2010-03-21 22:41:13 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Note: Protocol task mast be public!
   * 
   * @author Andreas Sonntag
   */
  public static class Protocol extends SendProtocol
  {
    private String root;

    public Protocol()
    {
      super(Message.DIRECTORY_PROTOCOL_NAME);
    }

    protected boolean initialize(Context context) throws Exception
    {
      IDataTable configTable = context.getDataTable(DirectoryProperties.NAME);
      configTable.setMaxRecords(1);
      configTable.qbeClear();
      configTable.qbeSetValue(DirectoryProperties.state, DirectoryProperties.state_ENUM._active);
      if (configTable.search() > 0)
      {
        IDataTableRecord configuration = configTable.getRecord(0);

        this.root = configuration.getStringValue(DirectoryProperties.root);

        return true;
      }

      return false;
    }

    protected int repeatMaxRetries()
    {
      return 0;
    }

    protected void send(SendMessage message) throws Exception
    {
      if (message.getAttachmentCount() == 0)
        throw new Exception("Message does not contain attached files to write to directory");

      String directory = message.getRecipientInfo();
      {
        // Path is relative?
        File dir = new File(directory);
        if (!dir.isAbsolute())
        {
          directory = this.root + File.separator + directory;
        }
        dir = new File(directory);
        if (!dir.isDirectory())
        {
          throw new Exception("'" + directory + "' is not a directory");
        }
      }

      // write attachments to the directory
      //
      for (int i = 0; i < message.getAttachmentCount(); i++)
      {
        SendAttachment attachment = message.getAttachment(i);

        String filename = directory + File.separator + attachment.getFilename();
        File file = new File(filename);
        if (file.exists())
        {
          if (!file.delete())
            throw new Exception("Can not delete old file '" + filename + "'");
        }
        OutputStream out = new FileOutputStream(file);
        try
        {
          out.write(attachment.getContent());
          out.flush();
        }
        finally
        {
          out.close();
        }
      }
    }
  }

  protected SendProtocol getProtocol()
  {
    return new Protocol();
  }
}
