/*
 * Created on 03.11.2010
 *
 */
package de.tif.jacob.util.flow.block.text;

import java.util.List;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.util.flow.FlowEngine;
import de.tif.jacob.util.flow.block.AbstractBlock;
import de.tif.jacob.util.flow.castor.Block;

public class Join extends AbstractBlock
{

  @Override
  public Object execute(FlowEngine engine, Context context, IDataTableRecord affectedRecord, Block currentBlock,List<Object> sparams, List<Object> dparams)
  {
    StringBuffer sb = new StringBuffer(1024);
    for (Object object : dparams)
    {
      if(object != FlowEngine.NULL)
        sb.append(object.toString());
    }
    return sb.toString();
  }
}

