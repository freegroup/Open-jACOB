package de.tif.jacob.util.flow;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.util.flow.block.AbstractBlock;
import de.tif.jacob.util.flow.block.control.Start;
import de.tif.jacob.util.flow.castor.Block;
import de.tif.jacob.util.flow.castor.Dynamic_param;
import de.tif.jacob.util.flow.castor.Flow;
import de.tif.jacob.util.flow.castor.Static_param;

public final class FlowEngine
{
  public static final Object NULL = new Object();
  public static final Object REENTRY = new Object();
  private final Flow flow;
  private String reentryId;
  private final String entryEvent;
  public Object param = null;
  public Map<Block, Block> nextInnerBlock = new HashMap<Block,Block>();
  public Map<String, Object> variable = new HashMap<String,Object>();
  public final IDataTransaction currentTransaction;
  
  Map<String, List<Object>> block2staticParam = new HashMap<String, List<Object>>();
  Map<String, List<Object>> block2dynamicParam = new HashMap<String, List<Object>>();
  
  public final boolean autoCommit;

  public static Map<String, Object> execute(Context context, String rule, String entryEvent,  IDataTableRecord affectedRecord, Map<String, Object> presetVariables) throws Exception
  {
    return execute(context, IOUtils.toInputStream(rule.trim()), entryEvent, affectedRecord, presetVariables);
  }

  public static Map<String, Object> execute(Context context, InputStream ruleInputStream, String entryEvent, IDataTableRecord affectedRecord, Map<String, Object> presetVariables) throws Exception
  {
    Flow flow = (Flow) Flow.unmarshalFlow(new InputStreamReader(ruleInputStream));
    FlowEngine engine=null;
    try
    {
      IDataTransaction trans =null;
      if(affectedRecord!=null)
        trans = affectedRecord.getCurrentTransaction();
      if(trans==null)
        engine=new FlowEngine(context.getDataAccessor().newTransaction(),true, flow, entryEvent);
      else
        engine=new FlowEngine(trans,false, flow, entryEvent);
      
      if(presetVariables!=null)
        engine.variable = presetVariables;
      
      engine.execute(context, affectedRecord);
    }
    catch (InvocationTargetException exc)
    {
      if (exc.getCause() instanceof Exception)
        throw (Exception) exc.getCause();
    }
    return engine.variable;
  }

  private FlowEngine(IDataTransaction trans,boolean autoCommit, Flow flow, String entryEvent)
  {
    this.flow = flow;
    this.entryEvent = entryEvent;
    this.currentTransaction = trans;
    this.autoCommit = autoCommit;
  }

  public void execute(Context context, IDataTableRecord affectedRecord) throws Exception
  {
    for (int i = 0; i < this.flow.getBlockCount(); i++)
    {
      Block block = this.flow.getBlock(i);
      if(block.getImplementation().equals(Start.class.getName()) && StringUtils.equals(block.getProperty(),this.entryEvent))
      {
         Object result = execute(context, block, affectedRecord);
         if (result != REENTRY && autoCommit==true)
         {
           currentTransaction.commit();
           currentTransaction.close();
         }
         break;
      }
    }
  }

  private Object execute(Context context, Block block, IDataTableRecord affectedRecord) throws Exception
  {
    Object result = NULL;
    
    if (this.reentryId == null)
    {
      block2staticParam.put(block.getId(), new ArrayList<Object>());
      block2dynamicParam.put(block.getId(), new ArrayList<Object>());
    }
    
    List<Object> staticParam = block2staticParam.get(block.getId());
    List<Object> dynamicParam = block2dynamicParam.get(block.getId());
    
    if(this.reentryId==null || !StringUtils.equals( this.reentryId ,block.getId()))
    {
      Static_param sp = block.getStatic_param();
      if (sp != null)
      {
        for (int i = 0; i < sp.getBlockCount(); i++)
        {
          Block sb = sp.getBlock(i);
          if (sb != null)
          {
            Object param = execute(context, sb, affectedRecord);
            if (param == REENTRY)
              return REENTRY;
            // kann bei einem Reentry passieren wenn der Block zuvor abgearbeitet wurde
            if(staticParam!=null)
              staticParam.add(param);
          }
          else
          {
            staticParam.add(NULL);
          }
        }
      }
      Dynamic_param dp = block.getDynamic_param();
      if (dp != null)
      {
        for (int i = 0; i < dp.getBlockCount(); i++)
        {
          Block b = dp.getBlock(i);
          if (b != null)
          {
            Object param = execute(context, b, affectedRecord);
            if (param == REENTRY)
              return REENTRY;
            // kann bei einem Reentry passieren wenn der Block zuvor abgearbeitet wurde
            if(dynamicParam!=null)
              dynamicParam.add(param);
          }
        }
      }
    }
    
    
    String implementation = block.getImplementation();
    if (implementation != null)
    {
      AbstractBlock instance = (AbstractBlock) Class.forName(implementation).newInstance();
      if(this.reentryId==null)
      {
        result = instance.execute(this, context, affectedRecord, block, staticParam, dynamicParam);
      }
      else if (StringUtils.equals(block.getId(), this.reentryId))
      {
        this.reentryId = null;
        result = instance.reentry(this, context, affectedRecord, block);
      }
    }
    
    if (result != REENTRY)
    {
      block2staticParam.remove(block.getId());
      block2dynamicParam.remove(block.getId());
      
      // Es kann sein, dass ein inner Block ausgeführt werden muss bevor der Nachfolger kommt
      //
      Block nextInner = this.nextInnerBlock.get(block);
      if(nextInner !=null)
      {
         result =execute(context,nextInner,affectedRecord);
         if(result!=REENTRY)
         {
           this.nextInnerBlock.remove(block);
         }
         else
         {
           return result;
         }
      }
      
      Block next = block.getBlock();
      if (next != null)
      {
        result = execute(context, next, affectedRecord);
      }
    }
    else
    {
      this.reentryId = block.getId();
    }
    return result;
  }

  public void setVariable(String key, Object value)
  {
     this.variable.put(key, value);
  }
  
  public Object getVariable(String key)
  {
     return this.variable.get(key);
  }
  
  public static void main(String[] args)
  {
    try
    {
      InputStream in = FlowEngine.class.getResourceAsStream("test.xml");
      FlowEngine.execute(Context.getCurrent(), in, "onClick", null, null);
    }
    catch (Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
