/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sun Feb 04 20:27:32 CET 2007
 */
package jacob.event.data;

import java.io.ByteArrayInputStream;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.ruleengine.RuleEngine;
import jacob.common.AppLogger;
import jacob.common.RuleCache;
import jacob.model.Circuit;
import jacob.model.My_circuit;
import jacob.model.Room;

import org.apache.commons.logging.Log;

/**
 *
 * @author andherz
 */
public class My_circuitTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: My_circuitTableRecord.java,v 1.2 2007/08/23 05:13:04 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
		String template ="<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"+
										"<circuit></circuit>";		
		tableRecord.setValue(transaction, My_circuit.xml, template);
		tableRecord.setValue(transaction, My_circuit.owner_key, Context.getCurrent().getUser().getKey());
		
		byte[] rule = RuleCache.get(Context.getCurrent(), My_circuit.NAME, "afterNewAction");
		if(rule!=null)
			RuleEngine.execute(new ByteArrayInputStream(rule),tableRecord);
	}



	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
		byte[] rule = RuleCache.get(Context.getCurrent(), My_circuit.NAME, "afterDeleteAction");
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
		byte[] rule = RuleCache.get(Context.getCurrent(), My_circuit.NAME, "beforeCommitAction");
		if(rule!=null)
			RuleEngine.execute(new ByteArrayInputStream(rule),tableRecord);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
		byte[] rule = RuleCache.get(Context.getCurrent(), My_circuit.NAME, "afterCommitAction");
		if(rule!=null)
			RuleEngine.execute(new ByteArrayInputStream(rule),tableRecord);
	}
}
