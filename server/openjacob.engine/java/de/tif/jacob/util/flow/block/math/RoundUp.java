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

public class RoundUp extends AbstractBlock
{
  
  @Override
  public Object execute(FlowEngine engine, Context context, IDataTableRecord affectedRecord, Block currentBlock, List<Object> sparams, List<Object> dparams)
  {
    if(sparams.size()==0)
      return FlowEngine.NULL;

    BigDecimal d1 = this.toDecimal(sparams, 0);
    BigDecimal d2 = this.toDecimal(sparams, 1);

    try
    {
      return d1.setScale(d2.intValue(), BigDecimal.ROUND_HALF_UP);
    }
    catch (Exception e)
    {
    }
    return FlowEngine.NULL;
  }
  
}
