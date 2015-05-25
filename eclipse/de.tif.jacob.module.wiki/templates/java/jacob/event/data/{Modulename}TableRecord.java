/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sun Jul 27 10:13:05 CEST 2008
 */
package jacob.event.data;

import java.util.List;
import java.util.Set;

import info.bliki.wiki.model.WikiModel;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import jacob.common.AppLogger;
import jacob.model.{Modulename};
import jacob.model.{Modulename}_link;

import org.apache.commons.logging.Log;

/**
 *
 * @author andherz
 */
public class {Modulename}TableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: {Modulename}TableRecord.java,v 1.2 2008/07/31 07:44:35 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

  public boolean filterSearchAction(IDataBrowserRecord browserRecord) throws Exception
  {
    Context context =Context.getCurrent();
    Object obj = context.getProperty("filterOrphanedPages");
    if(obj!=null)
    {
      Set linkTargets = (Set)context.getProperty("linkTargets");
      Set alreadyAddedRecords = (Set)context.getProperty("alreadyAddedRecords");
      String link_name = browserRecord.getSaveStringValue("browserLink_name");
      if(!alreadyAddedRecords.contains(link_name))
      {
        alreadyAddedRecords.add(link_name);
        return !linkTargets.contains(link_name);
      }
      return false;
    }
    return true;
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

    String wikitext = tableRecord.getSaveStringValue({Modulename}.wikitext);
    String wikiPageName = tableRecord.getStringValue({Modulename}.link_name);
    WikiModel model = new WikiModel("${image}","${title}");
    model.render(wikitext);
    Set<String> links = model.getLinks();
    
    IDataTable wikilinkTable = tableRecord.getAccessor().getTable({Modulename}_link.NAME);
    wikilinkTable.qbeClear();
    wikilinkTable.qbeSetKeyValue({Modulename}_link.source, wikiPageName);
    wikilinkTable.searchAndDelete(transaction);
    
    // neue Links anlegen
    for (String link : links)
    {
      IDataTableRecord wikilinkRecord =wikilinkTable.newRecord(transaction);
      wikilinkRecord.setValue(transaction,{Modulename}_link.source, wikiPageName);
      wikilinkRecord.setValue(transaction,{Modulename}_link.target, link);
    }
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}
