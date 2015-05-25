/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Nov 18 17:23:38 CET 2008
 */
package jacob.event.ui.request;

import jacob.common.masterdetail.MasterDetailManager;
import jacob.common.masterdetail.MasterDetailManager_CompanyContact;
import jacob.common.masterdetail.MasterDetailManager_CustomerContact;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;


/**
 *
 * @author achim
 */
 public class RequestHeaderGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: RequestHeaderGroup.java,v 1.6 2009/10/08 10:32:03 A.Boeken Exp $";
	static public final transient String RCS_REV = "$Revision: 1.6 $";
	/**
	 * @param context The current client context
   * @param status  The new status of the group.
	 * @param emitter The corresponding GUI element of this event handler
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGroup group) throws Exception
	{
	  // Some Button sin the CustomerContactDetail pane will be changed if you select a different
	  // request record.
    MasterDetailManager.refresh(MasterDetailManager_CustomerContact.INSTANCE, context);
    MasterDetailManager.refresh(MasterDetailManager_CompanyContact.INSTANCE, context);
	}

  @Override
  public Class getSearchBrowserEventHandlerClass()
  {
    return RequestBrowser.class;
  }
}
