/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Aug 06 15:01:39 CEST 2010
 */
package jacob.event.data;

import jacob.model.Active_news;

import java.util.Date;
import java.util.Locale;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.util.DatetimeUtil;

/**
 *
 * @author andherz
 */
public class Active_newsTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: Active_newsTableRecord.java,v 1.1 2010-08-06 16:00:27 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";


	@Override
  public void beforeSearchAction(IDataBrowser browser, IDataTable table, IRelationSet relationSet) throws Exception
  {
    Date now = new Date();
    now.setYear(70); // 1970
    table.qbeSetValue(Active_news.normalized_start, "<"+now.getTime());
    table.qbeSetValue(Active_news.normalized_end, ">"+now.getTime());
  }

  @Override
  public void beforeSearchAction(IDataTable table) throws Exception
  {
    Date now = new Date();
    now.setYear(70); // 1970
    table.qbeSetValue(Active_news.normalized_start, "<"+now.getTime());
    table.qbeSetValue(Active_news.normalized_end, ">"+now.getTime());
  }

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

		// enter your code here
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}
