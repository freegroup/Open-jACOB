/*
 * Created on Jul 4, 2004
 *
 */
package jacob.event.screen.f_administration.wgroupmember;

import java.util.HashMap;
import java.util.Map;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IComboBoxEventHandler;
import de.tif.jacob.util.StringUtil;

/**
 * Vorblenden der Notifikationsmethode der Workgroup falls der Anwender
 * in der ComboBox eine andere Benachrichtingungsart auswählt.<br>
 * 
 * @author Andreas Herz
 */
public class WorkgroupNotifymethod extends IComboBoxEventHandler
{
  private static Map notifyAddressFields= new HashMap();
  static
  {
    notifyAddressFields.put("Bearbeiter","");
    notifyAddressFields.put("Signal"    ,"workgroupEmail");
    notifyAddressFields.put("Email"     ,"workgroupEmail");
    notifyAddressFields.put("FAX"       ,"workgroupFax");
    notifyAddressFields.put("Funkruf"   ,"workgroupTelefon");
    notifyAddressFields.put("Drucker"   ,"");
    notifyAddressFields.put("Keine"     ,"");
  }
  
  /* 
   * @see de.tif.jacob.screen.event.IComboBoxEventHandler#onSelect(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IComboBox)
   */
  public void onSelect(IClientContext context, IComboBox checkBox) throws Exception
  {
    String notifyField=(String)notifyAddressFields.get(checkBox.getValue());
    if(notifyField==null || notifyField.length()==0)
    {
      context.getGroup().setInputFieldValue("workgroupNotificationaddr","");
    }
    else
    {
      String value=StringUtil.toSaveString(context.getGroup().getInputFieldValue(notifyField));
      
      context.getGroup().setInputFieldValue("workgroupNotificationaddr",value);
    }
  }

  /* 
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState status, IGuiElement emitter)  throws Exception
  {
  }

}
