/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jun 18 13:45:44 CEST 2009
 */
package jacob.event.ui.storage_email_outbound;

import de.tif.jacob.screen.*;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;

import jacob.common.AppLogger;
import jacob.common.htmleditor.HTMLEditorHelper;

import org.apache.commons.logging.Log;


/**
 *
 * @author achim
 */
 public class Storage_email_outboundGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: Storage_email_outboundGroup.java,v 1.2 2009/07/01 11:48:24 A.Herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * @param context The current client context
   * @param status  The new status of the group.
	 * @param emitter The corresponding GUI element of this event handler
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGroup group) throws Exception
	{
	  //storage_email_outboundStyledText_Preview
	  if (status.equals(IGroup.SEARCH))
    {
      context.getGroup().findByName("storage_email_outboundStyledText_Preview").setLabel("Preview Area");
    }
	  if (status.equals(IGroup.SELECTED))
    {
	    HTMLEditorHelper.previewBody(context);
    }
	}

  /**
   * Will be called if the will be change the state from visible=>hidden.
   * 
   * This happends if the user switch the Domain or Form which contains this group.
   * 
   * @param context The current client context
   * @param group   The corresponding group for this event
   */
  public void onHide(IClientContext context, IGroup group) throws Exception 
  {
    // insert your code here
  }
  
  /**
   * Will be called if the will be change the state from hidden=>visible.
   * 
   * This happends if the user switch to a Form which contains this group.
   * 
   * @param context The current client context
   * @param group   The corresponding group for this event
   */
  public void onShow(IClientContext context, IGroup group) throws Exception 
  {
    // insert your code here
  }
}
