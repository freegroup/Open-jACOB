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

public class Sqrt extends AbstractBlock
{
  
  @Override
  public Object execute(FlowEngine engine, Context context, IDataTableRecord affectedRecord, Block currentBlock, List<Object> sparams, List<Object> dparams)
  {
    if(sparams.size()==0)
      return FlowEngine.NULL;

    BigDecimal d1 = this.toDecimal(sparams, 0);

    try
    {
      return new BigDecimal( Math.sqrt(d1.doubleValue()));
    }
    catch (Exception e)
    {
    }
    return FlowEngine.NULL;
  }
  
}
