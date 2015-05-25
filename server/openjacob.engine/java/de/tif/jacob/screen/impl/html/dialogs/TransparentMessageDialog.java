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
import de.tif.jacob.screen.impl.dialogs.HTTPMessageDialog;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.ClientSession;


/**
 * The HTML message dialog is mapped to a javascript alert box.<br>
 * <br>
 * The first implementation has mapped the MessageDialog to the FormDialog base class
 * which opens a new InternetExplorer. This solution has been cancel on performance reasons.<br>
 * 
 * @author Andreas Herz
 */
public class TransparentMessageDialog  extends HTTPMessageDialog
{
  final boolean fadeOut;
  public TransparentMessageDialog(ClientContext context, String msg)
  {
    this(context, msg, true);
  }

  public TransparentMessageDialog(ClientContext context, String msg, boolean fadeOut)
  {
    super(context,msg, null);
    
    message=StringUtils.replace(msg,"\r\n","<br>");
    message=StringUtils.replace(message,"\n","<br>");
    message=StringUtils.replace(message,"\"","'");
    
    this.fadeOut = fadeOut;
  }

  public TransparentMessageDialog(String msg)
  {
    super(msg, null);
    this.fadeOut=true;
    message=StringUtils.replace(msg,"\r\n","<br>");
    message=StringUtils.replace(message,"\n","<br>");
    message=StringUtils.replace(message,"\"","'");
  }

  public void println(String message)
  {
    this.message = this.message+"<br>"+message;
  }
  

  public void show(int width, int height)
  {
    Context context  = Context.getCurrent();
    // the dialog call comes from a GuiCallback (user interaction)
    //
    if(context instanceof ClientContext)
    {  
      ClientContext cc=(ClientContext)context;
      String javascriptPopup = "new jACOBTransparentMessage(\""+this.message+"\","+this.fadeOut+");";
      cc.addOnLoadJavascript(javascriptPopup);
    }
    else if(context instanceof TaskContextUser)
    {  
      TaskContextUser tc =(TaskContextUser)context;
      String javascriptPopup = "new jACOBTransparentMessage(\""+message+"\","+this.fadeOut+");";
      ((ClientSession)(tc).getSession()).addAsynchronJavaScript(javascriptPopup);
    }
  }
}
