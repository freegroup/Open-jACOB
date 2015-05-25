/*
 * Created on 23.04.2004
 *
 */
package jacob.event.data;

import jacob.model.Quickcalls;
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
public class QuickcallsTableRecord extends DataTableRecordEventHandler
{
    static public final transient String RCS_ID = "$Id: QuickcallsTableRecord.java,v 1.1 2007/09/03 13:22:09 achim Exp $";
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
	  String sName = tableRecord.getSaveStringValue(Quickcalls.name);
    sName = sName.replaceAll(" ", "_");
    tableRecord.setValue(transaction, Quickcalls.name, sName);
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception 
	{
	}

}