/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jun 28 18:37:38 CEST 2010
 */
package jacob.event.data;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.definition.IRelationSet;
import jacob.common.AppLogger;
import jacob.common.BoUtil;
import jacob.common.JournalManager;
import jacob.model.Bo;
import jacob.model.Document;
import jacob.model.Folder;

import org.apache.commons.logging.Log;

/**
 *
 * @author andherz
 */
public class FolderTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: FolderTableRecord.java,v 1.1 2010-09-17 08:42:22 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

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
	  Context context = Context.getCurrent();
	  String boPkey= tableRecord.getStringValue(Folder.pkey);
	  
	  // Alle Kinder von dem Ordner löschen
	  //
	  IDataAccessor acc = context.getDataAccessor().newAccessor();
	  IDataTable boTable = acc.getTable(Bo.NAME);
	  boTable.qbeSetKeyValue(Bo.parent_bo_key, boPkey);
	  boTable.search(IRelationSet.LOCAL_NAME);
	  for(int i=0; i<boTable.recordCount(); i++)
	  {
	    IDataTableRecord childBo = boTable.getRecord(i);
	    if(childBo.getValue(Bo.folder_key)!=null)
	      childBo.getLinkedRecord(Folder.NAME).delete(transaction);
	    else
	      childBo.getLinkedRecord(Document.NAME).delete(transaction);
	  }
	  
	  // Das Bo löschen
	  //
    IDataTableRecord boRecord = BoUtil.findByPkey(Context.getCurrent(), boPkey);
    boRecord.delete(transaction);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
    JournalManager.append(tableRecord);
    
    // Falls ein Ordner gelöscht wird, dann werden die dokumente welche darin enthalten
    // sind nicht alle einzeln aufgeführt. Kann man ja wenn gefordert wieder ändern.
    JournalManager.disableHistoryForTransaction(transaction);


    // Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
		//
		if(tableRecord.isDeleted())
			return;

		tableRecord.setValue(transaction, Folder.change_date, "NOW");
		if(!tableRecord.isNew())
		{
		  IDataTableRecord boRecord = BoUtil.findByPkey(Context.getCurrent(), tableRecord.getStringValue(Folder.pkey));
		  boRecord.setValue(transaction, Bo.name, tableRecord.getStringValue(Folder.name));
		}
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}
