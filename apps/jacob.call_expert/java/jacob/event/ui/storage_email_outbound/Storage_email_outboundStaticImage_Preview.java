/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jun 10 14:14:51 CEST 2009
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
public class Storage_email_outboundStaticImage_Preview extends IStaticImageEventHandler implements IOnClickEventHandler
{
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
   HTMLEditorHelper.previewBody(context);
  }
}

