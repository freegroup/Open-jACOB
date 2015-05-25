/*
 * Created on 03.11.2010
 *
 */
package de.tif.jacob.util.flow.block;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.flow.FlowEngine;
import de.tif.jacob.util.flow.castor.Block;

public class AbstractBlock
{
  public Object execute(FlowEngine engine, de.tif.jacob.core.Context context, IDataTableRecord affectedRecord, Block currentBlock, List<Object> sparams, List<Object> dparams)
  {
    return FlowEngine.NULL;
  }

  public Object reentry(FlowEngine engine, de.tif.jacob.core.Context context, IDataTableRecord affectedRecord, Block currentBlock)
  {
    return FlowEngine.NULL;
  }


  protected final BigDecimal toDecimal(List<Object> params, int index)
  {
    try
    {
      Object test = params.get(index);
      if (test == null)
        return null;
      
      if(test==FlowEngine.NULL)
        return null;
           
      if(test instanceof BigDecimal)
        return (BigDecimal)test;
      
      String data = test.toString();
      data = StringUtils.replace(data,",",".");
      return new BigDecimal(data);
    }
    catch(Exception e)
    {
      // ignore
    }
    return null;
  } 

  protected final BigDecimal toSaveDecimal(List<Object> params, int index)
  {
    BigDecimal result = toDecimal(params,index);
    if(result==null)
      return new BigDecimal("0");
    return result;
  } 

  protected final String toSaveString(List<Object> params, int index)
  {
    try
    {
      Object test = params.get(index);
      if (test == null)
        return "";
      
      if(test==FlowEngine.NULL)
        return "";
      
      return test.toString();
    }
    catch(Exception e)
    {
      // ignore
    }
    return "";
  } 
  
  protected final boolean toBoolean(List<Object> params, int index)
  {
    try
    {
      Object test = params.get(index);
      
      if (test == null)
        return false;
      
      if(test==FlowEngine.NULL)
        return false;
      
      if(test instanceof Boolean)
        return (Boolean)test;
      
      if (test instanceof String)
      {
        test = StringUtil.replace(((String) test).toLowerCase(), "yes", "true");
        test = StringUtil.replace(((String) test).toLowerCase(), "no", "false");
        test = StringUtil.replace(((String) test).toLowerCase(), "1", "true");
        test = StringUtil.replace(((String) test).toLowerCase(), "0", "false");
        test = StringUtil.replace(((String) test).toLowerCase(), "ok", "true");
      }
      return Boolean.parseBoolean(test.toString());
    }
    catch (Exception e)
    {
    }
    return false;
  }
}
