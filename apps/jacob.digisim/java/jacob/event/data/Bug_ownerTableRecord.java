/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Feb 06 15:24:14 CET 2007
 */
package jacob.event.data;

import java.io.ByteArrayInputStream;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.ruleengine.RuleEngine;
import jacob.common.AppLogger;
import jacob.common.RuleCache;
import jacob.model.Bug_owner;
import jacob.model.My_bug;

import org.apache.commons.logging.Log;

/**
 *
 * @author andherz
 */
public class Bug_ownerTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: Bug_ownerTableRecord.java,v 1.1 2007/02/07 07:50:59 freegroup Exp $";
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
		byte[] rule = RuleCache.get(Context.getCurrent(), Bug_owner.NAME, "afterNewAction");
		if(rule!=null)
			RuleEngine.execute(new ByteArrayInputStream(rule),tableRecord);
	}



	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
		byte[] rule = RuleCache.get(Context.getCurrent(), Bug_owner.NAME, "afterDeleteAction");
		if(rule!=null)
			RuleEngine.execute(new ByteArrayInputStream(rule),tableRecord);
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
		byte[] rule = RuleCache.get(Context.getCurrent(), Bug_owner.NAME, "beforeCommitAction");
		if(rule!=null)
			RuleEngine.execute(new ByteArrayInputStream(rule),tableRecord);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
		byte[] rule = RuleCache.get(Context.getCurrent(), Bug_owner.NAME, "afterCommitAction");
		if(rule!=null)
			RuleEngine.execute(new ByteArrayInputStream(rule),tableRecord);
	}
}
