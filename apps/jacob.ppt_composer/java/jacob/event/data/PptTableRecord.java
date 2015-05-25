/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Mar 02 15:51:19 CET 2007
 */
package jacob.event.data;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.UserException;
import jacob.common.AppLogger;
import jacob.model.Ppt;

import org.apache.commons.logging.Log;

import com.aspose.slides.Presentation;

/**
 *
 * @author andherz
 */
public class PptTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: PptTableRecord.java,v 1.3 2007/10/02 10:53:07 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
		// Be in mind: It is not possible to modify the 'tableRecord', if we want
    // delete it
    //
    if (tableRecord.isDeleted())
      return;

    tableRecord.setValue(transaction, Ppt.changedate, "now");

    if (tableRecord.hasChangedValue(Ppt.document_de))
    {
      tableRecord.setDocumentValue(transaction, Ppt.thumbnail_de, null);
      tableRecord.setValue(transaction, Ppt.changedate_de, "now");
    }

    if (tableRecord.hasChangedValue(Ppt.document_en))
    {
      tableRecord.setDocumentValue(transaction, Ppt.thumbnail_en, null);
      tableRecord.setValue(transaction, Ppt.changedate_en, "now");
    }

    // Es sind nur dokumente vom tpye "PPT" erlaubt.
    //
    if (!tableRecord.hasNullValue(Ppt.document_de))
    {
      String filename = tableRecord.getDocumentValue(Ppt.document_de).getName();
      if (!filename.endsWith(".ppt"))
        throw new UserException("Only PowerPoint (*.ppt) files allowed.");
    }
    if (!tableRecord.hasNullValue(Ppt.document_en))
    {
      String filename = tableRecord.getDocumentValue(Ppt.document_en).getName();
      if (!filename.endsWith(".ppt"))
        throw new UserException("Only PowerPoint (*.ppt) files allowed.");
    }
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}
