/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Oct 31 09:43:03 CET 2006
 */
package jacob.event.data;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.UserException;
import jacob.common.AppLogger;
import jacob.model.Audit;

import org.apache.commons.logging.Log;

/**
 *
 * @author andherz
 */
public class AuditTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: AuditTableRecord.java,v 1.1 2007/11/25 22:17:55 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

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

		// Falls ein Audit als abgeschlossen markiert wird, dann muss der Bericht ausgefüllt sein
		// (der Ordnung zu Liebe ;-)
		//
	  if(tableRecord.getSaveStringValue(Audit.state).equals(Audit.state_ENUM._Abgeschlossen))
	  {
	  	if(tableRecord.getSaveStringValue(Audit.protocol).length()==0)
	  	{
	  		throw new UserException("It is required to fill out the protocol if you mark the audit as closed.");
	  	}
	  }
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}
