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
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.impl.dialogs.HTTPAskDialog;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.ClientSession;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AskDialog extends HTTPAskDialog
{
  String message="";
  String defaultValue="";
  
  public AskDialog(ClientContext context,String message, String defaultValue, IAskDialogCallback callback, boolean multiline)
  {
    super(context,callback, multiline);
    
    this.message      = filter(message);
    this.defaultValue = StringUtil.toSaveString(defaultValue);
  }

  public void show(int width, int height)
  {
    Context context  = Context.getCurrent();
    String javascriptPopup = "new jACOBPrompt(\""+message+"\",\""+defaultValue+"\",'"+getId()+"',"+this.multiline+");";

    // the dialog call comes from a GuiCallback (user interaction)
    //
    if(context instanceof ClientContext)
    {  
      ClientContext cc=(ClientContext)context;
      ((ClientSession)cc.getApplication().getSession()).addDialog(this);
      cc.addOnLoadJavascript(javascriptPopup);
    }
    // A schedules user task has requested a dialog
    //
    else if(context instanceof TaskContextUser)
    {  
      TaskContextUser tc =(TaskContextUser)context;
      ((ClientSession)tc.getSession()).addDialog(this);
      ((ClientSession)tc.getSession()).addAsynchronJavaScript(javascriptPopup);
    }
  }
  
  private static String filter(String text)
  {
    text = StringUtil.htmlEncode(text);
    text=StringUtils.replace(text,"\r\n","\n");
    text=StringUtils.replace(text,"\n","<br>");
    return StringUtils.replace(text,"\"","'");
  }
}
