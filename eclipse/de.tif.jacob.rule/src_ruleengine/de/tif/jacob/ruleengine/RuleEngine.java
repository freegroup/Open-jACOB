package de.tif.jacob.ruleengine;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.deployment.DeployManager;
import de.tif.jacob.letter.LetterFactory;
import de.tif.jacob.ruleengine.castor.BusinessObjectMethod;
import de.tif.jacob.ruleengine.castor.ConditionalExit;
import de.tif.jacob.ruleengine.castor.Rule;
import de.tif.jacob.ruleengine.castor.Ruleset;
import de.tif.jacob.util.StringUtil;

public final class RuleEngine 
{
	private final Map rules = new HashMap();
	private final Context context;
  
  public static void execute(InputStream ruleInputStream,IDataTableRecord affectedRecord) throws Exception
  {
  	Ruleset ruleset=(Ruleset)Ruleset.unmarshalRuleset(new InputStreamReader(ruleInputStream));
    try
    {
      new RuleEngine(Context.getCurrent()).execute(ruleset, affectedRecord);
    }
    catch(InvocationTargetException exc)
    {
      if(exc.getCause() instanceof Exception)
        throw (Exception)exc.getCause();
    }
  }
  
  private RuleEngine(Context context)
  {
    this.context = context;
  }
  
	private void execute(Ruleset ruleset,IDataTableRecord affectedRecord) throws Exception
	{
    for(int i =0; i<ruleset.getRuleCount();i++)
    {
    	Rule rule = ruleset.getRule(i);
    	rules.put(rule.getRuleId(),rule);
    }
    Rule startRule = (Rule)rules.get("start");
    if(startRule.getBusinessObjectMethod().getNextRuleId()!=null)
      execute((Rule)rules.get(startRule.getBusinessObjectMethod().getNextRuleId()),affectedRecord);
	}
	
	private void execute(Rule rule,IDataTableRecord affectedRecord) throws Exception
	{
    if(rule==null)
      return;
		if(rule.getDecision()!=null)
			execute(rule.getDecision(),affectedRecord);
		else 
			execute(rule.getBusinessObjectMethod(),affectedRecord);
	}
	
	/**
	 * 
	 * @param d
	 * @throws Exception
	 */
	private void execute(de.tif.jacob.ruleengine.castor.Decision d,IDataTableRecord affectedRecord) throws Exception
	{
		 Decision des  =(Decision)ClassProvider.createInstance(context.getApplicationDefinition(),d.getDecisionClass());
		 des.init(context,affectedRecord);
		 ArrayList params = new ArrayList();
		 for(int i=0; i<d.getParameterCount();i++)
			 params.add(resolveParameter(d.getParameter(i)));
		 
		 Class[] methodParams = new Class[params.size()];
		 for(int i=0;i<methodParams.length;i++)
			 methodParams[i]=String.class;
		 Method method = des.getClass().getMethod(d.getMethodName(),methodParams);
		 
		 Object result = method.invoke(des,params.toArray()) ;
     if (result!=null)
     {
       String resultString = result.toString();
       for(int i=0; i<d.getConditionalExitCount();i++)
       {
         ConditionalExit exit = d.getConditionalExit(i);
         if(resultString.equals(exit.getValue()))
         {
           execute((Rule)rules.get(exit.getRuleId()),affectedRecord);
           break;
         }
       }
     }
	}
	
	/**
	 * 
	 * @param bom
	 * @throws Exception
	 */
	private void execute(BusinessObjectMethod bom,IDataTableRecord affectedRecord) throws Exception
	{
		 BusinessObject bo = (BusinessObject)ClassProvider.createInstance(context.getApplicationDefinition(),bom.getBusinessClass());
     bo.init(context,affectedRecord);
		 ArrayList params = new ArrayList();
		 for(int i=0; i<bom.getParameterCount();i++)
			 params.add(resolveParameter(bom.getParameter(i)));
		 
		 Class[] methodParams = new Class[params.size()];
		 for(int i=0;i<methodParams.length;i++)
			 methodParams[i]=String.class;
		 Method method = bo.getClass().getMethod(bom.getMethodName(),methodParams);
		 
		 method.invoke(bo,params.toArray()) ;
		 
		 if(bom.getNextRuleId()!=null)
			 execute((Rule)rules.get(bom.getNextRuleId()),affectedRecord);
	}
  
  private String resolveParameter(String input) throws Exception
  {
    // replace the db_field(.....) with the corresponding values
    //
    input = new String(LetterFactory.createInstance(LetterFactory.MIMETYPE_TXT).transform(context,context.getDataAccessor(),input.getBytes()));
    input = StringUtil.replace(input,"{USERID}",context.getUser().getKey());
    input = StringUtil.replace(input,"{USERNAME}",context.getUser().getFullName());
    input = StringUtil.replace(input,"{LOGINID}",context.getUser().getLoginId());
    return input;
  }
  
}
