/*
 * Created on Jul 4, 2004
 *
 */
package jacob.event.ui.callaction;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.event.IComboBoxEventHandler;

/**
 * Falls die Gruppe in den Updatemodus (DATASTATUS_UPDATE) umschaltet darf die ComboBox<br>
 * nur editierbar sein wenn man den Empfänger von Hand einträgt.<br>
 * <br>
 * Die ComboBox wird ebenfalls durch die ComboBox 'Recipient' gesteuert. 
 * 
 *
 * @author Andreas Herz
 */
public class ActionMethod extends IComboBoxEventHandler
{
  /* 
   * @see de.tif.jacob.screen.event.IComboBoxEventHandler#onSelect(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IComboBox)
   */
  public void onSelect(IClientContext context, IComboBox combobox)  throws Exception
  {
  }

  /**
   * 
   */
  public void onGroupStatusChanged( IClientContext context,  IGuiElement.GroupState status,  IGuiElement emitter)  throws Exception
  {
    if(status==IGuiElement.UPDATE)
    {  
      String recipient =context.getGroup().getInputFieldValue("actionRecipient");
      ((ISingleDataGuiElement)emitter).setEnable(recipient.equals("folgende Adresse")||recipient.equals("Kunde"));
    }
    
  }
}

