/*
 * Created on Feb 23, 2004
 *
 */
package jacob.event.screen.f_call_entry.orgcustomerInt;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IComboBoxEventHandler;

/**
 * @author Andreas Herz
 *
 */
public final class CustomerintEmployeetype extends IComboBoxEventHandler
{
  static public final transient String RCS_ID = "$Id: CustomerintEmployeetype.java,v 1.5 2004/07/14 07:39:49 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";
  /* 
	 * @see de.tif.jacob.screen.event.IComboBoxEventHandler#onSelect(de.tif.jacob.screen.Focus, de.tif.jacob.screen.Form, de.tif.jacob.screen.Group, de.tif.jacob.screen.ComboBox)
	 */
	public void onSelect(IClientContext context, IComboBox comboBox) throws Exception
	{
	  boolean enabled = !comboBox.getValue().equals("Extern");

	  IGuiElement element = context.getForm().findByName("customerDepartment");
    element.setEnable(enabled);

    element = context.getForm().findByName("customerHpccorr");
    element.setEnable(enabled);
    
    element = context.getForm().findByName("customerFaxcorr");
    element.setEnable(enabled);
    
    element = context.getForm().findByName("customerAccountingcorr");
    element.setEnable(enabled);
    
    element = context.getForm().findByName("customerintSitepartcorr");
    element.setEnable(enabled);
  }
	
  /* 
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged( IClientContext arg0,  IGuiElement.GroupState arg1, IGuiElement arg2)    throws Exception
  {
  }

}
