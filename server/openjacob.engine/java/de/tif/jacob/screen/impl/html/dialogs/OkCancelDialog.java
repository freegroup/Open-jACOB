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
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.impl.dialogs.HTTPOkCancelDialog;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.ClientSession;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class OkCancelDialog  extends HTTPOkCancelDialog
{
  public OkCancelDialog(ClientContext context, String msg, IOkCancelDialogCallback callback)
  {
    super(context, msg,callback);
    
    message=filter(msg);
  }

  public void show(int width, int height)
  {
    Context context  = Context.getCurrent();
    // the dialog call comes from a GuiCallback (user interaction)
    //
    String javascriptPopup = "new jACOBConfirm(\""+message+"\",'"+getId()+"');";
    
    // Es ist bewusst erlaubt, dass nur einer von beiden Labels gesetzt werden muﬂ
    if(this.cancelLabel!=null || this.okLabel!=null)
      javascriptPopup = "new jACOBConfirm(\""+message+"\",'"+getId()+"',"+enclose(this.okLabel)+","+enclose(this.cancelLabel)+");";
    
    if(context instanceof ClientContext)
    {  
      ClientContext cc=(ClientContext)context;
      ((ClientSession)((ClientContext)context).getApplication().getSession()).addDialog(this);
      cc.addOnLoadJavascript(javascriptPopup);
    }
    else if(context instanceof TaskContextUser)
    {  
      TaskContextUser tc =(TaskContextUser)context;
      ((ClientSession)(tc).getSession()).addDialog(this);
      ((ClientSession)(tc).getSession()).addAsynchronJavaScript(javascriptPopup);
    }
  }

  private static String filter(String text)
  {
    text = StringUtil.htmlEncode(text);
    text=StringUtils.replace(text,"\r\n","\n");
    text=StringUtils.replace(text,"\n","<br>");
    return StringUtils.replace(text,"\"","'");
  }
  
  private static String enclose(String s)
  {
    if(s==null)
      return "null";
    return "'"+s+"'";
  }
}
