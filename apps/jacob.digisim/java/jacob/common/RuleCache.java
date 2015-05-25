package jacob.common;

import jacob.model.Rule;
import jacob.resources.ResourceProvider;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.ExceptionHandler;

public class RuleCache 
{
	private static Map<String, byte[]> hook2rule = new HashMap<String,byte[]>();
	
	public synchronized static void load(Context context) throws Exception
	{
		// Create a new DataAccessor to avoid side effects
		// (Don't touch the current data in the cache)
		//
		IDataAccessor accessor = context.getDataAccessor().newAccessor();
		IDataTable ruleTable =  accessor.getTable(Rule.NAME);
		ruleTable.search();
		if(ruleTable.recordCount()==0)
		{
			// no rules in the database => Load the initial ruleset into the db
			// Es muss immer ein Configuration Record existieren
			//
			IDataTransaction trans = accessor.newTransaction();
			try
			{
/*
					IDataTableRecord record = ruleTable.newRecord(trans);
					record.setValue(trans, Rule.tablealias, Rule.NAME);
					record.setValue(trans, Rule.action, "beforeCommitAction");
					InputStream stream =   ResourceProvider.class.getResourceAsStream("initialrules/bug_beforeCommitAction.xml");
					byte[] content = IOUtils.toByteArray(stream);
					stream.close();
					record.setDocumentValue(trans, Rule.rule, DataDocumentValue.create("rule1.xml",content));

					record = ruleTable.newRecord(trans);
					record.setValue(trans, Rule.tablealias, Rule.NAME);
					record.setValue(trans, Rule.action, "afterNewAction");
					stream =   ResourceProvider.class.getResourceAsStream("initialrules/bug_afterNewAction.xml");
					content = IOUtils.toByteArray(stream);
					stream.close();
					record.setDocumentValue(trans, Rule.rule, DataDocumentValue.create("rule2.xml",content));

					trans.commit();
*/					
			}
			catch(Exception exc)
			{
				ExceptionHandler.handle(exc);
			}
			finally
			{
				trans.close();
			}
			
		}
		else
		{
			for(int i=0;i<ruleTable.recordCount();i++)
			{
				IDataTableRecord tableRecord = ruleTable.getRecord(i);
				String alias  = tableRecord.getSaveStringValue(Rule.tablealias);
				String action = tableRecord.getSaveStringValue(Rule.action);
				byte[] rule   = tableRecord.getDocumentValue(Rule.rule).getContent();
				insert(context, alias,action,rule);
			}
		}
	}
	
	public synchronized static void insert(Context context, String alias, String event, byte[] rule) throws Exception
	{
		hook2rule.put(alias+"."+event, rule);
	}
	
	public synchronized static byte[] get(Context context, String alias, String event) throws Exception 
	{
		// initial load of the rules
		//
		if(hook2rule.size()==0)
			load(context);
		
		return hook2rule.get(alias+"."+event);
	}

	public synchronized static void remove(Context context, String alias, String action) throws Exception
	{
		hook2rule.remove(alias+"."+action);
	}
}
