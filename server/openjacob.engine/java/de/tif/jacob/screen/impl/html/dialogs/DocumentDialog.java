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
import de.tif.jacob.screen.impl.dialogs.HTTPDocumentDialog;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.ClientSession;

/**
 * @author Administrator
 *
 */
public class DocumentDialog  extends HTTPDocumentDialog
{
  private boolean enforcePrinting=false;
  
  public DocumentDialog(ClientContext context, String mimeType, String filename, byte[] document)
  {
    super(context, mimeType,filename,document);
  }

  /**
   * Shows an HTML information dialog on the client. 
   * No navigation elements available (Toolbar, Addressbar,....)
   * 
   * @param title    The title of the dialog
   * @param question The question of the dialog.
   */
  public void show(int width, int height)
  {
    ClientContext cc = (ClientContext) Context.getCurrent();
    if (isEnforcePrinting() && "ie".equals(((ClientSession) cc.getSession()).getClientBrowserType()))
    {
      // iframe functionality seems not to work in IE when printing is enforced, i.e.
      // no reaction -> no print window opened
      cc.addAdditionalHTML("<script type=\"text/javascript\">\n");
      cc.addAdditionalHTML("\tnew jACOBPopup('dialogs/DocumentDialog.jsp?browser=" + cc.clientBrowser + "&guid=" + getId() + "'," + width + "," + height + ");\n");
      cc.addAdditionalHTML("</script>\n");
    }
    else
    {
      cc.addAdditionalHTML("<iframe id=\"file_download\" width=\"0\" height=\"0\" scrolling=\"no\" frameborder=\"0\" src=\"\"");
      cc.addAdditionalHTML("></iframe>\n");
      cc.addOnLoadJavascript("$('file_download').src='./" + generateUrl() + "';\n");
    }
    ((ClientSession) cc.getApplication().getSession()).addDialog(this);
  }

  public void enforcePrinting(boolean flag)
  {
    enforcePrinting=flag;
  }

  public boolean isEnforcePrinting()
  {
    return enforcePrinting;
  }

  public String generateUrl()
  {
    ClientContext context = (ClientContext)Context.getCurrent();
    return "dialogs/DocumentDialog.jsp?browser="+context.clientBrowser+"&guid="+getId();
  }
}
