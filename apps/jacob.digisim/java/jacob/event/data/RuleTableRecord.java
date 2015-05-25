/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Nov 17 20:29:57 CET 2006
 */
package jacob.event.data;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import jacob.common.AppLogger;
import jacob.common.RuleCache;
import jacob.model.Rule;

import org.apache.commons.logging.Log;

/**
 *
 * @author andherz
 */
public class RuleTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: RuleTableRecord.java,v 1.1 2007/02/02 22:26:46 freegroup Exp $";
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
		String template ="<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"+
										"<ruleset xmlns=\"http://www.example.org/ruleset\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ruleset\">\n"+
									  "<rule posX=\"110\" posY=\"205\" ruleId=\"start\">\n"+
								    "<businessObjectMethod xmlns=\"\"/>\n"+
								    "</rule>\n"+
										"</ruleset>";		
		DataDocumentValue doc = DataDocumentValue.create("any.ruleset", template.getBytes());
		tableRecord.setDocumentValue(transaction, Rule.rule, doc);
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
		String alias  = tableRecord.getSaveStringValue(Rule.tablealias);
		String action = tableRecord.getSaveStringValue(Rule.action);
		if(tableRecord.isDeleted())
		{
			RuleCache.remove(Context.getCurrent(), alias, action);
		}
		else
		{
			byte[] rule   = tableRecord.getDocumentValue(Rule.rule).getContent();
			RuleCache.insert(Context.getCurrent(), alias, action, rule);
		}
	}
}
