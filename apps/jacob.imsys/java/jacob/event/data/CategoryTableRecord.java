/*
 * Created on 23.04.2004
 *
 */
package jacob.event.data;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 * Table Rules für die Tablelle <b>Category</b><br>
 * Es werden berechnete Felder der Tabelle angepasst.<br>
 * 
 * @author Andreas Herz
 *
 */
public class CategoryTableRecord extends DataTableRecordEventHandler
{
    static public final transient String RCS_ID = "$Id: CategoryTableRecord.java,v 1.1 2005/06/02 16:29:44 mike Exp $";
    static public final transient String RCS_REV = "$Revision: 1.1 $";

  /* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord tableRecord,	IDataTransaction transaction) throws Exception 
	{
	}
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterDeleteAction(IDataTableRecord tableRecord,		IDataTransaction transaction) throws Exception 
	{
	}
	
	/**
	 * Der <code>categorylevel</code> einer Kategorie wird anhand des Vatergewerkes berechent.<br>
	 * <br>
	 * Ein Kindgewerk hat immer einen Level höher als der Vater.<br>
	 * Hat ein Gewerk keinen Vater ist sein Level=1.<br>
	 * 
	 * 
	 * @param tableRecord Der Tablerecord welcher in der Datebank festgeschrieben werden soll
	 * @param transaction Die Transaktion in der die Modifikation des Rekord passiert. 
	 */
	public void beforeCommitAction(IDataTableRecord tableRecord,	IDataTransaction transaction) throws Exception 
	{
	  IDataTableRecord parent=tableRecord.getLinkedRecord("parentcategory");
	  long level=1;
	  if(parent!=null)
	  {
	    Long categorylevel = parent.getLongValue("categorylevel");
	    if(categorylevel!=null)
	    {
	      level=categorylevel.longValue()+1;
	    }
	  }
	  tableRecord.setLongValue(transaction,"categorylevel",level);
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception 
	{
	}

}