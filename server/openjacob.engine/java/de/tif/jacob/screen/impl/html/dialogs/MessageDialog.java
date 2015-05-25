/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package de.tif.jacob.screen.impl.html.dialogs;

import org.apache.commons.lang.StringUtils;
import de.tif.jacob.core.Context;
import de.tif.jacob.scheduler.TaskContextUser;
import de.tif.jacob.screen.dialogs.IMessageDialogCallback;
import de.tif.jacob.screen.impl.dialogs.HTTPMessageDialog;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.ClientSession;
import de.tif.jacob.util.StringUtil;


/**
 * The HTML message dialog is mapped to a javascript alert box.<br>
 * <br>
 * The first implementation has mapped the MessageDialog to the FormDialog base class
 * which opens a new InternetExplorer. This solution has been cancel on performance reasons.<br>
 * 
 * @author Andreas Herz
 */
public class MessageDialog  extends HTTPMessageDialog
{
  protected String htmlMessage;
  protected String htmlExtendedMessage;
  
  public MessageDialog(ClientContext context, String msg, String extendedMessage, IMessageDialogCallback callback)
  {
    super(context,msg, callback);
    
    htmlMessage=filter(msg);
    htmlExtendedMessage = filter(extendedMessage);
  }

  public MessageDialog(String msg, IMessageDialogCallback callback)
  {
    super(msg, callback);
    
    htmlMessage=filter(msg);
    htmlExtendedMessage =null;
  }
  
  public void println(String message)
  {
    this.htmlMessage = this.htmlMessage+"\\n"+filter(message);
  }
  
  private static String filter(String text)
  {
    text = StringUtil.htmlEncode(text);
    text=StringUtils.replace(text,"\r\n","\n");
    text=StringUtils.replace(text,"\n","\\n");
    return StringUtils.replace(text,"\"","'");
  }



  public void show(int width, int height)
  {
    Context context  = Context.getCurrent();
    // the dialog call comes from a GuiCallback (user interaction)
    //
    if(context instanceof ClientContext)
    {  
      ClientContext cc=(ClientContext)context;
      String javascriptPopup;
      if(this.callback!=null)
      {
        ((ClientSession)cc.getApplication().getSession()).addDialog(this);
        javascriptPopup = "new jACOBAlert(\""+this.htmlMessage+"\",\""+htmlExtendedMessage+"\",\""+this.getId()+"\");";
      }
      else
      {
        javascriptPopup = "new jACOBAlert(\""+this.htmlMessage+"\",\""+htmlExtendedMessage+"\");";
      }
      cc.addOnLoadJavascript(javascriptPopup);
   }
    // the dialog comes from the schedule UserTask
    else if(context instanceof TaskContextUser)
    {  
      TaskContextUser tc =(TaskContextUser)context;
      String javascriptPopup;
      if(this.callback!=null)
      {
        ((ClientSession)tc.getSession()).addDialog(this);
        javascriptPopup = "new jACOBAlert(\""+this.htmlMessage+"\",\""+htmlExtendedMessage+"\",\""+this.getId()+"\");";
      }
      else
      {
        javascriptPopup = "new jACOBAlert(\""+this.htmlMessage+"\",\""+htmlExtendedMessage+"\");";
      }
      ((ClientSession)(tc).getSession()).addAsynchronJavaScript(javascriptPopup);
    }
  }
}
