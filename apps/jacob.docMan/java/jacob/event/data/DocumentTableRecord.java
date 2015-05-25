/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jul 01 15:35:06 CEST 2010
 */
package jacob.event.data;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import jacob.common.AppLogger;
import jacob.common.BoUtil;
import jacob.common.DataSize;
import jacob.common.JournalManager;
import jacob.model.Bo;
import jacob.model.Document;
import jacob.model.Folder;

import org.apache.commons.logging.Log;

/**
 *
 * @author andherz
 */
public class DocumentTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: DocumentTableRecord.java,v 1.1 2010-09-17 08:42:22 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

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
	  IDataTableRecord boRecord = BoUtil.findByPkey(Context.getCurrent(), tableRecord.getStringValue(Document.pkey));
	  boRecord.delete(transaction);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	  JournalManager.append(tableRecord);
	  
		// Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
		//
		if(tableRecord.isDeleted())
			return;

    tableRecord.setValue(transaction, Document.change_date, "NOW");
    String documentName = tableRecord.getDocumentValue(Document.content).getName();
    byte[] documentContent = tableRecord.getDocumentValue(Document.content).getContent();
		tableRecord.setValue(transaction, Document.name, documentName);
    tableRecord.setValue(transaction, Document.document_size_byte, documentContent.length);
    tableRecord.setValue(transaction, Document.document_size_text, (new DataSize(documentContent.length)).toString());
    if(!tableRecord.isNew())
    {
      IDataTableRecord boRecord = BoUtil.findByPkey(Context.getCurrent(), tableRecord.getStringValue(Document.pkey));
      boRecord.setValue(transaction, Bo.name, tableRecord.getStringValue(Document.name));
    }
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}
