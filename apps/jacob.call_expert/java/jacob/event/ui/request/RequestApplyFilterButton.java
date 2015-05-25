/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Nov 14 14:36:53 CET 2008
 */
package jacob.event.ui.request;

import jacob.common.BreadCrumbController_RequestCategory;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the RequestGenericButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class RequestApplyFilterButton extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: RequestApplyFilterButton.java,v 1.1 2009/02/17 15:12:15 A.Boeken Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";


  public void onClick(IClientContext context, IGuiElement emitter) throws Exception
  {
    String filterText = context.getGroup().getInputFieldValue("requestCategoryFilterText");
    BreadCrumbController_RequestCategory.onSearch(context, filterText);
  }

}
