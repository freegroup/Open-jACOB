package jacob.scheduler.system;

import jacob.common.AppLogger;
import jacob.model.Ppt;
import jacob.model.Ppt_recreate;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;

import com.aspose.slides.Presentation;
import com.aspose.slides.Slide;
import com.aspose.slides.Slides;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.SecondsIterator;
import de.tif.jacob.util.gif.Quantize;

/**
 * @author andherz
 * 
 */
public class ThumbnailRecreater extends SchedulerTaskSystem
{
  static public final transient String RCS_ID          = "$Id: ThumbnailRecreater.java,v 1.4 2007/10/02 15:09:07 herz Exp $";

  static public final transient String RCS_REV         = "$Revision: 1.4 $";

  static private final Dimension       DIMENSION_BIG   = new Dimension(500, 340);

  static private final Dimension       DIMENSION_SMALL = new Dimension(120, 90);

  // use this to log relvant information....not the System.out.println(...) ;-)
  //
  static protected final transient Log logger          = AppLogger.getLogger();

  // Start the task every 1 minutes
  // for more iterators see in the package <de.tif.jacob.scheduler.iterators.*>
  //
  final ScheduleIterator               iterator        = new SecondsIterator(10);

  static
  {
    System.out.println("scanning for JavaIO classes");
    ImageIO.scanForPlugins();
  }

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
   * The object
   * <code>context>/code> defines your current context in the jACOB application
   * server.<br>
   * You can use it to access the database or other relevatn application data.<br>
   */
  public void run(TaskContextSystem context) throws Exception
  {
    IDataTable pptTable = context.getDataTable(Ppt_recreate.NAME);
    pptTable.search();
    for (int i = 0; i < pptTable.recordCount(); i++)
    {
      IDataTableRecord ppt = pptTable.getRecord(i);
      recreateThumbnail(context, ppt);
    }
  }

  private void recreateThumbnail(TaskContextSystem context, IDataTableRecord ppt)
  {
    IDataTransaction trans = context.getDataAccessor().newTransaction();
    try
    {
      Presentation part_en = null;
      Presentation part_de = null;

      if (ppt.getDocumentValue(Ppt_recreate.document_de) != null && ppt.getDocumentValue(Ppt_recreate.document_de).getContent() != null)
        part_de = new Presentation(new ByteArrayInputStream(ppt.getDocumentValue(Ppt_recreate.document_de).getContent()));

      if (ppt.getDocumentValue(Ppt_recreate.document_en) != null && ppt.getDocumentValue(Ppt_recreate.document_en).getContent() != null)
        part_en = new Presentation(new ByteArrayInputStream(ppt.getDocumentValue(Ppt_recreate.document_en).getContent()));

      if (part_en != null)
      {
        BufferedImage img = part_en.getSlideByPosition(1).getThumbnail(DIMENSION_BIG);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ImageIO.write(img, "png", out);

        // ImageIO.write(img, "png", out);
        out.close();
        DataDocumentValue thumb = DataDocumentValue.create("thumbnail_en.png", out.toByteArray());

        ppt.setDocumentValue(trans, Ppt.thumbnail_en, thumb);

        img = part_en.getSlideByPosition(1).getThumbnail(DIMENSION_SMALL);
        out = new ByteArrayOutputStream();

        ImageIO.write(img, "png", out);
        out.close();
        thumb = DataDocumentValue.create("thumbnail_en_small.png", out.toByteArray());
        ppt.setDocumentValue(trans, Ppt.thumbnail_en_small, thumb);
        ppt.setValue(trans, Ppt_recreate.changedate_thumbnail, "now");
      }

      if (part_de != null)
      {
        BufferedImage img = part_de.getSlideByPosition(1).getThumbnail(DIMENSION_BIG);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ImageIO.write(img, "png", out);
        out.close();
        DataDocumentValue thumb = DataDocumentValue.create("thumbnail_de.png", out.toByteArray());
        ppt.setDocumentValue(trans, Ppt.thumbnail_de, thumb);

        img = part_de.getSlideByPosition(1).getThumbnail(DIMENSION_SMALL);
        out = new ByteArrayOutputStream();

        ImageIO.write(img, "png", out);
        out.close();
        thumb = DataDocumentValue.create("thumbnail_de_small.png", out.toByteArray());
        ppt.setDocumentValue(trans, Ppt.thumbnail_de_small, thumb);
        ppt.setValue(trans, Ppt_recreate.changedate_thumbnail, "now");
      }
      trans.commit();
    }
    catch (NoClassDefFoundError e)
    {
      // ignore, hibernate the job and return
      //
      ExceptionHandler.handle(e);
      this.hibernate();
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(e);
    }
    finally
    {
      trans.close();
    }
  }

}
