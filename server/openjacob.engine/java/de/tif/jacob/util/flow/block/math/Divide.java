/*
 * Created on 03.11.2010
 *
 */
package de.tif.jacob.util.flow.block.math;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.util.flow.FlowEngine;
import de.tif.jacob.util.flow.block.AbstractBlock;
import de.tif.jacob.util.flow.castor.Block;

public class Divide extends AbstractBlock
{
  
  @Override
  public Object execute(FlowEngine engine, Context context, IDataTableRecord affectedRecord, Block currentBlock, List<Object> sparams, List<Object> dparams)
  {
    BigDecimal d1 = this.toDecimal(sparams, 0);
    BigDecimal d2 = this.toDecimal(sparams, 1);
    try
    {
      return d1.divide(d2, MathContext.DECIMAL128);
    }
    catch (Exception e)
    {
    }
    return FlowEngine.NULL;
  }
  
  public static void main(String[] args)
  {
    try
    {
      BigDecimal d1 = new BigDecimal("44.4").setScale(300);
      BigDecimal d2 = new BigDecimal("2").setScale(300);
      
     System.out.println( d1.divide(d2,BigDecimal.ROUND_UP));
    }
    catch (Exception e)
    {
   
      e.printStackTrace();
    }
    
    
  }
}
