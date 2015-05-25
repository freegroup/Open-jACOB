/*
 * Created on 11.11.2010
 *
 */
package de.tif.jacob.util.flow.block.record;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.util.flow.FlowEngine;
import de.tif.jacob.util.flow.block.AbstractBlock;
import de.tif.jacob.util.flow.castor.Block;
public class SetField extends AbstractBlock
{
  @Override
  public Object execute(FlowEngine engine, Context context, IDataTableRecord affectedRecord, Block currentBlock, List<Object> sparams, List<Object> dparams)
  {
    String field = currentBlock.getProperty();
    String value = toSaveString(sparams, 0);
    
    // Es war kein Record vorhanden und/oder das Feld ist nicht Bestandteil vom Record. In diesem Fall wird
    // Versucht ein GUI Element zu finden welches so heist.
    //
    if (context instanceof IClientContext)
    {
      IClientContext cc = (IClientContext) context;
    
      IGuiElement element = cc.getGroup().findByName(field);
      if(element instanceof ISingleDataGuiElement)
      {
        ISingleDataGuiElement input = (ISingleDataGuiElement)element;
        input.setValue(value);
        // Es wurde ein GUI Element mit dem Namen gefunden. Jetzt dass zugehörige 
        // Datenbankfeld holen (falls vorhanden). Ist notwendig, da auch der TableRecord aktualisiert werden
        // sollte. Grund: onSuccess bei einem NewButton wird sonst die GUI mit dem Record überschrieben
        // und man kann im Workflow keine Default-Werte setzen.
        if(input.getTableField()!=null)
          field = input.getTableField().getName();
      }
    }

    if (affectedRecord != null)
    {
      try
      {
        String table = affectedRecord.getTableAlias().getName();
      
        // Der Record wurde vom Anwender in den UpdateModus geschaltet und eventuell in der GUI sichtbar
        // => GUI und Record in synch. halten
        if (engine.autoCommit == false && context instanceof IClientContext)
        {
          IClientContext cc = (IClientContext) context;
          List elements = cc.getForm().getGuiRepresentations(table, field);
          Iterator elementIter = elements.iterator();
          while (elementIter.hasNext())
          {
            ISingleDataGuiElement element = (ISingleDataGuiElement) elementIter.next();
            element.setValue(value);
          }
        }
        
        // Wenn der Wert in die GUI übertragen wurde, dann kann man es sich sparen den zusätzlich 
        // in den Record einzutragen. Eventuell ist es sogar ein ungültiger Wert. Dann würde hier
        // eine Exception fliegen. Hier kann eine Exce3ption fliegen....ist aber in dem Fall egal.
        affectedRecord.setValue(engine.currentTransaction, field, value);
        
        return FlowEngine.NULL;
      }
      catch (Exception e)
      {
      }
    }
    
    return FlowEngine.NULL;
  }
}