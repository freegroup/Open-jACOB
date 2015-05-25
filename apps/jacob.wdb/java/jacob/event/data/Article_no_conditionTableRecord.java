/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Oct 20 10:17:30 CEST 2010
 */
package jacob.event.data;

import jacob.model.Article_no_condition;
import jacob.model.Attachment;
import jacob.model.Chapter;
import jacob.model.History;
import jacob.model.Link;
import jacob.model.Menutree;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.IDataTransaction.EmbeddedTransactionMode;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.core.exception.UserException;

/**
 *
 * @author andreas
 */
public final class Article_no_conditionTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: Article_no_conditionTableRecord.java,v 1.2 2010-10-21 23:07:37 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
    IDataAccessor acc = tableRecord.getAccessor().newAccessor();
    
    // Alle untergeordneten Artikel löschen, sofern diese nicht separat im Papierkorb liegen
    //
    {
      IDataTable table = acc.getTable(tableRecord.getTableAlias());
      table.qbeSetKeyValue(Article_no_condition.article_parent_key, tableRecord.getValue(Article_no_condition.pkey));
      table.search();
      for (int i = 0; i < table.recordCount(); i++)
      {
        IDataTableRecord childRecord = table.getRecord(i);
        if (Article_no_condition.lifecycle_ENUM._recyclebin.equals(childRecord.getValue(Article_no_condition.lifecycle)))
          throw new UserException("Löschen nicht möglich.\n Es müssen zuerst alle untergeordneten Artikel im Papierkorb gelöscht werden!");
        childRecord.delete(transaction);
      }
    }
    
    // Alle Verweise auf chapter zurücksetzen
    //
    try
    {
      // we need another instance, since one table record instance cannot be handled by more than one transaction at once
      IDataTableRecord anotherInstanceOfArticleRecord = tableRecord.getTable().loadRecord(tableRecord.getPrimaryKeyValue());
      
      IDataTransaction preTrans = transaction.newEmbeddedTransaction(EmbeddedTransactionMode.PREPEND);
      anotherInstanceOfArticleRecord.setValue(preTrans, Article_no_condition.chapter01_key, null);
      anotherInstanceOfArticleRecord.setValue(preTrans, Article_no_condition.chapter02_key, null);
      anotherInstanceOfArticleRecord.setValue(preTrans, Article_no_condition.chapter03_key, null);
      anotherInstanceOfArticleRecord.setValue(preTrans, Article_no_condition.chapter04_key, null);
      anotherInstanceOfArticleRecord.setValue(preTrans, Article_no_condition.chapter05_key, null);
      anotherInstanceOfArticleRecord.setValue(preTrans, Article_no_condition.chapter06_key, null);
    }
    catch (RecordNotFoundException ex)
    {
      // already deleted
      return;
    }
    
    String pkey = tableRecord.getStringValue(Article_no_condition.pkey);

    // Alle Chapter zu dem Artikel löschen
    //
    {
      IDataTable table = acc.getTable(Chapter.NAME);
      table.qbeClear();
      table.qbeSetKeyValue(Chapter.article_key, pkey);
      table.searchAndDelete(transaction);
    }
    
    // Alle Attachments zu dem Artikel löschen
    //
    {
      IDataTable table = acc.getTable(Attachment.NAME);
      table.qbeClear();
      table.qbeSetKeyValue(Attachment.article_key, pkey);
      table.searchAndDelete(transaction);
    }
    
    // Alle Links zu dem Artikel löschen
    //
    {
      IDataTable table = acc.getTable(Link.NAME);
      table.qbeClear();
      table.qbeSetKeyValue(Link.article_key, pkey);
      table.searchAndDelete(transaction);
    }
    
    // Alle Historyeinträge zu dem Artikel löschen
    //
    {
      IDataTable table = acc.getTable(History.NAME);
      table.qbeClear();
      table.qbeSetKeyValue(History.article_key, pkey);
      table.searchAndDelete(transaction);
    }
    
    // Alle Verweise in einem Menu entfernen
    //
    {
      IDataTable table = acc.getTable(Menutree.NAME);
      table.qbeClear();
      table.qbeSetKeyValue(Menutree.article_key, pkey);
      table.search();
      for(int i=0;i<table.recordCount(); i++)
      {
        IDataTableRecord record  =table.getRecord(i);
        record.setValue(transaction, Menutree.article_key, null);
      }
    }
	}

	public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}
