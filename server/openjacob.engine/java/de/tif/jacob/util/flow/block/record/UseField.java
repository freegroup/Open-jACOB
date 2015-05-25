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
public class UseField extends AbstractBlock
{
  @Override
  public Object execute(FlowEngine engine, Context context, IDataTableRecord affectedRecord, Block currentBlock, List<Object> sparams, List<Object> dparams)
  {
    String field = currentBlock.getProperty();
    if (affectedRecord != null)
    {
      String table = affectedRecord.getTableAlias().getName();
      try
      {
        // nicht im UpdateMode. Man kann den Wert direkt aus dem Record holen
        //
        if (affectedRecord.isNewOrUpdated() == false)
        {
          return affectedRecord.getSaveStringValue(field, context.getLocale());
        }
        // Versuchen den Wert aus der GUI auszulesen. Wenn ein Feld hierfür
        // vorhanden ist.
        //
        if (context instanceof IClientContext)
        {
          IClientContext cc = (IClientContext) context;
          List elements = cc.getForm().getGuiRepresentations(table, field);
          Iterator elementIter = elements.iterator();
          while (elementIter.hasNext())
          {
            ISingleDataGuiElement element = (ISingleDataGuiElement) elementIter.next();
            return element.getValue();
          }
        }
        // keine GUI element gefunden welches dieses Feld enthält => doch aus
        // dem Record holen egal ob Update oder nicht.
        return affectedRecord.getSaveStringValue(field, context.getLocale());
      }
      catch (Exception e)
      {
      }
    }
    
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
        return input.getValue();
      }
    }
    
    // Weder im Record noch in der GUI gefunden
    //
   return FlowEngine.NULL;
  }
}