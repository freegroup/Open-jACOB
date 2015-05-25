/*
 * Created on 11.11.2010
 *
 */
package de.tif.jacob.util.flow.block.record;

import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.util.flow.FlowEngine;
import de.tif.jacob.util.flow.block.AbstractBlock;
import de.tif.jacob.util.flow.castor.Block;

public class UseFieldFrom extends AbstractBlock
{
  @Override
  public Object execute(FlowEngine engine, Context context, IDataTableRecord affectedRecord, Block currentBlock,List<Object> sparams, List<Object> dparams)
  {
    if(sparams.size()==0)
      return FlowEngine.NULL;
    
    Object param = sparams.get(0);
    if(param==null)
      return FlowEngine.NULL;
    
    String tableName = null;
    // Eventuell hat man ja schon einen Record in den Block reingesteckt.
    //
    if(param instanceof IDataTableRecord)
    {
      affectedRecord = (IDataTableRecord)param;
      tableName = affectedRecord.getTable().getName();
    }
    // versuchen den Record von der genannten TableAlias zu laden
    //
    else if(param instanceof String)
    {
      tableName = (String)param;
      try
      {
        IDataTable table = context.getDataTable(tableName);
        if(table.getSelectedRecord()!=null)
        {
            affectedRecord = table.getSelectedRecord(); 
        }
        else
        {
            if(affectedRecord == null)
              return FlowEngine.NULL;
            
            affectedRecord = affectedRecord.getLinkedRecord(tableName);
            if(affectedRecord == null)
              return FlowEngine.NULL;
        }
      }
      catch(Exception e)
      {
        return FlowEngine.NULL;
      }
    }
    
    try
    {
       String field = currentBlock.getProperty();
       // nicht im UpdateMode. Man kann den Wert direkt aus dem Record holen
       if(!affectedRecord.isNewOrUpdated())
       {
         return affectedRecord.getSaveStringValue(field,context.getLocale());
       }
       else if(context instanceof IClientContext)
       {
         IClientContext cc = (IClientContext)context;
         List elements = cc.getForm().getGuiRepresentations(tableName, field);
         Iterator elementIter = elements.iterator();
         while (elementIter.hasNext())
         {
           ISingleDataGuiElement element = (ISingleDataGuiElement) elementIter.next();
           return element.getValue();
         }
       }
       // keine GUI element gefunden welches dieses Feld enthält => doch aus dem Record holen
       //
       return affectedRecord.getSaveStringValue(field,context.getLocale());
    }
    catch(Exception e)
    {
      
    }
    return FlowEngine.NULL;
  }
}