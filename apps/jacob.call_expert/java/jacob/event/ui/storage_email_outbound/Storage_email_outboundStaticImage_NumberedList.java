/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jun 10 16:01:40 CEST 2009
 */
package jacob.event.ui.storage_email_outbound;


import de.tif.jacob.screen.*;
import de.tif.jacob.screen.event.*;

import jacob.common.AppLogger;
import jacob.common.htmleditor.HTMLEditorHelper;

import org.apache.commons.logging.Log;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 * 
 * @author achim
 */
public class Storage_email_outboundStaticImage_NumberedList extends IStaticImageEventHandler implements IOnClickEventHandler
{
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    HTMLEditorHelper.insertListTag(context,true);
  }
  /**
   * The event handler if the group status has been changed.<br>
   * 
   * @param context The current work context of the jACOB application. 
   * @param status  The new state of the group.
   * @param emitter The emitter of the event.
   */
  public void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState state, IStaticImage image) throws Exception
  {
    image.setEnable(state.equals(IGroup.NEW)||state.equals(IGroup.UPDATE));
  }
}

