/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Nov 01 08:31:41 CET 2006
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.common.SendFactory;
import jacob.model.Configuration;
import jacob.model.Document;
import jacob.model.Tagging;

import java.io.File;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.letter.LetterFactory;

/**
 *
 * @author andherz
 */
public class DocumentTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: DocumentTableRecord.java,v 1.2 2010/01/29 16:02:03 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

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
		// Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
		//
		if(tableRecord.isDeleted())
			return;

		// Der Name des Dokumentes wird in ein seperates Feld übernommen. Der Databrowser ist somit
		// "leichter". Das heist, dass bei der Anzeige des Browsers nicht das gesamte Dokument in den
		// Speicher geladen werden muss
		//
		DataDocumentValue doc = tableRecord.getDocumentValue(Document.file);
		if(doc!=null)
			tableRecord.setValue(transaction, Document.file_name, doc.getName());

		// Bestehende Tags aktualisieren oder wenn gefordert neue anlegen
		// (dass aktualisieren vorhandener Tags wird in eine Scheduler erledigt)
		//
		String tag= tableRecord.getSaveStringValue(Document.tag);
  	IDataTable taggingTable = tableRecord.getAccessor().getTable(Tagging.NAME);
		StringTokenizer tokenizer = new StringTokenizer(tag);
		while(tokenizer.hasMoreTokens())
		{
			String token = tokenizer.nextToken();
			taggingTable.qbeClear();
			taggingTable.qbeSetKeyValue(Tagging.tag,token );
			if(taggingTable.search()==0)
			{
				// Stichwort ist noch nicht vorhanden. Dieses wird jetzt angelegt
				//
				IDataTableRecord tagRecord = taggingTable.newRecord(transaction);
				tagRecord.setStringValue(transaction,Tagging.tag, token);
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
		if(tableRecord.isDeleted())
			return;
		
		Context context = Context.getCurrent();
		IApplicationDefinition app = context.getApplicationDefinition();

	  String pkey = tableRecord.getSaveStringValue(Document.pkey);
	  String absolutePath = Bootstrap.getApplicationRootPath()+"application/"+app.getName()+"/"+app.getVersion().toShortString()+"/cache/"+pkey+".png";
	  if(tableRecord.isDeleted())
	  {
	    FileUtils.forceDelete(new File(absolutePath));
	  }
	  else
	  {
  		DataDocumentValue thumbnail = tableRecord.getDocumentValue(Document.thumbnail);
  		if(thumbnail!=null)
  		{
  		  FileUtils.writeByteArrayToFile(new File(absolutePath), thumbnail.getContent());
  		}
	  }
	  
		// Falls der Record zur löschen markiert wurde, wird der Owner darüber informiert
		//
		if(tableRecord.hasChangedValue(Document.request_for_delete_date) && !tableRecord.hasNullValue(Document.request_for_delete_date))
		{
			String email = tableRecord.getStringValue(Document.owner_email);
			if(email!=null)
			{
			  IDataTable configurationTable = tableRecord.getAccessor().getTable(Configuration.NAME);
			  configurationTable.search();
			  if(configurationTable.getSelectedRecord()==null)
			  {
			  	logger.error("No valid configuration data found");
			  	return;
			  }
			  
			  String smtpHost    = configurationTable.getSelectedRecord().getSaveStringValue(Configuration.smtp_server);
			  String smtpUser    = configurationTable.getSelectedRecord().getSaveStringValue(Configuration.smtp_user);
			  String smtpFrom    = configurationTable.getSelectedRecord().getSaveStringValue(Configuration.from_email);
			  String smtpPasswd  = configurationTable.getSelectedRecord().getSaveStringValue(Configuration.smtp_password);
			  String msg         = configurationTable.getSelectedRecord().getSaveStringValue(Configuration.notify_about_deletemark);
			  String subject     = configurationTable.getSelectedRecord().getSaveStringValue(Configuration.notify_about_deletesubject);

			  // Merge the messages with the table record
			  //
			  subject = new String(LetterFactory.createInstance(LetterFactory.MIMETYPE_TXT).transform(Context.getCurrent(), tableRecord,subject.getBytes()));
			  msg     = new String(LetterFactory.createInstance(LetterFactory.MIMETYPE_TXT).transform(Context.getCurrent(), tableRecord,msg.getBytes()));
			  
			  SendFactory.send(smtpHost, smtpUser, smtpPasswd, smtpFrom, new String[]{email}, new String[]{},subject, msg);
			}				
		}
	}
}
