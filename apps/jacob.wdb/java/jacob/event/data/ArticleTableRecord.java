/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sat Aug 14 18:35:06 CEST 2010
 */
package jacob.event.data;

import jacob.model.Article;
import jacob.model.Attachment;
import jacob.model.Chapter;
import jacob.model.Link;
import jacob.model.Menutree;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.IDataTransaction.EmbeddedTransactionMode;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.RecordNotFoundException;

/**
 *
 * @author andherz
 */
public class ArticleTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: ArticleTableRecord.java,v 1.4 2010-09-16 17:53:38 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.4 $";

	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	  // Alle Verweise auf chapter zurücksetzen
	  //
	  try
    {
	    // we need another instance, since one table record instance cannot be handled by more than one transaction at once
      IDataTableRecord anotherInstanceOfArticleRecord = tableRecord.getTable().loadRecord(tableRecord.getPrimaryKeyValue());
      
      IDataTransaction preTrans = transaction.newEmbeddedTransaction(EmbeddedTransactionMode.PREPEND);
      anotherInstanceOfArticleRecord.setValue(preTrans, Article.chapter01_key, null);
      anotherInstanceOfArticleRecord.setValue(preTrans, Article.chapter02_key, null);
      anotherInstanceOfArticleRecord.setValue(preTrans, Article.chapter03_key, null);
      anotherInstanceOfArticleRecord.setValue(preTrans, Article.chapter04_key, null);
      anotherInstanceOfArticleRecord.setValue(preTrans, Article.chapter05_key, null);
      anotherInstanceOfArticleRecord.setValue(preTrans, Article.chapter06_key, null);
    }
	  catch (RecordNotFoundException ex)
	  {
	    // already deleted
	    return;
	  }
	  
    String pkey = tableRecord.getStringValue(Article.pkey);
    Context context = Context.getCurrent();

    // Alle Chapter zu dem Artikel löschen
    //
    {
      IDataTable table = context.getDataTable(Chapter.NAME);
      table.qbeClear();
      table.qbeSetKeyValue(Chapter.article_key, pkey);
      table.searchAndDelete(transaction);
    }
    
    // Alle Attachments zu dem Artikel löschen
    //
    {
      IDataTable table = context.getDataTable(Attachment.NAME);
      table.qbeClear();
      table.qbeSetKeyValue(Attachment.article_key, pkey);
      table.searchAndDelete(transaction);
    }
    
    // Alle Links zu dem Artikel löschen
    //
    {
      IDataTable table = context.getDataTable(Link.NAME);
      table.qbeClear();
      table.qbeSetKeyValue(Link.article_key, pkey);
      table.searchAndDelete(transaction);
    }
    
    // Alle Verweise in einem Menu entfernen
	  //
	  {
      IDataTable table = context.getDataTable(Menutree.NAME);
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
		// Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
		//
		if(tableRecord.isDeleted())
			return;

		// enter your code here
	}

	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}
