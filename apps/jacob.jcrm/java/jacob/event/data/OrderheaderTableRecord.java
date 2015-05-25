/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 *
 * @author andreas
 */
public class OrderheaderTableRecord extends DataTableRecordEventHandler
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
    // delete all line items as well
    //
    IDataTable orderlineitemTable = tableRecord.getAccessor().getTable("orderlineitem");
    orderlineitemTable.qbeClear();
    orderlineitemTable.qbeSetKeyValue("orderheader_key", tableRecord.getValue("pkey"));
    orderlineitemTable.searchAndDelete(transaction);
    
    // delete all documents as well
    //
    IDataTable documentTable = tableRecord.getAccessor().getTable("document");
    documentTable.qbeClear();
    documentTable.qbeSetKeyValue("orderheader_key", tableRecord.getValue("pkey"));
    documentTable.searchAndDelete(transaction);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord order, IDataTransaction transaction) throws Exception
	{
    // Set agent, i.e. the current user
    //
    IDataTable agent = order.getAccessor().getTable("orderAgent");
    agent.qbeClear();
    agent.qbeSetKeyValue("pkey", transaction.getUser().getKey());
    agent.search();
    if (agent.recordCount() == 1)
    {
      order.setLinkedRecord(transaction, agent.getRecord(0));
    }
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction trans) throws Exception
  {
    if (tableRecord.isDeleted())
      return;

    if (tableRecord.isNew())
    {
      IDataTableRecord quoteheader = tableRecord.getLinkedRecord("quoteheader");
      if (quoteheader != null)
      {
        // Set status of quote to closed, if an order is created
        // This will fail, if quote is locked by somebody else.
        // Nevertheless, this should not be the case since we have already
        // locked
        // this record within this transaction. At least if button "Create
        // order"
        // has been activated.
        quoteheader.setValue(trans, "status", "Closed");
      }
    }
  }
}
