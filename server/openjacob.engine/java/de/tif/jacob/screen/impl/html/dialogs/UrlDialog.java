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
import de.tif.jacob.core.Context;
import de.tif.jacob.scheduler.TaskContextUser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IUrlDialog;
import de.tif.jacob.screen.impl.dialogs.HTTPGenericDialog;
import de.tif.jacob.screen.impl.html.Application;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.ClientSession;
import de.tif.jacob.screen.impl.html.Domain;
import de.tif.jacob.screen.impl.html.Group;
/**
 * 
 * @author mike
 *
 */
/**
 * @author Administrator
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class UrlDialog extends HTTPGenericDialog implements IUrlDialog
{
  private String url = "";
  private boolean navigation = false;
  private boolean modal = false;
  private boolean scrollbar = false;
  private boolean useMainWindow = false;

  public UrlDialog(ClientContext context, String url)
  {
    super((Domain) context.getDomain(), (Group) context.getGroup());
    // Falls es sich um eine relative URL handelt wird diese mit der browserId
    // angereichert
    //
    if (!url.startsWith("http"))
    {
      if (url.indexOf("?") == -1)
        this.url = url + "?browser=" + context.clientBrowser;
      else
        this.url = url + "&browser=" + context.clientBrowser;
    }
    else
      this.url = url;
  }

  public UrlDialog(String url)
  {
    super(null, null);
    this.url = url;
  }

  public void useMainWindow(boolean flag)
  {
    this.useMainWindow = flag;
  }

  public void enableScrollbar(boolean scrollbar)
  {
    this.scrollbar = scrollbar;
  }

  public void enableNavigation(boolean flag)
  {
    navigation = flag;
  }

  public void setModal(boolean flag)
  {
    modal = flag;
  }

  public void show()
  {
    show(300, 200);
  }

  /**
   * Shows an HTML information dialog on the client. No navigation elements
   * available (Toolbar, Addressbar,....)
   * 
   * @param title
   *          The title of the dialog
   * @param question
   *          The question of the dialog.
   */
  public void show(int width, int height)
  {
    Context context = Context.getCurrent();
    // the dialog call comes from a GuiCallback (user interaction)
    //
    if (context instanceof ClientContext)
    {
      ClientContext cc = (ClientContext) context;
      String htmlCode = "";
      if(useMainWindow)
      {
        htmlCode = "<script type=\"text/javascript\">parent.window.location.href='" + url + "';</script>\n";
      }
      else if (modal)
      {
        htmlCode = "<script type=\"text/javascript\">popupModal('" + url + "'," + width + "," + height + "," + scrollbar + ");</script>\n";
      }
      else if (navigation == false)
      {
        htmlCode = "<script type=\"text/javascript\">new jACOBPopup('" + url + "'," + width + "," + height + "," + scrollbar + ");</script>\n";
      }
      else
      {
        htmlCode = "<script type=\"text/javascript\">new jACOBWindow('" + url + "'," + width + "," + height + ", " + scrollbar + ");</script>\n";
      }
      cc.addAdditionalHTML(htmlCode);
    }
    // the dialog call comes from a scheduled script
    //
    else if (context instanceof TaskContextUser)
    {
      TaskContextUser tc = (TaskContextUser) context;
      String htmlCode = "";
      if (modal)
        htmlCode = "popupModal('" + url + "'," + width + "," + height + ");\n";
      else if (navigation == false)
        htmlCode = "new jACOBPopup('" + url + "'," + width + "," + height + ");\n";
      else
        htmlCode = "new jACOBWindow('" + url + "'," + width + "," + height + ",true);\n";
      ((ClientSession) (tc).getSession()).addAsynchronJavaScript(htmlCode);
    }
  }

  /*
   * @see
   * de.tif.jacob.screen.dialogs.ICallbackDialog#processEvent(de.tif.jacob.screen
   * .IClientContext, int, java.lang.String, java.lang.String)
   */
  public void processEvent(IClientContext context, String event, String value) throws Exception
  {
  }
}
