/*
 * Created on 03.11.2010
 *
 */
package de.tif.jacob.util.flow.block.dialog;

import java.util.List;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IMessageDialogCallback;
import de.tif.jacob.util.flow.FlowEngine;
import de.tif.jacob.util.flow.block.AbstractBlock;
import de.tif.jacob.util.flow.castor.Block;

public class Alert extends AbstractBlock
{
  private static class Callback implements IMessageDialogCallback
  {
    private final FlowEngine engine;
    private Callback(FlowEngine engine)
    {
      this.engine = engine;
    }

    @Override
    public void onClose(IClientContext context) throws Exception
    {
      this.engine.execute(context,context.getSelectedRecord());
    }
  }
  
  @Override
  public Object execute(FlowEngine engine, Context context, IDataTableRecord affectedRecord,Block currentBlock, List<Object> sparams, List<Object> dparams)
  {  
   String msg = toSaveString(sparams,0);
 
    if(context instanceof IClientContext)
    {
       ((IClientContext )context).createMessageDialog(msg, new Callback(engine)).show();
       return FlowEngine.REENTRY;
    }
    System.out.println("Alert:"+msg);
    return FlowEngine.NULL;
  }
  

  @Override
  public Object reentry(FlowEngine engine, Context context, IDataTableRecord affectedRecord, Block currentBlock)
  {
    return FlowEngine.NULL;
  }
}
