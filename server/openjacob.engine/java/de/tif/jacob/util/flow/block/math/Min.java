/*
 * Created on 03.11.2010
 *
 */
package de.tif.jacob.util.flow.block.math;
import java.math.BigDecimal;
import java.util.List;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.util.flow.FlowEngine;
import de.tif.jacob.util.flow.block.AbstractBlock;
import de.tif.jacob.util.flow.castor.Block;

public class Min extends AbstractBlock
{
  
  @Override
  public Object execute(FlowEngine engine, Context context, IDataTableRecord affectedRecord, Block currentBlock, List<Object> sparams, List<Object> dparams)
  {
    if(dparams.size()==0)
      return FlowEngine.NULL;
    
    BigDecimal result = new BigDecimal(Double.MAX_VALUE);
    
    for(int i=0; i< dparams.size();i++)
    {
      BigDecimal value = this.toDecimal(dparams,i);
      if(value!=null)
        result = result.min(value);
    }
    return result;
  }
}
