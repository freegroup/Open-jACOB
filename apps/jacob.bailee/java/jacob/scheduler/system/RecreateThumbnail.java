package jacob.scheduler.system;

import jacob.common.Ico;
import jacob.common.PdfToImage;
import jacob.common.Thumbnail;
import jacob.model.Document;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.SecondsIterator;

/**
 * @author andherz
 *
 */
public class RecreateThumbnail extends SchedulerTaskSystem
{
	static public final transient String RCS_ID = "$Id: RecreateThumbnail.java,v 1.2 2010/02/08 16:23:38 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	public static final int THUMB_MINI=140;
  public static final int THUMB_MEDIUM=600;

	// Start the task every 1 minutes
	// for more iterators see in the package <de.tif.jacob.scheduler.iterators.*>
	//
	final ScheduleIterator iterator = new SecondsIterator(20);


	/**
	 * Returns the Iterator which defines the run interval of this job.<br>
	 * 
	 */
	public ScheduleIterator iterator()
	{
		return iterator;
	}

	/**
	 * The run method of the job.<br>
	 * The object <code>context>/code> defines your current context in the jACOB application
	 * server.<br>
	 * You can use it to access the database or other relevatn application data.<br>
	 */
	public void run(TaskContextSystem context) throws Exception
	{
    IDataTable documentTable = context.getDataTable(Document.NAME);
    documentTable.qbeSetKeyValue(Document.thumbnail, null);
    documentTable.search();

    for(int i=0;i<documentTable.recordCount();i++)
    {
      IDataTransaction trans = context.getDataAccessor().newTransaction();
      try
      {
        IDataTableRecord tableRecord = documentTable.getRecord(i);
        DataDocumentValue document = tableRecord.getDocumentValue(Document.file);
        Image image = null;
        
        if(document.getName().endsWith(".pdf"))
        {
          image = PdfToImage.pdf2(document.getContent());
        }
        else if(document.getName().endsWith(".ico"))
        {
          Ico ico = new Ico(new ByteArrayInputStream(document.getContent()));
          String[] resolutions = new String[ico.getNumImages()];
          int max = 0;
          for (int index = 0; index < resolutions.length; index++)
          {
            int product = ico.getImage(index).getWidth() * ico.getImage(index).getHeight();
            if(product>max)
            {
              image = ico.getImage(index);
              max = product;
            }
          }
        }
        else
        {
          image = new ImageIcon(document.getContent()).getImage();
        }
        
        
        BufferedImage thumb_mini = Thumbnail.createThumbnail(image, THUMB_MINI);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(thumb_mini, "png", stream );
        tableRecord.setDocumentValue(trans, Document.thumbnail, DataDocumentValue.create("thumbnail.png",stream.toByteArray()));
        tableRecord.setIntValue(trans, Document.thumbnail_width, thumb_mini.getWidth());
        tableRecord.setIntValue(trans, Document.thumbnail_height, thumb_mini.getHeight());
        
        // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
        // into integer pixels
        BufferedImage thumb_medium = Thumbnail.createThumbnail(image, THUMB_MEDIUM);;

        int width = thumb_medium.getWidth(null);
        int height= thumb_medium.getHeight(null);
        BufferedImage thumb_normalized = new BufferedImage(THUMB_MEDIUM, THUMB_MEDIUM, BufferedImage.TYPE_INT_ARGB);
        Graphics2D ig2 = thumb_normalized.createGraphics();
        ig2.drawImage(thumb_medium,Math.max(0,(THUMB_MEDIUM-width)/2),Math.max(0,(THUMB_MEDIUM-height)/2),null);

        stream = new ByteArrayOutputStream();
        ImageIO.write(thumb_normalized, "png", stream );
        tableRecord.setDocumentValue(trans, Document.normalized_thumbnail, DataDocumentValue.create("thumbnail.png",stream.toByteArray()));

        trans.commit();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
      finally
      {
        trans.close();
      }
    }
	}
}
