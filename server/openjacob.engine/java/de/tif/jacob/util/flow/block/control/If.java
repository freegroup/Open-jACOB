/*
 * Created on 03.11.2010
 *
 */
package de.tif.jacob.util.flow.block.control;

import java.util.List;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.util.flow.FlowEngine;
import de.tif.jacob.util.flow.block.AbstractBlock;
import de.tif.jacob.util.flow.castor.Block;

public class If extends AbstractBlock
{

  @Override
  public Object execute(FlowEngine engine, Context context, IDataTableRecord affectedRecord,Block currentBlock, List<Object> sparams, List<Object> dparams)
  {
    Block trueBlock = currentBlock.getInnerblocks().getBlock(0);   
    Block falseBlock = currentBlock.getInnerblocks().getBlock(1);
    
    if(toBoolean(sparams,0))
      engine.nextInnerBlock.put(currentBlock,trueBlock);
    else
      engine.nextInnerBlock.put(currentBlock,falseBlock);
    
    return FlowEngine.NULL;
  }
}
