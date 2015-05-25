/*
 * Created on Jul 4, 2004
 *
 */
package jacob.event.ui.callaction;

import java.util.HashSet;
import java.util.Set;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.event.IComboBoxEventHandler;

/**
 *
 */
public class ActionRecipient extends IComboBoxEventHandler
{
  static private Set emptyElements=new HashSet();
  
  static
  {
    emptyElements.add("Mitarbeiter");
    emptyElements.add("Arbeitsgruppe");
    emptyElements.add("NM Meisterei");
    emptyElements.add("AK des Auftrags");
  }
  
  /* 
   * @see de.tif.jacob.screen.event.IComboBoxEventHandler#onSelect(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IComboBox)
   */
  public void onSelect(IClientContext context, IComboBox combobox)  throws Exception
  {
    ISingleDataGuiElement element=(ISingleDataGuiElement)context.getGroup().findByName("actionMethod");
    if(combobox.getValue().equals("CC Liste"))
    {
      element.setValue("Email");
      element.setEnable(false);
    }
    else if(emptyElements.contains(combobox.getValue()))
    {
      element.setEnable(false);
      element.setValue("");
    }
    else
    {  
      element.setEnable(true);
    }
  }

  
  /* 
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged( IClientContext context,  IGuiElement.GroupState status,  IGuiElement emitter)  throws Exception
  {
  }
}
