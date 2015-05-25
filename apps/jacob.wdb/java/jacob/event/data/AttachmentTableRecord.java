/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Aug 16 21:49:35 CEST 2010
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.common.Thumbnail;
import jacob.model.Attachment;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
/**
 * 
 * @author andherz
 */
public class AttachmentTableRecord extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: AttachmentTableRecord.java,v 1.3 2010-09-08 21:41:35 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
  static private final transient Log logger = AppLogger.getLogger();

  private static final Set<String> IMAGE_SUFFIX = new HashSet<String>();
  static
  {
    IMAGE_SUFFIX.add("png");
    IMAGE_SUFFIX.add("gif");
    IMAGE_SUFFIX.add("jpg");
    // IMAGE_SUFFIX.add("tiff"); // kann der Browser nicht anzeigen
    // IMAGE_SUFFIX.add("bmp"); // kann der Browser nicht anzeigen
  }

  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    // Be in mind: It is not possible to modify the 'tableRecord', if we want
    // delete it
    //
    if (tableRecord.isDeleted())
      return;
    
    tableRecord.setDocumentValue(transaction, Attachment.thumbnail, null);
    tableRecord.setValue(transaction, Attachment.is_image, false);
    if (!tableRecord.hasNullValue(Attachment.document))
    {
      DataDocumentValue doc = tableRecord.getDocumentValue(Attachment.document);
      
      // Attachment-Beschreibung mit Dokumentnamen setzen
      if (tableRecord.hasNullValue(Attachment.title))
        tableRecord.setValue(transaction, Attachment.title, doc.getName());
        
      String suffix = FilenameUtils.getExtension(doc.getName()).toLowerCase();
      if (IMAGE_SUFFIX.contains(suffix))
      {
        // Thumbnail berechnen und dem Attachment hinzufügen.
        try
        {
          Image image = new ImageIcon(doc.getContent()).getImage();
          BufferedImage thumb_mini = Thumbnail.createThumbnail(image, 500);
          ByteArrayOutputStream stream = new ByteArrayOutputStream();
          ImageIO.write(thumb_mini, "png", stream);
          tableRecord.setDocumentValue(transaction, Attachment.thumbnail, DataDocumentValue.create("thumbnail.png", stream.toByteArray()));
          tableRecord.setValue(transaction, Attachment.is_image, true);
        }
        catch (Exception exc)
        {
          // ignore. Da kann so viel passieren. Kein richtiges JavaIO, ImageIO
          // oder Headless ist nicht richtig gesetzt......pech gehabt. Ist nur ein zusatzfeature.
          logger.warn("Can not create thumbnail for document '" + doc.getName() + "': " + exc.toString());
        }
       
      }
    }
  }

  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
  }
}
