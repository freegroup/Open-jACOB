/*
 * Created on 03.11.2010
 *
 */
package de.tif.jacob.util.flow.block.dialog;
import java.util.List;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.util.flow.FlowEngine;
import de.tif.jacob.util.flow.block.AbstractBlock;
import de.tif.jacob.util.flow.castor.Block;
public class Prompt extends AbstractBlock
{
  private static class Callback implements IAskDialogCallback
  {
    private final FlowEngine engine;
    private Callback(FlowEngine engine)
    {
      this.engine = engine;
    }
    @Override
    public void onOk(IClientContext context, String value) throws Exception
    {
      this.engine.param = value;
      this.engine.execute(context,context.getSelectedRecord());
    }

    @Override
    public void onCancel(IClientContext context) throws Exception
    {
      this.engine.param = "EMPTY";
      this.engine.execute(context,context.getSelectedRecord());
    }
  }
 
  @Override
  public Object execute(FlowEngine engine, Context context, IDataTableRecord affectedRecord, Block currentBlock, List<Object> sparams, List<Object> dparams)
  {
    Object param = FlowEngine.NULL;
    
    if (sparams.size() == 1)
      param = sparams.get(0);
    
    if (context instanceof IClientContext)
    {
      if (param == FlowEngine.NULL)
        param = "Question";
      
      ((IClientContext) context).createAskDialog(param.toString(), new Callback(engine)).show();
      return FlowEngine.REENTRY;
    }
    else
    {
      return "No User Input";
    }
  }

  @Override
  public Object reentry(FlowEngine engine, Context context, IDataTableRecord affectedRecord, Block currentBlock)
  {
    return engine.param;
  }
  
}
