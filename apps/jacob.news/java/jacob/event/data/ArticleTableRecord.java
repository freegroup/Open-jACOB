/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 *
 * @author andherz
 */
public class ArticleTableRecord extends DataTableRecordEventHandler
{

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	  if(tableRecord.isDeleted())
	    return;
	  
	  String           pkey   = tableRecord.getSaveStringValue("pkey");
	  IDataTableRecord parent = tableRecord.getLinkedRecord("parent_article");
	  IDataTableRecord group  = tableRecord.getLinkedRecord("group");
	  
	  tableRecord.setStringValue(transaction,"message_id","<"+tableRecord.getSaveStringValue("pkey")+"@jacob>");
	  if(parent!=null)
	  {
	    String groupName = group.getStringValue("name");
	    tableRecord.setStringValue(transaction,"reference",parent.getSaveStringValue("message_id"));
	    tableRecord.setStringValue(transaction,"xref","localhost "+groupName+":"+parent.getSaveStringValue("pkey"));
	  }
	  else
	  {
	    tableRecord.setStringValue(transaction,"reference","localhost");
	    tableRecord.setStringValue(transaction,"xref","");
	  }
	}
}
