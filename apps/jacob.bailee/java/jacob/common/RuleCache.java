package jacob.common;

import jacob.model.Rule;

import java.util.HashMap;
import java.util.Map;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

public class RuleCache 
{
	private static Map<String, RuleDocument> hook2rule = null;
	
	public synchronized static void load(Context context) throws Exception
	{
    if(hook2rule==null)
      hook2rule =new HashMap<String,RuleDocument>();
      
	  
		// Create a new DataAccessor to avoid side effects
		// (Don't touch the current data in the cache)
		//
		IDataAccessor accessor = context.getDataAccessor().newAccessor();
		IDataTable ruleTable =  accessor.getTable(Rule.NAME);
		ruleTable.search();
		if(ruleTable.recordCount()==0)
		{
		  /*
			// no rules in the database => Load the initial ruleset into the db
			// Es muss immer ein Configuration Record existieren
			//
			IDataTransaction trans = accessor.newTransaction();
			try
			{
					IDataTableRecord record = ruleTable.newRecord(trans);
					record.setValue(trans, Rule.tablealias, Bug.NAME);
					record.setValue(trans, Rule.action, "beforeCommitAction");
					InputStream stream =   ResourceProvider.class.getResourceAsStream("initialrules/bug_beforeCommitAction.xml");
					byte[] content = IOUtils.toByteArray(stream);
					stream.close();
					record.setDocumentValue(trans, Rule.rule, DataDocumentValue.create("rule1.xml",content));
          insert(context, alias,action,rule); TODO

					record = ruleTable.newRecord(trans);
					record.setValue(trans, Rule.tablealias, Bug.NAME);
					record.setValue(trans, Rule.action, "afterNewAction");
					stream =   ResourceProvider.class.getResourceAsStream("initialrules/bug_afterNewAction.xml");
					content = IOUtils.toByteArray(stream);
					stream.close();
					record.setDocumentValue(trans, Rule.rule, DataDocumentValue.create("rule2.xml",content));
          insert(context, "bug",action,rule); TODO

					trans.commit();
			}
			catch(Exception exc)
			{
				ExceptionHandler.handle(exc);
			}
			finally
			{
				trans.close();
			}
			*/
		}
		else
		{
			for(int i=0;i<ruleTable.recordCount();i++)
			{
				IDataTableRecord tableRecord = ruleTable.getRecord(i);
				String alias  = tableRecord.getSaveStringValue(Rule.tablealias);
				String action = tableRecord.getSaveStringValue(Rule.action);
				byte[] rule   = tableRecord.getDocumentValue(Rule.rule).getContent();
				insert(context, alias,action,new RuleDocument(tableRecord.getSaveStringValue(Rule.pkey), rule));
			}
		}
	}
	
	public synchronized static void insert(Context context, String alias,String event, RuleDocument rule) throws Exception
	{
	  if(hook2rule==null)
	    hook2rule =new HashMap<String,RuleDocument>();
	    
		hook2rule.put(alias+"."+event, rule);
	}
	
	 public synchronized static boolean has(Context context, String alias, String event) throws Exception 
	  {
	    // initial load of the rules
	    //
	    if(hook2rule==null)
	      load(context);
	    
	    return hook2rule.get(alias+"."+event)!=null;
	  }

	/**
	 * Returns ALWAYS a valid Rule document
	 * 
	 * @param context
	 * @param alias
	 * @param event
	 * @return
	 * @throws Exception
	 */
	public synchronized static RuleDocument get(Context context, String alias, String event) throws Exception 
	{
		// initial load of the rules
		//
		if(hook2rule==null)
			load(context);
		
		RuleDocument rule = hook2rule.get(alias+"."+event);
		
		if(rule ==null)
		{
	    IDataAccessor accessor = context.getDataAccessor().newAccessor();
      IDataTransaction trans = accessor.newTransaction();
      try
      {
  	    IDataTable ruleTable =  accessor.getTable(Rule.NAME);
        IDataTableRecord record = ruleTable.newRecord(trans);
        record.setValue(trans, Rule.tablealias, alias);
        record.setValue(trans, Rule.action, event);
        rule = new RuleDocument(record.getSaveStringValue(Rule.pkey), record.getDocumentValue(Rule.rule).getContent());
        trans.commit();
        insert(context, alias,event,rule);
      }
      finally
      {
        trans.close();
      }
		}
		return rule;
	}

	public synchronized static void remove(Context context, String alias, String action) throws Exception
	{
	  if(hook2rule!=null)
	    hook2rule.remove(alias+"."+action);
	}
}
